package com.wang.crawler.Io;

public abstract class Callback {
    private Callback callback;

    public Callback(Callback callback) {
        this.callback = callback;
    }

    public Callback() {
    }

    abstract public void callback(long result);

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
