package com.royalenfield.reprime.ui.home.service.search;

/**
 * @author BOP1KOR on 1/29/2019.
 */

public interface OnDealerSearchResultListener {

    void onDealerResponse();

    void onDealerResponseFailure(String errorMessage);
}
