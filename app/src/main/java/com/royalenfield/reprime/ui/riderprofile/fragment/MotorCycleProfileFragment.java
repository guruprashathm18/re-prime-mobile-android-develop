package com.royalenfield.reprime.ui.riderprofile.fragment;

import static com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryActivity.getSortedServiceHistoryList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.activity.AddVehicleActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.MotorcycleNotesActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MotorCycleProfileFragment.OnMotorcycleFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MotorCycleProfileFragment extends REBaseFragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = MotorCycleProfileFragment.class.getSimpleName();
    private OnMotorcycleFragmentInteractionListener mListener;
    private Context mActivityContext;
    //Views
    private Spinner mSpinner;
    private TextView mPurchaseDate;
    private TextView mColor;
    private TextView mManufactureYear;
    private TextView mEngineNumber;
    private TextView mLastServiced;
    private TextView mViewServiceHistory;
    private TextView mAddNote;
    //    private TextView mVisitNearestServiceCenter;
    private TextView mTvVehicleRegistrationNumber;
    private ConstraintLayout mLayout;
    private ImageView ivServiceHistory;
    private View viewLineServiceHistory;
    private TextView tvMotorCyleLabel;
    private ImageView ivNoteIcon;
    private TextView tvNoteDate;
    private ImageView ivAddNoteIcon;
    private View view2;
    private View view3;
    //    private TextView tvRegisterOtherMotorCyle;
    private TextView textView_note;
    private ConstraintLayout mConstraintNoVehicle;
    private TextView mYourMotorcycle;
    private String mRegistrationNumber;
    List<VehicleDataModel> mVehicleDetailsList = new ArrayList<>();
    private int mVehicleSpinnerPosition;
    private REUserModelStore mUserModelStore;
    Bundle  params = new Bundle();
    public MotorCycleProfileFragment() {
        // Required empty public constructor
    }

    public static MotorCycleProfileFragment newInstance() {
        MotorCycleProfileFragment fragment = new MotorCycleProfileFragment();
        return fragment;
    }

    /**
     * Broadcast Receiver for listening the Firestore Vehicle & ServiceHistory updates
     */
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (REConstants.FIRESTORE_UPDATE.equals(intent.getAction())) {
                Log.d(TAG, "onVehicleUpdateSuccess");
//                mVehicleDetailsList = mUserModelStore.getVehicleDetailsList();
                bindData();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motor_cycle_profile, container, false);
        mUserModelStore = REUserModelStore.getInstance();
        initViews(view);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REConstants.FIRESTORE_UPDATE));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Loading the UI in Handler because it lags if we load in MainThread


    }

    public void bindAdapterData(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mVehicleDetailsList = REServiceSharedPreference.getVehicleData(getContext());
                //Binds data to views.....
                bindData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    /**
     * Initialize all the views.
     *
     * @param view views.
     */

    private TextView mAddNewVehicle;

    private void initViews(View view) {
        mSpinner = view.findViewById(R.id.spinner_motorcyle_list);
        mPurchaseDate = view.findViewById(R.id.tv_purchase_date);
        mColor = view.findViewById(R.id.tv_color);
        mManufactureYear = view.findViewById(R.id.tv_manufacture_year);
        mEngineNumber = view.findViewById(R.id.tv_engine_number);
        mLastServiced = view.findViewById(R.id.tv_last_serviced_value);
        mViewServiceHistory = view.findViewById(R.id.tv_service_history);
        mAddNote = view.findViewById(R.id.tv_add_note);
        mTvVehicleRegistrationNumber = view.findViewById(R.id.tv_vehicle_registration_no);

        mLayout = view.findViewById(R.id.constraintLayout_motorcyle_details);

        mViewServiceHistory = view.findViewById(R.id.tv_service_history);
        mViewServiceHistory.setOnClickListener(this);

        ivServiceHistory = view.findViewById(R.id.iv_service_history);
        ivServiceHistory.setOnClickListener(this);
        viewLineServiceHistory = view.findViewById(R.id.view_line_service_history);
        tvMotorCyleLabel = view.findViewById(R.id.tv_motorcycle_notes_label);
        ivNoteIcon = view.findViewById(R.id.iv_note_icon);
        ivNoteIcon.setOnClickListener(this);
        tvNoteDate = view.findViewById(R.id.tv_note_date);
        ivAddNoteIcon = view.findViewById(R.id.iv_add_note_arrow_icon);
        ivAddNoteIcon.setOnClickListener(this);
        view2 = view.findViewById(R.id.view_line_seprator_2);
        view3 = view.findViewById(R.id.view3);
        textView_note = view.findViewById(R.id.textView_note);
        textView_note.setOnClickListener(this);
        mConstraintNoVehicle = view.findViewById(R.id.constraintLayout_novehicle);
        mYourMotorcycle = view.findViewById(R.id.tv_label_motorcyle_name);

        mAddNewVehicle = view.findViewById(R.id.add_new_vehicle);
        mAddNewVehicle.setOnClickListener(this);
    }

    /**
     * Binds the motor-cycle profile data to views.....
     */
    private void bindData() {
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            setVehicleDataAvailabeView();
            List<String> bikeList = new ArrayList<>();
            for (int i = 0; i < mVehicleDetailsList.size(); i++) {
                String modelCode = mVehicleDetailsList.get(i).getModelCode();
                String modelName = mVehicleDetailsList.get(i).getModelName();
           //     String truncatedModelName = REUtils.bikeNameInSpinner(modelName);
                bikeList.add(modelName);
            }
            //Set Spinner adapter for the bike list..
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(mActivityContext, R.layout.layout_spinner_item, bikeList);
            mSpinner.setAdapter(adapter);
            mSpinner.setOnItemSelectedListener(this);
            populateSelectedBikeDetails(0);
        } else {
            setNoVehicleDetailsAvailableView();
        }
    }

    /**
     * Update UI if there is vehicleData
     */
    private void setVehicleDataAvailabeView() {
        mConstraintNoVehicle.setVisibility(View.GONE);
        mYourMotorcycle.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.VISIBLE);
        mLayout.setVisibility(View.VISIBLE);
