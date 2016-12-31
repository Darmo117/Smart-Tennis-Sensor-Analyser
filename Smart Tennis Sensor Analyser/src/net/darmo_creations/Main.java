package net.darmo_creations;

import java.time.LocalDate;

import net.darmo_creations.io.FileReader;

public class Main {
  public static void main(String[] args) {
    Parser.parse(FileReader.readFile(args[0]), LocalDate.of(2016, 11, 11));
  }
}
