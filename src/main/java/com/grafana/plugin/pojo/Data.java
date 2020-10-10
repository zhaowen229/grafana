package com.grafana.plugin.pojo;

import java.util.List;

public class Data {

    private Range range;
    private List<Targets> targets;

    public void setRange(Range range) {
        this.range = range;
    }

    public Range getRange() {
        return range;
    }

    public void setTargets(List<Targets> targets) {
        this.targets = targets;
    }

    public List<Targets> getTargets() {
        return targets;
    }

}
