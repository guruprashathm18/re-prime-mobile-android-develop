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
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.RidersJoined;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;
import com.royalenfield.reprime.ui.home.service.rsa.listner.IRETelephoneManager;
import com.royalenfield.reprime.ui.riderprofile.presenter.RiderInvitedPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_ONGOING;
import static com.royalenfield.reprime.utils.REConstants.UPCOMIMGRIDE_TYPE;

public class RiderInvitedAdapter extends RecyclerView.Adapter<RiderInvitedAdapter.KeyPlacesViewHolderI> {

    private Activity mActivity;
    private Context mContext;
    private RiderInvitedPresenter mRiderInvitedPresenter;
    private int mListItemPosition;
    private String mRideType;
    private List<ProfileRidesResponse> mOngoingRideResponse;
    private List<ProfileRidesResponse> mUpcomingRideResponse;
    private String mRiderNo;
    private static final int RIDER_INVITED_CALL_PERMISSIONS_REQUESTS = 4;


    public RiderInvitedAdapter(Activity activity, Context context, RiderInvitedPresenter riderInvitedPresenter,
                               int mPosition, String rideType) {
        this.mContext = context;
        this.mActivity = activity;
        this.mRiderInvitedPresenter = riderInvitedPresenter;
        this.mListItemPosition = mPosition;
        this.mRideType = rideType;
        mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
        mUpcomingRideResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
    }

    @NonNull
    @Override
    public KeyPlacesViewHolderI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_invited_list_row, parent, false);
        return new KeyPlacesViewHolderI(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyPlacesViewHolderI holder, int position) {
        //Calling Row presenter here
        try {
            if (mRideType.equals(RIDE_TYPE_ONGOING)) {
                // This condition is kept for not binding the userDetails is RidersJoined section
                if (mOngoingRideResponse.get(mListItemPosition).getRidersJoined() != null &&
                        mOngoingRideResponse.get(mListItemPosition).getRidersJoined().get(position).getUser() != null &&
                        !Objects.equals(mOngoingRideResponse.get(mListItemPosition).getRidersJoined().get(position).getUser().
                                get(REFirestoreConstants.RIDERS_JOINED_USER_ID), REUtils.getUserId())) {
                    mRiderInvitedPresenter.onRiderInvitedRowViewAtPosition(position, holder, mListItemPosition, mRideType);
                } else {
                    //Setting the item height to zero. As we are not binding the loggedIn userInfo
                    holder.itemView.getLayoutParams().height = 0;
                }
            } else if (mRideType.equals(UPCOMIMGRIDE_TYPE)) {
                if (mUpcomingRideResponse.get(mListItemPosition).getRidersJoined() != null &&
                        mUpcomingRideResponse.get(mListItemPosition).getRidersJoined().get(position).getUser() != null &&
                        !Objects.equals(mUpcomingRideResponse.get(mListItemPosition).getRidersJoined().get(position).getUser().
                                get(REFirestoreConstants.RIDERS_JOINED_USER_ID), REUtils.getUserId())) {
                    mRiderInvitedPresenter.onRiderInvitedRowViewAtPosition(position, holder, mListItemPosition, mRideType);
                }else {
                    //Setting the item height to zero. As we are not binding the loggedIn userInfo
                    holder.itemView.getLayoutParams().height = 0;
                }
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public int getItemCount() {
        ArrayList<RidersJoined> mRidersJoined;
        try {
            if (mOngoingRideResponse != null && mOngoingRideResponse.size() > 0 && mRideType.
                    equals(REConstants.RIDE_TYPE_ONGOING)) {
                mRidersJoined = mOngoingRideResponse.get(mListItemPosition).getRidersJoined();
                return mRiderInvitedPresenter.getRideTourRowsCount(mRidersJoined != null &&
                        mRidersJoined.size() > 0 ? mRidersJoined.size() : 0);
            } else if (mUpcomingRideResponse != null && mUpcomingRideResponse.size() > 0 &&
                    mRideType.equals(REConstants.UPCOMIMGRIDE_TYPE)) {
                mRidersJoined = mUpcomingRideResponse.get(mListItemPosition).getRidersJoined();
                return mRiderInvitedPresenter.getRideTourRowsCount(mRidersJoined != null && mRidersJoined.size() > 0 ?
                        mRidersJoined.size() : 0);
            }
            return 0;
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
        return 0;
    }


    class KeyPlacesViewHolderI extends RecyclerView.ViewHolder implements View.OnClickListener, IRidesTourRowView,
            IRETelephoneManager {

        RECircularImageView placeImage;
        TextView textKeyPlace;
        ImageView imageCall;

        KeyPlacesViewHolderI(View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_image);
            textKeyPlace = itemView.findViewById(R.id.text_key_place);
            imageCall = itemView.findViewById(R.id.imageView_call);
            imageCall.setOnClickListener(this);
            imageCall.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageView_call) {
                if (mRideType.equals(RIDE_TYPE_ONGOING) && mOngoingRideResponse != null && mOngoingRideResponse.size() > 0) {
                    mRiderNo = String.valueOf(mOngoingRideResponse.get(mListItemPosition).
                            getRidersJoined().get(getAdapterPosition()).getUser().get(REFirestoreConstants.USER_INFO_PHONE_NO));
                    callRider();
                } else if (mRideType.equals(UPCOMIMGRIDE_TYPE) && mUpcomingRideResponse != null && mUpcomingRideResponse.size() > 0) {
                    mRiderNo = String.valueOf(mUpcomingRideResponse.get(mListItemPosition).
                            getRidersJoined().get(getAdapterPosition()).getUser().get(REFirestoreConstants.USER_INFO_PHONE_NO));
                    callRider();
                }
            }
        }

        /**
         * Checks the permission and calls to rider
         */
        private void callRider() {
            if (!mRiderNo.equals("")) {
                ((REBaseActivity) mContext).checkAndRequestCallPermissions(mContext, mActivity, mRiderNo,
                        RIDER_INVITED_CALL_PERMISSIONS_REQUESTS, this);
            } else {
                Toast.makeText(mContext, "Contact number unavailable", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void setKeyPlaceImage(String placeImageUrl) {
            REUtils.loadImageWithGlide(mContext, placeImageUrl, placeImage);
        }

        @Override
        public void setKeyPlaceName(String placeName) {
            textKeyPlace.setText(placeName);
            imageCall.setVisibility(View.VISIBLE);
        }

        @Override
        public void setItineraryDate(String date) {

        }

        @Override
        public void setItineraryDesciption(String desciption) {

        }

        @Override
        public void setRideGalleryImages(String srcPath) {

        }

        @Override
        public void setEventHighlightsRecyclerView(ArrayList<Map<String, Object>> eventHighlightsList) {

        }

        @Override
        public void simError(String messgae) {
            REUtils.showErrorDialog(mContext, messgae);
        }
    }
}


