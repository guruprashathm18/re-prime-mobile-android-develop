package com.royalenfield.reprime.models.response.web.verifytoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {


    @SerializedName("accountStatus")
    @Expose
    private AccountStatus accountStatus;
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("lName")
    @Expose
    private String lName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("contactDetails")
    @Expose
    private ContactDetails contactDetails;

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }


    public class AccountStatus {

        @SerializedName("verification")
        @Expose
        private String verification;
        @SerializedName("mediaRole")
        @Expose
        private String mediaRole;
        @SerializedName("isActive")
        @Expose
        private Boolean isActive;
        @SerializedName("passwordReset")
        @Expose
        private Boolean passwordReset;

        public String getVerification() {
            return verification;
        }

        public void setVerification(String verification) {
            this.verification = verification;
        }

        public String getMediaRole() {
            return mediaRole;
        }

        public void setMediaRole(String mediaRole) {
            this.mediaRole = mediaRole;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Boolean getPasswordReset() {
            return passwordReset;
        }

        public void setPasswordReset(Boolean passwordReset) {
            this.passwordReset = passwordReset;
        }
    }
        public class ContactDetails {

            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("emailVerified")
            @Expose
            private Boolean emailVerified;
            @SerializedName("mobile")
            @Expose
            private Mobile mobile;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Boolean getEmailVerified() {
                return emailVerified;
            }

            public void setEmailVerified(Boolean emailVerified) {
                this.emailVerified = emailVerified;
            }

            public Mobile getMobile() {
                return mobile;
            }

            public void setMobile(Mobile mobile) {
                this.mobile = mobile;
            }

        }
 public  class Mobile {

        @SerializedName("callingCode")
        @Expose
        private String callingCode;
        @SerializedName("isVerified")
        @Expose
        private Boolean isVerified;
        @SerializedName("number")
        @Expose
        private String number;

        public String getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(String callingCode) {
            this.callingCode = callingCode;
        }

        public Boolean getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(Boolean isVerified) {
            this.isVerified = isVerified;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }


}
