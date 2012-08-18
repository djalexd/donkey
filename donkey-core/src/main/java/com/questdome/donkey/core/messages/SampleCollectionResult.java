package com.questdome.donkey.core.messages;

import akka.util.Duration;
import com.questdome.donkey.core.SampleCollection;

/**
 * @author padawan
 * @since 8/18/12 3:22 AM
 */
public class SampleCollectionResult {

	private long start;
	private Duration duration;

	SampleCollection sampleCollection;
	public SampleCollectionResult(SampleCollection sampleCollection,
																long start, Duration duration) {

		this.sampleCollection = sampleCollection.clone(); // Perform a deep copy.
		this.start = start;
		this.duration = duration;
	}

	public long getStart() {
		return start;
	}

	public Duration getDuration() {
		return duration;
	}

	public SampleCollection getSampleCollection() {
		return sampleCollection;
	}
}
