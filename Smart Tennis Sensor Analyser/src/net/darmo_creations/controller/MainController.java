package net.darmo_creations.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.io.FileReader;
import net.darmo_creations.model.Day;
import net.darmo_creations.stats.StatsComputer;

public class MainController implements ActionListener, ItemListener {
  private MainFrame frame;

  private Map<LocalDate, Day> data;

  public MainController(MainFrame frame) {
    this.frame = frame;
    this.data = new HashMap<>();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("open")) {
      open();
    }
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    LocalDate date = (LocalDate) e.getItem();
    Day d = this.data.get(date);

    if (d != null)
      this.frame.updateInfo(date, d.getNbServes(), d.getFirstServes(), d.getSecondServes(), d.getExchanges(), d.getOtherServices());
  }

  private void open() {
    File f = this.frame.showOpenFileChooser();

    if (f != null) {
      String path = f.getAbsolutePath();
      Set<LocalDate> dates = FileReader.getDates(path);

      this.frame.setDates(dates);
      dates.forEach(date -> this.data.put(date, StatsComputer.getStats(path, date)));
    }
  }
}
