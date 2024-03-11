package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.ui.home.rides.presenter.DealerUpcomingRidesPresenter;
import com.royalenfield.reprime.ui.home.rides.views.DealerUpcomingRidesView;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author BOP1KOR on 3/26/2019.
 * {@link RecyclerView.Adapter} class that provides the list of Dealer Rides to the {@link RecyclerView}.
 */
public class DealerRidesListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    private List<DealerUpcomingRidesResponse> mDealerRidesList;
    private DealerUpcomingRidesPresenter mDealerUpcomingRidesPresenter;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private boolean isLoadMoreNeeded;

    //Listener for individual item click events.
    private OnItemClickListener onItemClickListener;
    private OnLoadMoreClickListener onLoadMoreClickListener;

    public DealerRidesListAdapter(Context mContext, List<DealerUpcomingRidesResponse> mDealerRidesList,
                                  OnItemClickListener clickListener) {
        mDealerUpcomingRidesPresenter = new DealerUpcomingRidesPresenter();
        this.mContext = mContext;
        this.mDealerRidesList = mDealerRidesList;
        setOnItemClickListener(clickListener);
    }

    public DealerRidesListAdapter(Context context,List<DealerUpcomingRidesResponse> dealerRidesList, RecyclerView recyclerView,Boolean isLoadMoreNeeded, OnItemClickListener clickListener,OnLoadMoreClickListener loadMoreClickListener) {
        this.mDealerRidesList = dealerRidesList;
        this.mContext = context;
        this.isLoadMoreNeeded = isLoadMoreNeeded;
        mDealerUpcomingRidesPresenter = new DealerUpcomingRidesPresenter();
        setOnItemClickListener(clickListener);
        setOnLoadMoreClickListener(loadMoreClickListener);
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
//    @NonNull
//    @Override
//    public DealerRidesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dealer_rides_list_item, parent, false);
//        return new DealerRidesItemHolder(view);
//    }


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
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
       // Log.e("AposD",""+position);
       // Log.e("BB","U "+mDealerRidesList.size()+ " pos "+position);
        return mDealerRidesList.size()==position ? VIEW_PROG :VIEW_ITEM ;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mDealerRidesList != null && isLoadMoreNeeded) {
            return mDealerRidesList.size()+1;
        }
        else if(mDealerRidesList!=null && !isLoadMoreNeeded)
        {
            return mDealerRidesList.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                               int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_dealer_rides_list_item, parent, false);

            vh = new DealerRidesItemHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_dealer_rides_load_more, parent, false);

            vh = new LoadMoreViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DealerRidesItemHolder) {
            mDealerUpcomingRidesPresenter.onBindPreviousRideRowViewAtPosition(position, (DealerUpcomingRidesView) holder, mDealerRidesList);

        } else {
            ((LoadMoreViewHolder) holder).tvLoadMore.setText("Load more");
        }
    }

    public class DealerRidesItemHolder extends RecyclerView.ViewHolder implements
            DealerUpcomingRidesView, View.OnClickListener {
        private TextView tvDealerName;
        private TextView tvDealerRideName;
        private TextView tvDealerRideLocationName;
        private ImageView tvDealerPicture;
        private FrameLayout mFrameMapImage;

        DealerRidesItemHolder(View itemView) {
            super(itemView);
            tvDealerName = (TextView)itemView.findViewById(R.id.tv_dealer_name);
            tvDealerRideName =(TextView) itemView.findViewById(R.id.tv_dealer_ride_name);
            tvDealerRideLocationName = (TextView)itemView.findViewById(R.id.tv_dealer_ride_location_address);
            tvDealerPicture = (ImageView) itemView.findViewById(R.id.iv_dealer_thumbnail);
            mFrameMapImage = (FrameLayout) itemView.findViewById(R.id.frame_map_image);
            mFrameMapImage.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
            itemView.setOnClickListener(this);
        }

        @Override
        public void setDealerName(String dealerName) {
            tvDealerName.setText(dealerName);
        }

        @Override
        public void setRideName(String rideName) {
            tvDealerRideName.setText(rideName);
        }

        @Override
        public void setRideRoute(String rideRoute) {
            tvDealerRideLocationName.setText(rideRoute);
        }

        @Override
        public void setRideImage(String url) {
            REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + url, tvDealerPicture);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                Bitmap rideBitmap = ((BitmapDrawable) tvDealerPicture.getDrawable()).getBitmap();
                onItemClickListener.onItemClick(view, getAdapterPosition(), getItemId(), rideBitmap);
            }
        }
    }

    /**
     * Setter for {@link DealerRidesListAdapter.OnItemClickListener} instance.
     *
     * @param onItemClickListener {@link DealerRidesListAdapter.OnItemClickListener}
     */
    private void setOnItemClickListener(DealerRidesListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void setOnLoadMoreClickListener(DealerRidesListAdapter.OnLoadMoreClickListener onLoadMoreClickListener) {
        this.onLoadMoreClickListener = onLoadMoreClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, long id, Bitmap bitmap);
    }

    public interface OnLoadMoreClickListener{
        void onLoadMoreClick(View view,int position);
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
                notifyItemInserted(mDealerRidesList.size()+1);
                notifyDataSetChanged();
            }
        }
    }
}
