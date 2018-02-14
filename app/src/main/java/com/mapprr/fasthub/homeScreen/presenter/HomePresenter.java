package com.mapprr.fasthub.homeScreen.presenter;

import com.mapprr.fasthub.core.presenter.MvpPresenter;
import com.mapprr.fasthub.homeScreen.model.SortType;
import com.mapprr.fasthub.homeScreen.network.SearchRepoTask;
import com.mapprr.fasthub.shared.model.Repo;
import com.mapprr.fasthub.shared.model.RepoSearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomePresenter extends MvpPresenter<HomePresenter.View> {

    private String searchKeyword;
    private List<Repo> repoList;
    private SortType sortType;

    private Date startDate;
    private Date endDate;

    public HomePresenter() {
        searchKeyword = "rxjava";
        sortType = SortType.WATCHERS_DESC;
    }

    @Override
    public void attachView(View view) {
        super.attachView(view);
        refreshResults();
    }

    public void onKeywordSearched(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        refreshResults();
    }

    public void onSortTypeSelected(SortType sortType) {
        this.sortType = sortType;
        refreshResults();
    }

    public void onCreatedDateRangeSet(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        refreshResults();
    }

    private void refreshResults() {
        getView().showLoader();
        getView().hideReposList();

        new SearchRepoTask(searchKeyword,
                           sortType,
                           startDate,
                           endDate,
                           response -> handleSearchResults(response),
                           (statusCode, errorMessage) -> handleSearchFailure(errorMessage)).execute();
    }

    private void handleSearchResults(RepoSearchResult searchResult) {
        if (isViewAttached()) {
            getView().hideLoader();

            if (searchResult.getRepoList().size() > 0) {
                repoList = searchResult.getRepoList();
                sortResultsForCustomSortType();
                getView().showReposList();
                getView().renderSearchedRepos(repoList);
            } else {
                getView().showNoResultsFoundError();
            }
        }
    }

    private void sortResultsForCustomSortType() {
        switch (sortType) {
            case WATCHERS_ASC:
                Collections.sort(repoList, new SortByWatchersComp());
                getView().renderSearchedRepos(repoList);
                break;
            case WATCHERS_DESC:
                Collections.sort(repoList, Collections.reverseOrder(new SortByWatchersComp()));
                break;
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

    public void onSortButtonClicked() {
        getView().showSortOptionsView(sortType);
    }

    public void onFiltersButtonClicked() {
        getView().showFiltersView(startDate, endDate);
    }

    private class SortByWatchersComp implements Comparator<Repo> {

        @Override
        public int compare(Repo repo1, Repo repo2) {
            return repo1.getWatchersCount() - repo2.getWatchersCount();
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

        void showSortOptionsView(SortType currentSortType);

        void showFiltersView(Date startDate, Date endDate);
    }

}
