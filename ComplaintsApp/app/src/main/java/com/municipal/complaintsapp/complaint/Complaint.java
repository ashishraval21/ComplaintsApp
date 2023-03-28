package com.municipal.complaintsapp.complaint;

import java.io.Serializable;
import java.util.Date;

public class Complaint  implements Serializable {
    private String title;

    private String description;
    private String status;
    private Date date;

    public Complaint(String title, String status, Date date, String description) {
        this.title = title;
        this.status = status;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }
    public String getDescription(){
        return description;
    }


}
