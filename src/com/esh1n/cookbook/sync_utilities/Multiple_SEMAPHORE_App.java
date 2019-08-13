package com.esh1n.cookbook.sync_utilities;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Multiple_SEMAPHORE_App {
    public static void main(String[] args) {
        Binary_Semafore_App.PrintQueue printQueue = new Binary_Semafore_App.PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Binary_Semafore_App.Job(printQueue), "Thread " + i);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
    }

    public static class PrintQueue {
        private static final int PRINTERS_COUNT = 3;
        private final Semaphore semaphore;
        private boolean freePrinters[];
        private Lock lockPrinters;

        public PrintQueue() {
            this.semaphore = new Semaphore(PRINTERS_COUNT);
            freePrinters = initPrintersFlags();
            lockPrinters = new ReentrantLock();
        }

        private boolean[] initPrintersFlags() {
            boolean[] freePrinters = new boolean[PRINTERS_COUNT];
            for (int i = 0; i < PRINTERS_COUNT; i++) {
                freePrinters[i] = true;
            }
            return freePrinters;
        }

        public void printJob(Object document) {
            try {
                semaphore.acquire();
                int assignedPrinter = getPrinter();
                long duration = (long) (Math.random() * 10);
                String name = Thread.currentThread().getName();
                System.out.printf("%s: PrintQueue : Printing a Job during %d seconds\n", name, duration);
                TimeUnit.SECONDS.sleep(duration);
                freePrinters[assignedPrinter] = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

        public int getPrinter() {
            try {
                lockPrinters.lock();
                for (int i = 0; i < PRINTERS_COUNT; i++) {
                    if (freePrinters[i]) {
                        freePrinters[i] = false;
                        return i;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lockPrinters.unlock();
            }
            return -1;
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
