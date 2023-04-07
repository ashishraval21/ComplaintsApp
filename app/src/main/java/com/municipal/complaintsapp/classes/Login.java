package com.municipal.complaintsapp.classes;

public class Login {

    private String Email;
    private String Password;

    public Login(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
}
