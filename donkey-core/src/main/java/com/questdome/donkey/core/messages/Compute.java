package com.questdome.donkey.core.messages;

import com.ning.http.client.Request;

import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/18/12 6:30 PM
 */
public class Compute {
	Request request;

	long duration;
	TimeUnit timeUnit;

	public Compute(Request request, long duration, TimeUnit timeUnit) {
		this.request = request;
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	public Request getRequest() {
		return request;
	}

	public long getDuration() {
		return duration;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
