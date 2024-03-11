package com.royalenfield.reprime.ui.helper;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.models.request.web.rides.WayPointsData;

import java.util.ArrayList;

/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br/>
 * </br/>
 * Expects the <code>RecyclerView.Adapter</code> to react to {@link
 * ItemTouchHelperAdapter} callbacks and the <code>RecyclerView.ViewHolder</code> to implement
 * {@link ItemTouchHelperViewHolder}.
 *
 * @author kiran dani
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private ArrayList<WayPointsData> myWayPointsList;
    private int fromPosition, toPosition;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, ArrayList<WayPointsData> mWayPointsList) {
        mAdapter = adapter;
        myWayPointsList = mWayPointsList;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        int swipeFlags = createSwipeFlags(position);
        int size = myWayPointsList.size();
        if (position == 0 || size <= 2) {
            return makeMovementFlags(0, swipeFlags);
        }
        return makeMovementFlags(createSwipeFlags(position), swipeFlags);
    }

    private int createSwipeFlags(int position) {
        return position == 0 ? 0 : ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getAdapterPosition() == 0 || target.getAdapterPosition() == 0) {
            // Restrict movement to source view type
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            }
        }
        fromPosition = source.getAdapterPosition();
        toPosition = target.getAdapterPosition();
        mAdapter.onItemMove(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        //mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
        mAdapter.onItemChanged(fromPosition, toPosition);
    }
}
