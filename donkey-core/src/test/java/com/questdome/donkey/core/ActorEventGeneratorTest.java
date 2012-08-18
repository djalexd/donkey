package com.questdome.donkey.core;

import akka.actor.ActorSystem;
import com.google.common.base.Function;
import org.fest.assertions.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 6:27 PM
 */
public class ActorEventGeneratorTest {
	static ActorSystem actorSystem;
	FunctionBasedEventGenerator functionBasedEventGenerator;

	@BeforeClass
	public static void setupActorSystem() {
		actorSystem = akka.actor.ActorSystem.create();
	}

	@AfterClass
	public static void tearDownActorSystem() {
		actorSystem.shutdown();
	}

	@Test
	public void should_generate_the_correct_trigger_times() {
		// when
		functionBasedEventGenerator =
				new FunctionBasedEventGenerator(constant(1L), TimeUnit.SECONDS);
		// then
		ArrayList<Long> times =
				functionBasedEventGenerator.computeTriggerTimes(10, TimeUnit.SECONDS);
		// assert
		Assertions.assertThat(times)
				.isNotNull().isNotEmpty().hasSize(10)
				.contains(0L, 1000L, 2000L, 3000L, 4000L, 5000L, 6000L, 7000L, 8000L, 9000L);
	}

	static Function<Long, Long> constant(final Long value) {
		return new Function<Long, Long>() {
			@Override
			public Long apply(@Nullable Long input) {
				return value;
			}
		};
	}
}
