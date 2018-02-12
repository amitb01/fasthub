package com.mapprr.fasthub.repoDetailsScreen.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapprr.fasthub.R;
import com.mapprr.fasthub.contributorDetailsScreen.view.ContributorDetailsActivity;
import com.mapprr.fasthub.core.view.MvpActivity;
import com.mapprr.fasthub.repoDetailsScreen.adapter.ContributorListAdapter;
import com.mapprr.fasthub.repoDetailsScreen.model.Contributor;
import com.mapprr.fasthub.repoDetailsScreen.presenter.RepoDetailsPresenter;
import com.mapprr.fasthub.shared.model.Repo;
import com.mapprr.fasthub.shared.util.TextViewLinkHandler;
import com.mapprr.fasthub.shared.webView.view.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RepoDetailsActivity extends MvpActivity<RepoDetailsPresenter>
        implements RepoDetailsPresenter.View {

    private static final String KEY_REPO_DATA = "repo_data";

    private ContributorListAdapter contributorListAdapter;

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.repo_photo)
    ImageView repoPhoto;
    @Bind(R.id.repo_full_name)
    TextView repoFullName;
    @Bind(R.id.repo_link)
    TextView repoLinkTV;
    @Bind(R.id.repo_description)
    TextView repoDescription;
    @Bind(R.id.contributor_list)
    RecyclerView contributorRV;
    @Bind(R.id.progress_loader)
    RelativeLayout progressLoader;

    public static Intent getCallingIntent(Context context, Repo repo) {
        Intent intent = new Intent(context, RepoDetailsActivity.class);
        intent.putExtra(KEY_REPO_DATA, repo);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backBtn.setOnClickListener(view -> onBackPressed());
        initRecyclerView();
    }

    @Override
    public RepoDetailsPresenter createPresenter(Bundle savedInstanceState) {
        Repo repo = (Repo) getIntent().getExtras().getSerializable(KEY_REPO_DATA);
        return new RepoDetailsPresenter(repo);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_repo_details;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contributorRV.setLayoutManager(layoutManager);

        contributorListAdapter = new ContributorListAdapter(new ArrayList<>(),
                                                            contributor -> getPresenter().onContributorClicked(contributor));
        contributorRV.setAdapter(contributorListAdapter);
    }

    @Override
    public void showRepoImage(String imageLink) {
        Glide.with(this)
             .load(imageLink)
             .asBitmap()
             .placeholder(new ColorDrawable(ContextCompat.getColor(this, R.color.grey_light)))
             .into(repoPhoto);
    }

    @Override
    public void showRepoName(String name) {
        repoFullName.setText(name);
    }

    @Override
    public void showRepoLink(String link) {
        repoLinkTV.setText(link);
        repoLinkTV.setMovementMethod(LinkMovementMethod.getInstance());
        repoLinkTV.setMovementMethod(new TextViewLinkHandler() {
            @Override
            public void onLinkClick(String url) {
                getPresenter().onRepoLinkClicked(url);
            }
        });
    }

    @Override
    public void showRepoDescription(String description) {
        repoDescription.setText(description);
    }

    @Override
    public void showWebViewScreen(String url) {
        startActivity(WebViewActivity.getCallingIntent(this, url));
    }

    @Override
    public void showContributorDetailsScreen(Contributor contributor) {
        Contributor.Author author = contributor.getAuthor();
        startActivity(ContributorDetailsActivity.getCallingIntent(this,
                                                                  author.getAvatarUrl(),
                                                                  author.getLogin(),
                                                                  author.getReposUrl()));
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
    public void showContributorsList(List<Contributor> contributorList) {
        runOnUiThread(() -> {
            contributorRV.setVisibility(View.VISIBLE);
            contributorListAdapter.updateAdapterData(contributorList);
        });
    }

    @Override
    public void hideContributorsList() {
        runOnUiThread(() -> contributorRV.setVisibility(View.GONE));
    }

    @Override
    public void showContributorFetchError(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show());
    }

    @Override
    public void showNoContributorsFoundError() {
        runOnUiThread(() -> Toast.makeText(this, getResources().getString(R.string.no_contributors_found_error),
                                           Toast.LENGTH_LONG).show());
    }
}
