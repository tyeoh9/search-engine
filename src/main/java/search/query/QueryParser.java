/**
 * Parses a user query string into normalized tokens suitable for searching the inverted index
 */

package search.query;

import search.analysis.Tokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class QueryParser {

    // Parse user query, normalize, and tokenize
    public List<String> parse(String rawQuery) {
        return new ArrayList<>(
                new HashSet<>(Tokenizer.tokenize(rawQuery))
        );
    }
}