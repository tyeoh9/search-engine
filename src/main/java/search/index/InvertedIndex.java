/**
 * Stores the core mapping from tokens to lists of postings, enabling fast lookup for queries
 *
 * Map<String, HashMap<Integer, Integer>>
 *
 * term -> {docId, termFreq}
 *
 */

package search.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.segment.TextSegment;
import search.analysis.Tokenizer;
import search.ingest.Document;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;

public class InvertedIndex {

    private final HashMap<String, HashMap<Integer, Integer>> invertedIndex;
    @JsonProperty("allVectors")
    private HashMap<Integer, float[]> vectorIndex = new HashMap<>(); // docId -> embedding
    private final EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

    @JsonProperty("wordCount")
    private HashMap<Integer, Integer> wordCount; // docId -> wordCount
    @JsonProperty("documentCount")
    private int documentCount;
    @JsonProperty("wordsParsed")
    private int wordsParsed;

    public InvertedIndex() {
        this.invertedIndex = new HashMap<>();
        this.wordCount = new HashMap<>();
        this.documentCount = 0;
        this.wordsParsed = 0;
    }

    // Indexing
    public void addDocument(Document doc) {
        // TODO: Tokenize document -> build local map -> merge with global

        // Traditional indexing - BM25
        String rawText = doc.getText();
        if (rawText == null || rawText.isBlank()) { return; }

        List<String> tokenizedText = Tokenizer.tokenize(rawText);
        int docSize = tokenizedText.size();

        documentCount++;
        this.wordCount.put(doc.getId(), docSize);
        this.wordsParsed += docSize;

        for (String token : tokenizedText) {
            HashMap<Integer, Integer> postings = this.getPostings(token);
            postings.merge(doc.getId(), 1, Integer::sum);
        }
    }

    // Lookup
    public HashMap<Integer, Integer> getPostings(String term) {
        return this.invertedIndex.computeIfAbsent(term, t -> new HashMap<>());
    }

    // Get document count
    public int getDocumentCount() {
        return this.documentCount;
    }

    // Get average doc length
    @JsonIgnore
    public double getAverageDocLength() {
        if (documentCount == 0) return 0;
        return (double) this.wordsParsed / this.getDocumentCount();
    }

    // Get length of specific document
    public int getDocLength(int docId) {
        return wordCount.get(docId);
    }

    // Get index
    public HashMap<String, HashMap<Integer, Integer>> getIndex() {
        return this.invertedIndex;
    }

    // Get embedding
    public float[] getEmbedding(int docId) {
        return vectorIndex.get(docId);
    }

    public Map<Integer, float[]> getAllVectors() {
        return vectorIndex;
    }

    public void buildVectorIndex(List<Document> docs) {
        List<Document> validDocs = docs.stream()
                .filter(doc -> doc.getText() != null && doc.getText().trim().length() > 10)
                .toList();

        int total = validDocs.size();
        for (int i = 0; i < total; i++) {
            Document doc = validDocs.get(i);
            try {
                TextSegment segment = TextSegment.from(doc.getText().trim());
                Embedding embedding = embeddingModel.embed(segment).content();
                vectorIndex.put(doc.getId(), embedding.vector());
            } catch (Exception e) {
                System.out.println("Skipping doc " + doc.getId() + " (" + doc.getTitle() + "): " + e.getMessage());
            }

            if (i % 100 == 0) System.out.println("Embedded " + i + "/" + total + " docs...");
        }
    }

    // Save index to disk
    public void save(Path path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(path.toFile(), this);
    }

    // Load index from disk
    public static InvertedIndex load(Path path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), InvertedIndex.class);
    }
}