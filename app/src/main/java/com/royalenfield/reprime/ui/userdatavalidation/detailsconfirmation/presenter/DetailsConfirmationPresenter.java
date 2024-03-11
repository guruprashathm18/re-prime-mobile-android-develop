package com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.presenter;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.view.DetailsConfirmationView;

public class DetailsConfirmationPresenter implements IDetailsConfirmationPresenter {

    private DetailsConfirmationView mDetailsConfirmationView;

    public DetailsConfirmationPresenter(DetailsConfirmationView detailsConfirmationView) {
        mDetailsConfirmationView = detailsConfirmationView;
    }

    @Override
    public void setScreenType(String screenType, String intentScreenType, String intentMessage) {

        if (mDetailsConfirmationView!=null) {
            if (intentScreenType.equals(screenType)) {
                mDetailsConfirmationView.setMessageOnView(intentMessage);
            } else {
                mDetailsConfirmationView.setMessageViewVisibility(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        mDetailsConfirmationView = null;
    }
}
