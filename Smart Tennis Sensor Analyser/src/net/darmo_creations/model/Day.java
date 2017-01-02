package net.darmo_creations.model;

import net.darmo_creations.controller.DataContainer;

public class Day {
  private final int nbServes, otherServices;
  private final DataContainer firstServes;
  private final DataContainer secondServes;
  private final DataContainer exchanges;

  public Day(int nbServes, int otherServices, DataContainer firstServes, DataContainer secondServes, DataContainer exchanges) {
    this.nbServes = nbServes;
    this.otherServices = otherServices;
    this.firstServes = firstServes;
    this.secondServes = secondServes;
    this.exchanges = exchanges;
  }

  public int getNbServes() {
    return this.nbServes;
  }

  public int getOtherServices() {
    return this.otherServices;
  }

  public DataContainer getFirstServes() {
    return this.firstServes;
  }

  public DataContainer getSecondServes() {
    return this.secondServes;
  }

  public DataContainer getExchanges() {
    return this.exchanges;
  }
}