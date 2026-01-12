/*
Stores important metadata about each Document
*/

package search.ingest;

public final class Document {

    private final int id;
    private final String title;
    private final String text;

    public Document(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }
}