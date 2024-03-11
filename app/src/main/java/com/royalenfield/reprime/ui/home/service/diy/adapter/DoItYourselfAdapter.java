package com.royalenfield.reprime.ui.home.service.diy.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.service.diy.view.DoItYourSelfRowView;
import com.royalenfield.reprime.models.request.diyyoutube.YoutubeVideoModel;
import com.royalenfield.reprime.ui.home.service.diy.presenter.DoItYourselfPresenter;
import com.royalenfield.reprime.ui.home.service.diy.presenter.DoItYourselfRowPresenter;
import com.royalenfield.reprime.ui.home.service.search.view.ItemClickListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoItYourselfAdapter extends RecyclerView.Adapter<DoItYourselfAdapter.YoutubeViewHolder> {
    private static final String TAG = DoItYourselfAdapter.class.getSimpleName();
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;
    private ItemClickListener mItemClickListener;
    private WeakReference<ItemClickListener> listenerRef;
    private DoItYourselfRowPresenter mDoItYourselfRowPresenter;
    private Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;

    public DoItYourselfAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, DoItYourselfRowPresenter mDoItYourselfRowPresenter, ItemClickListener mItemClickListener, DoItYourselfPresenter mDoItYourselfPresenter) {
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.mDoItYourselfRowPresenter = mDoItYourselfRowPresenter;
        this.mItemClickListener = mItemClickListener;
        thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.do_it_yourself_adapter, parent, false);
        return new YoutubeViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeViewHolder holder, int position) {
        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(position);
        mDoItYourselfRowPresenter.onBindDoItYourselfRow(position, holder, youtubeVideoModelArrayList);
    }

    @Override
    public int getItemCount() {
        return mDoItYourselfRowPresenter.getItemCount(youtubeVideoModelArrayList);
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    public class YoutubeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DoItYourSelfRowView {
        public YouTubeThumbnailView videoThumbnailImageView;
        public TextView videoTitle, likes;
        public ImageView mImageButton;
        ProgressBar mProgressBar;

        public YoutubeViewHolder(View itemView, ItemClickListener mItemClickListener) {
            super(itemView);
            listenerRef = new WeakReference<>(mItemClickListener);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            videoTitle = itemView.findViewById(R.id.video_title_label);
            mImageButton = itemView.findViewById(R.id.playButton);
            likes = itemView.findViewById(R.id.likes);
            mProgressBar = itemView.findViewById(R.id.progressYoutube);
            mImageButton.setOnClickListener(this);
            // mItemClickListner.onItemClick(itemView, getAdapterPosition());
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == mImageButton.getId()) {
                listenerRef.get().onItemClick(v, getAdapterPosition(), null);
            }
        }

        @Override
        public void setVideoTitle(String mVideoTitle) {
            videoTitle.setText(mVideoTitle);
        }

        @Override
        public void setLikes(String mLikes) {
            likes.setText(mLikes);
        }

        @Override
        public void setProgressBar(int i) {
            mProgressBar.setVisibility(i);
        }

        @Override
        public void setLikesVisible(int i) {
            likes.setVisibility(i);
        }

        @Override
        public void setVideoTitleVisible(int i) {
            videoTitle.setVisibility(i);
        }

        @Override
        public void setImageBtnVisible(int i) {
            mImageButton.setVisibility(i);
        }

        @Override
        public void initializeYoutubeThumbNailView(final YoutubeVideoModel mYoutubeVideoModel) {
            try {
                videoThumbnailImageView.initialize(REUtils.getREGoogleKeys().getYoutubeaPIKey(), new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        //when initialization is sucess, set the video id to thumbnail to load
                        thumbnailViewToLoaderMap.put(youTubeThumbnailView, youTubeThumbnailLoader);
                        youTubeThumbnailLoader.setVideo(mYoutubeVideoModel.getVideoId());
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                                videoTitle.setVisibility(View.VISIBLE);
                                likes.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                                mImageButton.setVisibility(View.VISIBLE);
                                youTubeThumbnailLoader.release();
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                                //print or show error when thumbnail load failed
                                videoTitle.setVisibility(View.VISIBLE);
                                likes.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                                mImageButton.setVisibility(View.VISIBLE);
                                Log.e(TAG, "Youtube Thumbnail Error");
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        //print or show error when initialization failed
                        mProgressBar.setVisibility(View.GONE);
                        mImageButton.setVisibility(View.VISIBLE);
                        videoTitle.setVisibility(View.VISIBLE);
                        likes.setVisibility(View.VISIBLE);
                        Log.e(TAG, "Youtube Initialization Failure");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Youtube Thumbnail Initialization Failure");
            }
        }
    }
}
