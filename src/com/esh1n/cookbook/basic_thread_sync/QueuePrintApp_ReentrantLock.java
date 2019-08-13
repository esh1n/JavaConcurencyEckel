package com.esh1n.cookbook.basic_thread_sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueuePrintApp_ReentrantLock {

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(new Job(printQueue), "Thread " + i);
        }
        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }
    }

    public static class PrintQueue {
        private final Lock queueLock = new ReentrantLock();

        public void printJob(Object document) {
            queueLock.lock();
            try {
                Long duration = (long) (Math.random() * 10000);
                String descr = Thread.currentThread().getName() + ": Print Queue: Printing a Job during " +
                        (duration / 1000) + " seconds";
                System.out.println(descr);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }
    }

    public static class Job implements Runnable {

        private PrintQueue printQueue;

        public Job(PrintQueue printQueue) {
            this.printQueue = printQueue;
        }

        @Override
        public void run() {
            System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
            printQueue.printJob(new Object());
            System.out.printf("%s The document has been printed\n", Thread.currentThread().getName());
        }
    }
}
