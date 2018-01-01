package com.scada.model.dataBase.ChangeParameterValue;

import java.io.Serializable;

public class ChangeParameterValue implements Serializable {

    private Integer id;
    private String stateSpaceTag;
    private Double value;
    private String date;

    public ChangeParameterValue(Integer id, String systemParameterName, Double value, String date) {
        this.id = id;
        this.stateSpaceTag = systemParameterName;
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
