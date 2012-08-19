package com.questdome.donkey.core;

import akka.actor.UntypedActor;
import com.questdome.donkey.core.messages.SampleCollectionResult;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
* User: padawan.
* Date: 8/20/12
* Time: 12:17 AM
*/
public class DisplayResultsListener extends UntypedActor {
	private static final Logger LOG = LoggerFactory.getLogger(DisplayResultsListener.class);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof SampleCollectionResult) {
			SampleCollectionResult results = (SampleCollectionResult) message;
			LOG.info("Samples collected in {} ms", results.getDuration().toMillis());
			displayResults(results.getSampleCollection());
		} else {
			unhandled(message);
		}
	}

	/**
	 * Show the SampleCollection
	 */
	void displayResults(SampleCollection collection) {

		SummaryStatistics stats = collection.getDurationStats();
		LOG.info("Duration stats");
		displaySummaryStatistics(stats);

		LOG.info("Status code frequencies");
		displayFrequency(collection.getStatusCodeFrequency());
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
}
