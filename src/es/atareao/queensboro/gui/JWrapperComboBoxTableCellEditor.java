/*
 * JCheckBoxTableCellEditor.java
 *
 * Created on 16 de agosto de 2007, 22:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.queensboro.gui;

import es.atareao.alejandria.gui.JBasicComboBoxTableCellEditor;
import es.atareao.alejandria.gui.JFieldMov;
import es.atareao.alejandria.gui.JMTable;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import es.atareao.queensboro.db.WrapperTable;


/**
 *
 * @author Propietario
 */
public class JWrapperComboBoxTableCellEditor extends JBasicComboBoxTableCellEditor implements ActionListener, TableCellEditor, Serializable, JFieldMov {
    protected static final long serialVersionUID=0L;
    public JWrapperComboBoxTableCellEditor() {
        super(new JWrapperComboBox());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column) {
        this._table=(JMTable)table;
        this._column=column;
        this._row=row;
        table.setRowSelectionInterval(row,row);
        ((JWrapperComboBox)this._component).setSelectedId((String)value);
        ((JWrapperComboBox)this._component).setForeground(table.getSelectionForeground());
        ((JWrapperComboBox)this._component).setBackground(table.getSelectionBackground());
        return ((JWrapperComboBox)this._component);
    }
    @Override
    public Object getCellEditorValue() {
        if(((JWrapperComboBox)this._component).getSelectedItem()==null){
            ((JWrapperComboBox)this._component).setSelectedIndex(0);
        }
        return ((JWrapperComboBox)this._component).getSelectedId();
    }
    public String getSelectedId(){
        return ((JWrapperComboBox)this._component).getSelectedId();
    }
    public void setSelectedId(String id){
        ((JWrapperComboBox)this._component).setSelectedId(id);
    }
    public Object getSelectedItem(){
        return ((JWrapperComboBox)this._component).getSelectedItem();
    }
    public void setElements(Vector<? extends WrapperTable> elements){
        ((JWrapperComboBox)this._component).setElements(elements);
    }
    @Override
    public void setHorizontalAlignment(int horizontalAlignment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHorizontalAlignment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}