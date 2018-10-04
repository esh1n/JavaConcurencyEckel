package com.esh1n.shared;

public abstract class IntGenerator {
    private volatile boolean cancelled = false;
    public abstract int next();
    public void cancel(){
        cancelled = true;
    }
    public boolean isCancelled(){
        return cancelled;
    }
}
