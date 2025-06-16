package nepxpress.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonsEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton viewBtn, editBtn, deleteBtn;
    private String referenceNumber;

    public ActionButtonsEditor() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.setBackground(Color.WHITE);

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

        // Add action listeners
        viewBtn.addActionListener(e -> {
            System.out.println("View clicked for reference: " + referenceNumber);
            fireEditingStopped();
        });

        editBtn.addActionListener(e -> {
            System.out.println("Edit clicked for reference: " + referenceNumber);
            fireEditingStopped();
        });

        deleteBtn.addActionListener(e -> {
            System.out.println("Delete clicked for reference: " + referenceNumber);
            fireEditingStopped();
        });

        // Add buttons to panel
        panel.add(viewBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // Get the reference number from the second column (index 1)
        referenceNumber = table.getValueAt(row, 1).toString();
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
} 