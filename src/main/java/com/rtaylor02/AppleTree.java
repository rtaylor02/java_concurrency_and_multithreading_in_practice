package com.rtaylor02;


public class AppleTree {
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
