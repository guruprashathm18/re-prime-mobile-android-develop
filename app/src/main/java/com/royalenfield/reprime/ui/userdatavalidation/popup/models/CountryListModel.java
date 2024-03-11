package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryListModel {


        @SerializedName("country")
        @Expose
        private List<CountryModel> country = null;

        public List<CountryModel> getCountry() {
            return country;
        }

        public void setCountry(List<CountryModel> country) {
            this.country = country;
        }


}
