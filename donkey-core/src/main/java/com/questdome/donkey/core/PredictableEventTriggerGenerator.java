package com.questdome.donkey.core;

import com.google.common.base.Function;
import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * An {@link TriggerGenerator} whose values at each step are predictable,
 * and will be computed before any trigger will be generated. This is the
 * base class for all continuous function, function-based generators;
 *
 * @author padawan
 * @since 8/22/12 10:16 AM
 */
public abstract class PredictableEventTriggerGenerator extends AbstractEventBasedTriggerGenerator {

	private final TimeUnit functionTimeUnit;
	private final long howManyTriggersPerUnit;

	protected PredictableEventTriggerGenerator(TimeUnit functionUnitTime) {
		this.functionTimeUnit = functionUnitTime;
		this.howManyTriggersPerUnit = TimeUnit.MILLISECONDS.convert(1, functionTimeUnit);
	}

	protected abstract Function<Long, Long> predictableFunction();

	@Override
	public void runFor(long duration, TimeUnit unit) {
		final long start = System.currentTimeMillis();
		//final long end = start + TimeUnit.MILLISECONDS.convert(duration, unit);

		// Construct an array of at least 'duration' elements
		final ArrayList<Long> triggerTimes = TriggerGeneratorUtils.computeTriggerTimes(
				duration, unit, predictableFunction(), functionTimeUnit);
		// we have all triggers now.

		final Timer scheduleTimer = new Timer();
		for (int i = 0; i < triggerTimes.size(); i++) {
			final int eventsSoFar = i;
			final long triggerTime = triggerTimes.get(i);
			scheduleTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					fireTriggerEvent(new TriggerEvent(start, triggerTime, eventsSoFar));
				}
			}, triggerTime);
		}

		// Schedule the cancel timer.
		scheduleTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Send the end event
				fireTriggerEndEvent(new TriggerFinishedEvent(triggerTimes.size()));

				cancel();
			}
		}, unit.toMillis(duration));
	}

}
