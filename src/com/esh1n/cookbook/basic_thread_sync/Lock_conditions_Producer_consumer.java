package com.esh1n.cookbook.basic_thread_sync;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Lock_conditions_Producer_consumer {

    public static void main(String[] args) {
        FileMock mock = new FileMock(100, 10);
        Buffer buffer = new Buffer(20);
        Producer producer = new Producer(mock, buffer);
        Thread tProducer = new Thread(producer, "Producer");

        Consumer consumers[] = new Consumer[3];
        Thread tConsumers[] = new Thread[3];

        for (int i = 0; i < 3; i++) {
            consumers[i] = new Consumer(buffer);
            tConsumers[i] = new Thread(consumers[i], "Consumer " + i);
        }
        tProducer.start();
        for (int i = 0; i < 3; i++) {
            tConsumers[i].start();
        }
    }

    public static class FileMock {
        private String[] content;
        private int index;

        public FileMock(int size, int length) {
            content = generateRandomStrings(size, length);
            index = 0;
        }

        private String[] generateRandomStrings(int size, int length) {
            String[] content = new String[size];
            for (int i = 0; i < size; i++) {
                content[i] = generateString(length);
            }
            return content;
        }

        private String generateString(int length) {
            StringBuilder buffer = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int indice = (int) (Math.random() * 255);
                buffer.append((char) indice);
            }
            return buffer.toString();
        }

        public boolean hasMoreLines() {
            return index < content.length;
        }

        public String getLine() {
            if (hasMoreLines()) {
                System.out.println("Mock: " + (content.length - index));
                return content[index++];
            }
            return "";
        }
    }

    public static class Buffer {
        private LinkedList<String> buffer;
        private int maxSize;
        private ReentrantLock lock;
        private Condition lines, space;
        private boolean hasPendingLines;

        public Buffer(int maxSize) {
            this.maxSize = maxSize;
            buffer = new LinkedList<>();
            lock = new ReentrantLock();
            lines = lock.newCondition();
            space = lock.newCondition();
            hasPendingLines = true;
        }

        public boolean isHasPendingLines() {
            return hasPendingLines || buffer.size() > 0;
        }

        public void setHasPendingLines(boolean hasPendingLines) {
            this.hasPendingLines = hasPendingLines;
        }

        public void insert(String line) {
            lock.lock();
            try {
                while (buffer.size() == maxSize) {
                    space.await();
                }
                buffer.offer(line);
                System.out.printf("%s: Inserted line :%d\n", Thread.currentThread().getName(), buffer.size());
                lines.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public String get() {
            String line = null;
            lock.lock();
            try {
                while ((buffer.size() == 0) && hasPendingLines) {
                    lines.await();
                }
                if (hasPendingLines) {
                    line = buffer.poll();
                    System.out.printf("%s: Line Readed %s %d\n",
                            Thread.currentThread().getName(), line, buffer.size());
                    space.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return line;
        }


    }

    public static class Producer implements Runnable {

        private FileMock mock;
        private Buffer buffer;

        public Producer(FileMock mock, Buffer buffer) {
            this.mock = mock;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            buffer.setHasPendingLines(true);
            while (mock.hasMoreLines()) {
                buffer.insert(mock.getLine());
            }
            buffer.setHasPendingLines(false);
        }
    }

    public static class Consumer implements Runnable {
        private Buffer buffer;

        public Consumer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            while (buffer.hasPendingLines) {
                processLine(buffer.get());
            }
        }

        private void processLine(String s) {
            try {
                // System.out.printf("Consume line:  %s\n", s);
                Thread.sleep(new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
