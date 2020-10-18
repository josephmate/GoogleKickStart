import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private String solve(
            long n,
            List<List<Long>> matrix
    ) {
        long maxSoFar = 0;

        // first try the horizontal diagonals
        for (int startC = 0; startC < n; startC++) {
            long sumSoFar = 0;

            for(int i = 0; startC + i < n; i++) {
                sumSoFar += matrix.get(i).get(startC + i);
            }

            if (sumSoFar > maxSoFar) {
                maxSoFar = sumSoFar;
            }
        }

        // now try vertical diagonals
        for (int startR = 0; startR < n; startR++) {
            long sumSoFar = 0;

            for(int i = 0; startR + i < n; i++) {
                sumSoFar += matrix.get(startR + i).get(i);
            }

            if (sumSoFar > maxSoFar) {
                maxSoFar = sumSoFar;
            }
        }

        return String.valueOf(maxSoFar);
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        long n = parseLongLine();
        List<List<Long>> matrix = parseLongMatrix(n);
        String result = solve(
                n,
                matrix
        );
        writer.write(result);
        writer.write("\n");
    }

    public void parseAndSolveProblems() throws IOException {
        long testCases = parseLongLine();
        for (int i = 1; i <= testCases; i++) {
            handleTestCase(i);
        }
    }

    private List<List<Long>> parseLongMatrix(long lines) throws IOException {
        List<List<Long>> result = new ArrayList<>();
        for(long i = 0; i < lines; i++) {
            result.add(parseLongListLine());
        }
        return result;
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
