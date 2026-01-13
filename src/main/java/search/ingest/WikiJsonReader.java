/**
 * Reads JSON lines from Wikipedia dump files and converts them into Document objects
 * Uses Jackson streaming
 */

package search.ingest;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class WikiJsonReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Reads a JSON file line by line and returns an iterator of Document objects
     *
     * Usage:
     * WikiJsonReader reader = new WikiJsonReader();
     *
     * try (InputStream in = Files.newInputStream(Path.of("wiki_500.json"))) {
     *     MappingIterator<Document> docs = reader.readDocuments(in);
     *
     *     while (docs.hasNext()) {
     *         Document doc = docs.next();
     *         // index document
     *     }
     * }
     */
    public MappingIterator<Document> readDocuments(InputStream in) throws IOException {
        return MAPPER.readerFor(Document.class).readValues(in);
    }
}