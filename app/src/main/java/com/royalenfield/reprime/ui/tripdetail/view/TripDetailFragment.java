package com.royalenfield.reprime.ui.tripdetail.view;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REUtils.logConnected;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.databinding.FragmentTripDetailBinding;
import com.royalenfield.reprime.ui.tripdetail.presenter.ITripDetailPresenter;
import com.royalenfield.reprime.ui.tripdetail.presenter.TripDetailPresenter;
import com.royalenfield.reprime.ui.triplisting.response.DataPoint;
import com.royalenfield.reprime.ui.triplisting.response.TravelHistory;
import com.royalenfield.reprime.ui.triplisting.response.TravelHistoryResponseModel;
import com.royalenfield.reprime.ui.triplisting.response.TripDelete;
import com.royalenfield.reprime.ui.triplisting.response.TripPoint;
import com.royalenfield.reprime.ui.triplisting.response.TripsAggregated;
import com.royalenfield.reprime.ui.triplisting.view.TripDeleteCallback;
import com.royalenfield.reprime.ui.triplisting.view.TripItemModel;
import com.royalenfield.reprime.utils.OnBlurDialogPositiveButtonClickListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.ButterKnife;

public class TripDetailFragment extends REBaseFragment implements OnMapReadyCallback
		, View.OnClickListener, ITripView, TravelHistoryCallback {

	private static final String KEY_TRIP_ID = "TripID";
	private static final String KEY_MODEL_NAME = "ModelName";
	private ArrayList<LatLng> mlatLngs = new ArrayList<>();
	private GoogleMap mGoogleMap;

	private TripDeleteCallback mTripDeleteCallback;
	private boolean tripDeleteSuccess = false;
	private WorkaroundMapFragment supportMapFragment;
	private Dialog settingDialog;
	private FragmentTripDetailBinding binding;
private ImageView imgBack;

	public static TripDetailFragment getInstance(TripItemModel tripId, String modelName) {
		TripDetailFragment fragment = new TripDetailFragment();
		Bundle args = new Bundle();
		args.putSerializable(KEY_TRIP_ID, tripId);
		args.putString(KEY_MODEL_NAME, modelName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (getView() != null)
			getView().post(this::setMapHeightDynamically);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_detail, container, false);
		View view = binding.getRoot();
		ButterKnife.bind(this, view);
		Bundle paramsScr = new Bundle();
		paramsScr.putString("screenname", "Connected Trip Summary Detail");
		REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
		initializeTopView(view);
		//   initializeTripDetailView();
		return view;
	}

	private void initializeTripDetailView(TravelHistory history) {

		if (history != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (history.getTripType().equalsIgnoreCase("MERGED")) {
						binding.spinnerBookingStatusList.setVisibility(View.VISIBLE);
						List<String> tripList = new ArrayList<>();
						//Set Spinner adapter for the bike list..


						SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
						tripList.add(formatter.format(new Date(Long.parseLong(history.getTripCreatedTs()) * 1000)));
						for (TripsAggregated aggregated : history.getTripsAggregated()) {
							tripList.add(formatter.format(new Date(Long.parseLong(aggregated.getTripCreatedTs()) * 1000)));
						}
						ArrayAdapter<String> adapter =
								new ArrayAdapter<>(getActivity(), R.layout.item_booking_spinner, tripList);
						adapter.setDropDownViewResource(R.layout.layout_spinner_item);
						// spinner.setAdapter(adapter);
						binding.spinnerBookingStatusList.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (event.getAction() == MotionEvent.ACTION_UP) {
									Bundle params = new Bundle();
									params.putString("eventCategory", "Connected Module");
									params.putString("eventAction", "Trip Summary");
									params.putString("eventLabel", "Drop down click");
									params.putString("modelName", getArguments().getString(KEY_MODEL_NAME));
									REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
								}
								return false;
							}
						});

						binding.spinnerBookingStatusList.setAdapter(adapter);
						binding.spinnerBookingStatusList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								Bundle params = new Bundle();
								params.putString("eventCategory", "Connected Module");
								params.putString("eventAction", "Trip Summary");
								params.putString("eventLabel", "Drop down item select");
								params.putString("modelName", getArguments().getString(KEY_MODEL_NAME));

								if (position == 0) {
									params.putString("tripID",history.getTripId());
									TripsAggregated data = new TripsAggregated();
									data.setFromTs(history.getFromTs());
									data.setTripCreatedTs(history.getTripCreatedTs());
									data.setToTs(history.getToTs());
									data.setTotalDist(history.getTotalDist());
									data.setStartAddress(history.getStartAddress());
									data.setEndAddress(history.getEndAddress());
									data.setTotalHaCount(history.getTotalHaCount());
									data.setTotalHbCount(history.getTotalHbCount());
									data.setOverspeedCount(history.getOverspeedCount());
									data.setTotalIdlingTime(history.getTotalIdlingTime());
									data.setAverageSpeed(history.getAverageSpeed());
									data.setTopSpeed(history.getTopSpeed());
									data.setTotalRunningTime(history.getTotalRunningTime());
									setViewData(data);
									mapLatLongPoints(history.getTripPoints());
								} else {
									params.putString("tripID",history.getTripsAggregated().get(position-1).getTripId());
									setViewData(history.getTripsAggregated().get(position-1));
								for(TripPoint list:history.getTripPoints()){
										if(list.getTripId().equalsIgnoreCase(history.getTripsAggregated().get(position-1).getTripId())){
											mapLatLongPointsInternal(list.getPoints());
											break;
										}
									}

								}
								REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}
						});
						binding.spinnerBookingStatusList.setSelection(0);
					} else {
						binding.spinnerBookingStatusList.setVisibility(View.GONE);
						TripsAggregated data = new TripsAggregated();
						data.setFromTs(history.getFromTs());
						data.setTripCreatedTs(history.getTripCreatedTs());
						data.setToTs(history.getToTs());
						data.setTotalDist(history.getTotalDist());
						data.setStartAddress(history.getStartAddress());
						data.setEndAddress(history.getEndAddress());
						data.setTotalHaCount(history.getTotalHaCount());
						data.setTotalHbCount(history.getTotalHbCount());
						data.setOverspeedCount(history.getOverspeedCount());
						data.setTotalIdlingTime(history.getTotalIdlingTime());
						data.setAverageSpeed(history.getAverageSpeed());
						data.setTopSpeed(history.getTopSpeed());
						data.setTotalRunningTime(history.getTotalRunningTime());
						setViewData(data);
						mapLatLongPoints(history.getTripPoints());
					}

					//  setDriverBehaviour(tripDetailModel.getTotalScore());
					binding.txvDest.setOnTouchListener((v, event) -> {

						binding.scroll.requestDisallowInterceptTouchEvent(true);

						return false;
					});
					binding.txvSource.setOnTouchListener((v, event) -> {

						binding.scroll.requestDisallowInterceptTouchEvent(true);

						return false;
					});

				}
			});


		}
	}

	private void setViewData(TripsAggregated history) {
		binding.txvTripStartTime.setText(REUtils.getFormattedTime(history.getTripCreatedTs()));
		binding.txvTripStartMonth.setText(REUtils.getMonth(history.getTripCreatedTs()));
		binding.txvTripStartDate.setText(REUtils.getDate(history.getTripCreatedTs()));
		binding.txvTripDistance.setText(history.getTotalDist() + "");
		binding.txvTripElapsedTimeInHr.setText(REUtils.getTimeInHr(history.getTotalRunningTime()));
		binding.txvTripElapsedTimeInMin.setText(REUtils.getTimeInMin(history.getTotalRunningTime()));
		binding.txvSource.setText(history.getStartAddress());
		binding.txvDest.setText(history.getEndAddress());
		binding.txvSource.setMovementMethod(new ScrollingMovementMethod());
		binding.txvDest.setMovementMethod(new ScrollingMovementMethod());
		binding.txvHarshAccelerationVal.setText(formatHAHBdVal(history.getTotalHaCount()));
		binding.txvHarshBreakingVal.setText(formatHAHBdVal(history.getTotalHbCount()));
		binding.txvOverSpeedingVal.setText(formatOverSpeedVal((history.getOverspeedCount())));
		formatEngineIdlTime(history.getTotalIdlingTime());
		binding.txvAvgSpeedValue.setText(history.getAverageSpeed() + "");
		binding.txvTopSpeedValue.setText(history.getTopSpeed() + "");
	}

