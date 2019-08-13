package com.esh1n.cookbook.sync_utilities;

import com.esh1n.SysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Phaser;

public class PhaserApp {

    public static void main(String[] args) {
        String end = "log";
        Phaser phaser = new Phaser(3);
        Thread javaThread = createSearchThread("Java", phaser, "log");
        Thread pythonThread = createSearchThread("python", phaser, "log");
        Thread phpThread = createSearchThread("php", phaser, "log");
        List<Thread> threads = Arrays.asList(javaThread, phpThread, pythonThread);
        for (Thread thread : threads) {
            SysUtil.sleep(100);
            thread.start();
        }
        try {
            javaThread.join();
            phpThread.join();
            pythonThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Terminated: " + phaser.isTerminated());
    }

    private static Thread createSearchThread(String name, Phaser phaser, String end) {
        File folderJava = mockFiles("Folder" + name, end);
        FileSearch javaFileSearch = new FileSearch(folderJava, end, phaser);
        return new Thread(javaFileSearch, name);
    }

    private static File mockFiles(String folderName, String end) {
        int filesCount = 15;
        List<File> files = new ArrayList<>(filesCount);
        for (int i = 0; i < filesCount; i++) {
            files.add(new File(false, "File" + i + end, null));
        }
        List<File> folders = new ArrayList<>(filesCount);
        for (int i = 0; i < filesCount; i++) {
            folders.add(new File(true, "Folder" + i, files));
        }
        return new File(true, "folderName", folders);
    }

    public static class File {
        private List<File> files;
        private boolean isDirectory;
        private String description;

        public File(boolean isDirectory, String description, List<File> files) {
            this.isDirectory = isDirectory;
            this.description = description;
            this.files = files;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public String getDescription() {
            return description;
        }

        public List<File> listFiles() {
            return files;
        }

    }

    public static class FileSearch implements Runnable {

        private File initFile;
        private String end;
        private List<String> results;
        private Phaser phaser;

        public FileSearch(File initFile, String end, Phaser phaser) {
            this.initFile = initFile;
            this.end = end;
            this.phaser = phaser;
            results = new ArrayList<>();
        }

        @Override
        public void run() {
            phaser.arriveAndAwaitAdvance();
            System.out.printf("%s: Starting.\n", Thread.currentThread().
                    getName());
            if (initFile.isDirectory()) {
                directoryProcess(initFile);
            }
            if (!checkResults()) {
                return;
            }
            filterResults();
            if (!checkResults()) {
                return;
            }
            showInfo();
            phaser.arriveAndDeregister();
            System.out.printf("%s : Work completed.\n", Thread.currentThread().getName());
        }

        private void directoryProcess(File initFile) {
            List<File> list = initFile.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory()) {
                        directoryProcess(file);
                    } else {
                        fileProcess(file);
                    }
                }
            }
        }

        private void fileProcess(File file) {
            SysUtil.sleep(100);
            if (file.getDescription().endsWith(end)) {
                results.add(file.getDescription());
            }
        }

        private void filterResults() {
            List<String> newResults = new ArrayList<>();
            for (String result : results) {
                if (result.contains("File")) {
                    newResults.add(result);
                }
            }
            results = newResults;
        }

        private boolean checkResults() {
            if (results.isEmpty()) {
                System.out.printf("%s: Phase %d: 0 results.\n", Thread.
                        currentThread().getName(), phaser.getPhase());
                System.out.printf("%s: Phase %d: End.\n", Thread.
                        currentThread().getName(), phaser.getPhase());
                phaser.arriveAndDeregister();
                return false;
            } else {
                System.out.printf("%s: Phase %d: %d results.\n", Thread.
                        currentThread().getName(), phaser.getPhase(), results.size());
                phaser.arriveAndAwaitAdvance();
                return true;
            }
        }

        private void showInfo() {
            System.out.printf("%s start of results.\n", Thread.currentThread().getName());
            for (String result : results) {
                System.out.printf("%s: %s", Thread.currentThread().getName(), result);
            }
            System.out.println("end of results");
            phaser.arriveAndAwaitAdvance();
        }
    }
}
