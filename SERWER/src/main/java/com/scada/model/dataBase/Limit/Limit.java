package com.scada.model.dataBase.Limit;

import java.io.Serializable;

public class Limit implements Serializable {

    private Integer id;
    private String tag;
    private String name;
    private String stateSpaceTag;
    private Double value;
    private Integer type;

    public Limit(Integer id, String tag, String name, String stateSpaceTag, Double value, Integer type) {
        this.id = id;
        this.tag = tag;
        this.name = name;
        this.stateSpaceTag = stateSpaceTag;
        this.value = value;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateSpaceTag() {
        return stateSpaceTag;
    }

    public void setStateSpaceTag(String stateSpaceName) {
        this.stateSpaceTag = stateSpaceName;
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
}
