package com.municipal.complaintsapp.API;

public enum ApiList {
    Register("api/User/SignUp"),
    Login("api/User/Login");

    Environment environment = Environment.live;

    private final String api;
    ApiList(String api) {
        this.api = environment.getUrl() +api;
    }

    public String getApi() {
        return api;
    }
}
