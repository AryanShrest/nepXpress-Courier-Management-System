package com.courier.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionButtonRenderer extends JButton implements TableCellRenderer {
    
    public ActionButtonRenderer() {
        setOpaque(true);
        setText("View");
        setBackground(new Color(66, 133, 244));
        setForeground(Color.WHITE);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
} 