package com.scada.model.dataBase.StateVariable;

import java.io.Serializable;

public class StateVariable implements Serializable{
    private String tag;
    private  String name;
    private  Integer id;

    public StateVariable(Integer id, String tag, String name) {
        this.tag = tag;
        this.name = name;
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

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
