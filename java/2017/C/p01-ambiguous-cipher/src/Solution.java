import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Solution {

    private static final int MISSING = -1;

    private long[] convertToNumbers(String string) {
        long[] result = new long[string.length()];
        for(int i = 0; i < result.length; i++) {
            result[i] = string.charAt(i) - 65;
        }
        return result;
    }

    private String convertToString(long[] numbers) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < numbers.length; i++) {
            builder.append((char)(numbers[i] + 65));
        }
        return builder.toString();
    }


    /**
     * <pre>
     * S = 18, O = 14, U = 20, and P = 15
     * First letter:        14  mod 26 = 14   O
     * Second letter: (18 + 20) mod 26 = 12   M
     * Third letter:  (14 + 15) mod 26 = 3    D
     * Fourth letter:       20  mod 26 = 20   U
     *
     * 0           1               2         3
     * D[1]   D[0]+D[2]       D[1]+D[3]   D[2]
     * O       S     U          O   P      U
     * O          M               D        U
     *
     * D[1]        = E[0]
     * D[0] + D[2] = E[1]
     * D[1] + D[3] = E[2]
     *        D[2] = E[3]
     *
     * D[1] = E[0]
     * D[2] = E[3]
     * D[0] = E[1] - D[0]
     * D[3] = E[2] - D[1]
     *
     *
     *
     * D[1]             = E[0]
     * D[0]    + D[2]   = E[1]
     * D[1]    + D[3]   = E[2]
     * D[2]    + D[4]   = E[3]
     * D[3]    + D[5]   = E[4]
     *        ...
     * D[i-1]  + D[i+1] = E[i]
     *        ...
     * D[N-2]  + D[N] = E[N-1]
     *           D[N-1] = E[N]
     *
     * D[0] = E[1] - D[2]
     * D[1] = E[0]
     * D[1] = E[2] - D[3]
     * D[2] = E[3] - D[4]
     * D[2] = E[1] - D[0]
     * ..
     * D[i] = E[i+1] - D[i+2]
     * D[i] = E[i-1] - D[i-2]
     * ..
     * D[N-1] = E[N-2] - D[N-3]
     * D[N-1] = E[N]   - D[N+1] = E[N]
     * D[N]   = E[N-1] - D[N-2]
     * D[N]   = E[N+1] - D[N+2] = IMPOSSIBLE
     * </pre>
     * @param input the string to decrypt
     * @return decrypted string if possible, null otherwise
     */
    private String solveImpl(
            String input
    ) {
        long[] encrypted = convertToNumbers(input);
        long[] decrypted = new long[encrypted.length];
        for (int i = 0; i < decrypted.length; i++) {
            decrypted[i] = MISSING;
        }
        decrypted[1] = encrypted[0];
        decrypted[decrypted.length-2] = encrypted[encrypted.length-1];
        boolean madeChange = true;
        // NESTED LOOP is not efficient, but good enough because max input size is 50 characters
        // 50*50 is only 2500 iterations of the loop! I suspect we could reduce this to linear,
        // but I'm aiming to write a solution as quickly as possible.
        while(madeChange) {
            madeChange = false;
            for (int i = 0; i < decrypted.length; i++) {
                if (decrypted[i] != MISSING) {
                    continue;
                }
                if (i - 2 >= 0
                    && decrypted[i-2] != MISSING
                ) {
                    madeChange = true;
                    decrypted[i] = encrypted[i-1] - decrypted[i-2];
                    if (decrypted[i] < 0) {
                        decrypted[i] += 26;
                    }
                } else if (i + 2 <= decrypted.length - 1
                        && decrypted[i+2] != MISSING
                ) {
                    madeChange = true;
                    decrypted[i] = encrypted[i+1] - decrypted[i+2];
                    if (decrypted[i] < 0) {
                        decrypted[i] += 26;
                    }
                }
            }
        }

        for (int i = 0; i < decrypted.length; i++) {
            if(decrypted[i] == -1) {
                return null;
            }
        }

        return convertToString(decrypted);
    }

    private String solve(
            String input
    ) {
        String result = solveImpl(input);
        if (result == null) {
            return "AMBIGUOUS";
        }
        return result;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        String str = parseStringLine();
        String result = solve(
                str
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
