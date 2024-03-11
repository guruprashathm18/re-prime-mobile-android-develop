package com.royalenfield.reprime.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.utils.REConstants;

import com.royalenfield.reprime.utils.RELog;

public class REAlertFragment extends REBaseFragment implements View.OnClickListener {

    private String mMessage;
    private String mType;

    public static REAlertFragment newInstance() {
        return new REAlertFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        try {
            getIntentData(view);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
        return view;
    }

    private void getIntentData(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMessage = bundle.getString(REConstants.ALERT_MESSAGE);
            mType = bundle.getString(REConstants.ALERT_TYPE);
            initViews(view);
        }
    }

    private void initViews(View view) {
        TextView textView_message = view.findViewById(R.id.textview_message);
        ImageView imageView_close = view.findViewById(R.id.imageView_close);
        ConstraintLayout mLoginConstraint = view.findViewById(R.id.login_constraint);
        TextView tvLogin = view.findViewById(R.id.tv_login);
        textView_message.setText(mMessage);
        if (mType.equals(REConstants.ALERT_TYPE_NAVIGATION_LOGIN)) {
            imageView_close.setVisibility(View.GONE);
            ImageView imageRoad = view.findViewById(R.id.image_road);
            imageRoad.setVisibility(View.VISIBLE);
            ImageView imageBike = view.findViewById(R.id.image_bike);
            imageBike.setVisibility(View.VISIBLE);
            mLoginConstraint.setVisibility(View.VISIBLE);
        }
        imageView_close.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView_close) {

        } else if (v.getId() == R.id.tv_login) {
            //Call Login screen
        }
    }
}
