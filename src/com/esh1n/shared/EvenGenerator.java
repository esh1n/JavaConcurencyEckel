package com.esh1n.shared;

public class EvenGenerator extends IntGenerator {

    private int currentValue = 0;

    @Override
    public int next() {
        ++currentValue;
        Thread.yield();
        ++currentValue;
        return currentValue;
    }
}
