package com.questdome.donkey.core.messages;

import com.ning.http.client.Request;

/**
 * @author padawan
 * @since 8/18/12 6:30 PM
 */
public class Compute {
	Request request;
	Compute(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}
}
