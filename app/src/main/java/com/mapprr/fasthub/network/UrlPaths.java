package com.mapprr.fasthub.network;

public class UrlPaths {

    /**
     * @return base url for current ApiType
     */
    public static String getBaseUrl() {
        return BASE_GITHUB_URL;
    }

    public static final String BASE_GITHUB_URL = "https://api.github.com";
    public static final String SEARCH_REPO_URL = "/search/repositories";
    public static final String GET_CONTRIBUTORS_URL = "/repos/%s/stats/contributors";

}