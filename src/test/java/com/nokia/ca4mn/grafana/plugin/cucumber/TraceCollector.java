package com.nokia.ca4mn.grafana.plugin.cucumber;

import org.apache.logging.log4j.core.LogEvent;

import com.nokia.ca4mn.grafana.plugin.cucumber.TraceCollectorFactory.AbstractTraceCollector;

public class TraceCollector extends AbstractTraceCollector {

    @Override
    public boolean isInterested(LogEvent event) {
        return event.getLoggerName().startsWith("com.nokia.ca4mn.ingester");
    }

}
