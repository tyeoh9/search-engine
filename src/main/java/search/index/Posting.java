/**
 * Represents a single occurrence of a term in a document, including document ID and term frequency
 */

package search.index;

public class Posting {

    private final int docId;
    private int termFrequency;

    public Posting(int docId, int termFrequency) {
        this.docId = docId;
        this.termFrequency = termFrequency;
    }
}