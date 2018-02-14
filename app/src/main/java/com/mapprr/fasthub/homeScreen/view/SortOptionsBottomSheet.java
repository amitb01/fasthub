package com.mapprr.fasthub.homeScreen.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.RadioGroup;

import com.mapprr.fasthub.R;
import com.mapprr.fasthub.homeScreen.model.SortType;

public class SortOptionsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_CURRENT_SORT_TYPE = "current_sort_type";

    private Callback onSortButtonClickListener;
    private RadioGroup radioGroup;

    public static SortOptionsBottomSheet newInstance(SortType currentSortType) {
        SortOptionsBottomSheet sortOptionsBottomSheet = new SortOptionsBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_SORT_TYPE, currentSortType);
        sortOptionsBottomSheet.setArguments(args);

        return sortOptionsBottomSheet;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SortOptionsBottomSheet.Callback) {
            this.onSortButtonClickListener = (Callback) context;
        } else {
            throw new IllegalStateException("Callbacks not implemented");
        }
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        //noinspection RestrictedApi
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.dialog_sort_list_feature, null);
        dialog.setContentView(contentView);

        radioGroup = contentView.findViewById(R.id.radio_group_sort);

        if (getArguments() != null) {
            SortType currentSortType = (SortType) getArguments().getSerializable(ARG_CURRENT_SORT_TYPE);
            setCurrentSelectedRadioButton(currentSortType);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> handleRadioButtonClick(checkedId));
    }

    private void setCurrentSelectedRadioButton(SortType currentSortType) {
        switch (currentSortType) {
            case WATCHERS_ASC:
                radioGroup.check(R.id.watchers_asc);
                break;
            case WATCHERS_DESC:
                radioGroup.check(R.id.watchers_desc);
                break;
            case FORKS_ASC:
                radioGroup.check(R.id.forks_asc);
                break;
            case FORKS_DESC:
                radioGroup.check(R.id.forks_desc);
                break;
            case STARGAZERS_ASC:
                radioGroup.check(R.id.stars_asc);
                break;
            case STARGAZERS_DESC:
                radioGroup.check(R.id.stars_desc);
                break;
        }
    }

    private void handleRadioButtonClick(int checkedId) {
        switch (checkedId) {
            case R.id.watchers_asc:
                onSortButtonClickListener.onSortButtonClicked(SortType.WATCHERS_ASC);
                break;
            case R.id.watchers_desc:
                onSortButtonClickListener.onSortButtonClicked(SortType.WATCHERS_DESC);
                break;
            case R.id.forks_asc:
                onSortButtonClickListener.onSortButtonClicked(SortType.FORKS_ASC);
                break;
            case R.id.forks_desc:
                onSortButtonClickListener.onSortButtonClicked(SortType.FORKS_DESC);
                break;
            case R.id.stars_asc:
                onSortButtonClickListener.onSortButtonClicked(SortType.STARGAZERS_ASC);
                break;
            case R.id.stars_desc:
                onSortButtonClickListener.onSortButtonClicked(SortType.STARGAZERS_DESC);
                break;
        }
    }

    public interface Callback {

        void onSortButtonClicked(SortType sortType);
    }
}
