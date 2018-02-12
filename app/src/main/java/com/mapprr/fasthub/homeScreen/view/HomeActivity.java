package com.mapprr.fasthub.homeScreen.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapprr.fasthub.R;
import com.mapprr.fasthub.core.view.MvpActivity;
import com.mapprr.fasthub.homeScreen.presenter.HomePresenter;
import com.mapprr.fasthub.repoDetailsScreen.view.RepoDetailsActivity;
import com.mapprr.fasthub.shared.adapter.RepoListAdapter;
import com.mapprr.fasthub.shared.model.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

public class HomeActivity extends MvpActivity<HomePresenter> implements HomePresenter.View {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_repos)
    RecyclerView repoRecyclerView;
    @Bind(R.id.progress_loader)
    RelativeLayout progressLoader;

    private MenuItem searchMenuItem;
    private boolean isSearchOpened = false;
    private EditText searchEditText;
    private Timer repoSearchDebounceTimer;
    private RepoListAdapter repoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showToolbar();
        initRecyclerView();
    }

    @Override
    public HomePresenter createPresenter(Bundle savedInstanceState) {
        return new HomePresenter();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchMenuItem = menu.findItem(R.id.action_search);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }

        super.onBackPressed();
    }

    private void showToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        repoRecyclerView.setLayoutManager(layoutManager);

        repoListAdapter = new RepoListAdapter(new ArrayList<>(), repo -> getPresenter().onRepoClicked(repo),
                                              RepoListAdapter.Source.HOME_SCREEN);
        repoRecyclerView.setAdapter(repoListAdapter);
    }

    private void handleMenuSearch() {
        if (isSearchOpened) {
            hideToolbarSearch();
        } else {
            showToolbarSearch();
        }
    }

    private void hideToolbarSearch() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
        actionBar.setDisplayShowTitleEnabled(true); //show the title in the action bar

        hideOnScreenKeyboard();
        toggleToolbarSearchIcon();

        isSearchOpened = false;
    }

    private void showToolbarSearch() {
        ActionBar actionBar = getSupportActionBar(); //get the actionbar

        actionBar.setDisplayShowCustomEnabled(true); //enable it to display a custom view in the action bar.
        actionBar.setCustomView(R.layout.search_bar);//add the custom view
        actionBar.setDisplayShowTitleEnabled(false); //hide the title

        initToolbarSearchView();
        showOnScreenKeyboard();
        toggleToolbarSearchIcon();

        isSearchOpened = true;
    }

    private void hideOnScreenKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    private void showOnScreenKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void toggleToolbarSearchIcon() {
        if (isSearchOpened) {
            searchMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search_black_24dp));
        } else {
            searchMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp));
        }
    }

    private void initToolbarSearchView() {
        ActionBar actionBar = getSupportActionBar(); //get the actionbar
        searchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.editSearch);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    handleSearchedKeyword(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        activateToolbarSearch();
    }

    private void activateToolbarSearch() {
        searchEditText.requestFocus();
        searchEditText.requestFocusFromTouch();
    }

    private void handleSearchedKeyword(String keyword) {
        if (repoSearchDebounceTimer != null) {
            repoSearchDebounceTimer.cancel();
        }

        repoSearchDebounceTimer = new Timer();
        repoSearchDebounceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getPresenter().onRepoSearched(keyword);
            }
        }, 750);
    }

    @Override
    public void renderSearchedRepos(List<Repo> repoList) {
        repoListAdapter.updateAdapterData(repoList);
    }

    @Override
    public void showRepoDetailsScreen(Repo clickedRepo) {
        startActivity(RepoDetailsActivity.getCallingIntent(this, clickedRepo));
    }

    @Override
    public void showLoader() {
        runOnUiThread(() -> progressLoader.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideLoader() {
        runOnUiThread(() -> progressLoader.setVisibility(View.GONE));
    }

    @Override
    public void showReposList() {
        runOnUiThread(() -> repoRecyclerView.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideReposList() {
        runOnUiThread(() -> repoRecyclerView.setVisibility(View.GONE));
    }

    @Override
    public void showSearchFailureError(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show());
    }

    @Override
    public void showNoResultsFoundError() {
        runOnUiThread(() -> Toast.makeText(this, getResources().getString(R.string.no_results_found_error),
                                           Toast.LENGTH_LONG).show());
    }
}


