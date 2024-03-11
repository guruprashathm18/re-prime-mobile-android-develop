package com.royalenfield.reprime.ui.custom.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.royalenfield.reprime.R;

/**
 * @author BOP1KOR on 12/4/2018.
 * <p>
 * Common title bar for the most of the screen which has image icon and title.
 */

public class TitleBarView extends ConstraintLayout implements View.OnClickListener {

    //Views
    public ImageView ivNavButton;
    public ImageView ivCreateRideRoadIcon;
    //OnNavigationIconClickListener
    private OnNavigationIconClickListener onNavigationIconClickListener;
    private OnCheckInIconClickListener onCheckInIconClickListener;
    private TextView tvTitle;

    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public TitleBarView(Context context) {
        this(context, null);
    }

    /**
     * Constructor with a context and attribute set.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with a context, attribute set and style resource.
     *
     * @param context      The context.
     * @param attrs        The attribute set.
     * @param defStyleAttr The style resource.
     */
    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //get the activity context
        if (context instanceof OnNavigationIconClickListener) {
            onNavigationIconClickListener = (OnNavigationIconClickListener) context;
        }
        //get the activity context
        if (context instanceof OnCheckInIconClickListener) {
            onCheckInIconClickListener = (OnCheckInIconClickListener) context;
        }
        //Initialize the views.
        initViews();
    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar, this);

        ivNavButton = view.findViewById(R.id.iv_navigation);
        ivCreateRideRoadIcon = view.findViewById(R.id.iv_createride_icon);
        tvTitle = view.findViewById(R.id.tv_actionbar_title);

        ivNavButton.setOnClickListener(this);
        ivCreateRideRoadIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navigation:
                if (onNavigationIconClickListener != null) {
                    onNavigationIconClickListener.onNavigationIconClick();
                }
                break;
            case R.id.iv_createride_icon:
                if (onCheckInIconClickListener != null) {
                    onCheckInIconClickListener.onCheckInClickListener();
                }
                break;
        }
    }

    /**
     * Binds data to views
     *
     * @param context the context
     * @param title   action bar title
     */
    public void bindData(Activity context, int drawableId, String title) {
        tvTitle.setText(title);
        ivNavButton.setImageResource(drawableId);
    }

    /**
     * Binds data to views
     *
     * @param context the context
     * @param title   action bar title
     *                We are extending the same layout file for create ride header UI by enabling path/road icon in the header view
     *                we are overloading the method so that already implemented activities or fragments will not have any side effects
     */
    public void bindData(Activity context, int drawableId, String title, int drawablePathIcon) {
        tvTitle.setText(title);
        ivNavButton.setImageResource(drawableId);
        ivCreateRideRoadIcon.setImageResource(drawablePathIcon);
        ivCreateRideRoadIcon.setVisibility(VISIBLE);
    }

    public void setTitleCaps(boolean allCaps) {
        tvTitle.setAllCaps(false);
    }


    public interface OnNavigationIconClickListener {
        void onNavigationIconClick();
    }

    public interface OnCheckInIconClickListener {
        void onCheckInClickListener();
    }
    public void setInterface(OnNavigationIconClickListener onNavigationIconClickListener){
        this.onNavigationIconClickListener=onNavigationIconClickListener;
    }


}
