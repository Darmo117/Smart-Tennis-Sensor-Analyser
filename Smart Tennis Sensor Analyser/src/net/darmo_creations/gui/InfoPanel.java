package net.darmo_creations.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.darmo_creations.controller.DataContainer;

public class InfoPanel extends JPanel {
  private static final long serialVersionUID = -6505267486766909304L;

  private JTable table;

  private boolean displayProportion;
  private String unit;

  public InfoPanel(String unitName1, String unitName2, String unit, boolean displayProportion) {
    setLayout(new GridLayout(1, 1));
    setBorder(new EmptyBorder(5, 5, 5, 5));

    String[][] data = new String[displayProportion ? 5 : 4][];
    int i = 0;

    data[i++] = new String[]{unitName1, ""};
    if (displayProportion)
      data[i++] = new String[]{"Proportion", ""};
    data[i++] = new String[]{unitName2 + " max", ""};
    data[i++] = new String[]{unitName2 + " moy", ""};
    data[i++] = new String[]{unitName2 + " min", ""};

    this.table = new JTable(data, new String[]{"", ""});
    this.table.setEnabled(false);
    this.table.setShowGrid(false);
    this.table.setIntercellSpacing(new Dimension());
    this.table.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
    this.table.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
    this.unit = unit;
    this.displayProportion = displayProportion;

    add(this.table);
  }

  public void setData(DataContainer data) {
    TableModel t = this.table.getModel();
    int i = 0;

    t.setValueAt(data != null ? "" + data.getNbUnit1() : "-", i++, 1);
    if (this.displayProportion)
      t.setValueAt((data != null ? String.format("%.2f %%", data.getProportion()) : "-"), i++, 1);
    t.setValueAt((data != null ? "" + data.getMax() + " " + this.unit : "-"), i++, 1);
    t.setValueAt((data != null ? "" + data.getAverage() + " " + this.unit : "-"), i++, 1);
    t.setValueAt((data != null ? "" + data.getMin() + " " + this.unit : "-"), i++, 1);
  }

  private class CellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -7121306538259351109L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      setBorder(new EmptyBorder(5, 5, 5, 5));
      cellComponent.setBackground(column == 0 ? Color.LIGHT_GRAY : new Color(240, 240, 240));

      return cellComponent;
    }
  }
}
