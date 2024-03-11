package com.royalenfield.reprime.ui.home.service.specificissue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.home.service.specificissue.fragment.ElectricalFragment;
import com.royalenfield.reprime.ui.home.service.specificissue.fragment.EngineFragment;
import com.royalenfield.reprime.ui.home.service.specificissue.fragment.GeneralFragment;
import com.royalenfield.reprime.ui.home.service.specificissue.fragment.VehicleFragment;
import com.royalenfield.reprime.ui.home.service.specificissue.interactor.IssueInteractor;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.application.REApplication.getAppContext;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class AnySpecificIssueActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        View.OnClickListener, TextWatcher, ViewPager.OnPageChangeListener, IssueInteractor {
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private TextView tvAddServiceIssues;
    private TextView tvIssueDescriptionTitle;
    private StringBuilder issueSelected = new StringBuilder();
    private EditText etAddDescription;
    private String strDescription = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_problem);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String strDescription = REApplication.getInstance().getDescriptionValue();
        if (strDescription != null && strDescription.length() > 0) {
            etAddDescription.setText(strDescription);
        }

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Specific Issues");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    private void initializeViews() {
        REApplication.getInstance().copySavedItemsToSelectedList(REServiceSharedPreference.getAllSavedServiceIssues(getAppContext()));
        TitleBarView mTitleBarView = findViewById(R.id.problem_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.text_label_motorcycle_issue));

        tvIssueDescriptionTitle = findViewById(R.id.issue_desc);
        tvAddServiceIssues = findViewById(R.id.issue_desc_add);
        Button btnSubmit = findViewById(R.id.btn_issue_submit);
        etAddDescription = findViewById(R.id.editText);
        ImageView ivNotesIcon = findViewById(R.id.iv_notes);
        btnSubmit.setOnClickListener(this);
        ivNotesIcon.setOnClickListener(this);
        hideIssueDescriptionView();

        RiderProfileViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // setCustomFont();
        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(this);

        etAddDescription.addTextChangedListener(this);


    }

    private void addTabs(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFrag(new GeneralFragment(), "General");
        adapter.addFrag(new EngineFragment(), "Engine");
        adapter.addFrag(new VehicleFragment(), "Vehicle");
        adapter.addFrag(new ElectricalFragment(), "Electricals");
        viewPager.setAdapter(adapter);
    }

    private void highLightCurrentTab(int position) {
        for (int iTabCount = 0; iTabCount < tabLayout.getTabCount(); iTabCount++) {
            TabLayout.Tab tab = tabLayout.getTabAt(iTabCount);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(iTabCount));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    private void hideIssueDescriptionView() {
        tvIssueDescriptionTitle.setVisibility(View.GONE);
        tvAddServiceIssues.setVisibility(View.GONE);
    }

    private void showIssueDescriptionView() {
        tvIssueDescriptionTitle.setVisibility(View.VISIBLE);
        tvAddServiceIssues.setVisibility(View.VISIBLE);
    }


    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }


    @Override
    public void showSelectedIssues() {
        ArrayList<String> selectedIssues = REUtils.getSelectedIssues(false, getAppContext());
        if (selectedIssues.size() > 0) {
            showIssueDescriptionView();
            setValues(selectedIssues);
        } else {
            hideIssueDescriptionView();
            REUtils.getSelectedIssues(false, getAppContext()).clear();
            issueSelected.setLength(0);
        }
    }

    @Override
    public void submitIssues() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Motorcycles-Schedule a service");
        params.putString("eventAction", "Any specific issue click");
        params.putString("eventLabel", "Submit click");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        REServiceSharedPreference.saveAllSelectedServiceIssues(getAppContext(), REApplication.getInstance().getAllSelectedServiceIssues());
        REApplication.getInstance().setDescriptionValue(strDescription);
        finish();//finishing activity
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_issue_submit:
                if ((issueSelected != null && issueSelected.length() > 0) || etAddDescription.getText().length() > 0) {
                    submitIssues();
                } else {
                    Toast.makeText(this, "No issues selected.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_notes:
                String MY_PREFS_NAME = "Notes_Pref";
                if (getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE) != null) {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String restoredNotesText = prefs.getString("notes", null);
                    if (restoredNotesText != null) {
                        StringBuilder stringBuilder = new StringBuilder(etAddDescription.getText().toString());
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append(restoredNotesText);
                        etAddDescription.setText(stringBuilder.toString());
                    }
                }
                break;
        }
    }

    public void setValues(final ArrayList<String> selectedValues) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            // UI code goes here
            try {
                issueSelected = new StringBuilder();
                if (selectedValues != null) {
                    for (String issue : selectedValues) {
                        issueSelected.append(issue).append("\n");
                    }
                }
                tvAddServiceIssues.setText(issueSelected.toString().trim());
            } catch (Exception e) {
                RELog.e(e);
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        strDescription = etAddDescription.getText().toString();
        Bundle params = new Bundle();
        params.putString("eventCategory", "Motorcycles-Schedule a service");
        params.putString("eventAction", "Any specific issue click");
        params.putString("eventLabel", "Anything specific textbox click");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        highLightCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context mContext;

        private ViewPagerAdapter(FragmentManager manager, Context context) {
            super(manager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public void setPrimaryItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
            super.setPrimaryItem(container, position, object);
            Fragment fragment = (Fragment) object;
            RiderProfileViewPager pager = (RiderProfileViewPager) container;
            if (fragment.getView() != null) {
                pager.measureCurrentView(fragment.getView());
            }
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        private View getTabView(int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            TextView tabTextView = view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));
            //tabTextView.setTextSize(16);
            tabTextView.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_regular));
            return view;
        }

        private View getSelectedTabView(int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            TextView tabTextView = view.findViewById(R.id.tabTextView);
            tabTextView.setText(mFragmentTitleList.get(position));
            tabTextView.setTextSize(19);
            tabTextView.setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_bold));
            return view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
