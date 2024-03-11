package com.royalenfield.reprime.ui.triplisting.view;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REUtils.logConnected;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.databinding.FragmentTripListingBinding;
import com.royalenfield.reprime.ui.calendar.CalendarDialogFragment;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.motorcyclehealthalert.fragment.HealthAlertFragment;
import com.royalenfield.reprime.ui.motorcyclehealthalert.listeners.OnCalendarDateSelectedListener;
import com.royalenfield.reprime.ui.motorcyclehealthalert.utils.HealthAlertUtils;
import com.royalenfield.reprime.ui.tripdetail.view.TripDetailFragment;
import com.royalenfield.reprime.ui.triplisting.presenter.ITripListingPresenter;
import com.royalenfield.reprime.ui.triplisting.presenter.TripListingPresenter;
import com.royalenfield.reprime.ui.triplisting.response.TripMergeResponseModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripListingFragment extends REBaseFragment implements TripItemClickListener
        , OnCalendarDateSelectedListener, ITripListingView, View.OnClickListener, TripDeleteCallback ,TripMergeCallback{

    private static final String KEY_MODEL_NAME = "ModelName";
    private static final String KEY_REGIS_NUM = "RegisNum";

    private REHomeActivity mREHomeActivityInstance;
    private ArrayList<TripItemModel> tripItemModels;
    private TripListAdapter adapter;
    private ImageView imgBack;
    private CalendarDay mSelectedSDate;
    private CalendarDay mSelectedEDate;
    private ITripListingPresenter tripListingPresenter;
    private Activity activity;
    private FragmentTripListingBinding binding;
private  String fromDateFormat;
private String toDateFormat;
private TextView imgEdit;
    public static TripListingFragment getInstance(String modelName, String regisNum) {
        TripListingFragment tripListingFragment = new TripListingFragment();
        Bundle args = new Bundle();
        args.putString(KEY_MODEL_NAME, modelName);
        args.putString(KEY_REGIS_NUM, regisNum);
        tripListingFragment.setArguments(args);
        return tripListingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripListingPresenter = new TripListingPresenter(this);

        buildConnectionForTrip();
    }

    private void hideREHeader() {
        if (activity instanceof REHomeActivity) {
            mREHomeActivityInstance = (REHomeActivity) activity;
            ((REHomeActivity) activity).showHideREHeader(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideREHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activity instanceof REHomeActivity) {
            mREHomeActivityInstance = (REHomeActivity) activity;
            ((REHomeActivity) activity).showHideREHeader(true);
        }
    }

    private void buildConnectionForTrip() {
        showLoading();
        new Thread(() -> tripListingPresenter.createConnectionForTripData()).start();
        REUtils.countDownTimer(this,0, this::showConnectionLostDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_listing, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Connected Trip Summary List");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initializeTopView(view);
        initializeDateView();
        bindDataOnView();
        setAdapter();
        return view;
    }

    public void showConnectionLostDialog(int type) {
		logConnected("tripDetails",KEY_REGIS_NUM,KEY_MODEL_NAME);
        REUtils.showConnectionLostDialog(getActivity(), new REUtils.OnDialogClickListener() {
            @Override
            public void onOkCLick() {
                switch (type){
                    case 0:
                        buildConnectionForTrip();
                        break;
                    case 1:
                        calanderDateSelection();
                        break;
                    case 2:
                        loadMoreClick();
                         break;
					case 3:
						mergeTrip();
						break;
                }


            }

            @Override
            public void onCancelCLick() {
                onClick(imgBack);
            }
        });
    }
private void mergeTrip(){
	List<String> data=	adapter.getSelectedTrips();
	REUtils.countDownTimer(this,3, this::showConnectionLostDialog);
	showLoading();
	new Thread(() -> {
		REApplication.getInstance().getREConnectedAPI()
				.mergeTrip(TripListingFragment.this, data.toArray(new String[data.size()]));
	}).start();
}
    private void setAdapter() {
        adapter = new TripListAdapter(this,getTripData(), this);
        binding.recycleTripListing.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL
                , false);
        binding.recycleTripListing.setLayoutManager(layoutManager);
        binding.recycleTripListing.setHasFixedSize(true);
    }

    private void initializeDateView() {
        DateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("d,", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String fromMonth = monthFormat.format(cal.getTime());
        String fromDate = dateFormat.format(cal.getTime());

        binding.txvFromMonth.setText(fromMonth.toUpperCase());
        binding.txvFromDate.setText(fromDate);

        String year = " " + HealthAlertUtils.YEAR_FORMAT.format(LocalDate.now());
        String eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(LocalDate.now());
        binding.txvFromYear.setText(year);

        String toMonth = HealthAlertUtils.MONTH_FORMAT.format(LocalDate.now());
        String toDate = HealthAlertUtils.DAY_FORMAT.format(LocalDate.now());

        binding.txvToMonth.setText(toMonth.toUpperCase());
        binding.txvToDate.setText(toDate);
        binding.txvToYear.setText(eYear);
         mSelectedSDate=CalendarDay.from(LocalDate.now().minusDays(1));
         mSelectedEDate=CalendarDay.from(LocalDate.now());
    }

    private void initializeTopView(View view) {
        View headerView = view.findViewById(R.id.header_view);
        TextView headerTitle = headerView.findViewById(R.id.txv_header_title);
        headerTitle.setText(getString(R.string.trip_analysis));
        imgBack = headerView.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
		 imgEdit=headerView.findViewById(R.id.img_edit);
		imgEdit.setOnClickListener(this);
		imgEdit.setVisibility(View.VISIBLE);
		imgEdit.setEnabled(false);
      //  binding.txtLoadMore=view.findViewById(R.id.txt_load_more);
        binding.txtLoadMore.setVisibility(View.INVISIBLE);
        binding.txtLoadMore.setOnClickListener(this);

    }

    private void bindDataOnView() {
        if (getArguments() != null) {
            binding.txvMotorcycleModel.setText(getArguments().getString(KEY_REGIS_NUM));
            binding.txvMotorcycleName.setText(getArguments().getString(KEY_MODEL_NAME));
        }
    }

    private List<TripItemModel> getTripData() {
        tripItemModels = new ArrayList<>();
        return tripItemModels;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onTripItemClicked(int adapterPosition) {
        if(tripItemModels.size()>0) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Connected Module");
            params.putString("eventAction", "Trip Summary");
            params.putString("eventLabel", tripItemModels.get(adapterPosition).getDistance());
            params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
            TripItemModel tripItemModel = tripItemModels.get(adapterPosition);
            loadfragment(this, tripItemModel);
        }
    }

    private void loadfragment(TripDeleteCallback callback, TripItemModel tripItemModel) {
        TripDetailFragment fragment = TripDetailFragment.getInstance(tripItemModel,getArguments().getString(KEY_MODEL_NAME));
        fragment.setTripDeleteCallback(callback);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragTrans = fragmentManager.beginTransaction();
            fragTrans.add(R.id.trip_container, fragment);
            fragTrans.addToBackStack(null);
            fragTrans.commit();
        }
    }

    @OnClick(R.id.ll_calendar_view)
    public void onCalendarViewClicked() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Connected Module");
        params.putString("eventAction", "Trip Summary");
        params.putString("eventLabel", "Calendar click");
        params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
        showCalendar();
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

    @Override
    public void OnDateSelected(CalendarDay sDate, CalendarDay eDate) {
        mSelectedSDate = sDate;
        mSelectedEDate = eDate;

        binding.txvFromMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(sDate.getDate()).toUpperCase());
        binding.txvFromDate.setText(HealthAlertUtils.DAY_FORMAT.format(sDate.getDate()));

        String sYear = " " + HealthAlertUtils.YEAR_FORMAT.format(sDate.getDate());
        binding.txvFromYear.setText(sYear);
        String eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(eDate.getDate());

        binding.txvToMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(eDate.getDate()).toUpperCase());
        binding.txvToDate.setText(HealthAlertUtils.DAY_FORMAT.format(eDate.getDate()));
        binding.txvToYear.setText(eYear);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = dateTimeFormatter.format(LocalTime.now());

         fromDateFormat = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(sDate.getDate());
         toDateFormat = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(eDate.getDate());
        tripItemModels.clear();
        updateTripCountValue(tripItemModels.size());
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
binding.txtLoadMore.setVisibility(View.GONE);
       calanderDateSelection();
        Bundle params = new Bundle();
        params.putString("eventCategory", "Connected Module");
        params.putString("eventAction", "Trip Summary");
        params.putString("eventLabel", fromDateFormat+" - "+toDateFormat);
        params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
    }

    private void calanderDateSelection() {
        new Thread(() -> tripListingPresenter.getTripDataWithFilter(getMilisecond(fromDateFormat+" 00:00:00"), getMilisecond(toDateFormat+" 23:59:00"))).start();
        REUtils.countDownTimer(this,1, this::showConnectionLostDialog);
        showLoading();
    }
