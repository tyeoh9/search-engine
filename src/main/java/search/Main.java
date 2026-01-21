/**
 * Orchestrates dataset loading, idnexing, and querying
 */

package search;

import search.index.InvertedIndex;
import search.index.Posting;
import search.ingest.WikiJsonReader;
import search.ingest.Document;
import search.query.QueryParser;
import search.query.Searcher;

import com.fasterxml.jackson.databind.MappingIterator;

import javax.management.Query;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        // TODO: Add orchestration logic

        // Retrieves all articles that contain the searchTerm
        Path file = Path.of("data/wiki_2000.json");

        WikiJsonReader reader = new WikiJsonReader();
        InvertedIndex index = new InvertedIndex();
        HashMap<Integer, String> docIdToTitle = new HashMap<Integer, String>();
        QueryParser parser = new QueryParser();
        Searcher searcher = new Searcher(index);

        // Stream and index each documents
        try (InputStream in = Files.newInputStream(file)) {
            MappingIterator<Document> docs = reader.readDocuments(in);
            while (docs.hasNext()) {
                Document doc = docs.next();
                System.out.println("Processing: " + doc.getTitle());
                docIdToTitle.put(doc.getId(), doc.getTitle());
                index.addDocument(doc);
            }
        }

        // Score and rank documents against search query
        String userQuery = "development of modern physics";
        searcher.scoreDocs(parser.parse(userQuery));
        List<Map.Entry<Integer, Double>> results = searcher.getTopK(5);
        System.out.println("\nResults for '" + userQuery + "':");
        for (Map.Entry<Integer, Double> r : results) {
            String relatedArticle = docIdToTitle.get(r.getKey());
            System.out.println("\t-" + relatedArticle + " (score: " + r.getValue() + ")");
        }
        System.out.println("\nFinished reading, indexing, and querying.");

    }
}