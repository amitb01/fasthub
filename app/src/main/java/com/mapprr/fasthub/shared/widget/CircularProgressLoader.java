package com.mapprr.fasthub.shared.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.mapprr.fasthub.R;
import com.pnikosis.materialishprogress.ProgressWheel;

public class CircularProgressLoader extends ProgressWheel {

    public CircularProgressLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularProgressLoader(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        spin();
        setBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        setBarWidth((int) context.getResources().getDimension(R.dimen.progress_loader_thickness));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) getContext().getResources().getDimension(R.dimen.progress_loader_width)
                                     + getPaddingLeft() + getPaddingRight(),
                             (int) getContext().getResources().getDimension(R.dimen.progress_loader_height)
                                     + getPaddingBottom() + getPaddingTop());
    }
}
