package nepxpress.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    
    public ActionButtonEditor() {
        button = new JButton();
        button.setOpaque(true);
        button.setText("View");
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.WHITE);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
} 