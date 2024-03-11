package com.royalenfield.reprime.ui.home.service.specificissue.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.service.specificissue.interactor.IssueInteractor;
import com.royalenfield.reprime.ui.home.service.specificissue.model.IssuesModel;

import java.util.ArrayList;

/**
 * A Simple Adapter for the RecyclerView
 */
public class IssuesFragmentAdapter extends RecyclerView.Adapter {
    private static final String TAG = "FragAdapter";
    private ArrayList<IssuesModel> engineIssues;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ArrayList<String> engineSelectedIssues = new ArrayList<>();
    private IssueInteractor issueSelectedListener;

    public IssuesFragmentAdapter(ArrayList<IssuesModel> engineIssues, IssueInteractor issueClickListener) {
        this.engineIssues = engineIssues;
        this.issueSelectedListener = issueClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        // inflate the item Layout
        View view;
        switch (viewType) {
            case IssuesModel.GENERAL_ISSUES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pbm_rowlayout, parent, false);
                return new EngineFragmentHolder(view);
            case IssuesModel.ENGINE_ISSUES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pbm_rowlayout, parent, false);
                return new EngineFragmentHolder(view);
            case IssuesModel.VEHICLE_ISSUES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pbm_rowlayout, parent, false);
                return new EngineFragmentHolder(view);
            case IssuesModel.ELECTRICAL_ISSUES_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pbm_rowlayout, parent, false);
                return new EngineFragmentHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (engineIssues.get(position).type) {
            case 0:
                return IssuesModel.GENERAL_ISSUES_TYPE;
            case 1:
                return IssuesModel.ENGINE_ISSUES_TYPE;
            case 2:
                return IssuesModel.VEHICLE_ISSUES_TYPE;
            case 3:
                return IssuesModel.ELECTRICAL_ISSUES_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        // set the data in items
       // Log.e(TAG, "FragmentAdapter Position = " + position);
        IssuesModel object = engineIssues.get(position);
        if (object != null) {
            switch (object.type) {
                case IssuesModel.GENERAL_ISSUES_TYPE:
                    ((EngineFragmentHolder) holder).tvIssueDescription.setText(object.getIssueName());
                    ((EngineFragmentHolder) holder).cbIssue.setChecked(object.getSelected());
                    break;
                case IssuesModel.ENGINE_ISSUES_TYPE:
                    ((EngineFragmentHolder) holder).tvIssueDescription.setText(object.getIssueName());
                    ((EngineFragmentHolder) holder).cbIssue.setChecked(object.getSelected());
                    break;
                case IssuesModel.VEHICLE_ISSUES_TYPE:
                    ((EngineFragmentHolder) holder).tvIssueDescription.setText(object.getIssueName());
                    ((EngineFragmentHolder) holder).cbIssue.setChecked(object.getSelected());
                    break;
                case IssuesModel.ELECTRICAL_ISSUES_TYPE:
                    ((EngineFragmentHolder) holder).tvIssueDescription.setText(object.getIssueName());
                    ((EngineFragmentHolder) holder).cbIssue.setChecked(object.getSelected());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return engineIssues.size();
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    private class EngineFragmentHolder extends RecyclerView.ViewHolder {
        private CheckBox cbIssue;
        private TextView tvIssueDescription;

        private EngineFragmentHolder(final View itemView) {
            super(itemView);
            cbIssue = itemView.findViewById(R.id.issue_checkbox);
            tvIssueDescription = itemView.findViewById(R.id.issue_description);
            cbIssue.setClickable(false);
            itemView.setTag(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cbIssue.isChecked()) {
                        cbIssue.setChecked(false);
                        engineSelectedIssues.remove(tvIssueDescription.getText().toString());
                    } else {
                        cbIssue.setChecked(true);
                        engineSelectedIssues.add(tvIssueDescription.getText().toString());
                    }

                    engineIssues.get(getAdapterPosition()).setSelected(cbIssue.isChecked());

//                    Log.e(TAG, "Selected Value = " + engineIssues.get(getAdapterPosition()).getSelected());
//                    Log.e(TAG, "Selected Value = " + engineIssues.get(getAdapterPosition()).getIssueName());
                    if (engineIssues.get(getAdapterPosition()).type == IssuesModel.GENERAL_ISSUES_TYPE) {
                        REApplication.getInstance().setSelectedIssues("General", engineIssues);
                    } else if (engineIssues.get(getAdapterPosition()).type == IssuesModel.ENGINE_ISSUES_TYPE) {
                        REApplication.getInstance().setSelectedIssues("Engine", engineIssues);
                    } else if (engineIssues.get(getAdapterPosition()).type == IssuesModel.VEHICLE_ISSUES_TYPE) {
                        REApplication.getInstance().setSelectedIssues("Vehicle", engineIssues);
                    } else if (engineIssues.get(getAdapterPosition()).type == IssuesModel.ELECTRICAL_ISSUES_TYPE) {
                        REApplication.getInstance().setSelectedIssues("Electricals", engineIssues);
                    }

                    issueSelectedListener.showSelectedIssues();

                }
            });
        }
    }
}


