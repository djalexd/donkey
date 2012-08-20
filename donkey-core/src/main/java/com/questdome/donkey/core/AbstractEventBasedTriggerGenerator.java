package com.questdome.donkey.core;

import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;

import javax.swing.event.EventListenerList;

/**
 * Base class for {@link EventBasedTriggerGenerator}. Does nothing more than
 * providing an event listener list, along with helper methods to trigger events.
 * User: padawan.
 * Date: 8/20/12
 * Time: 6:49 PM
 */
public abstract class AbstractEventBasedTriggerGenerator implements EventBasedTriggerGenerator {
	private final EventListenerList eventListenerList = new EventListenerList();

	@Override
	public void addOnTriggerListener(TriggerListener listener) {
		eventListenerList.add(TriggerListener.class, listener);
	}

	protected final void fireTriggerEvent(TriggerEvent event) {
		TriggerListener[] listeners = eventListenerList.getListeners(TriggerListener.class);
		for (TriggerListener listener : listeners) {
			listener.onTriggerEvent(event);
		}
	}

	protected final void fireTriggerEndEvent(TriggerFinishedEvent event) {
		TriggerListener[] listeners = eventListenerList.getListeners(TriggerListener.class);
		for (TriggerListener listener : listeners) {
			listener.onEnd(event);
		}
	}
}
