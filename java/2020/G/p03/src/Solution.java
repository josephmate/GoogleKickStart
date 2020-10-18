import java.io.*;
import java.util.*;

public class Solution {

    private long tryDown(
            long targetValue,
            long startValue,
            long modulus
    ) {
        // example 0 1 2
        // 2 to 0
        // 2 - 0 = 2
        if(startValue >= targetValue) {
            return startValue - targetValue;
        }
        // example 0 1 2
        // 0 to 2
        // 3 - (2 - 0) = 1
        return modulus - (targetValue - startValue);
    }

    private long tryUp(
            long targetValue,
            long startValue,
            long modulus
    ) {
        // example 0 1 2
        // 0 to 2
        // 2 - 0 = 2
        if (startValue <= targetValue) {
            return targetValue - startValue;
        }
        // example 0 1 2
        // 2 to 0
        // 3 - (2 - 0) = 1
        return modulus - (startValue - targetValue);
    }

    private long tryValue(
            long targetValue,
            long modulus,
            long[] values
    ) {
        long distanceSoFar = 0;

        for(int i = 0; i < values.length; i++) {
            long upDistance = tryUp(targetValue, values[i], modulus);
            long downDistance = tryDown(targetValue, values[i], modulus);
            if (upDistance < downDistance) {
                distanceSoFar += upDistance;
            } else {
                distanceSoFar += downDistance;
            }
        }

        return distanceSoFar;
    }

    private String solve(
            long w,
            long n,
            List<Long> rawValues
    ) {
        // translate from 1 to N to 0 to N-1 to make it easier to work with modulus N
        long[] values = toArray(rawValues);
        for(int i =0 ; i < values.length; i++) {
            values[i] = values[i] - 1;
        }

        // try a gradient descent solution
        // hoping there's a global max
        Deque<Work> queue = new ArrayDeque<>();
        Set<Long> tried = new HashSet<>();
        for(int i = 0; i < values.length; i++) {
            queue.add(new Work(values[i], Long.MAX_VALUE));
            tried.add(values[i]);
        }
        long minSoFar = Long.MAX_VALUE;
        while (!queue.isEmpty()) {
            Work work = queue.pollFirst();
            long distance = tryValue(work.comboToTry, n, values);
            if (distance <= work.scoreFromLastCombo) {
                final long upValueToTry;
                if (work.comboToTry == n - 1) {
                    upValueToTry = 0;
                } else {
                    upValueToTry = work.comboToTry + 1;
                }
                final long downValueToTry;
                if (work.comboToTry == 0) {
                    downValueToTry = n - 1;
                } else {
                    downValueToTry = work.comboToTry - 1;
                }

                if (!tried.contains(upValueToTry)) {
                    queue.add(new Work(upValueToTry, distance));
                    tried.add(upValueToTry);
                }
                if (!tried.contains(downValueToTry)) {
                    queue.add(new Work(downValueToTry, distance));
                    tried.add(downValueToTry);
                }
                if (distance < minSoFar) {
                    minSoFar = distance;
                }
            }
        }

        return String.valueOf(minSoFar);
    }

    private static class Work {
        private final long comboToTry;
        private final long scoreFromLastCombo;

        private Work(long comboToTry, long scoreFromLastCombo) {
            this.comboToTry = comboToTry;
            this.scoreFromLastCombo = scoreFromLastCombo;
        }
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long,Long> pair = parsePairLongLine();
        List<Long> values = parseLongListLine();
        String result = solve(
                pair.getFirst(),
                pair.getSecond(),
                values
        );
        writer.write(result);
        writer.write("\n");
    }

    public long[] toArray(List<Long> values) {
        long[] result = new long[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
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
