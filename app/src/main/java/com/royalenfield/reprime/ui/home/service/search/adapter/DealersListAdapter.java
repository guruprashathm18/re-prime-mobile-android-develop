package com.royalenfield.reprime.ui.home.service.search.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.home.service.search.presenter.SearchRowViewPresenter;
import com.royalenfield.reprime.ui.home.service.search.view.DealersListRowView;
import com.royalenfield.reprime.ui.home.service.search.view.ItemClickListener;
import com.royalenfield.reprime.ui.home.service.search.view.SearchFilterEmptyListListener;
import com.royalenfield.reprime.utils.REConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DealersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<DealerMasterResponse> dealersList;
    private List<DealerMasterResponse> filteredList;
    private boolean filterFlag = false;
    private String mType;
    private List<POIResults> searchResultList;
    private SearchRowViewPresenter mSearchRowViewPresenter;
    private ItemClickListener mItemClickListener;
    private CheckInListener checkInListener;
    private WeakReference<ItemClickListener> listenerRef;
    private SearchFilterEmptyListListener mSearchFilterEmptyListListener;

    public DealersListAdapter(List<DealerMasterResponse> dealersList, ItemClickListener itemClickListener,
                              SearchFilterEmptyListListener searchFilterEmptyListListener,
                              SearchRowViewPresenter searchRowViewPresenter, String mType) {
        this.dealersList = dealersList;
        this.filteredList = dealersList;
        this.mItemClickListener = itemClickListener;
        this.mSearchRowViewPresenter = searchRowViewPresenter;
        this.mSearchFilterEmptyListListener = searchFilterEmptyListListener;
        this.mType = mType;
    }

    public DealersListAdapter(List<POIResults> searchResults, CheckInListener itemClickListener, String mType) {
        this.searchResultList = searchResults;
        this.checkInListener = itemClickListener;
        this.mType = mType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dealers_list, viewGroup, false);
            return new MyViewHolder(v, mItemClickListener);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_check_in, viewGroup, false);
            return new CheckInViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            if (this.filteredList != null && this.filteredList.size() > 0) {
                ((MyViewHolder) holder).dealresName.setText(this.filteredList.get(position).getDealerName());
                String mDealerAddress = this.filteredList.get(position).getAddress();
                if (mDealerAddress != null && !mDealerAddress.isEmpty()) {
                    ((MyViewHolder) holder).dealerAddress.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).dealerAddress.setText(this.filteredList.get(position).getAddress());
                } else {
                    ((MyViewHolder) holder).dealerAddress.setVisibility(View.GONE);
                }
                String city = this.filteredList.get(position).getCity();
                if (city != null && !city.isEmpty()) {
                    ((MyViewHolder) holder).dealerCity.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).dealerCity.setText(this.filteredList.get(position).getCity());
                } else {
                    ((MyViewHolder) holder).dealerCity.setVisibility(View.GONE);
                }
                String distance_value = this.filteredList.get(position).getDistance();
                if (distance_value != null && !distance_value.isEmpty()) {

                    ((MyViewHolder) holder).distance.setVisibility(View.VISIBLE);
                    // if (Integer.parseInt(this.filteredList.get(position).getDistance())>= 1)
                    ((MyViewHolder) holder).distance.setText(this.filteredList.get(position).getDistance()+" km");
                }else{
                    ((MyViewHolder) holder).distance.setVisibility(View.GONE);
                }

                if (this.filteredList.get(position) != null && this.filteredList.get(position).getDealerSource() != null) {
                    if (this.filteredList.get(position).getDealerSource().equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                        ((MyViewHolder) holder).pickupInfo.setText(REApplication.getAppContext().getResources().
                                getString(R.string.pick_and_drop));
                        ((MyViewHolder) holder).dealerSource.setText(REApplication.getAppContext().getResources().getString(R.string.text_online));
                    } else {
                        ((MyViewHolder) holder).pickupInfo.setText(REApplication.getAppContext().getResources().
                                getString(R.string.no_pick_and_drop));
                        ((MyViewHolder) holder).dealerSource.setText(REApplication.getAppContext().getResources().getString(R.string.text_offline));
                    }
                }

                Drawable drawable = REApplication.getInstance().getDrawable(R.drawable.shape);
                ((MyViewHolder) holder).locationImage.setImageDrawable(drawable);
            }
        } else {
            ((CheckInViewHolder) holder).mTvCheckInPlaceName.setText(searchResultList.get(position).getName());
            ((CheckInViewHolder) holder).mTvCheckInAddress.setText(searchResultList.get(position).getFormatted_address());
        }
    }

    @Override
    public int getItemCount() {
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            if (filteredList != null) {
                return mSearchRowViewPresenter.getDealersListRowsCount(filteredList);
            } else {
                if (dealersList != null) {
                    return mSearchRowViewPresenter.getDealersListRowsCount(filteredList);
                }
            }
        } else {
            if (searchResultList != null && searchResultList.size() > 0)
                return searchResultList.size();
        }
        return 0;
    }


    @Override
    public Filter getFilter() {
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            filterFlag = true;
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    if (charSequence.toString().isEmpty()) {
                        filteredList = new ArrayList<>();
                        filteredList = dealersList;
                    } else {
                        filteredList = mSearchRowViewPresenter.getFilteredResults(charSequence.toString(), dealersList);
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filteredList = (ArrayList<DealerMasterResponse>) filterResults.values;
                    //  if(filteredList!=null)
                    if (filteredList != null && filteredList.size() > 0) {
                        mSearchFilterEmptyListListener.onSearchFilter(false);
                        notifyDataSetChanged();
                    } else {
                        if (mSearchFilterEmptyListListener != null)
                            mSearchFilterEmptyListListener.onSearchFilter(true);
                    }
                }
            };
        }
        return null;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements DealersListRowView, View.OnClickListener {
        TextView dealresName, pickupInfo, dealerSource, dealerAddress, dealerCity, distance;
        ImageView locationImage;
        //ImageView image;

        private MyViewHolder(View itemView, ItemClickListener mItemClickListner) {
            super(itemView);
            // get the reference of item view's
            listenerRef = new WeakReference<>(mItemClickListner);
            dealresName = itemView.findViewById(R.id.dealersName);
            pickupInfo = itemView.findViewById(R.id.pickupInfo);
            dealerSource = itemView.findViewById(R.id.dealer_source);
            dealerCity = itemView.findViewById(R.id.dealerCity);
            distance = itemView.findViewById(R.id.distance);
            locationImage = itemView.findViewById(R.id.shape);
            dealerAddress = itemView.findViewById(R.id.dealerAddress);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onItemClick(v, getAdapterPosition(), filteredList);
        }

        @Override
        public void setDealersName(String dealersName) {
            dealresName.setText(dealersName);
        }

        @Override
        public void pickupInfo(String pickupInfo) {

        }

        @Override
        public void setDistance(String distance) {

        }

        @Override
        public void setImage(Drawable drawable) {

        }
    }

    public class CheckInViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvCheckInPlaceName, mTvCheckInAddress;

        CheckInViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCheckInPlaceName = itemView.findViewById(R.id.tv_check_in_place_name);
            mTvCheckInAddress = itemView.findViewById(R.id.tv_check_in_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            checkInListener.CheckInClicked(v, getAdapterPosition(), mTvCheckInPlaceName.getText().toString(),
                    mTvCheckInAddress.getText().toString());
        }
    }

    public interface CheckInListener {
        void CheckInClicked(View view, int position, String strSelectedValue, String formattedAddress);
    }

}
