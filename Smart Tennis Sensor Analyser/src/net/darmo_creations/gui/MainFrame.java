package net.darmo_creations.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import net.darmo_creations.controller.DataContainer;
import net.darmo_creations.controller.MainController;

public class MainFrame extends JFrame {
  private static final long serialVersionUID = 2426665404072947885L;

  private JFileChooser fileChooser;

  private JLabel nbServesLbl, otherServicesLbl;
  private JComboBox<LocalDate> dateCombo;
  private InfoPanel firstServesPnl, secondServesPnl, exchangesPnl;

  public MainFrame() {
    super("Statistiques Smart Tennis Sensor");
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    MainController controller = new MainController(this);

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    this.fileChooser.setFileFilter(new FileFilter() {
      @Override
      public String getDescription() {
        return "Fichier CSV (*.csv)";
      }

      @Override
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
      }
    });

    setJMenuBar(initJMenuBar(controller));

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    JPanel p = new JPanel();
    p.add(new JLabel("Services :"));
    p.add(this.nbServesLbl = new JLabel());
    add(p, gbc);

    gbc.gridx = 1;
    p = new JPanel();
    p.add(new JLabel("Date"));
    p.add(this.dateCombo = new JComboBox<>(new DefaultComboBoxModel<>()));
    this.dateCombo.addItemListener(controller);
    add(p, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    add(this.firstServesPnl = new InfoPanel("1ers services", "Vitesse", "km/h", true), gbc);

    gbc.gridx = 1;
    add(this.secondServesPnl = new InfoPanel("2nds services", "Vitesse", "km/h", true), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    p = new JPanel();
    p.add(new JLabel("Autres services :"));
    p.add(this.otherServicesLbl = new JLabel());
    add(p, gbc);

    gbc.gridy = 3;
    add(this.exchangesPnl = new InfoPanel("Échanges", "Durée", "s", false), gbc);

    updateInfo(null, -1, null, null, null, -1);

    pack();
    setLocationRelativeTo(null);
  }

  private JMenuBar initJMenuBar(ActionListener listener) {
    JMenuBar menuBar = new JMenuBar();
    JMenuItem i;

    JMenu fileMenu = new JMenu("Fichier");
    fileMenu.add(i = new JMenuItem("Ouvrir..."));
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
    i.setActionCommand("open");
    i.addActionListener(listener);

    menuBar.add(fileMenu);

    return menuBar;
  }

  public void setDates(Set<LocalDate> dates) {
    DefaultComboBoxModel<LocalDate> model = (DefaultComboBoxModel<LocalDate>) this.dateCombo.getModel();

    model.removeAllElements();
    dates.forEach(date -> model.addElement(date));
    this.dateCombo.setSelectedIndex(0);
  }

  public void updateInfo(LocalDate date, int nbServes, DataContainer firstServes, DataContainer secondServes, DataContainer exchanges, int otherServices) {
    if (date != null) {
      this.nbServesLbl.setText("" + nbServes);
      this.firstServesPnl.setData(firstServes);
      this.secondServesPnl.setData(secondServes);
      this.exchangesPnl.setData(exchanges);
      this.otherServicesLbl.setText("" + otherServices);
    }
    else {
      this.nbServesLbl.setText("-");
      this.firstServesPnl.setData(null);
      this.secondServesPnl.setData(null);
      this.exchangesPnl.setData(null);
      this.otherServicesLbl.setText("-");
    }
  }

  public File showOpenFileChooser() {
    this.fileChooser.showOpenDialog(this);
    return this.fileChooser.getSelectedFile();
  }
}
