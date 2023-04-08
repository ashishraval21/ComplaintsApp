package com.municipal.complaintsapp.complaint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Complaint  implements Serializable {

    private int Id;
    private String Title;

    private String Description;
    private String Status;
    private Date Date;

    private List<Attachment> Attachments;

    private String Latitude;

    private String Longitude;

    public Complaint( int id, String title, String status, Date date, String description, List<Attachment> Attachments, String Latitude, String Longitude) {
        this.Id = id;
        this.Title = title;
        this.Status = status;
        this.Date = date;
        this.Description = description;
        this.Attachments =Attachments;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public String getTitle() {
        return Title;
    }

    public String getStatus() {
        return Status;
    }

    public Date getDate() {
        return Date == null ? new Date() : Date;
    }
    public String getDescription(){
        return Description;
    }

    public int getId() {
        return Id;
    }

    public List<Attachment> getAttachments() {
        return Attachments;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }
}
