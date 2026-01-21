/**
 * Holds constants and config parameters
 */

package search.config;

import java.nio.file.Path;

public final class Config {

    // Prevent instantiation
    private Config() {}

    // Data paths
    public static final Path WIKI_DATASET = Path.of("data/wiki_10000.json");
    public static final Path INDEX_PATH = Path.of("data/index_10000.json");
    public static final Path DOC_TITLES_PATH = Path.of("data/doc_titles_10000.json");

    // Query settings
    public static final int TOP_K_RESULTS = 5;
}