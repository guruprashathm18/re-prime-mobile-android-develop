package com.royalenfield.reprime.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class RERecyclerViewSeperatorDecoration extends RecyclerView.ItemDecoration {

    private final Paint mPaint;

    /**
     * Draws a line in given color
     *
     * @param context  : Context
     * @param color
     * @param heightDp
     */
    public RERecyclerViewSeperatorDecoration(@NonNull Context context, @ColorInt int color,
                                             @FloatRange(from = 0, fromInclusive = false) float heightDp) {
        mPaint = new Paint();
        mPaint.setColor(color);
        final float thickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                heightDp, context.getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(thickness);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        // we want to retrieve the position in the list
        final int position = params.getViewAdapterPosition();

        // and add a separator to any view but the last one
        if (position < state.getItemCount()) {
            outRect.set(0, 0, 0, (int) mPaint.getStrokeWidth()); // left, top, right, bottom
        } else {
            outRect.setEmpty(); // 0, 0, 0, 0
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        final int offset = (int) (mPaint.getStrokeWidth() / 2);

        // this will iterate over every visible view
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i == (parent.getChildCount())) {
                continue;
            }
            // get the view
            final View view = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            // get the position
            final int position = params.getViewAdapterPosition();

            // and finally draw the separator
            if (position < state.getItemCount()) {
                c.drawLine(view.getLeft(), view.getBottom() + offset, view.getRight(), view.getBottom() + offset, mPaint);
            }
        }
    }
}
