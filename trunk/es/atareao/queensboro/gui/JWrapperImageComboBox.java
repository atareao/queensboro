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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.gui.SortedListComboBoxModel;
import es.atareao.alejandria.lib.StringUtils;
import es.atareao.queensboro.db.WrapperImage;
import es.atareao.queensboro.db.WrapperTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
/**
 *
 * @author Lorenzo Carbonell
 */
public class JWrapperImageComboBox extends JComboBox implements JComboBox.KeySelectionManager{
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
    public JWrapperImageComboBox() {
        super();
        this.setModel(new SortedListComboBoxModel());
        this.setRenderer(new ComboBoxRenderer());
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
            }
        }
        //this.setElements(new Vector<WrapperTable<? extends WrapperTable>>());
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
    public void addElement(WrapperImage image){
        this.getComboBoxModel().addElement(image);
    }
    public WrapperImage getElementAt(int indice){
        return (WrapperImage)this.getComboBoxModel().getElementAt(indice);
    }
    
    public WrapperImage getElement(String id){
        for(int contador=0;contador<this.getComboBoxModel().getSize();contador++){
            if(this.getElementAt(contador).getId().equals(id)){
                return this.getElementAt(contador);
            }
        }
        return null;
    }
    public int getIndexOf(String  id){
        for(int contador=0;contador<this.getComboBoxModel().getSize();contador++){
            if(this.getElementAt(contador).getId().equals(id)){
                return contador;
            }
        }
        return -1;
    }
    
    public int getIndexOf(WrapperImage elemento){
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
    public void removeElement(WrapperImage elemento) {
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
    public int buscaAnterior(WrapperImage objeto){
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
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID=0L;
        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if((value!=null)&&(value instanceof WrapperImage)){
                try {
                    WrapperImage wi = (WrapperImage) value;
                    if (wi.getDescripcion() != null) {
                        setText(wi.getDescripcion());
                    }
                    if (wi.getImageIcon() != null) {
                        setIcon(wi.getImageIcon());
                    }
                } catch (MalformedURLException ex) {
                    ErrorDialog.manejaError(ex);
                }
            }
            return this;
        }
    }
   
}
