/**
 * Reads input files (datasets), parses it, and passes it to the indexer
 */

package search.ingest;

import com.fasterxml.jackson.databind.MappingIterator;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IngestRunner {

    public static void main(Path file) throws Exception {
        WikiJsonReader read = new WikiJsonReader();

        try (InputStream in = Files.newInputStream(file) {
            MappingIterator<Document> docs = reader.readDocuments(in);

            while (docs.hasNext()) {
                Document doc = docs.next();
                // TODO: Process document
                // TODO: Index document
            }
        }
    }
}