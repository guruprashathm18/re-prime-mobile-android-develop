package com.royalenfield.reprime.ui.onboarding.userprofile;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.royalenfield.reprime.R;

/**
 * Upload Picture layout for adding pictures from
 * gallery
 */

public class UploadPictureLayout extends ConstraintLayout {

    View view;
    TextView text_upload;

    public UploadPictureLayout(Context context) {
        super(context);
        init(context);
    }

    public UploadPictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UploadPictureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the view.
     *
     * @param context The context.
     */
    private void init(Context context) {
        view = inflate(context, R.layout.layout_uploadpicture, this);
        text_upload = view.findViewById(R.id.text_upload);

    }

    /**
     * Binds the data to the view.
     *
     * @param title {@link String } object.
     */
    public void bindData(String title) {
        text_upload.setText(title);
    }
}
