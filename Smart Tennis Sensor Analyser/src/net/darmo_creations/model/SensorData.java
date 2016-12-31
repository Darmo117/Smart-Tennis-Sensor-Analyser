package net.darmo_creations.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SensorData {
  private final LocalDateTime date;
  private final SwingType swingType;
  private final int swingSpeed;

  public SensorData(LocalDateTime date, SwingType swingType, int swingSpeed) {
    if (swingSpeed < 0)
      throw new IllegalArgumentException("vitesse négative");
    this.date = Objects.requireNonNull(date);
    this.swingType = Objects.requireNonNull(swingType);
    this.swingSpeed = swingSpeed;
  }

  public LocalDateTime getDate() {
    return this.date;
  }

  public SwingType getSwingType() {
    return this.swingType;
  }

  public boolean isServe() {
    return getSwingType() == SwingType.SERVE;
  }

  public int getSwingSpeed() {
    return this.swingSpeed;
  }

  @Override
  public String toString() {
    return String.format("%s %s %d", getDate(), getSwingType(), getSwingSpeed());
  }
}
