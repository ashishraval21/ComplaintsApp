package com.municipal.complaintsapp.classes;

public class ChangePassword {
    private int id;
    private String CurrentPassword;
    private String NewPassword;

    public ChangePassword(int id, String currentPassword, String newPassword) {
        this.id = id;
        CurrentPassword = currentPassword;
        NewPassword = newPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return CurrentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        CurrentPassword = currentPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
