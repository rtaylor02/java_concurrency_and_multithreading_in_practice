package com.rtaylor02;


public class Chapter4 {
    public static void main(String[] args) {
        Lesson1_Thread_ThreadLifeCycle.main();
    }

    public static class Lesson1_Thread_ThreadLifeCycle {
        public static void main() {
            System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().getState()); 

            Runnable runnable = () -> {
                System.out.println("From runnable: " + Thread.currentThread().getName() + ": " + Thread.currentThread().getState());
                try {
                    System.out.println(Thread.currentThread().getName() + " is going to sleep...");
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    System.out.println("Who woke me up??");
                }
            };

            Thread thread = new Thread(runnable);
            System.out.println("new Thread() - " + thread.getName() + ": " + thread.getState());
            thread.start();
            System.out.println("start() - " + thread.getName() + ": " + thread.getState());
            try {
                System.out.println("main is sleeping now..");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Checking on thread again before interrupt() - " + thread.getName() + ": " + thread.getState());
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }
}
