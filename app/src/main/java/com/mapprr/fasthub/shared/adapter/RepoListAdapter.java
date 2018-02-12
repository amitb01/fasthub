package com.mapprr.fasthub.shared.adapter;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mapprr.fasthub.R;
import com.mapprr.fasthub.shared.model.Repo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

    public enum Source {
        HOME_SCREEN, CONTRIBUTORS_SCREEN;
    }

    private List<Repo> repoList;
    private OnRepoClickListener repoClickListener;
    private Source launchSource;

    public RepoListAdapter(List<Repo> repoList, OnRepoClickListener repoClickListener, Source launchSource) {
        this.repoList = repoList;
        this.repoClickListener = repoClickListener;
        this.launchSource = launchSource;
    }

    @Override
    public int getItemCount() {
        if (launchSource == Source.HOME_SCREEN) {
            if (repoList.size() > 10) {
                return 10;
            }
        }

        return repoList.size();
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.bindView(repoList.get(position));
    }

    public void updateAdapterData(List<Repo> repoList) {
        this.repoList = repoList;
        notifyDataSetChanged();
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.repo_container)
        CardView repoContainer;
        @Bind(R.id.photo)
        ImageView repoPhoto;
        @Bind(R.id.name)
        TextView repoName;
        @Bind(R.id.full_name)
        TextView repoFullName;
        @Bind(R.id.watchers_count)
        TextView watchersCount;
        @Bind(R.id.fork_count)
        TextView forkCount;
        @Bind(R.id.stars_count)
        TextView starsCount;

        RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Repo currentRepo) {
            Glide.clear(repoPhoto);
            Glide.with(itemView.getContext())
                 .load(currentRepo.getOwner().getAvatarUrl())
                 .asBitmap()
                 .placeholder(new ColorDrawable(ContextCompat.getColor(itemView.getContext(), R.color.grey_light)))
                 .into(repoPhoto);

            repoName.setText(currentRepo.getName());
            repoFullName.setText(currentRepo.getFullName());
            watchersCount.setText(String.valueOf(currentRepo.getWatchersCount()));
            forkCount.setText(String.valueOf(currentRepo.getForksCount()));
            starsCount.setText(String.valueOf(currentRepo.getStargazersCount()));

            repoContainer.setOnClickListener(v -> onRepoClicked(currentRepo));
        }

        private void onRepoClicked(Repo clickedRepo) {
            if (repoClickListener != null) {
                repoClickListener.onRepoClicked(clickedRepo);
            }
        }
    }

    public interface OnRepoClickListener {

        void onRepoClicked(Repo repo);
    }
}
