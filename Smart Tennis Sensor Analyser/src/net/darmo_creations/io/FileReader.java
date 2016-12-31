package net.darmo_creations.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.darmo_creations.model.SensorData;
import net.darmo_creations.model.SwingType;

public class FileReader {
  public static List<SensorData> readFile(String path) {
    try {
      List<SensorData> data = new ArrayList<>();

      Files.lines(Paths.get(path)).forEach(line -> {
        String[] columns = line.split(",");
        if (!"timestamp".equals(trim(columns[0])))
          data.add(new SensorData(getDate(trim(columns[0])), SwingType.valueOf(trim(columns[3])), Integer.parseInt(trim(columns[5]))));
      });

      return data;
    }
    catch (IOException e) {
      return null;
    }
  }

  private static String trim(String str) {
    return str.replace("\"", "");
  }

  private static LocalDateTime getDate(String str) {
    return LocalDateTime.parse(str.replace(' ', 'T'));
  }

  private FileReader() {}
}
