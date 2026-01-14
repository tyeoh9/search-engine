/**
 * Stores important metadata about each Document
 */

package search.ingest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"revid", "url"})
public final class Document {

    private final int id;
    private final String title;
    private final String text;

    @JsonCreator
    public Document(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getId() { return this.id; }

    public String getTitle() { return this.title; }

    public String getText() { return this.text;}
}