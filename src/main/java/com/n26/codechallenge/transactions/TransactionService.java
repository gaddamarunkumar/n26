package com.n26.codechallenge.transactions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.codechallenge.util.DateUtility;

/**
 * TransactionService
 * 
 * @author Gaddam
 *
 */
@Service
public class TransactionService {

	private Logger logger = Logger.getLogger(TransactionService.class);

	@Autowired
	private TransactionRepository transactionRepository;

	public int persistTransaction(double amount, long timestamp) {
		transactionRepository.addTransaction(amount, timestamp);
		if (DateUtility.isUTCTimestampLessThan60Secs(timestamp)) {
			return 201;
		}
		logger.info("Received transaction with timestamp more than 60 secs utc.");
		return 204;
	}

	public Statistics getStatistics() {
		return transactionRepository.getStatistics();
	}

	public int totalTransactions() {
		return transactionRepository.totalRecordsTillNow();
	}
}
