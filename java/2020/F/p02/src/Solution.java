import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    /**
     *
     * |   | |   |
     * 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0
     *
     *
     * @param numTimeIntervals
     * @param maxDeployTime
     * @param values
     * @return
     */
    public static long solve(
            long numTimeIntervals,
            long maxDeployTime,
            List<Pair<Long, Long>> values
    ) {
        if (numTimeIntervals == 0) {
            return 0;
        }

        List<Pair<Long, Long>> sortedIntervals = values.stream().sorted(Comparator.comparing(Pair::getFirst))
                .collect(Collectors.toList());

        int robotsConsumed = 1;
        int currentIdx = 0;
        long deployTimeRemaining = maxDeployTime;
        long currentTime = sortedIntervals.get(0).first;
        while(currentIdx < sortedIntervals.size()) {
            Pair<Long, Long> currentInterval = values.get(currentIdx);

            long timeLeft = currentInterval.second - currentInterval.first;
            // need to consume time leftover from previous interval
            long timeUntilNextInterval = currentInterval.first - currentTime;
            deployTimeRemaining = deployTimeRemaining - timeUntilNextInterval;
            if (deployTimeRemaining < 0) {
                deployTimeRemaining = 0;
            }

            // consumed the time interval
            while (timeLeft > 0) {
                if (deployTimeRemaining > timeLeft) {
                    deployTimeRemaining = deployTimeRemaining - timeLeft;
                    timeLeft = 0;
                } else if (deployTimeRemaining == timeLeft) {
                    deployTimeRemaining = 0;
                    timeLeft = 0;
                } else {
                    timeLeft = timeLeft - deployTimeRemaining;
                    robotsConsumed++;
                    deployTimeRemaining = maxDeployTime;
                }
            }
            currentTime = currentInterval.second;
            currentIdx++;
        }

        return robotsConsumed;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long,Long> pair = parsePairLongLine();
        List<Pair<Long, Long>> values = parseManyLongPairs(pair.first.intValue());
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

    public static final class Pair<X, Y> {
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
