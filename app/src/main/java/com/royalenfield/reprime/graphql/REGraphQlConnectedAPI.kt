package com.royalenfield.reprime.graphql

import okhttp3.OkHttpClient

class REGraphQlConnectedAPI {

    private val BASE_URL = "https://connected-api.royalenfield.com"
    private val SUBSCRIPTION_BASE_URL = "wss://connected-api.royalenfield.com"

    private val SQL_CACHE_NAME = "reprimedb"
    //private var apolloClient: ApolloClient? = null

    fun getREGraphQlConnectedAPI() {
        val okHttpClient = OkHttpClient.Builder().build()
       // val apolloSqlHelper = ApolloSqlHelper(this, SQL_CACHE_NAME)
    }


}