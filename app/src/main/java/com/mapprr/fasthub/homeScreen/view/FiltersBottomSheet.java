package com.mapprr.fasthub.homeScreen.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapprr.fasthub.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FiltersBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";

    private CardView startDateBtn;
    private CardView endDateBtn;
    private TextView startDateTV;
    private TextView endDateTV;

    private Date startDate;
    private Date endDate;
    private Calendar startDateCalender;
    private Calendar endDateCalender;

    private Callback dateRangeListener;

    public static FiltersBottomSheet newInstance(Date startDate, Date endDate) {
        FiltersBottomSheet filtersBottomSheet = new FiltersBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_START_DATE, startDate);
        args.putSerializable(ARG_END_DATE, endDate);
        filtersBottomSheet.setArguments(args);

        return filtersBottomSheet;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FiltersBottomSheet.Callback) {
            this.dateRangeListener = (FiltersBottomSheet.Callback) context;
        } else {
            throw new IllegalStateException("Callbacks not implemented");
        }
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        //noinspection RestrictedApi
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.dialog_filters, null);
        dialog.setContentView(contentView);

        startDateBtn = contentView.findViewById(R.id.btn_start_date);
        endDateBtn = contentView.findViewById(R.id.btn_end_date);
        startDateTV = contentView.findViewById(R.id.tv_start_date);
        endDateTV = contentView.findViewById(R.id.tv_end_date);
        TextView refreshBtn = contentView.findViewById(R.id.btn_refresh);
        TextView applyBtn = contentView.findViewById(R.id.btn_apply);

        if (getArguments() != null) {
            startDate = (Date) getArguments().getSerializable(ARG_START_DATE);
            endDate = (Date) getArguments().getSerializable(ARG_END_DATE);
        }

        initializeStartDate();
        initializeEndDate();

        refreshBtn.setOnClickListener(view -> handleRefreshBtnClick());
        applyBtn.setOnClickListener(view -> dateRangeListener.onDateRangeSet(startDate, endDate));
    }

    private void handleRefreshBtnClick() {
        startDate = endDate = null;
        startDateCalender.setTime(new Date());
        endDateCalender.setTime(new Date());
        startDateTV.setText("DD/MM/YYYY");
        endDateTV.setText("DD/MM/YYYY");
    }

    private void initializeStartDate() {
        startDateBtn.setOnClickListener(view -> onStartDateBtnClicked());

        startDateCalender = Calendar.getInstance();
        if (startDate != null) {
            updateLabel(startDate, startDateTV);
            startDateCalender.setTime(startDate);
        } else {
            startDateCalender.setTime(new Date());
        }
    }

    private void initializeEndDate() {
        endDateBtn.setOnClickListener(view -> onEndDateBtnClicked());

        endDateCalender = Calendar.getInstance();
        if (endDate != null) {
            updateLabel(endDate, endDateTV);
            endDateCalender.setTime(endDate);
        } else {
            endDateCalender.setTime(new Date());
        }
    }

    private void updateLabel(Date date, TextView label) {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        label.setText(sdf.format(date));
    }

    private void onStartDateBtnClicked() {
        new DatePickerDialog(getContext(),
                             (view, year, monthOfYear, dayOfMonth) -> handleStartDateSet(year, monthOfYear, dayOfMonth),
                             startDateCalender.get(Calendar.YEAR),
                             startDateCalender.get(Calendar.MONTH),
                             startDateCalender.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleStartDateSet(int year, int monthOfYear, int dayOfMonth) {
        startDateCalender.set(Calendar.YEAR, year);
        startDateCalender.set(Calendar.MONTH, monthOfYear);
        startDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        startDate = startDateCalender.getTime();
        updateLabel(startDate, startDateTV);
    }

    private void onEndDateBtnClicked() {
        new DatePickerDialog(getContext(),
                             (view, year, monthOfYear, dayOfMonth) -> handleEndDateSet(year, monthOfYear, dayOfMonth),
                             endDateCalender.get(Calendar.YEAR),
                             endDateCalender.get(Calendar.MONTH),
                             endDateCalender.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleEndDateSet(int year, int monthOfYear, int dayOfMonth) {
        endDateCalender.set(Calendar.YEAR, year);
        endDateCalender.set(Calendar.MONTH, monthOfYear);
        endDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        endDate = endDateCalender.getTime();
        updateLabel(endDate, endDateTV);
    }

    public interface Callback {

        void onDateRangeSet(Date startDate, Date endDate);
    }

}
