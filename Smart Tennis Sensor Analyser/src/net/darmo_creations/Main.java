package net.darmo_creations;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

import net.darmo_creations.io.FileReader;
import net.darmo_creations.model.Serve;
import net.darmo_creations.model.Swing;

public class Main {
  public static void main(String[] args) {
    List<Swing> data = Parser.detectServes(FileReader.readFile(args[0]), LocalDate.of(2016, 11, 11));

    int nbServes = data.stream().filter(swing -> swing instanceof Serve).mapToInt(s -> 1).sum();
    int nbFirstServes = getNbServes(data, swing -> swing instanceof Serve && ((Serve) swing).getNumber() == 1);
    int nbSecondServes = getNbServes(data, swing -> swing instanceof Serve && ((Serve) swing).getNumber() == 2);
    int nbOtherServes = getNbServes(data, swing -> swing instanceof Serve && ((Serve) swing).getNumber() > 2);

    int nbValidFirstServes = getNbValidServes(data, 1);
    int maxFirst = reduceSpeed(data, 1, Integer::max);
    int minFirst = reduceSpeed(data, 1, Integer::min);
    int avgFirst = reduceSpeed(data, 1, Integer::sum) / nbValidFirstServes;

    int nbValidSecondServes = getNbValidServes(data, 2);
    int maxSecond = reduceSpeed(data, 2, Integer::max);
    int minSecond = reduceSpeed(data, 2, Integer::min);
    int avgSecond = reduceSpeed(data, 2, Integer::sum) / nbValidSecondServes;

    System.out.println(nbFirstServes);
    System.out.println(nbSecondServes);
    System.out.println(nbOtherServes);

    System.out.println("Nombre de services : " + nbServes);
    System.out.println();
    System.out.format("Proportion de 1ers services : %.2f%%\n", (100 * (double) nbValidFirstServes / nbFirstServes));
    System.out.format("Vitesse max : %d km/h\n", maxFirst);
    System.out.format("Vitesse moy : %d km/h\n", avgFirst);
    System.out.format("Vitesse min : %d km/h\n", minFirst);
    System.out.println();
    System.out.format("Proportion de 2èmes services : %.2f%%\n", (100 * (double) nbValidSecondServes / (nbFirstServes + nbSecondServes)));
    System.out.format("Vitesse max : %d km/h\n", maxSecond);
    System.out.format("Vitesse moy : %d km/h\n", avgSecond);
    System.out.format("Vitesse min : %d km/h\n", minSecond);
    System.out.println();

    int nb = 0;
    int min = Integer.MAX_VALUE;
    int max = 0;
    int avg = 0;
    Swing last = null;

    LocalDateTime first = null;
    for (Swing swing : data) {
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

    System.out.format("Nombre d'échanges : %d\n", nb);
    System.out.format("Durée max : %d min %d s\n", max / 60, max % 60);
    System.out.format("Durée moy : %d min %d s\n", avg / 60, avg % 60);
    System.out.format("Durée min : %d min %d s\n", min / 60, min % 60);
    System.out.println();

    data.forEach(System.out::println);
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
