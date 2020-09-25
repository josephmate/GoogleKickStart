import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  palindrome of odd length
 *  BAAAB
 *  Must have only one character with odd count, all other must be even
 *  If more than one character has odd count, then it's impossible to create a palindrome.
 *
 *  palindrome of even length
 *  ABBA
 *  If any are odd, then there are an even number of them.
 *  Then,Impossible to create a palindrome.
 *
 *  can pre compute prefixes and suffixes
 *  Ex:
 *  A
 *  A B
 *  A B C D
 *  A B C D E
 *          E
 *        D E
 *      C D E
 *    B C D E
 *  Now to make any count, we can do
 *  1 to 3 : already precomputed
 *  2 to 4 : 1 to 4 minus 1 to 1
 *  3 to 5 : 1 to 5 minus 1 to 2
 *  in general X Y can be calculated with:
 *  1 to Y minus 1 to (X - 1)
 */
public class Solution {

    private Map<Long, Map<Character, Long>> preComputePrefixCharCounts(String inputStr) {
        Map<Long, Map<Character, Long>> preComputedCounts = new HashMap<>();
        Map<Character, Long> accumulatedCounts = new HashMap<>();
        long position = 1;
        for (char character : inputStr.toCharArray()) {
            long count = accumulatedCounts.getOrDefault(character, 0L);
            count++;
            accumulatedCounts.put(character, count);
            // need to clone the map
            preComputedCounts.put(position, new HashMap<>(accumulatedCounts));
            position++;
        }
        return preComputedCounts;
    }

    private Map<Character, Long> subtractMaps(
            Map<Character, Long> bigMap,
            Map<Character, Long> smallMap
    ) {
        Map<Character, Long> result = new HashMap<>(bigMap);

        for(Map.Entry<Character, Long> entry : smallMap.entrySet()) {
            char character = entry.getKey();
            long smaller = entry.getValue();
            result.put(entry.getKey(),
                    bigMap.get(character) - smaller);
        }

        return result;
    }

    private boolean canPalindrome(
            long lowerBound,
            long upperBound,
            Map<Long, Map<Character, Long>> preComputedPrefixCharCounts
    ) {
        // 1 to upperBound
        final Map<Character, Long> bigMap = preComputedPrefixCharCounts.get(upperBound);
        // 1 to lower bound - 1
        final Map<Character, Long> smallMap;
        if (lowerBound <= 1) {
            smallMap = new HashMap<>();
        } else {
            smallMap =  preComputedPrefixCharCounts.get(lowerBound - 1);
        }
        final Map<Character, Long> subtractedMaps = subtractMaps(bigMap, smallMap);
        // number of odd must be less than equal to 1 to be able to make a palindrome
        return subtractedMaps.values().stream()
                .filter(value -> value % 2 == 1)
                .count() <= 1;
    }

    private long solve(
            long numBlocks,
            long numQuestions,
            String inputStr,
            List<Pair<Long, Long>> questions) {
        Map<Long, Map<Character, Long>> preComputedPrefixCharCounts = preComputePrefixCharCounts(inputStr);
        long totalPalindrome = 0;
        for (Pair<Long,Long> question : questions) {
            if (canPalindrome(question.first, question.second, preComputedPrefixCharCounts)) {
                totalPalindrome++;
            }
        }
        return totalPalindrome;
    }

    private void handleTestCase(int testCase) throws IOException {
        writer.write("Case #" + testCase + ": ");
        Pair<Long, Long> pair = parsePairLongLine();
        String inputStr = parseStringLine();
        List<Pair<Long, Long>> values = parseManyLongPairs(pair.second.intValue());
        long result = solve(
                pair.first,
                pair.second,
                inputStr,
                values
        );
        writer.write(String.valueOf(result));
        writer.write("\n");
    }

    public void parseAndSolveProblem() throws IOException {
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
            new Solution(reader, writer).parseAndSolveProblem();
        }
    }
}
