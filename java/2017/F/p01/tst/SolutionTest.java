import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

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

    @Test
    public void testManyPairsOfSquares() {
        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= 1000; j++) {
                assertEquals(2, Solution.solve(i*i + j*j), i + " " + j);
            }
        }
    }
}