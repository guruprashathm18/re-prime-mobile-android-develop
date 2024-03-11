package com.royalenfield.reprime.ui.riderprofile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVGImageView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.AsyncTaskForLoadingSvgImage;
import com.royalenfield.reprime.ui.home.rides.activity.RidesTourActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.RideDetailsActivity;
import com.royalenfield.reprime.ui.riderprofile.presenter.CustomRidesRowPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.CustomRidesRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;

/**
 * Adapter for binding marquee rides recyclerView
 */
public class CustomRidesAdapter extends RecyclerView.Adapter<CustomRidesAdapter.PreviousRidesHolder> {

    private final Context mContext;
    private Activity mActivity;
    private CustomRidesRowPresenter mPreviousRideRowPresenter;
    private String mType;

    public CustomRidesAdapter(Activity activity, Context context, CustomRidesRowPresenter previousRidesRowPresente,
                              String type) {
        this.mActivity = activity;
        this.mContext = context;
        this.mType = type;
        mPreviousRideRowPresenter = previousRidesRowPresente;
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @NonNull
    @Override
    public PreviousRidesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customrides, parent, false);
        return new PreviousRidesHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PreviousRidesHolder holder, int position) {
        // setting width and height of cardView based on device density
        mPreviousRideRowPresenter.onBindPreviousRideRowViewAtPosition(position, holder, mType);
        if (mType.equals(REConstants.BOOKMARKS_TYPE) || mType.equals(REConstants.PASTRIDE_TYPE)) {
            // setting cardview width and height programatically
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            holder.mCardview.getLayoutParams().width = (int) (displaymetrics.widthPixels / 2.25);
        }

    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int count = 0;
        switch (mType) {
            case REConstants.PASTRIDE_TYPE:
                count = 4;
                break;
            case REConstants.BOOKMARKS_TYPE:
                count = 4;
            case REConstants.MARQUEE_RIDE:
                if (RERidesModelStore.getInstance().getMarqueeRidesResponse() != null) {
                    count = RERidesModelStore.getInstance().getMarqueeRidesResponse().size();
                }
                break;
            default:
                break;
        }
        return count;
    }

    /**
     * A Simple ViewHolder for the PreviousRides RecyclerView
     */
    public class PreviousRidesHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CustomRidesRowView {

        private CardView mCardview;
        private TextView mRideTitle, mRideDate, mRideOwner;
        private ImageView mThumbNailImage, mRideOwnerImagePng;
        private SVGImageView mRideOwnerImage;
        private FrameLayout mFrameMapImage;

        private PreviousRidesHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCardview = itemView.findViewById(R.id.cardview);
            mRideTitle = itemView.findViewById(R.id.textView_ride);
            mRideDate = itemView.findViewById(R.id.textView_ridedate);
            mThumbNailImage = itemView.findViewById(R.id.imageView_ride);
            mRideOwnerImage = itemView.findViewById(R.id.imageView_owner);
            mRideOwnerImagePng = itemView.findViewById(R.id.imageView_owner_png);
            mRideOwner = itemView.findViewById(R.id.textView_owner);
            mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            mFrameMapImage.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
        }

        @Override
        public void onClick(View v) {
            try {
                switch (mType) {
                    case REConstants.PASTRIDE_TYPE:
                        Intent intent = new Intent(mContext, RideDetailsActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, 0);
                        break;
                    case REConstants.MARQUEE_RIDE:
                        Intent mIntent = new Intent(mContext, RidesTourActivity.class);
                        mIntent.putExtra(RIDE_TYPE, RIDE_TYPE_MARQUEE);
                        mIntent.putExtra(RIDES_LIST_POSITION, getAdapterPosition());
                        mContext.startActivity(mIntent);
                        ((REBaseActivity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                    default:
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                RELog.e(e);
            } catch (Exception e) {
                RELog.e(e);
            }
        }

        @Override
        public void setRideDetail(String rideDetail) {
            mRideTitle.setText(rideDetail);
        }

        @Override
        public void setRideDate(SpannableStringBuilder rideDate) {
            mRideDate.setText(rideDate);
        }

        @Override
        public void setRideImage(String url) {
            REUtils.loadImageWithGlide(mContext, url, mThumbNailImage);
        }

        @Override
        public void setRideLogoImage(String url) {
            String extension = url.substring(url.lastIndexOf("."));
            if (extension.equalsIgnoreCase(".svg")) {
                mRideOwnerImage.setVisibility(View.VISIBLE);
                mRideOwnerImagePng.setVisibility(View.GONE);
                new AsyncTaskForLoadingSvgImage(url, mRideOwnerImage).execute();
            } else {
                mRideOwnerImagePng.setVisibility(View.VISIBLE);
                mRideOwnerImage.setVisibility(View.GONE);
                RequestBuilder<Bitmap> requestBuilder = Glide.with(mContext)
                        .asBitmap()
                        .load(url);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_image_loading);
                requestBuilder
                        .apply(options)
                        .into(mRideOwnerImagePng);
            }
        }

        @Override
        public void setRideOwner(String owner) {
            if (owner != null) {
                mRideOwner.setVisibility(View.VISIBLE);
                mRideOwner.setText(owner);
            } else {
                mRideOwner.setVisibility(View.GONE);
            }
        }
    }
}