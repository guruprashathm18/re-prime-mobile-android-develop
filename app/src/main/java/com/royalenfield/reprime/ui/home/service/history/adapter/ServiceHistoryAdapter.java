package com.royalenfield.reprime.ui.home.service.history.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.home.service.billsummary.views.ServiceBillSummaryActivity;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceHistoryRowPresenter;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.
        ServiceHistoryViewHolder> {

    private List<ServiceHistoryResponse> mServiceHistoryList;
    private Context mContext;
    private Activity mActivity;
    private ServiceHistoryRowPresenter mServiceHistoryRowPresenter;
    private int mVehicleSpinnerPosition;


    public ServiceHistoryAdapter(Activity activity, Context context, ServiceHistoryRowPresenter
            serviceHistoryRowPresenter, List<ServiceHistoryResponse> serviceHistoryResponse, int position) {
        this.mContext = context;
        this.mActivity = activity;
        this.mServiceHistoryRowPresenter = serviceHistoryRowPresenter;
        this.mServiceHistoryList = serviceHistoryResponse;
        this.mVehicleSpinnerPosition = position;
    }

    @NonNull
    @Override
    public ServiceHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicehistory,
                parent, false);
        return new ServiceHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHistoryViewHolder holder, int position) {
        //Calling Row presenter here
        mServiceHistoryRowPresenter.onBindServiceHistoryRowViewAtPosition(position, holder,
                mServiceHistoryList);
    }

    @Override
    public int getItemCount() {
        return mServiceHistoryRowPresenter.getRepositoriesRowsCount(mServiceHistoryList);
    }

    /**
     * Viewholder for recyclerview
     */
    public class ServiceHistoryViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, ServiceHistoryRowView {
        private TextView mViewbillsummary, mInvoicedate, mInvoiceamount;


        public ServiceHistoryViewHolder(View view) {
            super(view);
            mViewbillsummary = view.findViewById(R.id.textview_view_bill_summary);
            mViewbillsummary.setOnClickListener(this);
            mInvoicedate = view.findViewById(R.id.textView_invoice_date);
            mInvoiceamount = view.findViewById(R.id.textView_invoice_amount);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textview_view_bill_summary:
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Service History");
                    params.putString("eventAction", "View bill summary click");
                   REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, params);
                    Intent intent = new Intent(mContext, ServiceBillSummaryActivity.class);
                    //Sending position of the item clicked
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("vehicleSpinnerPosition", mVehicleSpinnerPosition);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void setInvoiceDate(String date) {
            //TODO Date logic needs to be implemented
//            mInvoicedate.setText(date);
            mInvoicedate.setText(REUtils.getOrdinalDateObjectForHistoryDate(mContext, date));
        }

        @Override
        public void SetInvoiceAmount(String amount) {
            try {
                //Rounding off the amount
                if (amount != null && !amount.equals(REApplication.getAppContext().getResources()
                        .getString(R.string.text_hyphen_na))) {
                    mInvoiceamount.setText(String.format(mContext.getResources().
                                    getString(R.string.amount_rs),
                            mContext.getResources().getString(R.string.rupees),
                            REUtils.getRoundoffAmount(amount), mContext.
                                    getResources().getString(R.string.rupees_slash)));
                } else {
                    mInvoiceamount.setText(amount);
                }
            } catch (Exception e) {
                RELog.e(e);
            }

        }
    }
}
