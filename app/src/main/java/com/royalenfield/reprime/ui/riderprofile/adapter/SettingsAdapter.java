package com.royalenfield.reprime.ui.riderprofile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.homescreen.view.CustomerCareActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.AppSettingActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ChangePasswordActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ContactUsActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ConsentUpdateActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ContactUsActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.SettingsActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import static com.royalenfield.reprime.utils.REConstants.ADAPTER_POSITION;
import static com.royalenfield.reprime.utils.REConstants.INPUT_PROFILE_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.INPUT_SPLASH_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.KEY_ACCOUNT_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SETTINGS_GTM;
import static com.royalenfield.reprime.utils.REConstants.OUR_WORLD_EVENTS;
import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REGISTRATION_POLICIES_TYPE;

/**
 * @author BOP1KOR on 3/4/2019.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    //Settings option list.
    private String[] mSettingsOptionList;
    private Context mContext;
    private Activity mActivity;
    private String mInputType;
    private int mPosition;
    private Intent intent;

    //Provides a suitable constructor (depends on the kind of data set)
    public SettingsAdapter(Context context, Activity activity, String[] settingsOptionList, String type) {
        mContext = context;
        mActivity = activity;
        mInputType = type;
        mSettingsOptionList = settingsOptionList;
    }

    public SettingsAdapter(Context context, Activity activity, int position, String type) {
        mContext = context;
        mActivity = activity;
        mPosition = position;
        mInputType = type;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create a new View
        View optionRowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_option_list, parent, false);
        return new SettingsViewHolder(optionRowView);
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
//        // - get element from your data set at this position
//        // - replace the contents of the view with that element
//        holder.mSettingsOptionView.setText(mSettingsOptionList[position]);
        if (mInputType != null) {
            switch (mInputType) {
                case RIDE_TYPE_POPULAR:
                    holder.mSettingsOptionView.setText(RERidesModelStore.getInstance().getPopularRidesResponse().
                            get(mPosition).getPolicyUrls().get(position).get(REGISTRATION_POLICIES_TYPE));
                    break;
                case RIDE_TYPE_MARQUEE:
                    holder.mSettingsOptionView.setText(RERidesModelStore.getInstance().getMarqueeRidesResponse().
                            get(mPosition).getPolicyUrls().get(position).get(REGISTRATION_POLICIES_TYPE));
                    break;
                case INPUT_SPLASH_ACTIVITY:
                case INPUT_PROFILE_ACTIVITY:
                    holder.mSettingsOptionView.setText(mSettingsOptionList[position]);
                    break;
                case OUR_WORLD_EVENTS:
                    holder.mSettingsOptionView.setText(RERidesModelStore.getInstance().getOurWorldEventsResponse().
                            get(mPosition).getPolicyUrls().get(position).get(REGISTRATION_POLICIES_TYPE));
                    break;
            }
        }
    }

    /**
     * Return the size of your data set (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        int count = 0;
        switch (mInputType) {
            case RIDE_TYPE_POPULAR:
                count = RERidesModelStore.getInstance().getPopularRidesResponse().get(mPosition).getPolicyUrls().size();
                break;
            case RIDE_TYPE_MARQUEE:
                count = RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mPosition).getPolicyUrls().size();
                break;
            case INPUT_SPLASH_ACTIVITY:
            case INPUT_PROFILE_ACTIVITY:
                count = mSettingsOptionList.length;
                break;
            case OUR_WORLD_EVENTS:
                count = RERidesModelStore.getInstance().getOurWorldEventsResponse().get(mPosition).getPolicyUrls().size();
                break;
        }
        return count;
    }

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSettingsOptionView;

        SettingsViewHolder(View itemView) {
            super(itemView);
            mSettingsOptionView = itemView.findViewById(R.id.tv_settings_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (mInputType) {
                case REConstants.INPUT_SPLASH_ACTIVITY:
                case REConstants.INPUT_PROFILE_ACTIVITY:
                    String[] array= mContext.getResources().getStringArray(R.array.list_settings);
                    String[] consentarray= mContext.getResources().getStringArray(R.array.consent_array);
                    //Opening the corresponding activities depending on position
                    if (getAdapterPosition() == 0) {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Settings");
                        params.putString("eventAction", mContext.getResources().getString(R.string.text_terms_and_conditions)+" clicked");
                        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
                        intent = new Intent(mContext, REWebViewActivity.class);
                        intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, mContext.getResources().getString(R.string.text_terms_and_conditions).toUpperCase());
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);

                    } else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(array[1])) {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Settings");
                        params.putString("eventAction", "Faq clicked");
                        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
                        intent = new Intent(mContext, REWebViewActivity.class);
                        intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, mContext.getResources().getString(R.string.text_faqs));
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    }
//					else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(array[2])) {
//						Bundle params = new Bundle();
//						params.putString("eventCategory", "Settings");
//						params.putString("eventAction", "Change Password clicked");
//						REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
//						intent = new Intent(mContext, ChangePasswordActivity.class);
//						mActivity.startActivity(intent);
//						mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
//					}
                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(consentarray[0])) {

                        if (REApplication.getInstance().mFirebaseAuth != null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {

                            intent = new Intent(mContext, ConsentUpdateActivity.class);
                            mActivity.startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                        }
                        else{
							String txt;
							if (REUtils.CHECK_FIREBASE_AUTH_INPROGRESS)
								txt=mContext.getResources().getString(R.string.firebase_auth_progress_fetch_data);
							else
								txt=mContext.getResources().getString(R.string.firebase_auth_error);
                            REUtils.showErrorDialog(mContext,txt);

                        }
                    }
//                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(array[3])) {
//                        intent = new Intent(mContext, AppSettingActivity.class);
//                        mActivity.startActivity(intent);
//                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
//
//                    }
//                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(array[4])) {
//                        intent = new Intent(mContext, ContactUsActivity.class);
//                        mActivity.startActivity(intent);
//                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
//
//                    }
                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(consentarray[1])) {
                        ( (SettingsActivity) mActivity).forgotMeConfirmtion();
                    }
                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(mActivity.getResources().getString(R.string.app_setting))) {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Settings");
                        params.putString("eventAction", "App Settings clicked");
                        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
                        intent = new Intent(mContext, AppSettingActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    }
                    else if (mSettingsOptionList[getAdapterPosition()].equalsIgnoreCase(mActivity.getResources().getString(R.string.text_contact_us))) {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Settings");
                        params.putString("eventAction", "Contact Us clicked");
                        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
                        intent = new Intent(mContext, CustomerCareActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    }

                    break;
                case RIDE_TYPE_POPULAR:
                    openWebView(RIDE_TYPE_POPULAR);
                    break;
                case RIDE_TYPE_MARQUEE:
                    openWebView(RIDE_TYPE_MARQUEE);
                    break;
                case OUR_WORLD_EVENTS:
                    openWebView(OUR_WORLD_EVENTS);
                default:
                    break;
            }
        }

        private void openWebView(String rideTypeMarquee) {
            Intent mIntent = new Intent(mContext, REWebViewActivity.class);
            mIntent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, rideTypeMarquee);
            mIntent.putExtra(RIDES_LIST_POSITION, mPosition);
            mIntent.putExtra(ADAPTER_POSITION, getAdapterPosition());
            mActivity.startActivity(mIntent);
            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        }
    }
}
