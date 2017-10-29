package com.scada.model.dataBase.Work;

import java.io.Serializable;

public class Work implements Serializable{

    private Integer id;
    private String stateSpaceTag;
    private Double value;
    private String date;

    public Work(Integer id, String stateSpaceTag, Double value, String date) {
        this.id = id;
        this.stateSpaceTag = stateSpaceTag;
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
