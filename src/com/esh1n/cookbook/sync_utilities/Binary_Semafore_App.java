package com.esh1n.cookbook.sync_utilities;

import java.util.concurrent.Semaphore;

public class Binary_Semafore_App {
    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Job(printQueue), "Thread " + i);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
    }

    public static class PrintQueue {
        private final Semaphore semaphore;

        public PrintQueue() {
            this.semaphore = new Semaphore(1);
        }

        public void printJob(Object document) {
            try {
                semaphore.acquire();
                long duration = (long) (Math.random() * 10);
                System.out.printf("%s: PrintQueue : Printing a Job during %d seconds\n", Thread.currentThread().getName(), duration);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
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
            String name = Thread.currentThread().getName();
            System.out.printf("%s: Going to print a job\n", name);
            printQueue.printJob(new Object());
            System.out.printf("%s,The doc has been printed\n", name);
        }
    }
}
