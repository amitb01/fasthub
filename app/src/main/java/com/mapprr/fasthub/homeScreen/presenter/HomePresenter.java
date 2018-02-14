package com.mapprr.fasthub.homeScreen.presenter;

import com.mapprr.fasthub.core.presenter.MvpPresenter;
import com.mapprr.fasthub.homeScreen.network.SearchRepoTask;
import com.mapprr.fasthub.shared.model.Repo;
import com.mapprr.fasthub.shared.model.RepoSearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePresenter extends MvpPresenter<HomePresenter.View> {

    public enum SortType {
        WATCHERS_ASC, WATCHERS_DESC, FORKS_ASC, FORKS_DESC, STARGAZERS_ASC, STARGAZERS_DESC
    }

    private String DEFAULT_SEARCH_KEYWORD = "rxjava";

    private List<Repo> repoList;
    private SortType sortType;

    public HomePresenter() {
        sortType = SortType.WATCHERS_DESC;
    }

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

    public void onSortTypeSelected(SortType sortType) {
        this.sortType = sortType;
        getView().showLoader();
        getView().hideReposList();

        switch (sortType) {
            case WATCHERS_ASC:
                Collections.sort(repoList, new SortByWatchersComp());
                break;
            case WATCHERS_DESC:
                Collections.sort(repoList, Collections.reverseOrder(new SortByWatchersComp()));
                break;
            case FORKS_ASC:
                Collections.sort(repoList, new SortByForksComp());
                break;
            case FORKS_DESC:
                Collections.sort(repoList, Collections.reverseOrder(new SortByForksComp()));
                break;
            case STARGAZERS_ASC:
                Collections.sort(repoList, new SortByStarsComp());
                break;
            case STARGAZERS_DESC:
                Collections.sort(repoList, Collections.reverseOrder(new SortByStarsComp()));
                break;
        }

        getView().hideLoader();
        getView().showReposList();
        getView().renderSearchedRepos(repoList);
    }

    private void handleSearchResults(RepoSearchResult searchResult) {
        if (isViewAttached()) {
            if (searchResult.getRepoList().size() > 0) {
                repoList = searchResult.getRepoList();
                onSortTypeSelected(sortType);
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

    private class SortByWatchersComp implements Comparator<Repo> {

        @Override
        public int compare(Repo repo1, Repo repo2) {
            return repo1.getWatchersCount() - repo2.getWatchersCount();
        }
    }

    private class SortByForksComp implements Comparator<Repo> {

        @Override
        public int compare(Repo repo1, Repo repo2) {
            return repo1.getForksCount() - repo2.getForksCount();
        }
    }

    private class SortByStarsComp implements Comparator<Repo> {

        @Override
        public int compare(Repo repo1, Repo repo2) {
            return repo1.getStargazersCount() - repo2.getStargazersCount();
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
