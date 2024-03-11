package com.royalenfield.appIndexing

object REAppIndexProvider {
    fun getInstance(): IAppIndexManager {
        return REAppIndexManager()
    }
}