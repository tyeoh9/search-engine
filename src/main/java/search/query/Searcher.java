/**
 * Executes queries against the inverted index and ranks documents based on token matches and frequencies
 *
 * Method:
 * 1. Accumulate scores for each document given the tokenized query
 * 2. Use a hashmap to keep track of docScores (e.g. docId -> score);
 *    'score' here would be sum of occurrences of all terms or weighted sum
 * 3. Sort documents based on score
 * 4. Return top k matching documents
 * 5. Try using TF-IDF instead
 *
 */

