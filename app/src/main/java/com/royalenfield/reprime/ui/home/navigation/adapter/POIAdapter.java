package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.navigation.model.POIModel;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

/**
 * Adapter creates views for the user created rides items and binds the data to the views.
 */

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder> {

    private Context mContext;
    private List<POIModel> poiModelList;
    private POIClickListener poiClickListener;
    private int selectedPosition = -1;
    private int[] mSelectedPOI = {R.drawable.ic_fork, R.drawable.ic_fuelstation, R.drawable.ic_coffeecup,
            R.drawable.ic_bed, R.drawable.ic_park};
    private boolean isFromMap = false;

    public POIAdapter(Context context,
                      List<POIModel> list, POIClickListener poiClickListener, boolean isFromMap) {
        this.mContext = context;
        this.poiModelList = list;
        this.poiClickListener = poiClickListener;
        this.isFromMap = isFromMap;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_poi_view, parent,
                false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        holder.poiName.setText(poiModelList.get(position).getName());
        if (isFromMap) {
            holder.poiName.setTextColor(ContextCompat.getColor(mContext, R.color.black_three));
        } else {
            holder.poiName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }
        //Changing the icon and bg color based on selection
        if (selectedPosition == position) {
            holder.poiImageBox.setImageDrawable(mContext.getResources().getDrawable(R.color.yellow));
            //REUtils.modifyDrawableColor(mContext, mSelectedPOI[position], Color.WHITE);
            REUtils.modifyDrawableColor(mContext, mSelectedPOI[position], ContextCompat.getColor(mContext, R.color.black_effective));
            holder.poiImage.setImageResource(mSelectedPOI[position]);
        } else {
            holder.poiImageBox.setImageDrawable(mContext.getResources().getDrawable(R.color.white));
            REUtils.modifyDrawableColor(mContext, poiModelList.get(position).getDrawable(),
                    ContextCompat.getColor(mContext, R.color.black_effective));
            holder.poiImage.setImageResource(poiModelList.get(position).getDrawable());
        }
    }

    @Override
    public int getItemCount() {
        return poiModelList.size();
    }

    class POIViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poiImage;
        RECircularImageView poiImageBox;
        TextView poiName;

        POIViewHolder(View itemView) {
            super(itemView);
            poiImageBox = itemView.findViewById(R.id.poi_image_box);
            poiImage = itemView.findViewById(R.id.poi_image);
            poiName = itemView.findViewById(R.id.poi_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle params = new Bundle();
            params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
            params.putString("eventAction", REConstants.poiCategoriesClicked);
            params.putString("eventLabel", poiModelList.get(getAdapterPosition()).getName());
//            params.putString("modelName", REUtils.getDeviceModel());
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
            if (!REUtils.isLocationEnabled(mContext)) {
                poiClickListener.poiItemCheckLocationisEnabled();
            } else {
                int iPosition = getAdapterPosition();
                boolean isChecked;
                if (iPosition != selectedPosition) {
                    //check the category
                    isChecked = true;
                    selectedPosition = iPosition;
                } else {
                    //uncheck the category
                    isChecked = false;
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
                poiClickListener.poiItemClicked(poiModelList.get(iPosition).getName(), isChecked);
            }
        }
    }

    public void setPOIItemHighlighted(int iPosition) {
        selectedPosition = iPosition;
        notifyDataSetChanged();
    }

    public interface POIClickListener {
        void poiItemClicked(String strPoiName, boolean isChecked);

        void poiItemCheckLocationisEnabled();
    }

}
