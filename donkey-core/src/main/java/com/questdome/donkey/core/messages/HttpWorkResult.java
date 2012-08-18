package com.questdome.donkey.core.messages;

import com.questdome.donkey.core.Sample;

/**
 * @author padawan
 * @since 8/18/12 6:31 PM
 */
public class HttpWorkResult {
	Sample sample;
	public HttpWorkResult(Sample sample) {
		this.sample = sample;
	}

	public Sample getSample() {
		return sample;
	}
}
