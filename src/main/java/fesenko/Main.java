package fesenko;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AppleTree[] trees = AppleTree.createGarden(6);

        java.util.concurrent.Callable<Void> worker1 = createWorker(trees, 0, 2, "Alex");
        java.util.concurrent.Callable<Void> worker2 = createWorker(trees, 2, 4, "Bob");
        java.util.concurrent.Callable<Void> worker3 = createWorker(trees, 4, 6, "Carol");

        java.util.concurrent.ForkJoinPool.commonPool().invokeAll(java.util.Arrays.asList(worker1, worker2, worker3));

        System.out.println();
        System.out.println("All done");
    }

    private static java.util.concurrent.Callable<Void> createWorker(AppleTree[] trees, int startTreeIndexInclusive, int endTreeIndexExclusive, String workerName) {
        return () -> {
            for (int i = startTreeIndexInclusive; i < endTreeIndexExclusive; i++) {
                trees[i].pickApples(workerName);
            }

            return null;
        };
    }
}
