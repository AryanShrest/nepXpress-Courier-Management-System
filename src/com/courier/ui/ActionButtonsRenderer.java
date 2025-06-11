package com.courier.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ActionButtonsRenderer extends JPanel implements TableCellRenderer {
    private JButton viewBtn, editBtn, deleteBtn;

    public ActionButtonsRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        setBackground(Color.WHITE);

        // Create buttons
        viewBtn = new JButton("Vi...");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("D...");

        // Style buttons
        viewBtn.setBackground(new Color(70, 192, 222));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setPreferredSize(new Dimension(45, 25));

        editBtn.setBackground(new Color(65, 105, 225));
        editBtn.setForeground(Color.WHITE);
        editBtn.setPreferredSize(new Dimension(45, 25));

        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setPreferredSize(new Dimension(45, 25));

        // Add buttons to panel
        add(viewBtn);
        add(editBtn);
        add(deleteBtn);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
} 