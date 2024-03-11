package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor;

import android.net.Uri;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnDocsSubmitListener;

import java.io.File;

public interface IUploadDocInteractor {

     void uploadDocs(File ownerFile, OnDocsSubmitListener onDocsSubmitListener, AddVehicleResponse addVehicleResponse);

}
