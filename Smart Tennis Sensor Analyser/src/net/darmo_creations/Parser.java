package net.darmo_creations;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import net.darmo_creations.model.SensorData;
import net.darmo_creations.model.SwingType;

public class Parser {
  /**
   * 
   * @param sensorData
   */
  public static void parse(List<SensorData> sensorData, LocalDate date) {
    SensorData last = null;
    int j = 1;

    for (int i = 0; i < sensorData.size(); i++) {
      SensorData current = sensorData.get(i);
      SensorData next = i < sensorData.size() - 1 ? sensorData.get(i + 1) : null;

      if (last != null && current.getDate().toLocalDate().equals(date)) {
        System.out.print(current.getDate().toLocalTime() + " ");
        if (next != null && (!last.isServe() || last.isServe() && !isIntervalLessThan(last.getDate(), current.getDate(), 20)) && current.isServe() && next.isServe() && isIntervalLessThan(current.getDate(), next.getDate(), 20)) {
          System.out.println("1er service");
          j = 2;
        }
        else if (isIntervalLessThan(last.getDate(), current.getDate(), 20) && last.getSwingType() == SwingType.SERVE && current.getSwingType() == SwingType.SERVE) {
          System.out.println(j + "ème service");
          j++;
        }
        else {
          System.out.println(current.isServe() ? "1er service" : current.getSwingType());
          j = 1;
        }
      }
      last = current;
    }
  }

  /**
   * Indique si le nombre de secondes séparant deux dates est inférieur ou égal à l'intervalle
   * donné.
   * 
   * @param d1 la première date
   * @param d2 la deuxième date
   * @param interval l'intervalle en secondes
   * @return true si l'intervalle est inférieur ou égal à celui donné, false sinon
   */
  private static boolean isIntervalLessThan(LocalDateTime d1, LocalDateTime d2, int interval) {
    return Duration.between(d1, d2).getSeconds() <= interval;
  }
}
