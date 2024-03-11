package com.royalenfield.reprime.ui.home.ourworld.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

public class NewsAndLaunchesDetailsActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    private NewsResponse mNewsResponse;
    private TitleBarView mTitleBarView;
    private TextView mTvNewsDate;
    private TextView mTvNewsDescription;
    private TextView mTvContentBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_and_launches_details);
        getIntentData();
        initViews();
    }

    private void getIntentData() {
        mNewsResponse = REUtils.getmNewsResponse();
    }

    private void initViews() {
        mTitleBarView = findViewById(R.id.tb_news_title);
        mTvNewsDate = findViewById(R.id.tv_news_date);
        mTvNewsDescription = findViewById(R.id.tv_news_description);
        mTvContentBody = findViewById(R.id.text_contentBody);
        if (mNewsResponse != null) {
            setDataToViews();
        }
    }

    private void setDataToViews() throws IndexOutOfBoundsException {
        String mDate, mDescription, mTitle, mContentBody;
        try {
            mTitle = mNewsResponse.getNewsTitle();
            mDate = mNewsResponse.getNewsDateString();
            mDescription = mNewsResponse.getDescription();
            mContentBody = Html.fromHtml(mNewsResponse.getContentBody()).toString();
            mTitleBarView.bindData(this, R.drawable.icon_close, mTitle != null && !mTitle.isEmpty() ? mTitle : "");
            mTvNewsDate.setText(mDate != null && !mDate.isEmpty() ? mDate : getApplicationContext().
                    getResources().getString(R.string.text_hyphen_na));
            mTvNewsDescription.setText(mDescription != null && !mDescription.isEmpty() ? mDescription
                    : getApplicationContext().getResources().getString(R.string.text_hyphen_na));
            mTvContentBody.setText(mContentBody != null && !mContentBody.isEmpty() ? Html.fromHtml(mContentBody) :
                    getApplicationContext().getResources().getString(R.string.text_hyphen_na));

        } catch (NullPointerException e) {
            RELog.e(e);
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }
}
