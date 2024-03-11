package com.royalenfield.reprime.ui.riderprofile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.ui.home.rides.activity.InRideActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.RideDetailsActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.UpcomingRideActivity;
import com.royalenfield.reprime.ui.riderprofile.presenter.ProfileRidesRowPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.ProfileRidesRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.*;

/**
 * RecyclerView Adapter for Rides
 */
public class ProfileRidesAdapter extends RecyclerView.Adapter<ProfileRidesAdapter.ProfileRidesHolder> {

    private Context mContext;
    private String mType;
    private Activity mActivity;
    private ProfileRidesRowPresenter mProfileRidesRowPresenter;


    public ProfileRidesAdapter(Activity activity, Context context, ProfileRidesRowPresenter profileRidesRowPresenter,
                               String type) {
        mActivity = activity;
        mContext = context;
        this.mType = type;
        mProfileRidesRowPresenter = profileRidesRowPresenter;
    }


    @NonNull
    @Override
    public ProfileRidesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_upcomingrides, viewGroup, false);
        return new ProfileRidesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRidesHolder viewHolder, int position) {
        //Calling Row presenter here
        mProfileRidesRowPresenter.onBindProfileRideRowViewAtPosition(position, viewHolder, mType);
    }

    @Override
    public int getItemCount() {
        List<ProfileRidesResponse> mProfileRidesResponse = null;
        switch (mType) {
            case PASTRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPastRideResponse();
                break;
            case UPCOMIMGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
                break;
            case PENDINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPendingRideResponse();
                break;
            case REJECTEDRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getRejectedRideResponse();
                break;
            case ONGOINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getOngoingRideResponse();
                break;
        }
        return mProfileRidesRowPresenter.getRidesRowCount(mProfileRidesResponse != null
                && mProfileRidesResponse.size() > 0 ? mProfileRidesResponse.size() : 0);
    }

    /**
     * A Simple ViewHolder for the UpcomingRide RecyclerView
     */
    public class ProfileRidesHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ProfileRidesRowView {

        TextView mFriendsInvitedText, mRideDetail, mRideDate;
        ImageView mRideImage;

        private ProfileRidesHolder(final View itemView) {
            super(itemView);
            mFriendsInvitedText = itemView.findViewById(R.id.textView_friendsinvited);
            mRideDetail = itemView.findViewById(R.id.textView_rideDetail);
            mRideDate = itemView.findViewById(R.id.textView_rideDate);
            mRideImage = itemView.findViewById(R.id.imageView_ride);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                Intent mIntent = null;
                switch (mType) {
                    case PENDINGRIDE_TYPE:
                        mIntent = new Intent(mContext, RideDetailsActivity.class);
                        mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, PENDINGRIDE_TYPE);
                        break;
                    case UPCOMIMGRIDE_TYPE:
                        mIntent = new Intent(mContext, UpcomingRideActivity.class);
                        mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, UPCOMIMGRIDE_TYPE);
                        break;
                    case PASTRIDE_TYPE:
                        mIntent = new Intent(mContext, RideDetailsActivity.class);
                        mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, PASTRIDE_TYPE);
                  /*  mIntent.putExtra(RIDE_IMAGE, String.valueOf(REUserModelStore.getInstance().getPastRideResponse().
                            get(getAdapterPosition()).getRideImages().get(0).getSrcPath()));
                    mIntent.putExtra(RIDE_ID, REUserModelStore.getInstance().getPastRideResponse().get(getAdapterPosition()).getUserId());
                    mIntent.putExtra(RIDE_TYPE_REQ, "user-ride");*/
                        break;
                    case REJECTEDRIDE_TYPE:
                        mIntent = new Intent(mContext, RideDetailsActivity.class);
                        mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, REJECTEDRIDE_TYPE);
                        break;
                    case ONGOINGRIDE_TYPE:
                        Intent closeOngoingRide = new Intent(REConstants.RIDE_ONGOING_CLOSE);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(closeOngoingRide);
                        mIntent = new Intent(mContext, InRideActivity.class);
                        mIntent.putExtra(POSITION, getAdapterPosition());
                        break;
                    default:
                        break;
                }
                assert mIntent != null;
                mIntent.putExtra(RIDES_LIST_POSITION, getAdapterPosition());
                mActivity.startActivity(mIntent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            } catch (IndexOutOfBoundsException e) {
                RELog.e(e);
            } catch (Exception e) {
                RELog.e(e);
            }

        }

        @Override
        public void setFriendsInvited(String friendsInvited) {
            mFriendsInvitedText.setText(friendsInvited);
        }

        @Override
        public void setRideDetail(String rideDetail) {
            mRideDetail.setText(rideDetail);
        }

        @Override
        public void setRideDate(String rideStartDate, String rideEndDate) {
            String strStartDate = REUtils.getDateObject(mActivity, rideStartDate);
            String strEndDate = REUtils.getDateObject(mActivity, rideEndDate);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(strStartDate);
            stringBuffer.append(" - ");
            stringBuffer.append(strEndDate);
            mRideDate.setText(stringBuffer);
        }

        @Override
        public void setRideImage(String imagePath) {
            REUtils.loadImageWithGlide(mContext, imagePath, mRideImage);
        }
    }
}
