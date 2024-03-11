package com.royalenfield.reprime.ui.home.service.search.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.royalenfield.reprime.R;

public class SearchBannerLayout extends ConstraintLayout {
    View view;
    TextView headerText;
    ImageView headerImage;

    public SearchBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchBannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the view.
     *
     * @param context The context.
     */
    private void init(Context context) {
        view = inflate(context, R.layout.search_banner, this);
        headerText = view.findViewById(R.id.tv_search_banner);
        headerImage = findViewById(R.id.back_image);
    }

    /**
     * Binds the data to the view.
     *
     * @param header {@link String } object.
     */
    public void bindData(String header) {
        headerText.setText(header);
    }


    /**
     * Binds the drawable(image) to view.
     *
     * @param image
     */

    public void bindImage(Drawable image) {
        if (image != null)
            headerImage.setImageDrawable(image);
    }


}
