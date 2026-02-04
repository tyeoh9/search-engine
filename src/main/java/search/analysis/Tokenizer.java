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

import org.tartarus.snowball.ext.englishStemmer;

public class Tokenizer {

    public static List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return List.of(); // Returns empty list (immutable)
        }

        // NOTE: Not thread safe
        englishStemmer stemmer = new englishStemmer();

        return Arrays.stream(text
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+"))
                .filter(token -> !token.isBlank())
                .filter(token -> !Config.STOPWORDS.contains(token))
                .map(token -> {
                    stemmer.setCurrent(token);
                    stemmer.stem();
                    return stemmer.getCurrent();
                })
                .toList();
    }
}