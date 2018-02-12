package com.mapprr.fasthub.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mapprr.fasthub.core.Mapprr;

public class NetworkManager {

    private static NetworkManager mInstance;
    private RequestQueue mRequestQueue;

    private NetworkManager() {
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(Mapprr.getInstance());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancel(String tag) {
        mRequestQueue.cancelAll(tag);
    }
}

