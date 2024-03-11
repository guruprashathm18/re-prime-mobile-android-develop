package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.fragment.AddFavouriteAddressDialogSheet;
import com.royalenfield.reprime.ui.home.navigation.fragment.RenameAddressDialogFragment;
import com.royalenfield.reprime.ui.home.navigation.model.AddAddress;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

public class SavedLocationsListActivity extends REBaseActivity implements View.OnClickListener, AddFavouriteAddressDialogSheet.ItemClickListener, TitleBarView.OnNavigationIconClickListener {

    ImageView imgHome, imgOffice, imgFav1, imgFav2, imgFav3, imgFav4, imgFav5;
    TextView tvFav1, tvFav2, tvFav3, tvFav4, tvFav5;
    TextView tvHomeId, tvOfficeId, tvFav1Id, tvFav2Id, tvFav3Id, tvFav4Id, tvFav5Id;
    public static final int REQUESTCODE_MORE_FAVOURITE_ADDRESS = 153;
    TitleBarView mTitleBarView;
    String addAddress;
    String selectedFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_location_list_activity);
        addAddress = getResources().getString(R.string.choose_location);
        initView();
        setDefaultLocations();
        setAddressFromPreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAddressFromPreference();
    }

    private void initView() {
        mTitleBarView = findViewById(R.id.plan_ride_header);
        imgHome = findViewById(R.id.home_add_img);
        imgHome.setOnClickListener(this);
        imgOffice = findViewById(R.id.office_add_img);
        imgOffice.setOnClickListener(this);
        imgFav1 = findViewById(R.id.fav1_add_img);
        imgFav1.setOnClickListener(this);
        imgFav2 = findViewById(R.id.fav2_add_img);
        imgFav2.setOnClickListener(this);
        imgFav3 = findViewById(R.id.fav3_add_img);
        imgFav3.setOnClickListener(this);
        imgFav4 = findViewById(R.id.fav4_add_img);
        imgFav4.setOnClickListener(this);
        imgFav5 = findViewById(R.id.fav5_add_img);
        imgFav5.setOnClickListener(this);
        tvFav1 = findViewById(R.id.txt_sv_fav1);
        tvFav2 = findViewById(R.id.txt_sv_fav2);
        tvFav3 = findViewById(R.id.txt_sv_fav3);
        tvFav4 = findViewById(R.id.txt_sv_fav4);
        tvFav5 = findViewById(R.id.txt_sv_fav5);
        tvHomeId = findViewById(R.id.txt_sv_home_id);
        tvOfficeId = findViewById(R.id.txt_sv_office_id);
        tvFav1Id = findViewById(R.id.txt_sv_fav1_id);
        tvFav2Id = findViewById(R.id.txt_sv_fav2_id);
        tvFav3Id = findViewById(R.id.txt_sv_fav3_id);
        tvFav4Id = findViewById(R.id.txt_sv_fav4_id);
        tvFav5Id = findViewById(R.id.txt_sv_fav5_id);
        ConstraintLayout home = findViewById(R.id.fav_item_home);

        mTitleBarView.bindData(this, R.drawable.back_arrow, "Saved Locations");
        home.setOnClickListener(this);
        ConstraintLayout office = findViewById(R.id.fav_item_office);
        office.setOnClickListener(this);
        ConstraintLayout fav1 = findViewById(R.id.fav_item_fav1);
        fav1.setOnClickListener(this);
        ConstraintLayout fav2 = findViewById(R.id.fav_item_fav2);
        fav2.setOnClickListener(this);
        ConstraintLayout fav3 = findViewById(R.id.fav_item_fav3);
        fav3.setOnClickListener(this);
        ConstraintLayout fav4 = findViewById(R.id.fav_item_fav4);
        fav4.setOnClickListener(this);
        ConstraintLayout fav5 = findViewById(R.id.fav_item_fav5);
        fav5.setOnClickListener(this);
    }


    private void setDefaultLocations() {
        if (RecentRouteLocationManager.getAddressList(this).size() == 0) {
            LatLng latLngTemp = new LatLng(0, 0);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Home", latLngTemp, 0), this, 0);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Work", latLngTemp, 1), this, 1);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 1", latLngTemp, 2), this, 2);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 2", latLngTemp, 3), this, 3);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 3", latLngTemp, 4), this, 4);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 4", latLngTemp, 5), this, 5);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 5", latLngTemp, 6), this, 6);
        }
    }


    public void setAddressFromPreference() {
        try {
            if (RecentRouteLocationManager.getAddressList(this).size() >= 1) {
                if (RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvHomeId.setText(addAddress);
                    imgHome.setVisibility(View.GONE);
                } else {
                    tvHomeId.setText(RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().toString().substring(8));
                    imgHome.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 2) {
                if (RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvOfficeId.setText(addAddress);
                    imgOffice.setVisibility(View.GONE);
                } else {
                    tvOfficeId.setText(RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().toString().substring(8));
                    imgOffice.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 3) {
                tvFav1.setText(RecentRouteLocationManager.getAddressList(this).get(2).getLocationName());
                if (RecentRouteLocationManager.getAddressList(this).get(2).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvFav1Id.setText(addAddress);
                    imgFav1.setVisibility(View.GONE);
                } else {
                    tvFav1Id.setText(RecentRouteLocationManager.getAddressList(this).get(2).getLocationCoordinates().toString().substring(8));
                    imgFav1.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 4) {
                tvFav2.setText(RecentRouteLocationManager.getAddressList(this).get(3).getLocationName());
                if (RecentRouteLocationManager.getAddressList(this).get(3).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvFav2Id.setText(addAddress);
                    imgFav2.setVisibility(View.GONE);
                } else {
                    tvFav2Id.setText(RecentRouteLocationManager.getAddressList(this).get(3).getLocationCoordinates().toString().substring(8));
                    imgFav2.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 5) {
                tvFav3.setText(RecentRouteLocationManager.getAddressList(this).get(4).getLocationName());

                if (RecentRouteLocationManager.getAddressList(this).get(4).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvFav3Id.setText(addAddress);
                    imgFav3.setVisibility(View.GONE);
                } else {
                    tvFav3Id.setText(RecentRouteLocationManager.getAddressList(this).get(4).getLocationCoordinates().toString().substring(8));
                    imgFav3.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 6) {
                tvFav4.setText(RecentRouteLocationManager.getAddressList(this).get(5).getLocationName());

                if (RecentRouteLocationManager.getAddressList(this).get(5).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvFav4Id.setText(addAddress);
                    imgFav4.setVisibility(View.GONE);
                } else {
                    tvFav4Id.setText(RecentRouteLocationManager.getAddressList(this).get(5).getLocationCoordinates().toString().substring(8));
                    imgFav4.setVisibility(View.VISIBLE);
                }
            }
            if (RecentRouteLocationManager.getAddressList(this).size() >= 7) {
                tvFav5.setText(RecentRouteLocationManager.getAddressList(this).get(6).getLocationName());

                if (RecentRouteLocationManager.getAddressList(this).get(6).getLocationCoordinates().equals(new LatLng(0, 0))) {
                    tvFav5Id.setText(addAddress);
                    imgFav5.setVisibility(View.GONE);
                } else {
                    tvFav5Id.setText(RecentRouteLocationManager.getAddressList(this).get(6).getLocationCoordinates().toString().substring(8));
                    imgFav5.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_add_img:
                menuClicked();
                if (tvHomeId.getText().toString().equalsIgnoreCase((addAddress))) {
                    startActivity("Home", false);
                } else {
                    selectedFav="Home";
                    showBottomMenu("Home");
                }
                break;
            case R.id.office_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 1) {
                    if (tvOfficeId.getText().toString().equalsIgnoreCase((addAddress))) {
                        startActivity("Work", false);
                    } else {
                        selectedFav="Work";
                        showBottomMenu("Work");
                    }
                }
                break;
            case R.id.fav1_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 2) {
                    if (tvFav1Id.getText().toString().equalsIgnoreCase((addAddress))) {
                        startActivity("Location 1", false);
                    } else {
                        selectedFav=tvFav1.getText().toString();
                        showBottomMenu(selectedFav);
                    }
                }
                break;
            case R.id.fav2_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 3) {
                    if (tvFav2Id.getText().toString().equalsIgnoreCase((addAddress))) {
                        startActivity("Location 2", false);
                    } else {
                        selectedFav=tvFav2.getText().toString();
                        showBottomMenu(selectedFav);
                    }
                }
                break;
            case R.id.fav3_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 4) {
                    if (tvFav3Id.getText().toString().equalsIgnoreCase((addAddress))) {
                        startActivity("Location 3", false);
                    } else {
                        selectedFav=tvFav3.getText().toString();
                        showBottomMenu(selectedFav);
                    }
                }
                break;
            case R.id.fav4_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 5) {
                    if (tvFav4Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 4", false);
                    } else {
                        selectedFav=tvFav4.getText().toString();
                        showBottomMenu(selectedFav);
                    }
                }
                break;
            case R.id.fav5_add_img:
                menuClicked();
                if (RecentRouteLocationManager.getAddressList(this).size() >= 6) {
                    if (tvFav5Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 5", false);
                    } else {
                        selectedFav=tvFav5.getText().toString();
                        showBottomMenu(selectedFav);
                    }
                }
                break;
            case R.id.fav_item_home:
                if (tvHomeId.getText().toString().equalsIgnoreCase(addAddress)) {
                    startActivity("Home", false);
                } else {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", "Location Type Selected");
                    params.putString("eventLabel", "Home");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                    AddAddress address = RecentRouteLocationManager.getAddressList(this).get(0);
                    setResultData(address);
                }
                break;
            case R.id.fav_item_office:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 1) {
                    if (tvOfficeId.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Work", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Work");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(1);
                        setResultData(address);
                    }
                }
                break;
            case R.id.fav_item_fav1:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 2) {
                    if (tvFav1Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 1", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Location 1");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(2);
                        setResultData(address);
                    }
                }
                break;
            case R.id.fav_item_fav2:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 3) {
                    if (tvFav2Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 2", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Location 2");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(3);
                        setResultData(address);
                    }
                }
                break;
            case R.id.fav_item_fav3:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 4) {
                    if (tvFav3Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 3", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Location 3");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(4);
                        setResultData(address);
                    }
                }
                break;
            case R.id.fav_item_fav4:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 5) {
                    if (tvFav4Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 4", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Location 4");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(5);
                        setResultData(address);
                    }
                }
                break;
            case R.id.fav_item_fav5:
                if (RecentRouteLocationManager.getAddressList(this).size() >= 6) {
                    if (tvFav5Id.getText().toString().equalsIgnoreCase(addAddress)) {
                        startActivity("Location 5", false);
                    } else {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Location Type Selected");
                        params.putString("eventLabel", "Location 5");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        AddAddress address = RecentRouteLocationManager.getAddressList(this).get(6);
                        setResultData(address);
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void menuClicked() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Kebab Menu Clicked");
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
    }

    private void showBottomMenu(String value) {
        AddFavouriteAddressDialogSheet aSheet = AddFavouriteAddressDialogSheet.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("type", value);
        aSheet.setArguments(bundle);
        aSheet.show(getSupportFragmentManager(), AddFavouriteAddressDialogSheet.TAG);
    }

    private void startActivity(String type, boolean isEdit) {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Edit Location");
        params.putString("eventLabel", type);
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

        Intent i = new Intent(SavedLocationsListActivity.this, NavigationAddAddressMapActivity.class);
        i.putExtra("location_type", type);
        if(isEdit) {
            i.putExtra("edit", type);
        }
        startActivity(i);
    }

    @Override
    public void onItemClick(String item, String address_type) {
        RELog.e("SavedLocationListActivity", "" + item);
        if (item.equalsIgnoreCase("Edit")) {
            startActivity(address_type, true);
        } else if (item.equalsIgnoreCase("Delete")) {
            deleteAddressFromSavedList(address_type);
            setAddressFromPreference();
        } else if (item.equalsIgnoreCase("Rename")) {
            showRenameDialog(address_type);
        }
    }

    private void showRenameDialog(String address_type) {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Rename Location");
        params.putString("eventLabel", address_type);
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

        RenameAddressDialogFragment renameAddressDialogFragment = RenameAddressDialogFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("type", address_type);
        bundle.putString("rename", selectedFav);
        renameAddressDialogFragment.setArguments(bundle);
        renameAddressDialogFragment.show(getSupportFragmentManager(), RenameAddressDialogFragment.TAG);
    }

    private void deleteAddressFromSavedList(String selectedFav) {

        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Remove Location");
        params.putString("eventLabel", selectedFav);
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

        ArrayList<AddAddress> savedFavourites = RecentRouteLocationManager.getAddressList(this);
        boolean isDuplicate;
        if(selectedFav != null) {
            for (AddAddress address : savedFavourites) {
                isDuplicate = selectedFav.equalsIgnoreCase(address.getLocationName());
                if (isDuplicate) {
                    switch (address.getPosition()) {
                        case 0:
                        case 1:
                            address.setLocationCoordinates(new LatLng(0, 0));
                            RecentRouteLocationManager.updateAddressList(address, this, address.getPosition());
                            break;
                        default:
                            address.setLocationName("Location " + (address.getPosition() - 1));
                            address.setLocationCoordinates(new LatLng(0, 0));
                            RecentRouteLocationManager.updateAddressList(address, this, address.getPosition());
                            break;
                    }
                }
            }
        }
    }

    public void setResultData(AddAddress address) {

        RELog.e("setResultData: ", "clicked on coordinate layout");
        Intent intent = new Intent();
        intent.putExtra("place", address.getLocationName());
        intent.putExtra("latitude", address.getLocationCoordinates().latitude);
        intent.putExtra("longitude", address.getLocationCoordinates().longitude);
        intent.putExtra("isRecentLocation", true);
        setResult(REQUESTCODE_MORE_FAVOURITE_ADDRESS, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        setResult(REQUESTCODE_MORE_FAVOURITE_ADDRESS, getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(REQUESTCODE_MORE_FAVOURITE_ADDRESS, getIntent());
        super.onBackPressed();
    }
}
