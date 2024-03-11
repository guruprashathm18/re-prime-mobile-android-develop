package com.royalenfield.reprime.ui.home.service.fragment;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;

/**
 * Root fragment for servicing which holds REServicingFragment & ServiceAppointmentStatusFragment
 */
public class REServicingRootFragment extends REBaseFragment {

    private FragmentTransaction mFragmentTransaction;

    public static REServicingRootFragment newInstance() {
        return new REServicingRootFragment();
    }

    /**
     * Calling the fragment once it is is visible to user
     *
     * @param isVisibleToUser : boolean for fragment visibility
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mFragmentTransaction == null) {
            assert getFragmentManager() != null;
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.root_frame, new REServicingFragment());
            mFragmentTransaction.commitAllowingStateLoss();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_servicing_root, container, false);
    }


    @Override
    public void onDestroy() {
        mFragmentTransaction = null;
        super.onDestroy();
    }
}
