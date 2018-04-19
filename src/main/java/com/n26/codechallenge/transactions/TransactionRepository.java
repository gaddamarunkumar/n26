package com.n26.codechallenge.transactions;

import java.text.DecimalFormat;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.n26.codechallenge.util.DateUtility;

/**
 * TransactionRepository
 * 
 * @author Gaddam
 *
 */
@Component
public class TransactionRepository {

	private Logger logger = Logger.getLogger(TransactionRepository.class);

	private static int counter = 0;
	private static final Set<Transaction> TRANSACTION_60_SEC_CACHE = new HashSet<Transaction>();

	public int totalRecordsTillNow() {
		return counter;
	}

	{
		logger.info("Starting cleaner and statictics calculator thread .... ");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			while (true) {
				try {
					Set<Transaction> transactionsToRemove = null;
					synchronized (TRANSACTION_60_SEC_CACHE) {
						transactionsToRemove = TRANSACTION_60_SEC_CACHE.stream()
								.filter(transaction -> !DateUtility
										.isUTCTimestampLessThan60Secs(transaction.getCreatedTimeStamp()))
								.collect(Collectors.toSet());
						if (transactionsToRemove.size() > 0) {
							logger.info(
									"Cleaning up out dated transaction, total count : " + transactionsToRemove.size());
						}

						// TODO: can archive [transactionsToRemove] if we really
						// care about the removing transactions
						TRANSACTION_60_SEC_CACHE.removeAll(transactionsToRemove);
					}

					DoubleSummaryStatistics doubleSummaryStatistics = null;
					synchronized (TRANSACTION_60_SEC_CACHE) {
						doubleSummaryStatistics = TRANSACTION_60_SEC_CACHE.stream()
								.collect(Collectors.summarizingDouble(Transaction::getAmount));
					}

					statistics.setSum(getDoubleValue(doubleSummaryStatistics.getSum()));
					statistics.setAverage(getDoubleValue(doubleSummaryStatistics.getAverage()));
					statistics.setMax(getDoubleValue(doubleSummaryStatistics.getMax()));
					statistics.setMin(getDoubleValue(doubleSummaryStatistics.getMin()));
					statistics.setCount(doubleSummaryStatistics.getCount());

					// TODO: keeping the threshold to 1 second, this can be
					// debatable based on use case.
					Thread.sleep(1 * 1000);
				} catch (Exception exception) {
					exception.printStackTrace();
					logger.error("Error cleaning up or calculating statistics, will try in a sec.", exception);
				}
			}
		});
	}

	public double getDoubleValue(double value) {
		if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY) {
			return 0d;
		} else {
			DecimalFormat formatter = new DecimalFormat("0.0000");
			return Double.valueOf(formatter.format(value));
		}
	}

	public void addTransaction(double amount, long timestamp) {
		synchronized (TRANSACTION_60_SEC_CACHE) {
			TRANSACTION_60_SEC_CACHE.add(new Transaction(++counter, amount, timestamp, System.currentTimeMillis()));
		}
	}

	private static final Statistics statistics = new Statistics();

	public Statistics getStatistics() {
		return statistics;
	}
}
