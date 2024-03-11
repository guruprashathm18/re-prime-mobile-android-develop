package com.royalenfield.reprime.ui.home.homescreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.homescreen.view.CustomerCareActivity;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

public class HelpAndSupportFragment extends REBaseFragment implements View.OnClickListener {
    public HelpAndSupportFragment() {
        // Required empty public constructor
    }

    public static HelpAndSupportFragment newInstance(String param1, String param2) {
        return new HelpAndSupportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_help_support, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        TextView mTvHeading = rootView.findViewById(R.id.tv_search_banner);
        mTvHeading.setText(getString(R.string.text_help_and_support));
        rootView.findViewById(R.id.back_image).setOnClickListener(this);
        rootView.findViewById(R.id.llDIYGuides).setOnClickListener(this);
        rootView.findViewById(R.id.llCustomerCare).setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_image) {
            /*GO Back*/
        } else if (view.getId() == R.id.llDIYGuides) {
            callDIYMethod();
        } else if (view.getId() == R.id.llCustomerCare) {
            startExplicitActivity(CustomerCareActivity.class);
        }
    }

    private void callDIYMethod() {
        if (REUtils.isAppInstalled(REConstants.YOUTUBE_PACKAGENAME)) {
            startExplicitActivity(DoItYourSelfActivity.class);
        } else {
            REUtils.showErrorDialog(getActivity(),
                    getActivity().getResources().getString(R.string.diy_youtube_install_error));
        }
    }

    /**
     * Starts the Create Ride activity.
     */
    private void startExplicitActivity(Class activity) {
        startActivity(new Intent(getActivity(), activity));
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }
}
