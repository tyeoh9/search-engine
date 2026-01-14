/**
 * Reads input files (datasets), parses it, and passes it to the indexer
 */

package search.ingest;

import search.index.InvertedIndex;

import com.fasterxml.jackson.databind.MappingIterator;
import search.index.Posting;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class IngestRunner {

    public static InvertedIndex run(Path file) throws Exception {
        WikiJsonReader reader = new WikiJsonReader();
        InvertedIndex index = new InvertedIndex();

        try (InputStream in = Files.newInputStream(file)) {
            MappingIterator<Document> docs = reader.readDocuments(in);
            while (docs.hasNext()) {
                Document doc = docs.next();
                index.addDocument(doc);
            }
        }

        return index;
    }
}