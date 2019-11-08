package com.josephmate.google.kickstart

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
        return lower < upper
    }

    fun currentGuess(): Int {
        // no loss of precision because a+b can overflow, but (a+b)/2 cannot
        return ((lower.toLong() + upper.toLong()) / 2).toInt()
    }
}

fun main(args: Array<String>) {
}
