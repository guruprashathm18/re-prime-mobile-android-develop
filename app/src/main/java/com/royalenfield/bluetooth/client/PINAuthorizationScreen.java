package com.royalenfield.bluetooth.client;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

public class PINAuthorizationScreen extends BottomSheetDialogFragment
        implements View.OnKeyListener, View.OnClickListener {

    public static final String TAG = PINAuthorizationScreen.class.getSimpleName();
    private ItemClickListener mListener;
    private ImageView mImageClose;
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinFifthDigitEditText;
    private EditText mPinSixthDigitEditText;

    public static PINAuthorizationScreen newInstance() {
        return new PINAuthorizationScreen();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheet);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_authorized_bottomsheet, container, false);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mPINAuthBroadcastReceiver,
                new IntentFilter("pinAuth"));
        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPINAuthBroadcastReceiver);
    }

    /**
     *
     */
    private BroadcastReceiver mPINAuthBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("pinAuth".equals(intent.getAction())) {
                Log.e("PINDialog", "Auth Broadcast called");
                boolean b = intent.getBooleanExtra("auth", false);
                if (!b) {
                    mPinFirstDigitEditText.setText("");
                    mPinSecondDigitEditText.setText("");
                    mPinThirdDigitEditText.setText("");
                    mPinForthDigitEditText.setText("");
                    mPinFifthDigitEditText.setText("");
                    mPinSixthDigitEditText.setText("");

                    mPinFirstDigitEditText.requestFocus();
                } else {
                    dismiss();
                }
            }
        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("screenName", "Tripper Authorize");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, bundle);
        mImageClose = view.findViewById(R.id.img_close_view);
        mImageClose.setOnClickListener(this);
        view.findViewById(R.id.bt_trust).setOnClickListener(this);
        mPinFirstDigitEditText = view.findViewById(R.id.pin_first_edittext);
//        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText = view.findViewById(R.id.pin_second_edittext);
//        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText = view.findViewById(R.id.pin_third_edittext);
//        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText = view.findViewById(R.id.pin_forth_edittext);
//        mPinForthDigitEditText.setOnKeyListener(this);
        mPinFifthDigitEditText = view.findViewById(R.id.pin_fifth_edittext);
//        mPinFifthDigitEditText.setOnKeyListener(this);
        mPinSixthDigitEditText = view.findViewById(R.id.pin_sixth_edittext);
//        mPinSixthDigitEditText.setOnKeyListener(this);
        setupInputListeners();
    }

    private Drawable changeDrawableColor(Context context, int newColor) {
        try {
            Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.ic_cross).mutate();
            mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
            return mDrawable;
        } catch (Exception e) {
            RELog.e(e);
        }
        return null;
    }


    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resize bottom sheet dialog so it doesn't span the entire width past a particular measurement
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels - 100;
        int height = -1; // MATCH_PARENT
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public void setRouteListener(ItemClickListener routeListener) {
        mListener = routeListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        try {
            int id = view.getId();
            if (id == R.id.img_close_view) {
                dismiss();
            } else if (id == R.id.bt_trust) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", "Authorization");
                params.putString("eventLabel", "Connect");
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(mPinFirstDigitEditText.getText());
                stringBuffer.append(mPinSecondDigitEditText.getText());
                stringBuffer.append(mPinThirdDigitEditText.getText());
                stringBuffer.append(mPinForthDigitEditText.getText());
                stringBuffer.append(mPinFifthDigitEditText.getText());
                stringBuffer.append(mPinSixthDigitEditText.getText());
                if (stringBuffer.length() == 6) {
                    if (mListener != null)
                        mListener.onPinEnryCallback(stringBuffer.toString());
                } else if (stringBuffer.length() < 6) {
                    Toast.makeText(getActivity(), R.string.text_invalid_pin, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Resources.NotFoundException e) {
            RELog.e(e);
        }
    }


    private void setupInputListeners() {
        mPinFirstDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 1) {
//                    mPinSecondDigitEditText.requestFocus(View.FOCUS_DOWN);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinFirstDigitEditText.getText().toString().length() == 1) {
                    mPinSecondDigitEditText.requestFocus();
                }

            }
        });
        mPinSecondDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (mPinSecondDigitEditText.getText().toString().length() == 0) {
                    mPinFirstDigitEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinSecondDigitEditText.getText().toString().length() == 1) {
                    mPinThirdDigitEditText.requestFocus();
                }
            }
        });
        mPinThirdDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (mPinThirdDigitEditText.getText().toString().length() == 0) {
                    mPinSecondDigitEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinThirdDigitEditText.getText().toString().length() == 1) {
                    mPinForthDigitEditText.requestFocus();
                }
            }
        });
        mPinForthDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (mPinForthDigitEditText.getText().toString().length() == 0) {
                    mPinThirdDigitEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinForthDigitEditText.getText().toString().length() == 1) {
                    mPinFifthDigitEditText.requestFocus();
                }
            }
        });
        mPinFifthDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (mPinFifthDigitEditText.getText().toString().length() == 0) {
                    mPinForthDigitEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinFifthDigitEditText.getText().toString().length() == 1) {
                    mPinSixthDigitEditText.requestFocus();
                }
            }
        });
        mPinSixthDigitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (mPinSixthDigitEditText.getText().toString().length() == 0) {
                    mPinFifthDigitEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        Log.e("PIN", "onKey :" + keyEvent + "code: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (mPinSixthDigitEditText.getText().length() == 0) {
                // mPinsixthDigitEditText.setText("");
                mPinFifthDigitEditText.requestFocus();
            }
            if (mPinFifthDigitEditText.getText().length() == 0) {
                //mPinFifthDigitEditText.setText("");
                mPinForthDigitEditText.requestFocus();
            }
            if (mPinForthDigitEditText.getText().length() == 0) {
                // mPinForthDigitEditText.setText("");
                mPinThirdDigitEditText.requestFocus();
            }
            if (mPinThirdDigitEditText.getText().length() == 0) {
                //mPinThirdDigitEditText.setText("");
                mPinSecondDigitEditText.requestFocus();
            }
            if (mPinSecondDigitEditText.getText().length() == 0) {
                //mPinSecondDigitEditText.setText("");
                mPinFirstDigitEditText.requestFocus();
            }
            if (mPinFirstDigitEditText.getText().length() == 0) {
                mPinFirstDigitEditText.setText("");
            }
        }
        return true;
    }


    public interface ItemClickListener {
        void onPinEnryCallback(String pinEntry);
    }
}
