package com.questdome.donkey.core;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.*;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * User: padawan.
 * Date: 8/20/12
 * Time: 6:48 PM
 */
public class DistributionBasedEventGenerator extends PredictableEventTriggerGenerator {

	public static class RandomDistribution implements DiscreteDistribution {
		private static final String NO_CUMULATIVE =
				"cumulativeProbability not applicable to random distribution";

		private final Random random;
		private int min;
		private int max;
		public RandomDistribution(int min, int max) {
			this(0, min, max);
		}
		public RandomDistribution(long seed, int min, int max) {
			this.random = new Random(seed);
			Preconditions.checkState(min < max, "min expected to be smaller than max");
		}

		@Override
		public double probability(int i) {
			return min + random.nextInt(max - min);
		}

		@Override
		public double cumulativeProbability(int i) throws MathException {
			throw new RuntimeException(NO_CUMULATIVE);
		}

		@Override
		public double cumulativeProbability(int i, int i1) throws MathException {
			throw new RuntimeException(NO_CUMULATIVE);
		}

		@Override
		public int inverseCumulativeProbability(double v) throws MathException {
			throw new RuntimeException(NO_CUMULATIVE);
		}
	}

	DiscreteDistribution randomDistribution;
	public DistributionBasedEventGenerator(DiscreteDistribution randomDistribution, TimeUnit distributionTimeUnit) {
		super(distributionTimeUnit);
		this.randomDistribution = randomDistribution;
	}

	@Override
	protected Function<Long, Long> predictableFunction() {
		return new Function<Long, Long>() {
			@Override
			public Long apply(@Nullable Long input) {
				Preconditions.checkNotNull(input, "This function only know how to handle non-null inputs");
				return (long) randomDistribution.probability(input.intValue());
			}
		};
	}
}
