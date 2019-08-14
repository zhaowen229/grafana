package com.nokia.ca4mn.grafana.plugin.cucumber;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.nokia.ca4mn.grafana.plugin.cucumber.TraceCollectorFactory.AbstractTraceCollector;

@Plugin(name = "UnitTest", category = "Core", elementType = "appender", printObject = true)
public class TraceAppender extends AbstractAppender {

    protected TraceAppender(String name, Filter filter, Layout<? extends Serializable> layout,
            boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        for (AbstractTraceCollector collector : TraceCollectorFactory.getInstance().getTraceCollectors()) {
            if (collector.isInterested(event)) {
                collector.setTrace(event.getLevel() + ": " + event.getMessage().getFormattedMessage());
            }
        }
    }

    @PluginFactory
    public static TraceAppender createAppender(@PluginAttribute("name") String name,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("No name provided for customized TraceAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new TraceAppender(name, filter, layout, ignoreExceptions);
    }

}
