package com.josephmate.google.kickstart

import org.junit.Assert
import java.io.*
import kotlin.test.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Problem3AlarmTest {

    @Test fun test() {
        runTestCase(
                listOf(
                    "2",
                    "2 3 1 2 1 2 1 1 9",
                    "10 10 10001 10002 10003 10004 10005 10006 89273"
                ), listOf(
                    "Case #1: 52",
                    "Case #2: 739786670",
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
        AlarmSolver().solve(bin, resultStream)

        Assert.assertEquals(expectedResult, String(result.toByteArray()).lines())
    }

}
