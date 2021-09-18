import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

    @Test
    public void test1() {
        final String actual = Solution.solve(
                3,
                12,
                Arrays.asList(
                        new Solution.Triple<>(3L, 2L, 1L),
                        new Solution.Triple<>(6L, 2L, 2L),
                        new Solution.Triple<>(1L, 3L, 2L)
                )
        );
        assertEquals("2", actual);
    }

    @Test
    public void test2() {
        final String actual = Solution.solve(
                2,
                30,
                Arrays.asList(
                        new Solution.Triple<>(1L, 2L, 27L),
                        new Solution.Triple<>(3L, 2L, 1L)
                )
        );
        assertEquals("0", actual);
    }

    @Test
    public void test3() {
        final String actual = Solution.solve(
                1,
                11,
                Arrays.asList(
                        new Solution.Triple<>(2L, 1L, 2L),
                        new Solution.Triple<>(4L, 1L, 5L),
                        new Solution.Triple<>(8L, 2L, 2L)
                )
        );
        assertEquals("IMPOSSIBLE", actual);
    }

    @Test
    public void test4() {
        final String actual = Solution.solve(
                10,
                5000,
                Arrays.asList(
                        new Solution.Triple<>(14L, 27L, 31L),
                        new Solution.Triple<>(27L, 11L, 44L),
                        new Solution.Triple<>(30L, 8L, 20L),
                        new Solution.Triple<>(2000L, 4000L, 3L)
                )
        );
        assertEquals("4", actual);
    }

}