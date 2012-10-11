package com.questdome.donkey.core;


import akka.actor.*;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.questdome.donkey.core.messages.Compute;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author alex dobjanschi
 * @since 8/19/12 11:48 PM
 */
public class PerfSeesm {
	private static final Logger LOG = LoggerFactory.getLogger(PerfSeesm.class);

	public static void main(String[] args) {
		ActorSystem actorSystem = akka.actor.ActorSystem.create("HttpSampler_System");
		//
		final EventBasedTriggerGenerator generator =
				new ConstantFunctionEventGenerator(2, TimeUnit.SECONDS);

		final ActorRef listener = actorSystem.actorOf(new Props(DisplayResultsListener.class), "listener");

		ActorRef sampler = actorSystem.actorOf(new Props(new akka.actor.UntypedActorFactory() {
			public Actor create() {
				return new HttpSampler2(8, generator, listener);
			}
		}), "sampler");

		// Start
		final Request request = new RequestBuilder("GET").setUrl("http://google.com").build();
		sampler.tell(new Compute(request, 10L, TimeUnit.SECONDS));
	}
}
