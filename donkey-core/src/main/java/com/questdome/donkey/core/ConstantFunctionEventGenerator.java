package com.questdome.donkey.core;

import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;


public class ConstantFunctionEventGenerator extends FunctionBasedEventGenerator {

	public ConstantFunctionEventGenerator(final long eventsPerUnit, TimeUnit unit) {
		super(new Function<Long, Long>() {
			@Override
			public Long apply(@Nullable Long input) {
				return eventsPerUnit;
			}
		}, unit);
	}
}
