package com.municipal.complaintsapp.API;

public enum ApiList {
    Register("api/User/SignUp"),
    Login("api/User/Login"),

    ChangePassword("api/User/ChangePassword"),

    AddComplaint("api/Complaint/AddCompaint"),

    FetchAllComplaints("api/Complaint/GetComplaintsByUser/"),

    FetchComplaintById("/api/Complaint/GetComplaintById/"),

    FetchCategoryTypes("/api/Category/GetAllCategory");
    Environment environment = Environment.live;

    private final String api;
    ApiList(String api) {
        this.api = environment.getUrl() +api;
    }

    public String getApi() {
        return api;
    }
}
