package com.questdome.donkey.core;


import akka.actor.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.questdome.donkey.core.messages.Compute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author padawan
 * @since 8/19/12 11:48 PM
 */
public class PerfSeesm {
	private static final Logger LOG = LoggerFactory.getLogger(PerfSeesm.class);

/*	private static ActorSystem actorSystem;

	@BeforeClass
	public static void setupActorSystem() {
		actorSystem = akka.actor.ActorSystem.create("HttpSampler_System");
	}

	@AfterClass
	public static void tearDownActorSystem() {
		if (actorSystem != null) {
			actorSystem.shutdown();
		}
	}*/

	//@Test
	public static void main(String[] args) {
		ActorSystem actorSystem = akka.actor.ActorSystem.create("HttpSampler_System");
		//
		final EventBasedTriggerGenerator generator =
				new ConstantFunctionEventGenerator(1, TimeUnit.SECONDS);

		final ActorRef listener = actorSystem.actorOf(new Props(DisplayResultsListener.class), "listener");

		ActorRef sampler = actorSystem.actorOf(new Props(new akka.actor.UntypedActorFactory() {
			public Actor create() {
				return new HttpSampler2(4, generator, listener);
			}
		}), "sampler");

		// Start
		final Request request = new RequestBuilder("GET").setUrl("https://seesmic.com").build();
		sampler.tell(new Compute(request, 2L, TimeUnit.SECONDS));
	}
}
