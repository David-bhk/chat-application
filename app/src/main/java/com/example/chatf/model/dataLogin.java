package com.example.chatf.model;

public class dataLogin {
    private String key;
    private String username;
    private String name;
    private String password;
    private String image;

    public dataLogin() {

    }

    public dataLogin(String username, String name, String password, String image) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
