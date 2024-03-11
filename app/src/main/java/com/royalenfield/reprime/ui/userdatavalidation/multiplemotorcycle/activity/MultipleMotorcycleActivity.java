package com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.activity.DetailsConfirmationActivity;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.presenter.MultipleMotorcyclePresenter;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view.MotorcycleListAdapter;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view.MotorcyclesItem;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor.YourMotorcycleInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter.YourMotorcyclePresenter;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleMotorcycleActivity extends AppCompatActivity implements
        MotorcycleListAdapter.onItemClickListener, MultipleMotorcycleView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private MotorcycleListAdapter adapter;
    private ArrayList<MotorcyclesItem> updatedList;
    private MultipleMotorcyclePresenter mMultipleMotorcyclePresenter;
    private ArrayList<MotorcyclesItem> mOriginalList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_motorcycle);

        ButterKnife.bind(this);
        mMultipleMotorcyclePresenter = new MultipleMotorcyclePresenter(this, new YourMotorcycleInteractor());


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            TextView textView =getSupportActionBar().getCustomView().findViewById(R.id.tvTitle);
            textView.setText(getString(R.string.your_motorcycles));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        adapter = new MotorcycleListAdapter(getMotorcyclesListData(), this);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            REApplication.getInstance().setComingFromVehicleOnboarding(false);
            startActivity(new Intent(this, REHomeActivity.class));
            finish();
        }
        return true;
    }

    //TODO get data from API
    private List<MotorcyclesItem> getMotorcyclesListData() {

        List<VehicleDataModel> vehicleDataModelList = REServiceSharedPreference.getVehicleData(this);
        mOriginalList = new ArrayList<>();
        updatedList = new ArrayList<>();

        for (VehicleDataModel dataModel : vehicleDataModelList) {

            MotorcyclesItem motorcyclesItem = new MotorcyclesItem();

            motorcyclesItem.setEngineNum(dataModel.getEngineNo());
            motorcyclesItem.setRegistrationNumber(dataModel.getRegistrationNo());
            motorcyclesItem.setIsNotOwn(false);
            motorcyclesItem.setError(false);
            motorcyclesItem.setErrorMessage("");
            motorcyclesItem.setModelCode(dataModel.getModelCode());
            motorcyclesItem.setModelName(dataModel.getModelName());
            motorcyclesItem.setChasisNumber(dataModel.getChaissisNo());
            motorcyclesItem.setImageUrl(dataModel.getImageUrl());
            updatedList.add(motorcyclesItem);
        }

        for (MotorcyclesItem item : updatedList) {
            mOriginalList.add((MotorcyclesItem) item.clone());
        }

        return updatedList;
    }


    @Override
    public void onItemClick() {

        if (isMotorcycleDataChanged()) {
            if (isItemsValid()) {
                mMultipleMotorcyclePresenter.onConfirmButtonClicked(mOriginalList, updatedList);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            if (isItemsValid()) {
                showConfirmationScreenWithMsgId(R.string.thankyou_msg);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

    }

    private boolean isMotorcycleDataChanged() {
        return !mOriginalList.equals(updatedList);
    }

    public void showConfirmationScreenWithMsgId(int msgId) {
        Intent intent = new Intent(MultipleMotorcycleActivity.this
                , DetailsConfirmationActivity.class);
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TITLE, getString(msgId));
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE, "You already have" + getVehicleCount() +"Motorcycles linked to your mobile number");
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_Button, getString(R.string.view_my_profile));
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TYPE, getString(R.string.motorcycle_screen_type));
        startActivity(intent);
        finish();
    }

    private boolean isItemsValid() {
        boolean isValid = true;
        for (int i = 0 ; i< updatedList.size() ; i++) {
            MotorcyclesItem item = updatedList.get(i);
            setRegistrationNumValidity(item);
        }

        for (int i = 0 ;i<updatedList.size() ;i++) {
            MotorcyclesItem item = updatedList.get(i);
            if (item.isError()) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    private void setRegistrationNumValidity(MotorcyclesItem item) {

        String registrationNum = item.getmRegistrationNumber();

        if (TextUtils.isEmpty(registrationNum)) {
            item.setError(true);
            item.setErrorMessage(getString(R.string.enter_registration_num));
        } else if (registrationNum.length() < 8
                || registrationNum.length() > 10) {
            item.setError(true);
            item.setErrorMessage(getString(R.string.registrationNumRangeError));
        } else if (!registrationNum.matches("[a-zA-Z0-9]+")) {
            item.setError(true);
            item.setErrorMessage(getString(R.string.show_num_error));
        } else if (!registrationNum.matches("^.*[0-9].*$")) {
            item.setError(true);
            item.setErrorMessage(getString(R.string.show_num_error));
        } else if (!registrationNum.matches("^.*[a-zA-Z].*$")) {
            item.setError(true);
            item.setErrorMessage(getString(R.string.show_num_error));
        } else {
            item.setError(false);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(false);
        startActivity(new Intent(MultipleMotorcycleActivity.this, REHomeActivity.class));
        finish();
    }

    private int getVehicleCount() {
        if (REServiceSharedPreference.getVehicleData(this)!=null) {
            return REServiceSharedPreference.getVehicleData(this).size();
        } else {
            return 1;
        }
    }
}
