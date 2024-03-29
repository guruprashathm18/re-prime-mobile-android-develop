package com.royalenfield.reprime.ui.onboarding.facebooklogin;

import android.net.Uri;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author BOP1KOR on 11/20/2018.
 * <p>
 * TO Get Profile request response callback..
 */
public class GetUserCallback {

    private IGetUserResponse mGetUserResponse;
    private GraphRequest.Callback mCallback;

    public GetUserCallback(final IGetUserResponse getUserResponse) {

        mGetUserResponse = getUserResponse;
        mCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                FBUser user = null;
                try {
                    JSONObject userObj = response.getJSONObject();
                    if (userObj == null) {
                        return;
                    }
                    user = jsonToUser(userObj);

                } catch (JSONException e) {
                    // Handle exception ...
                }

                // Handled by REBaseActivity
                mGetUserResponse.onCompleted(user);
            }
        };
    }

    private FBUser jsonToUser(JSONObject user) throws JSONException {
        Uri picture = Uri.parse(user.getJSONObject("picture").getJSONObject("data").getString
                ("url"));
        String name = user.getString("name");
        String id = user.getString("id");
        String email = null;
        if (user.has("email")) {
            email = user.getString("email");
        }

        // Build permissions display string
        StringBuilder builder = new StringBuilder();
        JSONArray perms = user.getJSONObject("permissions").getJSONArray("data");
        builder.append("Permissions:\n");
        for (int i = 0; i < perms.length(); i++) {
            builder.append(perms.getJSONObject(i).get("permission")).append(": ").append(perms
                    .getJSONObject(i).get("status")).append("\n");
        }
        String permissions = builder.toString();

        return new FBUser(picture, name, id, email, permissions);
    }

    public GraphRequest.Callback getCallback() {
        return mCallback;
    }

    public interface IGetUserResponse {
        void onCompleted(FBUser user);
    }
}
