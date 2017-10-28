package com.scada.model.dataBase.ChangeParameterValue;

import java.io.Serializable;

public class ChangeParameterValue implements Serializable {

    private Integer id;
    private String systemParameterName;
    private Double value;
    private String date;

    public ChangeParameterValue(Integer id, String systemParameterName, Double value, String date) {
        this.id = id;
        this.systemParameterName = systemParameterName;
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSystemParameterName() {
        return systemParameterName;
    }

    public void setSystemParameterName(String stateSpaceName) {
        this.systemParameterName = stateSpaceName;
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
