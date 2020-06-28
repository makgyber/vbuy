package com.makgyber.vbuys;

import java.util.ArrayList;

public class Tindahan {
    private String tindahanId;
    private String tindahanName;
    private String owner;
    private String contactInfo;
    private String address;
    private String paymentOptions;
    private String deliveryOptions;
    private Boolean publish;
    private ArrayList<String> serviceArea;


    public Tindahan() {
        //need empty constructor
    }

    public Tindahan(String tindahanId, String tindahanName, String owner, String contactInfo, String address, String paymentOptions, String deliveryOptions, Boolean publish, ArrayList<String> serviceArea) {
        this.tindahanId = tindahanId;
        this.tindahanName = tindahanName;
        this.owner = owner;
        this.contactInfo = contactInfo;
        this.address = address;
        this.paymentOptions = paymentOptions;
        this.deliveryOptions = deliveryOptions;
        this.publish = publish;
        this.serviceArea = serviceArea;
    }

    public String getTindahanId() {
        return tindahanId;
    }

    public String getTindahanName() {
        return tindahanName;
    }

    public String getOwner() {
        return owner;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getPublish() {
        return publish;
    }

    public ArrayList<String> getServiceArea() {
        return serviceArea;
    }

    public String getPaymentOptions() {
        return paymentOptions;
    }

    public String getDeliveryOptions() {
        return deliveryOptions;
    }
}
