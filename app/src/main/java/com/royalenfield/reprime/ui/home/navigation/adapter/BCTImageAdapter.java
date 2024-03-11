package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.navigation.model.BCTImageModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;


/**
 * Adapter creates views for the user created rides items and binds the data to the views.
 */
public class BCTImageAdapter extends RecyclerView.Adapter<BCTImageAdapter.POIViewHolder> {

    private List<BCTImageModel> bitmapList;
    private BCTImgDeleteListener bctImgDeleteListener;
    private int IMAGE_MODE;
    private Context mContext;

    public BCTImageAdapter(Context ctx, int imageMode, List<BCTImageModel> list, BCTImgDeleteListener imgDeleteListener) {
        this.mContext = ctx;
        this.IMAGE_MODE = imageMode;
        this.bitmapList = list;
        this.bctImgDeleteListener = imgDeleteListener;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bct_img_view, parent,
                false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmapList.get(position).getDrawable());
        if(IMAGE_MODE == REConstants.IMAGE_MODE_UPLOAD) {
            holder.imageView.setImageBitmap(bitmapList.get(position).getDrawable());
        }else if(IMAGE_MODE == REConstants.IMAGE_MODE_VIEW) {
            REUtils.loadImageWithGlide(mContext, bitmapList.get(position).getName(), holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return IMAGE_MODE;
    }

    class POIViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, imgDelete;

        POIViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bct_add_image);
            imgDelete = itemView.findViewById(R.id.bct_del_image);
            imgDelete.setOnClickListener(this);
            if(IMAGE_MODE == 2){
                imgDelete.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (bctImgDeleteListener != null) {
                bitmapList.remove(getAdapterPosition());
                notifyDataSetChanged();
                bctImgDeleteListener.bctImageDeleted(bitmapList);
            }
        }
    }

    public interface BCTImgDeleteListener {
        void bctImageDeleted(List<BCTImageModel> list);
    }

}
