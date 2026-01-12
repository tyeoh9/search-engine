/*
Contains data of each entry in the inverted index
*/

package search.index;

public final class Posting {

    int docId;
    int termFrequency;

    public Posting(int docId, int termFrequency) {
        this.docId = docId;
        this.termFrequency = termFrequency;
    }
}