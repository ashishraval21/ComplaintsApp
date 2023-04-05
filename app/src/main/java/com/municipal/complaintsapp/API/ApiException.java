package com.municipal.complaintsapp.API;

import java.io.Serializable;

public class ApiException implements Serializable {

    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
