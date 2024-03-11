package com.royalenfield.reprime.ui.home.navigation.fragment;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.navigation.activity.SavedLocationsListActivity;
import com.royalenfield.reprime.ui.home.navigation.model.AddAddress;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

public class RenameAddressDialogFragment extends DialogFragment {

    private EditText mEditText;
    private TextView mTitle;
    String address_type;
    Context mContext;
    public static final String TAG = "RenameAddressDialogFragment";
    private String selectedFav;

    public static RenameAddressDialogFragment newInstance() {
        return new RenameAddressDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_rename_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        address_type = getArguments().getString("type");
        selectedFav = getArguments().getString("rename");
        mEditText = view.findViewById(R.id.et_rename_address);
        mTitle = view.findViewById(R.id.title);
        if (selectedFav != null) {
            mTitle.setText(selectedFav);
        }

        view.findViewById(R.id.no).setOnClickListener(v -> {
            dismiss();
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Cancel Rename");
            params.putString("eventLabel", address_type);
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
        });

        view.findViewById(R.id.yes).setOnClickListener(v -> {
            String value = mEditText.getText().toString().trim();
            if (!value.isEmpty()) {
                if (!isDuplicateFound(value)) {
                    updateAddressOfSavedItem(address_type, value);
                    ((SavedLocationsListActivity) getActivity()).setAddressFromPreference();
                    dismiss();
                } else {
                    Toast.makeText(mContext, " Duplicate found, please try again with new name.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "please enter name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAddressOfSavedItem(String address_type, String value) {
/*
        if (address_type.equalsIgnoreCase("Favourite 1")){
            RecentRouteLocationManager.updateAddressList(new AddAddress(value,RecentRouteLocationManager.getAddressList(mContext).get(2).getLocationCoordinates()),mContext,2);
        } else if (address_type.equalsIgnoreCase("Favourite 2")){
            RecentRouteLocationManager.updateAddressList(new AddAddress(value,RecentRouteLocationManager.getAddressList(mContext).get(3).getLocationCoordinates()),mContext,3);
        }else if (address_type.equalsIgnoreCase("Favourite 3")){
            RecentRouteLocationManager.updateAddressList(new AddAddress(value,RecentRouteLocationManager.getAddressList(mContext).get(4).getLocationCoordinates()),mContext,4);
        }else if (address_type.equalsIgnoreCase("Favourite 4")){
            RecentRouteLocationManager.updateAddressList(new AddAddress(value,RecentRouteLocationManager.getAddressList(mContext).get(5).getLocationCoordinates()),mContext,5);
        }else if (address_type.equalsIgnoreCase("Favourite 5")){
            RecentRouteLocationManager.updateAddressList(new AddAddress(value,RecentRouteLocationManager.getAddressList(mContext).get(6).getLocationCoordinates()),mContext,6);
        }
*/

        ArrayList<AddAddress> savedFavourites = RecentRouteLocationManager.getAddressList(mContext);
        boolean isDuplicate;
        if (selectedFav != null) {
            for (AddAddress address : savedFavourites) {
                isDuplicate = selectedFav.equalsIgnoreCase(address.getLocationName());
                if (isDuplicate) {
                    address.setLocationName(value);
                    RecentRouteLocationManager.updateAddressList(address, mContext, address.getPosition());
                }
            }
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Ok Rename");
            params.putString("eventLabel", address_type);
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
        }
    }


    private boolean isDuplicateFound(String selectedFav) {
        ArrayList<AddAddress> savedFavourites = RecentRouteLocationManager.getAddressList(mContext);
        boolean isDuplicate = false;
        for (AddAddress address : savedFavourites) {
            isDuplicate = selectedFav.equalsIgnoreCase(address.getLocationName());
            if (isDuplicate) {
                break;
            }
        }
        return isDuplicate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
