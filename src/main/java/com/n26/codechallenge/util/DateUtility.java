package com.n26.codechallenge.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * DateUtility
 * 
 * @author Gaddam
 *
 */
public final class DateUtility {

	public static final boolean isUTCTimestampLessThan60Secs(long timeInMillis) {
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		return (((utc.toInstant().toEpochMilli() - timeInMillis) / 1000) < 60);
	}
}
