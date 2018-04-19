package com.n26.codechallenge.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Transaction
 * 
 * @author Gaddam
 *
 */
@Data
@ToString
@AllArgsConstructor
public class Transaction {

	private int transactionID;
	private double amount;
	private long timestamp;
	private long createdTimeStamp;

	@Override
	public int hashCode() {
		return transactionID;
	}

	@Override
	public boolean equals(Object transaction) {
		if (transactionID == ((Transaction) transaction).getTransactionID()) {
			return true;
		}
		return false;
	}
}
