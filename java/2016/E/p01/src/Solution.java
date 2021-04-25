import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private static int countBs(
            String pattern
    ) {
        return countBs(pattern, 0, pattern.length()-1);
    }

    private static int countBs(
            String pattern,
            int start,
            int end
    ) {
        int numOfBs = 0;
        int index = start;
        while(true) {
            char currentChar = pattern.charAt(index);
            if (currentChar == 'B') {
                numOfBs++;
            }

            if (index == end) {
                break;
            }

            index = (index + 1) % pattern.length();
        }
        return numOfBs;
    }

    /**
     * Solution A
     * c from i to j  increment if pattern[c % L] == 'B'
     * Since problem size of i and j is 10^18 about 2^60. Although this solution will be easier to
     * implement, this is at the limit of brute forcing the calculation. So we should go with Solution B.
     *
     * Solution A
     * L length of string
     * 1) b number of B in pattern
     * 2) Figure out which section of pattern is in the remainder N%L
     * There will be some characters at the front and end that don't fit:
     *
     * 1 2 3 4 5 6 7 9 10
     * A B C A B C A B C
     *   |         |
     *   ==========
     *
     * Instead of determining the characters at the front and end that don't fit. We can rotate
     * the pattern so it matches the start at i. Then we can figure out how many characters to cut
     * off the end.
     *
     * @param pattern
     * @param i
     * @param j
     * @return
     */
    public static int solve(
            String pattern,
            int i,
            int j
    ) {
        // 0 index
        i = i - 1;
        j = j - 1;

        int numOfBsInPattern = countBs(pattern);
        int fullPatterns = (j - i + 1)/pattern.length();
        int totalBs = fullPatterns*numOfBsInPattern;

        if ( (j - i + 1)%pattern.length() > 0 ) {
            int patternStart = i % pattern.length();
            int patternEnd = j % pattern.length();
            totalBs += countBs(pattern, patternStart, patternEnd);
        }

        return totalBs;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        String line = parseStringLine();
        Pair<Long, Long> pair = parsePairLongLine();
        writer.write(String.valueOf(solve(line, pair.first.intValue(), pair.second.intValue())));
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

    private static final class Triple<X,Y,Z> extends Pair<X,Y> {
        public final Z third;

        public Triple(X first, Y second, Z third) {
            super(first, second);
            this.third = third;
        }

        public String toString() {
            return Arrays.asList(first, second, third).toString();
        }
    }

    private static class Pair<X, Y> {
        public final X first;
        public final Y second;

        public Pair(X first, Y second) {
            this.first = first;
            this.second = second;
        }

        public X getFirst() {
            return first;
        }

        public Y getSecond() {
            return second;
        }

        public String toString() {
            return Arrays.asList(first, second).toString();
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

    private int parseIntLine() throws IOException {
        try {
            int result = Integer.parseInt(reader.readLine().trim());
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
