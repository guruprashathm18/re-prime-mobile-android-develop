package com.royalenfield.reprime.ui.onboarding.userprofile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.royalenfield.reprime.R;

/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * FBUser profile screen to display the  all user info..
 */
public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView txtEmail = findViewById(R.id.text_facebook_email);
        TextView txtProfileName = findViewById(R.id.text_profile_name);
        ImageView ivProfilePicture = findViewById(R.id.iv_profile_picture);

        //TODO Below code is for receiving user facebook data. later it will be removed
        if (getIntent().getExtras() != null) {
          /*  Picasso.with(this).load(getIntent().getStringExtra("IMAGE_URL")).into(ivProfilePicture);
            txtEmail.setText(getIntent().getStringExtra("USER_EMAIL"));
            txtProfileName.setText(getIntent().getStringExtra("PROFILE_NAME"));*/
        }
    }
}
