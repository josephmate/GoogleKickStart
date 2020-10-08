import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private long findACycleMember(Map<Long, Set<Long>> adjacency) {
        Set<Long> visited = new HashSet<>();
        Deque<Pair<Long,Long>> queue = new ArrayDeque<>();
        queue.add(new Pair<>(adjacency.keySet().iterator().next(), null));

        while(!queue.isEmpty()) {
            Pair<Long,Long> currentPair = queue.pollFirst();
            long current = currentPair.first;
            Long previous = currentPair.second;
            if (visited.contains(current)) {
                return current;
            }

            visited.add(current);
            Set<Long> next = adjacency.get(current);
            if (next != null) {
                next.stream()
                    .filter(val -> previous == null || !previous.equals(val))
                    .map(val -> new Pair<>(val, current))
                    .forEach(queue::add);
            }
        }

        throw new IllegalStateException("Expected to find a cycle with len > 1");
    }

    private Set<Long> cycleMembers(long cycleMember, Map<Long, Set<Long>> adjacency) {
        Deque<Triple<Long,Long, Set<Long>>> queue = new ArrayDeque<>();
        queue.add(new Triple<>(cycleMember, null, new HashSet<>()));

        while(!queue.isEmpty()) {
            Triple<Long,Long, Set<Long>> currentTriple = queue.pollFirst();
            long current = currentTriple.first;
            Long previous = currentTriple.second;
            Set<Long> cycleSoFar = currentTriple.third;
            if (cycleSoFar.contains(current)) {
                return cycleSoFar;
            }

            Set<Long> next = adjacency.get(current);
            if (next != null) {
                Set<Long> biggerCycle = new HashSet<>(cycleSoFar);
                biggerCycle.add(current);
                next.stream()
                        .filter(val -> previous == null || !previous.equals(val))
                        .map(val -> new Triple<>(val, current, biggerCycle))
                        .forEach(queue::add);
            }
        }

        throw new IllegalStateException("Expected to find a cycle with len > 1");
    }

    private long distanceToCycle(
            long start,
            Set<Long> cycleMembers,
            Map<Long, Set<Long>> adjacency) {
        Set<Long> visited = new HashSet<>();
        Deque<Triple<Long,Long, Long>> queue = new ArrayDeque<>();
        queue.add(new Triple<>(start, null, 0L));


        while(!queue.isEmpty()) {
            Triple<Long,Long,Long> currentTriple = queue.pollFirst();
            long current = currentTriple.first;
            Long previous = currentTriple.second;
            long distance = currentTriple.third;
            if (cycleMembers.contains(current)) {
                return distance;
            }
            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            Set<Long> next = adjacency.get(current);
            if (next != null) {
                next.stream()
                        .filter(val -> previous == null || !previous.equals(val))
                        .map(val -> new Triple<>(val, current, distance + 1))
                        .forEach(queue::add);
            }
        }

        throw new IllegalStateException("Expected to find a cycle with len > 1");
    }

    private void add(Map<Long, Set<Long>> adjacency, long key, long value) {
        Set<Long> values = adjacency.get(key);
        if (values == null) {
            values = new HashSet<>();
            adjacency.put(key, values);
        }
        values.add(value);
    }

    private String solve(
            long numPlanets,
            List<Pair<Long, Long>> tubes
    ) {
        Map<Long, Set<Long>> adjacency = new HashMap<>();
        for (Pair<Long,Long> tube : tubes) {
            add(adjacency, tube.first, tube.second);
            add(adjacency, tube.second, tube.first);
        }

        long cycleMember = findACycleMember(adjacency);
        Set<Long> cycleMembers = cycleMembers(cycleMember, adjacency);

        List<Long> result = new ArrayList<>();
        for(int i = 1; i <= numPlanets; i++) {
            result.add(distanceToCycle(i, cycleMembers, adjacency));
        }

        return result.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        long n = parseLongLine();
        List<Pair<Long, Long>> values = parseManyLongPairs((int)n);
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
