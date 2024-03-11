package com.royalenfield.reprime.ui.home.service.asynctask;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;

import java.util.List;

/**
 * Service AsynTask Listeners
 */
public class ServiceAsyncTaskListeners {

    /**
     * Listener for getting LatLng from Address
     */
    public interface onLocationComplete {
        void onAddressFromLocationComplete(LatLng latLng);
    }

    /**
     * Listener for getting dealer list / details
     */
    public interface DealerDataAsyncTaskCompleteListener {

        void onDealerListSuccess(List<DealerMasterResponse> dealersList);

        void onDealerDetailSuccess(DealerMasterResponse dealersResponse);
    }

    /**
     * Listener for getting sorted dealerList with current location
     */
    public interface DealerDistanceCalc {
        void onDealersDistanceCalcComplete(List<DealerMasterResponse> dealersResponseList);
    }

    /**
     * Listener for getting modelNo from RegNo
     */
    public interface GetModelFromRegNo {
        void onGetModelComplete(String modelName);
    }
}
