package com.liez.test;

import java.io.Serializable;

public class RequestResponseBody implements Serializable {

	private static final long serialVersionUID = 1L;

	private String body;

	private Head head;

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Head getHead() {
		return this.head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public String toString() {
		return "RequestResponseBody{body='" + this.body + '\'' + ", head=" + this.head + '}';
	}
}
