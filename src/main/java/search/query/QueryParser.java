/**
 * Parses a user query string into normalized tokens suitable for searching the inverted index
 */

package search.query;

import search.analysis.Tokenizer;

import java.util.List;

public class QueryParser {

    // TODO: Deduplicate repeat words in the query

    // Parse user query, normalize, and tokenize
    public List<String> parse(String rawQuery) {
        return Tokenizer.tokenize(rawQuery);
    }
}