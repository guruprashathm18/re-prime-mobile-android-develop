package com.royalenfield.reprime.ui.home.navigation.presenter;

import com.royalenfield.reprime.models.request.web.tbtauthentication.TbtAuthRequest;
import com.royalenfield.reprime.ui.home.navigation.interactor.RENavigationInteractor;
import com.royalenfield.reprime.ui.home.navigation.listener.TBTAutenticationListner;

public class ReNavigationPresenter {

    private RENavigationInteractor reNavigationInteractor;
    private TBTAutenticationListner tbtAutenticationListner;

    public ReNavigationPresenter(RENavigationInteractor reNavigationInteractor,TBTAutenticationListner tbtAutenticationListner){
        this.reNavigationInteractor = reNavigationInteractor;
        this.tbtAutenticationListner = tbtAutenticationListner;
    }

    public void getTbtAuthkey(TbtAuthRequest guid,TBTAutenticationListner tbtAutenticationListner){
        reNavigationInteractor.getTbtAuthkey(guid,tbtAutenticationListner);
    }
}
