package com.scada.model.dataBase.Andon;

import java.io.Serializable;

public class Andon implements Serializable{

    private Integer id;
    private String stateSpaceTag;
    private String limitTag;
    private Double value;
    private String date;
    private Integer type;

    public Andon(Integer id, String stateSpaceTag, String limit, Double value, String date, Integer type) {
        this.id = id;
        this.stateSpaceTag = stateSpaceTag;
        this.limitTag = limit;
        this.value = value;
        this.date = date;
        this.type = type;
    }
    public Andon(Integer id, String limit, Double value, String date, Integer type) {
        this.id = id;
        this.stateSpaceTag = "";
        this.limitTag = limit;
        this.value = value;
        this.date = date;
        this.type = type;
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

    public String getLimitTag() {
        return limitTag;
    }

    public void setLimitTag(String limit) {
        this.limitTag = limit;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
