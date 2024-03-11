package com.royalenfield.reprime.ui.motorcyclehealthalert.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.GetAllAlertsByUniqueId;
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.HealthAlertModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.w3c.dom.Text;
import com.royalenfield.reprime.utils.REUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import static com.facebook.FacebookSdk.getApplicationContext;
import static com.royalenfield.reprime.base.REBaseActivity.CALL_PERMISSIONS_REQUESTS;
import static com.royalenfield.reprime.ui.motorcyclehealthalert.models.HealthAlertModel.ALERT_TYPE;
import static com.royalenfield.reprime.ui.motorcyclehealthalert.models.HealthAlertModel.ATTENTION_ALERT_TYPE;

public class VehicleHealthAlertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final REBaseActivity baseActivity;
    private List<GetAllAlertsByUniqueId> mList;
    private  String modelName;

    public VehicleHealthAlertAdapter(Context context, List<GetAllAlertsByUniqueId> list,String modelName) {
        this.mList = list;
        this.mContext = context;
        this.baseActivity = (REBaseActivity) context;
        this.modelName=modelName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ATTENTION_ALERT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attention_vehicle_health_alert, parent, false);
                return new AttentionHealthAlertViewHolder(view);
            case ALERT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_health_alert, parent, false);
                return new HealthAlertViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GetAllAlertsByUniqueId object = mList.get(position);
		String timeSTamp=object.getFromTs();
		if(object.getAlerttype()!=null&&object.getAlerttype().equalsIgnoreCase("ignition")){
			timeSTamp=object.getToTs();
		}
        String time = REUtils.getFormattedTime(timeSTamp);
        String date = REUtils.getDate(timeSTamp);
        String month = REUtils.getMonth(timeSTamp);
        int colorResource;
        switch (getItemViewType(position)) {

            case ATTENTION_ALERT_TYPE:
                ((AttentionHealthAlertViewHolder) holder).mTitle.setText(object.getAlertName());
                ((AttentionHealthAlertViewHolder) holder).mDescription.setText(object.getAlertDescription());
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((AttentionHealthAlertViewHolder) holder).cardView.getLayoutParams();
                ((AttentionHealthAlertViewHolder) holder).leftBorder.getLayoutParams().height = layoutParams.height;
                ((AttentionHealthAlertViewHolder) holder).txtDate.setText(date);
                ((AttentionHealthAlertViewHolder) holder).txtMonth.setText(month);
                ((AttentionHealthAlertViewHolder) holder).txtTime.setText(time);

                /*set color according type*/

if(object.getAlertPriority()==null){
    ((AttentionHealthAlertViewHolder) holder).leftBorder.setVisibility(View.INVISIBLE);
    ((AttentionHealthAlertViewHolder) holder).rsaIcon.setVisibility(View.INVISIBLE);
}
else{
    ((AttentionHealthAlertViewHolder) holder).leftBorder.setVisibility(View.VISIBLE);

    if (object.getAlertPriority()!=null&&object.getAlertPriority().equalsIgnoreCase("D0")) {
        ((AttentionHealthAlertViewHolder) holder).rsaIcon.setVisibility(View.VISIBLE);
    }
    else
        ((AttentionHealthAlertViewHolder) holder).rsaIcon.setVisibility(View.INVISIBLE);
    colorResource = mContext.getResources().getColor(R.color.attention_alert_left_header_color);
    if (object.getAlertPriority() != null && object.getAlertPriority().equalsIgnoreCase("D0"))
            colorResource = mContext.getResources().getColor(R.color.attention_alert_left_header_color);
        else if (object.getAlertPriority() != null && object.getAlertPriority().equalsIgnoreCase("D1"))
            colorResource = mContext.getResources().getColor(R.color.attention_alert_left_header_color1);
        else if(object.getAlertPriority() != null && object.getAlertPriority().equalsIgnoreCase("D2"))
            ((AttentionHealthAlertViewHolder) holder).leftBorder.setVisibility(View.INVISIBLE);
else
        ((AttentionHealthAlertViewHolder) holder).leftBorder.setVisibility(View.INVISIBLE);

    ((AttentionHealthAlertViewHolder) holder).rsaIcon.setColorFilter(colorResource, PorterDuff.Mode.SRC_IN);
        ((AttentionHealthAlertViewHolder) holder).leftBorder.setBackgroundColor(colorResource);



}
if(object.getAlertCompleted()!=null){
    if(object.getAlertCompleted()){
        colorResource = mContext.getResources().getColor(R.color.attention_alert_left_header_color2);
       ((AttentionHealthAlertViewHolder) holder).leftBorder.setVisibility(View.VISIBLE);
        ((AttentionHealthAlertViewHolder) holder).leftBorder.setBackgroundColor(colorResource);
        ((AttentionHealthAlertViewHolder) holder).rsaIcon.setVisibility(View.INVISIBLE);
    }
   }

                ((AttentionHealthAlertViewHolder) holder).rsaIcon.setOnClickListener(view -> {
                    callingRSA(object.getAlertName());
                });

                break;
            case ALERT_TYPE:
                ((HealthAlertViewHolder) holder).mTitle.setText(object.getAlertName());
                ((HealthAlertViewHolder) holder).mDescription.setText(object.getAlertDescription());
                ((HealthAlertViewHolder) holder).txtMonth.setText(month);
                ((HealthAlertViewHolder) holder).txtDate.setText(date);
                ((HealthAlertViewHolder) holder).txtTime.setText(time);
                ((HealthAlertViewHolder) holder).leftBorder.setVisibility(View.INVISIBLE);
                if(object.getAlertCompleted()!=null){
                    if(object.getAlertCompleted()){
                        colorResource = mContext.getResources().getColor(R.color.attention_alert_left_header_color2);
                        ((HealthAlertViewHolder) holder).leftBorder.setVisibility(View.VISIBLE);
                        ((HealthAlertViewHolder) holder).leftBorder.setBackgroundColor(colorResource);
                    }
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            GetAllAlertsByUniqueId object = mList.get(position);
            if (object != null&&object.getAlertPriority()!=null) {
                if (object.getAlertPriority().equals("P0") || object.getAlertPriority().equals("P1")|| object.getAlertPriority().equals("P2"))
                        return ATTENTION_ALERT_TYPE;

            }
        }
        return 0;
    }

    public static class AttentionHealthAlertViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate;
        private TextView mTitle;
        private TextView mDescription;
        private View leftBorder;
        private ImageView rsaIcon;
        private TextView txtType;
        private CardView cardView;
        private TextView txtMonth;
        private TextView txtTime;

        AttentionHealthAlertViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.header);
            mDescription = itemView.findViewById(R.id.body);
            leftBorder = itemView.findViewById(R.id.left_border);
            rsaIcon = itemView.findViewById(R.id.rsa);
            cardView = itemView.findViewById(R.id.card_view);
            txtMonth = itemView.findViewById(R.id.txt_month);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }

    public static class HealthAlertViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private TextView txtMonth;
        private TextView txtTime;
        private TextView txtDate;
        private View leftBorder;

        HealthAlertViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.header);
            mDescription = itemView.findViewById(R.id.body);
            txtMonth = itemView.findViewById(R.id.txt_month);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtTime = itemView.findViewById(R.id.txt_time);
            leftBorder=itemView.findViewById(R.id.left_border);
        }
    }

    public void setVehicleData(List<GetAllAlertsByUniqueId> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    public void resetVehicleData(List<GetAllAlertsByUniqueId> list) {
        this.mList.clear();
        notifyDataSetChanged();
    }

    private void callingRSA(String title) {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Connected Module");
        params.putString("eventAction", "Vehicle Alerts");
        params.putString("eventLabel", "RSA "+title);
        params.putString("modelName",modelName);
        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
		String callingNumber= REUtils.getConnectedFeatureKeys().getCustomerCare();
		if(callingNumber==null||callingNumber.isEmpty()){
			callingNumber= getApplicationContext().getString(R.string.connectedCustomCare);
		}
        baseActivity.checkAndRequestCallPermissions(getApplicationContext(), baseActivity,
				callingNumber, CALL_PERMISSIONS_REQUESTS, baseActivity);
    }
}