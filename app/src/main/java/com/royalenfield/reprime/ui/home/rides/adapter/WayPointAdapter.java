package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.rides.activity.PlanRideActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ModifyRidesActivity;
import com.royalenfield.reprime.utils.REConstants;

import java.util.ArrayList;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_INRIDE;
import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_UPCOMINGRIDE;

/**
 * This adapter holds the waypoints
 */
public class WayPointAdapter extends RecyclerView.Adapter<WayPointAdapter.WayPointHolder> {

    private static final int TYPE_START = 1;
    private static final int TYPE_MIDDEL = 2;
    private static final int TYPE_END = 3;
    private final Context mContext;
    private ArrayList<WayPointsData> mWaypoints;
    private OnEditTextFocusListener mEditTextListener;
    private String mRideType;


    public WayPointAdapter(Context context, ArrayList<WayPointsData> waypoints, OnEditTextFocusListener listener,
                           String type) {
        this.mContext = context;
        setOnFocusListener(listener);
        mWaypoints = waypoints;
        mRideType = type;
    }

    private void setOnFocusListener(OnEditTextFocusListener onFocusListener) {
        this.mEditTextListener = onFocusListener;
    }

    @NonNull
    @Override
    public WayPointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_START) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waypoint_start, parent,
                    false);
            return new WayPointHolder(view);
        } else if (viewType == TYPE_MIDDEL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waypoint_middle, parent,
                    false);
            return new WayPointHolder(view);
        } else if (viewType == TYPE_END) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waypoint_end, parent,
                    false);
            return new WayPointHolder(view);
        } else {
            throw new RuntimeException("The type has to be ");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WayPointHolder holder, int position) {
        holder.mWaypointEdittext.setText(mWaypoints.get(position).getPlaceName());
        if (mWaypoints.get(position).getPlaceName() != null) {
            holder.mWaypointEdittext.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_regular));
        }
        if (mWaypoints.size() == 1 && holder.mDottedLine != null) {
            holder.mDottedLine.setVisibility(View.GONE);
        } else if (mWaypoints.size() > 1 && holder.mDottedLine != null) {
            holder.mDottedLine.setVisibility(View.VISIBLE);
        }
        holder.mWaypointEdittext.setCursorVisible(false);
        manageViews(holder, position);

    }

    private void manageViews(WayPointHolder holder, int position) {
        if (mRideType.equals(WAYPOINT_INRIDE) || mRideType.equals(WAYPOINT_UPCOMINGRIDE)) {
            if (position != 0) {
                holder.mRemoveWaypoint.setVisibility(View.GONE);
            }
            holder.mWaypointEdittext.setOnClickListener(null);
        } else if (mRideType.equals(REConstants.WAYPOINT_PLANRIDE) || mRideType.equals(REConstants.WAYPOINT_MODIFYRIDE)) {
            if (mWaypoints != null && mWaypoints.size() > 0 && position == mWaypoints.size() - 1) {
                if (mWaypoints.get(position).getPlaceName() != null &&
                        mWaypoints.get(position).getPlaceName().equals("")) {
                    holder.mWaypointEdittext.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_light));
                    holder.mWaypointEdittext.setHintTextColor(mContext.getResources().getColor(R.color.black_two));
                    holder.mWaypointEdittext.setHint(mContext.getResources().getString(R.string.label_add_destination));
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_START;
        } else if (position == mWaypoints.size() - 1) {
            return TYPE_END;
        } else {
            return TYPE_MIDDEL;
        }
    }

    @Override
    public int getItemCount() {
        return mWaypoints.size();
    }

    /**
     * Checks if the removed way-point is Destination if yes disables the ride plan information and
     * updates the destination way-point list with o.
     */
    private void removeDestinationFromPlanRide() {
        //Handles destination location removed logic
        if (mWaypoints != null && mWaypoints.size() > 0) {
            WayPointsData wayPointData = new WayPointsData(0, 0, "");
            mWaypoints.set(mWaypoints.size() - 1, wayPointData);
        }
        notifyDataSetChanged();
        if (mContext instanceof PlanRideActivity) {
            //Hides the ride plan details if destination is removed from the route.
            ((PlanRideActivity) mContext).hideRecommendedRouteFragment();
        }

        if (mContext instanceof ModifyRidesActivity) {
            //Hides the ride plan details if destination is removed from the route.
            ((ModifyRidesActivity) mContext).hideRecommendedRouteFragment();
        }
    }

    public interface OnEditTextFocusListener {
        void onEditTextFocusChange(boolean isDestination);
    }

    /**
     * A Simple ViewHolder for the WayPoint RecyclerView
     */
    public class WayPointHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            TextWatcher {

        ImageView mRemoveWaypoint;
        EditText mWaypointEdittext;
        ImageView mDottedLine;

        private WayPointHolder(final View itemView) {
            super(itemView);
            mRemoveWaypoint = itemView.findViewById(R.id.imageView_close);
            if (mRemoveWaypoint != null) mRemoveWaypoint.setOnClickListener(this);
            mWaypointEdittext = itemView.findViewById(R.id.waypoint_edittext);
            mWaypointEdittext.setOnClickListener(this);
            mDottedLine = itemView.findViewById(R.id.view_dottedline);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            try {
                int mPosition = getAdapterPosition();
                switch (v.getId()) {
                    case R.id.imageView_close:

                        if (mWaypoints != null && mPosition != mWaypoints.size() - 1) {
                            //Removes the waypoint from the middle of Start or Destination.
                            mWaypoints.remove(mPosition);
                            notifyItemRemoved(mPosition);
                        } else {
                            removeDestinationFromPlanRide();
                        }
                        // Updating route-map if any way-point, that can be start, destination or stop over  is removed.
                        if (mContext instanceof PlanRideActivity) {
                            ((PlanRideActivity) mContext).updateRoute(mWaypoints);
                        }

                        // Updating route-map if any way-point, that can be start, destination or stop over  is removed.
                        if (mContext instanceof ModifyRidesActivity) {
                            ((ModifyRidesActivity) mContext).updateRoute(mWaypoints);
                        }

                        break;
                    case R.id.waypoint_edittext:
                        if (getAdapterPosition() == mWaypoints.size() - 1) {
                            mWaypointEdittext.setCursorVisible(true);
                            mEditTextListener.onEditTextFocusChange(true);
                        } else if (getAdapterPosition() == 0) {
                            mWaypointEdittext.setCursorVisible(true);
                            mEditTextListener.onEditTextFocusChange(false);
                        }
                        break;
                    default:
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                RELog.e(e);
            } catch (Exception e) {
                RELog.e(e);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (getAdapterPosition() == mWaypoints.size() - 1) {
                mEditTextListener.onEditTextFocusChange(true);
            } else if (getAdapterPosition() == 0) {
                mEditTextListener.onEditTextFocusChange(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
