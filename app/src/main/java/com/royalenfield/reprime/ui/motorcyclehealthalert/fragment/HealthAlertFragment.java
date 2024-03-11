package com.royalenfield.reprime.ui.motorcyclehealthalert.fragment;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REUtils.logConnected;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.databinding.HealthAlertFragmentBinding;
import com.royalenfield.reprime.ui.calendar.CalendarDialogFragment;
import com.royalenfield.reprime.ui.home.homescreen.fragments.MotorCycleFragment;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.motorcyclehealthalert.adapter.VehicleHealthAlertAdapter;
import com.royalenfield.reprime.ui.motorcyclehealthalert.listeners.OnCalendarDateSelectedListener;
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.GetAllAlertsByUniqueId;
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.VehicleAlertResponse;
import com.royalenfield.reprime.ui.motorcyclehealthalert.utils.HealthAlertUtils;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthAlertFragment extends REBaseFragment
        implements OnCalendarDateSelectedListener, View.OnClickListener, HealthAlertInterface {

    private static final String KEY_MODEL_NAME = "ModelName";
    private static final String KEY_REGIS_NUM = "RegisNum";
    private static final String KEY_CHIS_NUM = "ChesisNum";

    private ImageView back;
    private CalendarDay mSelectedSDate;
    private CalendarDay mSelectedEDate;
    private HealthAlertFragmentBinding binding;
    private VehicleHealthAlertAdapter adapter;
    private String chessiNumber;
    private TextView txtLoadMore;
    private  List<GetAllAlertsByUniqueId> alertList=new ArrayList<>();


    public static HealthAlertFragment getInstance(String modelName, String regisNum,String chesisNo) {
        HealthAlertFragment healthAlertFragment = new HealthAlertFragment();
        Bundle args = new Bundle();
        args.putString(KEY_MODEL_NAME, modelName);
        args.putString(KEY_REGIS_NUM, regisNum);
        args.putString(KEY_CHIS_NUM,chesisNo);
        healthAlertFragment.setArguments(args);
        return healthAlertFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        hideREHeaderFromActivity(context, false);

        if (!REApplication.CODE_STUB_DEMO) {
            buildSocketConnection();
        }
    }

    private void getStubbedData() {
        VehicleAlertResponse deviceLatestLocation = new Gson().fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(),
                "vehicle_alert_data.json"), VehicleAlertResponse.class);
        vehicleAlertResponseSuccess(deviceLatestLocation);
    }

    private void buildSocketConnection() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        //String pastDate = dateFormat.format(cal.getTime()); //your formatted date here
        //String date = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(LocalDate.now());

        String pastDate = cal.getTimeInMillis() / 1000 + "";
        String date = System.currentTimeMillis() / 1000 + "";

        /*call socket in new thread*/
        showLoading();
        new Thread(() -> REApplication.getInstance().getREConnectedAPI()
                .createConnectionForHealthAlert(HealthAlertFragment.this, null,REUtils.getConnectedFeatureKeys().getHealthAlertCount(),"OPEN"))
                .start();
        /*custom timer for hide loader if socket not respond in 10 sec*/
        //REUtils.countDownTimer(this, 10000);
        REUtils.countDownTimer(this, 0,this::showConnectionLostDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.health_alert_fragment, container, false);
        View view = binding.getRoot();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Connected Vehicle Alerts");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        back = view.findViewById(R.id.img_back);
        back.setOnClickListener(this);
        ((TextView) view.findViewById(R.id.txv_header_title)).setText(getString(R.string.vehicle_alert));
        txtLoadMore=view.findViewById(R.id.txt_load_more_health);
        txtLoadMore.setVisibility(View.GONE);
        txtLoadMore.setOnClickListener(this);
		view.findViewById(R.id.img_edit).setVisibility(View.GONE);
        binding.rbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Vehicle Alerts");
                params.putString("eventLabel", "All");
                params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                if (REApplication.getInstance().getREConnectedAPI().getStompClientInstance().isConnected()) {
                    alertList=new ArrayList<>();
                    if (adapter != null && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                                adapter.resetVehicleData(alertList);


                        });
                    }
                    REApplication.getInstance().getREConnectedAPI()
                            .sendForHealthAlertEvent(HealthAlertFragment.this, null,REUtils.getConnectedFeatureKeys().getHealthAlertCount(),"ALL");

                    showLoading();
                }
            }
        });
        binding.rbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Vehicle Alerts");
                params.putString("eventLabel", "Open");
                params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                if (REApplication.getInstance().getREConnectedAPI().getStompClientInstance().isConnected()) {
                    alertList=new ArrayList<>();
                    if (adapter != null && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            adapter.resetVehicleData(alertList);


                        });
                    }
                    REApplication.getInstance().getREConnectedAPI()
                            .sendForHealthAlertEvent(HealthAlertFragment.this, null,REUtils.getConnectedFeatureKeys().getHealthAlertCount(),"OPEN");

                    showLoading();
                }
            }
        });
        initHealthAlertList();
    }

    public void showConnectionLostDialog(int type) {
		logConnected("getAllAlertsByUniqueId",KEY_REGIS_NUM,KEY_MODEL_NAME);
        REUtils.showConnectionLostDialog(getActivity(), new REUtils.OnDialogClickListener() {
            @Override
            public void onOkCLick() {
                switch (type){
                    case 0:
                        buildSocketConnection();
                        break;
                    case 1:
                        loadMoreClick();
                        break;

                }

            }

            @Override
            public void onCancelCLick() {
                onClick(back);
            }
        });
    }

    private void initHealthAlertList() {
        adapter = new VehicleHealthAlertAdapter(getActivity(), getData(),getArguments().getString(KEY_MODEL_NAME));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);
    }

    public static List<GetAllAlertsByUniqueId> getData() {
        List<GetAllAlertsByUniqueId> list = new ArrayList<>();
        return list;
    }


    private void showCalendar() {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(getString(R.string.calendar_dialog_fragment));
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CalendarDialogFragment dialogFragment = new CalendarDialogFragment();
        if (mSelectedSDate != null && mSelectedEDate != null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(CalendarDialogFragment.KEY_START_DATE, mSelectedSDate);
            bundle.putParcelable(CalendarDialogFragment.KEY_END_DATE, mSelectedEDate);
            dialogFragment.setArguments(bundle);
        }

        dialogFragment.setCalendarDateSelectedListener(this);
        dialogFragment.show(ft, getString(R.string.calendar_dialog_fragment));
    }

    private void hideREHeaderFromActivity(Context activity, boolean hideShowValue) {
        try {
            ((REHomeActivity) activity).showHideREHeader(hideShowValue);
        } catch (ClassCastException e) {
         //   e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideREHeaderFromActivity(getContext(), false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hideREHeaderFromActivity(getContext(), true);
        ((MotorCycleFragment)getParentFragment()).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void OnDateSelected(CalendarDay sDate, CalendarDay eDate) {

        mSelectedSDate = sDate;
        mSelectedEDate = eDate;

//        binding.startMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(sDate.getDate()).toUpperCase());
//        binding.startDate.setText(HealthAlertUtils.DAY_FORMAT.format(sDate.getDate()));
//        String sYear = " " + HealthAlertUtils.YEAR_FORMAT.format(sDate.getDate());
//        binding.startYear.setText(sYear);
//        String eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(eDate.getDate());
//        binding.endMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(eDate.getDate()).toUpperCase());
//        binding.endDate.setText(HealthAlertUtils.DAY_FORMAT.format(eDate.getDate()));
//        binding.endYear.setText(eYear);

        // create a calendar
        Calendar calStart = Calendar.getInstance();

        calStart.set(sDate.getYear(), sDate.getMonth() - 1, sDate.getDay());
        calStart.set(Calendar.HOUR, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        System.out.println("Current year is :" + calStart.getTime());
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(eDate.getYear(), eDate.getMonth() - 1, eDate.getDay());
        calEnd.set(Calendar.HOUR, 0);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.SECOND, 59);
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        System.out.println("Current year is :" + calEnd.getTime());

        if (REApplication.getInstance().getREConnectedAPI().getStompClientInstance().isConnected()) {
            REApplication.getInstance().getREConnectedAPI()
                    .sendForHealthAlertEvent(HealthAlertFragment.this, null,REUtils.getConnectedFeatureKeys().getHealthAlertCount(),"OPEN");

            showLoading();
        }

    }

    @Override
    public void vehicleAlertResponseSuccess(VehicleAlertResponse payload) {
        hideLoading();
        if(REUtils.countDownTimer!=null){
            REUtils.countDownTimer.cancel();
        }
        if(payload.getData()!=null&&payload.getData().getGetAllAlertsByUniqueId().size()>0) {
           getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if(payload.getData().getGetAllAlertsByUniqueId().size()==Integer.parseInt(REUtils.getConnectedFeatureKeys().getHealthAlertCount())){
                        txtLoadMore.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtLoadMore.setVisibility(View.GONE);
                      //  Toast.makeText(getActivity(),"You're all up to date!",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            if(alertList.size()==0&&binding.rbOpen.isChecked()) {
                FirestoreManager.getInstance().updateAlertTimestamp(payload.getData().getGetAllAlertsByUniqueId().get(0).getFromTs() + "", getArguments().getString(KEY_CHIS_NUM), new OnFirestoreCallback() {
                    @Override
                    public void onFirestoreSuccess() {

                    }

                    @Override
                    public void onFirestoreFailure(String message) {

                    }
                });
            }
            if (adapter != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (payload.getData() != null) {

                        List<GetAllAlertsByUniqueId> list = payload.getData().getGetAllAlertsByUniqueId();
                        alertList.addAll(list);
                        adapter.setVehicleData(list);
                    }

                });
            }
        }
        else{
            if(alertList.size() != 0) {
                getActivity().runOnUiThread(() -> {
                    REUtils.showErrorDialog(getActivity(), "No more alerts available");
                    txtLoadMore.setVisibility(View.GONE);
                });
            }
        }
        getActivity().runOnUiThread(() -> {
            if (alertList.size() == 0) {
                binding.txtNoAlerts.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.txtNoAlerts.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.health_alert_back:
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
                break;
            case R.id.img_back:
                if (getActivity() != null)
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                break;
            case R.id.txt_load_more_health:
                loadMoreClick();

                break;
        }
    }

    private void loadMoreClick() {
        if (REApplication.getInstance().getREConnectedAPI().getStompClientInstance().isConnected()) {
            REApplication.getInstance().getREConnectedAPI()
                    .sendForHealthAlertEvent(HealthAlertFragment.this, alertList.get(alertList.size()-1).getFromTs(),REUtils.getConnectedFeatureKeys().getHealthAlertCount(),binding.rbOpen.isChecked()?"OPEN":"ALL");

            showLoading();
            REUtils.countDownTimer(this,1, this::showConnectionLostDialog);
        }
    }
}
