package com.github.bingoohuang.blackcat.maven.demo;


import com.github.bingoohuang.blackcat.instrument.annotations.BlackcatMonitor;
import com.github.bingoohuang.blackcat.instrument.callback.Blackcat;

@BlackcatMonitor(debug = true)
public class Demo {
    public void printCustom() {
        Blackcat cat = new Blackcat();
        cat.start();
        try {
            String s = "Hello World";
            System.out.println(s);
            cat.finish();
        } catch (Throwable var5) {
            cat.uncaught(var5);
            throw var5;
        }
    }

    public void printCustomLog(String thing) {
        Blackcat.log("give me some {}", thing);
    }
    @BlackcatMonitor
    public void printOne() {
        String s = "Hello World";
        System.out.println(s);
    }

    @BlackcatMonitor
    public void printTwo(String s) {
        printOne();
        printOne();
    }

    @BlackcatMonitor
    public void printException() {
        throw new RuntimeException("A Exception");
    }

    @BlackcatMonitor
    public void printCatch() {
        try {
            printException();
        } catch (Exception e) {
            System.out.println("CATCH Exception: " + e.getMessage());
        }
    }

    @BlackcatMonitor
    public void printTrycatch() {
        try {
            int a = 1 / 0;
            System.out.println(a);
        } catch (RuntimeException ex) {
//            throw ex;
            System.out.println("catch");
        } finally {
            System.out.println("finally");
        }
    }

    public static void main(String[] args) {
        Demo m = new Demo();
//        m.printOne();
//        m.printOne();
//        m.printTwo("123");
//        m.printCatch();
//        m.printTrycatch();
//
        m.printCustom();
        m.printCustomLog("dingoo");
    }
}
