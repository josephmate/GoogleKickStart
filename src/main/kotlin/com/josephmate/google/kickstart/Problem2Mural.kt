package com.josephmate.google.kickstart

import java.awt.Paint
import java.io.InputStream
import java.io.PrintStream
import java.util.*
import kotlin.collections.HashMap

/**
 * First I tried iterating all possible moves by the adversary and the painter, looking for the max. However,
 * that recursion requires too much stack. Also that first method gets 13331 wrong so it still needs improvement.
 * 13331 should be 3+3+1=7.
 * <p>
 * Second, I started thinking about what the end state would look like. 1 contiguous block of size N/2 for the
 * painter and one or two remaining contiguous blocks for the adversary. So then I thought what if you have a
 * N/2 size window and shift it from left to right, remembering the max. Unfortunately, that doesn't work because
 * of the case 13331. The window would find 333 as the max, but the adversary would take 13 or 31, and would not
 * allow the painter to get all 3 3's.
 * </p>
 * <p>
 * Third, I tried to build on that and try to maximize the adversary by looking at all possible i + j = N/2 contiguous
 * spaces. Unfortunately, that doesn't work either because of the case 0044. The adversary would take 44 as the maximum
 * but the painter would not allow that. The correct result should be 4+4 for the painter. The painter selects the right
 * most position first, prevent the destruction from capturing the remaining 4.
 * </p>
 */
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
    val memoizationMap = HashMap<PaintState, Int>()
    for(paintPosn in 0 until problem.numOfDigits) {
        val current = (iterateDestruction(memoizationMap, problem.wallBeauty, encodeStart(problem.numOfDigits, paintPosn))
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
            numOfDigits,
            paintPosn,
            paintPosn,
            -1,
            numOfDigits
            )
}

fun iterateDestruction(memoizationMap: MutableMap<PaintState, Int>, wallBeautyScore: String, paintState: PaintState): Int {
    val alreadyComputed: Int? = memoizationMap[paintState]
    if(alreadyComputed != null) {
        return alreadyComputed
    }

    var max = 0;
    if(paintState.canAdvanceLeftDestruction()) {
        val current = iteratePaint(memoizationMap, wallBeautyScore, paintState.advanceLeftDestruction())
        if(current > max) {
            max = current
        }
    }
    if(paintState.canAdvanceRightDestruction()) {
        val current = iteratePaint(memoizationMap, wallBeautyScore, paintState.advanceRightDestruction())
        if(current > max) {
            max = current
        }
    }

    memoizationMap[paintState] = max
    return max
}

fun iteratePaint(memoizationMap: MutableMap<PaintState, Int>, wallBeautyScore: String, paintState: PaintState): Int {
    val alreadyComputed: Int? = memoizationMap[paintState]
    if(alreadyComputed != null) {
        return alreadyComputed
    }

    var max = 0;
    if(paintState.canPaintLeft()) {
        val current = (iterateDestruction(memoizationMap, wallBeautyScore, paintState.paintLeft())
            + calcBeauty(wallBeautyScore, paintState.leftMostPaint - 1))
        if(current > max) {
            max = current
        }
    }
    if(paintState.canPaintRight()) {
        val current = (iterateDestruction(memoizationMap, wallBeautyScore, paintState.paintRight())
            + calcBeauty(wallBeautyScore, paintState.rightMostPaint + 1))
        if(current > max) {
            max = current
        }
    }

    memoizationMap[paintState] = max
    return max
}

fun calcBeauty(wallBeautyScore: String, posn: Int): Int {
    return wallBeautyScore.get(posn).toInt() - 48
}

class Problem(val numOfDigits: Int, val wallBeauty: String) {
}

data class PaintState(
        val numOfDigits: Int,
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
        return rightMostPaint < numOfDigits - 1
                && (rightMostPaint + 1) < rightDesctruction
    }

    fun canAdvanceLeftDestruction(): Boolean {
        return leftDestruction + 1 < leftMostPaint
    }

    fun canAdvanceRightDestruction(): Boolean {
        return rightDesctruction - 1 > rightMostPaint
    }

    fun paintLeft(): PaintState {
        return PaintState(
                numOfDigits,
                leftMostPaint - 1,
                rightMostPaint,
                leftDestruction,
                rightDesctruction
        )
    }

    fun paintRight(): PaintState {
        return PaintState(
                numOfDigits,
                leftMostPaint,
                rightMostPaint + 1,
                leftDestruction,
                rightDesctruction
        )
    }

    fun advanceLeftDestruction(): PaintState {
        return PaintState(
                numOfDigits,
                leftMostPaint,
                rightMostPaint,
                leftDestruction + 1,
                rightDesctruction
        )
    }

    fun advanceRightDestruction(): PaintState {
        return PaintState(
                numOfDigits,
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
