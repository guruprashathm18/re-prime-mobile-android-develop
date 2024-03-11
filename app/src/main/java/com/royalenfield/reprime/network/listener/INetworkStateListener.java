package com.royalenfield.reprime.network.listener;

public interface INetworkStateListener {

    String CONNECTION_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    void onNetworkConnect();

    void onNetworkDisconnect();

}
