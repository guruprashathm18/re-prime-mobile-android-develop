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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;

import com.royalenfield.reprime.utils.RELog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TerrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TerrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TerrainFragment extends Fragment implements View.OnClickListener {
    private Button btnTopLeft;
    private Button btnTopRight;
    private Button btnBtmLeft;
    private Button btnBtmRight;
    private String mHighlightSelectedCategory = "";
    private OnFragmentInteractionListener mListener;

    public TerrainFragment() {
        // Required empty public constructor
    }

    public static TerrainFragment newInstance() {
        return new TerrainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_type_of_terrain, container, false);
        initViews(rootView);
        return rootView;
    }


    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        btnTopLeft = rootView.findViewById(R.id.rural_roads);
        btnTopRight = rootView.findViewById(R.id.broken_tarmac);
        btnBtmLeft = rootView.findViewById(R.id.off_road);
        btnBtmRight = rootView.findViewById(R.id.highway);

        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
        btnBtmLeft.setOnClickListener(this);
        btnBtmRight.setOnClickListener(this);

        highlightButton(btnTopLeft);
        noHighlightButton(btnTopRight);
        noHighlightButton(btnBtmLeft);
        noHighlightButton(btnBtmRight);
        onButtonPressed(btnTopLeft.getText().toString());

        if (mHighlightSelectedCategory.length() > 0) {
            highlightSelectedCategory(mHighlightSelectedCategory);
        }
    }

    enum Category {
        RURALROADS, BROKENTARMAC, OFFROAD, HIGHWAY
    }

    // Prints a line about Day using switch
    public void categoryIsLike(String strCategory) {
        mHighlightSelectedCategory = strCategory;
    }

    public void highlightSelectedCategory(String strCategory) {
        try {
            Category day = Category.valueOf(strCategory);
            switch (day) {
                case RURALROADS:
                    highlightButton(btnTopLeft);
                    noHighlightButton(btnTopRight);
                    noHighlightButton(btnBtmLeft);
                    noHighlightButton(btnBtmRight);
                    onButtonPressed(btnTopLeft.getText().toString());
                    break;
                case BROKENTARMAC:
                    highlightButton(btnTopRight);
                    noHighlightButton(btnTopLeft);
                    noHighlightButton(btnBtmLeft);
                    noHighlightButton(btnBtmRight);
                    onButtonPressed(btnTopRight.getText().toString());
                    break;
                case OFFROAD:
                    highlightButton(btnBtmLeft);
                    noHighlightButton(btnTopLeft);
                    noHighlightButton(btnTopRight);
                    noHighlightButton(btnBtmRight);
                    onButtonPressed(btnBtmLeft.getText().toString());
                    break;
                case HIGHWAY:
                    highlightButton(btnBtmRight);
                    noHighlightButton(btnTopLeft);
                    noHighlightButton(btnTopRight);
                    noHighlightButton(btnBtmLeft);
                    onButtonPressed(btnBtmRight.getText().toString());
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onTerrainFragmentInteraction(uri);
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
            case R.id.rural_roads:
                highlightButton(btnTopLeft);
                noHighlightButton(btnTopRight);
                noHighlightButton(btnBtmLeft);
                noHighlightButton(btnBtmRight);
                onButtonPressed(btnTopLeft.getText().toString());
                break;
            case R.id.broken_tarmac:
                highlightButton(btnTopRight);
                noHighlightButton(btnTopLeft);
                noHighlightButton(btnBtmLeft);
                noHighlightButton(btnBtmRight);
                onButtonPressed(btnTopRight.getText().toString());
                break;

            case R.id.off_road:
                highlightButton(btnBtmLeft);
                noHighlightButton(btnTopLeft);
                noHighlightButton(btnTopRight);
                noHighlightButton(btnBtmRight);
                onButtonPressed(btnBtmLeft.getText().toString());
                break;

            case R.id.highway:
                highlightButton(btnBtmRight);
                noHighlightButton(btnTopLeft);
                noHighlightButton(btnBtmLeft);
                noHighlightButton(btnTopRight);
                onButtonPressed(btnBtmRight.getText().toString());

                break;
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
        void onTerrainFragmentInteraction(String uri);
    }

}
