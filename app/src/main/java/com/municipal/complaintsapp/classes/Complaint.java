package com.municipal.complaintsapp.classes;

public class Complaint {

    private int UserId;

    private int CategoryId;
    private String Title;
    private String Description;

    public Complaint(int userId, int CategoryId, String title, String description) {
        UserId = userId;
        this.CategoryId = CategoryId;
        Title = title;
        Description = description;
    }

    public int getCategoryId() {
        return CategoryId;
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
