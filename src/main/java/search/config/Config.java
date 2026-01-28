/**
 * Holds constants and config parameters
 */

package search.config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;

public final class Config {

    // Prevent instantiation
    private Config() {}

    // Dataset size
    public static final int SIZE = 100000;

    // Data paths
    public static final Path WIKI_DATASET = Path.of(String.format("data/wiki_%d.json", SIZE));
    public static final Path INDEX_PATH = Path.of(String.format("data/index_%d.json", SIZE));
    public static final Path DOC_TITLES_PATH = Path.of(String.format("data/doc_titles_%d.json", SIZE));


    // Query settings
    public static final int TOP_K_RESULTS = 5;

    // Stop words
    public static final HashSet<String> STOPWORDS = new HashSet<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));
}