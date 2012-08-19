package com.questdome.donkey.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 7:15 PM
 */
public class FunctionBasedEventGenerator implements EventBasedTriggerGenerator {

	EventListenerList eventListenerList = new EventListenerList();

	// This function is used to generate the number of
	// events at given ticks (x). This is a discrete function
	// f: N => N, with f(x) = eventFunction(x).
	private final Function<Long, Long> eventFunction;
	private final TimeUnit functionTimeUnit;
	private final long howManyTriggersPerUnit;

	protected FunctionBasedEventGenerator(Function<Long, Long> eventFunction,
																				TimeUnit functionTimeUnit) {

		this.eventFunction = eventFunction;
		this.functionTimeUnit = functionTimeUnit;
		this.howManyTriggersPerUnit = TimeUnit.MILLISECONDS.convert(1, functionTimeUnit);
	}

	@Override
	public void addOnTriggerListener(TriggerListener listener) {
		eventListenerList.add(TriggerListener.class, listener);
	}

	@Override
	public void runFor(long duration, TimeUnit unit) {

		final long start = System.currentTimeMillis();
		//final long end = start + TimeUnit.MILLISECONDS.convert(duration, unit);


		// Construct an array of at least 'duration' elements
		final ArrayList<Long> triggerTimes = computeTriggerTimes(duration, unit);
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



	@VisibleForTesting
	ArrayList<Long> computeTriggerTimes(long duration, TimeUnit unit) {
		final long convertedDuration = functionTimeUnit.convert(duration, unit);

		ArrayList<Long> triggerTimes = new ArrayList<Long>(Long.valueOf(duration).intValue());

		for (long i = 0; i <= convertedDuration; i++) {
			// Now this is function space x, get y.
			final Long numEventsInFunctionSpace = eventFunction.apply(i);
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

	private void fireTriggerEvent(TriggerEvent event) {
		TriggerListener[] listeners = eventListenerList.getListeners(TriggerListener.class);
		for (TriggerListener listener : listeners) {
			listener.onTriggerEvent(event);
		}
	}

	private void fireTriggerEndEvent(TriggerFinishedEvent event) {
		TriggerListener[] listeners = eventListenerList.getListeners(TriggerListener.class);
		for (TriggerListener listener : listeners) {
			listener.onEnd(event);
		}
	}
}
