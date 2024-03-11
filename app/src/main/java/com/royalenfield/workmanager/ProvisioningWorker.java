package com.royalenfield.workmanager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreCallback;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;

import static com.royalenfield.reprime.utils.REConstants.KEY_UPDATE_PROVISIONING;

public class ProvisioningWorker extends Worker {
    public static final String TASK_DESC ="task_data" ;

    public ProvisioningWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
     * This method is responsible for doing the work
     * so whatever work that is needed to be performed
     * we will put it here
     *
     * For example, here I am calling the method displayNotification()
     * It will display a notification
     * So that we will understand the work is executed
     * */

    @NonNull
    @Override
    public Result doWork() {
        String taskDesc = getInputData().getString(TASK_DESC);

        FirestoreManager.getInstance().updateProvisionTableDataToFirestore( taskDesc, new OnFirestoreCallback() {
            @Override
            public void onFirestoreSuccess() {
                try {
                    REPreference.getInstance().putBoolean(REApplication.getAppContext(),KEY_UPDATE_PROVISIONING,false);
                } catch (PreferenceException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFirestoreFailure(String message) {

            }
        });
        return Result.success();
    }
}
