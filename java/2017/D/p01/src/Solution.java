import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

  /**
   * Solution 1: Naive
   * Since the small input only has 16 cities,
   * we can try all possible combinations in
   * 2^16 time, well within the 20 second timelimit.
   *
   * This solution will fail on the large input set with a runtime of 2^2000.
   *
   * Solution 2: Work it out backwards
   * I solved a similar problem in the past were I could start from the end state and work it out
   * backwards (2020 B P02: Bus Routes). Maybe that can be done here.
   *
   * However, I have doubts that this solution will work because of a key difference: in this
   * problem we have the option of sight seeing or not.
   *
   * Solution 3: Greedy Earliest arrival time
   * Using the bus route solution, we can figure out the earliest time to arrive.
   * Then from there, we can incrementally determine if we can add on more sight seeing.
   *
   * Maybe a greedy solution will work here where we add the min time usage?
   * However, I'm not confident that a greedy solution will work because add 1 sight seeing could
   * have a change reaction on all the cities after causing us to take a later bus.
   * Similarly if we try a different greedy strategy where we start from furthest city,
   * it might have been more beneficial to go with a closer city.
   *
   * On second thought, maybe the greedy strategy to add the furthest city make sense. Assume that
   * an earlier city would have been better, then the sight seeing delay is at least Ts, which is
   * still the same delay at the furthest city, with risk of missing more buses.
   *
   * I will try to code up this solution and see if it works.
   *
   * After more thinking about it, the belief that pick any closer city, than the furthest city would
   * result in a delay in at least the delay as long as picking the furthest is incorrect because,
   * if the sight seeing time fits within the delay of waiting for the bus, that would be better than
   * picking the furthest city!
   *
   * Solution 4: Dynamic Programming
   * Knowing the solution to a problem of N-1 does not help you solve a problem of N.
   * Assume you know the max sight seeing for N-3 (3 cities removed.). You cannot use that solution
   * to solve N-2 because you could potentially remove the sight seeing from the first city, giving
   * a sub optimal for N-3, but allows you to include cities N-2, N-1, and N.
   *
   * The sub problems in the other directions has a similar problem. Assume you have a solution for
   * 4 to N, then removing something from 4 to N could potentially allow you to sightsee at 1, 2 and 3.
   *
   * Solution 5: Divide and Conquer
   * Assume you can check if x cities fit in F(N) time.
   * Binary search on N
   * ex:
   *   Try N/2 sight seeing
   *    if yes then 3N/4
   *      etc
   *    if no try N/4
   *      etc
   * In total this would take lgN * F(N) time
   *
   * How quickly can we check if x cities fit?
   * Naive would be to try all N choose x combos (maximized by x=N/2) which I think has a complexity
   * that too large.
   *
   * At this point, I gave up and checking the analysis that google provided.
   */
  public static Optional<Long> solveImpl(
      final long sightSeeingTime,
      final long latestArrivalTime,
      final List<Triple<Long, Long, Long>> cities
  ) {
    return Optional.empty();
  }

  public static String solve(
      long sightSeeingTime,
      long latestArrivalTime,
      List<Triple<Long, Long, Long>> cities
  ) {
    return solveImpl(sightSeeingTime, latestArrivalTime, cities)
        .map(String::valueOf)
        .orElse("IMPOSSIBLE");
  }

  private void handleTestCase(int testCase) throws IOException {
    writer.write("Case #" + testCase + ": ");
    Triple<Long, Long, Long> triple = parseTripleLongLine();
    int numCities = triple.first.intValue();
    List<Triple<Long, Long, Long>> cities = parseManyLongTriples(numCities - 1);
    writer.write(String.valueOf(solve(triple.second, triple.third, cities)));
    writer.write("\n");
  }

  public String longArrayToString(Collection<Long> longArray) {
    return longArray.stream()
	    .map(l -> l.toString())
	    .collect(Collectors.joining(" "));
  }

  public void parseAndSolveProblems() throws IOException {
    long testCases = parseLongLine();
    for (int i = 1; i <= testCases; i++) {
      handleTestCase(i);
    }
  }

  private static long pow(long base, long exponent, long mod) {
    long result = 1;
    for (int i = 0; i < exponent; i++)  {
      result = (result * base) % mod;
    }
    return result;
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

  private List<Triple<Long,Long,Long>> parseManyLongTriples(int numPairs) throws IOException {
    List<Triple<Long,Long,Long>> result = new ArrayList<>(numPairs);
    for (long i = 0; i < numPairs; i++) {
      result.add(parseTripleLongLine());
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

  private Triple<Long, Long, Long> parseTripleLongLine() throws IOException {
    try {
      String line = reader.readLine().trim();
      String [] columns = line.split(" ");
      long first = Long.parseLong(columns[0]);
      long second = Long.parseLong(columns[1]);
      long third = Long.parseLong(columns[2]);
      currentLine++;
      return new Triple<>(first, second, third);
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
