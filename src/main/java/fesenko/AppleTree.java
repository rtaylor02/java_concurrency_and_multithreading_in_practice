package fesenko;

public class AppleTree {
    private final String label;
    private final int numberOfApples;

    public static AppleTree[] createGarden(int size) {
        AppleTree[] garden = new AppleTree[size];
        for (int i = 0; i < size; i++) {
            garden[i] = new AppleTree("tree#" + i);
        }
        return garden;
    }

    public AppleTree(String label) {
        this.label = label;
        this.numberOfApples = 3;
    }

    public int pickApples(String workerName) {
        try {
            for (int i = 0; i < numberOfApples; i ++) {
                java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                System.out.printf("%5s picks apple#%d from %10S%n", workerName, i, label);
            }
        } catch (InterruptedException ie) {
            System.out.println("Who woke me up?");
        }

        return numberOfApples;
    }

    public int pickApples() {
        return pickApples(getWorkerName(Thread.currentThread().getName()));
    }

    private String getWorkerName(String threadName) {
        java.util.Map<String, String> map = new java.util.HashMap<>();

        map.put("ForkJoinPool.commonPool-worker-1", "Alice");
        map.put("ForkJoinPool.commonPool-worker-2", "Bob");
        map.put("ForkJoinPool.commonPool-worker-3", "Carol");
        map.put("ForkJoinPool.commonPool-worker-4", "Dan");

        return map.getOrDefault(threadName, threadName);
    }
}
