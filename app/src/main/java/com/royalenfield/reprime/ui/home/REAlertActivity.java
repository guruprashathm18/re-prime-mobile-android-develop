package com.royalenfield.reprime.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;

public class REAlertActivity extends REBaseActivity {

    private String mTitle;
    private static OnItemClickListener mItemClickListener;

    public static Intent getIntent(Context context, String title, OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        Intent intent = new Intent(context, REAlertActivity.class);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_prompt);
        initViews();
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TextView mMessage = findViewById(R.id.textview_message);
        Button aPositiveBTN = findViewById(R.id.activity_alert_yes_BTN);
        aPositiveBTN.setOnClickListener(v -> {
            mItemClickListener.onPositiveButton();
            finish();
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);

        });
        Button aNegativeBTN = findViewById(R.id.activity_alert_no_BTN);
        aNegativeBTN.setOnClickListener(v -> {
            mItemClickListener.onNegativeButton();
            finish();
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
        });
        mTitle = getIntent().getStringExtra("title");
        //setting Message
        mMessage.setText(mTitle);
    }


    @Override
    public void onBackPressed() {
    }

    public interface OnItemClickListener {
        void onPositiveButton();

        void onNegativeButton();
    }
}
