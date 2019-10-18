package com.example.easydriveapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userfile {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String _id;

    public Userfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Userfile{" +
                "name='" + name + '\'' +
                ", _id='" + _id + '\'' +
                '}';
    }
}
