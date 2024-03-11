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

public class RideStartTimeAdapter extends RecyclerView.Adapter<RideStartTimeAdapter.RideStartTImeHolder> {

    private final Context mContext;
    private final ArrayList<String> mTimes;
    private final Typeface mTypefaceBold, mTypefaceRegular;
    private int currentPosition;
    private boolean mEnable;
    private RidesListeners.RideGetSelectedTimeListener mSelectedTimeListener;

    public RideStartTimeAdapter(Context context, ArrayList<String> time, boolean enable,
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
     * @param rideStartTImeHolder : RideStartTImeHolder
     */
    private void setBackgroundColors(RideStartTImeHolder rideStartTImeHolder) {
        if (!mEnable) {
            rideStartTImeHolder.mTimeText.setTextColor(mContext.getResources().getColor(R.color.white_50));
            rideStartTImeHolder.mViewVert1.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideStartTImeHolder.mViewHorz1.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideStartTImeHolder.mViewHorz2.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideStartTImeHolder.mViewVert2.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
            rideStartTImeHolder.mViewMiddleLine.setBackgroundColor(mContext.getResources().getColor(R.color.white_50));
        } else {
            rideStartTImeHolder.mTimeText.setTextColor(mContext.getResources().getColor(R.color.white));
            rideStartTImeHolder.mViewVert1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideStartTImeHolder.mViewHorz1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideStartTImeHolder.mViewHorz2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideStartTImeHolder.mViewVert2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rideStartTImeHolder.mViewMiddleLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    /**
     * Setting heights based on selection
     *
     * @param rideStartTImeHolder : RideStartTImeHolder
     * @param position : int
     */
    private void setViewsHeight(RideStartTImeHolder rideStartTImeHolder, int position) {
        rideStartTImeHolder.mTimeText.setText(mTimes.get(position));
        ConstraintLayout.LayoutParams mConstarintMarginLayoutParams = (ConstraintLayout.LayoutParams)
                rideStartTImeHolder.mTimeText.getLayoutParams();
        ConstraintLayout.LayoutParams mConstarintHeightLayoutParams = (ConstraintLayout.LayoutParams)
                rideStartTImeHolder.mViewMiddleLine.getLayoutParams();
        if (position == currentPosition) {
            mConstarintMarginLayoutParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_selected_margin);
            rideStartTImeHolder.mTimeText.setTextSize(18);
            rideStartTImeHolder.mTimeText.setTypeface(mTypefaceBold);
            rideStartTImeHolder.mTimeText.setLayoutParams(mConstarintMarginLayoutParams);
            mConstarintHeightLayoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_selected_height);
            rideStartTImeHolder.mViewMiddleLine.setLayoutParams(mConstarintHeightLayoutParams);
        } else {
            mConstarintMarginLayoutParams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_margin);
            rideStartTImeHolder.mTimeText.setTextSize(18);
            rideStartTImeHolder.mTimeText.setTypeface(mTypefaceRegular);
            rideStartTImeHolder.mTimeText.setLayoutParams(mConstarintMarginLayoutParams);
            mConstarintHeightLayoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.ridetime_view_height);
            rideStartTImeHolder.mViewMiddleLine.setLayoutParams(mConstarintHeightLayoutParams);
        }
    }

    @NonNull
    @Override
    public RideStartTImeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ridetime_start, parent, false);
        return new RideStartTImeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideStartTImeHolder rideStartTImeHolder, int position) {
        setBackgroundColors(rideStartTImeHolder);
        setViewsHeight(rideStartTImeHolder, position);
    }


    @Override
    public int getItemCount() {
        return mTimes.size();
    }

    /**
     * A Simple ViewHolder for the RideStartTIme RecyclerView
     */
    public class RideStartTImeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTimeText;
        private View mViewMiddleLine, mViewVert1, mViewHorz1, mViewHorz2, mViewVert2;

        private RideStartTImeHolder(final View itemView) {
            super(itemView);
            mTimeText = itemView.findViewById(R.id.text_time);
            mViewMiddleLine = itemView.findViewById(R.id.view_middleline);
            mViewVert1 = itemView.findViewById(R.id.view_vert_1);
            mViewHorz1 = itemView.findViewById(R.id.view_horz_1);
            mViewHorz2 = itemView.findViewById(R.id.view_horz2);
            mViewVert2 = itemView.findViewById(R.id.view_vert2);
            if (mEnable) {
                itemView.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if(mSelectedTimeListener.getSelectedDate()) {
                currentPosition = getAdapterPosition();
                notifyDataSetChanged();
                mSelectedTimeListener.getSelectedTime(mTimeText.getText().toString());
            }
        }
    }
}
