import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

    @Test
    public void upperBoundCakes() {
        long max = 0;
        for(int i = 1; i<= 10000; i++) {
            max = Math.max(max, Solution.solve(i));
        }
        System.out.println(max + " upperBoundCakes");
    }

    @Test
    public void runtimeBruteForce1() {
        double count = 1;
        for(long i = 1; i <= 100; i++) {
            count *= 10000L/(i*i);
        }
        System.out.println(count + " operations of runtimeBruteForce1");
    }

    public long countOps(long base, long remaining) {
        if (remaining == 0) {
            return 1;
        }
        long total = 0;
        for(long i = 1; i*base*base <= remaining; i++) {
            total += countOps(base+1, remaining-(i*base*base));
        }
        if (total == 0) {
            return 1;
        }
        return total;
    }

    /**
     * takes too long to compute.
     */
    @Test
    public void runtimeBruteForceOptimized() {
        System.out.println(countOps(1L, 500) + " operations of runtimeBruteForceOptimized");
    }

    @Test
    public void testSample() {
        assertEquals(3, Solution.solve(3));
        assertEquals(1, Solution.solve(4));
        assertEquals(2, Solution.solve(5));
    }

    @Test
    public void testPerfectSquares() {
        for (int i = 1; i <= 1000; i++) {
            assertEquals(1, Solution.solve(i*i));
        }
    }

    @Test
    public void testPairOfSquares() {
        assertEquals(2, Solution.solve(3*3 + 3*3));
    }

    private boolean isASquare(long value) {
        double sqrt = Math.sqrt(value);
        return Math.abs(sqrt - Math.floor(sqrt)) < 0.00001;
    }

    @Test
    public void testManyPairsOfSquares() {
        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 100; j++) {
                if(!isASquare(i*i + j*j)) {
                    assertEquals(2, Solution.solve(i * i + j * j), i + " " + j);
                }
            }
        }
    }
}