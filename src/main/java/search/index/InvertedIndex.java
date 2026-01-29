/**
 * Stores the core mapping from tokens to lists of postings, enabling fast lookup for queries
 *
 * Map<String, HashMap<Integer, Integer>>
 *
 * term -> {docId, termFreq}
 *
 */

package search.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import search.analysis.Tokenizer;
import search.ingest.Document;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class InvertedIndex {

    private final HashMap<String, HashMap<Integer, Integer>> invertedIndex;
    private int documentCount;

    public InvertedIndex() {
        this.invertedIndex = new HashMap<>();
        this.documentCount = 0;
    }

    // Indexing
    public void addDocument(Document doc) {
        // TODO: Tokenize document -> build local map -> merge with global

        String rawText = doc.getText();
        if (rawText == null || rawText.isBlank()) { return; }

        documentCount++;

        List<String> tokenizedText = Tokenizer.tokenize(rawText);
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

    // Get index
    public HashMap<String, HashMap<Integer, Integer>> getIndex() {
        return this.invertedIndex;
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