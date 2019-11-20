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

fun encodeStart(numOfDigits: Int, paintPosn: Int): PaintState {
    val builder: StringBuilder = StringBuilder(numOfDigits)
    for(i in 1..numOfDigits) {
        builder.append(' ')
    }
    builder.setCharAt(paintPosn, '*')
    return PaintState(
            builder.toString(),
            paintPosn,
            paintPosn,
            -1,
            numOfDigits
            )
}

class Problem(val numOfDigits: Int, val wallBeauty: String) {
}

class PaintState(
        val stringEncoding: String,
        val leftMostPaint: Int,
        val rightMostPaint: Int,
        val leftDestruction: Int,
        val rightDesctruction: Int
) {

    fun canGoLeft(): Boolean {
        return leftMostPaint >= 0
                && (leftMostPaint - 1) > leftDestruction
    }

    fun canGoRight(): Boolean {
        return rightMostPaint < stringEncoding.length - 1
                && (rightMostPaint + 1) < rightDesctruction
    }

    fun canAdvanceLeftDestruction(): Boolean {
        return leftDestruction + 1 < leftMostPaint
    }

    fun canAdvanceRightDestruction(): Boolean {
        return rightDesctruction - 1 > rightMostPaint
    }

    fun goLeft(): PaintState {
        val builder = StringBuilder(stringEncoding)
        builder.setCharAt(leftMostPaint - 1, '*')
        return PaintState(
                builder.toString(),
                leftMostPaint - 1,
                rightMostPaint,
                leftDestruction,
                rightDesctruction
        )
    }

    fun goRight(): PaintState {
        val builder = StringBuilder(stringEncoding)
        builder.setCharAt(rightMostPaint + 1, '*')
        return PaintState(
                builder.toString(),
                leftMostPaint,
                rightMostPaint + 1,
                leftDestruction,
                rightDesctruction
        )
    }

    fun advanceLeftDestruction(): PaintState {
        val builder = StringBuilder(stringEncoding)
        builder.setCharAt(leftDestruction + 1, 'x')
        return PaintState(
                builder.toString(),
                leftMostPaint,
                rightMostPaint,
                leftDestruction + 1,
                rightDesctruction
        )
    }

    fun advanceRightDestruction(): PaintState {
        val builder = StringBuilder(stringEncoding)
        builder.setCharAt(rightDesctruction - 1, 'x')
        return PaintState(
                builder.toString(),
                leftMostPaint,
                rightMostPaint,
                leftDestruction,
                rightDesctruction - 1
        )
    }
}

fun main(args: Array<String>) {
    MuralSolver().solve(System.`in`, System.out)
}
