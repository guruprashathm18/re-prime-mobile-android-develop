package com.royalenfield.reprime.ui.home.homescreen.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public interface OnFirestoreResponseListener {

    void onSuccess(DocumentSnapshot dataSnapshot);

}
