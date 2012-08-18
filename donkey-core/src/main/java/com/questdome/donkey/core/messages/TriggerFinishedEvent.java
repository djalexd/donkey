package com.questdome.donkey.core.messages;

/**
 * Signals all interested parties that a timer is done sending events.
 * @author padawan
 * @since 8/17/12 1:27 PM
 */
public class TriggerFinishedEvent {

	int generatorId;
	long numGeneratedEvents;

	public TriggerFinishedEvent(int generatorId, long numGeneratedEvents) {
		this.generatorId = generatorId;
		this.numGeneratedEvents = numGeneratedEvents;
	}

	public long getNumGeneratedEvents() {
		return numGeneratedEvents;
	}
}
