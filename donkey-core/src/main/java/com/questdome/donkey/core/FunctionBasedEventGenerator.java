package com.questdome.donkey.core;

import com.google.common.base.Function;

import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 7:15 PM
 */
public class FunctionBasedEventGenerator extends PredictableEventTriggerGenerator {

	// This function is used to generate the number of
	// events at given ticks (x). This is a discrete function
	// f: N => N, with f(x) = eventFunction(x).
	private final Function<Long, Long> eventFunction;

	protected FunctionBasedEventGenerator(Function<Long, Long> eventFunction,
																				TimeUnit functionTimeUnit) {

		super(functionTimeUnit);
		this.eventFunction = eventFunction;
	}

	@Override
	protected Function<Long, Long> predictableFunction() {
		return this.eventFunction;
	}
}
