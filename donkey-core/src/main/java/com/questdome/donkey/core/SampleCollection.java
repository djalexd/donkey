package com.questdome.donkey.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.SummaryStatistics;
import org.apache.commons.math.stat.SummaryStatisticsImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a collection of samples. The purpose of this wrapper is to
 * provide an end-of-collection concept (when a samples is done collecting
 * data).
 *
 * @author padawan
 * @since 8/16/12 7:41 PM
 */
public class SampleCollection implements Cloneable {
	private final List<Sample> samples;

	public SampleCollection(int initialCapacity) {
		this.samples = new ArrayList<Sample>(initialCapacity);
	}

	public SampleCollection(List<Sample> samples) {
		this.samples = Lists.newArrayList(samples);
	}

	public void addSample(Sample sample) {
		samples.add(sample);
	}

	public List<Sample> getSamples() {
		return ImmutableList.copyOf(this.samples);
	}

	@Override
	public synchronized SampleCollection clone() {
		return new SampleCollection(this.getSamples());
	}

	public SummaryStatistics getDurationStats() {
		final SummaryStatistics statistics = new SummaryStatisticsImpl();
		for (Sample s : samples) {
			statistics.addValue(s.getDuration());
		}
		return statistics;
	}

	public Frequency getStatusCodeFrequency() {
		final Frequency frequency = new Frequency();
		for (Sample s : samples) {
			frequency.addValue(s.getStatusCode());
		}
		return frequency;
	}

	public SummaryStatistics getSizeStats() {
		return null;
	}

	public synchronized int getNumSamples() {
		return samples.size();
	}
}
