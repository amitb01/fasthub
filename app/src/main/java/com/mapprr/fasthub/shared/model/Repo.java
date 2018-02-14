package com.mapprr.fasthub.shared.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Repo implements Serializable {

    private int id;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    private Owner owner;
    @SerializedName("private")
    private boolean isPrivate;
    @SerializedName("html_url")
    private String htmlUrl;
    private String description;
    @SerializedName("fork")
    private boolean isForked;
    private String url;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("pushed_at")
    private String pushedAt;
    private String homepage;
    private int size;
    @SerializedName("stargazers_count")
    private int stargazersCount;
    @SerializedName("watchers_count")
    private int watchersCount;
    private String language;
    @SerializedName("forks_count")
    private int forksCount;
    @SerializedName("open_issues_count")
    private int openIssuesCount;

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public Owner getOwner() {
        return owner;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public static class Owner implements Serializable {

        private String login;
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("gravatar_id")
        private String gravatarId;
        private String url;
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        private String type;

        public String getAvatarUrl() {
            return avatarUrl;
        }

    }

}

