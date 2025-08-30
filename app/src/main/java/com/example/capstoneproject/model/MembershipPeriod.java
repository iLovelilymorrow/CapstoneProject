package com.example.capstoneproject.model;

import com.google.firebase.Timestamp;

public class MembershipPeriod
{
    private String membershipPeriodID;
    private String membershipTypeID;
    private String receiptID;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean isActive;

    public MembershipPeriod(String membershipPeriodID, String membershipTypeID, String receiptID, Timestamp startDate, Timestamp endDate, boolean isActive) {
        this.membershipPeriodID = membershipPeriodID;
        this.membershipTypeID = membershipTypeID;
        this.receiptID = receiptID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public String getMembershipPeriodID() {
        return membershipPeriodID;
    }

    public String getMembershipTypeID() {
        return membershipTypeID;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return isActive;
    }
}