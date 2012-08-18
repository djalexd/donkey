package com.questdome.donkey.core.messages;

import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 6:28 PM
 */
public class TriggerStartEvent {
	long duration;
	TimeUnit timeUnit;
	public TriggerStartEvent(long duration, TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	public long getDuration() {
		return duration;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
