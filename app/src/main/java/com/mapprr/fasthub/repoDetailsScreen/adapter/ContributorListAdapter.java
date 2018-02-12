package com.mapprr.fasthub.repoDetailsScreen.adapter;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mapprr.fasthub.R;
import com.mapprr.fasthub.repoDetailsScreen.model.Contributor;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContributorListAdapter extends RecyclerView.Adapter<ContributorListAdapter.ContributorViewHolder> {

    private List<Contributor> contributorList;
    private OnContributorClickListener contributorClickListener;

    public ContributorListAdapter(List<Contributor> contributorList,
                                  OnContributorClickListener contributorClickListener) {
        this.contributorList = contributorList;
        this.contributorClickListener = contributorClickListener;
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }

    @Override
    public ContributorListAdapter.ContributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contributor, parent, false);
        return new ContributorListAdapter.ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContributorListAdapter.ContributorViewHolder holder, int position) {
        holder.bindView(contributorList.get(position));
    }

    public void updateAdapterData(List<Contributor> contributorList) {
        this.contributorList = contributorList;
        notifyDataSetChanged();
    }

    class ContributorViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.contributor_container)
        LinearLayout contributorContainer;
        @Bind(R.id.contributor_photo)
        ImageView contributorPhoto;
        @Bind(R.id.contributor_name)
        TextView contributorName;

        ContributorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Contributor contributor) {
            Glide.clear(contributorPhoto);
            Glide.with(itemView.getContext())
                 .load(contributor.getAuthor().getAvatarUrl())
                 .asBitmap()
                 .placeholder(new ColorDrawable(ContextCompat.getColor(itemView.getContext(), R.color.grey_light)))
                 .into(contributorPhoto);

            contributorName.setText(contributor.getAuthor().getLogin());

            contributorContainer.setOnClickListener(v -> onRepoClicked(contributor));
        }

        private void onRepoClicked(Contributor clickedContributor) {
            if (contributorClickListener != null) {
                contributorClickListener.onContributorClicked(clickedContributor);
            }
        }
    }

    public interface OnContributorClickListener {

        void onContributorClicked(Contributor contributor);
    }
}
