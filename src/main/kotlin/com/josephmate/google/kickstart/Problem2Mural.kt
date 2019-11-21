package com.josephmate.google.kickstart

import java.io.InputStream
import java.io.PrintStream
import java.util.*


class MuralSolver() {

    fun solve(input: InputStream, output: PrintStream) {
        val scanner = Scanner(input);
        val numTestCases = scanner.nextInt();
        for(testCase in 1..numTestCases) {
            val numOfDigits = scanner.nextInt()
            scanner.nextLine() // read the rest of the line
            val line = scanner.nextLine()
            val problem = Problem(numOfDigits, line)
            val max = findMax(problem)

            output.println("Case #$testCase: $max")
        }
    }
}

fun findMax(problem: Problem): Int {
    var max = 0;
    for(paintPosn in 0 until problem.numOfDigits) {
        val current = (iterateDestruction(problem.wallBeauty, encodeStart(problem.numOfDigits, paintPosn))
            + calcBeauty(problem.wallBeauty, paintPosn))
        if( current > max )  {
            max = current
        }
    }
    return max
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

fun iterateDestruction(wallBeautyScore: String, paintState: PaintState): Int {
    var max = 0;
    if(paintState.canAdvanceLeftDestruction()) {
        val current = iteratePaint(wallBeautyScore, paintState.advanceLeftDestruction())
        if(current > max) {
            max = current
        }
    }
    if(paintState.canAdvanceRightDestruction()) {
        val current = iteratePaint(wallBeautyScore, paintState.advanceRightDestruction())
        if(current > max) {
            max = current
        }
    }
    return max
}

fun iteratePaint(wallBeautyScore: String, paintState: PaintState): Int {
    var max = 0;
    if(paintState.canPaintLeft()) {
        val current = (iterateDestruction(wallBeautyScore, paintState.paintLeft())
            + calcBeauty(wallBeautyScore, paintState.leftMostPaint - 1))
        if(current > max) {
            max = current
        }
    }
    if(paintState.canPaintRight()) {
        val current = (iterateDestruction(wallBeautyScore, paintState.paintRight())
            + calcBeauty(wallBeautyScore, paintState.rightMostPaint + 1))
        if(current > max) {
            max = current
        }
    }
    return max
}

fun calcBeauty(wallBeautyScore: String, posn: Int): Int {
    return wallBeautyScore.get(posn).toInt() - 48
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

    fun canPaintLeft(): Boolean {
        return leftMostPaint >= 0
                && (leftMostPaint - 1) > leftDestruction
    }

    fun canPaintRight(): Boolean {
        return rightMostPaint < stringEncoding.length - 1
                && (rightMostPaint + 1) < rightDesctruction
    }

    fun canAdvanceLeftDestruction(): Boolean {
        return leftDestruction + 1 < leftMostPaint
    }

    fun canAdvanceRightDestruction(): Boolean {
        return rightDesctruction - 1 > rightMostPaint
    }

    fun paintLeft(): PaintState {
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

    fun paintRight(): PaintState {
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
