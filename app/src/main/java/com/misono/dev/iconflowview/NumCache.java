package com.misono.dev.iconflowview;


import com.misono.dev.iconflowview.observable.Observable;

public class NumCache extends Observable<Integer> {

    private static final NumCache instance = new NumCache();
    int num;

    public static NumCache getInstance() {
        return instance;
    }

    public void setNum(int num) {
        if (num != this.num) {
            setChanged();
            notifyObservers(num);
        }
        this.num = num;
    }
}
