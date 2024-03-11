package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;

public interface UploadProfilePicView extends REMvpView {

    void onUploadPicSuccess(String message);

    void onUploadPicFailure(String message);
}
