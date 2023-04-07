package com.municipal.complaintsapp.classes;

public class Complaint {

    private int UserId;
    private String Title;
    private String Description;

    public Complaint(int userId, String title, String description) {
        UserId = userId;
        Title = title;
        Description = description;
    }

    public int getUserId() {
        return UserId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }
}
