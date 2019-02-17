package ru.svetomech.currencyconverter.ui.shared;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class MvpPresenter<T extends IMvpView> {

    private WeakReference<T> mView;

    public void attachView(@NonNull T view) {
        mView = new WeakReference<>(view);
        onViewAttached();
    }

    protected void onViewAttached() {

    }

    public void detachView() {
        mView.clear();
    }

    @Nullable
    public T getView() {
        return mView.get();
    }

}
