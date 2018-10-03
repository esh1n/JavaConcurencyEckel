package com.esh1n.join;

public class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
    }

    public void run(){
        try{
            sleep(duration);
        }catch (InterruptedException e){
            System.out.println(getName()+ " was interrupted. "+ "isInterrupted: "+isInterrupted());
            return;
        }
        System.out.println(getName()+ " was awakened");
    }
}
