package com.esh1n.daemons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for(int i=0;i<10;i++){
            exec.execute(new SimpleDaemonsDemo.SimpleDaemon());
        }
        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(500);
    }
}
