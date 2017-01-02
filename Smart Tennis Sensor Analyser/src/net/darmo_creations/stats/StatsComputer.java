package net.darmo_creations.stats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

import net.darmo_creations.controller.DataContainer;
import net.darmo_creations.io.FileReader;
import net.darmo_creations.model.Day;
import net.darmo_creations.model.Serve;
import net.darmo_creations.model.Swing;

public class StatsComputer {
  public static Day getStats(String path, LocalDate date) {
    List<Swing> swings = Parser.parse(FileReader.readFile(path), date);

    int nbServes = swings.stream().filter(swing -> swing instanceof Serve).mapToInt(s -> 1).sum();
    int nbFirstServes = getNbServes(swings, swing -> swing instanceof Serve && ((Serve) swing).getNumber() == 1);
    int nbSecondServes = getNbServes(swings, swing -> swing instanceof Serve && ((Serve) swing).getNumber() == 2);
    int nbOtherServes = getNbServes(swings, swing -> swing instanceof Serve && ((Serve) swing).getNumber() > 2);

    int nbValidFirstServes = getNbValidServes(swings, 1);
    int maxFirst = reduceSpeed(swings, 1, Integer::max);
    int minFirst = reduceSpeed(swings, 1, Integer::min);
    int avgFirst = reduceSpeed(swings, 1, Integer::sum) / nbValidFirstServes;
    DataContainer cFirst = new DataContainer(nbValidFirstServes, 100 * (float) nbValidFirstServes / nbFirstServes, maxFirst, avgFirst, minFirst);

    int nbValidSecondServes = getNbValidServes(swings, 2);
    int maxSecond = reduceSpeed(swings, 2, Integer::max);
    int minSecond = reduceSpeed(swings, 2, Integer::min);
    int avgSecond = reduceSpeed(swings, 2, Integer::sum) / nbValidSecondServes;
    DataContainer cSecond = new DataContainer(nbValidSecondServes, 100 * (float) nbValidSecondServes / (nbFirstServes + nbSecondServes), maxSecond, avgSecond, minSecond);

    int nb = 0;
    int min = Integer.MAX_VALUE;
    int max = 0;
    int avg = 0;
    Swing last = null;

    LocalDateTime first = null;
    for (Swing swing : swings) {
      if (last != null && !Parser.isIntervalLessThan(last.getDate(), swing.getDate(), Parser.GAP)) {
        int duration = (int) Duration.between(first, last.getDate()).getSeconds();

        min = Math.min(min, duration);
        max = Math.max(max, duration);
        avg += duration;
        nb++;
        first = swing.getDate();
      }
      else if (last == null) {
        first = swing.getDate();
      }
      last = swing;
    }
    avg /= nb;
    DataContainer cExchanges = new DataContainer(nb, max, avg, min);

    return new Day(nbServes, nbOtherServes, cFirst, cSecond, cExchanges);
  }

  private static int getNbServes(List<Swing> data, Predicate<Swing> fun) {
    return data.stream().filter(fun).mapToInt(s -> 1).sum();
  }

  /**
   * Calcule le nombre de n-ièmes services valides.
   * 
   * @param data les coups
   * @param serveNb le numéro des services
   * @return le nombre de n-ièmes services valides
   */
  private static int getNbValidServes(List<Swing> data, int serveNb) {
    int nb = 0;

    for (int i = 0; i < data.size(); i++) {
      Swing current = data.get(i);
      Swing next = i < data.size() - 1 ? data.get(i + 1) : null;

      if (current instanceof Serve && ((Serve) current).getNumber() == serveNb && //
          (!(next instanceof Serve) || ((Serve) next).getNumber() != serveNb + 1))
        nb++;
    }

    return nb;
  }

  /**
   * Opère une réduction sur la vitesse des n-ièmes services valides.
   * 
   * @param data les coups
   * @param serveNb le numéro des services
   * @param fun la fonction de réduction
   * @return la valeur après réduction des vitesses
   */
  private static int reduceSpeed(List<Swing> data, int serveNb, IntBinaryOperator fun) {
    int speed = 0;
    boolean foundAny = false;

    for (int i = 0; i < data.size(); i++) {
      Swing current = data.get(i);
      Swing next = i < data.size() - 1 ? data.get(i + 1) : null;

      if (current instanceof Serve && ((Serve) current).getNumber() == serveNb && //
          (!(next instanceof Serve) || ((Serve) next).getNumber() != serveNb + 1)) {
        if (!foundAny) {
          speed = current.getSpeed();
          foundAny = true;
        }
        else
          speed = fun.applyAsInt(speed, current.getSpeed());
      }
    }

    return speed;
  }
}
