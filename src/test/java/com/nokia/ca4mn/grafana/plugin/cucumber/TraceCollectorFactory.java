package com.nokia.ca4mn.grafana.plugin.cucumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;

public class TraceCollectorFactory {

    private static TraceCollectorFactory instance = new TraceCollectorFactory();
    List<AbstractTraceCollector> traceCollectors = new ArrayList<>();

    private TraceCollectorFactory() {
    }

    public static TraceCollectorFactory getInstance() {
        return instance;
    }

    public void registerTraceCollector(AbstractTraceCollector... collectors) {
        traceCollectors.addAll(Arrays.asList(collectors));
    }

    public List<AbstractTraceCollector> getTraceCollectors() {
        return traceCollectors;
    }

    public void clearTraceCollectors() {
        for (AbstractTraceCollector collector : traceCollectors) {
            collector.clearTraces();
        }
        traceCollectors.clear();
    }

    public static abstract class AbstractTraceCollector {

        List<String> traces = new ArrayList<>();

        public void clearTraces() {
            traces.clear();
        }

        public List<String> getTraces() {
            return traces;
        }

        public synchronized long matchCount(String regex) {
            final boolean regexContainBr = regex.indexOf('\n') != -1;
            return traces.stream().filter(t -> {
                if (!regexContainBr) {
                    int brPos = t.indexOf('\n');
                    if (brPos != -1) {
                        t = StringUtils.remove(t.substring(0, brPos), '\r');
                    }
                }
                return t.matches(regex);
            }).count();
        }

        public synchronized Optional<Matcher> matchLast(String regex) {
            Pattern pattern = Pattern.compile(regex);
            return traces.stream().map(pattern::matcher).filter(Matcher::matches).findFirst();
        }

        public synchronized void setTrace(String trace) {
            traces.add(trace);
        }

        public abstract boolean isInterested(LogEvent event);

    }

}
