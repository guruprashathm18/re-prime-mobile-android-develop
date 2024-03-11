package com.royalenfield.reprime.ui.home.service.diy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.REUtils;

/**
 * YoutubePlayerActivity contains youTubePlayerView to play youtube videos with play & pause button.
 */
public class YoutubePlayerActivity extends YouTubeBaseActivity implements TitleBarView.OnNavigationIconClickListener,
		INetworkStateListener {
	private static final String TAG = YoutubePlayerActivity.class.getSimpleName();
	TitleBarView mTitleBarView;
	private String videoID, title, likes;
	// private YouTubePlayerView youTubePlayerView;
	private WebView youTubePlayerView;
	private Dialog mNoInternetDialog;
	FrameLayout frameLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtube_player_view);
		Button bookServiceBtn = findViewById(R.id.bookService);
		frameLayout = findViewById(R.id.youTubeFrameLayout);
		videoID = getIntent().getStringExtra("video_id");
		title = getIntent().getStringExtra("title");
		likes = getIntent().getStringExtra("likes");
		// youTubePlayerView = findViewById(R.id.youTubePlayerView);
		youTubePlayerView = findViewById(R.id.youTubePlayerView);
		mTitleBarView = findViewById(R.id.custom_topbar);
		TextView titleView = findViewById(R.id.title);
		TextView viewlikes = findViewById(R.id.likes);
		mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.play_video));


		// initializeYoutubePlayer();
		if (videoID != null) {
			initializeYoutubePlayerWeb();
		}
		if (title != null) {
			titleView.setText(title);
		}
		if (likes != null) {
			viewlikes.setText(likes + " Likes");
		}


		bookServiceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(YoutubePlayerActivity.this, REHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
			}
		});
	}

	private void initializeYoutubePlayerWeb() {

		String myYouTubeVideoUrl = videoID;
		String dataUrl = "<html>" +
				"<body>" +
				"<iframe width=\"340\" height=\"315\" src=\"https://www.youtube.com/embed/" + myYouTubeVideoUrl + "\" frameborder=\"0\" allowfullscreen/>" +
				"</body>" +
				"</html>";


		WebSettings webSettings = youTubePlayerView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		youTubePlayerView.setPadding(0, 0, 0, 0);
		youTubePlayerView.setInitialScale(getScale());

		youTubePlayerView.loadData(dataUrl, "text/html", "utf-8");
		youTubePlayerView.setWebChromeClient(new CustomWebChromeClient(this));
		youTubePlayerView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});

	}

	private int getScale() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width) / new Double(360);
		val = val * 100d;
		return val.intValue();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause Called");
		REApplication.getInstance().setIsAppInForeground(false);
		//Unregister network state change listener.
		REApplication.getInstance().setNetworkChangeListener(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume Called");
		REApplication.getInstance().setIsAppInForeground(true);
		//Register for network state change.
		REApplication.getInstance().setNetworkChangeListener(this);
		//Checks the network state when activity is resumed
		if (REUtils.isNetworkAvailable(this)) {
			onNetworkConnect();
		} else {
			onNetworkDisconnect();
		}
	}



	/**
	 * Hides the no internet dialog
	 */
	private void hideNoInternetDialog() {
		if (mNoInternetDialog != null) {
			mNoInternetDialog.cancel();
		}
	}


	@Override
	public void onNavigationIconClick() {
		this.finish();
		overridePendingTransition(0, R.anim.slide_out_right);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
	}

	@Override
	public void onNetworkConnect() {
		hideNoInternetDialog();
	}

	@Override
	public void onNetworkDisconnect() {
		hideNoInternetDialog();
		mNoInternetDialog = REDialogUtils.showNoInternetDialog(this);
		mNoInternetDialog.show();
	}
}


