package com.royalenfield.reprime.ui.home.ourworld.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.utils.CustomTypefaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private List<EventsResponse> mEventsResponse;
    private Activity mContext;
    private String mLightweightpageURL;
    private final OnItemClickListener listener;

    public EventsAdapter(Activity context, List<EventsResponse> eventsResponse, OnItemClickListener listener) {
        this.mEventsResponse = eventsResponse;
        this.mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_events, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        String eventsThumbnailImageUrl = mEventsResponse.get(position).getThumbnailImageSrc();
        REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + eventsThumbnailImageUrl, holder.eventImage);
        holder.eventName.setText(mEventsResponse.get(position).getTitle());
        formatDateForBikesJoined(position, holder.eventDate);
    }

    @Override
    public int getItemCount() {
        if (mEventsResponse != null && mEventsResponse.size() > 0) {
            return mEventsResponse.size();
        }
        return 0;
    }

    private void formatDateForBikesJoined(int position, TextView eventDate) {
        Typeface fontLight = ResourcesCompat.getFont(mContext, R.font.montserrat_light);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mEventsResponse.get(position).getStartDate());
        stringBuilder.append(" - ");
        stringBuilder.append(mEventsResponse.get(position).getEndDate());
        if (mEventsResponse.get(position).getRidersLimit() != null && mEventsResponse.get(position).getRidersLimit().length() > 0) {
            stringBuilder.append(" | ");
            stringBuilder.append(mEventsResponse.get(position).getRidersLimit());
            stringBuilder.append(" ");
            stringBuilder.append(mContext.getResources().getString(R.string.bikers_registered));

            stringBuilder.setSpan(new CustomTypefaceSpan("", fontLight), stringBuilder.length() -
                            mContext.getResources().getString(R.string.bikers_registered).length(),
                    stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        eventDate.setText(stringBuilder);

    }

    class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView eventName;
        TextView eventDate;
        TextView explore;
        Button register;
        ImageView eventImage;
        FrameLayout frameLayout;

        private EventsViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            explore = itemView.findViewById(R.id.explore);
            register = itemView.findViewById(R.id.button_register);
            register.setOnClickListener(this);
            eventImage = itemView.findViewById(R.id.event_image);
            frameLayout = itemView.findViewById(R.id.event_gradient);
            frameLayout.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                //Intent intent = null;
                if (v.getId() == R.id.button_register) {
                    mLightweightpageURL = mEventsResponse.get(getAdapterPosition()).getLightWeightPageUrl();
                    if (mLightweightpageURL != null && !mLightweightpageURL.isEmpty()) {
                        //intent = new Intent(mContext, RidesLightWeightWebActivity.class);
                        listener.onEventsRegisterClick(getAdapterPosition(),mEventsResponse.get(getAdapterPosition()));
                    }
                } else {
                    //intent = new Intent(mContext, RidesTourActivity.class);
                    listener.onEventsDetailsClick(getAdapterPosition(),mEventsResponse.get(getAdapterPosition()));

                }
                /*if (intent != null) {
                    intent.putExtra(RIDE_TYPE, REConstants.OUR_WORLD_EVENTS);
                    intent.putExtra(RIDES_LIST_POSITION, getAdapterPosition());
                    mContext.startActivity(intent);
                    mContext.overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                }*/
            } catch (IndexOutOfBoundsException e) {
                RELog.e(e);
            } catch (Exception e) {
                RELog.e(e);
            }

        }
    }

    public interface OnItemClickListener {
        void onEventsRegisterClick(int  position, EventsResponse eventsResponse);

        void onEventsDetailsClick(int  position, EventsResponse eventsResponse);
    }

}
