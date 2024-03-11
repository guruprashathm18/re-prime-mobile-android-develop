package com.royalenfield.reprime.ui.home.service.diy.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.diyyoutube.VideoAttribute;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.remoteconfig.DiyDatum;
import com.royalenfield.reprime.models.response.remoteconfig.DiyVideo;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.diy.adapter.DoItYourselfAdapter;
import com.royalenfield.reprime.ui.home.service.diy.interactor.DoItYourselfInteractor;
import com.royalenfield.reprime.ui.home.service.diy.presenter.DoItYourselfPresenter;
import com.royalenfield.reprime.ui.home.service.diy.presenter.DoItYourselfRowPresenter;
import com.royalenfield.reprime.ui.home.service.diy.view.DoItYourselfView;
import com.royalenfield.reprime.ui.home.service.search.view.ItemClickListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REGridSpacingItemDecoration;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_DIY_CONTAINER;
import static com.royalenfield.reprime.utils.REConstants.KEY_DIY_DIRNAME;
import static com.royalenfield.reprime.utils.REConstants.KEY_DIY_FILENAME;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class DoItYourSelfActivity extends REBaseActivity implements DoItYourselfView, TitleBarView.OnNavigationIconClickListener,
        AdapterView.OnItemSelectedListener {

    private static final int noOfRows = 2;
    TitleBarView mTitleBarView;
    RecyclerView mRecyclerView;
    private List<String> bikeList = new ArrayList<>();
    private List<String> videoID = new ArrayList<>();
    List<DiyDatum> diyVideos = new ArrayList<>();
    List<DiyVideo> diyVideoId = new ArrayList<>();
    private Spinner mSpinner;
    TextView mSpinnerItemView;
    //For setting the custom font.
    private Typeface mTypefaceBold, mTypefaceRegular;
    private DoItYourselfPresenter mDoItYourselfPresenter;
    private DoItYourselfAdapter mDoItYourselfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_your_self);
        initializeView();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "DIY Videos");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    private void initializeView() {
        mTitleBarView = findViewById(R.id.custom_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_header_diy_guides));
        mDoItYourselfPresenter = new DoItYourselfPresenter(this, new DoItYourselfInteractor());
        //To set font programatically.
        mTypefaceBold = ResourcesCompat.getFont(this, R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(this, R.font.montserrat_regular);
        mSpinner = findViewById(R.id.spinner);
        downloadDataFromAzure();
    }

    private void populateBikeListSpinner(String mVideosData) {
        //Parse and get the data from JSON String downloaded from Azure
        diyVideos = REUtils.getDiyVideoList(mVideosData).getDiyData();
        for (int i = 0; i < diyVideos.size(); i++) {
            String motorcycles = diyVideos.get(i).getMotorcycleName();
            //adding list of motorcycles from Json data
            bikeList.add(motorcycles);
        }
        //setting List of Motorcycle parsed from Json data to Spinner
        setAdapter();
    }

    private void setAdapter() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item,
                        bikeList);
        mSpinner.setAdapter(spinnerArrayAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showProgress() {
        showLoading();
    }

    @Override
    public void hideProgress() {
        hideLoading();
    }

    @Override
    public void setDataToRecyclerView(ArrayList<YoutubeVideoModel> mYoutubeVideoModelList) {
        int spacing = Math.round(4.6f * getResources().getDisplayMetrics().density);
        mRecyclerView = findViewById(R.id.view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, noOfRows));
        mRecyclerView.addItemDecoration(new REGridSpacingItemDecoration(2, spacing, false));
        mRecyclerView.setHasFixedSize(true);

        final ArrayList<YoutubeVideoModel> finalMYoutubeVideoModelList = mYoutubeVideoModelList;
        mDoItYourselfAdapter = new DoItYourselfAdapter(this, mYoutubeVideoModelList, new DoItYourselfRowPresenter(), new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, List<DealerMasterResponse> filteredList) {
                Bundle params = new Bundle();
                params.putString("eventCategory", REConstants.KEY_DIY_GTM);
                params.putString("eventAction", mSpinnerItemView.getText().toString());
                params.putString("eventLabel", finalMYoutubeVideoModelList.get(position).getTitle());
                REUtils.logGTMEvent(REConstants.KEY_DIY_GTM, params);

                Intent youtubeViewIntent = new Intent(DoItYourSelfActivity.this, YoutubePlayerActivity.class);
                youtubeViewIntent.putExtra("video_id", finalMYoutubeVideoModelList.get(position).getVideoId());
                youtubeViewIntent.putExtra("title", finalMYoutubeVideoModelList.get(position).getTitle());
                youtubeViewIntent.putExtra("likes", finalMYoutubeVideoModelList.get(position).getLikes());
                startActivity(youtubeViewIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            }
        }, mDoItYourselfPresenter);
        mRecyclerView.setAdapter(mDoItYourselfAdapter);
    }

    @Override
    public void onResponseFailure(String message) {
        Log.d("API", "fetchVideoAttributes() API failure");
        REUtils.showErrorDialog(this, message);
    }

    @Override
    public void onFetchSuccess(VideoAttribute mVideoAttribute) {
        Log.d("API", "fetchVideoAttributes() API success");
        mDoItYourselfPresenter.fetchVideoList(mVideoAttribute);
    }


    @Override
    public void onNavigationIconClick() {
        if (mDoItYourselfAdapter != null) {
            mDoItYourselfAdapter.releaseLoaders();
        }
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            mSpinnerItemView = ((TextView) view);
            mSpinnerItemView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().
                    getDrawable(R.drawable.path_2), null);
            mSpinnerItemView.setTextColor(getResources().getColor(R.color.black));
