package com.example.easydriveapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Userfile {
    @SerializedName("filename")
    @Expose
    private String name;

    @SerializedName("ID")
    @Expose
    private int _id;


    @SerializedName("extension")
    @Expose
    private String fileExtension;

    enum FileType {
        IMAGE,
        AUDIO,
        VIDEO,
        PDF,
        TEXT
    }

    public Userfile(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int get_id() {
        return _id;
    }
    public String getExtension() {
        return fileExtension;
    }


    public FileType getFileType() {
        if (fileExtension.equals("jpg") || name.contains("png")) {
            return FileType.IMAGE;
        }
        if (fileExtension.equals("mp3")) {
            return FileType.AUDIO;
        }
        if (fileExtension.equals("pdf")) {
            return FileType.PDF;
        }
        if (fileExtension.equals("txt") || name.contains("xml")) {
            return FileType.TEXT;
        }
        else {
            return null;
        }
    }
}
