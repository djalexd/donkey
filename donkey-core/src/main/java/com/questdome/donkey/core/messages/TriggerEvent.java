package com.questdome.donkey.core.messages;

/**
 * TODO refine
 * @author alex dobjanschi
 * @since 8/16/12 12:37 AM
 */
public class TriggerEvent {

	long startTime; // when triggering started for TriggerEventGenerator.
	long triggerTime; // when this trigger occurred.
	long triggerCountSoFar; // how many triggers (including this one) were generated.

	public TriggerEvent(long startTime, long triggerTime, long triggerCountSoFar) {
		this.startTime = startTime;
		this.triggerTime = triggerTime;
		this.triggerCountSoFar = triggerCountSoFar;
	}
}
