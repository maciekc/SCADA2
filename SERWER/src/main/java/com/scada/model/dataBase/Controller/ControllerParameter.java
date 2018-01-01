package com.scada.model.dataBase.Controller;

public class ControllerParameter {

    private String tag;
    private double value;

    public ControllerParameter(String tag, double value) {
        this.tag = tag;
        this.value = value;
    }

    public ControllerParameter() {
        this.tag = "";
        this.value = 0.0;
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
