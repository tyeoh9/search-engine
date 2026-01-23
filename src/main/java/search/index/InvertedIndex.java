/**
 * Stores the core mapping from tokens to lists of postings, enabling fast lookup for queries
 *
 * Map<String, List<Posting>>
 *
 * For example, an entry will look like:
 *     java → [
 *        Posting(docId=1, termFrequency=1),
 *        Posting(docId=3, termFrequency=1)
 *     ]
 */

package search.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import search.analysis.Tokenizer;
import search.ingest.Document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvertedIndex {

    // TODO: Instead of using List<Posting> use HashMap<Integer, Integer>
    private final HashMap<String, List<Posting>> invertedIndex;
    private int documentCount;

    public InvertedIndex() {
        this.invertedIndex = new HashMap<>();
        this.documentCount = 0;
    }

    // Indexing
    public void addDocument(Document doc) {
        // TODO: Skip very common stop words (e.g. the, of, and)
        // TODO: Tokenize document -> build local map -> merge with global
        documentCount++;

        String rawText = doc.getText();

        if (rawText == null || rawText.isBlank()) { return; }

        List<String> tokenizedText = Tokenizer.tokenize(rawText);
        for (String token : tokenizedText) {
            List<Posting> postings = this.getPostings(token);
            Posting posting = findPosting(postings, doc.getId());

            if (posting == null) {
                postings.add(new Posting(doc.getId(), 1));
            } else {
                posting.incrementFreq();
            }
        }
    }

    // Lookup
    public List<Posting> getPostings(String term) {
        return this.invertedIndex.computeIfAbsent(term, t -> new ArrayList<>());
    }

    // Find posting
    public Posting findPosting(List<Posting> postings, int docId) {
        for (Posting posting : postings) {
            if (posting.getDocId() == docId) {
                return posting;
            }
        }
        return null;
    }

    // Get document count
    public int getDocumentCount() {
        return this.documentCount;
    }

    // Get index
    public HashMap<String, List<Posting>> getIndex() {
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