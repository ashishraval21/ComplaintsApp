package com.municipal.complaintsapp.API;

public enum Environment {

    local("http://localhost:44301/"),
    live("http://sonali-api.cleverapps.in/");

    private final String url;
    Environment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
