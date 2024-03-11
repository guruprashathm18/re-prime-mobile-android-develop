package com.royalenfield.reprime.ui.home.service.search.view;

import android.view.View;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;

import java.util.List;

public interface ItemClickListener {
    void onItemClick(View view, int position, List<DealerMasterResponse> filteredList);
}
