package com.esh1n.cookbook.sync_utilities;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierApp {

    private static final int ROWS = 10000;
    private static final int NUMBERS = 1000;
    private static final int SEARCH = 5;
    private static final int PARTICIPANTS = 5;
    private static final int LINES_PARTICIPANTS = 2000;

    public static void main(String[] args) {
        MatrixMock matrixMock = new MatrixMock(ROWS, NUMBERS, SEARCH);
        Results results = new Results(ROWS);
        Grouper grouper = new Grouper(results);

        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS, grouper);
        Searcher[] searchers = new Searcher[PARTICIPANTS];
        for (int i = 0; i < PARTICIPANTS; i++) {
            int startIndex = i * LINES_PARTICIPANTS;
            searchers[i] = new Searcher(startIndex, startIndex + LINES_PARTICIPANTS, matrixMock, results, SEARCH, barrier);
            new Thread(searchers[i]).start();
        }
        System.out.printf("Main: The main thread has finished.\n");
    }

    public static class MatrixMock {

        private int data[][];

        public MatrixMock(int size, int length, int number) {
            data = initData(size, length);
            int counter = calculateOccurrences(data, number);
            System.out.printf("Mock: There are %d occurrences of number in generated data.\n", counter);

        }


        private int calculateOccurrences(int[][] data, int number) {
            int counter = 0;
            for (int i = 0; i < data.length; i++) {
                int length = data[i].length;
                for (int j = 0; j < length; j++) {
                    if (data[i][j] == number) {
                        counter++;
                    }
                }
            }
            return counter;
        }

        private int[][] initData(int size, int length) {
            int[][] data = new int[size][length];
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < length; j++) {
                    data[i][j] = random.nextInt(10);
                }
            }
            return data;
        }

        public int[] getRow(int row) {
            if (row >= 0 && data[row] != null) {
                return data[row];
            }
            return null;
        }
    }

    public static class Results {

        private int data[];

        public Results(int size) {
            data = new int[size];
        }

        public void setData(int position, int value) {
            data[position] = value;
        }

        public int[] getData() {
            return data;
        }
    }

    public static class Searcher implements Runnable {

        private final CyclicBarrier barrier;
        private int firstRow;
        private int lastRow;
        private MatrixMock mock;
        private Results results;
        private int number;

        public Searcher(int firstRow, int lastRow, MatrixMock mock, Results results, int number, CyclicBarrier barrier) {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.mock = mock;
            this.results = results;
            this.number = number;
            this.barrier = barrier;
        }

        @Override
        public void run() {

            String name = Thread.currentThread().getName();
            System.out.printf("%s: Processing lines from %d to %d.\n", name, firstRow, lastRow);
            searching();
            System.out.printf("%s :Lines processed.\n", name);
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void searching() {
            int counter = 0;
            for (int i = firstRow; i < lastRow; i++) {
                int row[] = mock.getRow(i);
                if (row != null) {
                    counter = 0;
                    for (int j = 0; j < row.length; j++) {
                        if (row[j] == number) {
                            counter++;
                        }
                    }
                }
                results.setData(i, counter);
            }
        }
    }

    public static class Grouper implements Runnable {

        private Results results;

        public Grouper(Results results) {
            this.results = results;
        }

        @Override
        public void run() {
            int finalResult = 0;
            System.out.printf("Grouper: Processing results...\n");
            for (int number : results.getData()) {
                finalResult += number;
            }
            System.out.printf("Grouper: Total result : %d.\n", finalResult);
        }
    }

}
