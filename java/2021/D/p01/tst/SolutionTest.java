import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

    @Test
    public void test() {
      for (int k = 1; k <= 5; k++) {
        for (int n = 1; n <= 7; n++) {
          for (String s : Solution.generateAll(n, k)) {
            String message = s + " n=" + n + " k=" + k;
            try {
              assertEquals(Solution.naive(k, s), Solution.solve(n, k, s), message);
            } catch (Exception e) {
              System.out.println(message);
              throw e;
            }
          }
        }
      }
    }

  @Test
  public void testFailingCase() {
    String s = "abb";
    int n = 3;
    int k = 2;
    assertEquals(Solution.naive(k, s), Solution.solve(n, k, s), s + " n=" + n + " k=" + k);
  }

  @Test
  public void testFailingCase2() {
    String s = "abba";
    int n = 4;
    int k = 2;
    assertEquals(Solution.naive(k, s), Solution.solve(n, k, s), s + " n=" + n + " k=" + k);
  }

  @Test
  public void testFailingCase3() {
    String s = "aaba";
    int n = 4;
    int k = 2;
    assertEquals(Solution.naive(k, s), Solution.solve(n, k, s), s + " n=" + n + " k=" + k);
  }

  @Test
  public void testFailingCase4() {
    String s = "baba";
    int n = 4;
    int k = 2;
    assertEquals(Solution.naive(k, s), Solution.solve(n, k, s), s + " n=" + n + " k=" + k);
  }
}