package com.esh1n.join;

public class Joiner extends Thread{
    private Sleeper sleeper;
    public Joiner(String name, Sleeper sleeper){
        super(name);
        this.sleeper = sleeper;
    }
    public void run(){
        try{
            sleeper.join();
        }catch (InterruptedException e){
            System.out.println("Joiner was interrupted" );
        }
        System.out.println(getName()+ " join completed" );
    }

}
