package com.esh1n.daemons;

import java.util.concurrent.TimeUnit;

public class SimpleDaemonsDemo {

    static class SimpleDaemon implements Runnable {

        @Override
        public void run() {
            try {
                while (true){
                    TimeUnit.MILLISECONDS.sleep(100);
                    System.out.println(Thread.currentThread()+ " "+this);
                }

            }catch (InterruptedException e){
                System.out.println("Sleep() interrupted");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
            Thread daemon = new Thread(new SimpleDaemon());
            daemon.setDaemon(true);
            daemon.start();
        }
        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(200);
    }
}

