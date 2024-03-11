package com.royalenfield.reprime.ui.home.rides.fragment.createride;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.royalenfield.reprime.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateRideTitleInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateRideTitleInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRideTitleInfo extends Fragment implements TextView.OnEditorActionListener, TextWatcher {

    private TextView tvRideTitle;
    private TextView tvRideDesc;
    private static final String ARG_TITLE = "param1";
    private static final String ARG_DESC = "param2";
    private String mParamTitle = "";
    private String mParamDesc = "";
    private OnFragmentInteractionListener mListener;

    public CreateRideTitleInfo() {
        // Required empty public constructor
    }

    public static CreateRideTitleInfo newInstance() {
        return new CreateRideTitleInfo();
    }

    public static CreateRideTitleInfo newInstance(String strTitle, String strDescription) {
        CreateRideTitleInfo fragment = new CreateRideTitleInfo();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, strTitle);
        args.putString(ARG_DESC, strDescription);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitle = getArguments().getString(ARG_TITLE);
            mParamDesc = getArguments().getString(ARG_DESC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_ride_title, container, false);
        initViews(rootView);
        return rootView;
    }


    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        tvRideTitle = rootView.findViewById(R.id.your_ride_title);
        tvRideTitle.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        tvRideTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);
        tvRideDesc = rootView.findViewById(R.id.your_ride_description);
        tvRideDesc.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tvRideDesc.setRawInputType(InputType.TYPE_CLASS_TEXT);

        tvRideTitle.setText(mParamTitle);
        tvRideDesc.setText(mParamDesc);

        tvRideTitle.setOnEditorActionListener(this);
        tvRideDesc.setOnEditorActionListener(this);

        tvRideTitle.addTextChangedListener(this);
        tvRideDesc.addTextChangedListener(this);
//        mListener.onCreateRideHeaderDetails(tvRideTitle.getText().toString(), tvRideDesc.getText().toString());
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
        mListener = null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NONE) {
            mListener.onCreateRideHeaderDetails(tvRideTitle.getText().toString(), tvRideDesc.getText().toString());
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mListener.onCreateRideHeaderDetails(tvRideTitle.getText().toString(), tvRideDesc.getText().toString());
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
        void onCreateRideHeaderDetails(String title, String desc);
    }
}
