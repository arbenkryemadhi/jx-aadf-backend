package com.arben.jxaadf.appuser;

public class AppUser {
    private String appUserId;
    private String firstName;
    private String lastName;
    private String email;

    public AppUser(String appUserId, String firstName, String lastName, String email) {
        this.appUserId = appUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
