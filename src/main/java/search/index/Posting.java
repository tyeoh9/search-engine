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

    public int getDocId() { return this.docId; }

    public int getFrequency() { return this.termFrequency; }

    // Increment term frequency
    public void incrementFreq() {
        this.termFrequency++;
    }

    // Print posting (for debugging)
    public void printPosting() {
        System.out.println("DocId: " + this.getDocId() + ", Frequency: " + this.getFrequency());
    }
}