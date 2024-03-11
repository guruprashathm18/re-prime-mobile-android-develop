package com.royalenfield.reprime.ui.barcode.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.web.qr.Data;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

public class BarcodeSuccessActivity extends REBaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_success);
		Bundle  paramsScr = new Bundle();
		paramsScr.putString("screenname", "Spares Genuinity Check Success");
		REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, paramsScr);
        TextView mTvHeading = findViewById(R.id.tv_actionbar_title);
        mTvHeading.setText(getString(R.string.spare_part).toUpperCase());
        ImageView ivNavigation = findViewById(R.id.iv_navigation);
        ivNavigation.setOnClickListener(this);
        Data data = new Gson().fromJson(getIntent().getStringExtra("DATA"), Data.class);
        TextView txt=(findViewById(R.id.txt_part_name));
        TextView txtPartCode=(findViewById(R.id.txt_part_code));
        TextView txtMrp=(findViewById(R.id.txt_price));
        TextView txtSoldBy=(findViewById(R.id.txt_dealer));
        txt.setText(data.getPartName());
        txtPartCode.setText(data.getPartCode());
        txtMrp.setText(data.getPrice());
        txtSoldBy.setText(data.getDealerName());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_navigation) {
            Intent intent=new Intent();
            setResult(2,intent);
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(2,intent);
    }
}
