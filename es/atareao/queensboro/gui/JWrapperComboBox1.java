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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import es.atareao.alejandria.gui.SortedListComboBoxModel;
import es.atareao.alejandria.lib.StringUtils;
import es.atareao.queensboro.db.WrapperTable;
/**
 *
 * @author Lorenzo Carbonell
 */
public class JWrapperComboBox1 extends JComboBox implements JComboBox.KeySelectionManager{
    //
    //********************************CONSTANTES********************************
    //
    private final static long serialVersionUID=0L;
    //
    // *********************************CAMPOS*********************************
    //
    private String searchFor;
    private long lap;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de JDefaultComboBox
     */
    public JWrapperComboBox1() {
        super();
        this.setModel(new SortedListComboBoxModel());
        this.setEditable(false);
        lap = new java.util.Date().getTime();
        
        setKeySelectionManager(this);
        JTextField  tf;
        if(getEditor() != null){
            tf = (JTextField)getEditor().getEditorComponent();
            if(tf != null){
                tf.setDocument(new CBDocument());        
                addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent evt){
                        JTextField tf = (JTextField)getEditor().getEditorComponent();
                        String text = tf.getText();
                        SortedListComboBoxModel aModel = getComboBoxModel();
                        String current;
                        for(int i = 0; i < aModel.getSize(); i++){
                            current = aModel.getElementAt(i).toString();
                            if(current.toLowerCase().startsWith(text.toLowerCase())){
                                tf.setText(current);
                                tf.setSelectionStart(text.length());
                                tf.setSelectionEnd(current.length());
                                break;
                            }
                        }
                    }
                });        //new AutocompleteComboBox(this);
                addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        formKeyReleased(evt);
                    }
                });
            }
        }
        //this.setElements(new Vector<WrapperTable<? extends WrapperTable>>());
    }
    //
    //********************************METODOS***********************************
    //
    private void formKeyReleased(java.awt.event.KeyEvent evt) {
        if(this.getModel().getSize()>0){
            int id=selectionForKey(evt.getKeyChar(),this.getModel());
            if(id>-1){
                //this.setSelectedIndex(id);
                this.setSelectedId(((WrapperTable)this.getItemAt(id)).getId());
            }
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
    /*
    @Override       
    public void setSelectedItem(Object element){
        if(element instanceof DefaultComboBoxElement){
            int seleccionado=this.getIndexOf((DefaultComboBoxElement)element);
            if(seleccionado>-1){
                super.setSelectedIndex(seleccionado);
            }
        }else{
            if(element instanceof String){
                int seleccionado=this.getIndexOf((String)element);
                if(seleccionado>-1){
                    super.setSelectedIndex(seleccionado);
                    //this.setSelectedIndex(seleccionado);
                }
            }
        }
    }*/
    public void setElements(Object[] elements){
        this.setModel(new SortedListComboBoxModel());
        this.getComboBoxModel().addAll(elements);
        this.setSelectedIndex(0);
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
    public class CBDocument extends PlainDocument{
        private final static long serialVersionUID=0L;
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{
            if (str==null) {
                return;
            }
            super.insertString(offset, str, a);
            if(!isPopupVisible() && str.length() != 0) {
                fireActionEvent();
            }
        }
    }    
    @Override
    public int selectionForKey(char aKey, ComboBoxModel aModel){
        long now = new java.util.Date().getTime();
        if (searchFor!=null && aKey==KeyEvent.VK_BACK_SPACE && searchFor.length()>0){
            searchFor = searchFor.substring(0, searchFor.length() -1);
        }else{
            // System.out.println(lap);
            // Kam nie hier vorbei.
            if(lap + 1000 < now) {
                searchFor = "" + aKey;
            }else {
                searchFor = searchFor + aKey;
            }
        }
        lap = now;
        String current;
        for(int i = 0; i < aModel.getSize(); i++){
            current = aModel.getElementAt(i).toString().toLowerCase();
            if (current.toLowerCase().startsWith(searchFor.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //
   
}
