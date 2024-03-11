package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import java.util.ArrayList;

public class MasterDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CountryModel> mDataList;
    private final OnMasterItemClickListener mClickListener;
    private final String mMasterDataType;


    public MasterDataAdapter(ArrayList<CountryModel> dataList, OnMasterItemClickListener clickListener, String masterDataType) {
        mDataList = dataList;
        mClickListener = clickListener;
        mMasterDataType = masterDataType;
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

        void bindItems(CountryModel model) {

            if (mMasterDataType.equals(CustomDialog.MasterDataType.COUNTRY_CODE.mDataType)
                    || mMasterDataType.equals(CustomDialog.MasterDataType.S1_COUNTRY_CODE.mDataType)) {
//                masterItem.setText(model.getDescription() + "("+model.geDialingCode()+")");

                masterItem.setText(mItemView.getResources().getString(R.string.country_code_with_desc
                        , model.getDescription(), model.getDiallingcode()));

            } else {
                masterItem.setText(model.getDescription());
            }
            if (model.isSelected() == 1) {
                masterItem.setTextColor(mItemView.getResources().getColor(R.color.white));
            }
            masterItem.setOnClickListener(view -> {

                if (mMasterDataType.equals(CustomDialog.MasterDataType.COUNTRY_CODE.mDataType)) {
                    mClickListener.onCountryCodeClicked(getAdapterPosition());
                } else if (mMasterDataType.equals(CustomDialog.MasterDataType.S1_COUNTRY_CODE.mDataType)) {
                    mClickListener.onS1CountryCodeClicked(getAdapterPosition());
                } else {
                    mClickListener.onMasterItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
