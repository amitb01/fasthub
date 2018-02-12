package com.mapprr.fasthub.shared.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RepoSearchResult implements Serializable {

    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incomplete_results")
    private boolean isResultsIncomplete;
    @SerializedName("items")
    List<Repo> repoList;

    public List<Repo> getRepoList() {
        return repoList;
    }
}
