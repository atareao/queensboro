package es.atareao.queensboro.db;

/*
 * ***********************Software description*********************************
 * Row.java
 * 
 * 
 * ***********************Software description*********************************
 * 
 * Copyright (C) 2007 - Lorenzo Carbonell
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



//
//********************************IMPORTACIONES*********************************
//
import java.util.Enumeration;
import java.util.Hashtable;
/**
 *
 * @author Lorenzo Carbonell
 */
public class Row {
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Row(Hashtable<String,String> row){
        this.setRow(row);
    }
    public Row(){
        this(new Hashtable<String,String>());
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    public void clear(){
        this.getRow().clear();
    }
    public boolean containsColumn(String columnName){
        String key=Nomenclator.stdName(columnName);
        Enumeration<String> columns=this.getColumnNames();
        while(columns.hasMoreElements()){
            if(key.equals(columns.nextElement())){
                return true;
            }
        }
        return false;
        //return this.getRow().containsKey(Nomenclator.stdName(columnName));
    }
    public boolean containsValue(String value){
        return this.getRow().containsValue(Nomenclator.stdValue(value));
    }
    public String getValue(String columnName){
        return this.getRow().get(Nomenclator.stdName(columnName));
    }
    public String getInsertValue(String columnName){
        return Nomenclator.insValue(this.getRow().get(Nomenclator.stdName(columnName)));
    }    
    public Enumeration<String> getColumnNames(){
        return this.getRow().keys();
    }
    public void put(String columnName,String value){
        String key=Nomenclator.stdName(columnName);
        String val=Nomenclator.stdValue(value);
        if(!this.containsColumn(key)){
            this.getRow().put(key, val);
        }
    }
    public void remove(String columnName){
        String key=Nomenclator.stdName(columnName);
        if(this.containsColumn(key)){
            this.getRow().remove(key);
        }        
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 
    private Hashtable<String,String> _row=new Hashtable<String,String>();

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  ">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Campos  ">

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 
    private Hashtable<String, String> getRow() {
        return _row;
    }

    private void setRow(Hashtable<String, String> row) {
        this._row = row;
    }
    // </editor-fold> 
}
