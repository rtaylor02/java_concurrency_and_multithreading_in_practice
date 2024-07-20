package com.rtaylor02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        Lesson2_usingForkJoinPoolWithCallables.main();
    }

    private static class Lesson2_usingForkJoinPoolWithCallables {
        public static void main(String... args) {
            List<Callable<Void>> workers = createWorkers(3, createAppleGarden(6));
            ForkJoinPool.commonPool().invokeAll(workers);
        }
    
        private static AppleTree[] createAppleGarden(int numberOfAppleTrees) {
            AppleTree[] trees = new AppleTree[numberOfAppleTrees];
    
            for (int i = 0; i < numberOfAppleTrees; i++) {
                trees[i] = new AppleTree(i, 3);
            }
    
            return trees;
        }
    
        private static List<Callable<Void>> createWorkers(int numberOfWorkers, AppleTree[] garden) {
            String[] names = createWorkerNames(numberOfWorkers);
    
            List<Callable<Void>> workers = new ArrayList<>(numberOfWorkers);
            workers.add(createWorker(0, 2, garden, names[0]));
            workers.add(createWorker(2, 4, garden, names[1]));
            workers.add(createWorker(4, 6, garden, names[2]));
            
            return workers;
        }
    
        private static String[] createWorkerNames(int numberOfNames) {
            String[] workerNames = new String[numberOfNames];
            for (int i = 0; i < numberOfNames; i++) {
                workerNames[i] = "worker-" + i;
            }
            return workerNames;
        }
    
        private static Callable<Void> createWorker(int treeNumberInclusive, int treeNumberExclusive, AppleTree[] trees, String workerName) {
            return () -> {
                for (int i = treeNumberInclusive; i < treeNumberExclusive; i++) {
                    trees[i].pickApples(workerName);
                }
    
                return null;
            };
        }

        private static class AppleTree {
            private int id;
            private int totalApples;

            public AppleTree(int id, int totalApples) {
            this.id = id;
            this.totalApples = totalApples;
            }

            public void pickApples(String workerName) {
            //String picker = Thread.currentThread().getName();
            String picker = workerName;

            for (int i = 0; i < this.totalApples; i++) {
                // System.out.println(picker + " is picking apple #" + i + " from tree #" + this.id);
                try {
                Thread.sleep(1000); // To simulate the duration of picking apples from 1 tree
                } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
                }
            }
            System.out.println(picker + " finished with tree #" + this.id);
  }
        }
    }
    
}