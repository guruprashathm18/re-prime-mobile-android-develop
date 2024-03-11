package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;

import java.util.ArrayList;

public class RideEndTimeAdapter extends RecyclerView.Adapter<RideEndTimeAdapter.RideEndTimeHolder> {

    private final Context mContext;
    private final ArrayList<String> mTimes;
    private final Typeface mTypefaceBold, mTypefaceRegular;
    private int currentPosition;
    private boolean mEnable;
    private RidesListeners.RideGetSelectedTimeListener mSelectedTimeListener;

    public RideEndTimeAdapter(Context context, ArrayList<String> time, boolean enable,
                              RidesListeners.RideGetSelectedTimeListener listener, int selectedTime) {
        this.mContext = context;
        mTimes = time;
        mTypefaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(mContext, R.font.montserrat_regular);
        mEnable = enable;
        mSelectedTimeListener = listener;
        currentPosition = selectedTime;
    }

    /**
     * Setting colors based on selection
     *
     * @param rideEndTimeHolder : rideEndTimeHolder
     */
    private void setBackgroundColors(RideEndTimeAdapter.RideEndTimeHolder rideEndTimeHolder) {
        if (!mEnable) {
            rideEndTimeHolder.mTimeText.setTextColor(mContext.getResources().getColor(R.color.white_50));
            rideEndTimeHolder.mViewVert1.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideEndTimeHolder.mViewHorz1.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideEndTimeHolder.mViewHorz2.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideEndTimeHolder.mViewVert2.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideEndTimeHolder.mViewMiddleLine.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
        } else {
            rideEndTimeHolder.mTimeText.setTextColor(mContext.getResources().getColor(R.color.white));
            rideEndTimeHolder.mViewVert1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideEndTimeHolder.mViewHorz1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideEndTimeHolder.mViewHorz2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideEndTimeHolder.mViewVert2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideEndTimeHolder.mViewMiddleLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    /**
     * Setting heights based on selection
     *
     * @param rideEndTimeHolder : RideEndTimeHolder
     * @param position          : int
     */
    private void setViewsHeight(RideEndTimeAdapter.RideEndTimeHolder rideEndTimeHolder, int position) {
        rideEndTimeHolder.mTimeText.setText(mTimes.get(position));
        ConstraintLayout.LayoutParams mConstarintMarginLayoutParams = (ConstraintLayout.LayoutParams)
                rideEndTimeHolder.mTimeText.getLayoutParams();
        ConstraintLayout.LayoutParams mConstarintHeightLayoutParams = (ConstraintLayout.LayoutParams)
                rideEndTimeHolder.mViewMiddleLine.getLayoutParams();
        if (position == currentPosition) {
            mConstarintMarginLayoutParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_selected_margin);
            rideEndTimeHolder.mTimeText.setTextSize(18);
            rideEndTimeHolder.mTimeText.setTypeface(mTypefaceBold);
            rideEndTimeHolder.mTimeText.setLayoutParams(mConstarintMarginLayoutParams);
            mConstarintHeightLayoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_selected_height);
            rideEndTimeHolder.mViewMiddleLine.setLayoutParams(mConstarintHeightLayoutParams);
        } else {
            mConstarintMarginLayoutParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_margin);
            rideEndTimeHolder.mTimeText.setTextSize(18);
            rideEndTimeHolder.mTimeText.setTypeface(mTypefaceRegular);
            rideEndTimeHolder.mTimeText.setLayoutParams(mConstarintMarginLayoutParams);
            mConstarintHeightLayoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_height);
            rideEndTimeHolder.mViewMiddleLine.setLayoutParams(mConstarintHeightLayoutParams);
        }
    }

    @NonNull
    @Override
    public RideEndTimeAdapter.RideEndTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ridetime_end, parent, false);
        return new RideEndTimeAdapter.RideEndTimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideEndTimeAdapter.RideEndTimeHolder rideEndTimeHolder, int position) {
        setBackgroundColors(rideEndTimeHolder);
        setViewsHeight(rideEndTimeHolder, position);
    }


    @Override
    public int getItemCount() {
        return mTimes.size();
    }

    /**
     * A Simple ViewHolder for the RideEndTIme RecyclerView
     */
    public class RideEndTimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTimeText;
        private View mViewMiddleLine, mViewVert1, mViewHorz1, mViewHorz2, mViewVert2;

        private RideEndTimeHolder(final View itemView) {
            super(itemView);
            mTimeText = itemView.findViewById(R.id.text_end_time);
            mViewMiddleLine = itemView.findViewById(R.id.view_middleline_end_time);
            mViewVert1 = itemView.findViewById(R.id.view_vert_1_end_time);
            mViewHorz1 = itemView.findViewById(R.id.view_horz_1_end_time);
            mViewHorz2 = itemView.findViewById(R.id.view_horz2_end_time);
            mViewVert2 = itemView.findViewById(R.id.view_vert2_end_time);
            if (mEnable) {
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (mSelectedTimeListener.getSelectedDate() && mSelectedTimeListener.getSelectedEndDate()) {
                currentPosition = getAdapterPosition();
                notifyDataSetChanged();
                mSelectedTimeListener.getSelectedEndTime(mTimeText.getText().toString());
            }
        }
    }
}
