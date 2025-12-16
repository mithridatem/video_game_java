package com.adrar.videogame.entity;

import java.sql.Date;
import java.util.ArrayList;

public class Game {
    //Attributs
    private Integer id;
    private String title;
    private Date publishedAt;
    private String type;
    private ArrayList<Device> devices;

    //Constructeur
    public Game()
    {
        this.devices = new ArrayList<>();
    }

    public Game(String title, Date publishedAt, String type)
    {
        this.title = title;
        this.publishedAt = publishedAt;
        this.type = type;
        this.devices = new ArrayList<>();
    }

    public Game(String title, Date publishedAt, String type, ArrayList<Device> devices)
    {
        this.title = title;
        this.publishedAt = publishedAt;
        this.type = type;
        this.devices = devices;
    }

    //Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device)
    {
        this.devices.add(device);
    }

    public void removeDevice(Device device)
    {
        this.devices.remove(device);
    }
}
