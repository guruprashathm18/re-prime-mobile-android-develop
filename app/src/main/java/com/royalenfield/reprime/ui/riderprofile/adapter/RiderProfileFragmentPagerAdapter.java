package com.royalenfield.reprime.ui.riderprofile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.ViewGroup;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.base.viewpager.REFragmentPagerAdapter;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;

import java.util.List;

/**
 * @author BOP1KOR on 12/26/2018.
 * <p>
 * FragmentPagerAdapter specific for the profile tabs...
 */

public class RiderProfileFragmentPagerAdapter extends REFragmentPagerAdapter {
    private int mCurrentPosition = -1;

    /**
     * Constructor.
     *
     * @param fm        {@link FragmentManager} instance.
     * @param fragments {@link List} of {@link Fragment}s.
     */
    public RiderProfileFragmentPagerAdapter(FragmentManager fm, List<REBaseFragment> fragments) {
        super(fm, fragments);
    }

    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            RiderProfileViewPager pager = (RiderProfileViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
}
