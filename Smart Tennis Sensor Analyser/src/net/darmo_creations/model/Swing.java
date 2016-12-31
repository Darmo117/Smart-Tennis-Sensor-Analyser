package net.darmo_creations.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Swing {
  private final LocalDateTime date;
  private final SwingType type;
  private final int speed;

  public Swing(LocalDateTime date, SwingType type, int speed) {
    if (speed < 0)
      throw new IllegalArgumentException("vitesse négative");
    this.date = Objects.requireNonNull(date);
    this.type = Objects.requireNonNull(type);
    this.speed = speed;
  }

  public LocalDateTime getDate() {
    return this.date;
  }

  public SwingType getType() {
    return this.type;
  }

  public boolean isServe() {
    return getType() == SwingType.SERVE;
  }

  public int getSpeed() {
    return this.speed;
  }

  public String getName() {
    return this.type.getName();
  }

  @Override
  public String toString() {
    // @f0
    return String.format(
        "[%02d/%02d/%04d %02d:%02d:%02d] %s à %d km/h",
        getDate().getDayOfMonth(),
        getDate().getMonthValue(),
        getDate().getYear(),
        getDate().getHour(),
        getDate().getMinute(),
        getDate().getSecond(),
        getName(),
        getSpeed());
    // @f1
  }
}
