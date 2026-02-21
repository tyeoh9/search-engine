/**
 * Executes queries against the inverted index and ranks documents based on token matches and frequencies
 *
 * Method:
 * 1. Accumulate scores for each document given the tokenized query
 * 2. Use a hashmap to keep track of docScores (e.g. docId -> score);
 *    'score' refers to the BM25 score
 * 3. Sort documents based on score
 * 4. Return top k matching documents
 *
 */

package search.query;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import search.index.InvertedIndex;
import search.query.QueryParser;

import javax.management.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    // Maps docId -> score
    private final HashMap<Integer, Double> docScores;
    private final InvertedIndex index;
    private final EmbeddingModel model;

    public Searcher(InvertedIndex index) {
        this.docScores = new HashMap<>();
        this.index = index;
        this.model = new AllMiniLmL6V2QuantizedEmbeddingModel();
    }

    // Increase score for docId by additionalScore
    public void increaseScore(int docId, double additionalScore) {
        this.docScores.merge(docId, additionalScore, Double::sum);
    }

    // Score document using BM25
    public void scoreDocsBM25(List<String> tokenizedQuery) {
        int totalDocs = index.getDocumentCount();
        double avgdl = index.getAverageDocLength();

        // BM25 Hyperparameters
        final double k1 = 1.2;
        final double b = 0.75;

        for (String token : tokenizedQuery) {
            HashMap<Integer, Integer> postings = this.index.getPostings(token);
            int df = postings.size();

            if (df == 0) continue;

            double idf = Math.log(1 + (totalDocs - df + 0.5) / (df + 0.5));

            postings.forEach((docId, termFreq) -> {
                int docLen = index.getDocLength(docId);

                double tfLowerPart = termFreq + k1 * (1 - b + b * (docLen / avgdl));
                double bm25Tf = (termFreq * (k1 + 1)) / tfLowerPart;

                double finalScore = idf * bm25Tf;
                this.increaseScore(docId, finalScore);
            });
        }
    }

    public void scoreDocsSemantic(String rawQuery) {
        float[] queryVector = this.model.embed(rawQuery).content().vector();

        index.getAllVectors().forEach((docId, docVector) -> {
            double similarity = cosineSimilarity(queryVector, docVector);
            // We boost semantic scores to align them with BM25 ranges if needed
            this.increaseScore(docId, similarity);
        });
    }

    private double cosineSimilarity(float[] v1, float[] v2) {
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            normA += v1[i] * v1[i];
            normB += v2[i] * v2[i];
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public List<Map.Entry<Integer, Double>> searchHybrid(String userQuery, int topK) {
        QueryParser parser = new QueryParser();

        // 1. Get Ranked BM25 Results
        List<String> tokens = parser.parse(userQuery);
        this.docScores.clear();
        this.scoreDocsBM25(tokens);
        List<Integer> bm25Ranked = getTopKIds(100);

        // 2. Get Ranked Semantic Results
        this.docScores.clear();
        this.scoreDocsSemantic(userQuery);
        List<Integer> vectorRanked = getTopKIds(100);

        // 3. Fuse them using RRF
        Map<Integer, Double> hybridScores = new HashMap<>();
        int k = 60; // Standard constant

        fuse(hybridScores, bm25Ranked, k);
        fuse(hybridScores, vectorRanked, k);

        // 4. Sort and return
        return hybridScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .toList();
    }

    private void fuse(Map<Integer, Double> hybridScores, List<Integer> rankedIds, int k) {
        for (int i = 0; i < rankedIds.size(); i++) {
            int docId = rankedIds.get(i);
            int rank = i + 1; // Ranks start at 1
            double score = 1.0 / (k + rank);
            hybridScores.merge(docId, score, Double::sum);
        }
    }

    // Returns the top K documents that match
    public List<Map.Entry<Integer, Double>> getTopK(int k) {
        return docScores.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(k)
                .toList();
    }

    // TODO: Use PriorityQueue of size K
    public List<Integer> getTopKIds(int k) {
        return docScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // Sort descending
                .limit(k)
                .map(Map.Entry::getKey)
                .toList();
    }

}