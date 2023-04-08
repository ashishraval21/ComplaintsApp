package com.municipal.complaintsapp.complaint;

import java.io.Serializable;
import java.util.Date;

public class Attachment implements Serializable {

    private int ComplaintId;
    private String FileName;
    private String FilePath;
    private boolean IsDeleted;
    private Date CreatedDate;

    public Attachment(int complaintId, String fileName, String filePath, boolean isDeleted, Date createdDate) {
        ComplaintId = complaintId;
        FileName = fileName;
        FilePath = filePath;
        IsDeleted = isDeleted;
        CreatedDate = createdDate;
    }

    public int getComplaintId() {
        return ComplaintId;
    }

    public String getFileName() {
        return FileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }
}
