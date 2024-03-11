package com.royalenfield.reprime.ui.signup.views;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;

public class PrivacyPolicyAndTermsActivity extends AppCompatActivity implements TitleBarView.OnNavigationIconClickListener {

    TitleBarView mTitleBarView;
    String mType;
    TextView mTextPrivacyAndTerms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy_termsconditions);
        // Getting intent data to know the type whether it is Privacy Policy or Terms of Condition
        if (getIntent() != null) mType = getIntent().getStringExtra("activity_type");
        initializeView();
    }

    /**
     * Initializing Views
     */
    private void initializeView() {
        mTitleBarView = findViewById(R.id.custom_topbar);
        mTitleBarView.bindData(this, R.drawable.icon_close, mType);
        mTextPrivacyAndTerms = findViewById(R.id.text_privacy_terms);
        //Setting text based on type
        if (mType.equals("Privacy Policy")) {
            mTextPrivacyAndTerms.setText(R.string.text_privacypolicy);
        } else {
            mTextPrivacyAndTerms.setText(R.string.text_termsofcondition);
        }
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
