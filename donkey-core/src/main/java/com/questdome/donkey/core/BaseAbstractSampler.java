package com.questdome.donkey.core;


public abstract class BaseAbstractSampler implements Sampler {
	
	protected TriggerGenerator triggerEventGenerator;

	protected BaseAbstractSampler() {}
	protected BaseAbstractSampler(TriggerGenerator triggerEventGenerator) {
		this.triggerEventGenerator = triggerEventGenerator;
	}

	public void setEventGenerator(TriggerGenerator triggerEventGenerator) {
		this.triggerEventGenerator = triggerEventGenerator;
	}
}
