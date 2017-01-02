package net.darmo_creations.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.darmo_creations.model.Swing;
import net.darmo_creations.model.SwingType;

public class FileReader {
  public static List<Swing> readFile(String path) {
    try {
      List<Swing> data = new ArrayList<>();

      Files.lines(Paths.get(path)).forEach(line -> {
        String[] columns = line.split(",");
        // On ignore la première ligne (commence par "timestamp").
        if (!"timestamp".equals(trimQuotes(columns[0])))
          data.add(new Swing(getDate(trimQuotes(columns[0])), SwingType.valueOf(trimQuotes(columns[3])), Integer.parseInt(trimQuotes(columns[5]))));
      });

      return data;
    }
    catch (IOException e) {
      return null;
    }
  }

  public static SortedSet<LocalDate> getDates(String path) {
    try {
      SortedSet<LocalDate> data = new TreeSet<>();

      Files.lines(Paths.get(path)).forEach(line -> {
        String[] columns = line.split(",");
        // On ignore la première ligne (commence par "timestamp").
        if (!"timestamp".equals(trimQuotes(columns[0])))
          data.add(getDate(trimQuotes(columns[0])).toLocalDate());
      });

      return data;
    }
    catch (IOException e) {
      return null;
    }
  }

  private static String trimQuotes(String str) {
    return str.replace("\"", "");
  }

  private static LocalDateTime getDate(String str) {
    return LocalDateTime.parse(str.replace(' ', 'T'));
  }

  private FileReader() {}
}
