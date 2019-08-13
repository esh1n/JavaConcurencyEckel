package com.esh1n.cookbook.basic_thread_sync;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class DaemonExample {
    public static void main(String[] args) {
        Deque<Event> deque = new ArrayDeque<>();
        Writer writer = new Writer(deque);
        for (int index = 0; index < 3; index++) {
            Thread thread = new Thread(writer);
            thread.start();
        }
        CleanerTask cleanerTask = new CleanerTask(deque);
        cleanerTask.start();
    }

    static class Event {
        private String description;
        private Date date;

        public Event(String description, Date date) {
            this.date = date;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public static class Writer implements Runnable {

        private Deque<Event> deque;

        Writer(Deque<Event> deque) {
            this.deque = deque;
        }

        @Override
        public void run() {
            for (int index = 0; index < 30; index++) {
                String description = String.format("Thread %s generated an event", Thread.currentThread().getId());
                deque.addFirst(new Event(description, new Date()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class CleanerTask extends Thread {
        private Deque<Event> deque;

        CleanerTask(Deque<Event> deque) {
            this.deque = deque;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                Date date = new Date();
                clean(date.getTime());
            }
        }

        private void clean(long date) {
            long difference;
            if (deque.size() == 0) {
                return;
            }
            do {
                Event e = deque.getLast();
                difference = date - e.getDate().getTime();
                if (difference > TimeUnit.SECONDS.toMillis(2)) {
                    System.out.printf("Cleaned event %s\n", e.getDescription());
                    deque.removeLast();
                    System.out.printf("Deque size  : %d\n", deque.size());
                } else {
                    // System.out.print("Event is Actual - new loop\n");
                }
            } while (difference > 10000);

        }

    }
}
