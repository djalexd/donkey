package com.questdome.donkey.core;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 6:27 PM
 */
public class TriggerGeneratorUtilsTest {

	@Test
	public void should_generate_the_correct_trigger_times_constant() {
		// then
		ArrayList<Long> times = TriggerGeneratorUtils.computeTriggerTimes(
				10, TimeUnit.SECONDS, constant(1L), TimeUnit.SECONDS);
		// assert
		Assertions.assertThat(times)
				.isNotNull().isNotEmpty().hasSize(11)
				.contains(numbersFromToMilliseconds(0, 10));
	}

	@Test
	public void should_generate_the_correct_trigger_times_linear_up() {
		// then
		ArrayList<Long> times = TriggerGeneratorUtils.computeTriggerTimes(
				4L, TimeUnit.SECONDS, linear(0L, 2L, 4L), TimeUnit.SECONDS);
		// assert
		Assertions.assertThat(times)
				.isNotNull().isNotEmpty().hasSize(4)
				.contains(2000L, 3000L, 4000L, 4000L);
	}

	static Function<Long, Long> constant(final Long value) {
		return new Function<Long, Long>() {
			@Override
			public Long apply(@Nullable Long input) {
				return value;
			}
		};
	}

	static Function<Long, Long> linear(final Long start, final Long end, final Long duration) {
		return new Function<Long, Long>() {
			@Override
			public Long apply(@Nullable Long input) {
				Preconditions.checkNotNull(input);
				if (input < 0 || input > duration)
					throw new IllegalStateException("Invalid function range, use [0," + duration + ")");
				return start + (end - start) * input / duration;
			}
		};
	}

	static Long[] numbersFromToMilliseconds(int from, int to) {
		Preconditions.checkState(from < to, "from is larger or equal than to");
		Long[] values = new Long[to - from];
		for (int i = from; i < to; i++) {
			values[i] = i * 1000L;
		}

		return values;
	}
}
