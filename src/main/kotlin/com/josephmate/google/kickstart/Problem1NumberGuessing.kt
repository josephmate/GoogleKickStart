package com.josephmate.google.kickstart

import java.io.InputStream
import java.io.PrintStream
import java.util.*


class Solver() {

    fun solve(input: InputStream, output: PrintStream) {
        val scanner = Scanner(input);
        val numTestCases = scanner.nextInt();
        for(testCase in 1..numTestCases) {
            val lower = scanner.nextInt();
            val upper = scanner.nextInt();
            val maxGuess = scanner.nextInt();
            val emptyLine = scanner.nextLine();
            var guessState = Guess(lower, upper);
blah@       for(guess in 1..maxGuess) {
                output.println(guessState.currentGuess());
                val line = scanner.nextLine();
                when(line) {
                    "TOO_SMALL" -> {
                        guessState = guessState.guessUpper();
                    } "TOO_BIG" -> {
                        guessState = guessState.guessLower();
                    } "CORRECT" -> {
                        break@blah;
                    } "WRONG_ANSWER" -> {
                        return;
                    } else -> {
                        throw RuntimeException("got unexpected line: $line") // unexpected
                    }
                }
            }
        }
        input.close();
        output.close();
    }

}


class Guess(lower: Int, upper: Int) {

    private val lower = lower
    private val upper = upper

    fun guessLower(): Guess {
        return Guess(lower, currentGuess() - 1)
    }

    fun guessUpper(): Guess {
        return Guess(currentGuess() + 1, upper)
    }

    fun isValid(): Boolean {
        return lower <= upper
    }

    fun currentGuess(): Int {
        // no loss of precision because a+b can overflow, but (a+b)/2 cannot
        return ((lower.toLong() + upper.toLong()) / 2).toInt()
    }
}

fun main(args: Array<String>) {
    Solver().solve(System.`in`, System.out)
}
