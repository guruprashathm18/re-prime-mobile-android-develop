package com.royalenfield.reprime.ui.home.rides.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author BOP1KOR on 3/26/2019.
 * <p>
 * Class rersponsible for holding the fragment.
 */
public class FragmentFrameHolder extends FrameLayout {
    //Fragment instance.
    private Fragment mFragment;

    /**
     * Constructor with context.
     *
     * @param context the context.
     */
    public FragmentFrameHolder(@NonNull Context context) {
        super(context);
    }

    /**
     * Constructor with a context and attribute set.
     *
     * @param context the context.
     * @param attrs   the attribute set.
     */
    public FragmentFrameHolder(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor with a context, attribute set and style parameters.
     *
     * @param context      the context.
     * @param attrs        the attribute set.
     * @param defStyleAttr the style parameter.
     */
    public FragmentFrameHolder(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Returns the current fragment held.
     *
     * @return {@link Fragment} currently held by this {@link FragmentFrameHolder}.
     */
    public Fragment getFragment() {
        return mFragment;
    }

    /**
     * Sets the current fragment.
     *
     * @param mFragment {@link Fragment} to be set.
     */
    private void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    /**
     * Loads the fragment dynamically.
     *
     * @param fragment {@link Fragment} object to be loaded.
     */
    public void loadFragment(FragmentActivity activity, Fragment fragment, String tag) {
        //Create a fragment manager instance.
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        //Create a fragment transaction instance.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //show animations.
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //Load the fragment object dynamically.
        if (mFragment == null) {
            fragmentTransaction.add(getId(), fragment, tag);
        } else {
            fragmentTransaction.replace(getId(), fragment, tag);
        }
        setFragment(fragment);
        //Save the changes.
        //Temporary Fix: Calling #commitAllowingStateLoss() instead of #commit(), to avoid IllegalStateException.
        fragmentTransaction.commitAllowingStateLoss();
    }

}
