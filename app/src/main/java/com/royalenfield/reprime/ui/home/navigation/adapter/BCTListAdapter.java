package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.micro.tenzing.client.model.TripMetadata;
import com.royalenfield.reprime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter creates views for the user created rides items and binds the data to the views.
 */
public class BCTListAdapter extends RecyclerView.Adapter<BCTListAdapter.POIViewHolder> {

    private List<TripMetadata> tripMetadata;
    private BCTTripListener bctTripListener;

    public BCTListAdapter(List<TripMetadata> list, BCTTripListener tripListener) {
        this.tripMetadata = list;
        this.bctTripListener = tripListener;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent,
                false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        holder.tvTripName.setText(tripMetadata.get(position).getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = sdf.parse(tripMetadata.get(position).getCreated().toString());
            String date_val =  sdf1.format(date);
        holder.tvTripDate.setText(date_val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvTripDesc.setText(tripMetadata.get(position).getDescription());
        if(tripMetadata.get(position).getVisibility().getValue() != null &&
                tripMetadata.get(position).getVisibility().getValue().equalsIgnoreCase("Public")) {
            holder.tvDistance.setText("Public");
        }else{
            holder.tvDistance.setText("Private");
        }
    }

    @Override
    public int getItemCount() {
        return tripMetadata.size();
    }
    class POIViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTripName, tvDistance,tvTripDate,tvTripDesc;
        ImageView ivDelete;

        POIViewHolder(View itemView) {
            super(itemView);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvTripName = itemView.findViewById(R.id.tv_title);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvTripDate = itemView.findViewById(R.id.tv_trip_date);
            tvTripDesc = itemView.findViewById(R.id.tv_trip_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("test","trip id ="+tripMetadata.get(getAdapterPosition()).getTripId());
            bctTripListener.bctTripClicked(tripMetadata.get(getAdapterPosition()));
        }
    }

    public interface BCTTripListener {
        void bctTripClicked(TripMetadata tripMetadata);
    }

}
