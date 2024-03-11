package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class TripWebViewFragment extends REBaseFragment {

    private static final String TAG = TripWebViewFragment.class.getSimpleName();
private WebView webView;

    public TripWebViewFragment() {
        // Required empty public constructor
    }

    public static TripWebViewFragment newInstance() {
        // Required empty public constructor
        return new TripWebViewFragment();
    }


    @Override
    public void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip_web_view, container, false);
        webView=rootView.findViewById(R.id.webview);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Loading the UI in Handler because it lags if we load in MainThread
    }


    /**
     * Initialising views and fragments
     *
     * @param rootView : rootView
     */
    public void initViews(View rootView) {
webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("https://google.com");

    }


}
