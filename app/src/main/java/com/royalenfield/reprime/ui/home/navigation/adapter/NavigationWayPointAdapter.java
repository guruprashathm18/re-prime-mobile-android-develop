package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.helper.ItemTouchHelperAdapter;
import com.royalenfield.reprime.ui.helper.ItemTouchHelperViewHolder;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationFragment;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.Collections;

import com.royalenfield.reprime.utils.RELog;

import static android.view.View.GONE;
import static com.royalenfield.reprime.ui.home.navigation.fragment.NavigationAddWayPointFragment.WAY_POINT_HINT;

/**
 * This adapter holds the waypoints for navigation
 */
public class NavigationWayPointAdapter extends RecyclerView.Adapter<NavigationWayPointAdapter.WayPointHolder> implements ItemTouchHelperAdapter {

    public static final int TYPE_START = 1;
    public static final int TYPE_END = 2;
    private final Context mContext;
    private ArrayList<WayPointsData> mWaypoints;
    private OnEditTextFocusListener mEditTextListener;
    private int iNavViewType = 0;
    private boolean mIsRecord = false;
    private int originalPosition;
    private boolean isFirstMove = true;
    private WayPointHolder mHolder;
    private RENavigationFragment.Controls mControls = RENavigationFragment.Controls.EXPAND;
    private boolean isDisableWaypoint = false;

    public NavigationWayPointAdapter(Context context, ArrayList<WayPointsData> waypoints,
                                     OnEditTextFocusListener listener, boolean isrecord) {
        this.mContext = context;
        setOnFocusListener(listener);
        mWaypoints = waypoints;
        mIsRecord = isrecord;
    }

    public void setDisableWaypoint(boolean isDisable) {
        this.isDisableWaypoint = isDisable;
    }

    public void setOnFocusListener(OnEditTextFocusListener onFocusListener) {
        this.mEditTextListener = onFocusListener;
    }

