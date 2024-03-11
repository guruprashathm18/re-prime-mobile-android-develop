package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.home.rides.presenter.CheckInRowRowPresenter;
import com.royalenfield.reprime.ui.home.rides.views.CheckInRowView;
import com.royalenfield.reprime.utils.REUtils;

/**
 * Adapter creates views for the check-in items and binds the data to the views.
 */

public class CheckInsAdapter extends RecyclerView.Adapter<CheckInsAdapter.CheckInRowListHolder> {

    private Context mContext;
    private CheckInRowRowPresenter mCheckInRowPresenter;
    private CheckInResponse mCheckInResponse;

    public CheckInsAdapter(Context context, CheckInRowRowPresenter checkInRowPresenter, CheckInResponse checkInResponse) {
        this.mContext = context;
        this.mCheckInRowPresenter = checkInRowPresenter;
        this.mCheckInResponse = checkInResponse;
    }

    @NonNull
    @Override
    public CheckInRowListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_in_list, parent, false);
        return new CheckInRowListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckInRowListHolder holder, int position) {
        mCheckInRowPresenter.onBindCheckInViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return mCheckInRowPresenter.getCheckInCount(mCheckInResponse);
    }

    /**
     * A Simple ViewHolder for the Check-in RecyclerView
     */

    class CheckInRowListHolder extends RecyclerView.ViewHolder implements CheckInRowView {

        private TextView tvCheckInType, tvCheckInPlaceName, tvCheckInLocation;
        private ImageView ivPlaceImage;
        private FrameLayout mFramePlaceImage;

        CheckInRowListHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaceImage = itemView.findViewById(R.id.iv_check_in_place);
            tvCheckInLocation = itemView.findViewById(R.id.tv_check_in_location);
            tvCheckInPlaceName = itemView.findViewById(R.id.tv_check_in_place_name);
            tvCheckInType = itemView.findViewById(R.id.tv_check_in_type);
            mFramePlaceImage = itemView.findViewById(R.id.frame_place_image);
            mFramePlaceImage.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
        }

        @Override
        public void setCheckInType(String checkInType) {
            tvCheckInType.setText(checkInType);
        }

        @Override
        public void setCheckInLocation(String checkInLocation) {
            tvCheckInLocation.setText(checkInLocation);
        }

        @Override
        public void setCheckInPlaceName(String checkInPlaceName) {
            tvCheckInPlaceName.setText(checkInPlaceName);
        }

        @Override
        public void setCheckInPlaceImage(String url) {
            REUtils.loadImageWithGlide(mContext, url, ivPlaceImage);
        }
    }

}
