package com.josephmate.google.kickstart

import org.junit.Assert
import java.io.*
import kotlin.test.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Problem1NumberGuessingTest {

    @Test fun testNotValid() {
        val guess = Guess(1,0);
        Assert.assertFalse(guess.isValid());
    }

    @Test fun testLower() {
        var guess = Guess(0,100);
        Assert.assertTrue(guess.isValid());
        Assert.assertEquals(50, guess.currentGuess())
        guess = guess.guessLower();
        Assert.assertEquals((0+49)/2, guess.currentGuess())
    }

    @Test fun testHigher() {
        var guess = Guess(0,100);
        Assert.assertTrue(guess.isValid());
        Assert.assertEquals(50, guess.currentGuess())
        guess = guess.guessUpper();
        Assert.assertEquals((51+100)/2, guess.currentGuess())
    }

    @Test fun testOverflow() {
        val guess = Guess(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Assert.assertTrue(guess.isValid());
        Assert.assertEquals(Integer.MAX_VALUE, guess.currentGuess())
    }

    @Test fun testReallyClose() {
        var guess = Guess(0, 1);
        Assert.assertEquals(0, guess.currentGuess())
        Assert.assertTrue(guess.isValid());
        guess = guess.guessUpper();
        Assert.assertEquals(1, guess.currentGuess())
    }

    @Test fun testSolver() {
        val testCase = { writerToSolver: PrintStream, reader: BufferedReader ->
            println("TESTCASE: sending input")
            writerToSolver.println("1")
            writerToSolver.println("0 100")
            writerToSolver.println("100")

            println("TESTCASE: reading response")
            Assert.assertEquals(50, reader.readLine().toInt())
            println("TESTCASE: telling it to stop")
            writerToSolver.println("CORRECT")
            writerToSolver.close();
            writerToSolver.close();
            reader.close();
        }
        runTestCase(testCase)
    }

    @Test fun testWrongAnswerShouldStop() {
        val testCase = { writerToSolver: PrintStream, reader: BufferedReader ->
            println("TESTCASE: sending input")
            writerToSolver.println("1")
            writerToSolver.println("0 100")
            writerToSolver.println("100")

            println("TESTCASE: reading response")
            reader.readLine()
            println("TESTCASE: telling it to stop")
            writerToSolver.println("WRONG_ANSWER")
            writerToSolver.close();
            reader.close();
        }
        runTestCase(testCase)
    }

    @Test fun testMultipleIterationsToAnswer() {
        val testCase = { writerToSolver: PrintStream, reader: BufferedReader ->
            println("TESTCASE: sending input")
            writerToSolver.println("1")
            writerToSolver.println("0 100")
            writerToSolver.println("100")
            println("TESTCASE: reading response")
            val firstResponse = reader.readLine().toInt()

            println("TESTCASE: telling solver need larger")
            writerToSolver.println("TOO_SMALL")
            val biggerResponse = reader.readLine().toInt()
            Assert.assertTrue(biggerResponse > firstResponse)

            println("TESTCASE: telling solver need smaller")
            writerToSolver.println("TOO_BIG")
            val biggerSmallerResponse = reader.readLine().toInt()
            Assert.assertTrue(biggerSmallerResponse > firstResponse)
            Assert.assertTrue(biggerSmallerResponse < biggerResponse)

            println("TESTCASE: telling solver done")
            writerToSolver.println("CORRECT")
            writerToSolver.close();
            reader.close();
        }
        runTestCase(testCase)
    }

    private fun runTestCase(testCase:(PrintStream, BufferedReader) -> Unit) {
        val solverReceivingFromTestClass = PipedInputStream()
        val testClassSendingToSolver = PipedOutputStream()
        solverReceivingFromTestClass.connect(testClassSendingToSolver)

        val testClassReceivingFromSolver = PipedInputStream()
        val fromSolverToTestClass = PipedOutputStream()
        testClassReceivingFromSolver.connect(fromSolverToTestClass)

        val executorService = Executors.newFixedThreadPool(2);

        try {
            val solverFuture = executorService.submit(Callable<Throwable>(){
                try {
                    println("SOLVER: starting")
                    NumberGuessingSolver().solve(solverReceivingFromTestClass, PrintStream(fromSolverToTestClass))
                    println("SOLVER: finished")
                } catch(t: Throwable) {
                    return@Callable t;
                }
                return@Callable null;
            })
            val testCaseFuture = executorService.submit(Callable<Throwable>(){
                try {
                    println("TESTCASE: starting")
                    testCase(PrintStream(testClassSendingToSolver), testClassReceivingFromSolver.bufferedReader())
                    println("TESTCASE: finished")
                } catch(t: Throwable) {
                    return@Callable t;
                }
                return@Callable null;
            })

            val testCaseException: Throwable? = testCaseFuture.get(10, TimeUnit.SECONDS)
            if(testCaseException != null) {
                throw testCaseException
            }
            val solverException: Throwable? = solverFuture.get(10, TimeUnit.SECONDS)
            if(solverException != null) {
                throw solverException
            }
        } finally {
            executorService.shutdown();
        }
    }
}
