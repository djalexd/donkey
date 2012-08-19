package com.questdome.donkey.core;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.google.common.base.Preconditions;
import com.ning.http.client.*;
import com.questdome.donkey.core.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @author padawan
 * @since 8/18/12 12:11 AM
 */
public class HttpSampler2 extends UntypedActor {
	private static final Logger LOG = LoggerFactory.getLogger(HttpSampler2.class);

	private final int numWorkers;

	private final ActorRef listener;
	private final ActorRef generator;
	private final ActorRef workersRouter;

	private Request request;

	private final SampleCollection sampleCollection;
	private long numExpectedSamples;

	private long start;

	public HttpSampler2(int numWorkers, final EventBasedTriggerGenerator generator, ActorRef listener) {
		this.numWorkers = numWorkers;
		this.sampleCollection = new SampleCollection(10);

		this.generator = this.getContext()
				.actorOf(new Props(new UntypedActorFactory() {
					@Override
					public Actor create() {
						return new ActorEventGenerator(generator);
					}
				}));
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
			generator.tell(new TriggerStartEvent(compute.getDuration(), compute.getTimeUnit()));

		} else if (message instanceof TriggerEvent) {
			Preconditions.checkNotNull(this.request, "Request is null, send Compute message first");

			// Create the request
			workersRouter.tell(new HttpWorkRequest(request), getSelf());
		} else if (message instanceof TriggerFinishedEvent) {
			// Store the number of expected samples. This will surely occur before
			// all HttpWorkResults get back.
			numExpectedSamples = ((TriggerFinishedEvent) message).getNumGeneratedEvents();
			checkSampleCollectionAndNotify();

		} else if (message instanceof HttpWorkResult) {
			// Add the sample to collection.
			HttpWorkResult result = (HttpWorkResult) message;
			sampleCollection.addSample(result.getSample());

			LOG.debug("Received work result {} ms", result.getSample().getDuration());
			checkSampleCollectionAndNotify();

		} else {
			unhandled(message);

		}
	}


	private void checkSampleCollectionAndNotify() {
		if (sampleCollection.getNumSamples() == this.numExpectedSamples) {
			// We're done.
			Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
			listener.tell(new SampleCollectionResult(this.sampleCollection, start, duration), getSelf());
			getContext().stop(getSelf());
		}
	}
}


