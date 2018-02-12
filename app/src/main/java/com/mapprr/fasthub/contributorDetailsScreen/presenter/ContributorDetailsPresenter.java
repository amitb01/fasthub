package com.mapprr.fasthub.contributorDetailsScreen.presenter;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mapprr.fasthub.contributorDetailsScreen.network.SearchContributorRepoTask;
import com.mapprr.fasthub.core.Mapprr;
import com.mapprr.fasthub.core.presenter.MvpPresenter;
import com.mapprr.fasthub.shared.model.Repo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ContributorDetailsPresenter extends MvpPresenter<ContributorDetailsPresenter.View> {

    private String contributorAvatarUrl;
    private String contributorName;
    private String contributorReposUrl;

    public ContributorDetailsPresenter(String contributorAvatarUrl,
                                       String contributorName,
                                       String contributorReposUrl) {

        this.contributorAvatarUrl = contributorAvatarUrl;
        this.contributorName = contributorName;
        this.contributorReposUrl = contributorReposUrl;
    }

    @Override
    public void onViewForeground() {
        super.onViewForeground();
        renderView();
    }

    public void onRepoClicked(Repo clickedRepo) {
        getView().showRepoDetailsScreen(clickedRepo);
    }

    private void renderView() {
        getView().showContributorImage(contributorAvatarUrl);
        getView().showContributorName(contributorName);

        fetchAndShowContributorRepos();
    }

    private void fetchAndShowContributorRepos() {
        getView().showLoader();
        getView().hideReposList();
        new SearchContributorRepoTask(contributorReposUrl,
                                      response -> handleReposFetchSuccess(response),
                                      (statusCode, errorMessage) -> handleReposFetchFailure(errorMessage)).execute();
    }

    private void handleReposFetchSuccess(String resultJson) {
        try {
            JSONArray jsonArray = new JSONArray(resultJson);
            if (jsonArray.length() > 0) {
                List<Repo> repoList = Mapprr.getInstance()
                                            .getGsonInstance()
                                            .fromJson(resultJson, new TypeToken<List<Repo>>() {
                                            }.getType());

                if (isViewAttached()) {
                    getView().hideLoader();
                    getView().showReposList();
                    getView().showContributorRepos(repoList);
                }
            } else {
                if (isViewAttached()) {
                    getView().hideLoader();
                    getView().showNoReposFoundError();
                }
            }
        } catch (JSONException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    private void handleReposFetchFailure(String errorMessage) {
        if (isViewAttached()) {
            getView().hideLoader();
            getView().showRepoFetchError(errorMessage);
        }
    }

    public interface View extends MvpPresenter.View {

        void showContributorImage(String imageLink);

        void showContributorName(String name);

        void showContributorRepos(List<Repo> repoList);

        void showRepoDetailsScreen(Repo clickedRepo);

        void showLoader();

        void hideLoader();

        void showReposList();

        void hideReposList();

        void showRepoFetchError(String errorMessage);

        void showNoReposFoundError();
    }
}
