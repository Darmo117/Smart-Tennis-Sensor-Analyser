package net.darmo_creations.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import net.darmo_creations.controller.DataContainer;

public class InfoPanel extends JPanel {
  private static final long serialVersionUID = -6505267486766909304L;

  private JTable table;

  private boolean displayProportion;
  private String unit;

  public InfoPanel(String unitName1, String unitName2, String unit, boolean displayProportion) {
    setLayout(new GridLayout(1, 1));

    String[][] data = new String[displayProportion ? 5 : 4][];
    int i = 0;

    data[i++] = new String[]{unitName1, ""};
    if (displayProportion)
      data[i++] = new String[]{"Proportion", ""};
    data[i++] = new String[]{unitName2 + " max", ""};
    data[i++] = new String[]{unitName2 + " moy", ""};
    data[i++] = new String[]{unitName2 + " min", ""};

    this.table = new JTable(data, new String[]{"", ""});
    this.unit = unit;
    this.displayProportion = displayProportion;

    add(this.table);
  }

  public void setData(DataContainer data) {
    TableModel t = this.table.getModel();
    int i = 0;

    t.setValueAt(data != null ? "" + data.getNbUnit1() : "-", i++, 1);
    if (this.displayProportion)
      t.setValueAt((data != null ? String.format("%.2f", data.getProportion()) : "-") + " %", i++, 1);
    t.setValueAt((data != null ? "" + data.getMax() : "-") + " " + this.unit, i++, 1);
    t.setValueAt((data != null ? "" + data.getAverage() : "-") + " " + this.unit, i++, 1);
    t.setValueAt((data != null ? "" + data.getMin() : "-") + " " + this.unit, i++, 1);
  }
}
