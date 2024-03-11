package com.royalenfield.firebase.realTimeDatabase;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;

import java.util.List;

public interface OnFirebaseDealerResponseCallback {

    void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList);

    void onFirebaseDealerListFailure(String message);

    void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse);

    void onFirebaseDealerDetailFailure(String message);


}
