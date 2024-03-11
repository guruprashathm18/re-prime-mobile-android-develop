package com.royalenfield.reprime.models.request.web.signup;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is the request for sending signup data to Retrofit
 */

public class SignupRequest {

        @SerializedName("fName")
        @Expose
        private String fName;
        @SerializedName("lName")
        @Expose
        private String lName;
        @SerializedName("gender")
        @Expose
        private String gender="male";
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("isRoyalEnfieldOwner")
        @Expose
        private String isRoyalEnfieldOwner="false";
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("callingCode")
        @Expose
        private String callingCode;
        @SerializedName("mobile")
        @Expose
        private String mobile;

    public SignupRequest(String fName, String lName, String gender, String password, String dob, String isRoyalEnfieldOwner, String email, String callingCode, String mobile) {
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.otp = password;
        this.dob = dob;
        this.isRoyalEnfieldOwner = isRoyalEnfieldOwner;
        this.email = email;
        this.callingCode = callingCode;
        this.mobile = mobile;
    }
	public SignupRequest(){}

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

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getIsRoyalEnfieldOwner() {
            return isRoyalEnfieldOwner;
        }

        public void setIsRoyalEnfieldOwner(String isRoyalEnfieldOwner) {
            this.isRoyalEnfieldOwner = isRoyalEnfieldOwner;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(String callingCode) {
            this.callingCode = callingCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }


}
