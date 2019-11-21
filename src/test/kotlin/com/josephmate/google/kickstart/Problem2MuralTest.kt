package com.josephmate.google.kickstart

import org.junit.Assert
import java.io.*
import kotlin.test.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Problem2MuralTest {

    @Test fun test() {
        runTestCase(
                listOf(
                    "4",
                    "4",
                    "1332",
                    "4",
                    "9583",
                    "3",
                    "616",
                    "10",
                    "1029384756"
                ), listOf(
                    "Case #1: 6",
                    "Case #2: 14",
                    "Case #3: 7",
                    "Case #4: 31",
                    ""
                )
        )
    }

    @Test fun testBig() {
        val expected = 5000000/2
        val input: StringBuilder = StringBuilder()
        for( i in 1..5000000) {
            input.append("1")
        }
        runTestCase(
                listOf(
                        "1",
                        "5000000",
                        input.toString()
                ), listOf(
                "Case #1: $expected",
                ""
                )
        )
    }

    private fun runTestCase(inputLines: List<String>, expectedResult: List<String>) {
        val bos = ByteArrayOutputStream()
        val printWriter = PrintStream(bos);
        for(line in inputLines) {
            printWriter.println(line);
        }
        printWriter.close();

        val bin = ByteArrayInputStream(bos.toByteArray())
        val result = ByteArrayOutputStream()
        val resultStream = PrintStream(result)
        MuralSolver().solve(bin, resultStream)

        Assert.assertEquals(expectedResult, String(result.toByteArray()).lines())
    }

}
