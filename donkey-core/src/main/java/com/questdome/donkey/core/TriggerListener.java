package com.questdome.donkey.core;

import com.questdome.donkey.core.messages.TriggerEvent;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;
import com.questdome.donkey.core.messages.TriggerStartEvent;

import java.util.EventListener;

/**
* @author padawan
* @since 8/18/12 8:31 PM
*/
public interface TriggerListener extends EventListener {
	void onTriggerEvent(TriggerEvent event);

	void onStart(TriggerStartEvent event);
	void onEnd(TriggerFinishedEvent event);
}
