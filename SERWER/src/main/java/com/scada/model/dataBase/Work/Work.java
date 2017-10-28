package com.scada.model.dataBase.Work;

import java.io.Serializable;

public class Work implements Serializable{

    private Integer id;
    private String stateSpaceName;
    private Double value;
    private String date;

    public Work(Integer id, String stateSpaceName, Double value, String date) {
        this.id = id;
        this.stateSpaceName = stateSpaceName;
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
