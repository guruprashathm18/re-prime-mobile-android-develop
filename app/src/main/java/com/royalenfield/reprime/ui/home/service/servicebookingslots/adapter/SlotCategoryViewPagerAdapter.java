package com.royalenfield.reprime.ui.home.service.servicebookingslots.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment.SlotCategoryFragment;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * ViewPager Adapter for slotCategory TabLayout.
 * shows time slots based on slotTag,SlotTags would be,
 * Morning :: 9am to 11.45
 * AfterNoon :: 12pm to 15:45 pm
 * Evening :: 16pm to 5.45pm
 */

public class SlotCategoryViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private int mTotalTabs;
    private ArrayList<String> mTimeSlotList;
    private TabLayout mTabLayout;

    public SlotCategoryViewPagerAdapter(Context context, FragmentManager supportFragmentManager,
                                        int tabCount, ArrayList<String> timeSlotList, TabLayout tabLayout) {
        super(supportFragmentManager);
        this.mContext = context;
        this.mTotalTabs = tabCount;
        this.mTimeSlotList = timeSlotList;
        this.mTabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SlotCategoryFragment.newInstance(REApplication.getInstance().getApplicationContext().getResources().getString(R.string.Morning), mTimeSlotList);
            case 1:
                return SlotCategoryFragment.newInstance(REApplication.getInstance().getApplicationContext().getResources().getString(R.string.Afternoon), mTimeSlotList);
            case 2:
                return SlotCategoryFragment.newInstance(REApplication.getInstance().getApplicationContext().getResources().getString(R.string.Evening), mTimeSlotList);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mTabLayout.getTabAt(position).getText());
        tabTextView.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_regular));
        return view;
    }

    /**
     * To make the slected tabLayout text bold.
     *
     * @param position : int
     * @return : View
     */

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mTabLayout.getTabAt(position).getText());
        tabTextView.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_bold));
        return view;
    }

    @Override
    public void setPrimaryItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        RiderProfileViewPager pager = (RiderProfileViewPager) container;
        if (fragment != null && fragment.getView() != null) {
            pager.measureCurrentView(fragment.getView());
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
