package com.rtaylor02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        AppleTree[] appleGarden = new AppleTree[6];

        List<Callable<Void>> workers = createWorkers(3, createAppleGarden(3));
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
        String[] names = createWorkerNames(garden.length);

        List<Callable<Void>> workers = new ArrayList<>(numberOfWorkers);
        for (int i = 0; i < garden.length; i++) {
            workers.add(createWorker(i, i + 2, garden, names[i]));
        }
        
        return workers;
    }

    private static String[] createWorkerNames(int numberOfNames) {
        String[] workerNames = new String[numberOfNames];
        for (int i = 0; i < numberOfNames; i++) {
            workerNames[i] = "worker-" + i;
        }
        return workerNames;
    }

    private static Callable<Void> createWorker(int treeNumberInclusive, int treeNumberExclusive, AppleTree[] garden, String workerName) {

        return () -> {
            for (int i = treeNumberInclusive; i < treeNumberExclusive; i++) {
                garden[i].pickApples(workerName);
            }

            return null;
        };
    }
}