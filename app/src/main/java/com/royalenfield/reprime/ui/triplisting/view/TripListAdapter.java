package com.royalenfield.reprime.ui.triplisting.view;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.triplisting.response.TripData;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<TripItemModel> mTripdata;
    private final TripItemClickListener mItemClickListener;
private boolean isChecked=false;
private TripListingFragment fragment;
    public TripListAdapter(TripListingFragment fragment,List<TripItemModel> tripData, TripItemClickListener itemClickListener) {
        mTripdata = tripData;
        mItemClickListener = itemClickListener;
		this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trip_item_view, parent, false);
        return new TripItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TripItemViewHolder)holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mTripdata.size();
    }

    public class TripItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txv_trip_start_time)
        TextView mTripStartTime;

        @BindView(R.id.txv_trip_start_date)
        TextView mTripStartDate;

        @BindView(R.id.txv_trip_start_month)
        TextView mTripStartMonth;

        @BindView(R.id.txv_trip_distance)
        TextView mTripDistance;

        @BindView(R.id.txv_trip_elapsed_time_in_hr)
        TextView mTripElapsedTimeInHr;

        @BindView(R.id.txv_trip_elapsed_time_in_min)
        TextView mTripElapsedTimeInMin;

        @BindView(R.id.txv_source)
        TextView mSource;

        @BindView(R.id.txv_dest)
        TextView mTextDest;

		@BindView(R.id.check_box)
		CheckBox mCheck;

		@BindView(R.id.rel_item)
				RelativeLayout relativeLayout;

		@BindView(R.id.merged_badge)
				TextView mergedBadge;

		@BindView(R.id.rel_merged)
		LinearLayout relMerged;


		TripItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindItem(int position) {

            TripItemModel itemModel = mTripdata.get(position);
            mTripStartTime.setText(REUtils.getFormattedTime(itemModel.getTripCreatedTime()));
            mTripStartMonth.setText(REUtils.getMonth(itemModel.getTripCreatedTime()));
            mTripStartDate.setText(REUtils.getDate(itemModel.getTripCreatedTime()));
            mTripDistance.setText(itemModel.getDistance());
            mTripElapsedTimeInHr.setText(REUtils.getTimeInHr(itemModel.getTotalRunningTime()));
            mTripElapsedTimeInMin.setText(REUtils.getTimeInMin(itemModel.getTotalRunningTime()));
            mSource.setText(itemModel.getSource());
            mTextDest.setText(itemModel.getDestination());
				if(mTripdata.get(position).getTripType().equalsIgnoreCase("Individual")) {
					mCheck.setVisibility(View.VISIBLE);
					mergedBadge.setVisibility(View.GONE);
					relMerged.setVisibility(View.GONE);
				}
				else{
				mCheck.setVisibility(View.GONE);
					mergedBadge.setVisibility(View.VISIBLE);
					relMerged.setVisibility(View.VISIBLE);
			}

			if(mTripdata.get(position).isChecked())
				mCheck.setChecked(true);
			else
				mCheck.setChecked(false);

relativeLayout.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {


		mItemClickListener.onTripItemClicked(getAdapterPosition());
		relativeLayout.setEnabled(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				relativeLayout.setEnabled(true);
			}
		},1000);
	}
});
			mCheck.setOnClickListener(v -> {
				List<String> tripitme=	getSelectedTrips();
			if (mCheck.isChecked()) {
						if(tripitme.size()<10) {
							mCheck.setChecked(true);
							mTripdata.get(position).setChecked(true);
							sendGAData(mTripdata.get(position).getTripId(),true);
						}
						else{
							REUtils.showErrorDialog(itemView.getContext(),"Maximum 10 trips can be selected");
						//	Toast.makeText(itemView.getContext(), "you have reached 10 trip merge limit",Toast.LENGTH_SHORT).show();
							mCheck.setChecked(false);
							mTripdata.get(position).setChecked(false);
						}
					} else {
				       sendGAData(mTripdata.get(position).getTripId(),false);
						mCheck.setChecked(false);
						mTripdata.get(position).setChecked(false);
					}
				List<String> tripitme1=	getSelectedTrips();
				if(tripitme1!=null&&tripitme1.size()>1){
					fragment.setMergeVisibility(true);
				}
				else{
					fragment.setMergeVisibility(false);
				}

			});
        }


		@Override
		public void onClick(View v) {
			mItemClickListener.onTripItemClicked(getAdapterPosition());
		}
	}

	private void sendGAData(String tripid,boolean b) {
		Bundle params = new Bundle();
		params.putString("eventCategory", "Connected Module");
		params.putString("eventAction", "Trip Summary");
		params.putString("modelName",fragment.getModelName());
		params.putString("tripID",tripid);
		if(b)
			params.putString("eventLabel", "Checkbox select");
		else
		params.putString("eventLabel", "Checkbox deselect");

		REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM,params);
	}
//	public void setChecked(boolean isCheck){
//		isChecked=isCheck;
//		if(!isCheck)
//			for(TripItemModel trips:mTripdata)
//				trips.setChecked(false);
//		notifyDataSetChanged();
//	}

	public ArrayList<String> getSelectedTrips(){
		ArrayList<String> chedkedTrip=new ArrayList<>();
		for(TripItemModel trips:mTripdata){
			if(trips.isChecked()){
			chedkedTrip.add(trips.getTripId());
			}
		}
		return chedkedTrip;
	}
}
