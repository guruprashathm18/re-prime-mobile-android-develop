package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;

public class MotorcycleNotesActivity extends REBaseActivity implements View.OnClickListener {

    EditText edittext_write_notes;
    Button button_save_notes;
    String MY_PREFS_NAME = "Notes_Pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorcycle_notes);
        initViews();
    }

    //initialising the views
    public void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_motorcycle_notes);
        mTitleBarView.bindData(this, R.drawable.icon_close, getApplicationContext().getResources().getString(R.string.text_motorcycle_notes_title));
        mTitleBarView.ivNavButton.setOnClickListener(this);
        edittext_write_notes = findViewById(R.id.edittext_write_notes);
        edittext_write_notes.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edittext_write_notes.setRawInputType(InputType.TYPE_CLASS_TEXT);
        button_save_notes = findViewById(R.id.button_save_notes);
        button_save_notes.setOnClickListener(this);

        //Getting notes string from shared preferences
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredNotesText = prefs.getString("notes", null);
        if (restoredNotesText != null) {
            edittext_write_notes.setText(restoredNotesText);
        }
    }

    /**
     * saving notes to sharedpreference
     *
     * @param notes : notes from edittext
     */
    public void saveNotes(String notes) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("notes", notes);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_notes:
                String NOTES = edittext_write_notes.getText().toString();
                if (!NOTES.isEmpty()) {
                    saveNotes(NOTES);
//                Intent intent = new Intent();
//                intent.putExtra("notes", NOTES);
//                int requestCode = 1;
//                setResult(requestCode, intent);
                    finish();
                    overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.enternotes_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_navigation:
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
        }
    }

}
