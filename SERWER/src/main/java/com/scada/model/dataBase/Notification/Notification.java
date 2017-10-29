package com.scada.model.dataBase.Notification;

import java.io.Serializable;

public class Notification implements Serializable {

    private Integer id;
    private String variableTag;
    private String limitTag;
    private Double value;
    private Integer type;
    private String date;

    public Notification(Integer id, String variableTag, String limitTag, Double value, Integer type, String date) {

        this.id = id;
        this.variableTag = variableTag;
        this.limitTag = limitTag;
        this.value = value;
        this.type = type;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVariableTag() {
        return variableTag;
    }

    public void setVariableTag(String variableTag) {
        this.variableTag = variableTag;
    }

    public String getLimitTag() {
        return limitTag;
    }

    public void setLimitTag(String limitTag) {
        this.limitTag = limitTag;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
