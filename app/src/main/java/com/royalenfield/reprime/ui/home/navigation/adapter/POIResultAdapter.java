package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.midgard.core.search.models.results.Photo;
import com.bosch.softtec.components.midgard.core.search.models.results.PoiSearchResult;
import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Adapter creates views for the user created rides items and binds the data to the views.
 */
public class POIResultAdapter extends RecyclerView.Adapter<POIResultAdapter.POIViewHolder> {

    private Context mContext;
    private List<SearchResult> poiResults;
    private POIClickListener poiClickListener;

    public POIResultAdapter(Context context, List<SearchResult> poiResults, POIClickListener poiClickListener) {
        this.mContext = context;
        this.poiResults = poiResults;
        this.poiClickListener = poiClickListener;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poi_result_view, parent,
                false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        PoiSearchResult searchResult = (PoiSearchResult) poiResults.get(position);
        holder.poiTitle.setText(searchResult.getPoiName());
        holder.poiDescription.setText(searchResult.getAddress().getFormattedAddress());
        Set<Photo> set = ((PoiSearchResult) poiResults.get(position)).getPhotos();
        ArrayList<Photo> list = new ArrayList<>(set);
        if (list != null && list.size() > 0) {
            String strPOIUrl = " ";
                strPOIUrl = REUtils.getMobileappbaseURLs().getTbtURL()+"tbt/place/photo?maxwidth=400&photoreference=" +
                        list.get(0).getPhotoReference();
            REUtils.loadImageWithGlide(mContext, strPOIUrl, holder.poiImage);
        }
    }

    @Override
    public int getItemCount() {
        return poiResults.size();
    }

    class POIViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poiImage;
        TextView poiTitle, poiDescription;

        POIViewHolder(View itemView) {
            super(itemView);
            poiImage = itemView.findViewById(R.id.poi_image_view);
            poiTitle = itemView.findViewById(R.id.poi_result_title);
            poiDescription = itemView.findViewById(R.id.poi_result_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int iPosition = getAdapterPosition();
            poiClickListener.poiResultItemClicked(poiResults.get(iPosition));
        }
    }

    public interface POIClickListener {
        void poiResultItemClicked(SearchResult strPoiName);
    }

}