//        mVisitNearestServiceCenter.setVisibility(View.VISIBLE);
        //TODO need to remove comments if notes is required
//        tvMotorCyleLabel.setVisibility(View.VISIBLE);
//        ivNoteIcon.setVisibility(View.VISIBLE);
//        textView_note.setVisibility(View.VISIBLE);
//        ivAddNoteIcon.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
//        tvRegisterOtherMotorCyle.setVisibility(View.VISIBLE);
    }

    /**
     * Update UI if there is no vehicleData
     */
    private void setNoVehicleDetailsAvailableView() {
        //TODO no bike views need to display.......
        mConstraintNoVehicle.setVisibility(View.VISIBLE);
//        mVisitNearestServiceCenter.setVisibility(View.VISIBLE);
//        tvRegisterOtherMotorCyle.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        mYourMotorcycle.setVisibility(View.GONE);
        mSpinner.setVisibility(View.GONE);
        mLayout.setVisibility(View.GONE);
        ivServiceHistory.setVisibility(View.GONE);
        mViewServiceHistory.setVisibility(View.GONE);
        mAddNote.setVisibility(View.GONE);
        ivServiceHistory.setVisibility(View.GONE);
        viewLineServiceHistory.setVisibility(View.GONE);
        //TODO need to remove comments if notes is required
//        tvMotorCyleLabel.setVisibility(View.GONE);
        //ivNoteIcon.setVisibility(View.GONE);
//        textView_note.setVisibility(View.GONE);
        tvNoteDate.setVisibility(View.GONE);
        ivAddNoteIcon.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
    }

    private void populateSelectedBikeDetails(int position) {
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            VehicleDataModel vehicleDetailsResponse = mVehicleDetailsList.get(position);
            //TODO set puchase date
            mPurchaseDate.setText((vehicleDetailsResponse.getPurchaseDate() != null && !vehicleDetailsResponse.getPurchaseDate().isEmpty()) ?
                    REUtils.getOrdinalDateObject(getContext(), vehicleDetailsResponse.getPurchaseDate(), false) : getActivity().getResources().getString(R.string.text_notavailable));
            mRegistrationNumber = vehicleDetailsResponse.getRegistrationNo();
            mTvVehicleRegistrationNumber.setText((vehicleDetailsResponse.getRegistrationNo() != null && !vehicleDetailsResponse.getRegistrationNo().isEmpty()) ?
                    vehicleDetailsResponse.getRegistrationNo() :
                    getActivity().getResources().getString(R.string.text_notavailable));
            //TODO Need to show only year.
            mManufactureYear.setText((vehicleDetailsResponse.getDateOfMfg() != null && !vehicleDetailsResponse.getDateOfMfg().isEmpty()) ?
                    REUtils.getManufacturingYear(vehicleDetailsResponse.getDateOfMfg()) : getActivity().getResources().getString(R.string.text_notavailable));
            mEngineNumber.setText((vehicleDetailsResponse.getEngineNo() != null && !vehicleDetailsResponse.getEngineNo().isEmpty()) ?
                    vehicleDetailsResponse.getEngineNo() : getActivity().getResources().getString(R.string.text_notavailable));
            mLastServiced.setText(R.string.text_notavailable);//TODO once we get the last visited date will set the same.
        } else {
            mPurchaseDate.setText(R.string.text_notavailable);
            mTvVehicleRegistrationNumber.setText(R.string.text_notavailable);
            mManufactureYear.setText(R.string.text_notavailable);
            mEngineNumber.setText(R.string.text_notavailable);
            mLastServiced.setText(R.string.text_notavailable);//TODO once we get the last visited date will set the same.
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMotorcycleFragmentInteraction(uri);
        }
    }

    /**
     * Handles serviceHistory views if history is available
     *
     * @param position
     */
    private void handleServiceHistoryViews(int position) {
        mViewServiceHistory.setVisibility(View.VISIBLE);
        //TODO need to remove comments if notes is required
//            viewLineServiceHistory.setVisibility(View.VISIBLE);
        ivServiceHistory.setVisibility(View.VISIBLE);
        mViewServiceHistory.setTextColor(mActivityContext.getResources().getColor(R.color.white));
        ivServiceHistory.setImageDrawable(mActivityContext.getDrawable(R.drawable.next_arrow_copy_5));
        ivServiceHistory.setEnabled(true);
        mViewServiceHistory.setEnabled(true);
        if (mVehicleDetailsList.get(position).getServiceHistoryResponse() != null && mVehicleDetailsList.get(position).getServiceHistoryResponse().size() > 0) {
            ArrayList<ServiceHistoryResponse> response = getSortedServiceHistoryList(mVehicleDetailsList.get(position).getServiceHistoryResponse());
            String mInvoiceDate = response.get(0).getInvoiceDate();
            mLastServiced.setText((mInvoiceDate != null && !mInvoiceDate.isEmpty()) ?
                    REUtils.getOrdinalDateObjectForHistoryDate(getContext(), mInvoiceDate) : getActivity().
                    getResources().getString(R.string.text_notavailable));
        }
    }

    /**
     * Handles NoServiceHistory views if history is unavailable
     */
    private void handleNoServiceHistoryViews() {
        ivServiceHistory.setEnabled(false);
        mViewServiceHistory.setVisibility(View.VISIBLE);
        mViewServiceHistory.setTextColor(mActivityContext.getResources().getColor(R.color.white_30));
        mViewServiceHistory.setEnabled(false);
        //TODO need to remove comments if notes is required
//            viewLineServiceHistory.setVisibility(View.VISIBLE);
        ivServiceHistory.setVisibility(View.VISIBLE);
        ivServiceHistory.setImageDrawable(mActivityContext.getDrawable(R.drawable.next_arrow_grey));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityContext = context;
        if (context instanceof OnMotorcycleFragmentInteractionListener) {
            mListener = (OnMotorcycleFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        params = new Bundle();
        params.putString("eventCategory", "User Profile");
        params.putString("eventAction", "Motorcycles");
        params.putString("eventLabel", mSpinner.getSelectedItem().toString());
        REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
        mVehicleSpinnerPosition = position;
        populateSelectedBikeDetails(position);
        try {
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null, null, mActivityContext.getResources().getDrawable(R.drawable.path_2), null);
            ((TextView) view).setTextColor(mActivityContext.getResources().getColor(R.color.black));
            ((TextView) view).setTypeface(Typeface.createFromAsset(mActivityContext.getAssets(), "montserrat_bold.ttf"));
        } catch (Exception e) {
            RELog.e(e);
        }
        if (mVehicleDetailsList.get(position).getServiceHistoryResponse() != null &&
                mVehicleDetailsList.get(position).getServiceHistoryResponse().size() > 0) {
            handleServiceHistoryViews(position);
        } else {
            handleNoServiceHistoryViews();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_note_arrow_icon:
            case R.id.textView_note:
            case R.id.iv_note_icon:
                Intent notes_intent = new Intent(getActivity(), MotorcycleNotesActivity.class);
                startActivity(notes_intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                break;
            case R.id.tv_service_history:
            case R.id.iv_service_history:
                params = new Bundle();
                params.putString("eventCategory", "User Profile");
                params.putString("eventAction", "Motorcycles");
                params.putString("eventLabel", "View Service History");
                params.putString("modelName", mSpinner.getSelectedItem().toString());
                REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                Intent intent = new Intent(getContext(), ServiceHistoryActivity.class);
                intent.putExtra("position", mVehicleSpinnerPosition);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.add_new_vehicle:
                 params = new Bundle();
                params.putString("eventCategory", "Vehicle Onboarding");
                params.putString("eventAction", "Add new vehicle click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                startActivity(new Intent(getActivity(), AddVehicleActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMotorcycleFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMotorcycleFragmentInteraction(Uri uri);
    }
}
