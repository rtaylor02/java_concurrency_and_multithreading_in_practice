package com.rtaylor02;

import jdk.jfr.Threshold;

import javax.naming.PartialResultException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Misc class is meant for my sandbox area where I have a play with anything
 * not necessarily following a book or course.
 */
public class Misc {
    public static void main(String[] args) {
    
    }
    
    
    /**
     * This class provides the following code sample:
     * 1) use of ExecutorService created via Executors. NOTE: always call shutdown() on ExecutorService
     * 2) use of Callable and Runnable
     * 3) use of Future as result of a task
     */
    private static class Misc1 {
        public static void main(String[] args) throws InterruptedException, ExecutionException {
            // Task as Callable
            Runnable runnable = () -> System.out.println("Callable here ..");
            
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            try {
                executorService.submit(runnable);
            } finally {
                System.out.println("Shutting down executorService");
                // REMEMBER to always shutdown() an ExecutorService
                executorService.shutdown();
            }
            System.out.println("End...");
            // =============================================================================
            Callable<Integer> callable = () -> Integer.valueOf(111);
            ExecutorService executorService1 = Executors.newSingleThreadExecutor();
            Future<Integer> result = executorService1.submit(callable);
            System.out.println("Result is " + result.get());
            
            // REMEMBER to always shutdown() an ExecutorService
            executorService1.shutdown();
        }
    }
    
    /**
     * This class provides the following code sample:
     * 1) use of ExecutorService created via Executors
     * 2) use of RecursiveTask
     * 3) use of RecursiveAction
     */
    private static class UsingForkJoinPool {
        private static class MyRecursiveTask extends RecursiveTask<Integer> {
            private final int[] array;
            private static final int THRESHOLD = 2;
            private static int totalForked = 0;
            
            MyRecursiveTask(int[] array) {
                this.array = array;
            }
            
            @Override
            protected Integer compute() {
                if (array.length < THRESHOLD) {
                    return Integer.valueOf(array[0]);
                } else {
                    totalForked++;
                    
                    MyRecursiveTask m1 = new MyRecursiveTask(Arrays.copyOfRange(array, 0, array.length / 2));
                    MyRecursiveTask m2 = new MyRecursiveTask(Arrays.copyOfRange(array, array.length / 2, array.length));
                    
                    m1.fork();
                    System.out.println("Total forked = " + totalForked);
                    
                    return m2.compute() + m1.join();
                }
            }
        }
        
        private static int[] array = {1, 2, 3};
        
        public static void main(String[] args) {
            int result = ForkJoinPool.commonPool().invoke(new MyRecursiveTask(array));
            System.out.println("Result = " + result);
        }
        
        
    }
    
    
}

