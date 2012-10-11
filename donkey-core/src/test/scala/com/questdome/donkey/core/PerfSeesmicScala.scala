package com.questdome.donkey.core

import akka.actor.{ActorSystem, Props}
import com.ning.http.client.{RequestBuilder}
import com.questdome.donkey.core.messages.Compute
import com.questdome.donkey.core.{HttpSampler2, DisplayResultsListener, ConstantFunctionEventGenerator}
import java.util.concurrent.TimeUnit

/**
 * User: padawan.
 * Date: 8/22/12
 * Time: 11:45 PM
 */
object PerfSeesmicScala extends App {

	var actorSystem = ActorSystem.create("HttpSampler_System")
	val generator = new ConstantFunctionEventGenerator(2, TimeUnit.SECONDS)
	val listener = actorSystem.actorOf(Props[DisplayResultsListener], "listener")
	val sampler = actorSystem.actorOf(Props(new HttpSampler2(8, generator, listener)), name = "sampler")

	// Start
	val request = new RequestBuilder("GET").setUrl("http://google.com").build
	sampler.tell(new Compute(request, 10L, TimeUnit.SECONDS))
}
