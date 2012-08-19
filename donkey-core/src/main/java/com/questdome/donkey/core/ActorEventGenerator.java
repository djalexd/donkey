package com.questdome.donkey.core;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;
import com.questdome.donkey.core.messages.TriggerStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Akka implementation for triggering events.
 * @author padawan
 * @since 8/18/12 3:27 AM
 */
public class ActorEventGenerator extends UntypedActor {
	private static Logger LOG = LoggerFactory.getLogger(ActorEventGenerator.class);

	EventBasedTriggerGenerator triggerEventGenerator;

	public ActorEventGenerator(EventBasedTriggerGenerator triggerEventGenerator) {
		this.triggerEventGenerator = triggerEventGenerator;
		this.triggerEventGenerator.addOnTriggerListener(new TriggerAdapter() {
			@Override
			public void onTriggerEvent(TriggerEvent event) {
				getContext().parent().tell(event);
			}

			@Override
			public void onEnd(TriggerFinishedEvent event) {
				getContext().parent().tell(event);
			}
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TriggerStartEvent) {
			TriggerStartEvent start = (TriggerStartEvent) message;

			LOG.debug("Event generator triggered with {} {}",
					start.getDuration(),
					start.getTimeUnit());

			this.triggerEventGenerator.runFor(start.getDuration(), start.getTimeUnit());

		} else {
			unhandled(message);
		}
	}
}