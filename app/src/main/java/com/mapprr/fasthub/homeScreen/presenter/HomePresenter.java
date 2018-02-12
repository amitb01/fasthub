package com.mapprr.fasthub.homeScreen.presenter;

import com.mapprr.fasthub.core.presenter.MvpPresenter;
import com.mapprr.fasthub.homeScreen.network.SearchRepoTask;
import com.mapprr.fasthub.shared.model.Repo;
import com.mapprr.fasthub.shared.model.RepoSearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePresenter extends MvpPresenter<HomePresenter.View> {

    private String DEFAULT_SEARCH_KEYWORD = "rxjava";

    @Override
    public void attachView(View view) {
        super.attachView(view);
        onRepoSearched(DEFAULT_SEARCH_KEYWORD);
    }

    public void onRepoSearched(String searchKeyword) {
        getView().showLoader();
        getView().hideReposList();
        new SearchRepoTask(searchKeyword,
                           response -> handleSearchResults(response),
                           (statusCode, errorMessage) -> handleSearchFailure(errorMessage)).execute();
    }

    private void handleSearchResults(RepoSearchResult searchResult) {
        if (isViewAttached()) {
            if (searchResult.getRepoList().size() > 0) {
                sortRepoListByWatchers(searchResult.getRepoList());
                getView().hideLoader();
                getView().showReposList();
                getView().renderSearchedRepos(searchResult.getRepoList());
            } else {
                getView().hideLoader();
                getView().showNoResultsFoundError();
            }
        }
    }

    private void handleSearchFailure(String errorMessage) {
        if (isViewAttached()) {
            getView().hideLoader();
            getView().showSearchFailureError(errorMessage);
        }
    }

    public void onRepoClicked(Repo clickedRepo) {
        getView().showRepoDetailsScreen(clickedRepo);
    }

    private void sortRepoListByWatchers(List<Repo> repoList) {
        Collections.sort(repoList, new SortByWatchersComp());
    }

    private class SortByWatchersComp implements Comparator<Repo> {

        @Override
        public int compare(Repo repo1, Repo repo2) {
            return repo2.getWatchersCount() - repo1.getWatchersCount();
        }
    }

    public interface View extends MvpPresenter.View {

        void renderSearchedRepos(List<Repo> repoList);

        void showRepoDetailsScreen(Repo clickedRepo);

        void showLoader();

        void hideLoader();

        void showReposList();

        void hideReposList();

        void showSearchFailureError(String errorMessage);

        void showNoResultsFoundError();
    }

}