    @NonNull
    @Override
    public WayPointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        iNavViewType = viewType;
        if (viewType == TYPE_START) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waypoint_start, parent,
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
        mHolder = holder;
        String placeName = mWaypoints.get(position).getPlaceName();
        if (placeName != null) {
            holder.mWaypointEdittext.setText(placeName);
            if (placeName.equals(WAY_POINT_HINT)) {
                holder.mWaypointEdittext.setTextColor(ContextCompat.getColor(mContext, R.color.hint_color));
                holder.mWaypointEdittext.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_light));
            } else {
                holder.mWaypointEdittext.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.mWaypointEdittext.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_medium));
            }
        }
        holder.mWaypointEdittext.setCursorVisible(false);

        if (mControls.equals(RENavigationFragment.Controls.COLLAPSE)) {
            //to show only the destination on the live route
            holder.constraintLayout.setVisibility(View.VISIBLE);
        } else {
            if (mWaypoints.size() > 2) {
                holder.constraintLayout.setVisibility(View.VISIBLE);
            } else if (position == 0) {
                holder.constraintLayout.setVisibility(GONE);
            }
        }

        if (mWaypoints.size() == 2 && holder.mDottedLine1 != null && holder.mDottedLine2 != null) {
            holder.mDottedLine1.setVisibility(GONE);
            holder.mDottedLine2.setVisibility(GONE);
        } else if (mWaypoints.size() > 2 && holder.mDottedLine1 != null && holder.mDottedLine2 != null) {
            holder.mDottedLine1.setVisibility(View.VISIBLE);
            holder.mDottedLine2.setVisibility(View.VISIBLE);
            if (position == mWaypoints.size() - 1 && holder.mDottedLine1 != null && holder.mDottedLine2 != null) {
                holder.mDottedLine1.setVisibility(GONE);
                holder.mDottedLine2.setVisibility(View.VISIBLE);
            }
        }

        if (mControls.equals(RENavigationFragment.Controls.COLLAPSE)) {
            //to show only the destination on the live route
            if (position == mWaypoints.size() - 1) {
                holder.mLocationIMG.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_red));
                holder.mWaypointEdittext.setHint(mContext.getResources().getString(R.string.navigation_hint_enter_destination));
                holder.mRemoveWaypoint.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.filtericon));
            }
            assert holder.mDottedLine1 != null;
            holder.mDottedLine1.setVisibility(GONE);
            assert holder.mDottedLine2 != null;
            holder.mDottedLine2.setVisibility(GONE);
            holder.mWaypointEdittext.setOnClickListener(null);
        } else {
            if (position == mWaypoints.size() - 1) {
                holder.mLocationIMG.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_red));
                holder.mWaypointEdittext.setHint(mContext.getResources().getString(R.string.navigation_hint_enter_destination));
                holder.mRemoveWaypoint.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.filtericon));
            } else if (position == 0) {
                holder.mLocationIMG.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_green));
            } else {
                holder.mLocationIMG.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_yellow));
                holder.mRemoveWaypoint.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_close_waypoint));
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        //to show only the destination on the live route
        if (mControls.equals(RENavigationFragment.Controls.COLLAPSE)) {
            return TYPE_END;
        } else {
            if (position == 0) {
                return TYPE_START;
            } else {
                return TYPE_END;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mWaypoints.size();
    }

    public void collapseControls() {
        mControls = RENavigationFragment.Controls.COLLAPSE;
        mHolder.mWaypointEdittext.setOnClickListener(null);
        notifyDataSetChanged();
    }

    public void expandControls() {
        mControls = RENavigationFragment.Controls.EXPAND;
        mHolder.mWaypointEdittext.setOnClickListener(null);
        notifyDataSetChanged();
    }

    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then end position of the moved item.
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (isFirstMove)
            originalPosition = fromPosition;
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mWaypoints, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mWaypoints, i, i - 1);
            }
        }
        if (mHolder != null && mHolder.mDottedLine1 != null && mHolder.mDottedLine2 != null) {
            mHolder.mDottedLine1.setVisibility(GONE);
            mHolder.mDottedLine2.setVisibility(GONE);
        }
        notifyItemMoved(fromPosition, toPosition);
        isFirstMove = false;
    }

    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position The position of the item dismissed.
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     */
    @Override
    public void onItemDismiss(int position) {
        mWaypoints.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemChanged(int fromPosition, int toPosition) {
        // Updating route-map if any way-point is swapped to different position from its original position.
        if (originalPosition != toPosition) {
            mEditTextListener.onNavigationWaypointSwapped();
        }
        notifyDataSetChanged();
        isFirstMove = true;
    }

    public void updateWayPoints(ArrayList<WayPointsData> aWayPointsList) {
        mWaypoints = aWayPointsList;
    }

    public interface OnEditTextFocusListener {
        void onEditTextFocusChange(boolean isDestination, boolean isWayPoint, int position);

        void onNavigationWaypointRemoved(int position, boolean isEmptyWayPoint);

        void onFilterIconClicked();

        void onNavigationWaypointSwapped();
    }

    /**
     * A Simple ViewHolder for the WayPoint RecyclerView
     */
    public class WayPointHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            TextWatcher, ItemTouchHelperViewHolder {

        ImageView mRemoveWaypoint;
        EditText mWaypointEdittext;
        ImageView mDottedLine1, mDottedLine2, mLocationIMG;
        ConstraintLayout constraintLayout;

        private WayPointHolder(final View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.itemWaypoint);
            mRemoveWaypoint = itemView.findViewById(R.id.imageView_close);
            mWaypointEdittext = itemView.findViewById(R.id.waypoint_edittext);
            mLocationIMG = itemView.findViewById(R.id.imageView_location);
            //This disable the click if it is in recording flow
            if (!mIsRecord) {
                mWaypointEdittext.setOnClickListener(this);
                if (mRemoveWaypoint != null) {
                    mRemoveWaypoint.setOnClickListener(this);
                }
            }
            mDottedLine1 = itemView.findViewById(R.id.view_dottedline1);
            mDottedLine2 = itemView.findViewById(R.id.view_dottedline2);
            itemView.setOnClickListener(this);
            if (iNavViewType == TYPE_START) {
                constraintLayout.setVisibility(GONE);
            }
        }


        @Override
        public void onClick(View v) {
            int mPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.imageView_close:
                    if (mWaypoints != null && mWaypoints.size() > 0 && mPosition != mWaypoints.size() - 1) {
                        if (isDisableWaypoint)
                            return;
                        try {
                            REUtils.preventMultipleClick(v, 1000);
                            String placeName = mWaypoints.get(mPosition).getPlaceName();
                            //Removes the waypoint from the middle of Start or Destination.
                            mWaypoints.remove(mPosition);
                            notifyItemChanged(0);
                            notifyItemRemoved(mPosition);
                            notifyItemChanged(mWaypoints.size() - 1);

                            // Updating route-map if any way-point, that can be start, destination or stop over  is removed.
                            mEditTextListener.onNavigationWaypointRemoved(mPosition, placeName.equals(WAY_POINT_HINT));
                        } catch (Exception e) {
                            RELog.e(e);
                        }
                    } else {
                        if (!mIsRecord) {
                            mEditTextListener.onFilterIconClicked();
                        }
                    }
                    break;
                case R.id.waypoint_edittext:
                    if (isDisableWaypoint)
                        return;
                    if (getAdapterPosition() == mWaypoints.size() - 1) {
                        //mWaypointEdittext.setCursorVisible(true);
                        //If destination box clicked
                        mEditTextListener.onEditTextFocusChange(true, false, getAdapterPosition());
                    } else if (getAdapterPosition() > 0) {
                        //If way point box clicked disregard origin box
                        mEditTextListener.onEditTextFocusChange(false, true, getAdapterPosition());
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (getAdapterPosition() == mWaypoints.size() - 1) {
                //If destination text changed
                mEditTextListener.onEditTextFocusChange(true, false, getAdapterPosition());
            } else if (getAdapterPosition() > 0) {
                //If way point box text changed disregard origin box
                mEditTextListener.onEditTextFocusChange(false, true, getAdapterPosition());
            }
//            else if (getAdapterPosition() == 0) {
//                mEditTextListener.onEditTextFocusChange(false);
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        /**
         * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
         * Implementations should update the item view to indicate it's active state.
         */
        @Override
        public void onItemSelected() {
            if (getAdapterPosition() == 0) {
            } else {
                itemView.setBackgroundColor(Color.LTGRAY);
            }
        }

        /**
         * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
         * state should be cleared.
         */
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
