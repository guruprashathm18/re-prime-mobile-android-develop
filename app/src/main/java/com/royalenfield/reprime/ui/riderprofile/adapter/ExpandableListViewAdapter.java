package com.royalenfield.reprime.ui.riderprofile.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import androidx.core.content.res.ResourcesCompat;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.web.booking.FinanceModel;
import com.royalenfield.reprime.models.response.web.booking.Group;
import com.royalenfield.reprime.models.response.web.booking.PartsDatum;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.models.response.web.booking.PaymentModeModel;
import com.royalenfield.reprime.utils.CustomTypefaceSpan;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;

import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Group> mGroups;
    public LayoutInflater mInflater;
    public Activity mActivity;

    public ExpandableListViewAdapter(Activity act, SparseArray<Group> groups) {
        mActivity = act;
        this.mGroups = groups;
        mInflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        TextView text = null;



        if(group.string.equalsIgnoreCase(mActivity.getString(R.string.finance_details))){
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.finance_child_item, null);
            }
            final List<FinanceModel> children = (List<FinanceModel>) getChild(groupPosition, childPosition);
            LinearLayout lin = (LinearLayout)convertView.findViewById(R.id.ll_finance);
            int index = 0;
            for(FinanceModel model:children){
                index++;
                addTextAndLabel(lin,model.getFinancerName(),mActivity.getResources().getString(R.string.financer_name));
                addTextAndLabel(lin,model.getFinancerStatus(),mActivity.getResources().getString(R.string.approval_status));
                addTextAndLabel(lin,model.getFinancerLoanId(),mActivity.getResources().getString(R.string.loan_id));
                addTextAndLabel(lin,model.getFinancerLoanAmount(),mActivity.getResources().getString(R.string.loan_amount));
                addTextAndLabel(lin,model.getFinancerDownPayment(),mActivity.getResources().getString(R.string.down_payment));
                addTextAndLabel(lin,model.getFinancerTenure(),mActivity.getResources().getString(R.string.repayment_tenure));
                addTextAndLabel(lin,model.getFinancerInterestRate(),mActivity.getResources().getString(R.string.flat_intrest_rate));
                addTextAndLabel(lin,model.getFinancerEmi(),mActivity.getResources().getString(R.string.estimated_emi));
                addTextAndLabel(lin,model.getFinancerProcessingFee(),mActivity.getResources().getString(R.string.processing_fee));

                if(index<children.size())
                addSeprator(lin);
            }

        }
        else if(group.string.equalsIgnoreCase(mActivity.getString(R.string.mode_of_payment))){
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.payment_mode_child_item, null);
            }
            LinearLayout lin = (LinearLayout)convertView.findViewById(R.id.ll);
            final PaymentModeModel children = (PaymentModeModel) getChild(groupPosition, childPosition);
            addTextAndLabel(lin,children.getPaymentMode(),mActivity.getResources().getString(R.string.payment_mode));
            addTextAndLabel(lin,children.getDdNumber(),mActivity.getResources().getString(R.string.dd_number));
            addTextAndLabel(lin,children.getChequeNumber(),mActivity.getResources().getString(R.string.cheque_number));
            addTextAndLabel(lin,children.getBankName(),mActivity.getResources().getString(R.string.bank_name));
            addTextAndLabel(lin,children.getCardType(),mActivity.getResources().getString(R.string.card_type));
            addTextAndLabel(lin,children.getDateOfDd(),mActivity.getResources().getString(R.string.date_of_dd));
            addTextAndLabel(lin,children.getDateOfCheque(),mActivity.getResources().getString(R.string.date_of_cheque));
            addTextAndLabel(lin,children.getPaymentDate(),mActivity.getResources().getString(R.string.payment_date));
            addTextAndLabel(lin,children.getBankReferenceNumber(),mActivity.getResources().getString(R.string.bank_refrence_number));
            addTextAndLabel(lin,children.getApprovalCode(),mActivity.getResources().getString(R.string.approval_code));



