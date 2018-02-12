package com.mapprr.fasthub.homeScreen.network;

import android.util.Log;

import com.mapprr.fasthub.network.MasterNetworkTask;
import com.mapprr.fasthub.network.Params;
import com.mapprr.fasthub.network.UrlPaths;
import com.mapprr.fasthub.network.listeners.VolleyOnErrorListener;
import com.mapprr.fasthub.network.listeners.VolleyOnSuccessListener;
import com.mapprr.fasthub.shared.model.RepoSearchResult;

import java.util.HashMap;

public class SearchRepoTask {

    public static final String TAG = "SearchRepoTask";

    private String searchQuery;
    private VolleyOnSuccessListener<RepoSearchResult> volleyOnSuccessListener;
    private VolleyOnErrorListener volleyOnErrorListener;

    public SearchRepoTask(String searchQuery,
                          VolleyOnSuccessListener<RepoSearchResult> volleyOnSuccessListener,
                          VolleyOnErrorListener volleyOnErrorListener) {

        this.searchQuery = searchQuery;
        this.volleyOnSuccessListener = volleyOnSuccessListener;
        this.volleyOnErrorListener = volleyOnErrorListener;
    }

    public void execute() {
        String url = UrlPaths.getBaseUrl() + UrlPaths.SEARCH_REPO_URL;

        HashMap<String, String> urlParams = new HashMap<>();
        searchQuery = searchQuery.replaceAll(" ", "+");
        urlParams.put(Params.Url.QUERY, searchQuery);

        MasterNetworkTask masterNetworkTask = new MasterNetworkTask(TAG);
        masterNetworkTask.setUrlParams(urlParams);
        masterNetworkTask.GET(url);
        masterNetworkTask.setResponseCallbacks(RepoSearchResult.class,
                                               new MasterNetworkTask.ResponseCallbacks<RepoSearchResult>() {
                                                   @Override
                                                   public void onSuccessfulResponse(RepoSearchResult response) {
                                                       Log.d(TAG, "Fetched successfully");
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
