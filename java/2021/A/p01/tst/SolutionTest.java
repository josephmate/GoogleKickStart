import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolutionTest {

    @Test
    public void testSample() {
        assertEquals(0, Solution.solve(5, 1, "ABCAA"));
        assertEquals(1, Solution.solve(4, 2, "ABAA"));
    }

    @Test
    public void test() {
        assertEquals(0, Solution.solve(3, 1, "ABC"));
        assertEquals(1, Solution.solve(3, 1, "ABA"));
    }

}