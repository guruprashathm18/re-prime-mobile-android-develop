package com.royalenfield.reprime.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;

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
public class RERecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        /*int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                //rv.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }*/
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
