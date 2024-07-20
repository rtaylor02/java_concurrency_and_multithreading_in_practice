package com.rtaylor02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class Chapter1 {
    public static void main(String[] args) {
        // Lesson2_ExecutingTasksInParallelWithForkJoinPool.main();
        Lesson3_JoiningTheResultsOfTheTasks.main();
        // Lesson3_Challenge.main("1", "2", "3", "4", "5", "6", "7"); // 28
    }

    private static class Lesson2_ExecutingTasksInParallelWithForkJoinPool {
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
    
    private static class Lesson3_JoiningTheResultsOfTheTasks {
        public static void main(String... args) {
            AppleTree[] trees = createTrees(12);

            ForkJoinPool pool = new ForkJoinPool();
            int totalApples = pool.invoke(new MyRecursiveTask(trees));

            System.out.println("Total apples: " + totalApples);
        }

        private static AppleTree[] createTrees(int totalTrees) {
            AppleTree[] trees = new AppleTree[totalTrees];

            for (int i = 0; i < totalTrees; i++) {
                trees[i] = new AppleTree(4, "Tree" + i);
            }

            return trees;
        }

        private static class AppleTree {
            private int numberOfApples;
            private String label;

            public AppleTree(int numberOfApples, String label) {
                this.numberOfApples = numberOfApples;
                this.label = label;
            }

            public int getApples() {
                return numberOfApples;
            }
        }

        private static class MyRecursiveTask extends RecursiveTask<Integer> {
            private int threshold = 2;
            private AppleTree[] trees;

            public MyRecursiveTask(AppleTree[] trees) {
                this.trees = trees;
            }

            @Override
            protected Integer compute() {
                // Divide and conquer
                if (trees.length < threshold) {
                    System.out.println(Thread.currentThread().getName() + " is counting apples...");
                    return Arrays.stream(trees).mapToInt(t -> t.getApples()).sum();
                } else {
                    AppleTree[] bunch1 = Arrays.copyOfRange(trees, 0, trees.length / 2);
                    AppleTree[] bunch2 = Arrays.copyOfRange(trees, trees.length / 2, trees.length);

                    MyRecursiveTask leftSum = new MyRecursiveTask(bunch1);
                    MyRecursiveTask rigthSum = new MyRecursiveTask(bunch2);

                    leftSum.fork();

                    return rigthSum.compute() + leftSum.join();
                }
            }
        }
    }

    /**
     * Challenge:
     * Given an array of ints, calculate the sum of its elements with 
     * ForkJoinPool and RecursiveTask
     */
    private static class Lesson3_Challenge {
        MyRecursiveTask task = new MyRecursiveTask();
        
        private static class MyRecursiveTask extends RecursiveTask<Integer> {
            private int[] numbers;
            private int threshold = 4;

            public MyRecursiveTask(int... numbers) {
                if (numbers.length > 0) {
                    this.numbers = numbers;
                } else {
                    this.numbers = null;
                }
            }

            @Override
            public Integer compute() {
                // ForkJoinPool follows divide-conquer operation model
                // Divide tasks if total numbers > threshold. Do it yourself if it's too small
                if (this.numbers.length < this.threshold) {
                    Integer result = IntStream.of(numbers).map(i -> Integer.valueOf(i)).sum();
                    return result;
                } else {
                    // Divide task into 2 parts
                    int[] halfNumbersStart = Arrays.copyOfRange(this.numbers, 0, this.numbers.length / 2);
                    int[] halfNumbersEnd = Arrays.copyOfRange(this.numbers, this.numbers.length / 2, this.numbers.length);
                    
                    // Assign both subtasks to 2 RecursiveTask
                    MyRecursiveTask leftSum = new MyRecursiveTask(halfNumbersStart);
                    MyRecursiveTask rightSum = new MyRecursiveTask(halfNumbersEnd);

                    leftSum.fork();
                    
                    // Compose result from subresults
                    return rightSum.compute() + leftSum.join();
                }
            }
        }

        public static void main(String... args) {
            ForkJoinPool pool = new ForkJoinPool();
            Integer sum = pool.invoke(new MyRecursiveTask(Arrays.stream(args).map(s -> Integer.valueOf(s)).mapToInt(i -> i).toArray()));

            System.out.println("Sum is: " + sum);
        }
    }
}