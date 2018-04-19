package com.n26.codechallenge.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.n26.codechallenge.transactions.Statistics;
import com.n26.codechallenge.transactions.TransactionService;

/**
 * TestStatistics
 * 
 * @author Gaddam
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class TestStatistics {

	// Creating 1 reader thread, and 10 dumping threads and each thread will
	// dump 25000 transaction with delay of 1 milli second, and after 60 seconds
	// we can see out dated transactions are cleaned.

	private static final int TRANSACTIONS_DUMPED_BY_EACH_THREAD = 25000;

	@Autowired
	TransactionService transactionService;

	private static int createThreadCount = 1;
	private static final int TOTAL_THREAD_COUNT = 10;
	private static int statsReadCount = 1;
	private static final int TOTAL_STATS_READ_LIMIT = 120;

	private static final long MEGABYTE = 1024L * 1024L;

	@Test
	public void testDumpAndRead() {
		long start = System.currentTimeMillis();
		ExecutorService readService = Executors.newSingleThreadExecutor();
		Future<?> future = readService.submit(() -> {
			System.out.println("[INFO] Starting stats thread ... ");
			while (statsReadCount <= TOTAL_STATS_READ_LIMIT) {
				try {
					long statStart = System.currentTimeMillis();
					Statistics statistics = transactionService.getStatistics();
					System.out.println("[INFO] Stats Thread: " + statistics + " timeTaken: "
							+ ((System.currentTimeMillis() - statStart) / 1000) + " Secs, totalRecordsTillNow: "
							+ transactionService.totalTransactions() + ", elapsed time "
							+ ((System.currentTimeMillis() - start) / 1000) + " Secs, "
							+ (TOTAL_STATS_READ_LIMIT - statsReadCount) + " Secs to exit.");

					if (statsReadCount % 20 == 0) {
						Runtime runtime = Runtime.getRuntime();
						long memory = runtime.totalMemory() - runtime.freeMemory();
						System.out.println("Used memory is megabytes: " + (memory / MEGABYTE));
					}
					Thread.sleep(1 * 1000);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				++statsReadCount;
			}
		});

		ExecutorService dumpServices = Executors.newFixedThreadPool(TOTAL_THREAD_COUNT);
		for (createThreadCount = 1; createThreadCount <= TOTAL_THREAD_COUNT; createThreadCount++) {
			dumpServices.submit(() -> {
				System.out.println("[INFO] Starting dump thread " + createThreadCount + " ... ");
				int transactionCount = 0;
				while (transactionCount <= TRANSACTIONS_DUMPED_BY_EACH_THREAD) {
					transactionService.persistTransaction(createThreadCount * 1.1, System.currentTimeMillis());
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}
					++transactionCount;
				}
			});
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}

		try {
			future.get();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
