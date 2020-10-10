package com.grafana.plugin.cucumber;

import org.apache.logging.log4j.core.LogEvent;

import com.grafana.plugin.cucumber.TraceCollectorFactory.AbstractTraceCollector;

public class TraceCollector extends AbstractTraceCollector {

	@Override
	public boolean isInterested(LogEvent event) {
		return event.getLoggerName().startsWith("com");
	}

}
