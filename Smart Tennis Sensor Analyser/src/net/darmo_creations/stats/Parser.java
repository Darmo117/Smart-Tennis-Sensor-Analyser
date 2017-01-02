package net.darmo_creations.stats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.darmo_creations.model.Serve;
import net.darmo_creations.model.Swing;
import net.darmo_creations.model.SwingType;

public class Parser {
  public static final int GAP = 15;

  /**
   * 
   * @param sensorData
   */
  public static List<Swing> parse(List<Swing> sensorData, LocalDate date) {
    List<Swing> data = new ArrayList<>();
    Swing last = null;
    int serveNb = 1;

    for (int i = 0; i < sensorData.size(); i++) {
      Swing current = sensorData.get(i);
      Swing next = i < sensorData.size() - 1 ? sensorData.get(i + 1) : null;

      if (last != null && current.getDate().toLocalDate().equals(date)) {
        if (next != null && (!last.isServe() || last.isServe() && !isIntervalLessThan(last.getDate(), current.getDate(), GAP)) && current.isServe() && next.isServe() && isIntervalLessThan(current.getDate(), next.getDate(), GAP)) {
          data.add(new Serve(current.getDate(), current.getSpeed(), 1));
          serveNb = 2;
        }
        else if (isIntervalLessThan(last.getDate(), current.getDate(), GAP) && last.getType() == SwingType.SERVE && current.getType() == SwingType.SERVE) {
          data.add(new Serve(current.getDate(), current.getSpeed(), serveNb));
          serveNb++;
        }
        else if (!isIntervalLessThan(last.getDate(), current.getDate(), GAP) && (current.getType() == SwingType.FOREHAND_VOLLEY || current.getType() == SwingType.BACKHAND_VOLLEY)) {
          data.add(new Swing(current.getDate(), current.getType() == SwingType.FOREHAND_VOLLEY ? SwingType.FOREHAND_SLICE : SwingType.BACKHAND_SLICE, current.getSpeed()));
          serveNb = 1;
        }
        else {
          data.add(current.isServe() ? new Serve(current.getDate(), current.getSpeed(), 1) : current);
          serveNb = 1;
        }
      }
      last = current;
    }

    return data;
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
  public static boolean isIntervalLessThan(LocalDateTime d1, LocalDateTime d2, int interval) {
    return Math.abs(Duration.between(d1, d2).getSeconds()) <= interval;
  }
}
