package com.royalenfield.reprime.ui.home.homescreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

public class NavigationTBTMapFragment extends SupportMapFragment /*implements View.OnTouchListener*/ {

    public static final String TAG_MAP_FRAGMENT = NavigationTBTMapFragment.class.getName();
    public View mapView;
    public TouchableWrapper touchView;
    private NavigationTBTMapFragment.OnTouchListener listener;

    public static NavigationTBTMapFragment newInstance() {
        return new NavigationTBTMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mapView = super.onCreateView(inflater, parent, savedInstanceState);
        // overlay a touch view on map view to intercept the event
        touchView = new TouchableWrapper(getActivity());
        touchView.addView(mapView);
        return touchView;
    }

    @Override
    public View getView() {
        return mapView;
    }

    public void setOnTouchListener(NavigationTBTMapFragment.OnTouchListener listener) {
        this.listener = listener;
    }

    public interface OnTouchListener {
        void onTouch();
    }

    public class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    listener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    listener.onTouch();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}