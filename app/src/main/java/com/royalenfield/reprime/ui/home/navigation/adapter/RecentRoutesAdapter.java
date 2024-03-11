package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRoute;

import java.util.ArrayList;

public class RecentRoutesAdapter extends RecyclerView.Adapter<RecentRoutesAdapter.ViewHolder> {

    private ArrayList<RecentRoute> localDataSet;
    public OnRecentRouteItemClickListener onRecentRouteItemClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final TextView textView2;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.text_place);
            textView2 = (TextView) view.findViewById(R.id.tv_recent_route_address);
            view.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getTextView2() {
            return textView2;
        }

        @Override
        public void onClick(View v) {
            if (onRecentRouteItemClickListener != null)

                onRecentRouteItemClickListener.onRecentRouteItemClick(localDataSet.get(getAdapterPosition()).getmWayPointList());
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public RecentRoutesAdapter(ArrayList<RecentRoute> dataSet, OnRecentRouteItemClickListener onrecentRouteItemClickListener) {
        localDataSet = dataSet;
        onRecentRouteItemClickListener = onrecentRouteItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_route_search, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //WayPointsData wayPointsData = localDataSet.get(position);

           /* if (localDataSet.get(position).getmWayPointList().size()>2){
                viewHolder.getTextView().setText(localDataSet.get(position).getmWayPointList().get(1).getPlaceName()+","+localDataSet.get(position).getmWayPointList().get(2).getPlaceName());
                Log.e("Routedetails1",""+localDataSet.get(position).getmWayPointList().get(1).getPlaceName()+","+localDataSet.get(position).getmWayPointList().get(2).getPlaceName());
            } else {
                viewHolder.getTextView().setText(localDataSet.get(position).getmWayPointList().get(1).getPlaceName());
                Log.e("Routedetails2",""+localDataSet.get(position).getmWayPointList().get(1).getPlaceName());
            }*/

        if (localDataSet.get(position).getmWayPointList().size() > 2) {
            viewHolder.getTextView().setText(localDataSet.get(position).getmWayPointList().get(localDataSet.get(position).getmWayPointList().size() - 1).getPlaceName());
            Log.e("Routedetails1", "" + localDataSet.get(position).getmWayPointList().get(localDataSet.get(position).getmWayPointList().size() - 1).getPlaceName());
            StringBuilder sb = new StringBuilder();
                /*for (int i=1;i<localDataSet.get(position).getmWayPointList().size()-1;i++){
                   // sb.append(localDataSet.get(position).getmWayPointList().get(i).getPlaceName());
                    sb.append(localDataSet.get(position).getmWayPointList().get(i).getPlaceName());
                    sb.append(",");
                }*/
            if (localDataSet.get(position).getmWayPointList().size() == 3) {
                sb.append(localDataSet.get(position).getmWayPointList().size() - 2 + " Waypoint added");
            } else {
                sb.append(localDataSet.get(position).getmWayPointList().size() - 2 + " Waypoints added");
            }
            viewHolder.getTextView2().setText(sb);
            Log.e("Routedetails2", "" + sb);
        } else {
            try {
                if (!localDataSet.get(position).getmWayPointList().get(1).getPlaceName().isEmpty()) {
                    viewHolder.getTextView().setText(localDataSet.get(position).getmWayPointList().get(1).getPlaceName());
                    Log.e("Routedetails3", "" + localDataSet.get(position).getmWayPointList().get(1).getPlaceName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (localDataSet != null && localDataSet.size() > 0) {
            return localDataSet.size();
        } else {
            return 0;
        }
    }

    public interface OnRecentRouteItemClickListener {
        void onRecentRouteItemClick(ArrayList<WayPointsData> place);
    }
}
