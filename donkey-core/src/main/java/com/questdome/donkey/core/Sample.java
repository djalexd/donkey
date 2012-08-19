package com.questdome.donkey.core;

/**
 * @author padawan
 * @since 8/16/12 2:48 AM
 */
public class Sample {

	long start; // UTC
	long duration; // milliseconds, from start to end
	long end; // start + duration

	String uri;

	int statusCode;
	int responseBodySize;

	public Sample(long start, long duration, String uri, int statusCode, int responseBodySize) {
		this.start = start;
		this.duration = duration;
		this.end = this.start + this.duration;
		this.uri = uri;
		this.statusCode = statusCode;
		this.responseBodySize = responseBodySize;
	}

	public long getStart() {
		return start;
	}

	public long getDuration() {
		return duration;
	}

	public long getEnd() {
		return end;
	}

	public String getUri() {
		return uri;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getResponseBodySize() {
		return responseBodySize;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Sample");
		sb.append("{duration=").append(duration);
		sb.append(", uri='").append(uri).append('\'');
		sb.append(", statusCode=").append(statusCode);
		sb.append(", responseBodySize=").append(responseBodySize);
		sb.append('}');
		return sb.toString();
	}
}
