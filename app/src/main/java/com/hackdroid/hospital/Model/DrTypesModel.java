package com.hackdroid.hospital.Model;

public class DrTypesModel {
String id , type,image ;

    public DrTypesModel() {
    }

    public DrTypesModel(String id, String type, String image) {
        this.id = id;
        this.type = type;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
