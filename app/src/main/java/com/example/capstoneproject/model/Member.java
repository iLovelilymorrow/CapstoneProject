package com.example.capstoneproject.model;

import com.google.firebase.Timestamp;

public class Member
{
    private String memberID;
    private String membershipPeriodID;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String routineID;
    private boolean trialMode;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Member(String memberID, String membershipPeriodID, String firstName, String lastName, int age, String gender, String routineID, boolean trialMode, Timestamp createdAt, Timestamp updatedAt) {
        this.memberID = memberID;
        this.membershipPeriodID = membershipPeriodID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.routineID = routineID;
        this.trialMode = trialMode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getMembershipPeriodID() {
        return membershipPeriodID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public boolean isTrialMode() {
        return trialMode;
    }

    public String getRoutineID() {
        return routineID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}