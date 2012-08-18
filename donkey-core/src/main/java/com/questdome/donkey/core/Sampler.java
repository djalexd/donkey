package com.questdome.donkey.core;

/**
 * A sampler generates (http) requests for tests. 
 * @author alex dobjanschi
 * @since 8/15/12 11:41 PM
 */
public interface Sampler {
	void setEventGenerator(TriggerGenerator triggerEventGenerator);
}
