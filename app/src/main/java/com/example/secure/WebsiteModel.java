package com.example.secure;

public class WebsiteModel {
    private int id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String note;
    private String web_logo;

    // Constructors


    public WebsiteModel(int id, String name, String url, String username, String password, String note, String web_logo) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.note = note;
        this.web_logo = web_logo;
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

    public String getusername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }

    public String getWeb_logo() { return web_logo; }


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

    public void setusername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setWeb_logo(String web_logo) {
        this.web_logo = web_logo;
    }

    //to string
    @Override
    public String toString() {
        return "WebsiteModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", note='" + note + '\'' +
                ", web_logo='" + web_logo + '\'' +
                '}';
    }
}
