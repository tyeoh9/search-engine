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

import search.index.InvertedIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    // Maps docId -> score
    private final HashMap<Integer, Double> docScores;
    private final InvertedIndex index;

    public Searcher(InvertedIndex index) {
        this.docScores = new HashMap<>();
        this.index = index;
    }

    // Increase score for docId by additionalScore
    public void increaseScore(int docId, double additionalScore) {
        this.docScores.merge(docId, additionalScore, Double::sum);
    }

    // Score document using BM25
    public void scoreDocs(List<String> tokenizedQuery) {
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

    // Returns the top K documents that match
    public List<Map.Entry<Integer, Double>> getTopK(int k) {
        return docScores.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(k)
                .toList();
    }

}