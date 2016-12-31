package net.darmo_creations.model;

public enum SwingType {
  FOREHAND_SPIN_FLAT("Coup droit Spin/Plat"),
  FOREHAND_SLICE("Coup droit Slice"),
  FOREHAND_VOLLEY("Coup droit vollée"),
  BACKHAND_SPIN_FLAT("Revers Spin/Plat"),
  BACKHAND_SLICE("Revers Slice"),
  BACKHAND_VOLLEY("Revers vollée"),
  SMASH("Smash"),
  SERVE("Service");

  private final String name;

  private SwingType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return getName();
  }
}
