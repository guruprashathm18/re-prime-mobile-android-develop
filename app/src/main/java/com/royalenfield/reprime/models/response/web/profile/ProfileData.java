package com.royalenfield.reprime.models.response.web.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AccountStatus;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.ContactData;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProfileData {

    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("addressInfo")
    @Expose
    private ArrayList<Data> addressInfo;
	@SerializedName("accountStatus")
	@Expose
	private AccountStatus accountStatus;
    @SerializedName("lName")
    @Expose
    private String lName;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("contactDetails")
    @Expose
    private ContactData contactDetails;

    @SerializedName("profileUrl")
    @Expose
    private String profileUrl;


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    private String secondary;

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String firstName) {
        this.fName = firstName;
    }

    public ArrayList<Data> getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(ArrayList<Data> addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lastName) {
        this.lName = lastName;
    }


    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }



    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
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

    public ContactData getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactData contactDetails) {
        this.contactDetails = contactDetails;
    }

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
}


