/**
 * Reads input files (datasets), parses it, and passes it to the indexer
 */

package search.ingest;

import com.fasterxml.jackson.databind.MappingIterator;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IngestRunner {

    public static void run(Path file) throws Exception {
        WikiJsonReader reader = new WikiJsonReader();

        try (InputStream in = Files.newInputStream(file)) {
            MappingIterator<Document> docs = reader.readDocuments(in);

            while (docs.hasNext()) {
                Document doc = docs.next();
                // TODO: Index document
            }
        }
    }
}