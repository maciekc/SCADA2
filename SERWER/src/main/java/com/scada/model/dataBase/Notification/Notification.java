package com.scada.model.dataBase.Notification;

import java.io.Serializable;

public class Notification implements Serializable {

    private Integer id;
    private String name;
    private String limitName;
    private Double value;
    private Integer type;
    private String date;

    public Notification(Integer id, String name, String limitName, Double value, Integer type, String date) {

        this.id = id;
        this.name = name;
        this.limitName = limitName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLimitName() {
        return limitName;
    }

    public void setLimitName(String limitName) {
        this.limitName = limitName;
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
