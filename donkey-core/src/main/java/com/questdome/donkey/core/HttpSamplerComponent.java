package com.questdome.donkey.core;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.Response;

import java.io.IOException;

/**
 * Wrap a http client, so a large number of requests can be paralelized.
 */
class HttpSamplerComponent {
	private AsyncHttpClient asyncHttpClient;
	private SampleCollection sampleCollection;

	HttpSamplerComponent(AsyncHttpClient asyncHttpClient, SampleCollection sampleCollection) {
		this.asyncHttpClient = asyncHttpClient;
		this.sampleCollection = sampleCollection;
	}

	// Doesn't need to be synchronized or anything
	void execute(final Request request) {

		try {
			final Long start = System.currentTimeMillis();
			asyncHttpClient.executeRequest(request, new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(Response response) throws Exception {
					final long end = System.currentTimeMillis();
					final Sample sample = new Sample(start, end - start,
							request.getUrl(),
							response.getStatusCode(),
							response.getResponseBodyAsBytes().length);
					sampleCollection.addSample(sample);
					return sample;
				}
			});

		} catch (IOException e) {
			// TODO Put this to sample collection
		}
	}
}
