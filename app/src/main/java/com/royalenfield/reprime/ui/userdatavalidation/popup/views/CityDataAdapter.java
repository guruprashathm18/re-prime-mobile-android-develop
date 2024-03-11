package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;

import java.util.ArrayList;

public class CityDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CityModel> mDataList;
    private final OnMasterItemClickListener mClickListener;


    public CityDataAdapter(ArrayList<CityModel> dataList, OnMasterItemClickListener clickListener) {
        mDataList = dataList;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_data
                , parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolder)holder).bindItems(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        private final TextView masterItem;
        private final View mItemView;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            masterItem = itemView.findViewById(R.id.txv_master_item);
        }

        void bindItems(CityModel model) {
            masterItem.setText(model.getCityName());
            if (model.getSelected() == 1) {
                masterItem.setTextColor(mItemView.getResources().getColor(R.color.white));
            }
            masterItem.setOnClickListener(view -> mClickListener.onCityItemClicked(getAdapterPosition()));
        }
    }
}
