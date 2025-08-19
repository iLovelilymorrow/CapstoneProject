package com.example.capstoneproject.model; // Or your chosen package

import com.google.firebase.Timestamp; // Important: Use Firebase Timestamp
import com.google.firebase.firestore.ServerTimestamp; // For creationDate
import com.google.firebase.firestore.IgnoreExtraProperties; // Good practice

@IgnoreExtraProperties // Recommended: Prevents crashes if Firestore has extra fields not in your class
public class MembershipType {

    // Firestore document fields
    private String name;
    private int duration; // Assuming duration is in days (integer)
    private String description;
    private double price; // Use double for monetary values, or long if storing cents as integer
    @ServerTimestamp // Automatically populates with server time on creation
    private Timestamp creationDate;
    private Timestamp untilDate; // Can be null

    // IMPORTANT: A public no-argument constructor is required for Firestore
    public MembershipType() {
        // Firestore needs this for deserialization
    }

    // Constructor with parameters (optional, but convenient for creating new objects)
    public MembershipType(String name, int duration, String description, double price, Timestamp untilDate) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.untilDate = untilDate;
        // creationDate will be set by @ServerTimestamp
    }

    // --- Getters and Setters for all fields ---
    // Firestore uses these to map data to/from the document

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
