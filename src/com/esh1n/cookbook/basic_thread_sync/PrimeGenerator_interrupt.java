package com.esh1n.cookbook.basic_thread_sync;

public class PrimeGenerator_interrupt extends Thread {
    public static void main(String[] args) {
        Thread thread = new PrimeGenerator_interrupt();
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    @Override
    public void run() {
        long number = 1L;
        while (true) {
            if (isPrime(number)) {
                System.out.printf("Number %d is Prime\n", number);
            }
            if (isInterrupted()) {
                System.out.println("Generator has been interrupted");
                return;
            }
            number++;

        }
    }

    private boolean isPrime(long number) {
        if (number <= 2) {
            return true;
        }
        for (long i = 2; i < number; i++) {
            if ((number % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
