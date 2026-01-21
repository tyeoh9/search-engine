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

import java.util.HashMap;

public class Searcher {

    // Maps docId -> score
    private HashMap<Integer, Double> docScores;

    public Searcher() {
        this.docScores = new HashMap<>();
    }

    // Increase score for docId by additionalScore
    public void increaseScore(int docId, double additionalScore) {
        this.docScores.merge(docId, additionalScore, Double::sum);
    }

}