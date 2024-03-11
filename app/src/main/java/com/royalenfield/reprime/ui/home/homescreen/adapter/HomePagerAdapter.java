package com.royalenfield.reprime.ui.home.homescreen.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Parcelable;
import android.view.ViewGroup;

import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Home Viewpager
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private RiderProfileViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();


    public HomePagerAdapter(FragmentManager fragmentManager, RiderProfileViewPager viewPager) {
        super(fragmentManager);
        mViewPager = viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {

        return mFragmentList.size();
    }

    public void addFrag(ArrayList<Fragment> fragment, ArrayList<String> titleList) {
        mFragmentList = fragment;
        mFragmentTitleList = titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        if (fragment.getView() != null) {
            mViewPager.measureCurrentView(fragment.getView());
        }
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
