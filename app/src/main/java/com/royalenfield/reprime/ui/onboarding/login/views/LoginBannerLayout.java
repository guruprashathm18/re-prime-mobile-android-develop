package com.royalenfield.reprime.ui.onboarding.login.views;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.royalenfield.reprime.R;

/**
 * Created by ins1cob on 11/20/2018.
 */

public class LoginBannerLayout extends ConstraintLayout {
    View view;
    TextView headerText;
    ImageView headerImage;

    public LoginBannerLayout(Context context) {
        super(context);
        init(context);
    }

    public LoginBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoginBannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the view.
     *
     * @param context The context.
     */
    private void init(Context context) {
        view = inflate(context, R.layout.login_banner, this);
        headerText = view.findViewById(R.id.tv_login_banner_text);
        headerImage = view.findViewById(R.id.iv_re_name_logo);

    }

    /**
     * Binds the data to the view.
     *
     * @param header {@link String } object.
     */
    public void bindData(String header, int drawable) {
        headerText.setText(header);
        headerImage.setImageResource(drawable);
    }

}
