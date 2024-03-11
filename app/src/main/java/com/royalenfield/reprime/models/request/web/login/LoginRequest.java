package com.royalenfield.reprime.models.request.web.login;

/**
 * @author BOP1KOR on 12/7/2018.
 */

public class LoginRequest {

    private String email;

    private String credentials;

    private String password;

    private String userType;

    public LoginRequest(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public LoginRequest(String credentials, String userType) {
        this.credentials = credentials;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "LoginRequest [creadentials = " + credentials + ", userType = " + userType + "]";
//        return "LoginRequest [email = " + email + ", password = " + password + ", userType = " + userType + "]";
    }
}
