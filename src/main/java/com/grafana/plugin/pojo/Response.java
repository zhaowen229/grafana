package com.grafana.plugin.pojo;

import java.util.List;

public class Response {

    private String target;
    private List<List<Double>> datapoints;

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setDatapoints(List<List<Double>> datapoints) {
        this.datapoints = datapoints;
    }

    public List<List<Double>> getDatapoints() {
        return datapoints;
    }

}
