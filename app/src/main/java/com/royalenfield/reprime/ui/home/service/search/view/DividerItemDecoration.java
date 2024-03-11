package com.royalenfield.reprime.ui.home.service.search.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import android.view.View;
import com.royalenfield.reprime.R;

public class DividerItemDecoration extends ItemDecoration {
    private Drawable rDivider;

    public DividerItemDecoration(Context context) {
        rDivider = context.getResources().getDrawable(R.drawable.horizontal_devider_recycleview);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + rDivider.getIntrinsicHeight();

            rDivider.setBounds(left, top, right, bottom);
            rDivider.draw(c);
        }
    }


}
