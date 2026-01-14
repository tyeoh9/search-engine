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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {

    private final Map<String, List<Posting>> invertedIndex;

    public InvertedIndex() {
        this.invertedIndex = new HashMap<>();
    }
}