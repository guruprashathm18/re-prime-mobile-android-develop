package com.royalenfield.reprime.models.request.web.changepassword;

public class ChangePasswordRequest {

    private String prevPassword,password,confirmPassword;

    public ChangePasswordRequest(String previousPassword,String newPassword,String confirmPassword){
        this.prevPassword=previousPassword;
        this.password=newPassword;
        this.confirmPassword=confirmPassword;
    }

    public String getPreviousPassword() {
        return prevPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.prevPassword = previousPassword;
    }

    public String getNewPassword() {
        return password;
    }

    public void setNewPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
