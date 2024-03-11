package com.royalenfield.reprime.ui.home.service.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.custom.views.MapBuilder;
import com.royalenfield.reprime.ui.custom.views.MarkerBuilder;
import com.royalenfield.reprime.ui.custom.views.ServiceCenterDataModel;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.search.view.SearchFilterEmptyListListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * @author JAL7KOR on 2/1/2019.
 */
public class ServiceCenterDataViewAdapter extends RecyclerView.Adapter implements Filterable {

    private Context context;
    private int lastSelectedPosition = -1;
    private RecyclerViewClickListener mRecyclerViewClickListener;
    private ArrayList<ServiceCenterDataModel> serviceCenterArrayList;
    private Filter filter;
    private com.google.android.gms.maps.model.LatLng latLng;
    private SearchFilterEmptyListListener mSearchFilterEmptyListListener;
    private String crmStatus;
    private String mServiceCenterNo;
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;

    public ServiceCenterDataViewAdapter(Context context, ArrayList<ServiceCenterDataModel> serviceCenterList,
                                        RecyclerViewClickListener issueClickListener, SearchFilterEmptyListListener
                                                searchFilterEmptyListListener) {
        this.context = context;
        this.serviceCenterArrayList = serviceCenterList;
        this.mRecyclerViewClickListener = issueClickListener;
        latLng = new com.google.android.gms.maps.model.LatLng(
                REUserModelStore.getInstance().getLatitude(),
                REUserModelStore.getInstance().getLongitude());
        mSearchFilterEmptyListListener = searchFilterEmptyListListener;

    }

