package com.mapprr.fasthub.homeScreen.network;

import android.util.Log;

import com.mapprr.fasthub.homeScreen.model.SortType;
import com.mapprr.fasthub.network.MasterNetworkTask;
import com.mapprr.fasthub.network.Params;
import com.mapprr.fasthub.network.UrlPaths;
import com.mapprr.fasthub.network.listeners.VolleyOnErrorListener;
import com.mapprr.fasthub.network.listeners.VolleyOnSuccessListener;
import com.mapprr.fasthub.shared.model.RepoSearchResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SearchRepoTask {

    private static final String TAG = "SearchRepoTask";

    private static final String VAL_SORTBY_STARS = "stars";
    private static final String VAL_SORTBY_FORKS = "forks";
    private static final String VAL_ORDER_ASC = "asc";
    private static final String VAL_ORDER_DESC = "desc";

    private String searchQuery;
    private SortType sortType;
    private String startDate;
    private String endDate;
    private VolleyOnSuccessListener<RepoSearchResult> volleyOnSuccessListener;
    private VolleyOnErrorListener volleyOnErrorListener;

    public SearchRepoTask(String searchQuery,
                          SortType sortType,
                          Date startDate,
                          Date endDate,
                          VolleyOnSuccessListener<RepoSearchResult> volleyOnSuccessListener,
                          VolleyOnErrorListener volleyOnErrorListener) {

        this.searchQuery = searchQuery;
        this.sortType = sortType;
        this.volleyOnSuccessListener = volleyOnSuccessListener;
        this.volleyOnErrorListener = volleyOnErrorListener;

        String dateFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        if (startDate != null) {
            this.startDate = sdf.format(startDate);
            this.startDate = this.startDate.replaceAll("/", "-");
        }
        if (endDate != null) {
            this.endDate = sdf.format(endDate);
            this.endDate = this.endDate.replaceAll("/", "-");
        }
    }

    public void execute() {
        String url = UrlPaths.getBaseUrl() + UrlPaths.SEARCH_REPO_URL;

        searchQuery = searchQuery.replaceAll(" ", "+");
        if (startDate != null && endDate != null) {
            searchQuery += "+created:" + startDate + ".." + endDate;
        } else if (startDate != null) {
            searchQuery += "+created:>=" + startDate;
        } else if (endDate != null) {
            searchQuery += "+created:<=" + endDate;
        }

        HashMap<String, String> urlParams = new HashMap<>();
        urlParams.put(Params.Url.QUERY, searchQuery);

        switch (sortType) {
            case FORKS_ASC:
                urlParams.put(Params.Url.SORT, VAL_SORTBY_FORKS);
                urlParams.put(Params.Url.ORDER, VAL_ORDER_ASC);
                break;
            case FORKS_DESC:
                urlParams.put(Params.Url.SORT, VAL_SORTBY_FORKS);
                urlParams.put(Params.Url.ORDER, VAL_ORDER_DESC);
                break;
            case STARGAZERS_ASC:
                urlParams.put(Params.Url.SORT, VAL_SORTBY_STARS);
                urlParams.put(Params.Url.ORDER, VAL_ORDER_ASC);
                break;
            case STARGAZERS_DESC:
                urlParams.put(Params.Url.SORT, VAL_SORTBY_STARS);
                urlParams.put(Params.Url.ORDER, VAL_ORDER_DESC);
                break;
        }

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
