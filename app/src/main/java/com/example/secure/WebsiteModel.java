package com.example.secure;

public class WebsiteModel {
    private int id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String note;

    // Constructors

    public WebsiteModel(int id,String name, String url, String username, String password, String note) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public WebsiteModel(){

    }
    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }

    // Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
