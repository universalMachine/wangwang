package com.wang.crawler.Io;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Phaser;
import java.util.concurrent.RunnableFuture;

public class PhaserFuture extends FutureTask {
    private final Phaser phaser;
    public PhaserFuture(Callable callable, Phaser phaser) {
        super(callable);
        this.phaser = phaser;
    }

    @Override
    protected void done() {
        //System.out.println("done");
       phaser.arriveAndDeregister();
    }
}
