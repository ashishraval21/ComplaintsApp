package com.municipal.complaintsapp.classes;

import java.util.List;

public class AddComplaint extends Complaint{
    private List<String> images;

    private Double Latitude;

    private Double Longitude;

    public AddComplaint(int userId, int complaintType, String title, String description, List<String> fileNames,
                        Double latitude, Double longitude) {
        super(userId, complaintType, title, description);
        this.images = fileNames;
        this.Latitude = latitude;
        this.Longitude = longitude;

    }

    public List<String> getImages() {
        return images;
    }
}
