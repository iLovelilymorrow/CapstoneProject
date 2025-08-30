package com.example.capstoneproject.model;

import com.google.firebase.Timestamp;

public class Receipt
{
    private String receiptID;
    private Timestamp dateIssued;
    private double cost;
    private String adminID;
    private String memberID;
    private String membershipPeriodID;
    private String membershipTypeID;
    private String paymentMethod;

    public Receipt(String receiptID, Timestamp dateIssued, double cost, String adminID, String memberID, String membershipPeriodID, String membershipTypeID, String paymentMethod) {
        this.receiptID = receiptID;
        this.dateIssued = dateIssued;
        this.cost = cost;
        this.adminID = adminID;
        this.memberID = memberID;
        this.membershipPeriodID = membershipPeriodID;
        this.membershipTypeID = membershipTypeID;
        this.paymentMethod = paymentMethod;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public Timestamp getDateIssued() {
        return dateIssued;
    }

    public double getCost() {
        return cost;
    }

    public String getAdminID() {
        return adminID;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getMembershipPeriodID() {
        return membershipPeriodID;
    }

    public String getMembershipTypeID() {
        return membershipTypeID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
