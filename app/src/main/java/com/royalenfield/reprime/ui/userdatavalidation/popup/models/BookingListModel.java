package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingListModel {


        @SerializedName("country")
        @Expose
        private List<BookingModel> country = null;

        public List<BookingModel> getCountry() {
            return country;
        }

        public void setCountry(List<BookingModel> country) {
            this.country = country;
        }


}
