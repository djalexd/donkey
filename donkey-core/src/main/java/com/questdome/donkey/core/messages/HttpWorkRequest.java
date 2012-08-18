package com.questdome.donkey.core.messages;

import com.ning.http.client.Request;

/**
 * @author padawan
 * @since 8/18/12 6:31 PM
 */
public class HttpWorkRequest {
	Request request;
	public HttpWorkRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}
}
