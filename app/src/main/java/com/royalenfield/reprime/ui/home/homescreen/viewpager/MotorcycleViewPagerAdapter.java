package com.royalenfield.reprime.ui.home.homescreen.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MotorcycleViewPagerAdapter extends PagerAdapter {

    private final View.OnClickListener clickListener;
    private Context mContext;
    private List<VehicleDataModel> vehicleDataList;
    private TextView txtConnect;
    private ImageView imgMotorcycle;
    private VehicleDataModel vehicleData;

    public MotorcycleViewPagerAdapter(Context context, List<VehicleDataModel> vehicleList, View.OnClickListener clickListener) {
        mContext = context;
        vehicleDataList = vehicleList;
        this.clickListener = clickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        vehicleData = vehicleDataList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_motorcycle_page, collection, false);
        setDataToLayout(layout);
        collection.addView(layout);
        return layout;

    }

    private void setDataToLayout(ViewGroup layout) {
        imgMotorcycle = layout.findViewById(R.id.imgMotorcycle);
        REUtils.getFirebaseReactUrl(layout.getContext(), vehicleData.getModelCode(), imgMotorcycle, null,true,null);
        txtConnect = layout.findViewById(R.id.txtConnect);
        txtConnect.setOnClickListener(clickListener);

        if (vehicleData.getImageUrl() != null) {
         //   setImage(vehicleData.getImageUrl());
        }
        imgMotorcycle.setOnClickListener(clickListener);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return vehicleDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void showConnectButton(boolean value) {
        txtConnect.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public void updateVehicleDataList(List<VehicleDataModel> updatedList) {
        vehicleDataList = updatedList;
    }

    public void setImage(String imagePath) {
       /* if (imgMotorcycle != null) {
            Glide.with(mContext)
                    .load(imagePath)
                    .placeholder(R.drawable.placeholder_re)
                    .fitCenter()
                    .into(imgMotorcycle);
        }*/
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
