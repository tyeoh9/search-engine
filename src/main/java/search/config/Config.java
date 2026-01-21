/**
 * Holds constants and config parameters
 */

package search.config;

import java.nio.file.Path;

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
}