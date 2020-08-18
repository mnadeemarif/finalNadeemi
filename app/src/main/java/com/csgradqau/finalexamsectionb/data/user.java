package com.csgradqau.finalexamsectionb.data;

public class user {

    private String id,name,username,doj,password,gender,type,marketingSector;

    public user() {
    }

    public user(String id, String name, String username, String doj, String password, String gender, String type, String marketingSector) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.doj = doj;
        this.password = password;
        this.gender = gender;
        this.type = type;
        this.marketingSector = marketingSector;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarketingSector() {
        return marketingSector;
    }

    public void setMarketingSector(String marketingSector) {
        this.marketingSector = marketingSector;
    }
}
