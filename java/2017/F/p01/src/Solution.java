import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private static long sqrt(long size) {
        return (long)Math.floor(Math.sqrt(size));
    }

    private static boolean canFit(long cakes, long requestedCakeArea, long biggestCake) {
        if(requestedCakeArea == 0 && cakes == 0) {
            return true;
        }
        if(cakes == 0 || requestedCakeArea < 0) {
            return false;
        }

        for(long i = biggestCake; i >= 1; i--) {
            if (canFit(cakes-1, requestedCakeArea-(i*i), biggestCake)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Solution 1)
     * At first I thought a greedy solution going from largest to smallest square would work.
     * Count example 3*3 + 3*3 shows that greedy solution does not work.
     *
     * Now I will try a naive brute force.
     * sqrt(10000) = 100
     *
     * i_1 * 1^2 + i_2 * 2^2 + ... + i_99 * 99^2 + i_100 * 100^2 = N
     * min (i_1 + i_2 + ... + i_99 + i_100)
     *
     * Solution 2) Brainstorming
     * How about start all in i_1
     * then try to move stuff into i_2
     * then try to move stuff into i_3
     * ...
     * then try to move stuff into i_99
     * then try to move stuff into i_100
     * Not sure how to 'bubble' up the cakes into large cakes
     *
     * Solution 3) Try all possible combos
     * for i_1 from 1 to 10000/1^2
     *  for i_2 from 1 to 10000/2^2
     *   ...
     *    for i_99 from 1 tp 10000/99^2
     *     for i_100 from 1 tp 10000/100^2
     * Runtime:
     * 10000/1^2 * 10000/2^2 * ...  * 10000/99^2  * 10000/100^2
     * It's not feasible to calculate these ~10^77 operations.
     *
     * Solution 4) Try all possible combos, a bit smarter
     * for i_1 from 1 to (10000)/1^2
     *  for i_2 from 1 to (10000 - i_1)/2^2
     *   ...
     *    for i_99 from 1 tp (10000 - i_1 - i_2 - ... - i_98)/99^2
     *     for i_100 from 1 tp (10000 - i_1 - i_2 - ... - i_99)/100^2
     * Even with that optimization it will be too slow since it's reduces by big-O smaller than
     * the big-O of solution 3)
     *
     * Solution 4)
     * What if we do something similar to a problem I was stuck on before:
     * - search on the number of cakes needed
     *
     * Brute force from 1 cake to N cakes until we find one that works.
     * Using the greedy solution + trying from 1 to 10,000 the upper bound number of cakes is 7
     * cakes!
     *
     * So at worst our algorithm is 100^7, which is 2^46 which takes hours to a day to calculate.
     *
     * Let's try it anyways. Maybe we get lucky and all the problems are only 3 to 4 cakes!
     *
     * @param requestedCakeArea
     * @return
     */
    public static long solve(
            final long requestedCakeArea
    ) {
        // anything beyond 5 cakes will take to long to compute
        // 100^5 = 2^34
        // 100^6 = 2^40
        // 100^7 = 2^47
        for(int cakes = 1; cakes <=5 ; cakes++) {
            if (canFit(cakes, requestedCakeArea, sqrt(requestedCakeArea))) {
                return cakes;
            }
        }
        return 0;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        writer.write(String.valueOf(solve(parseLongLine())));
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
