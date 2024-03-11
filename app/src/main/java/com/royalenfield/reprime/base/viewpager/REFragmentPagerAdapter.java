package com.royalenfield.reprime.base.viewpager;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;

import java.util.List;

/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * {@link FragmentPagerAdapter} for {@link ViewPager} used for the splash screen.
 */

public class REFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<REBaseFragment> mFragments;
    private FragmentManager mFragmentManager;

    /**
     * Constructor.
     *
     * @param fm        {@link FragmentManager} instance.
     * @param fragments {@link List} of {@link Fragment}s.
     */
    public REFragmentPagerAdapter(FragmentManager fm, List<REBaseFragment> fragments) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragments = fragments;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * Returns the active fragment instance.
     *
     * @param container The container.
     * @param position  The current page in the {@link ViewPager}.
     * @return {@link Fragment} instance.
     */
    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return mFragmentManager.findFragmentByTag(name);
    }

    /**
     * Returns the Custom view for the TabLayout.
     *
     * @param context the context.
     * @param count   item count.
     * @param name    tab name.
     * @return View tab view.
     */
    public View getTabView(Context context, String count, String name) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.layout_custom_tab, null);
        TextView tabCount = v.findViewById(R.id.tv_tab_count);
        tabCount.setText(count);
        TextView tabName = v.findViewById(R.id.tv_tab_name);
        tabName.setText(name);
        return v;
    }

    /**
     * Provides the Custom View for the Selected Tab for the TabLayout.
     *
     * @param context the activity context.
     * @param count   count for the tab item
     * @param tabName tab name
     * @return view.
     */
    public View getSelectedTabView(Context context, String count, String tabName) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_tab, null);
        TextView tvTabCount = view.findViewById(R.id.tv_tab_count);
        tvTabCount.setText(count);
        TextView tvTabName = view.findViewById(R.id.tv_tab_name);
        tvTabName.setText(tabName);
        tvTabName.setTextColor(context.getResources().getColor(R.color.white));
        tvTabCount.setTextColor(context.getResources().getColor(R.color.white));
        tvTabCount.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(context, R.font.montserrat_bold));
        tvTabName.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(context, R.font.montserrat_bold));
        return view;
    }

}
