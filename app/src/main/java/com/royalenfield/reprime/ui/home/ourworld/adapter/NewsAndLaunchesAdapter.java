package com.royalenfield.reprime.ui.home.ourworld.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

public class NewsAndLaunchesAdapter extends RecyclerView.Adapter<NewsAndLaunchesAdapter.NewsAndLaunchesViewHolder> {

    private List<NewsResponse> mNewsResponse;
    private Activity mContext;
    private final OnItemClickListener listener;

    public NewsAndLaunchesAdapter(Activity context, List<NewsResponse> newsResponse, OnItemClickListener listener) {
        this.mContext = context;
        this.mNewsResponse = newsResponse;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsAndLaunchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_and_launches,
                parent, false);
        return new NewsAndLaunchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAndLaunchesViewHolder holder, int position) {
        holder.launchName.setText(mNewsResponse.get(position).getNewsTitle());
        String newsThumbnailImageUrl = mNewsResponse.get(position).getThumbnailImageSrc();
        REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + newsThumbnailImageUrl,
                holder.launchImage);
    }

    @Override
    public int getItemCount() {
        if (mNewsResponse != null && mNewsResponse.size() > 0) {
            return mNewsResponse.size();
        }
        return 0;
    }

    class NewsAndLaunchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView launchName;
        ImageView launchImage;
        FrameLayout frameLayout;

        private NewsAndLaunchesViewHolder(View itemView) {
            super(itemView);
            launchName = itemView.findViewById(R.id.launch_name);
            launchImage = itemView.findViewById(R.id.launch_image);
            frameLayout = itemView.findViewById(R.id.news_gradient);
            frameLayout.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onNewsItemClick(mNewsResponse.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onNewsItemClick(NewsResponse position);
    }

}
