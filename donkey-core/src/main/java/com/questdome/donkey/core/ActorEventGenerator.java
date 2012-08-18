package com.questdome.donkey.core;

import akka.actor.UntypedActor;
import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerStartEvent;

/**
 * Akka implementation for triggering events.
 * @author padawan
 * @since 8/18/12 3:27 AM
 */
public class ActorEventGenerator extends UntypedActor {

	EventBasedTriggerGenerator triggerEventGenerator;
	public ActorEventGenerator(EventBasedTriggerGenerator triggerEventGenerator) {
		this.triggerEventGenerator = triggerEventGenerator;
		this.triggerEventGenerator.addOnTriggerListener(new TriggerAdapter() {
			@Override
			public void onTriggerEvent(TriggerEvent event) {
				getSender().tell(event);
			}
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TriggerStartEvent) {
			TriggerStartEvent start = (TriggerStartEvent) message;
			this.triggerEventGenerator.runFor(start.getDuration(), start.getTimeUnit());

		} else {
			unhandled(message);
		}
	}
}