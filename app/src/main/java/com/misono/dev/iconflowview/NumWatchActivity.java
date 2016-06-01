package com.misono.dev.iconflowview;

import android.app.Activity;
import android.os.Bundle;

import com.misono.dev.iconflowview.observable.Observable;
import com.misono.dev.iconflowview.observable.Observer;


public class NumWatchActivity extends Activity implements Observer<Integer> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NumCache.getInstance().addObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NumCache.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable<Integer> observable, Integer data) {
        if (observable == NumCache.getInstance()) {
            // TODO: 16/6/1 .....
        }
    }
}