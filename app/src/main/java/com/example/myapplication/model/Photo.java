package com.example.myapplication.model;

public class Photo {
    private String id;
    private String url;
    private String name;


    public Photo() { }


    public Photo(String id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public String getId() { return id; }
    public String getUrl() { return url; }
    public String getName() { return name; }
}