//            if(children.getPaymentMode()!=null&&children.getPaymentMode().equalsIgnoreCase("CASH")) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//            }
//            if(children.getPaymentMode()!=null&&(children.getPaymentMode().equalsIgnoreCase("Online")||children.getPaymentMode().equalsIgnoreCase("Demand Draft")) ) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//                addTextAndLabel(lin,children.getDdNumber(),"DD Number:");
//                addTextAndLabel(lin,children.getBankName(),"Bank Name:");
//                addTextAndLabel(lin,children.getDateOfDd(),"Date of DD:");
//
//            }
//
//            if(children.getPaymentMode()!=null&&children.getPaymentMode().equalsIgnoreCase("Cheque") ) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//                addTextAndLabel(lin,children.getChequeNumber(),"Cheque Number:");
//                addTextAndLabel(lin,children.getBankName(),"Bank Name:");
//                addTextAndLabel(lin,children.getDateOfCheque(),"Date of Cheque:");
//
//            }
//
//            if(children.getPaymentMode()!=null&&children.getPaymentMode().equalsIgnoreCase("CREDIT CARD") ) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//                addTextAndLabel(lin,children.getCardType(),"Card Type:");
//                addTextAndLabel(lin,children.getPaymentDate(),"Payment Date:");
//                addTextAndLabel(lin,children.getApprovalCode(),"Approval Code:");
//
//            }
//
//            if(children.getPaymentMode()!=null&&children.getPaymentMode().equalsIgnoreCase("DEBIT CARD") ) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//                addTextAndLabel(lin,children.getCardType(),"Card Type:");
//                addTextAndLabel(lin,children.getPaymentDate(),"Payment Date:");
//                addTextAndLabel(lin,children.getApprovalCode(),"Approval Code:");
//
//            }
//            if(children.getPaymentMode()!=null&&children.getPaymentMode().equalsIgnoreCase("Bank Transfer") ) {
//                addTextAndLabel(lin,children.getPaymentMode(),"Payment Mode:");
//                addTextAndLabel(lin,children.getPaymentDate(),"Payment Date:");
//                addTextAndLabel(lin,children.getBankReferenceNumber(),"Bank Reference Number:");
//
//            }

        }
else if(group.string.equalsIgnoreCase(mActivity.getString(R.string.apprael_parts))){
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.apprael_child_item, null);
            }
            text = (TextView) convertView.findViewById(R.id.textViewChild);
            final PartsDatum children = (PartsDatum) getChild(groupPosition, childPosition);
            text.setText(children.getPartdescription()+" ("+children.getQty()+")");
            //customTextView(mActivity,text,children);
        }
        else {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.child_item, null);
            }
            text = (TextView) convertView.findViewById(R.id.textViewChild);
            final String children = (String) getChild(groupPosition, childPosition);


            text.setText(children);

        }
        return convertView;
    }

    private void addTextAndLabel(LinearLayout lin,String txt,String label) {
        if(txt!=null&&!txt.equalsIgnoreCase("null")&&!txt.isEmpty()) {
            TextView dynamicTextView = new TextView(mActivity);
            LinearLayout.LayoutParams lp = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dynamicTextView.setLayoutParams(lp);
            SpannableStringBuilder builder = new SpannableStringBuilder();

            builder.append(getBoldSpan(label));
            builder.append("\n");
            if (txt != null)
                builder.append(getRegularSpan(txt));
            dynamicTextView.setText(builder, TextView.BufferType.SPANNABLE);
            lp.setMargins(50, 2, 10, 20);
            dynamicTextView.setLayoutParams(lp);
            lin.addView(dynamicTextView);
        }
    }

    private void addSeprator(LinearLayout lin) {
            Button dynamicTextView = new Button(mActivity);
            LinearLayout.LayoutParams lp = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);

            dynamicTextView.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        int paddingDp = 5;
        int paddingDpTop = 4;
        float density = mActivity.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);
        int paddingPixelTop = (int)(paddingDpTop * density);
        lp.setMargins(0,paddingPixelTop,0,paddingPixel);
            dynamicTextView.setLayoutParams(lp);

            lin.addView(dynamicTextView);

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).children.size();
    }

    private SpannableStringBuilder getRegularSpan(String text){
        SpannableStringBuilder SS = new SpannableStringBuilder(text);
        Typeface typefaceRegular = ResourcesCompat.getFont(mActivity, R.font.montserrat_regular);
        SS.setSpan(new AbsoluteSizeSpan(18,true), 0,SS.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SS.setSpan (new RECustomTyperFaceSpan(typefaceRegular), 0, text.length(), 0);
        SS.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.black_two)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return  SS;
    }

    private SpannableStringBuilder getBoldSpan(String text){
        SpannableStringBuilder SS = new SpannableStringBuilder(text);
        Typeface typefaceBold = ResourcesCompat.getFont(mActivity, R.font.montserrat_bold);
        SS.setSpan(new AbsoluteSizeSpan(14,true), 0,SS.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SS.setSpan (new RECustomTyperFaceSpan(typefaceBold), 0, text.length(), 0);
        SS.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.black_two)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return  SS;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item, null);
        }
        Group group = (Group) getGroup(groupPosition);
        TextView txt = (TextView) convertView.findViewById(R.id.textViewGroup);
        txt.setText(group.string);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void customTextView(Context context, TextView view,PartsDatum data) {
        Typeface mTypefaceMontserratRegular = ResourcesCompat.getFont(context,
                R.font.montserrat_regular);
        Typeface typeface_montserrat_semibold = ResourcesCompat.getFont(context,
                R.font.montserrat_bold);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(data.getPartdescription());
        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
                data.getPartdescription().length(), 0);

        spanTxt.append(" ");
        spanTxt.append("("+data.getQty()+")");
        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length()
                - data.getQty().length()-2, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

}

