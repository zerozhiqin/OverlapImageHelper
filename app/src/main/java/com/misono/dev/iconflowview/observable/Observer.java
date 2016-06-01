package com.misono.dev.iconflowview.observable;

public interface Observer<T> {
    void update(Observable<T> observable, T data);
}
