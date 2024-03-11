package com.royalenfield.reprime.ui.home.rides;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.utils.REUtils;


/*
 * It contains the recommended route details.
 * */

public class RecommendedRouteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBarView mTitleBarView;
    private ImageView mCircularImage1, mCircularImage2, mCircularImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_route_detail);
        initViews();
    }

    /*
     * Initialising the views
     */

    public void initViews() {
        mTitleBarView = findViewById(R.id.tb_recommended_route_details);
        mTitleBarView.bindData(this, R.drawable.back_arrow, "");
        ImageView mNavCloseBtn = findViewById(R.id.iv_navigation);
        mNavCloseBtn.setOnClickListener(this);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.motorcycle_default);
        Bitmap circularBitMapWithBorder = REUtils.getCircularBitmapWithWhiteBorder(icon, 15);
        Bitmap circularBitmapWithoutBorder = REUtils.getCircularBitmapWithWhiteBorder(icon, 0);
        mCircularImage1 = findViewById(R.id.other_riders_image1);
        mCircularImage2 = findViewById(R.id.other_riders_image2);
        mCircularImage3 = findViewById(R.id.other_riders_image3);
        mCircularImage1.setImageBitmap(circularBitMapWithBorder);
        mCircularImage2.setImageBitmap(circularBitMapWithBorder);
        mCircularImage3.setImageBitmap(circularBitmapWithoutBorder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navigation:
                finish();
                overridePendingTransition(0, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

}
