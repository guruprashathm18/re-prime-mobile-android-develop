package com.royalenfield.reprime.ui.home.rides.custom;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.web.dealerride.Dealer;
import com.royalenfield.reprime.ui.home.rides.views.DealerRidesListView;

/**
 * @author BOP1KOR on 3/26/2019.
 * <p>
 * View Holder class DealerRides objects.
 */
public class DealerRidesItemHolder extends RecyclerView.ViewHolder {
    private TextView tvDealerName;
    private TextView tvDealerRideName;
    private TextView tvDealerRideLocationName;
    private ImageView tvDealerPicture;

    public DealerRidesItemHolder(View itemView) {
        super(itemView);
        tvDealerName = itemView.findViewById(R.id.tv_dealer_name);
        tvDealerRideName = itemView.findViewById(R.id.tv_dealer_ride_name);
        tvDealerRideLocationName = itemView.findViewById(R.id.tv_dealer_ride_location_address);
        tvDealerPicture = itemView.findViewById(R.id.iv_dealer_thumbnail);
    }

    /**
     * Returns a view for use in the {@link DealerRidesListView}.
     *
     * @param context     The context.
     * @param holder      {@link DealerRidesItemHolder} object.
     * @param dealerRides The {@link Dealer} object.
     */
    public static void setupItem(Context context, DealerRidesItemHolder holder, Dealer dealerRides) {
        holder.tvDealerPicture.setBackgroundResource(R.drawable.dealer_rides);
        holder.tvDealerName.setText(dealerRides.getCompanyName());
        holder.tvDealerRideName.setText(dealerRides.getRideName());
        holder.tvDealerRideLocationName.setText(dealerRides.getEndPoint().getName());
    }
}
