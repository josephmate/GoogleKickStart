import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

    public static long naive(int k, String s) {
      return Solution.generateAll(s.length(), k)
          .stream()
          .filter(Solution::isPalindrome)
          .filter(pali -> pali.compareTo(s) < 0)
          .count();
    }

    @Test
    public void test() {
      for (int k = 1; k <= 5; k++) {
        for (int n = 1; n <= 7; n++) {
          for (String s : Solution.generateAll(n, k)) {
            assertEquals(naive(5, s), Solution.solve(0, 5, s), s + " n=" + n + " k=" + k);
          }
        }
      }
    }
}