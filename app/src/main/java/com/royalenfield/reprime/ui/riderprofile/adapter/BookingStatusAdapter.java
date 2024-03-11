package com.royalenfield.reprime.ui.riderprofile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.BookingModel;

import java.util.List;

/**
 * RecyclerView Adapter for Rides
 */
public class BookingStatusAdapter extends RecyclerView.Adapter<BookingStatusAdapter.ProfileRidesHolder> {

    private Context mContext;
    private String mType;
    private Activity mActivity;
    private List<BookingModel> list;

    public BookingStatusAdapter(Activity activity, Context context,
                                List<BookingModel> listData) {
        mActivity = activity;
        mContext = context;
        this.list = listData;
    }


    @NonNull
    @Override
    public ProfileRidesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_booking_status, viewGroup, false);
        return new ProfileRidesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRidesHolder viewHolder, int position) {
        //Calling Row presenter here
        viewHolder.mFriendsInvitedText.setText("Bike Name:"+list.get(position).getName());
        viewHolder.mRideDetail.setText("TransctionId:"+list.get(position).getTransId());
        viewHolder.mRideDate.setText("Transaction Status:"+list.get(position).getBookingStatus());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,list.get(position).getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * A Simple ViewHolder for the UpcomingRide RecyclerView
     */
    public class ProfileRidesHolder extends RecyclerView.ViewHolder {

        TextView mFriendsInvitedText, mRideDetail, mRideDate;


        private ProfileRidesHolder(final View itemView) {
            super(itemView);
            mFriendsInvitedText = itemView.findViewById(R.id.textView_friendsinvited);
            mRideDetail = itemView.findViewById(R.id.textView_rideDetail);
            mRideDate = itemView.findViewById(R.id.textView_rideDate);

        }


    }
}
