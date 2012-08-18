package com.questdome.donkey.core;

import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;
import com.questdome.donkey.core.messages.TriggerStartEvent;

/**
 * No-op implementation of TriggerListener. Allows subclasses to
 * listen to only a subset of events.
 * @author padawan
 * @since 8/18/12 8:31 PM
 */
public class TriggerAdapter implements TriggerListener {
	@Override
	public void onTriggerEvent(TriggerEvent event) {}
	@Override
	public void onStart(TriggerStartEvent event) {}
	@Override
	public void onEnd(TriggerFinishedEvent event) {}
}
