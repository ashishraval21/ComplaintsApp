package com.municipal.complaintsapp.classes;

import java.util.List;

public class AddComplaint extends Complaint{
    private List<String> images;

    public AddComplaint(int userId, String title, String description, List<String> fileNames) {
        super(userId, title, description);
        this.images = fileNames;
    }

    public List<String> getImages() {
        return images;
    }
}
