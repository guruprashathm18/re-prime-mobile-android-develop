package com.royalenfield.reprime.ui.home.navigation.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royalenfield.bluetooth.utils.BLEConstants;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.utils.REConstants;

import java.util.ArrayList;
import java.util.List;

public class RecentRouteLocationManager {

    public static ArrayList<RecentLocation> getRecentLocationList(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            ArrayList<RecentLocation> recentLocationList = null;
            try {
                recentLocationList = new Gson().fromJson(sharedPreferences.getString(REConstants.NAV_PREF_RECENT_LOCATION_LIST_KEY,
                        null),
                        new TypeToken<ArrayList<RecentLocation>>() {
                        }.getType());
            } catch (Exception e){
               // updateMyTBTDevice(new ArrayList<>(),context);
            }
            if (recentLocationList != null) {
                return recentLocationList;
            } else {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }
    public static void saveRecentLocationList(RecentLocation recentLocation, Context context) {
        ArrayList<RecentLocation> recentLocationList = getRecentLocationList(context);
        if (recentLocationList.size()>4){
            recentLocationList.remove(0);
        }
        recentLocationList.add(recentLocation);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putString(REConstants.NAV_PREF_RECENT_LOCATION_LIST_KEY, new Gson().toJson(recentLocationList));
        editor.apply();
    }

    //Recent Routes
    public static ArrayList<RecentRoute> getRecentRoutes(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            ArrayList<RecentRoute> recentRoutes = null;
            try {
                recentRoutes = new Gson().fromJson(sharedPreferences.getString(REConstants.NAV_PREF_RECENT_ROUTE_LIST_KEY,
                        null),
                        new TypeToken<List<RecentRoute>>() {
                        }.getType());
            } catch (Exception e){
                // updateMyTBTDevice(new ArrayList<>(),context);
            }
            if (recentRoutes != null) {
                return recentRoutes;
            } else {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }
    public static void saveRecentRoute(RecentRoute recentRoute, Context context) {
        ArrayList<RecentRoute> recentRouteList = getRecentRoutes(context);
        if (recentRouteList.size()>4){
            recentRouteList.remove(0);
        }
        recentRouteList.add(recentRoute);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putString(REConstants.NAV_PREF_RECENT_ROUTE_LIST_KEY, new Gson().toJson(recentRouteList));
        editor.apply();
    }
    // for Work and home address
    public static void saveAddressList(AddAddress address, Context context,int position) {
        ArrayList<AddAddress> addressList = getAddressList(context);
        addressList.add(position,address);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putString(REConstants.NAV_PREF_ADDRESS_LIST_KEY, new Gson().toJson(addressList));
        editor.apply();
    }
    public static void updateAddressList(AddAddress address, Context context, int position) {
        ArrayList<AddAddress> addressList = getAddressList(context);
        addressList.remove(position);
        addressList.add(position,address);
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().
                getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        editor.putString(REConstants.NAV_PREF_ADDRESS_LIST_KEY, new Gson().toJson(addressList));
        editor.apply();
    }
    public static ArrayList<AddAddress> getAddressList(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            ArrayList<AddAddress> addressList = null;
            try {
                addressList = new Gson().fromJson(sharedPreferences.getString(REConstants.NAV_PREF_ADDRESS_LIST_KEY,
                        null),
                        new TypeToken<ArrayList<AddAddress>>() {
                        }.getType());
            } catch (Exception e){
                // updateMyTBTDevice(new ArrayList<>(),context);
            }
            if (addressList != null) {
                return addressList;
            } else {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }
}
