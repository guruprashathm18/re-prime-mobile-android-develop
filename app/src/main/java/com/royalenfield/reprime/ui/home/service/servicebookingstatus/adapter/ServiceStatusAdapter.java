package com.royalenfield.reprime.ui.home.service.servicebookingstatus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;

import java.util.List;


public class ServiceStatusAdapter extends RecyclerView.Adapter<ServiceStatusAdapter.MyViewHolder> {

    private static final int TYPE_START = 1;
    private static final int TYPE_MIDDEL = 2;
    private static final int TYPE_END = 3;
    private Context mContext;
    private List<ServiceJobCardStatusList> mServiceJobCardStatusList;
    private String[] mStatusStrings = {"Service request received", "Vehicle Inward", "Bike being serviced",
            "Service completed", "Proceed to payment", "Bike Ready for delivery"};


    public ServiceStatusAdapter(Context context, List<ServiceJobCardStatusList> mServiceJobCardStatusList) {
        mContext = context;
        this.mServiceJobCardStatusList = mServiceJobCardStatusList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_START) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_service_start, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_MIDDEL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_service_middle, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_END) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_service_end, parent, false);
            return new MyViewHolder(view);
        } else {
            throw new RuntimeException("The type has to be ");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mName.setText(mStatusStrings[position]);
        if (mServiceJobCardStatusList.get(position).getService_status()) {
            holder.mTickMark.setVisibility(View.VISIBLE);
            holder.mStatusSquare.setImageDrawable(mContext.getDrawable(R.drawable.status_square_white));
        } else {
            holder.mTickMark.setVisibility(View.GONE);
            holder.mStatusSquare.setImageDrawable(mContext.getDrawable(R.drawable.status_square_black));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_START;
        } else if (position == 5) {
            return TYPE_END;
        } else {
            return TYPE_MIDDEL;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mTickMark, mStatusSquare;

        MyViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.text_status);
            mTickMark = view.findViewById(R.id.imageView_tickmark);
            mStatusSquare = view.findViewById(R.id.imageView_status_square);

        }
    }
}
