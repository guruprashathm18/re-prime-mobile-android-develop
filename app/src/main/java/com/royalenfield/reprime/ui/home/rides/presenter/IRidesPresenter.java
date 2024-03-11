package com.royalenfield.reprime.ui.home.rides.presenter;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface IRidesPresenter {

    void getDealerRideList(String category, int page, String latitude, String longitude);

    void requestMarqueeRidesFormServer(String date, String branchCode);

    void requestPopularRidesFormServer(String date, String branchCode);
}
