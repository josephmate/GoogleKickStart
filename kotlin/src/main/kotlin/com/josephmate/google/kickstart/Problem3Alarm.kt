package com.josephmate.google.kickstart

import java.io.InputStream
import java.io.PrintStream
import java.util.*
import kotlin.collections.HashMap

class AlarmSolver() {
    fun solve(input: InputStream, output: PrintStream) {
        val scanner = Scanner(input);
        val numTestCases = scanner.nextInt();
        for(testCase in 1..numTestCases) {
            val arrayLen =  scanner.nextInt();
            val wakeupCalls =  scanner.nextInt();
            val x1 =  scanner.nextInt();
            val y1 =  scanner.nextInt();
            val c =  scanner.nextInt();
            val d =  scanner.nextInt();
            val e1 =  scanner.nextInt();
            val e2 =  scanner.nextInt();
            val f =  scanner.nextInt();
            val solution = impl(arrayLen, wakeupCalls, x1, y1, c, d, e1, e2, f)
            output.println("Case #$testCase: $arrayLen $wakeupCalls $x1 $y1 $c $d $e1 $e2 $f")
        }
    }

    /**
     * Limits
     * 1 ≤ T ≤ 100.
     * Time limit: 90 seconds per test set.
     * Memory limit: 1 GB.
     * 1 ≤ x1 ≤ 10^5.
     * 1 ≤ y1 ≤ 10^5
     * 1 ≤ C ≤ 10^5.
     * 1 ≤ D ≤ 10^5.
     * 1 ≤ E1 ≤ 10^5.
     * 1 ≤ E2 ≤ 10^5.
     * 1 ≤ F ≤ 10^5.
     *
     * Manually calculate
     * A = 2
     * K = 3
     * x1 = 1
     * y1 = 2
     * c 1
     * d 2
     * e1 1
     * e2 1
     * f 9
     *
     * x2 = (c * x1 + d * y1 + e1) %f
     *    = (1 *  1 + 2 * 2 + 1 )  % 9
     *    = 6
     * y2 = (d * x[i-1] + c*y[i-1] + e2) % f
     *    = (2 * 1      + 1 * 2 + 1 ) % 9
     *    = 5
     * A1 = (x1 + y1) % f
     *    = (1 + 2) % 9
     *    = 3
     * A2 = (x2 + y2) % f
     *    = (6 + 5) % 9
     *    = 2
     * 1st power
     * A = [3 2]
     * 1st power ( [3] )   = 3 * 1^1
     *                     = 3
     * 1st power ( [2] )   = 2 * 1^1
     *                     = 2
     * 1st power ( [3 2] ) = 3 * 1^2 + 2 * 2 ^ 1
     *                     = 3 + 4
     *                     = 7
     * sum of 1st power is 12
     * 2nd power
     * [3]   = 3 * 1^2
     *       = 3
     * [2]   = 2 * 1^2
     *       = 2
     * [3 2] = 3 * 1^2 + 2 * 2^2
     *       = 3 + 8
     *       = 11
     * sum of 2nd power is 16
     *
     * 3rd power
     * [3]   = 3 * 1^3
     *       = 3
     * [2]   = 2 * 1^3
     *       = 2
     * [3 2] = 3 * 1^3 + 2 * 2^3
     *       = 3 + 16
     *       = 19
     * sum of 3rd power is 24
     *
     * sum if powers = 12 + 16 + 24
     *               = 52
     */
    private fun impl(arrayLen: Int, wakeupCalls: Int, x1: Int, y1: Int, c: Int, d: Int, e1: Int, e2: Int, f: Int): Int  {
        val alarms = impl(arrayLen, wakeupCalls, x1, y1, c, d, e1, e2, f)



        // max signed integer 2,147,483,647
        // solution is modulo 1,000,000,007
        // so we can use signed integer for this problem
        return 10 % 1000000007
    }

    private fun calcAlarms(arrayLen: Int, wakeupCalls: Int, x1: Int, y1: Int, c: Int, d: Int, e1: Int, e2: Int, f: Int): IntArray {
        val x = IntArray(arrayLen)
        x[0] = x1
        val y = IntArray(arrayLen)
        y[0] = y1
        val A = IntArray(arrayLen)
        A[0] = (x[0] + y[0]) % f
        for(i in 1 until arrayLen) {
            x[i] = (c * x[i-1] + d*y[i-1] + e1) % f
            y[i] = (d * x[i-1] + c*y[i-1] + e2) % f
            A[i] = (x[i] + y[i]) % f
        }

        return A
    }
}




fun main(args: Array<String>) {
    AlarmSolver().solve(System.`in`, System.out)
}