//	private void setDriverBehaviour(double totalScore) {
//		Drawable resId;
//		if (totalScore >= 80 && totalScore <= 100) {
//			resId = requireActivity().getResources().getDrawable(R.drawable.ic_rider_behaviour_good);
//		} else if (totalScore >= 40 && totalScore < 80) {
//			resId = requireActivity().getResources().getDrawable(R.drawable.ic_rider_behaviour_average);
//		} else {
//			resId = requireActivity().getResources().getDrawable(R.drawable.ic_rider_behaviour_poor);
//		}
//		binding.imgRiderBehaviour.setBackground(resId);
//	}

	private String formatOverSpeedVal(Integer val) {

		if (val > 0 && val < 10) {
			return "0" + val;
		} else {
			return val + "";
		}
	}

	private String formatHAHBdVal(Integer val) {

		if (val > 0.0 && val < 10.0) {
			return "0" + val;
		} else {
			return val + "";
		}
	}

	private void formatEngineIdlTime(Double seconds) {


		long longVal = seconds.longValue();
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		String engineTimeInHr ;
		String engineTimeInMin;
		String engineTimeInSec;


		if (hours >= 0 && hours < 10) {
			engineTimeInHr = "0" + hours;
		} else {
			engineTimeInHr = hours + "";
		}

		if (mins >= 0 && mins < 10) {
			engineTimeInMin = "0" + mins;
		} else {
			engineTimeInMin = mins + "";
		}
		if (seconds >= 0 && seconds < 10) {
			engineTimeInSec = "0" + secs;

		} else {
			engineTimeInSec = secs + "";
		}
		if(hours>0) {
			binding.txvEngineTimeInHr.setText(engineTimeInHr);
			binding.txvEngineHrUnit.setText(getString(R.string.hr));
			binding.txvEngineTimeInMin.setText(engineTimeInMin);
			binding.txvEngineMinUnit.setText(getString(R.string.min));
		}
		else{
			binding.txvEngineTimeInHr.setText(engineTimeInMin);
			binding.txvEngineTimeInMin.setText(engineTimeInSec);
			binding.txvEngineHrUnit.setText(getString(R.string.min));
			binding.txvEngineMinUnit.setText(getString(R.string.seconds));
		}


	}

	private TripItemModel getTripDetail() {
		TripItemModel tripItemModel = null;
		if (getArguments() != null) {
			tripItemModel = (TripItemModel) getArguments().getSerializable(KEY_TRIP_ID);
		}
		return tripItemModel;
	}

	private void initializeTopView(View view) {
		View headerView = view.findViewById(R.id.header_view);
		TextView headerTitle = headerView.findViewById(R.id.txv_header_title);
		headerTitle.setText(getString(R.string.trip_details));
		ImageView deleteImage = headerView.findViewById(R.id.img_delete);
		deleteImage.setVisibility(View.VISIBLE);
		deleteImage.setOnClickListener(this);
		headerView.findViewById(R.id.img_edit).setVisibility(View.GONE);
		imgBack=headerView.findViewById(R.id.img_back);
		imgBack.setOnClickListener(this);

		supportMapFragment = ((WorkaroundMapFragment) (getChildFragmentManager().findFragmentById(R.id.fragment_map_view)));
		supportMapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
			@Override
			public void onTouch() {
				binding.scroll.requestDisallowInterceptTouchEvent(true);
			}
		});
		if (supportMapFragment != null)
			supportMapFragment.getMapAsync(this);
		showLoading();
		REUtils.countDownTimer(this,0, this::showConnectionLostDialog);
		new Thread(() -> {
			if (getTripDetail() != null)
				REApplication.getInstance().getREConnectedAPI()
						.getTravelHistoryData(TripDetailFragment.this, getTripDetail().getTripId());
		}).start();
	}
	public void showConnectionLostDialog(int type) {
		logConnected("tripDetails","",KEY_MODEL_NAME);
		REUtils.showConnectionLostDialog(getActivity(), new REUtils.OnDialogClickListener() {
			@Override
			public void onOkCLick() {
				showLoading();
				REUtils.countDownTimer(TripDetailFragment.this,0, TripDetailFragment.this::showConnectionLostDialog);
				new Thread(() -> {
					if (getTripDetail() != null)
						REApplication.getInstance().getREConnectedAPI()
								.getTravelHistoryData(TripDetailFragment.this, getTripDetail().getTripId());
				}).start();
			}

			@Override
			public void onCancelCLick() {
				onClick(imgBack);
			}
		});
	}

	private void setMapHeightDynamically() {

		DisplayMetrics displayMetrics = new DisplayMetrics();

		int minimumHeight = 270;
		int finalHeight;
		if (getActivity() != null) {
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int screenHeight = displayMetrics.heightPixels;

			int calculatedHeight = screenHeight - binding.tripDetailView.getMeasuredHeight()
					- binding.speedView.getMeasuredHeight() - binding.riderBehaviourView.getMeasuredHeight() - 600;

			if (calculatedHeight < minimumHeight) {
				finalHeight = minimumHeight;
			} else {
				finalHeight = calculatedHeight;
			}

			if (supportMapFragment.getView() != null) {
				ViewGroup.LayoutParams params = supportMapFragment.getView().getLayoutParams();
				params.height = finalHeight;
				supportMapFragment.getView().setLayoutParams(params);
			}
		}

	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
	}

	private Marker getMarkers2(GoogleMap googleMap) {
		return googleMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f).icon(REUtils.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_end_trip))
				.position(mlatLngs.get(mlatLngs.size() - 1)));
	}


	private Marker getmarker1(GoogleMap googleMap) {
		return googleMap.addMarker(new MarkerOptions()
				.anchor(0.5f, 0.5f).icon(REUtils.bitmapDescriptorFromVector(getActivity(), R.drawable.ic_start_trip))
				.position(mlatLngs.get(0)));
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.img_back:
				if (getActivity() != null)
					getActivity().onBackPressed();
				Bundle paramsScr = new Bundle();
				paramsScr.putString("screenname", "Connected Trip Summary List");
				REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
				break;

			case R.id.img_delete:
				Bundle params = new Bundle();
				params.putString("eventCategory", "Connected Module");
				params.putString("eventAction", "Trip Summary");
				params.putString("eventLabel", "Delete");
				params.putString("modelName", getArguments().getString(KEY_MODEL_NAME));
				REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
				REUtils.showBlurDialog(getActivity(), getResources().getString(R.string.trip_delete)
						, getResources().getString(R.string.are_you_sure_you_want_delete_the_trip), new OnBlurDialogPositiveButtonClickListener() {
							@Override
							public void onPositiveClicked() {
								Bundle params = new Bundle();
								params.putString("eventCategory", "Connected Module");
								params.putString("eventAction", "Confirm Delete");
								params.putString("eventLabel", "Yes");
								params.putString("modelName", getArguments().getString(KEY_MODEL_NAME));
								REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
								showLoading();
								ITripDetailPresenter tripDetailPresenter = new TripDetailPresenter(TripDetailFragment.this);
								new Thread(() -> tripDetailPresenter.deleteTrip(getTripDetail().getUnformattedStartTime(), getTripDetail().getToTime(), getTripDetail().getTripId())).start();
							}

							@Override
							public void onNegetiveClicked() {
								Bundle params = new Bundle();
								params.putString("eventCategory", "Connected Module");
								params.putString("eventAction", "Confirm Delete");
								params.putString("eventLabel", "No");
								params.putString("modelName", getArguments().getString(KEY_MODEL_NAME));
								REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
							}
						});
				break;
		}
	}

	@Override
	public void finishFragment(TripDelete data) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoading();
					if (data.isStatus()) {
						if (getActivity() != null)
							getActivity().getSupportFragmentManager().popBackStack();
						tripDeleteSuccess = true;
						Bundle paramsScr = new Bundle();
						paramsScr.putString("screenname", "Connected Trip Summary List");
						REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
					} else {
						REUtils.showErrorDialog(getActivity(), data.getMessage());
					}
				}
			});
		}
	}

	@Override
	public void onTravelHistorySuccess(TravelHistoryResponseModel responseModel) {
		hideLoading();
		if(REUtils.countDownTimer!=null){
			REUtils.countDownTimer.cancel();
		}
		initializeTripDetailView(responseModel.getData().getData());

	}

	private void mapLatLongPoints(List<TripPoint> dataPoints) {
		mlatLngs = new ArrayList<>();
		for (TripPoint tripPoint : dataPoints) {
			for (DataPoint dataPoint : tripPoint.getPoints()) {
				mlatLngs.add(new LatLng(dataPoint.getLat(), dataPoint.getLng()));
			}
		}
		if (getActivity() != null)
			getActivity().runOnUiThread(this::updateMap);
	}

	private void mapLatLongPointsInternal(List<DataPoint> dataPoints) {
		mlatLngs = new ArrayList<>();
			for (DataPoint dataPoint : dataPoints) {
				mlatLngs.add(new LatLng(dataPoint.getLat(), dataPoint.getLng()));

		}
		if (getActivity() != null)
			getActivity().runOnUiThread(this::updateMap);
	}

	private void updateMap() {
		if (mGoogleMap != null && mlatLngs.size() > 0) {
			mGoogleMap.clear();
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlatLngs.get(0), 15f));
			mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
			mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
			mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
			PolylineOptions polylineOptions = new PolylineOptions();
			polylineOptions.color(Color.BLACK);
			polylineOptions.width(4);
			polylineOptions.addAll(mlatLngs);
			mGoogleMap.addPolyline(polylineOptions);
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			ArrayList<Marker> markers = new ArrayList<>();
			markers.add(getmarker1(mGoogleMap));
			markers.add(getMarkers2(mGoogleMap));

			for (Marker marker : markers) {
				builder.include(marker.getPosition());
			}
			LatLngBounds bounds = builder.build();
			int padding = 100;
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
			mGoogleMap.moveCamera(cu);
		}
		else{
			mGoogleMap.clear();
		}
	}

	public void setTripDeleteCallback(TripDeleteCallback callback) {
		mTripDeleteCallback = callback;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTripDeleteCallback != null && tripDeleteSuccess && getTripDetail() != null) {
			mTripDeleteCallback.onTripDeleteSuccess(getTripDetail().getTripId());
		}

	}
}
