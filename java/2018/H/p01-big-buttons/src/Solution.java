import java.io.*;
import java.util.*;

public class Solution {

    private boolean isAnotherPrefix(String input, Set<String> others) {
        for (String other : others) {
            if (!input.equals(other) && input.startsWith(other)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> deduplicatePrefixes(List<String> forbiddenSequences) {
        Set<String> deduplicatedStrings = new HashSet<>(forbiddenSequences);
        Set<String> result = new HashSet<>();

        for (String prefix : deduplicatedStrings) {
            if (!isAnotherPrefix(prefix, deduplicatedStrings)) {
                result.add(prefix);
            }
        }

        return result;
    }

    private long longPow(long base, long exponent) {
        long result = 1;
        for (int i = 0; i < exponent; i++) {
            result = result * base;
        }
        return result;
    }

    /**
     * We cannot enumerate all of the button press.
     * That's potentially 2^50 in 20 seconds per test.
     *
     * As a result, we need a more efficient way of calculating the result.
     *
     * Notice that L len(forbidden) will eliminate
     * 1 / L of the sequences
     *
     * For instance:
     * A eliminates 1 / 2
     * B eliminates 1 / 2
     * AA eliminates 1 / 4
     * AB eliminates 1 / 4
     * BA eliminates 1 / 4
     * BB eliminates 1 / 4
     *
     * Also notice that:
     * A eliminates all the sequences that AA, AB eliminated.
     * As result we want to deduplicate any repeated sequences.
     * After de-duplicating we can count up the eliminated
     * 2^N - eliminated
     * will be the result.
     *
     * @param buttonPresses
     * @param numStrings
     * @param forbiddenSequences
     * @return
     */
    private long solve(
            long buttonPresses,
            long numStrings,
            List<String> forbiddenSequences
    ) {
        Set<String> deduplicatedPrefixes = deduplicatePrefixes(forbiddenSequences);
        final long totalPossibleSequences = longPow(2, buttonPresses);
        long numInvalidSequences = 0;
        for (String deduplicatedPrefix : deduplicatedPrefixes) {
            numInvalidSequences += totalPossibleSequences/longPow(2, deduplicatedPrefix.length());
        }
        return totalPossibleSequences - numInvalidSequences;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long,Long> pair = parsePairLongLine();
        List<String> values = parseManyStringLine(pair.second.intValue());
        long result = solve(
                pair.first,
                pair.second,
                values
        );
        writer.write(String.valueOf(result));
        writer.write("\n");
    }

    public void parseAndSolveProblems() throws IOException {
        long testCases = parseLongLine();
        for (int i = 1; i <= testCases; i++) {
            handleTestCase(i);
        }
    }

    private List<Long> parseLongListLine() throws IOException {
        try {
            String line = reader.readLine();
            String [] columns = line.split(" ");
            List<Long> result = new ArrayList<>(columns.length);
            for (int i = 0; i < columns.length; i++) {
                result.add(Long.parseLong(columns[i]));
            }
            currentLine++;
            return result;
        } catch (RuntimeException e) {
            System.err.println("Was expecting line " + currentLine + " to have many Longs");
            throw e;
        } catch (IOException e) {
            System.err.println("Could not read line " + currentLine);
            throw e;
        }
    }
    private List<String> parseManyStringLine(int numStrings) throws IOException {
        List<String> result = new ArrayList<>(numStrings);
        for (long i = 0; i < numStrings; i++) {
            result.add(parseStringLine());
        }
        return result;
    }

    private String parseStringLine() throws IOException {
        try {
            String line = reader.readLine();
            currentLine++;
            return line;
        } catch (RuntimeException e) {
            System.err.println("Was expecting line " + currentLine + " to fit into a Long");
            throw e;
        } catch (IOException e) {
            System.err.println("Could not read line " + currentLine);
            throw e;
        }
    }

    private static final class Pair<X, Y> {
        public final X first;
        public final Y second;

        public Pair(X first, Y second) {
            this.first = first;
            this.second = second;
        }
    }

    private List<Pair<Long,Long>> parseManyLongPairs(int numPairs) throws IOException {
        List<Pair<Long,Long>> result = new ArrayList<>(numPairs);
        for (long i = 0; i < numPairs; i++) {
            result.add(parsePairLongLine());
        }
        return result;
    }

    private Pair<Long, Long> parsePairLongLine() throws IOException {
        try {
            String line = reader.readLine().trim();
            String [] columns = line.split(" ");
            long first = Long.parseLong(columns[0]);
            long second = Long.parseLong(columns[1]);
            currentLine++;
            return new Pair<>(first, second);
        } catch (RuntimeException e) {
            System.err.println("Was expecting line " + currentLine + " to fit into a Long");
            throw e;
        } catch (IOException e) {
            System.err.println("Could not read line " + currentLine);
            throw e;
        }
    }

    private long parseLongLine() throws IOException {
        try {
            long result = Long.parseLong(reader.readLine().trim());
            currentLine++;
            return result;
        } catch (RuntimeException e) {
            System.err.println("Was expecting line " + currentLine + " to fit into a Long");
            throw e;
        } catch (IOException e) {
            System.err.println("Could not read line " + currentLine);
            throw e;
        }
    }

    private final BufferedReader reader;
    private final BufferedWriter writer;
    private long currentLine = 1;

    public Solution(
            BufferedReader reader,
            BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            new Solution(reader, writer).parseAndSolveProblems();
        }
    }
}
