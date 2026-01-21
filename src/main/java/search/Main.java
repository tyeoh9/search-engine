/**
 * Orchestrates dataset loading, indexing, and querying
 */

package search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import search.config.Config;
import search.index.InvertedIndex;
import search.ingest.WikiJsonReader;
import search.ingest.Document;
import search.query.QueryParser;
import search.query.Searcher;

import com.fasterxml.jackson.databind.MappingIterator;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        InvertedIndex index;
        Map<Integer, String> docIdToTitle;
        ObjectMapper mapper = new ObjectMapper();

        // Load index and doc titles if it exists
        if (Files.exists(Config.INDEX_PATH) && Files.exists(Config.DOC_TITLES_PATH)) {
            System.out.println("Loading index from disk...");
            index = InvertedIndex.load(Config.INDEX_PATH);
            docIdToTitle = mapper.readValue(Config.DOC_TITLES_PATH.toFile(), new TypeReference<Map<Integer, String>>() {});
        } else { // Build otherwise
            System.out.println("Building index...");
            index = new InvertedIndex();
            docIdToTitle = new HashMap<>();

            WikiJsonReader reader = new WikiJsonReader();

            try (InputStream in = Files.newInputStream(Config.WIKI_DATASET)) {
                MappingIterator<Document> docs = reader.readDocuments(in);
                while (docs.hasNext()) {
                    Document doc = docs.next();
                    docIdToTitle.put(doc.getId(), doc.getTitle());
                    index.addDocument(doc);
                }
            }

            index.save(Config.INDEX_PATH);
            mapper.writeValue(Config.DOC_TITLES_PATH.toFile(), docIdToTitle);
        }

        // Score and rank documents against user query
        String query = "china";
        search(query, index, docIdToTitle);

    }

    private static void search(String userQuery, InvertedIndex index, Map<Integer, String> docIdToTitle) {
        QueryParser parser = new QueryParser();
        Searcher searcher = new Searcher(index);

        searcher.scoreDocs(parser.parse(userQuery));
        List<Map.Entry<Integer, Double>> results = searcher.getTopK(Config.TOP_K_RESULTS);
        System.out.println("\nResults for '" + userQuery + "':");
        for (Map.Entry<Integer, Double> r : results) {
            String relatedArticle = docIdToTitle.get(r.getKey());
            System.out.println("\t-" + relatedArticle + " (score: " + r.getValue() + ")");
        }
    }
}