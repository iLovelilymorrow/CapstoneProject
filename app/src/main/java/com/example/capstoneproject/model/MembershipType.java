package com.example.capstoneproject.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MembershipType
{
    private String membershipTypeID;
    private String name;
    private int duration;
    private String description;
    private double price;
    @ServerTimestamp
    private Timestamp creationDate;
    private Timestamp untilDate; // Can be null

    public MembershipType()
    {

    }

    public MembershipType(String membershipTypeID, String name, int duration, String description, double price, Timestamp untilDate)
    {
        this.membershipTypeID = membershipTypeID;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.untilDate = untilDate;
        // creationDate will be set by @ServerTimestamp
    }

    public String getMembershipTypeID() {
        return membershipTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Timestamp untilDate) {
        this.untilDate = untilDate;
    }

    @Override
    public String toString() {
        return "MembershipType{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate=" + (creationDate != null ? creationDate.toDate().toString() : "null") + // Format timestamp
                ", untilDate=" + (untilDate != null ? untilDate.toDate().toString() : "null") + // Format timestamp
                '}';
    }
}
