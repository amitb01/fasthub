package com.mapprr.fasthub.contributorDetailsScreen.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapprr.fasthub.R;
import com.mapprr.fasthub.contributorDetailsScreen.presenter.ContributorDetailsPresenter;
import com.mapprr.fasthub.core.view.MvpActivity;
import com.mapprr.fasthub.repoDetailsScreen.view.RepoDetailsActivity;
import com.mapprr.fasthub.shared.adapter.RepoListAdapter;
import com.mapprr.fasthub.shared.model.Repo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ContributorDetailsActivity extends MvpActivity<ContributorDetailsPresenter>
        implements ContributorDetailsPresenter.View {

    private static final String KEY_CONTRIBUTOR_AVATAR_URL = "avatar_url";
    private static final String KEY_CONTRIBUTOR_NAME = "contributor_name";
    private static final String KEY_CONTRIBUTOR_REPOS_URL = "repos_url";

    private RepoListAdapter repoListAdapter;

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.contributor_photo)
    ImageView contributorPhoto;
    @Bind(R.id.contributor_name)
    TextView contributorNameTV;
    @Bind(R.id.list_repos)
    RecyclerView repoListRV;
    @Bind(R.id.progress_loader)
    RelativeLayout progressLoader;

    public static Intent getCallingIntent(Context context, String contributorAvatarUrl,
                                          String contributorName, String contributorReposUrl) {
        Intent intent = new Intent(context, ContributorDetailsActivity.class);
        intent.putExtra(KEY_CONTRIBUTOR_AVATAR_URL, contributorAvatarUrl);
        intent.putExtra(KEY_CONTRIBUTOR_NAME, contributorName);
        intent.putExtra(KEY_CONTRIBUTOR_REPOS_URL, contributorReposUrl);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backBtn.setOnClickListener(view -> onBackPressed());
        initRecyclerView();
    }

    @Override
    public ContributorDetailsPresenter createPresenter(Bundle savedInstanceState) {
        String contributorAvatarUrl = getIntent().getStringExtra(KEY_CONTRIBUTOR_AVATAR_URL);
        String contributorName = getIntent().getStringExtra(KEY_CONTRIBUTOR_NAME);
        String contributorReposUrl = getIntent().getStringExtra(KEY_CONTRIBUTOR_REPOS_URL);

        return new ContributorDetailsPresenter(contributorAvatarUrl, contributorName, contributorReposUrl);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_contributor_details;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        repoListRV.setLayoutManager(layoutManager);

        repoListAdapter = new RepoListAdapter(new ArrayList<>(), repo -> getPresenter().onRepoClicked(repo),
                                              RepoListAdapter.Source.CONTRIBUTORS_SCREEN);
        repoListRV.setAdapter(repoListAdapter);
    }

    @Override
    public void showContributorImage(String imageLink) {
        Glide.with(this)
             .load(imageLink)
             .asBitmap()
             .placeholder(new ColorDrawable(ContextCompat.getColor(this, R.color.grey_light)))
             .into(contributorPhoto);
    }

    @Override
    public void showContributorName(String name) {
        contributorNameTV.setText(name);
    }

    @Override
    public void showContributorRepos(List<Repo> repoList) {
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
        runOnUiThread(() -> repoListRV.setVisibility(View.VISIBLE));
    }

    @Override
    public void hideReposList() {
        runOnUiThread(() -> repoListRV.setVisibility(View.GONE));
    }

    @Override
    public void showRepoFetchError(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show());
    }

    @Override
    public void showNoReposFoundError() {
        runOnUiThread(() -> Toast.makeText(this, getResources().getString(R.string.no_repos_found_error),
                                           Toast.LENGTH_LONG).show());
    }
}
