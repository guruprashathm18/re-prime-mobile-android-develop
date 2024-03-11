package com.royalenfield.reprime.ui.home.rides.custom;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author BOP1KOR on 3/28/2019.
 * <p>
 * This class Add equal spacing to RecyclerView items automatically. Can handle horizontal, vertical, and grid display modes
 */
public class RecyclerViewItemOffsetDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;
    private int mSpacing;
    private int mDisplayMode;

    public RecyclerViewItemOffsetDecoration(int spacing) {
        this(spacing, -1);
    }

    public RecyclerViewItemOffsetDecoration(int spacing, int displayMode) {
        this.mSpacing = spacing;
        this.mDisplayMode = displayMode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    /**
     * Sets the spacing between the Recycler view
     *
     * @param outRect       The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom).
     * @param layoutManager responsible for measuring and positioning item views
     * @param position      position
     * @param itemCount     item count
     */
    private void setSpacingForDirection(Rect outRect, RecyclerView.LayoutManager layoutManager, int position, int itemCount) {
        //Check and Resolve display mode automatically
        //position == itemCount - 1 ? mSpacing : 0
        if (mDisplayMode == -1) {
            mDisplayMode = resolveDisplayMode(layoutManager);
        }
        switch (mDisplayMode) {
            case HORIZONTAL:
                outRect.left = 0;
                outRect.right = position == itemCount - 1 ? 0 : mSpacing;
                outRect.top = 0;
                outRect.bottom = 0;
                break;
            case VERTICAL:
                outRect.left = mSpacing;
                outRect.right = mSpacing;
                outRect.top = mSpacing;
                outRect.bottom = position == itemCount - 1 ? mSpacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

                    //Edited by Jal7KOR
                    //TODO GRID means, we want space between the items not at left and top of the start and end items
                    outRect.left = 0;
                    outRect.right = position % cols == cols - 1 ? 0 : mSpacing;
                    outRect.top = 0;
                    outRect.bottom = position / cols == rows - 1 ? 0 : mSpacing;
                }
                break;
        }
    }

    /**
     * Checks the display mode of the view
     *
     * @param layoutManager layout manager
     * @return mode of the list view
     */
    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}
