package com.esh1n.shared;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenCheckerDemo {



    private final static int count = 10;

    public static void test(IntGenerator gen){
       System.out.println("exit by ctrl c");
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0;i<count;i++){
            executorService.execute(new EvenChecker(gen,i));
        }
        executorService.shutdown();
    }

    public static void main(String[] args){
        System.out.println("start unsync even generator.Seems to end soon");
        test(new EvenGenerator());

        System.out.println("start sync even generator.Seems to work forever");
        test(new SynchronizedEvenGenerator());

        System.out.println("start mutex sync even generator.Seems to work forever");
        test(new MutexEvenGenerator());
    }
}

