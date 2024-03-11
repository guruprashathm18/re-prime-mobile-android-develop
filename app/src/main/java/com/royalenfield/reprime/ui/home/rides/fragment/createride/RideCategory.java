package com.royalenfield.reprime.ui.home.rides.fragment.createride;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.royalenfield.reprime.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RideCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RideCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RideCategory extends Fragment {

    private RadioGroup radioGroup;

    private OnFragmentInteractionListener mListener;
    private static String mRideCategory;
    private RadioButton radioButton;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RideCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static RideCategory newInstance(String rideCategory) {
        mRideCategory = rideCategory;
        return new RideCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_choose_ride_category, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        radioGroup = rootView.findViewById(R.id.rg_ride_category_option);
        ArrayList<String> buttonNames = new ArrayList<>();
        buttonNames.add("Day Ride");
        buttonNames.add("Weekend Ride");
        buttonNames.add("Long Weekend Ride");
        buttonNames.add("Multiday Ride");

        for (String rbText : buttonNames) {
            radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.layout_radiobutton, null);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 15, 0, 0);
            radioButton.setLayoutParams(params);
            radioButton.setId(radioGroup.getChildCount());
            radioButton.setText(rbText);
            if (mRideCategory != null && !mRideCategory.isEmpty()) {
                if (rbText.equalsIgnoreCase(mRideCategory)) {
                    setRadioButton();
                }
            } else {
                //By default Day ride is enabled
                if (rbText.equalsIgnoreCase("Day Ride")) {
                    setRadioButton();
                }
            }
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radio button by returned id
            RadioButton rdButtonSelected = rootView.findViewById(selectedId);
            onButtonPressed(rdButtonSelected.getText().toString());
        });

    }

    private void setRadioButton() {
        radioButton.setChecked(true);
        onButtonPressed(radioButton.getText().toString());
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentRideCategory(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRideCategory = null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentRideCategory(String uri);
    }
}
