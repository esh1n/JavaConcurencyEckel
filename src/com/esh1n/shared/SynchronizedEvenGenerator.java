package com.esh1n.shared;

public class SynchronizedEvenGenerator extends IntGenerator {

    private int currentValue = 0;

    @Override
    public synchronized int next() {
        ++currentValue;
        ++currentValue;
        return currentValue;
    }
}
