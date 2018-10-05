package com.esh1n.shared;

public  class EvenChecker implements Runnable{
    private IntGenerator generator;
    private final int id;
    public EvenChecker(IntGenerator g, int ident){
        generator = g;
        id = ident;
    }
    public void run(){
        while(!generator.isCancelled()){
            int val = generator.next();
            if(val % 2 !=0){
                System.out.println(val + "not even!");
                generator.cancel();
            }
        }
        System.out.println("end of generation "+Thread.currentThread().getName());
    }
}
