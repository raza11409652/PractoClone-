package com.hackdroid.hospital.Model;

public class Drugs {
    String  id , name , ven , remarks , group ;

    public Drugs() {
    }

    public Drugs(String id, String name, String ven, String remarks, String group) {
        this.id = id;
        this.name = name;
        this.ven = ven;
        this.remarks = remarks;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVen() {
        return ven;
    }

    public void setVen(String ven) {
        this.ven = ven;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
