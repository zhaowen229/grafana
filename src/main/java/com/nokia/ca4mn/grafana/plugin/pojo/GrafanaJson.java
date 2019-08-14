package com.nokia.ca4mn.grafana.plugin.pojo;

import java.util.List;

public class GrafanaJson {

	private Request request;
	private List<Response> response;

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setResponse(List<Response> response) {
		this.response = response;
	}

	public List<Response> getResponse() {
		return response;
	}

}