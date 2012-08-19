package com.questdome.donkey.core;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.questdome.donkey.core.messages.HttpWorkRequest;
import com.questdome.donkey.core.messages.HttpWorkResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author padawan
 * @since 8/18/12 6:32 PM
 */
public class HttpSampler2Worker extends UntypedActor {
	private static Logger LOG = LoggerFactory.getLogger(HttpSampler2Worker.class);

	final AsyncHttpClient asyncHttpClient;
	public HttpSampler2Worker() {
		this.asyncHttpClient = new AsyncHttpClient();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof HttpWorkRequest) {
			final HttpWorkRequest request = (HttpWorkRequest) message;

			LOG.debug("Received work {} {}",
					request.getRequest().getMethod(),
					request.getRequest().getUrl());
			// Store the instance for async call below.
			final ActorRef sender = getSender();

			final long start = System.currentTimeMillis();
			asyncHttpClient.executeRequest(request.getRequest(), new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(Response response) throws Exception {
					final long end = System.currentTimeMillis();

					final Sample sample = new Sample(start, end - start,
							request.getRequest().getUrl(),
							response.getStatusCode(),
							response.getResponseBodyAsBytes().length);

					sender.tell(new HttpWorkResult(sample), getSelf());
					return sample;
				}
			});

		} else {

			unhandled(message);
		}
	}
}
