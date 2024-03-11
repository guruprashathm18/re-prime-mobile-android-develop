package com.royalenfield.reprime.base;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.utils.REDialogUtils;


/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * {@link Fragment} class that acts base class for all fragments used in application.
 */
public abstract class REBaseFragment extends Fragment implements REMvpView {
    private REBaseActivity mActivity;
    private ProgressDialog mProgressDialog;

    public REBaseFragment() {
        // Required empty public constructor
    }

    /**
     * To hide the keyboard
     *
     * @param activity view in the foreground.
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        if (activity instanceof REHomeActivity) {
            ((REHomeActivity)activity).setDoublePressToExitFlag(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public REBaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void showLoading() {
        hideLoading();
        if (this.getContext() != null) {
            mProgressDialog = REDialogUtils.showLoadingDialog(this.getContext());
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
            REDialogUtils.removeHandlerCallbacks();
        }
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
            REDialogUtils.removeHandlerCallbacks();
        }
        super.onDestroy();
    }

    public boolean isLoaderShowing() {
        if (mProgressDialog != null) {
            return mProgressDialog.isShowing();
        }
        return false;
    }
}
