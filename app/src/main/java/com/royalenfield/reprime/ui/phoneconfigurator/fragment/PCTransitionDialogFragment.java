package com.royalenfield.reprime.ui.phoneconfigurator.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.royalenfield.reprime.R;

import org.jetbrains.annotations.NotNull;

public class PCTransitionDialogFragment extends DialogFragment {

    private static PCTransitionDialogFragment pcTransitionDialogFragment;


    public static PCTransitionDialogFragment newInstance() {
        if (pcTransitionDialogFragment == null) {
            pcTransitionDialogFragment = new PCTransitionDialogFragment();
        }
        return pcTransitionDialogFragment;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pc_transition_fragment, container,
                false);
    }
}
