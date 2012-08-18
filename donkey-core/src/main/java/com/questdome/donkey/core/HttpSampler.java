package com.questdome.donkey.core;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ning.http.client.*;
import com.questdome.donkey.core.messages.TriggerFinishedEvent;
import com.questdome.donkey.core.messages.TriggerEvent;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class HttpSampler extends BaseAbstractSampler {
	final static int NUM_COMPONENTS = 16;
	private static Logger LOG = LoggerFactory.getLogger(HttpSampler.class);

	private final SampleCollection sampleCollection;
	final String targetUri;

	final HttpSamplerComponent[] components;
	int lastUsed = 0;
	long numTriggersGenerated = -1;

	public HttpSampler(String targetUri, EventBus eventBus) {
		eventBus.register(this);

		this.targetUri = targetUri;
		this.sampleCollection = new SampleCollection(10);
		this.components = new HttpSamplerComponent[NUM_COMPONENTS];
		for (int i = 0; i < NUM_COMPONENTS; i++) {
			this.components[i] = new HttpSamplerComponent(new AsyncHttpClient(), this.sampleCollection);
		}

		// We need another timer to determine when sub-components
		// have finished gathering samples
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Return immediately if generator didn't provide us yet
				// with a number
				if (numTriggersGenerated < 0) { return; }
				LOG.info("Processed so far {} / {}", sampleCollection.getNumSamples(), numTriggersGenerated);
				if (sampleCollection.getNumSamples() < numTriggersGenerated) { return; }
				displayResults();

				// Cancel the timer.
				cancel();
			}
		}, 0L, 1000);
	}

	@SuppressWarnings("unused")
	@AllowConcurrentEvents
	@Subscribe
	public void handleTrigger(TriggerEvent evt) {
		lastUsed = (lastUsed++) % NUM_COMPONENTS;
		try {
			components[lastUsed].execute(new RequestBuilder("GET").setUrl(targetUri).build());
		} catch (NullPointerException e) {
			LOG.error("NPE {} {}", lastUsed, components[lastUsed]);
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void triggerDone(TriggerFinishedEvent e) {
		numTriggersGenerated = e.getNumGeneratedEvents();
	}

	/**
	 * Show the SampleCollection
	 */
	void displayResults() {

		SampleCollection collectionClone = sampleCollection.clone();

		SummaryStatistics stats = collectionClone.getDurationStats();
		LOG.info("Duration stats");
		displaySummaryStatistics(stats);

		LOG.info("Status code frequencies");
		displayFrequency(collectionClone.getStatusCodeFrequency());
	}

	void displaySummaryStatistics(SummaryStatistics stats) {
		LOG.info("Samples = {}", stats.getN());
		LOG.info("Min = {}", stats.getMin());
		LOG.info("Max = {}", stats.getMax());
		LOG.info("Avg = {}", stats.getMean());
		LOG.info("Std. Dev = {}", stats.getStandardDeviation());
	}

	void displayFrequency(Frequency frequency) {
		Iterator i = frequency.valuesIterator();
		while (i.hasNext()) {
			Object o = i.next();
			LOG.info("{} = {}", o, frequency.getCount(o));
		}
	}

	public static void main(String[] args) {
		final EventBus eventBus = new EventBus();
		TriggerGenerator generator = new SimpleRegularEventGenerator(50, TimeUnit.SECONDS, eventBus);

		HttpSampler sampler = new HttpSampler("https://seesmic.com", eventBus);
		sampler.setEventGenerator(generator);

		generator.runFor(5, TimeUnit.SECONDS);
	}
}


