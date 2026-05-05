package com.example.servicerequestapp;

public class RequestModel {
    public int id;
    public String title;
    public String status;

    public RequestModel(int id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
}