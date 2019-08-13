package com.esh1n.cookbook.basic_thread_sync;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JoinExample {
    public static void main(String[] args) {
        DataSourceLoader dsLoader = new DataSourceLoader();
        Thread threadD = new Thread(dsLoader, "DataThread");
        NetworkConnectionsLoader nsLoader = new NetworkConnectionsLoader();
        Thread threadN = new Thread(nsLoader, "DataThread");
        threadD.start();
        threadN.start();
        try {
            threadD.join();
            threadN.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Config FINISHED :%s\n", new Date());

    }

    public static class NetworkConnectionsLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Start fetch :%s\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Finish fetch :%s\n", new Date());

        }
    }

    public static class DataSourceLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Start load :%s\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Finish load :%s\n", new Date());

        }
    }

}
