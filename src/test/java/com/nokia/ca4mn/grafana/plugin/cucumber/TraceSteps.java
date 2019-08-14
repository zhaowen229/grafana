package com.nokia.ca4mn.grafana.plugin.cucumber;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import junit.framework.TestCase;

public class TraceSteps {
    private TraceCollector tracer = new TraceCollector();

    @Before("@trace")
    public void setUpTrace() {
        TraceCollectorFactory.getInstance().registerTraceCollector(tracer);
    }

    @After("@trace")
    public void tearDownTrace() {
        TraceCollectorFactory.getInstance().clearTraceCollectors();
    }

    @Then("log wrote {int} time(s) for {string}")
    public void log_wrote_times_for(Integer wantedlogTimes, String logPattern) throws InterruptedException {
        log_wrote_times_for_in_at_most_seconds(wantedlogTimes, logPattern, 7);
    }

    @Then("log wrote {int} time(s) for {string} in at most {int} second(s)")
    public void log_wrote_times_for_in_at_most_seconds(Integer wantedlogTimes, String logPattern, Integer timeout)
            throws InterruptedException {
        try {
            Awaitility.await("trace").atMost(timeout, TimeUnit.SECONDS)
                    .until(() -> tracer.matchCount(logPattern) == wantedlogTimes);
        } catch (ConditionTimeoutException e) {
            TestCase.fail(e.getMessage() + " Matched count: " + tracer.matchCount(logPattern) + ". Current traces: "
                    + tracer.getTraces());
        }
    }

    @Then("log wrote at least {int} time(s) for {string}")
    public void log_wrote_at_least_times_for(Integer wantedlogTimes, String logPattern) throws InterruptedException {
        log_wrote_at_least_times_for_in_at_most_seconds(wantedlogTimes, logPattern, 20);
    }

    @Then("log wrote at least {int} time(s) for {string} in at most {int} second(s)")
    public void log_wrote_at_least_times_for_in_at_most_seconds(Integer wantedlogTimes, String logPattern,
            Integer timeout) throws InterruptedException {
        try {
            Awaitility.await("trace").atMost(timeout, TimeUnit.SECONDS)
                    .until(() -> tracer.matchCount(logPattern) >= wantedlogTimes);
        } catch (ConditionTimeoutException e) {
            TestCase.fail(e.getMessage() + " Matched count: " + tracer.matchCount(logPattern) + ". Current traces: "
                    + tracer.getTraces());
        }
    }
}
