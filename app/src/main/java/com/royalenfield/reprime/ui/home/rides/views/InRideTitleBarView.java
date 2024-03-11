package com.royalenfield.reprime.ui.home.rides.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;

public class InRideTitleBarView extends ConstraintLayout implements View.OnClickListener {

    //Views
    public ImageView ivNavButton;
    public ImageView ivCreateRideRoadIcon;
    //OnNavigationIconClickListener
    private TitleBarView.OnNavigationIconClickListener onNavigationIconClickListener;
    private TitleBarView.OnCheckInIconClickListener onCheckInIconClickListener;
    private TextView tvTitle;

    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public InRideTitleBarView(Context context) {
        this(context, null);
    }

    /**
     * Constructor with a context and attribute set.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public InRideTitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with a context, attribute set and style resource.
     *
     * @param context      The context.
     * @param attrs        The attribute set.
     * @param defStyleAttr The style resource.
     */
    public InRideTitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //get the activity context
        if (context instanceof TitleBarView.OnNavigationIconClickListener) {
            onNavigationIconClickListener = (TitleBarView.OnNavigationIconClickListener) context;
        }
        //get the activity context
        if (context instanceof TitleBarView.OnCheckInIconClickListener) {
            onCheckInIconClickListener = (TitleBarView.OnCheckInIconClickListener) context;
        }
        //Initialize the views.
        initViews();
    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_inride_titlebar, this);

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

}

