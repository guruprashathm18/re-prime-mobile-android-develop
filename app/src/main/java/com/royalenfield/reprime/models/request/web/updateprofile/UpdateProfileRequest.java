package com.royalenfield.reprime.models.request.web.updateprofile;

public class UpdateProfileRequest {

    private String fname;

    private String lname;

    private String email;

    private String phoneNo;

    private String dob;
    private String city;

    private String password;

    private String userId;

    private String aboutMe;

    public UpdateProfileRequest(String fname, String lname, String email, String phoneNo,
                                String dob, String city, String password, String userId, String aboutMe) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.city = city;
        this.password = password;
        this.userId = userId;
        this.aboutMe = aboutMe;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