public String getModelName(){
		return getArguments().getString(KEY_MODEL_NAME);
}

    private void updateTripCountValue(int tripCount) {

        String trip;
        if (tripCount > 1) {
            trip = "TRIPS";
        } else {
            trip = "TRIP";
        }
        binding.txvTripCountValue.setText(String.valueOf(tripCount));
        binding.txvTrip.setText(trip);
    }

    @Override
    public void updateTripCount(int size) {
        if(REUtils.countDownTimer!=null){
            REUtils.countDownTimer.cancel();
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> updateTripCountValue(tripItemModels.size()));
        }
        if(size==0){
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    binding.txtLoadMore.setVisibility(View.INVISIBLE);
                    // Stuff that updates the UI

                });
            }
        }
    }

    @Override
    public void setDataOnView(ArrayList<TripItemModel> tripItemModel) {


        if(tripItemModel.size()>0) {
            tripItemModels.addAll(tripItemModel);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {

                    if (tripItemModel.size() == Integer.parseInt(REUtils.getConnectedFeatureKeys().getTripCount())) {
                        binding.txtLoadMore.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtLoadMore.setVisibility(View.INVISIBLE);
                        //  Toast.makeText(getActivity(),"You're all up to date!",Toast.LENGTH_SHORT).show();
                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                });
            }
        }
        else{
            if( tripItemModel.size()>0) {
                getActivity().runOnUiThread(() -> REUtils.showErrorDialog(getActivity(), "No more trips available"));
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> binding.txtLoadMore.setVisibility(View.INVISIBLE));
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            if (getActivity() != null)
                getActivity().onBackPressed();
        }
        if (view.getId() == R.id.txt_load_more) {
          loadMoreClick();
        }
		if(view.getId()==R.id.img_edit){
			Bundle params = new Bundle();
			params.putString("eventCategory", "Connected Module");
			params.putString("eventAction", "Trip Summary");
			params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
			StringBuilder strbldr=new StringBuilder();
			List<String> data=	adapter.getSelectedTrips();

			for (int i=0;i<data.size();i++) {
				strbldr.append(data.get(i));
				if(i<data.size()-1){
					strbldr.append("|");
				}
			}

			params.putString("tripID",strbldr.toString());
				params.putString("eventLabel", "Merge Trips");


			REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM,params);
			mergeTrip();
		}


	}

    private void loadMoreClick() {
		fromDateFormat = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(mSelectedSDate.getDate());
		toDateFormat = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(mSelectedEDate.getDate());
		Long toDate=Long.parseLong(tripItemModels.get(tripItemModels.size()-1).getTripCreatedTime())-1;
        new Thread(() -> tripListingPresenter.getTripDataWithFilter(getMilisecond(fromDateFormat+" 00:00:00"),toDate.toString())).start();
        REUtils.countDownTimer(this,2, this::showConnectionLostDialog);
        showLoading();
    }

    public String getMilisecond(String date){

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
        Date mDate = sdf.parse(date);
        long timeInMilliseconds = mDate.getTime();
        System.out.println("Date in milli :: " + timeInMilliseconds);
        Log.e("MILISECONDS",timeInMilliseconds+"");
        return (timeInMilliseconds/1000)+"";
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return "";

}

    @Override
    public void onTripDeleteSuccess(String tripId) {

        TripItemModel selectedModel = null;
        for (TripItemModel itemModel : tripItemModels) {
            if (itemModel.getTripId().equals(tripId)) {
                selectedModel = itemModel;
                break;
            }
        }
        if (selectedModel != null) {
            tripItemModels.remove(selectedModel);
            adapter.notifyDataSetChanged();
            updateTripCountValue(tripItemModels.size());
        }
		List<String> tripitme1=	adapter.getSelectedTrips();
		if(tripitme1!=null&&tripitme1.size()>1){
			setMergeVisibility(true);
		}
		else{
			setMergeVisibility(false);
		}
    }
	public void setMergeVisibility(boolean showMerge){
imgEdit.setEnabled(showMerge);

	}

	@Override
	public void onTripMergeSuccess(TripMergeResponseModel message) {
		hideLoading();
		setMergeVisibility(false);
		if(REUtils.countDownTimer!=null){
			REUtils.countDownTimer.cancel();
		}
		if (getActivity() != null) {
		getActivity().runOnUiThread(() -> REUtils.showErrorDialogWithTitle(getActivity(),getString(R.string.update_alert), message.getData().getCreateTripMerge().getMessage(), new REUtils.OnDialogButtonClickListener() {
			@Override
			public void onOkCLick() {

				if(message.getData().getCreateTripMerge().getStatus()){
					Bundle params = new Bundle();
					params.putString("eventCategory", "Connected Module");
					params.putString("eventAction", "Trip Merge Pop Up");
					params.putString("modelName",getArguments().getString(KEY_MODEL_NAME));
					//	params.putString("tripID",tripid);
					params.putString("eventLabel", "Successful");
					REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
					buildConnectionForTrip();
					initializeDateView();
					setAdapter();
				}

			}
		}));
		}

	}



}
