package com.csgradqau.finalexamsectionb.data;

public class user {

    public static final String TABLE_NAME = "User";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SECTOR = "sector";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DOJ = "doj";
    public static final String COLUMN_type = "type";
    public static final String COLUMN_TIMESTAMP = "timestamp";


    public String id;
    private String name,username,doj,password,gender,type,marketingSector,image;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_SECTOR + " TEXT,"
                    + COLUMN_GENDER + " TEXT,"
                    + COLUMN_type + " TEXT,"
                    + COLUMN_DOJ + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public user(String id, String name, String username, String doj, String password, String gender, String type, String marketingSector, String image) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.doj = doj;
        this.password = password;
        this.gender = gender;
        this.type = type;
        this.marketingSector = marketingSector;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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
