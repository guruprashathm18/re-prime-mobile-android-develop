package com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

public class MotorcycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MotorcyclesItem> ListData;
    private static int TYPE_LIST_ITEM = 1;
    private static int TYPE_CONFIRM = 2;
    private final onItemClickListener listener;
    private Context mContext;

    public MotorcycleListAdapter(List<MotorcyclesItem> data, onItemClickListener onItemClickListener) {
        this.ListData = data;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_LIST_ITEM) {
            View listItem = inflater.inflate(R.layout.motorcycle_list_item, parent, false);
            return new ListItemViewHolder(listItem);
        } else {
            View confirmItem = inflater.inflate(R.layout.confirm_item, parent, false);
            return new ConfirmItemViewHolder(confirmItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_LIST_ITEM) {
            ((ListItemViewHolder) holder).bindItems(this.ListData.get(position), position);
        } else {
            ((ConfirmItemViewHolder) holder).bind(listener);
        }
    }

    @Override
    public int getItemCount() {
        return this.ListData.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == ListData.size()) {
            return TYPE_CONFIRM;
        } else {
            return TYPE_LIST_ITEM;
        }
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        private final View mItemView;
        public TextView textNumber;
        public TextView textName;
        public ImageView imageMotorcycle;
        public TextInputEditText textEditEngine;
        public TextInputEditText textEditRegistration;
        public ImageView checkBoxOwnMotorcycle;
        public TextInputLayout textInputRegistration;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            this.textNumber = itemView.findViewById(R.id.txv_number);
            this.textName = itemView.findViewById(R.id.txv_name);
            this.imageMotorcycle = itemView.findViewById(R.id.imgMotorcycle);
            this.textEditEngine = itemView.findViewById(R.id.text_edit_engine);
            this.textEditRegistration = itemView.findViewById(R.id.text_edit_registration);
            this.checkBoxOwnMotorcycle = itemView.findViewById(R.id.checkbox_own_motorcycle);
            this.textInputRegistration = itemView.findViewById(R.id.text_input_registration_num);


            this.textEditRegistration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    hideError();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    updateDataModel(editable.toString().trim(), getAdapterPosition());
            }
            });
            this.textEditRegistration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "UDV-Show Motorcycles");
                        params.putString("eventAction", ListData.get(getAdapterPosition()).getModelName());
                        params.putString("eventLabel", "Update Registration number click");
                        REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                    }
                }
            });

        }

        public void hideError() {
            this.textInputRegistration.setError(null);
            this.textInputRegistration.setErrorEnabled(false);
        }

        public void bindItems(MotorcyclesItem item, int position) {
            this.textNumber.setText(String.valueOf(position + 1) + ". ");
            this.textEditRegistration.setText(item.getmRegistrationNumber());
            this.textEditEngine.setText(item.getmEngineNum());

            REUtils.getFirebaseReactUrl(itemView.getContext(), item.getModelCode(), imageMotorcycle, textName,false,null);
//
//            this.textName.setText(item.getModelName());
//
//            Glide.with(itemView.getContext())
//                    .load(item.getImageUrl()).placeholder(R.drawable.classic_500_chrome)
//                    .fitCenter()
//                    .into(this.imageMotorcycle);

            if (item.ismIsNotOwn()) {
                this.checkBoxOwnMotorcycle.setImageDrawable(itemView.getResources().getDrawable(R.drawable.checkbox));
            } else {
                this.checkBoxOwnMotorcycle.setImageDrawable(itemView.getResources().getDrawable(R.drawable.unchecked_box));
            }

            if (item.isError()) {
                this.textInputRegistration.setError(item.getErrorMessage());
                this.textInputRegistration.setErrorEnabled(true);
            } else {
                this.textInputRegistration.setError(null);
                this.textInputRegistration.setErrorEnabled(false);
            }

            checkBoxOwnMotorcycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckStatusOfItems(position);
                }
            });
        }

        private void setCheckStatusOfItems(int position) {
            MotorcyclesItem item = ListData.get(position);

            if (item.ismIsNotOwn()) {
                this.checkBoxOwnMotorcycle.setImageDrawable(itemView.getResources().getDrawable(R.drawable.unchecked_box));
                item.setIsNotOwn(false);
            } else {
                Bundle params = new Bundle();
                params.putString("eventCategory", "UDV-Show Motorcycles");
                params.putString("eventAction", item.getModelName());
                params.putString("eventLabel", "I don't own this motorcycle click");
               REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                this.checkBoxOwnMotorcycle.setImageDrawable(itemView.getResources().getDrawable(R.drawable.checkbox));
                item.setIsNotOwn(true);
            }
        }

        private void updateDataModel(String registrationNum, int position) {

            MotorcyclesItem item = ListData.get(position);
            item.setRegistrationNumber(registrationNum.trim());
        }
    }




    private class ConfirmItemViewHolder extends RecyclerView.ViewHolder {
        private final View Item;

        public ConfirmItemViewHolder(View confirmItem) {
            super(confirmItem);
            this.Item = confirmItem;
//            this.Item.setEnabled(true);
        }

        public void bind(onItemClickListener listener) {
            this.Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "UDV-Show Motorcycles");
                    params.putString("eventAction", "Confirm click");
                   REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                    listener.onItemClick();
                }
            });
//            Item.setEnabled(false);
        }
    }

    public interface onItemClickListener {
        void onItemClick();
    }
}
