package com.n26.codechallenge.transactions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Statistics
 * 
 * @author Gaddam
 *
 */
@Data
@ToString
@NoArgsConstructor
public class Statistics {

	private double sum;
	private double average;
	private double max;
	private double min;
	private long count;

}
