package com.royalenfield.reprime.ui.home.service.specificissue.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.service.specificissue.adapter.IssuesFragmentAdapter;
import com.royalenfield.reprime.ui.home.service.specificissue.interactor.IssueInteractor;
import com.royalenfield.reprime.ui.home.service.specificissue.model.IssuesModel;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;


public class GeneralFragment extends Fragment {

    private IssueInteractor issueInteractor;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            issueInteractor = (IssueInteractor) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        mRecyclerView = view.findViewById(R.id.listRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        if (getUserVisibleHint()) {
            initRecyclerViewWithValues();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            initRecyclerViewWithValues();
        }
    }

    private void initRecyclerViewWithValues() {
        ArrayList<IssuesModel> list = new ArrayList<>();
        list.add(new IssuesModel(IssuesModel.GENERAL_ISSUES_TYPE, "General Service"));
        list.add(new IssuesModel(IssuesModel.GENERAL_ISSUES_TYPE, "Motorcycle Polish"));
        list.add(new IssuesModel(IssuesModel.GENERAL_ISSUES_TYPE, "Engine Polish"));
        list.add(new IssuesModel(IssuesModel.GENERAL_ISSUES_TYPE, "Motorcycle Wash"));

        String strKeyStore = "General";

        if (REUtils.getAllServiceIssues(strKeyStore) == null) {
            REApplication.getInstance().setSelectedIssues(strKeyStore, list);
        } else {
            list.clear();
            list = REUtils.getAllServiceIssues(strKeyStore);
            issueInteractor.showSelectedIssues();
        }
        //  call the constructor of ServiceCenterAdapter to send the reference and data to Adapter
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        IssuesFragmentAdapter customAdapter = new IssuesFragmentAdapter(list, issueInteractor);
        mRecyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

    }


}