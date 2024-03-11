package com.royalenfield.reprime.ui.splash.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;


/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * {@link REBaseFragment}Splash screen for this application..
 */
public class SplashScreenFragment extends REBaseFragment {


    private String mTitle;
    private int mDrawableId;
    private TextView mTvHeading;
    private RelativeLayout mRlSplashScreen;
    private ImageView imgLogo;

    public static SplashScreenFragment newInstance(int drawableId) {
        // Required empty public constructor
        SplashScreenFragment splashScreenFragment = new SplashScreenFragment();
        Bundle args = new Bundle();
        args.putInt("DRAWABLE_ID", drawableId);
        splashScreenFragment.setArguments(args);

        return splashScreenFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        mRlSplashScreen = rootView.findViewById(R.id.rl_splash_screen);
        imgLogo=rootView.findViewById(R.id.iv_re_name_logo1);
        if (getArguments() != null) {
            mTitle = getArguments().getString("TITLE");
            mDrawableId = getArguments().getInt("DRAWABLE_ID");
			try {
				imgLogo.setImageResource(mDrawableId);
			}
			catch (Exception e){

			}
        }
        return rootView;
    }

}
