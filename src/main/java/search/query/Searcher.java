/**
 * Executes queries against the inverted index and ranks documents based on token matches and frequencies
 *
 * Method:
 * 1. Accumulate scores for each document given the tokenized query
 * 2. Use a hashmap to keep track of docScores (e.g. docId -> score);
 *    'score' here would be sum of occurrences of all terms or weighted sum
 * 3. Sort documents based on score
 * 4. Return top k matching documents
 * 5. Try using TF-IDF instead
 *
 */

package search.query;

import search.index.InvertedIndex;
import search.index.Posting;

import java.util.HashMap;
import java.util.List;

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

    // Score documents against query using term frequencies (tf)
    public void scoreDocuments(List<String> tokenizedQuery) {
        for (String token : tokenizedQuery) {
            List<Posting> postings = this.index.getPostings(token);

            for (Posting p : postings) {
                this.increaseScore(p.getDocId(), p.getFrequency());
            }
        }
    }

}