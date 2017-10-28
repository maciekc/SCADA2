package com.scada.model.dataBase.Andon;

import java.io.Serializable;

public class Andon implements Serializable{

    private Integer id;
    private String stateSpaceName;
    private String limitName;
    private Double value;
    private String date;

    public Andon(Integer id, String stateSpaceName, String limit, Double value, String date) {
        this.id = id;
        this.stateSpaceName = stateSpaceName;
        this.limitName = limit;
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateSpaceName() {
        return stateSpaceName;
    }

    public void setStateSpaceName(String stateSpaceName) {
        this.stateSpaceName = stateSpaceName;
    }

    public String getLimitName() {
        return limitName;
    }

    public void setLimitName(String limit) {
        this.limitName = limit;
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
