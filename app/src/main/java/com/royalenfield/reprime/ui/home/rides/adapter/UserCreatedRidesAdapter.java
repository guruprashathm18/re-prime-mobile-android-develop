package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.ui.home.rides.activity.DealerRideDetailsActivity;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;


/**
 * Adapter creates views for the user created rides items and binds the data to the views.
 */
public class UserCreatedRidesAdapter extends RecyclerView.Adapter {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    private List<UserUpcomingRidesResponse> mUserUpcomingRidesResponse;
    private int clickedPosition=-1;
    private REBaseFragment mFragment;
    private Boolean isLoadMoreNeeded;

    private OnLoadMoreClickListener onLoadMoreClickListener;

    public UserCreatedRidesAdapter(REBaseFragment fragment, Context context,Boolean isLoadMoreNeeded, List<UserUpcomingRidesResponse> userUpcomingRidesResponse,OnLoadMoreClickListener loadMoreClickListener) {
        this.mContext = context;
        this.mFragment=fragment;
        this.isLoadMoreNeeded=isLoadMoreNeeded;
        this.mUserUpcomingRidesResponse = userUpcomingRidesResponse;
        setOnLoadMoreClickListener(loadMoreClickListener);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
     //   Log.e("AA","U "+mUserUpcomingRidesResponse.size()+ " pos "+position);
        if (mUserUpcomingRidesResponse.size()==position)
        {
            return VIEW_PROG;
        }
        else
        {
            return VIEW_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        //Log.e("test", " user upcoming rides size B: " + mUserUpcomingRidesResponse.size());
        if (mUserUpcomingRidesResponse != null && !isLoadMoreNeeded) {
            return mUserUpcomingRidesResponse.size();
        }
        else if(mUserUpcomingRidesResponse != null && isLoadMoreNeeded)
        {
            return mUserUpcomingRidesResponse.size()+1;
        }
        return 0;

    }
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                               int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_user_created_rides, parent, false);

            vh = new UserCreatedRidesViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_user_created_load_more, parent, false);

            vh = new LoadMoreViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof UserCreatedRidesViewHolder)
        {
//            if (!mUserUpcomingRidesResponse.get(position).getCreatedBy().equals(REUtils.getUserId())) {
//                ((UserCreatedRidesViewHolder) holder).rideName.setVisibility(View.VISIBLE);
//                ((UserCreatedRidesViewHolder) holder).route.setVisibility(View.VISIBLE);
//                ((UserCreatedRidesViewHolder) holder).rideImage.setVisibility(View.VISIBLE);
//                ((UserCreatedRidesViewHolder) holder).rideName.setText(mUserUpcomingRidesResponse.get(position).getRideName());
//                setRideRoute((UserCreatedRidesViewHolder) holder, position);
//                loadRiderImage((UserCreatedRidesViewHolder) holder, position);
//            } else {
//                ((UserCreatedRidesViewHolder) holder).rideName.setVisibility(View.GONE);
//                ((UserCreatedRidesViewHolder) holder).route.setVisibility(View.GONE);
//                ((UserCreatedRidesViewHolder) holder).rideImage.setVisibility(View.GONE);
//            }
            ((UserCreatedRidesViewHolder) holder).rideName.setVisibility(View.VISIBLE);
            ((UserCreatedRidesViewHolder) holder).route.setVisibility(View.VISIBLE);
            ((UserCreatedRidesViewHolder) holder).rideImage.setVisibility(View.VISIBLE);
            ((UserCreatedRidesViewHolder) holder).rideName.setText(mUserUpcomingRidesResponse.get(position).getRideName());
            setRideRoute((UserCreatedRidesViewHolder) holder, position);
            loadRiderImage((UserCreatedRidesViewHolder) holder, position);
        }
        else
        {
            ((LoadMoreViewHolder) holder).tvLoadMore.setText("Load more");
        }

    }

    private void setRideRoute(@NonNull UserCreatedRidesViewHolder holder, int position) {
        String mStartPoint = mUserUpcomingRidesResponse.get(position).getStartPoint();
        String mEndPoint = mUserUpcomingRidesResponse.get(position).getEndPoint();
        mStartPoint = REUtils.splitPlaceName(mStartPoint);
        mEndPoint = REUtils.splitPlaceName(mEndPoint);
        holder.route.setText(mStartPoint + " > " + mEndPoint);
    }

    private void loadRiderImage(@NonNull UserCreatedRidesViewHolder holder, int position) {
        String rideImageUrl = mUserUpcomingRidesResponse.get(position).getRideImages().get(0).
                get(REFirestoreConstants.RIDE_IMAGES_SRC_PATH);
        REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + rideImageUrl, holder.rideImage);
    }

    private void setOnLoadMoreClickListener(UserCreatedRidesAdapter.OnLoadMoreClickListener onLoadMoreClickListener) {
        this.onLoadMoreClickListener = onLoadMoreClickListener;
    }

    public interface OnLoadMoreClickListener{
        void onLoadMoreClick(View view,int position);
    }

    class UserCreatedRidesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RECircularImageView rideImage;
        TextView rideName;
        TextView route;

        UserCreatedRidesViewHolder(View itemView) {
            super(itemView);
            rideImage = itemView.findViewById(R.id.rider_image);
            rideName = itemView.findViewById(R.id.ride_name);
            route = itemView.findViewById(R.id.route);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickedPosition = getAdapterPosition();
            //Log.e("Clickedadap ",""+getAdapterPosition());
            //Log.e("Clicked ",""+clickedPosition);
       if(REUtils.isUserLoggedIn())
         gotoDealerRidesActivity();
       else {
           mFragment.startActivityForResult(new Intent(mFragment.getContext(), UserOnboardingActivity.class), LoginActivity.CODE_CREATE_RIDE_ACTIVITY);
           mFragment.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
       }
        }
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvLoadMore;

        public LoadMoreViewHolder(View v) {
            super(v);
            tvLoadMore = (TextView) v.findViewById(R.id.tv_load_more);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onLoadMoreClickListener != null) {
                onLoadMoreClickListener.onLoadMoreClick(v,getAdapterPosition());
                notifyItemInserted(mUserUpcomingRidesResponse.size()+1);
                notifyDataSetChanged();
            }
        }
    }
    public void gotoDealerRidesActivity(){
        try {
            if(clickedPosition>=0) {
                Intent intent = new Intent(mContext, DealerRideDetailsActivity.class);
                intent.putExtra(REConstants.DEALER_RIDES_TYPE, REConstants.TYPE_USER_CREATED_RIDE_DETAILS);
                intent.putExtra(REConstants.POSITION, clickedPosition);
                mContext.startActivity(intent);
                ((REBaseActivity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

}
