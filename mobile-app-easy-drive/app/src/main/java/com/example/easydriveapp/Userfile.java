package com.example.easydriveapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Userfile {
    @SerializedName("filename")
    @Expose
    private String name;

    @SerializedName("ID")
    @Expose
    private String _id;


    @SerializedName("extension")
    @Expose
    private String fileExtension;


    enum FileType {
        IMAGE,
        AUDIO,
        VIDEO,
        PDF,
        TEXT,
        UNKNOWN
    }

    public Userfile() {
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


    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        if (fileExtension != null) {
            return fileExtension;
        } else {
            return "";
        }
    }

    public void setFileType(){}
    public void getFileType(){}

    public FileType decideFileType() {

        if (fileExtension != null) {

            if (fileExtension.equals("jpg") || fileExtension.equals("png")) {
                return FileType.IMAGE;
            }
            if (fileExtension.equals("mp3")) {
                return FileType.AUDIO;
            }
            if (fileExtension.equals("pdf")) {
                return FileType.PDF;
            }
            if (fileExtension.equals("txt") || fileExtension.equals("xml")) {
                return FileType.TEXT;
            } else {
                return FileType.UNKNOWN;
            }
        } else {
            return FileType.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return "Userfile{" +
                "name='" + name + '\'' +
                ", _id=" + _id +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}
