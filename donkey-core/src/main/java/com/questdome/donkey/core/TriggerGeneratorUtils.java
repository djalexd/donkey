package com.questdome.donkey.core;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/22/12 10:42 AM
 */
public class TriggerGeneratorUtils {


	/**
	 * Compute the trigger times, given a <code>duration</code> (along with <code>time unit</code>)
	 * and a predictable function -- whose values don't change by applying same arguments twice.
	 * @param duration
	 * @param unit
	 * @param function
	 * @param functionTimeUnit
	 * @return
	 */
	public static ArrayList<Long> computeTriggerTimes(
			long duration, TimeUnit unit,
			Function<Long, Long> function,
			TimeUnit functionTimeUnit) {

		final long convertedDuration = functionTimeUnit.convert(duration, unit);
		final long howManyTriggersPerUnit = TimeUnit.MILLISECONDS.convert(1, functionTimeUnit);

		ArrayList<Long> triggerTimes = new ArrayList<Long>(Long.valueOf(duration).intValue());

		for (long i = 0; i <= convertedDuration; i++) {
			// Now this is function space x, get y.
			final Long numEventsInFunctionSpace = function.apply(i);
			if (numEventsInFunctionSpace == null)
				throw new IllegalStateException("Provide an event function that does not produce null values");
			// Skip function values 0.
			if (numEventsInFunctionSpace == 0L)
				continue;
			// Convert to millisecond space
			long durationBetweenEvents = howManyTriggersPerUnit / numEventsInFunctionSpace;
			for (long j = 0; j < numEventsInFunctionSpace; j++) {
				triggerTimes.add(TimeUnit.MILLISECONDS.convert(i, unit) + durationBetweenEvents * j);
			}
		}
		return triggerTimes;
	}
}
