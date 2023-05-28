package net.markwalder;

import java.util.TimeZone;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.time.MutableInstant;

public class Main {

	/**
	 * Number of parallel threads to run.
	 */
	private static final int THREADS = 10; // must be < 1000

	/**
	 * Number of iterations per thread.
	 */
	private static final int ITERATIONS = 1000;

	public static void main(String[] args) {

		// set UTC timezone
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		// simulate web application behavior (no thread-locals)
		System.setProperty("log4j2.enable.threadlocals", "false");

		// prepare a shared DatePatternConverter
		String[] options = { "HH:mm:ss.SSS" };
		DatePatternConverter converter = DatePatternConverter.newInstance(options);

		// prepare and start threads
		Thread[] threads = new Thread[THREADS];
		for (int t = 0; t < threads.length; t++) {
			final int threadId = t;

			// create a unique instant (event time) for each thread
			// where the number of milliseconds is equal to the thread id
			MutableInstant instant = new MutableInstant();
			instant.initFromEpochMilli(t, 0);

			// start thread
			threads[t] = new Thread(() -> run(threadId, instant, converter));
			threads[t].start();
		}

		// wait for threads to finish
		try {
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void run(int threadId, MutableInstant instant, DatePatternConverter converter) {

		// pre-calculated expected output: 00:00:00 + thread id as milliseconds
		String expectedOutput = "00:00:00." + String.format("%03d", threadId);

		for (int i = 0; i < ITERATIONS; i++) {

			// format instant
			StringBuilder builder = new StringBuilder();
			converter.format(instant, builder);
			String output = builder.toString();

			// check if output is as expected
			if (output.equals(expectedOutput)) {
				// System.out.println("Thread " + threadId + " got " + output + " in iteration " + i);
			} else {
				System.out.println("Thread " + threadId + " expected " + expectedOutput + " but got " + output + " in iteration " + i);
			}

		}

	}

}
