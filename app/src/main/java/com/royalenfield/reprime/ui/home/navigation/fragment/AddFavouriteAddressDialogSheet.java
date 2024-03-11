package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.reprime.R;

public class AddFavouriteAddressDialogSheet extends BottomSheetDialogFragment
        implements View.OnClickListener{
    String address_type;

    public static final String TAG = "AddFavouriteAddressDialogSheet";
    private ItemClickListener mListener;
    public static AddFavouriteAddressDialogSheet newInstance() {
        return new AddFavouriteAddressDialogSheet();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_fav_location_menu, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        address_type = getArguments().getString("type");
        if (address_type!=null && (address_type.equalsIgnoreCase("Home")||address_type.equalsIgnoreCase("Work"))){
            view.findViewById(R.id.tv_rename_fav_address).setVisibility(View.GONE);
            view.findViewById(R.id.renameView).setVisibility(View.GONE);
        } else{
            view.findViewById(R.id.tv_rename_fav_address).setVisibility(View.VISIBLE);
            view.findViewById(R.id.renameView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_rename_fav_address).setOnClickListener(this);
        }
        view.findViewById(R.id.tv_edit_fav_address).setOnClickListener(this);
        view.findViewById(R.id.tv_edit_del_address).setOnClickListener(this);
       // view.findViewById(R.id.textView4).setOnClickListener(this);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        TextView tvSelected = (TextView) view;
        mListener.onItemClick(tvSelected.getText().toString(),address_type);
        dismiss();
    }
    public interface ItemClickListener {
        void onItemClick(String item, String address_type);
    }
}