    /**
     * Adding bitmap to cache
     *
     * @param key    URL
     * @param bitmap will be saved in cache memory
     */
    private static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        try {
            if (getBitmapFromMemCache(key) == null) {
                REApplication.getMemoryCache().put(key, bitmap);
            }
        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage(),e.getLocalizedMessage());
        }

    }

    /**
     * Getting bitmap from cache
     *
     * @param key URL
     * @return bitmap will be displayed on screen
     */
    private static Bitmap getBitmapFromMemCache(String key) {
        return REApplication.getMemoryCache().get(key);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view;
        switch (viewType) {
            case ServiceCenterDataModel.LAST_VISITED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lastvisited_service_list,
                        parent, false);
                return new LastVisitedServiceHolder(view);
            case ServiceCenterDataModel.NEAR_YOU_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_near_service_list,
                        parent, false);
                return new NearYouServiceHolder(view);
            case ServiceCenterDataModel.SEARCH_SC_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_service_list,
                        parent, false);
                return new SearchServiceCenterHolder(view);
            case ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_near_service_list,
                        parent, false);
                return new NearYouServiceHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (serviceCenterArrayList.get(position).getViewType()) {
            case 0:
                return ServiceCenterDataModel.LAST_VISITED_TYPE;
            case 1:
                return ServiceCenterDataModel.NEAR_YOU_TYPE;
            case 2:
                return ServiceCenterDataModel.SEARCH_SC_TYPE;
            case 3:
                return ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, final int position) {
        // set the data in items
        ServiceCenterDataModel object = serviceCenterArrayList.get(position);
        if (object != null) {
            int iSelectedViewType = object.getViewType();
            DealerMasterResponse dealerMasterResponse = serviceCenterArrayList.get(position).getDealerMasterResponse();
            crmStatus = "";
            crmStatus = dealerMasterResponse.getDealerSource();
            mServiceCenterNo = dealerMasterResponse.getPhone();
            switch (object.getViewType()) {
                case ServiceCenterDataModel.LAST_VISITED_TYPE:
                    ((LastVisitedServiceHolder) holder).textViewAddress.setText(dealerMasterResponse.getDealerName());
                    if (crmStatus != null && crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                        ((LastVisitedServiceHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.pick_and_drop));
                    } else {
                        ((LastVisitedServiceHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.no_pick_and_drop));
                    }
                    if (latLng.latitude != 0 && latLng.longitude != 0 && !dealerMasterResponse.getDistance() .equals( "0")) {
                        ((LastVisitedServiceHolder) holder).textViewKMInfo.setText(context.getResources().
                                getString(R.string.distance, String.valueOf(dealerMasterResponse.getDistance())));
                    } else {
                        ((LastVisitedServiceHolder) holder).textViewKMInfo.setText("");
                    }
                    update(((LastVisitedServiceHolder) holder).imageView, dealerMasterResponse.getLatlng());
                    break;
                case ServiceCenterDataModel.NEAR_YOU_TYPE:
                case ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE:
                    dealerMasterResponse = serviceCenterArrayList.get(position).getDealerMasterResponse();
                    ((NearYouServiceHolder) holder).textViewAddress.setText(dealerMasterResponse.getDealerName());
                    if (crmStatus != null && crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                        ((NearYouServiceHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.pick_and_drop));
                    } else {
                        ((NearYouServiceHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.no_pick_and_drop));
                    }
                    if (latLng.latitude != 0 && latLng.longitude != 0 &&
                            dealerMasterResponse.getDistance() != null && !dealerMasterResponse.getDistance().isEmpty()) {
                        ((NearYouServiceHolder) holder).textViewKMInfo.setText(context.getResources().
                                getString(R.string.distance, String.valueOf(dealerMasterResponse.getDistance())));
                    } else {
                        ((NearYouServiceHolder) holder).textViewKMInfo.setText("");
                    }
                    ((NearYouServiceHolder) holder).radioButton.setChecked(object.getSelected());
                    if (iSelectedViewType == ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE) {
                        ((NearYouServiceHolder) holder).itemView.setOnClickListener(null);
                        ((NearYouServiceHolder) holder).radioButton.setVisibility(View.GONE);
                    } else {
                        if (mRecyclerViewClickListener == null) {
                            // This is for getting the slected branch position for reschedule flow
                            ((NearYouServiceHolder) holder).radioButton.setChecked(serviceCenterArrayList.get(position).getSelected());
                            serviceCenterArrayList.get(position).setSelected(serviceCenterArrayList.get(position).getSelected());
                        } else {
                            serviceCenterArrayList.get(position).setSelected(lastSelectedPosition == position);
                            ((NearYouServiceHolder) holder).radioButton.setChecked(lastSelectedPosition == position);
                        }
                    }
                    update(((NearYouServiceHolder) holder).imageView, dealerMasterResponse.getLatlng());
                    break;
                case ServiceCenterDataModel.SEARCH_SC_TYPE:
                    ((SearchServiceCenterHolder) holder).textViewAddress.setText(dealerMasterResponse.getDealerName());
                    if (crmStatus != null && crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                        ((SearchServiceCenterHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.pick_and_drop));
                    } else {
                        ((SearchServiceCenterHolder) holder).tvPickupDropOption.setText(context.getResources().
                                getString(R.string.no_pick_and_drop));
                    }
                    if (dealerMasterResponse.getDistance() != null && !dealerMasterResponse.getDistance().equals("0")) {
                        ((SearchServiceCenterHolder) holder).textViewKMInfo.setText(context.getResources().
                                getString(R.string.distance, String.valueOf(dealerMasterResponse.getDistance())));
                    } else {
                        ((SearchServiceCenterHolder) holder).textViewKMInfo.setText("");
                    }
                    update(((SearchServiceCenterHolder) holder).imageView, dealerMasterResponse.getLatlng());
                    if (mRecyclerViewClickListener != null) {
                        mRecyclerViewClickListener.onItemClick(position, ((SearchServiceCenterHolder) holder).selectLastVisitedServiceCenter());
                    }
                    break;
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter(serviceCenterArrayList);
        return filter;
    }

    @Override
    public int getItemCount() {
        return serviceCenterArrayList.size();
    }

    public void update(final ImageView imgView, final String strLatLong) {
        imgView.post(() -> {
            double mLatitude, mLongitude;
            if (strLatLong != null && !strLatLong.isEmpty() && !strLatLong.trim().isEmpty()) {
                String[] strLocation = strLatLong.split(",");
                if (strLocation.length > 0) {
                    mLatitude = Double.parseDouble(strLocation[0]);
                    mLongitude = Double.parseDouble(strLocation[1]);
                    MapBuilder mapBuilder = new MapBuilder().center(mLatitude, mLongitude).dimensions(imgView.getWidth(), imgView.getHeight()).zoom(15);
                    mapBuilder.setKey(REUtils.getREGoogleKeys().getStaticMapKey());
                    int markerColor = context.getResources().getColor(R.color.reddish_two);
                    mapBuilder.addMarker(new MarkerBuilder().color(markerColor).position(mLatitude, mLongitude));
                    String url = mapBuilder.build();
                    // Check whether cache is available or not
                    final Bitmap bitmap = getBitmapFromMemCache(url);
                    if (bitmap != null) {
                        imgView.setImageBitmap(bitmap);
                    } else {
                        // Getting from server
                        GetImageAsyncTask asyncTask = new GetImageAsyncTask(imgView);
                        asyncTask.execute(url);
                    }
                }
            } else {
                imgView.setImageResource(R.drawable.no_image_found);
            }
        });
    }

    /**
     * AsyncTask for downloading mapImage
     */
    private static class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewSetMap;

        // TODO need to check the crash while debugging
        GetImageAsyncTask(ImageView bitmap) {
            this.imageViewSetMap = new WeakReference<>(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {

                HttpGet httpRequest;
                httpRequest = new HttpGet(new URI(params[0]));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                InputStream instream = bufHttpEntity.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(instream);
                if (params[0] != null && bitmap != null) {
                    addBitmapToMemoryCache(params[0], bitmap);
                }
                if (instream != null) instream.close();
                return bitmap;
            } catch (IOException | URISyntaxException e) {
                RELog.e(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (imageViewSetMap.get() != null) {
                if (bitmap != null) {
                    imageViewSetMap.get().setImageBitmap(bitmap);
                } else {
                    imageViewSetMap.get().setImageResource(R.drawable.no_image_found);
                }
            }
        }
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    private class LastVisitedServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewAddress;
        TextView textViewKMInfo;
        TextView textViewStatus;
        TextView tvPickupDropOption;
        ImageView imageView;
        ImageView imgButtonLastVisited;
        FrameLayout mFrameMapImage;

        private LastVisitedServiceHolder(final View itemView) {
            super(itemView);
            this.textViewStatus = itemView.findViewById(R.id.textViewStatus);
            this.textViewAddress = itemView.findViewById(R.id.textViewAddress);
            this.textViewKMInfo = itemView.findViewById(R.id.textViewKMInfo);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.tvPickupDropOption = itemView.findViewById(R.id.textViewPickDropOption);
            this.imgButtonLastVisited = itemView.findViewById(R.id.imageButtonLastVisited);
            this.mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            itemView.setTag(this);
            if (mRecyclerViewClickListener != null) {
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.foreground_gradient_map_image));
                itemView.setOnClickListener(this);
            } else {
                imgButtonLastVisited.setVisibility(View.VISIBLE);
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.bg_service_center_view_disabled));
                textViewStatus.setVisibility(View.GONE);
                textViewKMInfo.setTextColor(context.getResources().getColor(R.color.white_30));
                textViewAddress.setTextColor(context.getResources().getColor(R.color.white_30));
                tvPickupDropOption.setTextColor(context.getResources().getColor(R.color.white_30));
                imgButtonLastVisited.setActivated(true);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.tick_66));
            }

        }

        private boolean selectLastVisitedServiceCenter() {
            boolean isSelected;
            if (imgButtonLastVisited.isActivated()) {
                imgButtonLastVisited.setActivated(false);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_circle_servicecenter));
                isSelected = false;
            } else {
                imgButtonLastVisited.setActivated(true);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.tick_66));
                isSelected = true;
            }
            return isSelected;
        }

        @Override
        public void onClick(View v) {
            //check crmstatus and show popup if delaer not provides online service
            //E - offline, D-Online booking allowed
            Bundle params = new Bundle();
            params.putString("eventCategory", "Motorcycles");
            params.putString("eventAction", "Last visited click");
           REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
            lastSelectedPosition = getAdapterPosition();
            DealerMasterResponse dealerMasterResponse = serviceCenterArrayList.get(lastSelectedPosition).getDealerMasterResponse();
            crmStatus = "";
            crmStatus = dealerMasterResponse.getDealerSource();
            mServiceCenterNo = dealerMasterResponse.getPhone();
            if (crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_OFFLINE)){
                //mServiceCenterNo = "88787";
                showOfflineServiceDialog(mServiceCenterNo);
            } else if(crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)){
                notifyDataSetChanged();
                mRecyclerViewClickListener.onItemClick(lastSelectedPosition, selectLastVisitedServiceCenter());
            }
            else{
                Toast.makeText(context, context.getResources().getString(R.string.dealer_noservice),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    private class NearYouServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus;
        TextView textViewAddress;
        TextView textViewKMInfo;
        TextView tvPickupDropOption;
        ImageView imageView;
        RadioButton radioButton;
        FrameLayout mFrameMapImage;

        private NearYouServiceHolder(final View itemView) {
            super(itemView);
            this.textViewStatus = itemView.findViewById(R.id.textViewStatus);
            this.textViewAddress = itemView.findViewById(R.id.textViewAddress);
            this.textViewKMInfo = itemView.findViewById(R.id.textViewKMInfo);
            this.tvPickupDropOption = itemView.findViewById(R.id.textViewPickDropOption);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.radioButton = itemView.findViewById(R.id.rd_nearby_service_center);
            this.mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            itemView.setTag(this);

            if (mRecyclerViewClickListener != null) {
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.foreground_gradient_map_image));
                itemView.setOnClickListener(this);
                if (latLng.latitude == 0 && latLng.longitude == 0) {
                    textViewStatus.setVisibility(View.GONE);
                }
                radioButton.setOnClickListener(v -> {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    mRecyclerViewClickListener.onItemClick(lastSelectedPosition, true);
                });
            } else {
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setChecked(true);
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.bg_service_center_view_disabled));
                textViewStatus.setVisibility(View.GONE);
                textViewKMInfo.setTextColor(context.getResources().getColor(R.color.white_30));
                textViewAddress.setTextColor(context.getResources().getColor(R.color.white_30));
                tvPickupDropOption.setTextColor(context.getResources().getColor(R.color.white_30));
            }
        }

        @Override
        public void onClick(View v) {
            //check crmstatus and show popup if delaer not provides online service
            //E - offline, D-Online booking allowed
            lastSelectedPosition = getAdapterPosition();
            DealerMasterResponse dealerMasterResponse = serviceCenterArrayList.get(lastSelectedPosition).getDealerMasterResponse();
            crmStatus = "";
            crmStatus = dealerMasterResponse.getDealerSource();
            mServiceCenterNo = dealerMasterResponse.getPhone();
            if (crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_OFFLINE)){
                //mServiceCenterNo = "88787";
                showOfflineServiceDialog(mServiceCenterNo);
            } else if(crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)){
                notifyDataSetChanged();
                mRecyclerViewClickListener.onItemClick(lastSelectedPosition, true);
            }
            else{
                Toast.makeText(context, context.getResources().getString(R.string.dealer_noservice),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    private class SearchServiceCenterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewAddress;
        TextView textViewKMInfo;
        TextView tvPickupDropOption;
        ImageView imageView;
        ImageView imgButtonLastVisited;
        FrameLayout mFrameMapImage;

        private SearchServiceCenterHolder(final View itemView) {
            super(itemView);
            this.textViewAddress = itemView.findViewById(R.id.textViewAddress);
            this.textViewKMInfo = itemView.findViewById(R.id.textViewKMInfo);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.tvPickupDropOption = itemView.findViewById(R.id.textViewPickDropOption);
            this.imgButtonLastVisited = itemView.findViewById(R.id.imageButtonLastVisited);
            this.mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            itemView.setTag(this);
            if (mRecyclerViewClickListener != null) {
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.foreground_gradient_map_image));
              /*  itemView.setOnClickListener(v -> mRecyclerViewClickListener.onItemClick(getAdapterPosition(),
                        selectLastVisitedServiceCenter()));*/
              itemView.setOnClickListener(this);
            } else {
                imgButtonLastVisited.setVisibility(View.VISIBLE);
                mFrameMapImage.setForeground(context.getDrawable(R.drawable.bg_service_center_view_disabled));
                //textViewStatus.setTextColor(context.getResources().getColor(R.color.white_30));
                textViewKMInfo.setTextColor(context.getResources().getColor(R.color.white_30));
                textViewAddress.setTextColor(context.getResources().getColor(R.color.white_30));
                tvPickupDropOption.setTextColor(context.getResources().getColor(R.color.white_30));
                imgButtonLastVisited.setActivated(true);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.tick_66));
            }

        }

        private boolean selectLastVisitedServiceCenter() {
            boolean isSelected;
            if (imgButtonLastVisited.isActivated()) {
                imgButtonLastVisited.setActivated(false);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_circle_servicecenter));
                isSelected = false;
            } else {
                imgButtonLastVisited.setActivated(true);
                imgButtonLastVisited.setImageDrawable(context.getResources().getDrawable(R.drawable.tick_66));
                isSelected = true;
            }
            return isSelected;
        }

        @Override
        public void onClick(View v) {
            //check crmstatus and show popup if delaer not provides online service
            //E - offline, D-Online booking allowed
            lastSelectedPosition = getAdapterPosition();
            DealerMasterResponse dealerMasterResponse = serviceCenterArrayList.get(lastSelectedPosition).getDealerMasterResponse();
            crmStatus = "";
            crmStatus = dealerMasterResponse.getDealerSource();
            mServiceCenterNo = dealerMasterResponse.getPhone();
            if (crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_OFFLINE)){
                //mServiceCenterNo = "88787";
                showOfflineServiceDialog(mServiceCenterNo);
            } else if(crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)){
                notifyDataSetChanged();
                mRecyclerViewClickListener.onItemClick(lastSelectedPosition, true);
            }
            else{
                Toast.makeText(context, context.getResources().getString(R.string.dealer_noservice),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Class for filtering in Arraylist listview. Objects need a valid
     * 'toString()' method.
     */

    private class AppFilter extends Filter {

        private ArrayList<ServiceCenterDataModel> sourceObjects;

        private AppFilter(List<ServiceCenterDataModel> objects) {
            sourceObjects = new ArrayList<>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq.length() > 0) {
                ArrayList<ServiceCenterDataModel> filter = new ArrayList<>();

                for (ServiceCenterDataModel object : sourceObjects) {
                    DealerMasterResponse dealersResponse = object.getDealerMasterResponse();
                    // the filtering itself:
                    if (dealersResponse.getDealerName() != null) {
                        if (dealersResponse.getDealerName().toLowerCase().contains(filterSeq)) {
                            filter.add(object);
                        }
                    }
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            if (results.values != null) {
                ArrayList<ServiceCenterDataModel> filtered = (ArrayList<ServiceCenterDataModel>) results.values;
                if (filtered.size() > 0) {
                    if (mSearchFilterEmptyListListener != null) {
                        mSearchFilterEmptyListListener.onSearchFilter(false);
                    }
                    serviceCenterArrayList = filtered;
                    notifyDataSetChanged();
                } else {
                    if (mSearchFilterEmptyListListener != null)
                        mSearchFilterEmptyListListener.onSearchFilter(true);
                }
            }

        }
    }
    private void showOfflineServiceDialog(String mServiceCenterNo) {
        REUtils.showOnlyOfflineServicDialog(((REBaseActivity) context), context.getString(R.string.text_servicecenter_offline_title), context.getString(R.string.text_servicecenter_offline_msg), new REUtils.OnDialogButtonClickListener() {
            @Override
            public void onOkCLick() {
                // Toast.makeText(getActivity(),"CALLLLLLLLL",Toast.LENGTH_SHORT).show();
                if (mServiceCenterNo != null && !mServiceCenterNo.equals("")) {
                    if (context != null) {
                        ((REBaseActivity) context).checkAndRequestCallPermissions(context,
                                ((REBaseActivity) context), mServiceCenterNo,
                                SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS, ((REBaseActivity) context));
                    }
                } else {
                    Toast.makeText(context, "Contact number unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}