package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVGImageView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.ui.home.rides.AsyncTaskForLoadingSvgImage;
import com.royalenfield.reprime.ui.home.rides.activity.RidesLightWeightWebActivity;
import com.royalenfield.reprime.ui.home.rides.activity.RidesTourActivity;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.*;

/**
 * Adapter for the RecyclerView to bind popular rides
 */
public class PopularRidesAdapter extends RecyclerView.Adapter<PopularRidesAdapter.PopularRidesHolder> {

    private List<PopularRidesResponse> mPopularRidesList;
    private Context mContext;
    private Typeface mTypeFaceBold, mTypeFaceLight;

    public PopularRidesAdapter(Context context, List<PopularRidesResponse> ridesList) {
        this.mPopularRidesList = ridesList;
        this.mContext = context;
    }

    @NotNull
    @Override
    public PopularRidesHolder onCreateViewHolder(@NotNull final ViewGroup parent, final int viewType) {
        mTypeFaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
        mTypeFaceLight = ResourcesCompat.getFont(mContext, R.font.montserrat_light);
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_popular_rides, parent, false);
        return new PopularRidesAdapter.PopularRidesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PopularRidesHolder holder, final int position) {
        // set the data in items
        String mTextNA = mContext.getResources().getString(R.string.text_hyphen_na);
        String mRideTitle = mPopularRidesList.get(position).getTitle();
        String rideStartDate = mPopularRidesList.get(position).getStartDateString();
        String rideEndDate =   mPopularRidesList.get(position).getEndDateString();
        rideStartDate = REUtils.getDateObject(mContext, rideStartDate);
        rideEndDate = REUtils.getDateObject(mContext, rideEndDate);
        String mRideDate = String.format(mContext.getResources().getString
                        (R.string.ride_date_format), rideStartDate,mContext.getResources().getString(R.string.ride_date_hyphen),
                rideEndDate);
        holder.tvRideTitle.setText(mRideTitle != null && !mRideTitle.isEmpty() ? mRideTitle : mTextNA);
        setRideDuration(holder, position);
        holder.tvRideDates.setText(!mRideDate.isEmpty() ? mRideDate : mTextNA);

        REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + mPopularRidesList.get(position).
                getThumbnailImagePath(), holder.ivPopularRides);
        String iconLogoImagePath = mPopularRidesList.get(position).getIconLogoImagePath();
        String extension = null;
        if (iconLogoImagePath != null && !iconLogoImagePath.isEmpty())  {
            extension = iconLogoImagePath.substring(iconLogoImagePath.lastIndexOf("."));
        }
        if (extension != null && !extension.isEmpty() && extension.equalsIgnoreCase(".svg")) {
            holder.mLogoImage.setVisibility(View.VISIBLE);
            holder.mLogoImagePng.setVisibility(View.GONE);
            new AsyncTaskForLoadingSvgImage(REUtils.getMobileappbaseURLs().getAssetsURL() + mPopularRidesList.get(position).
                    getIconLogoImagePath(), holder.mLogoImage).execute();
        } else {
            holder.mLogoImagePng.setVisibility(View.VISIBLE);
            holder.mLogoImage.setVisibility(View.GONE);
            RequestBuilder<Bitmap> requestBuilder = Glide.with(mContext)
                    .asBitmap()
                    .load(REUtils.getMobileappbaseURLs().getAssetsURL() + mPopularRidesList.get(position).
                            getIconLogoImagePath());
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_profile_noimage);
            requestBuilder.apply(options)
                    .into(holder.mLogoImagePng);
        }

    }


    @Override
    public int getItemCount() {
        if (mPopularRidesList != null && mPopularRidesList.size() > 0) {
            return mPopularRidesList.size();
        } else {
            return 0;
        }
    }

    private void setRideDuration(@NonNull final PopularRidesHolder holder, final int position) {
        String separator = "| ";
        String duration = mPopularRidesList.get(position).getDuration();
        if (duration != null && !duration.isEmpty()) {
            String[] durationArray = mPopularRidesList.get(position).getDuration().split(" ");
            String num = durationArray[0] + " ";
            String days = durationArray[1];
            SpannableStringBuilder spannable = new SpannableStringBuilder(separator + num + days);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceLight)), 0, separator.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), separator.length(), separator.length()
                    + num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceLight)), separator.length() + num.length(),
                    separator.length() + num.length() + days.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvRideDuration.setText(spannable);
        } else {
            holder.tvRideDuration.setText(R.string.text_hyphen_na);
        }
    }

    /**
     * Opens Registration webpage or Ride details activity based on the click action of user
     *
     * @param type     : String
     * @param position : int
     */
    private void startPopularDetailsActivity(String type, int position) {
        try {
            Intent mIntent = null;
            if (type.equals(RIDES_DETAILS_WEB_TYPE)) {
                if (mPopularRidesList.get(position).getLightWeightPageUrl() != null && !mPopularRidesList.
                        get(position).getLightWeightPageUrl().isEmpty()) {
                    mIntent = new Intent(mContext, RidesLightWeightWebActivity.class);
                }
            } else if (type.equals(RIDES_DETAILS_NATIVE_TYPE)) {
                mIntent = new Intent(mContext, RidesTourActivity.class);
            }
            if (mIntent != null) {
                mIntent.putExtra(RIDE_TYPE, RIDE_TYPE_POPULAR);
                mIntent.putExtra(RIDES_LIST_POSITION, position);
                mContext.startActivity(mIntent);
                ((REBaseActivity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    class PopularRidesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRideTitle, tvRideDuration;
        private TextView tvRideDates;
        private ImageView ivPopularRides;
        private Button btnRegister;
        private FrameLayout mFrameMapImage;
        private SVGImageView mLogoImage;
        private ImageView mLogoImagePng;

        private PopularRidesHolder(final View itemView) {
            super(itemView);
            tvRideTitle = itemView.findViewById(R.id.tv_ride_title);
            tvRideDates = itemView.findViewById(R.id.tv_ride_date);
            tvRideDuration = itemView.findViewById(R.id.textView_duration);
            ivPopularRides = itemView.findViewById(R.id.ivPopularRides);
            btnRegister = itemView.findViewById(R.id.btn_ride_register);
            mLogoImage = itemView.findViewById(R.id.imgRideIcon);
            mLogoImagePng = itemView.findViewById(R.id.imgRideIcon_png);
            mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            mFrameMapImage.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
            itemView.setOnClickListener(this);
            btnRegister.setOnClickListener(v -> startPopularDetailsActivity(RIDES_DETAILS_WEB_TYPE, getAdapterPosition()));
        }

        @Override
        public void onClick(View view) {
            startPopularDetailsActivity(RIDES_DETAILS_NATIVE_TYPE, getAdapterPosition());
        }
    }

}


