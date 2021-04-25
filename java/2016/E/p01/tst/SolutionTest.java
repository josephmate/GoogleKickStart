import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

    @Test
    public void test() {
        assertEquals(2, Solution.solve("RBB", 1, 3));
        assertEquals(1, Solution.solve("RBB", 1, 2));
        assertEquals(2, Solution.solve("RBB", 2, 3));
        assertEquals(0, Solution.solve("RBB", 1, 1));
        assertEquals(1, Solution.solve("RBB", 2, 2));
        assertEquals(1, Solution.solve("RBB", 3, 3));
    }

    @Test
    public void testExact() {
        assertEquals(2, Solution.solve("RBB", 4, 6));
        assertEquals(4, Solution.solve("RBB", 4, 9));
        assertEquals(6, Solution.solve("RBB", 4, 12));
    }

    @Test
    public void testFrontCut() {
        assertEquals(1, Solution.solve("BRB", 5, 6));
        assertEquals(3, Solution.solve("BRB", 5, 9));
        assertEquals(5, Solution.solve("BRB", 5, 12));
    }

    @Test
    public void testEndCut() {
        assertEquals(1, Solution.solve("BRB", 4, 5));
        assertEquals(3, Solution.solve("BRB", 4, 8));
        assertEquals(5, Solution.solve("BRB", 4, 11));
    }

    @Test
    public void testBothCut() {
        assertEquals(3, Solution.solve("BRBRB", 6, 10));
        assertEquals(1, Solution.solve("BRBRB", 7, 9));
        assertEquals(6, Solution.solve("BRBRB", 6, 15));
        assertEquals(4, Solution.solve("BRBRB", 7, 14));
        assertEquals(9, Solution.solve("BRBRB", 6, 20));
        assertEquals(7, Solution.solve("BRBRB", 7, 19));
    }

    /**
     *           1         2
     * 012345678901234567890123456789
     * BRBRBRBRRBBRBRBRBRRBBRBRBRBRRB
     */
    @Test
    public void testWrapPattern() {
        assertEquals(3, Solution.solve("BRBRBRBRRB", 18, 23));
    }

    public int solveNaive(
            String pattern,
            final int i,
            final int j
    ) {
        final int start = i - 1;
        final int end = j;
        final StringBuilder builder = new StringBuilder();
        while(builder.length() < j) {
            builder.append(pattern);
        }
        final String expandedPattern = builder.toString();
        int bCount = 0;
        for(int idx = start; idx < end; idx++) {
            if (expandedPattern.charAt(idx) == 'B') {
                bCount++;
            }
        }
        return bCount;
    }

    private List<String> generatePatterns(
            List<String> previous
    ) {
        List<String> result = new ArrayList<>();
        if (previous.isEmpty()) {
            result.add("B");
            result.add("R");
            return result;
        }

        result = new ArrayList<>();
        for(String pattern: previous) {
            result.add(pattern + "B");
            result.add(pattern + "R");
        }
        return result;
    }

    @Test
    public void bruteForceTest() {
        List<String> patterns = new ArrayList<>();
        for (int patternLen = 1; patternLen <= 10; patternLen++) {
            long t1 = System.currentTimeMillis();
            patterns = generatePatterns(patterns);
            long t2 = System.currentTimeMillis();
            System.out.println(t2-t1);


            long t3 = System.currentTimeMillis();
            for (String pattern : patterns) {
                for (int i = 1; i <= patternLen * 5; i++) {
                    for (int j = i; j <= patternLen * 5; j++) {
                        assertEquals(solveNaive(pattern, i, j), Solution.solve(pattern, i, j),
                                pattern + " i=" + i + " j=" + j);
                    }
                }
            }
            long t4 = System.currentTimeMillis();
            System.out.println(t4-t3);
            System.out.println(patternLen);
        }
    }

}