package com.royalenfield.reprime.ui.riderprofile.presenter;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import androidx.core.content.res.ResourcesCompat;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.riderprofile.views.CustomRidesRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * This is presenter for binding rows in a recyclerview
 */
public class CustomRidesRowPresenter implements ICustomRidesRowPresenter {

    private static final String TAG = CustomRidesRowPresenter.class.getSimpleName();
    private List<MarqueeRidesResponse> mMarqueeRidesResponse;


    @Override
    public void onBindPreviousRideRowViewAtPosition(int position, CustomRidesRowView rowView, String type) {
        try {
            //Logic is written base on type
            switch (type) {
                case REConstants.PASTRIDE_TYPE:
                    rowView.setRideLogoImage(null);
                    rowView.setRideOwner(null);
                    break;
                case REConstants.BOOKMARKS_TYPE:
                    rowView.setRideLogoImage("image");
                    rowView.setRideOwner("Akash");
                    break;
                case REConstants.MARQUEE_RIDE:
                    String mTextNA = REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na);
                    mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
                    String mRideDetail = mMarqueeRidesResponse.get(position).getTitle();
                    String mRideDate = String.valueOf(SpannableStringBuilder.valueOf(mMarqueeRidesResponse.get(position).getDuration()));
                    rowView.setRideDetail(mRideDetail != null && !mRideDetail.isEmpty() ? mRideDetail : mTextNA);
                    rowView.setRideImage(REUtils.getMobileappbaseURLs().getAssetsURL()
                            + mMarqueeRidesResponse.get(position).getThumbnailImagePath());
                    rowView.setRideLogoImage(REUtils.getMobileappbaseURLs().getAssetsURL()
                            + mMarqueeRidesResponse.get(position).getIconLogoImagePath());
                    rowView.setRideDate(setRideDuration(position));
                    break;
                default:
                    break;


            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private SpannableStringBuilder setRideDuration(int position) {
        Typeface mTypeFaceBold = ResourcesCompat.getFont(REApplication.getAppContext(), R.font.montserrat_bold);
        Typeface mTypeFaceLight = ResourcesCompat.getFont(REApplication.getAppContext(), R.font.montserrat_light);
        String[] duration = mMarqueeRidesResponse.get(position).getDuration().split(" ");
        String num = duration[0] + " ";
        String days = duration[1];
        SpannableStringBuilder spannable = new SpannableStringBuilder(num + days);
        spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 0, num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceLight)), num.length(), num.length() + days.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public int getRidesRowCount(int count) {
        return count;
    }


}
