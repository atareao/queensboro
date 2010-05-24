/*
 * ***********************Software description*********************************
 * JWrapperList.java
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
import javax.swing.JList;
import es.atareao.alejandria.gui.SortedListModel;
import es.atareao.queensboro.db.WrapperTable;
/**
 *
 * @author Lorenzo Carbonell
 */
public class JWrapperList extends JList{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public JWrapperList(){
        super();
        this.setModel(new SortedListModel());
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    public void addElement(WrapperTable<? extends WrapperTable> objeto){
        this.getListModel().addElement(objeto);
    }
    public WrapperTable<? extends WrapperTable> getElementAt(int indice){
        return (WrapperTable<? extends WrapperTable>)this.getListModel().getElementAt(indice);
    }
    
    public WrapperTable<? extends WrapperTable> getElement(String id){
        for(int contador=0;contador<this.getListModel().getSize();contador++){
            if(this.getElementAt(contador).getId().equals(id)){
                return this.getElementAt(contador);
            }
        }
        return null;
    }
    public int getIndexOf(String  id){
        for(int contador=0;contador<this.getListModel().getSize();contador++){
            if(this.getElementAt(contador).getId().equals(id)){
                return contador;
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
        return this.getListModel().getSize();
    }
    public void clear(){
        this.getListModel().removeAllElements();
    }
    public void removeAllElements() {
        this.getListModel().removeAllElements();
    }
    public void removeElement(WrapperTable elemento) {
        this.getListModel().removeElement(elemento);
    }
    public void removeElement(String key) {
        this.getListModel().removeElement(this.getElement(key));
    }
    public void removeElementAt(int indice) {
        this.getListModel().removeElementAt(indice);
    }

    public void setElements(Object[] elements){
        this.setModel(new SortedListModel());
        this.getListModel().addAll(elements);
        this.setSelectedIndex(0);
    }
    public void setElements(Vector<? extends WrapperTable> elements){
        this.setElements(elements.toArray());
    }
    public void setSelectedId(String id){
        int index=this.getIndexOf(id);
        this.setSelectedIndex(index);
    }
    public String getSelectedId(){
        if(this.getSelectedValue()==null){
            return null;
        }
        return ((WrapperTable)this.getSelectedValue()).getId();
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private SortedListModel getListModel(){
        return (SortedListModel)this.getModel();
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 

    // </editor-fold> 
}
