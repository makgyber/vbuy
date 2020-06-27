package com.makgyber.vbuys;

import java.util.ArrayList;

public class Product {
    private String id;
    private String productName;
    private String description;
    private String tindahanName;
    private String tindahanId;
    private Double price;
    private Boolean publish;
    private ArrayList<String> tags;
    private ArrayList<String> serviceArea;

    public Product() {
        //need empty constructor
    }

    public Product(String productName, String description, String tindahanName, String tindahanId, Double price, Boolean publish, ArrayList<String> tags, ArrayList<String> serviceArea) {
        this.productName = productName;
        this.description = description;
        this.tindahanName = tindahanName;
        this.tindahanId = tindahanId;
        this.price = price;
        this.publish = publish;
        this.tags = tags;
        this.serviceArea = serviceArea;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public String getTindahanName() {
        return tindahanName;
    }

    public String getTindahanId() {
        return tindahanId;
    }

    public Boolean getPublish() {
        return publish;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<String> getServiceArea() {
        return serviceArea;
    }

    public Double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
