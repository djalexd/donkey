package com.questdome.donkey.core;

/**
 * A {@link TriggerGenerator} that allows wiring to various events fired.
 *
 *
 * @author padawan
 * @since 8/18/12 7:24 PM
 */
public interface EventBasedTriggerGenerator extends TriggerGenerator {

	void addOnTriggerListener(TriggerListener listener);
}
