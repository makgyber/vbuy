package com.makgyber.vbuys.models;

import java.util.List;

public class Product {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String productName;
    private String description;
    private String tindahanName;
    private String tindahanId;
    private Double price;
    private Boolean publish;
    private List<String> tags;
    private List<String> serviceArea;
    private String imageUri;
    private String category;

    public Product() {
        //need empty constructor
    }

    public Product(String productName,
                   String description,
                   String tindahanName,
                   String tindahanId,
                   Double price,
                   Boolean publish,
                   List<String> tags,
                   List<String> serviceArea,
                   String imageUri,
                   String category) {
        this.productName = productName;
        this.description = description;
        this.tindahanName = tindahanName;
        this.tindahanId = tindahanId;
        this.price = price;
        this.publish = publish;
        this.tags = tags;
        this.serviceArea = serviceArea;
        this.imageUri = imageUri;
        this.category = category;
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

    public List<String> getTags() {
        return tags;
    }

    public List<String> getServiceArea() {
        return serviceArea;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
