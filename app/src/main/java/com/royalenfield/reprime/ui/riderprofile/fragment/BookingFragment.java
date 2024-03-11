package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.phoneconfigurator.view.PCWebViewActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.adapter.ExpandableListViewAdapter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.TRANS_SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class BookingFragment extends REBaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = BookingFragment.class.getSimpleName();
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
    private ExpandableListView mListView;
    private ExpandableListView mListViewFinance;
    private ExpandableListView mListViewPaymentMode;
    private ExpandableListView mListView2;
    private ExpandableListView mListView4;
    private TextView trackYourOrder;
    private TextView financeBooking;
    private TextView track_your_order_1;
    private View viewUnderLine;
    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance() {
        // Required empty public constructor
        return new BookingFragment();
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



//        SparseArray<Group> mGroups1 = new SparseArray<Group>();
//        Group group1 = new Group("Ride Sure");
//        for (int i = 0; i < 5; i++) {
//            group1.children.add("Ride Item" + i);
//        }
//        mGroups1.append(0, group1);
//        SparseArray<Group> mGroups2 = new SparseArray<Group>();
//        Group group2 = new Group("Finance Details");
//        for (int i = 0; i < 5; i++) {
//            FinanceModel moel=new FinanceModel();
//            moel.setBillDeskTrnsactionId("test"+i);
//            group2.children.add(moel);
//        }
//        mGroups2.append(0, group2);
//        ExpandableListAdapter adapter1 = new ExpandableListViewAdapter(getActivity(),
//                mGroups1);
//        mListView1.setAdapter(adapter1);
//        setListViewHeight(mListView1);
//        mListView1.setOnGroupClickListener((parent, v, groupPosition, id) -> {
//            setListViewHeight(parent, groupPosition);
//            return false;
//        });
//
//        ExpandableListAdapter adapter2 = new ExpandableListViewAdapter(getActivity(),
//                mGroups2);
//        mListView2.setAdapter(adapter2);
//        setListViewHeight(mListView2);
//        mListView2.setOnGroupClickListener((parent, v, groupPosition, id) -> {
//            setListViewHeight(parent, groupPosition);
//            return false;
//        });
    }
    public void setDataAndAdapter(){
        showLoading();
        listSuccess.clear();
        if(((REProfileActivity)getActivity()).bookingResponse!=null)
            listSuccess.addAll(((REProfileActivity)getActivity()).bookingResponse.getData());
        txtNoBooking.setText(((REProfileActivity)getActivity()).bookingError);
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
        if(v.getId()==R.id.track_your_order||v.getId()==R.id.track_your_order_1){
            Bundle params = new Bundle();
            params.putString("eventCategory", "User Profile");
            params.putString("eventAction", "Bookings");
            if(v.getId()==R.id.track_your_order_1)
                params.putString("eventLabel", "Track Your Order - Top");
            else
                params.putString("eventLabel", "Track Your Order - Bottom");
            params.putString("modelName", txtModelName.getText().toString());
            REUtils.logGTMEvent(REConstants.KEY_TRACK_ORDER_GTM, params);
            Intent intent = new Intent(getActivity(), PCWebViewActivity.class);
            intent.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_OPEN_ORDER_TRACKING);
            intent.putExtra(PCUtils.ORDER_ID,listSuccess.get(mSpinnerSelectedPosition).getPaymentCaseId());
            startActivity(intent);
        }
        else  if(v.getId()==R.id.finance_booking){
            Intent intent = new Intent(getActivity(), PCWebViewActivity.class);
            intent.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_OPEN_FINANCE);
            intent.putExtra(PCUtils.ORDER_ID,listSuccess.get(mSpinnerSelectedPosition).getPaymentCaseId());
            startActivity(intent);
        }
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
        mListView = (ExpandableListView) rootView.findViewById(R.id.activity_expandable_list_view);
        mListViewFinance = (ExpandableListView) rootView.findViewById(R.id.activity_expandable_list_view1);
        mListViewPaymentMode = (ExpandableListView) rootView.findViewById(R.id.activity_expandable_list_view2);
        mListView2 = (ExpandableListView) rootView.findViewById(R.id.activity_expandable_list_view3);

        mListView.setScrollContainer(false);
        mListView2.setScrollContainer(false);
        mListViewFinance.setScrollContainer(false);
        mListViewPaymentMode.setScrollContainer(false);
        mListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        mListViewFinance.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        mListViewPaymentMode.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        mListView2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        trackYourOrder=rootView.findViewById(R.id.track_your_order);
        track_your_order_1=rootView.findViewById(R.id.track_your_order_1);
        trackYourOrder.setOnClickListener(this);
        trackYourOrder.setVisibility(View.GONE);
        track_your_order_1.setOnClickListener(this);
        track_your_order_1.setVisibility(View.GONE);
        viewUnderLine=rootView.findViewById(R.id.under_line);
        viewUnderLine.setVisibility(View.GONE);
        financeBooking=rootView.findViewById(R.id.finance_booking);
        financeBooking.setOnClickListener(this);
        financeBooking.setVisibility(View.GONE);
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
                String modelName = listSuccess.get(i).getModelName();
                //     String truncatedModelName = REUtils.bikeNameInSpinner(modelName);
                bikeList.add(modelName);
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
        Bundle  params = new Bundle();
        params.putString("eventCategory", "User Profile");
        params.putString("eventAction", "Bookings");
        params.putString("eventLabel", mSpinner.getSelectedItem().toString());
        REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

        setCardData();
    }

    private void setCardData() {
        if (mSpinnerSelectedPosition >= 0) {
            Boolean showOrderTracking = listSuccess.get(mSpinnerSelectedPosition).getShowOrderTracking();
            if (showOrderTracking != null && showOrderTracking && REUtils.getFeatures().getDefaultFeatures().getShowTrackOrder().equalsIgnoreCase(FEATURE_ENABLED)) {
                trackYourOrder.setVisibility(View.VISIBLE);
                financeBooking.setVisibility(View.GONE);
                viewUnderLine.setVisibility(View.VISIBLE);
                track_your_order_1.setVisibility(View.VISIBLE);
            } else {
                trackYourOrder.setVisibility(View.GONE);
                financeBooking.setVisibility(View.GONE);
                viewUnderLine.setVisibility(View.GONE);
                track_your_order_1.setVisibility(View.GONE);
            }
            bookingConstraintLayout.setVisibility(View.VISIBLE);
            txtModelCode.setText(listSuccess.get(mSpinnerSelectedPosition).getModelCode());
            txtModelName.setText(listSuccess.get(mSpinnerSelectedPosition).getModelName());
            txtTransId.setText(listSuccess.get(mSpinnerSelectedPosition).getBillDeskTrnsactionId());
            txtBookingStatus.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingStatus());
            txtTransStatus.setText(listSuccess.get(mSpinnerSelectedPosition).getPaymentStatus());
            txtBookingDate.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingDate());
            txtBookingId.setText(listSuccess.get(mSpinnerSelectedPosition).getBookingId());
            List<PartsDatum> listParts = listSuccess.get(mSpinnerSelectedPosition).getPartsData();
            List<PartsDatum> listAppraelData = listSuccess.get(mSpinnerSelectedPosition).getApparelData();
            List<FinanceModel> finance = listSuccess.get(mSpinnerSelectedPosition).getFinancerDetails();
            PaymentModeModel paymentMode = listSuccess.get(mSpinnerSelectedPosition).getPaymentModeDetails();
            if (finance != null) {
                mListViewFinance.setVisibility(View.VISIBLE);
                SparseArray<Group> mGroupsFinance = new SparseArray<Group>();
                Group groupFinance = new Group(getResources().getString(R.string.finance_details));

                groupFinance.children.add(finance);
                mGroupsFinance.append(0, groupFinance);


                ExpandableListAdapter adapterFinance = new ExpandableListViewAdapter(getActivity(),
                        mGroupsFinance);
                mListViewFinance.setAdapter(adapterFinance);
                setListViewHeight(mListViewFinance);
                mListViewFinance.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Bookings");
                    params.putString("eventLabel", mSpinner.getSelectedItem().toString());
                    params.putString("dropDownOptions", "Finance Details  clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

                    setListViewHeight(parent, groupPosition);
                    return false;
                });
            } else {
                mListViewFinance.setVisibility(View.GONE);
            }

            if (paymentMode != null) {
                mListViewPaymentMode.setVisibility(View.VISIBLE);
                SparseArray<Group> mGroupsFinance = new SparseArray<Group>();
                Group groupFinance = new Group(getResources().getString(R.string.mode_of_payment));

                groupFinance.children.add(paymentMode);
                mGroupsFinance.append(0, groupFinance);


                ExpandableListAdapter adapterFinance = new ExpandableListViewAdapter(getActivity(),
                        mGroupsFinance);
                mListViewPaymentMode.setAdapter(adapterFinance);
                setListViewHeight(mListViewPaymentMode);
                mListViewPaymentMode.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Bookings");
                    params.putString("eventLabel", mSpinner.getSelectedItem().toString());
                    params.putString("dropDownOptions", "Payment mode clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

                    setListViewHeight(parent, groupPosition);
                    return false;
                });
            } else {
                mListViewPaymentMode.setVisibility(View.GONE);
            }

            if (listParts == null || listParts.size() == 0)
                mListView.setVisibility(View.GONE);
            else {
                mListView.setVisibility(View.VISIBLE);
                SparseArray<Group> mGroups = new SparseArray<Group>();
                Group group = new Group(getResources().getString(R.string.accessories_ride_sure));
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
                    params.putString("eventAction", "Bookings");
                    params.putString("eventLabel", mSpinner.getSelectedItem().toString());
                    params.putString("dropDownOptions", "Accessories and Ride Sure clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

                    setListViewHeight(parent, groupPosition);
                    return false;
                });


            }

            if (listAppraelData == null || listAppraelData.size() == 0)
                mListView2.setVisibility(View.GONE);
            else {
                mListView2.setVisibility(View.VISIBLE);
                SparseArray<Group> mGroups = new SparseArray<Group>();
                Group group = new Group(getResources().getString(R.string.apprael_parts));
                for (int i = 0; i < listAppraelData.size(); i++) {
                    group.children.add(listAppraelData.get(i));
                }
                mGroups.append(0, group);


                ExpandableListAdapter adapter = new ExpandableListViewAdapter(getActivity(),
                        mGroups);
                mListView2.setAdapter(adapter);
                setListViewHeight(mListView2);
                mListView2.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Bookings");
                    params.putString("eventLabel", mSpinner.getSelectedItem().toString());
                    params.putString("dropDownOptions", "Apprael clicked");
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

    // Creating an Adapter Class
    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         List<Datum> objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View view,
                                  ViewGroup parent) {

// Inflating the layout for the custom Spinner
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.layout_spinner_item, parent, false);

// Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout
                    .findViewById(R.id.tv_spinner_item);

// Setting the text using the array
            tvLanguage.setText(listSuccess.get(position).getModelName());
            return layout;
        }


        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

//    @Override
//    public void bookingSuccess(BookingResponse response) {
//        hideLoading();
//        listSuccess.clear();
//        listSuccess.addAll(response.getData());
//        new Handler().postDelayed(() -> {
//            //Binds data to views.....
//            bindData(0); //Setting first position to spinner
//        }, 1000);
//        if (listSuccess.size() == 0) {
//            txtNoBooking.setVisibility(View.VISIBLE);
//            mSpinner.setVisibility(View.GONE);
//            imgArrow.setVisibility(View.GONE);
//        } else {
//            txtNoBooking.setVisibility(View.GONE);
//            mSpinner.setVisibility(View.VISIBLE);
//            imgArrow.setVisibility(View.VISIBLE);
//        }
//    }

}
