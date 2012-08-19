package com.questdome.donkey.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.google.common.base.Preconditions;
import com.ning.http.client.*;
import com.questdome.donkey.core.messages.*;

import java.util.concurrent.TimeUnit;


/**
 * @author padawan
 * @since 8/18/12 12:11 AM
 */
public class HttpSampler2 extends UntypedActor {

	private final int numWorkers;

	private final ActorRef listener;
	private final ActorRef workersRouter;

	private Request request;

	private final SampleCollection sampleCollection;
	private long numExpectedSamples;

	private long start;

	public HttpSampler2(int numWorkers, ActorRef listener) {
		this.numWorkers = numWorkers;
		this.sampleCollection = new SampleCollection(10);

		this.listener = listener;
		this.workersRouter = this.getContext()
				.actorOf(new Props(HttpSampler2Worker.class)
						.withRouter(new RoundRobinRouter(numWorkers)), "workerRouter");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Compute) {
			Compute compute = (Compute) message;

			// Let's start
			this.start = System.currentTimeMillis();
			this.request = compute.getRequest();

			// And send a start event
			getSender().tell(
					new TriggerStartEvent(compute.getDuration(), compute.getTimeUnit()));

		} else if (message instanceof TriggerEvent) {
			Preconditions.checkNotNull(this.request, "Request is null, send Compute message first");

			// Create the request
			workersRouter.tell(new HttpWorkRequest(request));
		} else if (message instanceof TriggerFinishedEvent) {
			// Store the number of expected samples. This will surely occur before
			// all HttpWorkResults get back.
			numExpectedSamples = ((TriggerFinishedEvent) message).getNumGeneratedEvents();

		} else if (message instanceof HttpWorkResult) {
			// Add the sample to collection.
			HttpWorkResult result = (HttpWorkResult) message;
			sampleCollection.addSample(result.getSample());

			if (sampleCollection.getNumSamples() == this.numExpectedSamples) {
				// We're done.
				Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
				listener.tell(new SampleCollectionResult(this.sampleCollection, start, duration), getSelf());
				getContext().stop(getSelf());
			}

		} else {
			unhandled(message);

		}
	}
}


