/*
 * ***********************Software description*********************************
 * JWrapperGComboBox.java
 * 
 * 
 * ***********************Software description*********************************
 * 
 * Copyright (C) 2008 - Lorenzo Carbonell
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * **************************Software License***********************************
 * 
 */

package es.atareao.queensboro.gui;

//
//********************************IMPORTACIONES*********************************
//
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import es.atareao.alejandria.gui.SortedListComboBoxModel;
import es.atareao.alejandria.lib.StringUtils;
import es.atareao.queensboro.db.WrapperTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
/**
 *
 * @author Lorenzo Carbonell
 */
public class JWrapperComboBox extends JComboBox{
    //
    //********************************CONSTANTES********************************
    //
    private final static long serialVersionUID=0L;
    //
    // *********************************CAMPOS*********************************
    //
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de JDefaultComboBox
     */
    public JWrapperComboBox() {
        super();
        this.setModel(new SortedListComboBoxModel());
        JTextComponent editor2 = (JTextComponent)this.getEditor().getEditorComponent();
        // change the editor's document
        editor2.setDocument(new S03FixedAutoSelection(this.getModel()));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isDisplayable()) {
                    setPopupVisible(true);
                }
            }
        });
    }
    //
    //********************************METODOS***********************************
    //
    @Override
    protected boolean processKeyBinding(final KeyStroke ks,final KeyEvent e, final int condition, final boolean pressed) {
        if (hasFocus()) {
            return super.processKeyBinding(ks, e, condition, pressed);
        } else {
            requestFocus();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    processKeyBinding(ks, e, condition, pressed);
                }
            });
        return true;
        }
    }
    public void addElement(WrapperTable objeto){
        this.getComboBoxModel().addElement(objeto);
    }
    public WrapperTable getElementAt(int indice){
        return (WrapperTable)this.getComboBoxModel().getElementAt(indice);
    }
    
    public WrapperTable getElement(String id){
        for(int contador=0;contador<this.getComboBoxModel().getSize();contador++){
            if(this.getElementAt(contador).getId().equals(id)){
                return this.getElementAt(contador);
            }
        }
        return null;
    }
    public int getIndexOf(String  id){
        if(id!=null){
            for(int contador=0;contador<this.getComboBoxModel().getSize();contador++){
                if(this.getElementAt(contador).getId().equals(id)){
                    return contador;
                }
            }
        }
        return -1;
    }
    
    public int getIndexOf(WrapperTable elemento){
        if(elemento!=null){
            return this.getIndexOf(elemento.getId());
        }
        return -1;  
    }/*
    @Override
    public DefaultComboBoxElement getSelectedItem() {
        return this.getElementAt(this.getSelectedIndex());
    }
*/
    public int getListSize() {
        return this.getComboBoxModel().getSize();
    }
    public void clear(){
        this.getComboBoxModel().removeAllElements();
    }
    public void removeAllElements() {
        this.getComboBoxModel().removeAllElements();
    }
    public void removeElement(WrapperTable elemento) {
        this.getComboBoxModel().removeElement(elemento);
    }
    public void removeElement(String key) {
        this.getComboBoxModel().removeElement(this.getElement(key));
    }
    public void removeElementAt(int indice) {
        this.getComboBoxModel().removeElementAt(indice);
    }
    public void setElements(Object[] elements){
        this.setModel(new SortedListComboBoxModel());
        this.getComboBoxModel().addAll(elements);
        if(this.getItemCount()>0){
            this.setSelectedIndex(0);
        }
    }
    public void setElements(Vector<? extends WrapperTable> elements){
        this.setElements(elements.toArray());
    }
    public void setSelectedId(String id){
        if(id!=null){
            int index=this.getIndexOf(id);
            if(index>-1){
                this.setSelectedIndex(index);
                return;
            }
        }
        if(this.getListSize()>0){
            this.setSelectedIndex(0);
        }
    }
    public String getSelectedId(){
        if(this.getSelectedItem()==null){
            return null;
        }
        return ((WrapperTable)this.getSelectedItem()).getId();
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    private SortedListComboBoxModel getComboBoxModel(){
        return (SortedListComboBoxModel)this.getModel();
    }
    public int buscaAnterior(WrapperTable objeto){
        int contador=0;
        if (objeto!=null){
            for(contador=0;contador<this.getComboBoxModel().getSize();contador++){
                Object otro=this.getComboBoxModel().getElementAt(contador);
                if(StringUtils.sinAcentos(otro.toString()).compareTo(StringUtils.sinAcentos(objeto.toString()))>0){
                    return contador;
                }
            }
        }
        return contador;
    }    
    /**
     * 
     */
    //
    //**************************METODOS DE ACCESO*******************************
    //
    class S03FixedAutoSelection extends PlainDocument {
        ComboBoxModel model;
        // flag to indicate if setSelectedItem has been called
        // subsequent calls to remove/insertString should be ignored
        boolean selecting=false;

        public S03FixedAutoSelection(ComboBoxModel model) {
            this.model = model;
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            // return immediately when selecting an item
            if (selecting) return;
            super.remove(offs, len);
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            // return immediately when selecting an item
            if (selecting) return;
            System.out.println("insert " + str + " at " + offs);
            // insert the string into the document
            super.insertString(offs, str, a);
            // get the resulting string
            String content = getText(0, getLength());
            // lookup a matching item
            Object item = lookupItem(content);
            // select the item (or deselect if null)
            if(item!=model.getSelectedItem()) System.out.println("Selecting '" + item + "'");
            selecting = true;
            model.setSelectedItem(item);
            selecting = false;
        }

        private Object lookupItem(String pattern) {
            // iterate over all items
            for (int i=0, n=model.getSize(); i < n; i++) {
                Object currentItem = model.getElementAt(i);
                // current item starts with the pattern?
                if (currentItem.toString().startsWith(pattern)) {
                    return currentItem;
                }
            }
            // no item starts with the pattern => return null
            return null;
        }
    }
}
