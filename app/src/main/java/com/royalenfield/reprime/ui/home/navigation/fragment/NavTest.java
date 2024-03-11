package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationMapFragment;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;

public class NavTest extends REBaseFragment implements RENavigationMapFragment.OnTouchListener {

    private FragmentActivity mContext;
    private FragmentFrameHolder mMapFragmentFrame;
    private RENavigationMapFragment mMapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navtest, container, false);
        mMapFragmentFrame = view.findViewById(R.id.nav_map_holdertest);
        mMapFragment = RENavigationMapFragment.newInstance();
        if (mContext != null && mMapFragmentFrame != null) {
            mMapFragmentFrame.loadFragment(mContext, mMapFragment, MapFragment.TAG_MAP_FRAGMENT);
        }

        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("VGFG","VGFGonPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("VGFG","VGFGonDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("VGFG","VGFGonDestrView");
    }

    @Override
    public void onTouch() {

    }
}
