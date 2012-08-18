package com.questdome.donkey.core;

import com.google.common.eventbus.EventBus;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;
import com.questdome.donkey.core.messages.TriggerEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SimpleRegularEventGenerator implements TriggerGenerator {

	private final Timer timer;
	private final long eventsPerUnit;
	private final TimeUnit unit;
	private final long millisecondsBetweenEvents;
	private final EventBus eventBus;

	private long numGeneratedEvents;

	public SimpleRegularEventGenerator(long eventsPerUnit, TimeUnit unit, final EventBus eventBus) {

		this.eventsPerUnit = eventsPerUnit;
		this.unit = unit;
		this.millisecondsBetweenEvents = TimeUnit.MILLISECONDS.convert(1, unit) / eventsPerUnit;
		this.timer = new Timer("SimpleRegularTimer");
		this.eventBus = eventBus;
	}

	@Override
	public void runFor(long duration, TimeUnit unit) {

		// Start the timer when this method is invoked.
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				eventBus.post(new TriggerEvent(0, 0, 0));
				numGeneratedEvents++;
			}
		}, 0L, millisecondsBetweenEvents);

		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				// Shutdown the trigger event timer
				timer.cancel();
				//
				numGeneratedEvents--;
				eventBus.post(new TriggerFinishedEvent(0, numGeneratedEvents));
			}
		}, TimeUnit.MILLISECONDS.convert(duration, unit));
	}
}
