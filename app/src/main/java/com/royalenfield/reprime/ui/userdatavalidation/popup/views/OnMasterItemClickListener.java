package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

public interface OnMasterItemClickListener {
    void onMasterItemClicked(String value, String dataType);

    void onMasterItemClicked(int adapterPosition);

    void onStateClicked(int adapterPosition);

    void onCityItemClicked(int adapterPosition);

    void onCountryCodeClicked(int adapterPosition);

    void onS1CountryCodeClicked(int adapterPosition);
}
