package com.mapprr.fasthub.repoDetailsScreen.presenter;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mapprr.fasthub.core.Mapprr;
import com.mapprr.fasthub.core.presenter.MvpPresenter;
import com.mapprr.fasthub.repoDetailsScreen.model.Contributor;
import com.mapprr.fasthub.repoDetailsScreen.network.SearchContributorsTask;
import com.mapprr.fasthub.shared.model.Repo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class RepoDetailsPresenter extends MvpPresenter<RepoDetailsPresenter.View> {

    private Repo repo;

    public RepoDetailsPresenter(Repo repo) {
        this.repo = repo;
    }

    @Override
    public void onViewForeground() {
        super.onViewForeground();
        renderView();
    }

    public void onRepoLinkClicked(String url) {
        getView().showWebViewScreen(url);
    }

    private void renderView() {
        getView().showRepoImage(repo.getOwner().getAvatarUrl());
        getView().showRepoName(repo.getFullName());
        getView().showRepoLink(repo.getHtmlUrl());
        getView().showRepoDescription(repo.getDescription());

        fetchAndShowRepoContributors();
    }

    private void fetchAndShowRepoContributors() {
        getView().showLoader();
        getView().hideContributorsList();
        new SearchContributorsTask(repo.getFullName(),
                                   response -> handleRepoContributors(response),
                                   (statusCode, errorMessage) -> handleContributorsFetchFailure(errorMessage)).execute();
    }

    private void handleRepoContributors(String resultJson) {
        try {
            JSONArray jsonArray = new JSONArray(resultJson);
            if (jsonArray.length() > 0) {
                List<Contributor> contributorList = Mapprr.getInstance()
                                                          .getGsonInstance()
                                                          .fromJson(resultJson, new TypeToken<List<Contributor>>() {
                                                          }.getType());

                if (isViewAttached()) {
                    getView().hideLoader();
                    getView().showContributorsList(contributorList);
                }
            } else {
                if (isViewAttached()) {
                    getView().hideLoader();
                    getView().showNoContributorsFoundError();
                }
            }
        } catch (JSONException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    private void handleContributorsFetchFailure(String errorMessage) {
        if (isViewAttached()) {
            getView().hideLoader();
            getView().showContributorFetchError(errorMessage);
        }
    }

    public void onContributorClicked(Contributor contributor) {
        getView().showContributorDetailsScreen(contributor);
    }

    public interface View extends MvpPresenter.View {

        void showRepoImage(String imageLink);

        void showRepoName(String name);

        void showRepoLink(String link);

        void showRepoDescription(String description);

        void showWebViewScreen(String url);

        void showContributorDetailsScreen(Contributor contributor);

        void showLoader();

        void hideLoader();

        void showContributorsList(List<Contributor> contributorList);

        void hideContributorsList();

        void showContributorFetchError(String errorMessage);

        void showNoContributorsFoundError();
    }
}
