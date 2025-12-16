package com.adrar.videogame.entity;

public class Device {

    //Attibuts
    private Integer id;
    private String name;
    private String type;
    private String manufacturer;

    //Constructeurs
    public Device(){}
    public Device(String name, String type, String manufacturer) {
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
