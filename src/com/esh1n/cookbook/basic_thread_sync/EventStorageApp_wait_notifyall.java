package com.esh1n.cookbook.basic_thread_sync;

import java.util.Date;
import java.util.LinkedList;

public class EventStorageApp_wait_notifyall {
    public static void main(String[] args) {
        EventStorage eventStorage = new EventStorage();
        Producer producer = new Producer(eventStorage);
        Thread producerThread = new Thread(producer);

        Consumer consumer = new Consumer(eventStorage);
        Thread consumerThread = new Thread(consumer);

        consumerThread.start();
        producerThread.start();

    }

    public static class EventStorage {

        private int maxSize;
        private LinkedList<Date> storage;

        public EventStorage() {
            maxSize = 10;
            storage = new LinkedList<>();
        }

        public synchronized void set() {
            while (storage.size() == maxSize) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            storage.offer(new Date());
            System.out.printf("Set :%d\n", storage.size());
            notifyAll();
        }

        public synchronized void get() {
            while (storage.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String descr = String.format("Get: %d : %s", storage.size(), storage.poll());
            System.out.println(descr);
            notifyAll();
        }

    }

    public static class Producer implements Runnable {

        private EventStorage storage;

        public Producer(EventStorage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                storage.set();
            }
        }
    }

    public static class Consumer implements Runnable {
        private EventStorage storage;

        public Consumer(EventStorage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                storage.get();
            }
        }
    }
}
