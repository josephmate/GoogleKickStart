import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

  private static Set<Long> intersect(
          Set<Long> as,
          Set<Long> bs
  ) {
    Set<Long> intersection = new HashSet<>();
    for (Long a : as) {
      if (bs.contains(a)) {
        intersection.add(a);
      }
    }
    return intersection;
  }

  private static void addToMap(NavigableMap<Long, Set<Long>> map,
                   long key,
                   long value) {
    final Set<Long> values;
    if (map.containsKey(key)) {
      values = map.get(key);
    } else {
      values = new HashSet<>();
      map.put(key, values);
    }
    values.add(value);
  }

  /**
   *
   */
  public static long solve(
          long numDaysOpen,
          long n,
          long k,
          List<Triple<Long, Long, Long>> hses
  ) {
    Set<Long> potentialDays = new HashSet<>();
    NavigableMap<Long, Set<Long>> startDayToRides = new TreeMap<>();
    NavigableMap<Long, Set<Long>> endDayToRides = new TreeMap<>();
    for(int rideNum = 0; rideNum < hses.size(); rideNum++) {
      long start = hses.get(rideNum).second;
      long end = hses.get(rideNum).third;
      addToMap(startDayToRides, start, rideNum);
      addToMap(endDayToRides, end, rideNum);
      potentialDays.add(start);
      potentialDays.add(end);
    }

    long maxSoFar = 0;
    for (Long potentialDay : potentialDays) {
      Set<Long> ridesWithStartBeforeCurrentDay = startDayToRides.headMap(potentialDay, true)
              .values()
              .stream()
              .flatMap(Set::stream)
              .collect(Collectors.toSet());
      Set<Long> ridesWithEndBeforeCurrentDay = endDayToRides.tailMap(potentialDay, true)
              .values()
              .stream()
              .flatMap(Set::stream)
              .collect(Collectors.toSet());
      Set<Long> intersection = intersect(ridesWithEndBeforeCurrentDay, ridesWithStartBeforeCurrentDay);

      if (!intersection.isEmpty()) {
        // drop everything into a heap and get the k largest elements
        PriorityQueue<Long> heap = new PriorityQueue<>(intersection.size(), Comparator.reverseOrder());
        for(Long rideNum : intersection) {
          heap.add(hses.get(rideNum.intValue()).first);
        }

        int elements = 0;
        long scoreSoFar = 0;
        while (!heap.isEmpty() && elements < k) {
          scoreSoFar += heap.poll();
          elements++;
        }
        if (scoreSoFar > maxSoFar) {
          maxSoFar = scoreSoFar;
        }
      }
    }

    return maxSoFar;
  }

  private void handleTestCase(int testCase) throws IOException {
    writer.write("Case #" + testCase + ": ");
    Triple<Long,Long,Long> triple = parseTripleLongLine();
    long d = triple.first;
    long n = triple.second;
    long k = triple.third;
    List<Solution.Triple<Long, Long, Long>> triples = parseManyLongTriples((int)n);
    writer.write(String.valueOf(solve(
      d,
      n,
      k,
      triples
    )));
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
