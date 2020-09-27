import java.io.*;
import java.util.*;

public class Solution {

    private int findMin(
            int startIdx,
            long maxWithdraw,
            List<Long> forbiddenSequences) {

        int visited = 0;
        int currentIdx = startIdx;
        long minNumOfTurns = Long.MAX_VALUE;
        int minIdx = -1;

        while(visited < forbiddenSequences.size()) {
            long currentValue = forbiddenSequences.get(currentIdx);
            long numOfTurns = currentValue / maxWithdraw;
            if (currentValue % maxWithdraw > 0) {
                numOfTurns++;
            }
            if (currentValue > 0 && numOfTurns < minNumOfTurns) {
                minIdx = currentIdx;
                minNumOfTurns = numOfTurns;
            }

            visited++;
            currentIdx++;
            if (currentIdx >= forbiddenSequences.size()) {
                currentIdx = 0;
            }
        }

        return minIdx;
    }

    private void reduceWithdrawls(
            int start,
            int end,
            long numOfTurns,
            long maxWithdraw,
            List<Long> amountToWithdraw) {
        long valueToReduceBy = numOfTurns*maxWithdraw;
        int visited = 0;
        int currentIdx = start;
        while (visited < amountToWithdraw.size()) {
            amountToWithdraw.set(currentIdx, amountToWithdraw.get(currentIdx) - valueToReduceBy);

            if (currentIdx == end) {
                currentIdx++;
                visited++;

                if (currentIdx >= amountToWithdraw.size()) {
                    currentIdx = 0;
                }
                break;
            }

            currentIdx++;
            visited++;
            if (currentIdx >= amountToWithdraw.size()) {
                currentIdx = 0;
            }
        }

        if (numOfTurns > 1) {
            numOfTurns--;
            valueToReduceBy = numOfTurns*maxWithdraw;
            while (visited < amountToWithdraw.size()) {
                amountToWithdraw.set(currentIdx, amountToWithdraw.get(currentIdx) - valueToReduceBy);
                currentIdx++;
                visited++;
                if (currentIdx >= amountToWithdraw.size()) {
                    currentIdx = 0;
                }
            }
        }
    }

    private String solve(
            long numPeople,
            long maxWithdraw,
            List<Long> amountToWithdraw
    ) {
        int currentCustomer = 0;
        List<Integer> exited = new ArrayList<>();
        while(exited.size() < numPeople) {
            int nextDoneIdx = findMin(currentCustomer, maxWithdraw, amountToWithdraw);
            long totalWithdrawAmount = amountToWithdraw.get(nextDoneIdx);
            long numberOfTurnsNeed = totalWithdrawAmount / maxWithdraw;
            if (totalWithdrawAmount % maxWithdraw > 0) {
                numberOfTurnsNeed++;
            }
            reduceWithdrawls(currentCustomer, nextDoneIdx, numberOfTurnsNeed, maxWithdraw, amountToWithdraw);
            exited.add(nextDoneIdx);
            currentCustomer = nextDoneIdx;
        }

        StringBuilder result = new StringBuilder();
        boolean first = false;
        for(int exitee : exited) {
            if (first) {
                first = false;
            } else {
                result.append(" ");
            }
            result.append((exitee + 1));
        }

        return result.toString();
    }
    /*
        Map<Long, Long> remainingToWithdraw = new HashMap<>();
        Deque<Long> queue = new ArrayDeque<>();
        for(int i = 0; i < numPeople; i++) {
            queue.add(new Long(i));
            remainingToWithdraw.put(new Long(i), forbiddenSequences.get(i));
        }

        List<Long> exited = new ArrayList<>();
        while(!queue.isEmpty()) {
            long currentCustomer = queue.pollFirst();
            long remaining = remainingToWithdraw.get(currentCustomer) - maxWithdraw;
            if (remaining > 0) {
                remainingToWithdraw.put(currentCustomer, remaining);
                queue.add(currentCustomer);
            } else {
                exited.add(currentCustomer);
            }
        }
     */
    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long,Long> pair = parsePairLongLine();
        List<Long> values = parseLongListLine();
        String result = solve(
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
