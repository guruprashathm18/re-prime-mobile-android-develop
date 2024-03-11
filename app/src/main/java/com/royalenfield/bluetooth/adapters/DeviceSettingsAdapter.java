package com.royalenfield.bluetooth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.bluetooth.models.DeviceSettingsItems;
import com.royalenfield.bluetooth.views.DeviceSettingsRowView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.rides.adapter.SearchPlacesListAdapter;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

public class DeviceSettingsAdapter extends RecyclerView.Adapter<DeviceSettingsAdapter.DeviceSettingsHolder> {

    private final Context mContext;
    private List<DeviceSettingsItems> mItemsList;
    //Listener for individual item click events.
    private OnItemClickListener onItemClickListener;


    public DeviceSettingsAdapter(Context context, List<DeviceSettingsItems> aItemsList,
                                 OnItemClickListener clickListener) {
        this.mContext = context;
        mItemsList = aItemsList;
        setOnItemClickListener(clickListener);
    }

    /**
     * Setter for {@link SearchPlacesListAdapter.OnItemClickListener} instance.
     *
     * @param onItemClickListener {@link SearchPlacesListAdapter.OnItemClickListener}
     */
    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DeviceSettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble_device_settings, parent, false);
        return new DeviceSettingsAdapter.DeviceSettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceSettingsHolder deviceSettingsHolder, int position) {
        try {
            if (mItemsList != null && mItemsList.size() > 0) {
                DeviceSettingsItems settingItems = mItemsList.get(position);
                deviceSettingsHolder.setItem(settingItems.getItemName());
                if (settingItems.getItemName().equalsIgnoreCase(mContext.getString(R.string.text_software_update))) {
                    if (settingItems.getSoftwareUpdate().length() > 0) {
                        deviceSettingsHolder.mSoftBadgeCountTXT.setVisibility(View.VISIBLE);
                        deviceSettingsHolder.setBadgeCount(settingItems.getSoftwareUpdate());
                    } else
                        deviceSettingsHolder.mSoftBadgeCountTXT.setVisibility(View.INVISIBLE);
                } else {
                    deviceSettingsHolder.mSoftBadgeCountTXT.setVisibility(View.INVISIBLE);
                }
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    @Override
    public int getItemCount() {
        if (mItemsList != null && mItemsList.size() > 0) {
            return mItemsList.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int itemId, String item);
    }

    /**
     * A Simple ViewHolder for the DeviceSettingsHolder RecyclerView
     */
    public class DeviceSettingsHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            DeviceSettingsRowView {

        TextView mItemTXT, mSoftBadgeCountTXT;
        ImageButton mSelectionIMG;

        private DeviceSettingsHolder(final View itemView) {
            super(itemView);
            mItemTXT = itemView.findViewById(R.id.setting_item_TXT);
            mSelectionIMG = itemView.findViewById(R.id.setting_item_arrow_IMG);
            mSoftBadgeCountTXT = itemView.findViewById(R.id.software_badge_count_TXT);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                DeviceSettingsItems deviceSettingsItems = mItemsList.get(getAdapterPosition());
                if (deviceSettingsItems.getItemName().equalsIgnoreCase(mContext.getString(R.string.text_software_update))) {
                    if (deviceSettingsItems.getSoftwareUpdate().length() > 0) {
                        onItemClickListener.onItemClick(deviceSettingsItems.getItemId(),
                                deviceSettingsItems.getItemName());
                    }
                } else {
                    onItemClickListener.onItemClick(deviceSettingsItems.getItemId(),
                            deviceSettingsItems.getItemName());
                }
            }
        }

        @Override
        public void setItem(String item) {
            mItemTXT.setText(item);
        }

        @Override
        public void setBadgeCount(String count) {
            mSoftBadgeCountTXT.setText(count);
        }
    }
}
