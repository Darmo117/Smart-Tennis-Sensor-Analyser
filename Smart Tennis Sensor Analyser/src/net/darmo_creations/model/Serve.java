package net.darmo_creations.model;

import java.time.LocalDateTime;

public class Serve extends Swing {
  private int number;

  public Serve(LocalDateTime date, int speed, int number) {
    super(date, SwingType.SERVE, speed);
    this.number = number;
  }

  public int getNumber() {
    return this.number;
  }

  @Override
  public String getName() {
    return (getNumber() == 1 ? "1er" : getNumber() + "ème") + " " + super.getName();
  }
}
