package com.makgyber.vbuys.models;


import com.google.firebase.Timestamp;

import java.util.List;

public class Invoice {
    private String buyerId;
    private String buyerName;
    private String sellerId;
    private String sellerName;
    private Timestamp deliverByDate;
    private Timestamp dateCompleted;
    private Timestamp dateCreated;
    private Timestamp lastUpdated;
    private String status;
    private Number totalAmount;
    private List<InvoiceItem> items;
    private String delivery;
    private String paymentMethod;

}
