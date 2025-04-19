package com.example.connect_database;

public class userModel {
    public byte[] photo;
    public String name;
    public int age;
    public double height;
    public int weight;
    public String bloodGroup;
    public String medicalIssue;

    public userModel(byte[] photo, String name, int age, double height, int weight, String bloodGroup, String medicalIssue) {
        this.photo = photo;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
        this.medicalIssue = medicalIssue;
    }
}

