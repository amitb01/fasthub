package com.mapprr.fasthub.repoDetailsScreen.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contributor implements Serializable {

    private int total;
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public static class Author implements Serializable {

        private String login;
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("gravatar_id")
        private String gravatarId;
        private String url;
        @SerializedName("html_url")
        private String htmlUrl;
        @SerializedName("followers_url")
        private String followersUrl;
        @SerializedName("following_url")
        private String followingUrl;
        @SerializedName("gists_url")
        private String gistsUrl;
        @SerializedName("starred_url")
        private String starredUrl;
        @SerializedName("subscriptions_url")
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        private String organizationsUrl;
        @SerializedName("repos_url")
        private String reposUrl;
        @SerializedName("events_url")
        private String eventsUrl;
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        private String type;
        @SerializedName("site_admin")
        private boolean isSiteAdmin;

        public String getLogin() {
            return login;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }
    }
}