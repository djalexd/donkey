package com.questdome.donkey.core;

import java.util.concurrent.TimeUnit;

/**
 * Functions that generate triggers for {@link Sampler}. 
 * @author alex dobjanschi
 * @since Aug 15, 2012 11:52:39 PM
 */
public interface TriggerGenerator {
	void runFor(long duration, TimeUnit unit);
}
