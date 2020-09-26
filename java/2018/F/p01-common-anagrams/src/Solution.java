import java.io.*;
import java.util.*;

public class Solution {

    private Map<Character, Long> calcAnagramCount(String input) {
        Map<Character, Long> anagramCounts = new HashMap<>();
        for (char character : input.toCharArray()) {
            long count = anagramCounts.getOrDefault(character, 0L);
            count++;
            anagramCounts.put(character, count);
        }
        return anagramCounts;
    }

    private Set<Map<Character, Long>> generateAllPossibleAnagramCounts(String input) {
        Set<Map<Character, Long>> allPossibleAnagrams = new HashSet<>();
        for (int upperBound = 0; upperBound < input.length(); upperBound++) {
            for (int lowerBound = 0; lowerBound <= upperBound; lowerBound++) {
                allPossibleAnagrams.add(
                        calcAnagramCount(input.substring(lowerBound, upperBound + 1)));
            }
        }
        return allPossibleAnagrams;
    }

    /**
     * Since the length is at most 50 with 20 seconds per test set, I think we can implement this
     * in a naive O(N^3)
     *
     * @param length
     * @param first
     * @param second
     * @return
     */
    private long solve(
            long length,
            String first,
            String second
    ) {
        long result = 0;
        Set<Map<Character, Long>> allPossibleAnagramCounts = generateAllPossibleAnagramCounts(second);
        for (int upperBound = 0; upperBound < length; upperBound++) {
            for (int lowerBound = 0; lowerBound <= upperBound; lowerBound++) {
                if (allPossibleAnagramCounts.contains(
                        calcAnagramCount(first.substring(lowerBound, upperBound +1)))) {
                    result++;
                }
            }
        }
        return result;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        long length = parseLongLine();
        String firstString = parseStringLine();
        String secondString = parseStringLine();
        long result = solve(
                length,
                firstString,
                secondString
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
