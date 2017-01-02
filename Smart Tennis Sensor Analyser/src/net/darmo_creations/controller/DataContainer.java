package net.darmo_creations.controller;

public class DataContainer {
  private final int nbUnit1;
  private final float proportion;
  private final int max, average, min;

  public DataContainer(int nbUnit1, int max, int average, int min) {
    this(nbUnit1, -1, max, average, min);
  }

  public DataContainer(int nbUnit1, float proportion, int max, int average, int min) {
    this.nbUnit1 = nbUnit1;
    this.proportion = proportion;
    this.max = max;
    this.average = average;
    this.min = min;
  }

  public int getNbUnit1() {
    return this.nbUnit1;
  }

  public float getProportion() {
    return this.proportion;
  }

  public int getMax() {
    return this.max;
  }

  public int getAverage() {
    return this.average;
  }

  public int getMin() {
    return this.min;
  }
}
