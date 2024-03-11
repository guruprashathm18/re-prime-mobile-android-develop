package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.reprime.R;

public class OTAPUpdateFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private ItemClickListener mListener;
    private TextView mDescriptionTXT;
    private static String mDescriptionStr = "";
    private static boolean isDownloading = false;

    public static OTAPUpdateFragment newInstance(String description, boolean mDownloading) {
        mDescriptionStr = description;
        isDownloading = mDownloading;
        return new OTAPUpdateFragment();
    }

    public void setListener(ItemClickListener itemClickListener) {
        mListener = itemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otap_update, container, false);
        ImageView mImageClose = view.findViewById(R.id.imageView_close);
        mDescriptionTXT = view.findViewById(R.id.otap_update_desc_txt);
        mDescriptionTXT.setText(mDescriptionStr);
        mImageClose.setOnClickListener(this);
        Button mInstall = view.findViewById(R.id.button_install);
           mInstall.setText(R.string.btn_text_install);
        mInstall.setOnClickListener(this);
        Button mUpdateLater = view.findViewById(R.id.button_updatelater);
        mUpdateLater.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView_close) {
            dismiss();
        } else if (view.getId() == R.id.button_install) {
            mListener.onInstallClick();
        } else if (view.getId() == R.id.button_updatelater) {
            dismiss();
        }
    }

    public interface ItemClickListener {
        void onInstallClick();
    }
}
