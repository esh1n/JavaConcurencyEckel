package com.esh1n.join;

public class JoningDemo {
    public static void main(String[] args){
       Sleeper sleeper = new Sleeper("Sleepy",1500);
       Sleeper grumpy = new Sleeper("grumpy",1500);

       Joiner dope = new Joiner("Dope",sleeper);
       Joiner doc = new Joiner("Doc",grumpy);

       sleeper.start();
       dope.start();

       grumpy.start();
       doc.start();

       grumpy.interrupt();

    }
}
