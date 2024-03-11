package com.royalenfield.reprime.utils;

import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * An OnItemTouchListener allows the application to intercept touch events in progress at the
 * view hierarchy level of the RecyclerView before those touch events are considered for
 * RecyclerView's own scrolling behavior.
 * <p>
 * <p>This can be useful for applications that wish to implement various forms of gestural
 * manipulation of item views within the RecyclerView. OnItemTouchListeners may intercept
 * a touch interaction already in progress even if the RecyclerView is already handling that
 * gesture stream itself for the purposes of scrolling.</p>
 *
 * @see RecyclerView.SimpleOnItemTouchListener
 */
public class RERecyclerNavigationTouchListener implements RecyclerView.OnItemTouchListener {
    @Override
    public boolean onInterceptTouchEvent(@NotNull RecyclerView rv, @NotNull MotionEvent e) {

        int action = e.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            rv.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NotNull RecyclerView rv, @NotNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
