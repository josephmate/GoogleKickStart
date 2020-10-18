import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private static class RecResult {
        long count;
        long scoreSum;
    }

    private RecResult solveRecursive(
            long depth,
            List<Long> values,
            Map<List<Long>, RecResult> memoized) {
        RecResult result = new RecResult();
        if (values.size() == 1) {
            result.count = 1;
            result.scoreSum = 0;
            for(int i = 0; i < depth; i++) {
                System.out.print("\t");
            }
            System.out.println(values + " " + result.scoreSum + " " + result.count);
            return result;
        }

        if(memoized.containsKey(values)) {
            result = memoized.get(values);
            for(int i = 0; i < depth; i++) {
                System.out.print("\t");
            }
            System.out.println(values + " " + result.scoreSum + " " + result.count + " MEMOIZED");
            return result;
        }

        result.scoreSum = 0;
        result.count = 0;
        for (int i = 0; i < values.size() - 1; i++) {
            List<Long> clone = new ArrayList<>(values);
            long val = clone.remove(i);
            long currentRoundScore = val + clone.get(i);
            clone.set(i, val + clone.get(i));

            RecResult partialResult = solveRecursive(depth + 1, clone, memoized);
            result.scoreSum += partialResult.scoreSum + currentRoundScore;
            result.count += partialResult.count;
        }

        memoized.put(values, result);
        for(int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.println(values + " " + result.scoreSum + " " + result.count);
        return result;
    }

    private String solve(
            long len,
            List<Long> values
    ) {
        boolean[] visited = new boolean[values.size()];
        for(int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        RecResult result = solveRecursive(0, values, new HashMap<>());
        return String.valueOf((double)result.scoreSum / (double)result.count);
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        long n = parseLongLine();
        List<Long> values = parseLongListLine();
        String result = solve(
                n,
                values
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
