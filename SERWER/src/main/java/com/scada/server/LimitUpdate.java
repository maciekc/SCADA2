package com.scada.server;

public class LimitUpdate {
    private String tag;
    private double value;

    public LimitUpdate(String tag, double value) {
        this.tag = tag;
        this.value = value;
    }
    public LimitUpdate(String tag) {
        this.tag = tag;
        this.value = 0.;
    }

    public LimitUpdate() {
        this.tag = "";
        this.value = 0.;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
