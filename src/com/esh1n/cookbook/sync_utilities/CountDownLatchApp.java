package com.esh1n.cookbook.sync_utilities;

import com.esh1n.SysUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchApp {
    private static final int PARTICIPANTS = 7;

    public static void main(String[] args) {
        String[] names = new String[]{"Alex", "Dima", "Sergey", "Ivan", "Maxim", "Petr", "Anna"};
        VideoConference videoConference = new VideoConference(PARTICIPANTS);
        Thread conferenceThread = new Thread(videoConference);
        conferenceThread.start();

        for (int i = 0; i < PARTICIPANTS; i++) {
            Thread participantThread = new Thread(new Participant(names[i], videoConference));
            participantThread.start();
        }
    }

    public static class VideoConference implements Runnable {


        private final CountDownLatch controller;

        public VideoConference(int count) {
            controller = new CountDownLatch(count);
        }

        @Override
        public void run() {
            System.out.printf("VideoConference: Initialization: %d participants .\n", controller.getCount());
            try {
                controller.await();
                System.out.printf("VideoConference: All the participants have come\n");
                System.out.printf("VideoConference: Let's start");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO let's play with synchronized
        public synchronized void arrive(String name) {
            System.out.printf("%s has arrived.\n", name);
            controller.countDown();
            System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());
        }
    }

    public static class Participant implements Runnable {

        private String name;
        private VideoConference conference;

        public Participant(String name, VideoConference conference) {
            this.name = name;
            this.conference = conference;
        }

        @Override
        public void run() {
            long duration = (long) (Math.random() * 10);
            SysUtil.sleep(TimeUnit.SECONDS.toMillis(duration));
            conference.arrive(name);
        }
    }
}
