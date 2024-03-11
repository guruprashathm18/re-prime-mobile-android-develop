package com.royalenfield.reprime.ui.home.rides.activity;

import android.os.Bundle;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;

public class NotificationsActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    TitleBarView mTitleBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initializeView();
    }

    private void initializeView() {
        mTitleBarView = findViewById(R.id.custom_topbar);
        mTitleBarView.bindData(this, R.drawable.icon_close, getResources().getString(R.string.notifications_title));
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_down);
    }
}
