import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

    private static void  permutateImpl(long n, long m, List<List<Character>> result, List<Character> temp) {
        if (n == 0 && m == 0) {
            result.add(new ArrayList<>(temp));
        }
        if(n > 0) {
            temp.add('A');
            permutateImpl(n-1, m, result, temp);
            temp.remove(temp.size()-1);
        }
        if(m > 0) {
            temp.add('B');
            permutateImpl(n, m-1, result, temp);
            temp.remove(temp.size()-1);
        }
    }

    private static List<List<Character>> permutate(long n, long m) {
        List<List<Character>> result = new ArrayList<>();
        List<Character> temp = new ArrayList<>();
        permutateImpl(n, m, result, temp);
        return result;
    }

    private static boolean isAlwaysWinning(List<Character> chars) {
        if (chars.isEmpty()) {
            return false;
        }
        if (chars.get(0).equals('B')) {
            return false;
        }
        long aCount = 1;
        long bCount = 0;
        for (int i = 1; i < chars.size(); i++) {
            char c = chars.get(i);
            if (c == 'A') {
                aCount++;
            } else {
                bCount++;
            }
            if (bCount >= aCount) {
                return false;
            }
        }
        return true;
    }

    private static double solvePermutate(long n, long m) {
        List<List<Character>> permutations = permutate(n, m);
        long alwaysWinningCount = 0;
        for(List<Character> perm : permutations) {
            if(isAlwaysWinning(perm)) {
                alwaysWinningCount++;
            }
        }
        return (double)alwaysWinningCount / (double)permutations.size();
    }

    @Test
    public void calcSmall() {
        for(int n = 0; n <= 5; n++) {
            for(int m = 0; m <= 5; m++) {
                System.out.println(n + " " + m + " " + solvePermutate(n, m));
            }
        }
    }
}