//        To support all versions use
            mSpinnerItemView.setTypeface(mTypefaceBold);
        } catch (Exception e) {
            RELog.e(e);
        }
        Log.e("bikeeee",mSpinnerItemView.getText().toString());
        Bundle params = new Bundle();
        params.putString("eventCategory", REConstants.KEY_DIY_GTM);
        params.putString("eventAction", mSpinnerItemView.getText().toString());
        REUtils.logGTMEvent(REConstants.KEY_DIY_GTM, params);
        //To get the List of Video ID of selected Motorcycle
        getVideoId(position);
    }

    private void getVideoId(int position) {
        videoID = new ArrayList<>();
        diyVideoId = diyVideos.get(position).getDiyVideos();
        for (int i = 0; i < diyVideoId.size(); i++) {
            String videoid = diyVideoId.get(i).getVideoId();
            videoID.add(videoid);
        }
        String[] video_Ids = videoID.toArray(new String[videoID.size()]);
        //Call to get the Youtube Video Details for the List of Videos under selected Motorcycle
        mDoItYourselfPresenter.fetchVideoAttributes(video_Ids);
        Log.d("API", "fetchVideoAttributes() API called");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void downloadDataFromAzure() {
		String containerName="dev";
		if(BuildConfig.FLAVOR.contains("dev"))
			containerName= "dev";
		else if(BuildConfig.FLAVOR.contains("production"))
		containerName= "prod";
			else
		containerName= "uat";

		String dirName = "reapp/static/DIY-Videos", fileName = "diyVideos.json";
        try {
            containerName = REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_CONTAINER).length() == 0 ? containerName
                    : REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_CONTAINER);
            dirName = REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_DIRNAME).length() == 0 ? dirName
                    : REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_DIRNAME);
            fileName = REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_FILENAME).length() == 0 ? fileName
                    : REPreference.getInstance().getString(REApplication.getAppContext(), KEY_DIY_FILENAME);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        mDoItYourselfPresenter.downloadFileFromAzure(containerName, dirName, fileName);
    }

    @Override
    public void onVideosFetchSuccess(String mVideosData) {
        populateBikeListSpinner(mVideosData);
    }
}
