package com.questdome.donkey.core;

import akka.actor.UntypedActor;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.questdome.donkey.core.messages.HttpWorkRequest;
import com.questdome.donkey.core.messages.HttpWorkResult;

/**
 * @author padawan
 * @since 8/18/12 6:32 PM
 */
class HttpSampler2Worker extends UntypedActor {

	final AsyncHttpClient asyncHttpClient;
	public HttpSampler2Worker() {
		this.asyncHttpClient = new AsyncHttpClient();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof HttpWorkRequest) {
			final HttpWorkRequest request = (HttpWorkRequest) message;

			final long start = System.currentTimeMillis();
			asyncHttpClient.executeRequest(request.getRequest(), new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(Response response) throws Exception {
					final long end = System.currentTimeMillis();

					final Sample sample = new Sample(start, end - start,
							request.getRequest().getUrl(),
							response.getStatusCode(),
							response.getResponseBodyAsBytes().length);

					getSender().tell(new HttpWorkResult(sample), getSelf());
					return sample;
				}
			});
		} else {

			unhandled(message);
		}
	}
}
