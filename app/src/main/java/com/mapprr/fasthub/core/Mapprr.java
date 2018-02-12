package com.mapprr.fasthub.core;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Mapprr extends Application {

    private static Mapprr mMapprr;
    private Gson gsonInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mMapprr = this;
    }

    public static Mapprr getInstance() {
        return mMapprr;
    }

    public Gson getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder().create();
        }

        return gsonInstance;
    }
}

