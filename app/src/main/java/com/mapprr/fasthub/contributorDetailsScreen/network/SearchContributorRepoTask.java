package com.mapprr.fasthub.contributorDetailsScreen.network;

import android.util.Log;

import com.mapprr.fasthub.network.MasterNetworkTask;
import com.mapprr.fasthub.network.listeners.VolleyOnErrorListener;
import com.mapprr.fasthub.network.listeners.VolleyOnSuccessListener;

public class SearchContributorRepoTask {

    public static final String TAG = "SearchContributorRepoTask";

    private String reposUrl;
    private VolleyOnSuccessListener<String> volleyOnSuccessListener;
    private VolleyOnErrorListener volleyOnErrorListener;

    public SearchContributorRepoTask(String reposUrl,
                                     VolleyOnSuccessListener<String> volleyOnSuccessListener,
                                     VolleyOnErrorListener volleyOnErrorListener) {

        this.reposUrl = reposUrl;
        this.volleyOnSuccessListener = volleyOnSuccessListener;
        this.volleyOnErrorListener = volleyOnErrorListener;
    }

    public void execute() {
        MasterNetworkTask masterNetworkTask = new MasterNetworkTask(TAG);
        masterNetworkTask.GET(reposUrl);
        masterNetworkTask.setResponseCallbacks(String.class,
                                               new MasterNetworkTask.ResponseCallbacks<String>() {
                                                   @Override
                                                   public void onSuccessfulResponse(String response) {
                                                       Log.d("TAG", "Fetched successfully");
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
