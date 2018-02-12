package com.mapprr.fasthub.repoDetailsScreen.network;

import android.util.Log;

import com.mapprr.fasthub.network.MasterNetworkTask;
import com.mapprr.fasthub.network.UrlPaths;
import com.mapprr.fasthub.network.listeners.VolleyOnErrorListener;
import com.mapprr.fasthub.network.listeners.VolleyOnSuccessListener;

public class SearchContributorsTask {

    public static final String TAG = "SearchContributorsTask";

    private String repoFullName;
    private VolleyOnSuccessListener<String> volleyOnSuccessListener;
    private VolleyOnErrorListener volleyOnErrorListener;

    public SearchContributorsTask(String repoFullName,
                                  VolleyOnSuccessListener<String> volleyOnSuccessListener,
                                  VolleyOnErrorListener volleyOnErrorListener) {

        this.repoFullName = repoFullName;
        this.volleyOnSuccessListener = volleyOnSuccessListener;
        this.volleyOnErrorListener = volleyOnErrorListener;
    }

    public void execute() {
        String url = UrlPaths.getBaseUrl() + String.format(UrlPaths.GET_CONTRIBUTORS_URL, repoFullName);

        MasterNetworkTask masterNetworkTask = new MasterNetworkTask(TAG);
        masterNetworkTask.GET(url);
        masterNetworkTask.setResponseCallbacks(String.class,
                                               new MasterNetworkTask.ResponseCallbacks<String>() {
                                                   @Override
                                                   public void onSuccessfulResponse(String response) {
                                                       Log.d("TAG", "response");
                                                       volleyOnSuccessListener.onSuccessfulResponse(response);
                                                   }

                                                   @Override
                                                   public void onErrorResponse(int StatusCode, String errorMessage) {
                                                       volleyOnErrorListener.onErrorResponse(StatusCode, errorMessage);
                                                   }
                                               });
        masterNetworkTask.execute();
    }

}
