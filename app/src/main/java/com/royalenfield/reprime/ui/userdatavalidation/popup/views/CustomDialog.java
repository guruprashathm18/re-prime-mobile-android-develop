package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.onboarding.address.activity.AddLocationActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.TransparentPopActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialog extends DialogFragment implements OnMasterItemClickListener {

    private static final String KEY_MASTER_DATA_TYPE = "MasterDataType";
    private static final String KEY_MASTER_DATA = "MasterData";
    private static final String KEY_SELECTED = "Selected";

    private static final String KEY_COUNTRY_DATA = "CountryData";
    private static final String KEY_STATE_DATA = "StateData";
    private static final String KEY_CITY_DATA = "CityData";


    @BindView(R.id.master_data_view)
    RecyclerView masterDataView;
    List<MasterDataModel> mDataList = new ArrayList<>();
    private ArrayList<CountryModel> mCountryDataModel;
    private MasterDataAdapter mMasterDataAdapter;
    private ArrayList<StateModel> mStateDataModel;
    private StateDataAdapter mStatedataAdapter;
    private ArrayList<CityModel> mCityDataModel;
    private CityDataAdapter mCitydataAdapter;

    public static CustomDialog getInstance(String dataType, ArrayList<String> masterData, String selected) {

        CustomDialog customDialog = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MASTER_DATA_TYPE, dataType);
        bundle.putStringArrayList(KEY_MASTER_DATA, masterData);
        bundle.putString(KEY_SELECTED, selected);
        customDialog.setArguments(bundle);
        return customDialog;
    }

    public static CustomDialog getInstance(String dataType, ArrayList<CountryModel> countryData) {
        CustomDialog customDialog = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MASTER_DATA_TYPE, dataType);
        bundle.putParcelableArrayList(KEY_COUNTRY_DATA, countryData);
        customDialog.setArguments(bundle);
        return customDialog;
    }

    public static CustomDialog getInstance(String dataType, ArrayList<StateModel> stateData, int val) {
        CustomDialog customDialog = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MASTER_DATA_TYPE, dataType);
        bundle.putParcelableArrayList(KEY_STATE_DATA, stateData);
        customDialog.setArguments(bundle);
        return customDialog;
    }

    public static CustomDialog getInstance(String dataType, ArrayList<CityModel> cityData, int i, int i1) {
        CustomDialog customDialog = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MASTER_DATA_TYPE, dataType);
        bundle.putParcelableArrayList(KEY_CITY_DATA, cityData);
        customDialog.setArguments(bundle);
        return customDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            if (getArguments().getString(KEY_MASTER_DATA_TYPE) != null) {
                if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.COUNTRY.mDataType)
                        || getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.COUNTRY_CODE.mDataType)
                        || getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.S1_COUNTRY_CODE.mDataType)) {

                    mCountryDataModel = getArguments().getParcelableArrayList(KEY_COUNTRY_DATA);
                } else if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.STATE.mDataType)) {
                    mStateDataModel = getArguments().getParcelableArrayList(KEY_STATE_DATA);
                } else if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.CITY.mDataType)) {
                    mCityDataModel = getArguments().getParcelableArrayList(KEY_CITY_DATA);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            if (getActivity() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                dialog.getWindow().setLayout(width - 150, (height - 250));
            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_custom, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {

            if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.STATE.mDataType)) {
                mStatedataAdapter = new StateDataAdapter(mStateDataModel, this);
                masterDataView.setAdapter(mStatedataAdapter);
            } else if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.COUNTRY.mDataType)
                    || getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.COUNTRY_CODE.mDataType)
                    || getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.S1_COUNTRY_CODE.mDataType)
            ) {
                String masterDataType = getArguments().getString(KEY_MASTER_DATA_TYPE);
                mMasterDataAdapter = new MasterDataAdapter(mCountryDataModel, this, masterDataType);
                masterDataView.setAdapter(mMasterDataAdapter);
            } else if (getArguments().getString(KEY_MASTER_DATA_TYPE).equals(MasterDataType.CITY.mDataType)) {
                mCitydataAdapter = new CityDataAdapter(mCityDataModel, this);
                masterDataView.setAdapter(mCitydataAdapter);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL, false);
        masterDataView.setLayoutManager(linearLayoutManager);
        masterDataView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onMasterItemClicked(String value, String dataType) {

    }

    @Override
    public void onMasterItemClicked(int adapterPosition) {
        ((TransparentPopActivity) getActivity()).onCountrySelected(adapterPosition);
        dismiss();
    }

    @Override
    public void onStateClicked(int adapterPosition) {
        if( getActivity() instanceof AddLocationActivity)
            ((AddLocationActivity) getActivity()).onStateSelected(adapterPosition);
        else
        ((TransparentPopActivity) getActivity()).onStateSelected(adapterPosition);
        dismiss();
    }

    @Override
    public void onCityItemClicked(int adapterPosition) {
        if( getActivity() instanceof AddLocationActivity)
            ((AddLocationActivity) getActivity()).onCitySelected(adapterPosition);
        else
        ((TransparentPopActivity) getActivity()).onCitySelected(adapterPosition);
        dismiss();
    }

    @Override
    public void onCountryCodeClicked(int adapterPosition) {
        ((TransparentPopActivity) getActivity()).onCountryCodeSelected(adapterPosition);
        dismiss();
    }

    @Override
    public void onS1CountryCodeClicked(int adapterPosition) {

            ((TransparentPopActivity) getActivity()).onS1CountryCodeSelected(adapterPosition);
        dismiss();
    }

    public enum MasterDataType {

        COUNTRY("Country"),
        STATE("State"),
        CITY("City"),
        COUNTRY_CODE("CountryCode"),
        S1_COUNTRY_CODE("Secondary1CountryCode");

        public final String mDataType;

        MasterDataType(String dataType) {
            mDataType = dataType;
        }
    }


}
