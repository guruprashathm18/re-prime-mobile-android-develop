package com.royalenfield.reprime.ui.home.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author JAL7KOR on 1/10/2019.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    private Context mContext;

    private int iSelectedValue = -1;
    private RecyclerViewClickListener recyclerViewClickListener;
    private List<String> mTimeslotlist;

    public TimeSlotAdapter(Context context, List<String> timeList, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = context;
        this.mTimeslotlist = timeList;

        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NotNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slot_item, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull TimeSlotViewHolder holder, int position) {


        holder.textView_timeslot.setText(mTimeslotlist.get(position));
        if (position == iSelectedValue) {
            holder.textView_timeslot.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.textView_timeslot.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            holder.textView_timeslot.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            holder.textView_timeslot.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.textView_timeslot.setBackground(mContext.getResources().getDrawable(R.drawable.button_border_enable));
        }
    }

    @Override
    public int getItemCount() {
        if (mTimeslotlist != null && mTimeslotlist.size() > 4) {
            return 4;
        } else {
            if(mTimeslotlist!=null)
            return mTimeslotlist.size();
            else
                return 0;
        }
    }

    public void setSelectedTimeSlotEnabled(int position) {
        iSelectedValue = position;
        notifyDataSetChanged();

    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView_timeslot;

        private TimeSlotViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_timeslot = itemView.findViewById(R.id.textView_timeslot);
        }

        @Override
        public void onClick(View v) {
            iSelectedValue = getAdapterPosition();
            notifyDataSetChanged();
            recyclerViewClickListener.onTimeClick(getLayoutPosition());
        }
    }
}