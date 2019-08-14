package com.nokia.ca4mn.grafana.plugin.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * https://testingneeds.wordpress.com/2015/09/15/junit-runner-with-cucumberoptions/
 * https://docs.cucumber.io/cucumber/api/#junit
 * 
 * @tag '-Dcucumber.options="--tags @chunhui"'
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:build/reports/cucumber",
        "json:build/test-results/cucumber/cucumber.json" }, monochrome = true, features = "src/test", glue = "com.nokia.ca4mn")
public class JUnitTest {

}
