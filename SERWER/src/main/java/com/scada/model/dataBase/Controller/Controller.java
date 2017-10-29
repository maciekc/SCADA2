package com.scada.model.dataBase.Controller;

import java.io.Serializable;

public class Controller implements Serializable {

    private Integer id;
    private String stateSpaceTag;
    private String variableStateTag;
    private Double value;
    private String date;

    public Controller(Integer id, String stateSpaceTag, String variableStateTag, Double value, String date) {
        this.id = id;
        this.stateSpaceTag = stateSpaceTag;
        this.variableStateTag = variableStateTag;
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateSpaceTag() {
        return stateSpaceTag;
    }

    public void setStateSpaceTag(String stateSpaceTag) {
        this.stateSpaceTag = stateSpaceTag;
    }

    public String getVariableStateTag() {
        return variableStateTag;
    }

    public void setVariableStateTag(String variableStateTag) {
        this.variableStateTag = variableStateTag;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
