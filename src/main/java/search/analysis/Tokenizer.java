/**
 * Convert raw text -> normalized tokens:
 * - Lowercasing
 * - Token splitting
 * - Basic normalization
 * - Stopword removal
 */

package search.analysis;

import search.config.Config;

import java.util.Arrays;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return List.of(); // Returns empty list (immutable)
        }

        // TODO: Perform stemming as well
        return Arrays.stream(text
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+"))
                .filter(token -> !token.isBlank())
                .filter(token -> !Config.STOPWORDS.contains(token))
                .toList();
    }
}