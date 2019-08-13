package com.esh1n.cookbook.basic_thread_sync;

import com.esh1n.SysUtil;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock_PricesInfo {
    public static void main(String[] args) {
        PricesInfo pricesInfo = new PricesInfo();
        Reader[] readers = new Reader[5];
        Thread[] threadsReader = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readers[i] = new Reader(pricesInfo);
            threadsReader[i] = new Thread(readers[i]);
        }

        Writer writer = new Writer(pricesInfo);
        Thread threadWriter = new Thread(writer);

        for (int i = 0; i < 5; i++) {
            threadsReader[i].start();
        }
        threadWriter.start();
    }

    public static class PricesInfo {
        private double price1, price2;
        private ReadWriteLock lock;

        public PricesInfo() {
            price1 = 1.0;
            price2 = 2.0;
            lock = new ReentrantReadWriteLock();
        }

        public double getPrice1() {
            lock.readLock().lock();
            double value = price1;
            lock.readLock().unlock();
            return value;
        }

        public double getPrice2() {
            lock.readLock().lock();
            double value = price2;
            lock.readLock().unlock();
            return value;
        }

        public void setPrices(double price1, double price2) {
            lock.writeLock().lock();
            this.price1 = price1;
            this.price2 = price2;
            lock.writeLock().unlock();
        }
    }

    public static class Reader implements Runnable {

        private PricesInfo pricesInfo;

        public Reader(PricesInfo pricesInfo) {
            this.pricesInfo = pricesInfo;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                String threadName = Thread.currentThread().getName();
                String price1 = String.format("%s: Price 1: %f\n", threadName, pricesInfo.getPrice1());
                String price2 = String.format("%s: Price 2: %f\n", threadName, pricesInfo.getPrice2());
                System.out.println(price1 + "\n" + price2);
            }
        }
    }

    public static class Writer implements Runnable {

        private PricesInfo pricesInfo;

        public Writer(PricesInfo pricesInfo) {
            this.pricesInfo = pricesInfo;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("Writer: Attempt to modify the prices. \n");
                pricesInfo.setPrices(Math.random() * 10, Math.random() * 8);
                System.out.println("Writer: prices have been modified.");
                SysUtil.sleep(2);
            }
        }
    }
}
