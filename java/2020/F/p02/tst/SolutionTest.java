import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SolutionTest {

    private static Solution.Pair<Long,Long> pair(long a, long b) {
        return new Solution.Pair<>(a ,b);
    }

    private void check(long expected, long maxDeployTime, Solution.Pair<Long,Long> ... values) {
        Assert.assertEquals(
                expected,
                Solution.solve(values.length, maxDeployTime,
                        Arrays.asList(values))
        );
    }

    @Test
    public void solve1() {
        check(2,
                5,
                pair(1, 5),
                pair(10, 11),
                pair(8, 9)
                );
    }

    @Test
    public void solve2() {
        check(3,
                2,
                pair(1, 2),
                pair(3, 5),
                pair(13, 14)
        );
    }

    @Test
    public void solve3() {
        check(1,
                10,
                pair(1, 2),
                pair(2, 3),
                pair(5, 11)
        );
    }

    @Test
    public void solve4() {
        check(2,
                10,
                pair(1, 2),
                pair(2, 3),
                pair(5, 12)
        );
    }

    @Test
    public void solve5() {
        check(1,
                10,
                pair(1, 11)
        );
    }

    @Test
    public void solve6() {
        check(6,
                1,
                pair(1, 2),
                pair(2, 3),
                pair(4, 5),
                pair(7, 8),
                pair(8, 10)
        );
    }

    @Test
    public void solve7() {
        check(2,
                4,
                pair(1, 2),
                pair(2, 3),
                pair(4, 5),
                pair(7, 8),
                pair(8, 10)
        );
    }

    @Test
    public void solve8() {
        check(2,
                4,
                pair(1, 2),
                pair(2, 3),
                pair(4, 5),
                pair(7, 8),
                pair(8, 11)
        );
    }

    @Test
    public void solve9() {
        check(3,
                4,
                pair(1, 2),
                pair(2, 3),
                pair(4, 5),
                pair(7, 8),
                pair(8, 12)
        );
    }

}
