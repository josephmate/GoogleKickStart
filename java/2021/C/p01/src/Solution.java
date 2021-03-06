import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

  public static boolean isPalindrome(String str) {
    return new StringBuilder(str).reverse().toString().equals(str);
  }

  public static List<String> generateAll(int n, int k, List<String> str) {
    List<String> result = new ArrayList<>();
    if (n == str.size()) {
      StringBuilder b = new StringBuilder();
      str.stream().forEach(b::append);
      result.add(b.toString());
      return result;
    }

    for (int i = 0; i < k; i++) {
      str.add("" + (char)(97+i));
      result.addAll(generateAll(n, k, str));
      str.remove(str.size()-1);
    }
    return result;
  }

  public static List<String> generateAll(int n, int k) {
    return generateAll(n, k, new ArrayList<>());
  }

  public static long naive(int k, String s) {
    return generateAll(s.length(), k)
        .stream()
        .filter(Solution::isPalindrome)
        .filter(pali -> pali.compareTo(s) < 0)
        .count();
  }

  public static long solveImpl(
      int k,
      int currentPosn,
      int midPosn,
      char [] str
  ) {
    if (str.length == 0) {
      return 0;
    }

    if (currentPosn == midPosn) {
      for (int i = midPosn-1; i >= 0; i--) {
        final char a = str[i];
        final char b = str[str.length-i-1];
        if (a > b) {
          return 0;
        } else if (a < b) {
          return 1;
        }
      }
      return 0;
    }

    final char a = str[currentPosn];
    final char b = str[str.length-currentPosn-1];
    if (b < a) {
      // in this case we are always smaller, so the remaining characters are free to be whatever
      // they want
      final long allFreeCount = ((b - 97 + 1) * pow(k, midPosn - currentPosn - 1, 1000000007)) % 1000000007;
      return allFreeCount;
    } else {
      // in this case we have two sub cases
      // 1) when the character is the same as a
      // 2) when the character is less than a

      // 1) when the character is the same as a, we solve the sub problem
      final long sameAsACount = solveImpl(k, currentPosn+1, midPosn, str);

      // 2) when the character is less than a, the remaining characters are free to be whatever
      // they want to be.
      final long numCharsLessThanA = a-97;
      final long numFreeCharacters = (numCharsLessThanA * pow(k, midPosn - currentPosn -1, 1000000007)) % 1000000007;

      return (sameAsACount + numFreeCharacters) % 1000000007;
    }
  }

  private static long solveSmall(int k, String str) {
    return naive(k, str);
  }

  /**
   * Size 1
   * s1
   * x1
   * then
   * x1 < s1
   * so max(s1-x1, 0)
   *
   * Size 2
   * s1 s2
   * x1 x1
   * then
   *    1) x1 < s1
   *    2) x1 = s1 and x1 < s1
   * so
   *    1) max(s1 - x1, 0) * k^0
   *    2) if s1 < s2 then 1
   *       else s1 = x1 >= s2 therefore 0
   *
   * Size 3
   * s1 s2 s3
   * x1 x2 x1
   * then
   *      1) x1 < s1
   *      2) x1 = s1 and x2 < s2
   *      3) x1 = s1 and x2 = s2 and s1 < s3
   * so
   *      1) max(s1-x1, 0) * k^1
   *      2) max(s2-x2, 0) * k^0
   *      3) if s1 < s3 then 1
   *         else s1 = x1 >= s3 then 0
   * Size 4
   * s1 s2 s3 s4
   * x1 x2 x2 x1
   * then
   *    1) x1 < x2
   *    2) x1 = s1 and x2 < s2
   *    3) x1 = s1 and x2 = s2 and (s2 < s3 || (s2 = s3 and s1 < s4))
   * so
   *    1) max(s1-x1, 0) * k^1
   *    2) max(s2-x2, 0) * k^0
   *    3) if s1 < s4 or (s2 = s3 and s1 < s4)
   *       else 0
   *
   * Size 5
   * s1 s2 s3 s4 s5
   * x1 x2 x3 x2 x1
   * then
   *    1) x1 < s1
   *    2) x1 = s1 and x2 < s2
   *    3) x1 = s1 and x2 = s2 and x3 < s3
   *    4) x1 = s1 and x2 = s3 and x3 = x3
   *       and (
   *          s2 < s4
   *          or s2 = s4 and s1 < s5
   *       )
   */
  public static long solve(
      long n,
      long k,
      String str
  ) {
    if (str.length() <= 3) {
      return solveSmall((int)k, str);
    }

    final int midPosn;
    if (str.length() % 2 == 0) {
      midPosn = str.length() / 2;
    } else {
      midPosn = 1 + str.length() / 2;
    }
    return solveImpl((int)k, 0, midPosn, str.toCharArray());
    //return naive((int)k, str);
  }

  private void handleTestCase(int testCase) throws IOException {
    writer.write("Case #" + testCase + ": ");
    Pair<Long, Long> pair = parsePairLongLine();
    writer.write(String.valueOf(solve(pair.first, pair.second, parseStringLine())));
    writer.write("\n");
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
