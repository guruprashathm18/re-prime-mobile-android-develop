package com.royalenfield.reprime.ui.home.service.servicebookingslots.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;

import java.util.List;

/**
 * TimeSlotWithIntervalAdapter contains timeSlots recycleView which is
 * embedded in SlotCategoryFragment.
 */
public class TimeSlotWithIntervalAdapter extends RecyclerView.Adapter<TimeSlotWithIntervalAdapter.TimeSlotViewHolder> {
    private static Context mContext;
    private static int iSelectedValue = -1;
    private List<String> mTimeSlotList;
    private RecyclerViewClickListener mRecyclerViewClickListener;


    public TimeSlotWithIntervalAdapter(Context context, List<String> timeList, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = context;
        this.mTimeSlotList = timeList;
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public TimeSlotWithIntervalAdapter.TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeslotwithinterval, parent, false);
        return new TimeSlotWithIntervalAdapter.TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotWithIntervalAdapter.TimeSlotViewHolder holder, int position) {
        holder.textView_timeslot.setText(mTimeSlotList.get(position));
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
        return mTimeSlotList.size();
    }

    public void setSelectedTimeSlotEnabled(int position) {
        iSelectedValue = position;
        notifyDataSetChanged();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView_timeslot;

        public TimeSlotViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_timeslot = itemView.findViewById(R.id.textView_timeslot);

        }

        @Override
        public void onClick(View view) {
            iSelectedValue = getAdapterPosition();
            notifyDataSetChanged();
            mRecyclerViewClickListener.onTimeClick(iSelectedValue);
        }
    }
}
