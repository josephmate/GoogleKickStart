package com.josephmate.google.kickstart

import java.io.InputStream
import java.io.PrintStream
import java.util.*


class MuralSolver() {

    fun solve(input: InputStream, output: PrintStream) {
        val scanner = Scanner(input);
        val numTestCases = scanner.nextInt();
        for(testCase in 1..numTestCases) {
            val result = 0
            val numOfDigits = scanner.nextInt()
            scanner.nextLine() // read the rest of the line
            val line = scanner.nextLine()
            val problem = Problem(numOfDigits, line)
            output.println("Case #$testCase: $result $line")
        }
    }
}

class Problem(numOfDigits: Int, wallBeauty: String) {

}

fun main(args: Array<String>) {
    MuralSolver().solve(System.`in`, System.out)
}
