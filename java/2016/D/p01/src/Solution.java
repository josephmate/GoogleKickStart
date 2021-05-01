import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    /**
     * M < N <= 10
     * M < N <= 2000
     *
     * Solution 1: Brute force
     * Try every permutation of voting and see which one A is always winning.
     * O( (N+M) * (N+M)! ) because
     * 20*20! = 2^66 impossible
     * 4000*4000! = way too big to calculate
     *
     * Solution 2: Brute force better
     * At each vote you have two choices. Try both and count.
     * That means at each position you have 2 choices. Now the complexity becomes a little lower:
     * O(2^(N+M))
     * 2^20 is computable
     * 2^4000 is way too big to compute
     *
     * Solution 3: Divide and Conquer
     * Is the someway we can reformulate the problem as a smaller sub problem, then have a quick way
     * to combine the sub problem(s) to solve the current problem?
     * What we know the solution to M,N-1 or M-1,N Can we use that to solve N?
     * let P(M,N-1) and P(M-1,N) be solved.
     * What is P(M,N)?
     *   How does adding an M affect P(M-1,N)?
     *   How does adding an N affect P(M,N-1)?
     *   How do we combine the previous two results?
     *
     * Here I'm solving a bunch of small patterns to see if there's a way I can combine the sub
     * problems.
     * A B
     * N M
     * 0 0 => 0
     * 0 1 => 0
     * 1 0 => 100
     * 1 1 => 0
     * 0 2 => 0
     * 1 2 => 0
     * 2 0 => 100
     * 2 1 => 33
     * 2 2 => 0
     * 0 3 => 0
     * 1 3 => 0
     * 2 3 => 0
     * 3 0 => 100
     * 3 1 => 50  (A A A B), (A A B A), (A B A A), (B A A A)
     * 3 2 => 20
     * 3 3 => 0
     * 3 4 0.0
     * 3 5 0.0
     * 4 0 1.0
     * 4 1 0.6
     * 4 2 0.3333333333333333
     * 4 3 0.14285714285714285
     * 4 4 0.0
     * 4 5 0.0
     * 5 0 1.0
     * 5 1 0.6666666666666666
     * 5 2 0.42857142857142855
     * 5 3 0.25
     * 5 4 0.1111111111111111
     * 5 5 0.0
     *
     * Turns out that I might not even need to do divide and conquer. After writing all of the above
     * small examples, I notice a pattern where the result is (N-M)/(N+M) but I have no reason why
     * it works for all examples with N,M <= 5. I'm going try and submit with this.
     *
     * @param n the number of A votes
     * @param m the number of B votes
     * @return
     */
    public static double solve(
            final long n,
            final long m
    ) {
        if ( n <= m ) {
            throw new IllegalArgumentException(" n (" + n +") must be greater than m (" + m + ")");
        }

        return (double)(n-m)/(double)(n+m);
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long, Long> pair = parsePairLongLine();
        writer.write(String.valueOf(solve(pair.first, pair.second)));
        writer.write("\n");
    }

    public void parseAndSolveProblems() throws IOException {
        long testCases = parseLongLine();
        for (int i = 1; i <= testCases; i++) {
            handleTestCase(i);
        }
    }

    private static long sqrt(long size) {
        return (long)Math.floor(Math.sqrt(size));
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
