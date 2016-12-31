package net.darmo_creations;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.IntBinaryOperator;

import net.darmo_creations.io.FileReader;
import net.darmo_creations.model.Serve;
import net.darmo_creations.model.Swing;

public class Main {
  public static void main(String[] args) {
    List<Swing> data = Parser.detectServes(FileReader.readFile(args[0]), LocalDate.of(2016, 11, 11));

    int nbServes = data.stream().filter(swing -> swing.isServe()).mapToInt(s -> 1).sum();

    int nbFirstServes = getNbServes(data, 1);
    int maxFirst = reduceSpeed(data, 1, Integer::max);
    int minFirst = reduceSpeed(data, 1, Integer::min);
    int avgFirst = reduceSpeed(data, 1, Integer::sum) / nbFirstServes;

    int nbSecondServes = getNbServes(data, 2);
    int maxSecond = reduceSpeed(data, 2, Integer::max);
    int minSecond = reduceSpeed(data, 2, Integer::min);
    int avgSecond = reduceSpeed(data, 2, Integer::sum) / nbSecondServes;

    System.out.format("Proportion de 1ers services : %.2f%%\n", (100 * (double) nbFirstServes / nbServes));
    System.out.format("Vitesse max : %d km/h\n", maxFirst);
    System.out.format("Vitesse moy : %d km/h\n", avgFirst);
    System.out.format("Vitesse min : %d km/h\n", minFirst);
    System.out.println();
    System.out.format("Proportion de 2èmes services : %.2f%%\n", (100 * (double) nbSecondServes / nbServes));
    System.out.format("Vitesse max : %d km/h\n", maxSecond);
    System.out.format("Vitesse moy : %d km/h\n", avgSecond);
    System.out.format("Vitesse min : %d km/h\n", minSecond);
    System.out.println();

    int nb = 0;
    int min = Integer.MAX_VALUE;
    int max = 0;
    int avg = 0;
    Swing last = null;

    int i = 0;
    LocalDateTime first = null;
    for (Swing swing : data) {
      if (last != null && Parser.isIntervalLessThan(last.getDate(), swing.getDate(), 30)) {
        int duration = (int) Duration.between(first, swing.getDate()).getSeconds();

        min = Math.min(min, duration);
        max = Math.max(max, duration);
        avg += duration;
        nb++;
        i = 0;
      }
      else {
        if (i == 0)
          first = swing.getDate();
        i++;
      }
      last = swing;
    }
    avg /= nb;

    System.out.format("Nombre d'échanges : %d\n", nb);
    System.out.format("Durée max : %d min %d s\n", max / 60, max % 60);
    System.out.format("Durée moy : %d min %d s\n", avg / 60, avg % 60);
    System.out.format("Durée min : %d min %d s\n", min / 60, min % 60);
    System.out.println();

    data.forEach(System.out::println);
  }

  /**
   * Calcule le nombre de n-ièmes services.
   * 
   * @param data les coups
   * @param serveNb le numéro des services
   * @return le nombre de n-ièmes services
   */
  private static int getNbServes(List<Swing> data, int serveNb) {
    return data.stream().filter(swing -> swing.isServe() && ((Serve) swing).getNumber() == serveNb).mapToInt(s -> 1).sum();
  }

  /**
   * Opère une réduction sur la vitesse.
   * 
   * @param data les coups
   * @param serveNb le numéro des services
   * @param fun la fonction de réduction
   * @return la valeur après réduction des vitesses
   */
  private static int reduceSpeed(List<Swing> data, int serveNb, IntBinaryOperator fun) {
    return data.stream().filter(swing -> swing.isServe() && ((Serve) swing).getNumber() == serveNb).mapToInt(s -> s.getSpeed()).reduce(fun).getAsInt();
  }
}
