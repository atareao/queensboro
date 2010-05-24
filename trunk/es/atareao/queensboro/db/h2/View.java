/*
 * ***********************Software description*********************************
 * Index.java
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

package es.atareao.queensboro.db.h2;

//
//********************************IMPORTACIONES*********************************
//

import java.sql.SQLException;
import java.util.Vector;
import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.db.Conector;
import es.atareao.queensboro.db.QueryDelete;
import es.atareao.queensboro.db.Row;
import es.atareao.queensboro.db.WrapperTable;

/**
 *
 * @author Lorenzo Carbonell
 */
public class View extends WrapperTable<View>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public View(){
        super();
    }
    public View(Conector conector,String id) throws SQLException{
        super(View.class,conector,"information_schema","views",id);
    }    
    public View(Conector conector) throws SQLException{
        super(View.class,conector,"information_schema","views");
    }    
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    @Override
    public String toString() {
        return this.getValue("table_name");
    }
    //
    //*****************************METODOS SQL*********************************
    //
    /**
     * Delete a register
     * @param id the ID of the register that we want to remove
     * @return true if the register was removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    /**
     * Delete the current register
     * @return true if the current register was removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean delete() throws SQLException{
        if(this.read()){
            String viewName=this.getValue("table_name");
            return this.dropView(viewName);
        }
        return false;
    }
    /**
     * Delete a register
     * @param indexName 
     */
    @Override
    public boolean delete(String indexName) throws SQLException{
        return this.delete(new Condition("table_name",indexName,Condition.IGUAL));
    }
    /**
     * Delete all the registers than meet with the suplied condition
     * @param condition the condition that registers must meet to be deleted
     * @return true if the registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean delete(Condition condition) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(condition);
        return delete(conditions);
    }
    /**
     * Delete all the registers than meet with the suplied conditions
     * @param conditions the conditions that registers must meet to be deleted
     * @return true if the registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean delete(Vector<Condition> conditions) throws SQLException{
        for(View seleccionado:this.find(conditions)){
            seleccionado.delete();
        }
        return true;
    }
    /**
     * Delete all the registers than meet with the suplied conditions
     * @param queryDelete the conditions that registers must meet to be deleted
     * @return true if the registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean delete(QueryDelete queryDelete) throws SQLException{
        return delete(queryDelete.getConditions());
    }
    /**
     * Delete all registers
     * @return true if all registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean deleteAll() throws SQLException{
        return this.delete(new Condition("table_schema","information_schema",Condition.DISTINTO));
    }

    /**
     * Insert a new  register in the table. If ID is supplied, is not inserted
     * @param row 
     * @return 
     * @throws java.sql.SQLException raises an exception if an error happens
     * @deprecated 
     */
    @Override
    @Deprecated
    protected boolean insert(Row row) throws SQLException{
        return false;
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     * @deprecated use insert(String indexName,String tableName,String columnName) instead
     */
    
    @Deprecated
    @Override
    public boolean insert() throws SQLException{
        return false;
    }
    /**
     * 
     * @param viewName 
     * @param stringSql
     * @return
     * @throws java.sql.SQLException 
     */
    public boolean insert(String viewName,String stringSql) throws SQLException{
        return this.createView(viewName, stringSql);
    }
    /**
     * Update the values for current register. If ID is suplied, the current register
     * is set to ID
     * @param row 
     * @throws java.sql.SQLException raises an exception if an error ocurs
     * @deprecated  use update(String newIndexName) instead
     */
    @Override
    @Deprecated
    protected boolean update(Row row) throws SQLException{
        return false;
    }
    /**
     * Update the values for current register.
     * @return true if the register is updated
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    @Override
    public boolean update() throws SQLException{
        String viewName=this.getValue("table_name");
        if(viewName!=null){
            return this.recomplieView(viewName);
        }
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private boolean createView(String viewName,String stringSql) throws SQLException{
        if((viewName!=null)&&(stringSql!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE FORCE VIEW IF NOT EXISTS ");
            sb.append(viewName);
            sb.append(" AS ");
            sb.append(stringSql);
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
        
    }
    private boolean dropView(String viewName) throws SQLException{
        if(viewName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("DROP VIEW IF EXISTS ");
            sb.append(viewName);
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean recomplieView(String viewName) throws SQLException{
        if(viewName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER VIEW  ");
            sb.append(viewName);
            sb.append(" RECOMPILE;");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 

    // </editor-fold> 
}
