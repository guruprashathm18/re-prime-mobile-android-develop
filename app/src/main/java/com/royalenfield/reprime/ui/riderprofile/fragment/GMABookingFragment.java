package com.royalenfield.reprime.ui.riderprofile.fragment;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.web.booking.Datum;
import com.royalenfield.reprime.models.response.web.booking.FinanceModel;
import com.royalenfield.reprime.models.response.web.booking.Group;
import com.royalenfield.reprime.models.response.web.booking.PartsDatum;
import com.royalenfield.reprime.models.response.web.booking.PaymentModeModel;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.phoneconfigurator.view.PCWebViewActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.adapter.ExpandableListViewAdapter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class GMABookingFragment extends REBaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = GMABookingFragment.class.getSimpleName();
    private Spinner mSpinner;
    private int mSpinnerSelectedPosition = -1;
    public List<Datum> listSuccess = new ArrayList<>();
    private TextView txtModelName;
    private TextView txtTransId;
    private TextView txtBookingStatus;
    private TextView txtTransStatus;
    private TextView txtBookingId;
    private TextView txtBookingDate;
    private ConstraintLayout bookingConstraintLayout;
    private TextView txtNoBooking;
    private ImageView imgArrow;
    private TextView txtStatus;
    private TextView txtModelCode;
    private View viewUnderLine;
    private ExpandableListView mListView;
    private TextView trackYourOrder;
    private TextView txtTrackOrder1;
    private TextView tvLblModel;
    private TextView tvLblModelCode;
    public GMABookingFragment() {
        // Required empty public constructor
    }

    public static GMABookingFragment newInstance() {
        // Required empty public constructor
        return new GMABookingFragment();
    }


    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Loading the UI in Handler because it lags if we load in MainThread
    }
    public void setDataAndAdapter(){
        showLoading();
        listSuccess.clear();
        if(((REProfileActivity)getActivity()).bookingGMAResponse!=null)
            listSuccess.addAll(((REProfileActivity)getActivity()).bookingGMAResponse.getData());
        txtNoBooking.setText(((REProfileActivity)getActivity()).bookingGMAError);
        mSpinner.setVisibility(View.GONE);
        imgArrow.setVisibility(View.GONE);
        txtStatus.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {

            //Binds data to views.....
            if (listSuccess.size() == 0) {
                hideLoading();
                txtNoBooking.setVisibility(View.VISIBLE);
                mSpinner.setVisibility(View.GONE);
                // imgArrow.setVisibility(View.GONE);
                txtStatus.setVisibility(View.GONE);
            } else {
                txtNoBooking.setVisibility(View.GONE);
                mSpinner.setVisibility(View.VISIBLE);
                //  imgArrow.setVisibility(View.VISIBLE);
                txtStatus.setVisibility(View.GONE);
            }
            bindData(mSpinnerSelectedPosition==-1?0:mSpinnerSelectedPosition); //Setting first position to spinner

        }, 300);
    }

    @Override
    public void onClick(View v) {
    }


    /**
     * Initialising views and fragments
     *
     * @param rootView : rootView
     */
    private void initViews(View rootView) {
        mSpinner = rootView.findViewById(R.id.spinner_booking_status_list);
        txtModelName = rootView.findViewById(R.id.tv_model_name);
        txtTransId = rootView.findViewById(R.id.tv_trans_id);
        txtBookingStatus = rootView.findViewById(R.id.tv_booking_status);
        txtTransStatus = rootView.findViewById(R.id.tv_trans_status);
        bookingConstraintLayout = rootView.findViewById(R.id.constraintLayout_booking_details);
        bookingConstraintLayout.setVisibility(View.GONE);
        txtBookingId = rootView.findViewById(R.id.tv_booking_id);
        txtBookingDate = rootView.findViewById(R.id.tv_date_of_booking);
        txtNoBooking = rootView.findViewById(R.id.text_error);
        imgArrow=rootView.findViewById(R.id.calendar_image);
        txtStatus=rootView.findViewById(R.id.textView_status);
        txtModelCode=rootView.findViewById(R.id.tv_model_code);
        viewUnderLine=rootView.findViewById(R.id.under_line);
        viewUnderLine.setVisibility(View.GONE);
        trackYourOrder=rootView.findViewById(R.id.track_your_order);
        trackYourOrder.setVisibility(View.GONE);
        tvLblModel=rootView.findViewById(R.id.tv_label_bike_name);
        tvLblModelCode=rootView.findViewById(R.id.tv_label_bike_code);
        tvLblModelCode.setVisibility(View.GONE);
        tvLblModel.setVisibility(View.GONE);
        txtModelCode.setVisibility(View.GONE);
        txtModelName.setVisibility(View.GONE);
        txtTrackOrder1=rootView.findViewById(R.id.track_your_order_1);
        mListView = (ExpandableListView) rootView.findViewById(R.id.activity_expandable_list_view);
        mListView.setScrollContainer(false);
        txtTrackOrder1.setVisibility(View.GONE);
//        mSpinner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    Bundle params = new Bundle();
//                    params.putString("eventCategory", "User Profile");
//                    params.putString("eventAction", "Accessories Booking");
//                    params.putString("eventLabel","Accessories Order" );
//                    // params.putString("dropDownOptions", mSpinner.getSelectedItem().toString());
//                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
//                }
//                return false;
//            }
//        });
        mListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    /**
     * Binds the rides profile data to views.....
     */
    private void bindData(int position) {
        if (getContext() != null) {
//            mSpinner.setAdapter(new MyAdapter(getActivity(), R.layout.layout_spinner_item,
//                    listSuccess));
            // mSpinner.setSelection(position);
            List<String> bikeList = new ArrayList<>();
            for (int i = 0; i < listSuccess.size(); i++) {
                //   String modelCode = listSuccess.get(i).getModelName();
                String bookingDate = listSuccess.get(i).getBookingDate();
                //     String truncatedModelName = REUtils.bikeNameInSpinner(modelName);
                bikeList.add("Accessories Order - "+bookingDate);
            }
            //Set Spinner adapter for the bike list..
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getActivity(), R.layout.item_booking_spinner, bikeList);
            adapter.setDropDownViewResource(R.layout.layout_spinner_item);
            // spinner.setAdapter(adapter);
            mSpinner.setAdapter(adapter);
            mSpinner.setOnItemSelectedListener(this);
            mSpinner.setSelection(position);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerSelectedPosition = position;
        Bundle params = new Bundle();
        params.putString("eventCategory", "User Profile");
        params.putString("eventAction", "Accessories Booking");
        params.putString("eventLabel",mSpinner.getSelectedItem().toString() );
      //  params.putString("dropDownOptions", mSpinner.getSelectedItem().toString());
        REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

        setCardData();
    }

    private void setCardData() {
        if (mSpinnerSelectedPosition >= 0) {
             bookingConstraintLayout.setVisibility(View.VISIBLE);
//            txtModelCode.setText(listSuccess.get(mSpinnerSelectedPosition).getModelCode());
//            txtModelName.setText(listSuccess.get(mSpinnerSelectedPosition).getModelName());
            txtTransId.setText(listSuccess.get(mSpinnerSelectedPosition).getBillDeskTrnsactionId());
            txtBookingStatus.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingStatus());
            txtTransStatus.setText(listSuccess.get(mSpinnerSelectedPosition).getPaymentStatus());
            txtBookingDate.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingDate());
            txtBookingId.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingId());
            List<PartsDatum> listParts = listSuccess.get(mSpinnerSelectedPosition).getPartsData();
            if (listParts == null || listParts.size() == 0)
                mListView.setVisibility(View.GONE);
            else {
                mListView.setVisibility(View.VISIBLE);
                SparseArray<Group> mGroups = new SparseArray<Group>();
                Group group = new Group(getResources().getString(R.string.genuine_accessories));
                for (int i = 0; i < listParts.size(); i++) {
                    group.children.add(listParts.get(i).getPartdescription());
                }
                mGroups.append(0, group);


                ExpandableListAdapter adapter = new ExpandableListViewAdapter(getActivity(),
                        mGroups);
                mListView.setAdapter(adapter);
                setListViewHeight(mListView);
                mListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Accessories Booking");
                    params.putString("dropDownOptions","Genuine Motorcycle Accessories" );
                    params.putString("eventLabel", mSpinner.getSelectedItem().toString());
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

                    setListViewHeight(parent, groupPosition);
                    return false;
                });


            }

        }
        hideLoading();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void setListViewHeight(ExpandableListView listView) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupView = listAdapter.getGroupView(i, true, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();

            if (listView.isGroupExpanded(i)){
                for(int j = 0; j < listAdapter.getChildrenCount(i); j++){
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height+5;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
