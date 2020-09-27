import java.io.*;
import java.util.*;

public class Solution {

    private int findMinId(
            long maxWithdraw,
            Deque<Integer> queue,
            Map<Integer, Long> bankMap) {

        int minId = -1;
        long minNumOfTurns = Long.MAX_VALUE;
        for(int bankId : queue) {
            long currentValue = bankMap.get(bankId);
            long numOfTurns = currentValue / maxWithdraw;
            if (currentValue % maxWithdraw > 0) {
                numOfTurns++;
            }

            // otherwise O(N^2) without this
            // might have a chain of ones that work
            if (numOfTurns == 1) {
                return bankId;
            }

            if (currentValue > 0 && numOfTurns < minNumOfTurns) {
                minId = bankId;
                minNumOfTurns = numOfTurns;
            }
        }

        return minId;
    }

    private void reduceWithdrawls(
            Deque<Integer> queue,
            long nextDoneId,
            long numOfTurns,
            long maxWithdraw,
            Map<Integer, Long> bankMap) {
        long valueToReduceBy = numOfTurns*maxWithdraw;
        int visited = 0;

        Deque<Integer> thingsToAddBack = new ArrayDeque<>();

        while (true) {
            final int currentId = queue.pollFirst();
            bankMap.put(currentId, bankMap.get(currentId) - valueToReduceBy);

            // reached the instances that need to fully decrement
            // do not add back this id, because it needs to be removed
            // it's already at 0 dollars.
            if (currentId == nextDoneId) {
                break;
            }

            thingsToAddBack.add(currentId);
        }

        if (numOfTurns > 1) {
            numOfTurns--;
            valueToReduceBy = numOfTurns*maxWithdraw;

            for(int currentId : queue) {
                bankMap.put(currentId, bankMap.get(currentId) - valueToReduceBy);
            }
        }

        queue.addAll(thingsToAddBack);
    }

    private String solve(
            long numPeople,
            long maxWithdraw,
            List<Long> amountToWithdraw
    ) {
        Deque<Integer> queue = new ArrayDeque<>();
        Map<Integer, Long> bankMap = new HashMap<>();
        for (int i = 0; i < numPeople; i++) {
            queue.add(i);
            bankMap.put(i, amountToWithdraw.get(i));
        }

        List<Integer> exited = new ArrayList<>();
        while(exited.size() < numPeople) {
            int nextDoneId = findMinId(maxWithdraw,
                    queue,
                    bankMap);

            long totalWithdrawAmount = bankMap.get(nextDoneId);
            long numberOfTurnsNeed = totalWithdrawAmount / maxWithdraw;
            if (totalWithdrawAmount % maxWithdraw > 0) {
                numberOfTurnsNeed++;
            }

            reduceWithdrawls(queue, nextDoneId, numberOfTurnsNeed, maxWithdraw, bankMap);
            exited.add(nextDoneId);
        }

        StringBuilder result = new StringBuilder();
        boolean first = false;
        for(int exiter : exited) {
            if (first) {
                first = false;
            } else {
                result.append(" ");
            }
            result.append((exiter + 1));
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
