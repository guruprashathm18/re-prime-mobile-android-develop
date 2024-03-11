package com.royalenfield.reprime.ui.home.homescreen.listener;

/**
 * HeaderSwipeListener implemented by Activity
 * It receives swipe and click call back from REHeaderSwipeTouchListener
 */
public interface HeaderSwipeListener {

    void onSwipeLeft();

    void onSwipeRight();

    void onClick(int view);
}
