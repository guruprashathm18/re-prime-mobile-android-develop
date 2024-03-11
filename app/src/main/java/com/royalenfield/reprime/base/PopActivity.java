package com.royalenfield.reprime.base;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

public class PopActivity extends REBaseActivity{
	private TextView txtOk;
	private TextView txtMsg;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_customalert);
		this.setFinishOnTouchOutside(false);
		txtOk=findViewById(R.id.textView_alert_okbutton);
		txtMsg=findViewById(R.id.textView_alert_message);
		txtMsg.setText(getString(R.string.account_hasbeen_deactivated));
		txtOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle params = new Bundle();
				params.putString("eventCategory", "Logout Pop Up");
				params.putString("eventAction", "Ok");
				params.putString("eventLabel", "NA");
				REUtils.logGTMEvent(REConstants.LOGOUT_GTM, params);

				REUtils.gotoSignin(PopActivity.this);
			}
		});

	}

	@Override
	public void onBackPressed() {

	}
}