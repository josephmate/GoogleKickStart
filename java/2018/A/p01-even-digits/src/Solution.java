import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private List<Integer> toDigitsFromLong(long number) {
        String numberStr = String.valueOf(number);
        List<Integer> result = new ArrayList<>(numberStr.length());
        for(int i = 0; i < numberStr.length(); i++) {
            result.add(0);
        }
        for(int i = numberStr.length() - 1; i >= 0; i--) {
            // '0' is 48 in ascii
            result.set(numberStr.length() - 1 - i, numberStr.charAt(i) - 48);
        }
        return result;
    }

    private long toLongFromDigits(List<Integer> digits) {
        long sum = 0;
        long currentMultiplier = 1;
        for(int i = 0; i < digits.size(); i++) {
            int currentDigit = digits.get(i);
            sum += currentMultiplier * currentDigit;
            currentMultiplier *= 10;
        }
        return sum;
    }

    private long increaseUntilEven(
            long start
    ) {
        List<Integer> digits = toDigitsFromLong(start);
        for(int i = digits.size() - 1; i >= 0; i--) {
            if(digits.get(i) % 2 != 0) {
                if (digits.get(i) == 9) {
                    digits.add(2);
                    digits.set(i, 0);
                } else {
                    digits.set(i, digits.get(i) + 1);
                }
                for(int j = i - 1; j >= 0; j--) {
                    digits.set(j, 0);
                }
                return toLongFromDigits(digits);
            }
        }
        return toLongFromDigits(digits);
    }

    /***
     * 333
     * 288
     */
    private long decreaseUntilEven(
            long start
    ) {
        List<Integer> digits = toDigitsFromLong(start);
        for(int i = digits.size() - 1; i >= 0; i--) {
            if(digits.get(i) % 2 != 0) {
                digits.set(i, digits.get(i) - 1);
                for (int j = i - 1; j >= 0; j--) {
                    digits.set(j, 8);
                }
                return toLongFromDigits(digits);
            }
        }
        return toLongFromDigits(digits);
    }

    private String solve(
            long startNumber
    ) {
        long increased = increaseUntilEven(startNumber) - startNumber;
        long decreased = startNumber - decreaseUntilEven(startNumber);
        return String.valueOf(Math.min(increased, decreased));
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        long n = parseLongLine();
        String result = solve(
                n
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
