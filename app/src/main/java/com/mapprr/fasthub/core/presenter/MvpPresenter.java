package com.mapprr.fasthub.core.presenter;

import android.util.Log;

import java.lang.ref.WeakReference;

public abstract class MvpPresenter<V extends MvpPresenter.View> {

    private WeakReference<V> viewWeakRef;
    private boolean isViewForeground;

    /**
     * Attach the view to the presenter. The presenter holds a weak reference to the view.
     * This is called from the relevant lifecycle method in the view.
     */
    public void attachView(V view) {
        if (view == null) {
            throw new IllegalArgumentException("Cannot attach a null view to the presenter");
        }
        this.viewWeakRef = new WeakReference<>(view);
        Log.i("%s attachView()", this.getClass().getSimpleName());
    }

    /**
     * Returns the view reference if attached, else null. Use the {@link #isViewAttached()} method
     * before calling this.
     */
    protected V getView() {
        return viewWeakRef != null ? viewWeakRef.get() : null;
    }

    /**
     * Returns a boolean stating whether the view is still attached or not
     */
    protected boolean isViewAttached() {
        return getView() != null;
    }

    /**
     * This method is called when the view comes to the foreground.
     */
    public void onViewForeground() {
        isViewForeground = true;
        Log.i("%s onViewForeground()", this.getClass().getSimpleName());
    }

    /**
     * This method is called when the view goes in the background / another view comes to the foreground.
     * The view is still attached to the presenter.
     */
    public void onViewBackground() {
        isViewForeground = false;
        Log.i("%s onViewBackground()", this.getClass().getSimpleName());
    }

    protected boolean isViewForeground() {
        return isViewForeground;
    }

    /**
     * Detach the view from the presenter.
     * This is called from the relevant lifecycle method in the view.
     */
    public void detachView() {
        if (viewWeakRef != null) {
            viewWeakRef.clear();
            viewWeakRef = null;
        }
    }

    public interface View {

    }
}

