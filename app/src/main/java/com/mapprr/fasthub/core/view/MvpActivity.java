package com.mapprr.fasthub.core.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapprr.fasthub.core.presenter.MvpPresenter;

import butterknife.ButterKnife;

public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpPresenter.View {

    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResourceId() != -1) {
            setContentView(getLayoutResourceId());
            ButterKnife.bind(this);
        }

        presenter = createPresenter(savedInstanceState);
        getPresenter().attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onViewForeground();
    }

    @Override
    protected void onStop() {
        getPresenter().onViewBackground();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getPresenter().detachView();
        super.onDestroy();
    }

    public abstract int getLayoutResourceId();

    public abstract P createPresenter(Bundle savedInstanceState);

    public final P getPresenter() {
        return presenter;
    }
}
