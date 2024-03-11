package com.royalenfield.reprime.ui.riderprofile.interactor;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.booking.BookingResponse;
import com.royalenfield.reprime.models.response.web.booking.Datum;
import com.royalenfield.reprime.models.response.web.profile.UploadProfilePic;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.riderprofile.listeners.BookingDataListener;
import com.royalenfield.reprime.ui.riderprofile.listeners.GMABookingDataListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class BookingIntrector implements IBookingInteractor {
    @Override
    public void getBookingData(String guid, BookingDataListener listener) {



        REApplication.mFireStoreInstance.collection(guid).document("myBooking").collection("bikeBooking").addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                   Gson gson = new Gson();
                if (error != null) {
                    listener.onFailure( Objects.requireNonNull(error.getMessage()));
                    return;
                }
                    BookingResponse bookingResponse=new BookingResponse();
                    List<Datum> data =new ArrayList<>();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        JsonElement jsonElement = gson.toJsonTree(document.getData());
                        Datum datum = gson.fromJson(jsonElement, Datum.class);
                        data.add(datum);
                    }
                    bookingResponse.setData(data);
                    listener.onSuccess(bookingResponse);
            }
        });
    }

    @Override
    public void getGMABookingData(String guid, GMABookingDataListener listener) {
        REApplication.mFireStoreInstance.collection(guid).document("myBooking").collection("gmaBooking").addSnapshotListener((value, error) -> {
            Gson gson = new Gson();
            if (error != null) {
                listener.onGMAFailure( Objects.requireNonNull(error.getMessage()));
                return;
            }
            BookingResponse bookingResponse=new BookingResponse();
            List<Datum> data =new ArrayList<>();
            for (DocumentSnapshot document : value.getDocuments()) {
                JsonElement jsonElement = gson.toJsonTree(document.getData());
                Datum datum = gson.fromJson(jsonElement, Datum.class);
                data.add(datum);
            }
            bookingResponse.setData(data);
            listener.onGMASuccess(bookingResponse);
        });

    }
}


