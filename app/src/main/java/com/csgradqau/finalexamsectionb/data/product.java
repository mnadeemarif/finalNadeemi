package com.csgradqau.finalexamsectionb.data;

public class product {

    private String id,disc,type,quantity,location,price,addedby;
    public static String ADDED_BY;
    public product(){}

    public product(String id, String disc,String type, String quantity, String location, String price) {
        this.id = id;
        this.disc = disc;
        this.type = type;
        this.quantity = quantity;
        this.location = location;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
