package com.royalenfield.reprime.ui.home.rides.fragment.createride;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;

import org.jetbrains.annotations.NotNull;

import com.royalenfield.reprime.utils.RELog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomRideCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomRideCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomRideCategory extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    private static final String ARG_TITLE = "title";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParamType;
    private String mParamTitle;
    private String mParamStart;
    private String mParamMiddle;
    private String mParamEnd;
    private String mHighlightSelectedCategory = "";
    private String mSelectedRideType = "";
    private Button btnStart;
    private Button btnMiddle;
    private Button btnEnd;

    private OnFragmentInteractionListener mListener;

    public CustomRideCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomRideCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomRideCategory newInstance(String strType, String title, String param1, String param2, String param3) {
        CustomRideCategory fragment = new CustomRideCategory();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, strType);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamType = getArguments().getString(ARG_TYPE);
            mParamTitle = getArguments().getString(ARG_TITLE);
            mParamStart = getArguments().getString(ARG_PARAM1);
            mParamMiddle = getArguments().getString(ARG_PARAM2);
            mParamEnd = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ride_types, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        try {
            TextView tvTitle = rootView.findViewById(R.id.tv_level);
            btnStart = rootView.findViewById(R.id.start);
            btnMiddle = rootView.findViewById(R.id.middle);
            btnEnd = rootView.findViewById(R.id.end);

            tvTitle.setText(mParamTitle);
            btnStart.setText(mParamStart);
            btnMiddle.setText(mParamMiddle);
            btnEnd.setText(mParamEnd);

            btnStart.setOnClickListener(this);
            btnMiddle.setOnClickListener(this);
            btnEnd.setOnClickListener(this);

            highlightButton(btnStart);
            noHighlightButton(btnMiddle);
            noHighlightButton(btnEnd);
            onButtonPressed(btnStart.getText().toString());

            if (mHighlightSelectedCategory != null && mHighlightSelectedCategory.length() > 0) {
                highlightSelectedCategory(mHighlightSelectedCategory);
            }
            if (mSelectedRideType != null && mSelectedRideType.length() > 0) {
                highlightSelectedCategory(mSelectedRideType);
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        }

    }

    enum Category {
        EASY, MEDIUM, HARD,
        SOLO, PRIVATE, PUBLIC
    }

    public void categoryIsLike(String strCategory) {
        mHighlightSelectedCategory = strCategory;
    }

    public void typeIsLike(String strtype) {
        mSelectedRideType = strtype;
    }

    // Prints a line about Day using switch
    private void highlightSelectedCategory(String strCategory) {
        try {
            Category day = Category.valueOf(strCategory);
            switch (day) {
                case EASY:
                case SOLO:
                    highlightButton(btnStart);
                    noHighlightButton(btnMiddle);
                    noHighlightButton(btnEnd);
                    onButtonPressed(btnStart.getText().toString());
                    break;
                case MEDIUM:
                case PRIVATE:
                    highlightButton(btnMiddle);
                    noHighlightButton(btnStart);
                    noHighlightButton(btnEnd);
                    onButtonPressed(btnMiddle.getText().toString());
                    break;
                case HARD:
                case PUBLIC:
                    highlightButton(btnEnd);
                    noHighlightButton(btnMiddle);
                    noHighlightButton(btnStart);
                    onButtonPressed(btnEnd.getText().toString());
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            RELog.e(e);
        } catch (IllegalStateException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    /*
     * This method is to highlight the selected button.
     *
     */

    private void highlightButton(Button button) {
        button.setBackgroundColor(getResources().getColor(R.color.white));
        button.setTextColor(getResources().getColor(R.color.black));
        Typeface bold = ResourcesCompat.getFont(getActivity(), R.font.montserrat_bold);
        String text = button.getText().toString();
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        spannable.setSpan((new RECustomTyperFaceSpan(bold)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(spannable);
    }

    /*
     * This method is to remove the highlight of the unselected buttons.
     */

    private void noHighlightButton(Button button) {
        button.setBackground(getActivity().getDrawable(R.drawable.button_border_disable));
        button.setTextColor(getResources().getColor(R.color.white));
        Typeface regular = ResourcesCompat.getFont(getActivity(), R.font.montserrat_regular);
        String text = button.getText().toString();
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        spannable.setSpan((new RECustomTyperFaceSpan(regular)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(spannable);
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(mParamType, uri);
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
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                highlightButton(btnStart);
                noHighlightButton(btnMiddle);
                noHighlightButton(btnEnd);
                onButtonPressed(btnStart.getText().toString());
                break;
            case R.id.middle:
                highlightButton(btnMiddle);
                noHighlightButton(btnStart);
                noHighlightButton(btnEnd);
                onButtonPressed(btnMiddle.getText().toString());
                break;
            case R.id.end:
                highlightButton(btnEnd);
                noHighlightButton(btnStart);
                noHighlightButton(btnMiddle);
                onButtonPressed(btnEnd.getText().toString());
                break;
        }
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
        void onFragmentInteraction(String type, String uri);
    }
}
