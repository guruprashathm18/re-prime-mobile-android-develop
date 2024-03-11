package com.royalenfield.reprime.ui.home.homescreen.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class is to handle the swipe gestures on header tabs.
 */
public class REHeaderSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private HeaderSwipeListener mHeaderSwipeListener;
    private int downX;

    /**
     * Creates a GestureDetector with the supplied listener.
     * You may only use this constructor from a {@link android.os.Looper} thread.
     *
     * @param context the application's context
     * @throws NullPointerException if {@code listener} is null.
     * @see android.os.Handler#Handler()
     */
    public REHeaderSwipeTouchListener(Context context, HeaderSwipeListener swipelistener) {
        gestureDetector = new GestureDetector(context, new SingleTapListener());
        mHeaderSwipeListener = swipelistener;
    }

    /**
     * Header textview swipe
     */
    private void onSwipeLeft() {
        mHeaderSwipeListener.onSwipeLeft();
    }

    /**
     * Header textview swipe
     */
    private void onSwipeRight() {
        mHeaderSwipeListener.onSwipeRight();
    }

    /**
     * Header Textview click
     *
     * @param view: Textview
     */
    private void onClick(View view) {
        mHeaderSwipeListener.onClick(view.getId());
    }

    /**
     * This onTouch works for both click and swipe of header view
     *
     * @param v     :View
     * @param event :MotionEvent
     * @return boolean
     */
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            v.performClick();
            onClick(v); // Click action
            return true;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = (int) event.getX();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                int upX = (int) event.getX();
                if (upX > downX) {
                    onSwipeRight(); // swipe right
                } else  if (upX < downX) {
                    onSwipeLeft(); // swipe left
                }
                return true;
            }
        }

        return false;
    }

    /**
     * A convenience class to extend when you only want to listen for a subset
     * of all the gestures. This implements all methods in the
     * {@link GestureDetector.OnGestureListener}, {@link GestureDetector.OnDoubleTapListener},
     * and {@link GestureDetector.OnContextClickListener}
     * but does nothing and return {@code false} for all applicable methods.
     */
    private class SingleTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}

