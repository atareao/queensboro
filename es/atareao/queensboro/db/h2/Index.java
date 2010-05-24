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
public class Index extends WrapperTable<Index>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Index(){
        super();
    }
    public Index(Conector conector,String id) throws SQLException{
        super(Index.class,conector,"information_schema","indexes",id);
    }    
    public Index(Conector conector) throws SQLException{
        super(Index.class,conector,"information_schema","indexes");
    }    
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    @Override
    public String toString() {
        return this.getValue("index_name");
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
            String indexName=this.getValue("index_name");
            return this.dropIndex(indexName);
        }
        return false;
    }
    /**
     * Delete a register
     * @param indexName 
     */
    @Override
    public boolean delete(String indexName) throws SQLException{
        return this.delete(new Condition("index_name",indexName,Condition.IGUAL));
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
        for(Index seleccionado:this.find(conditions)){
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
        return this.delete(new Condition("index_name","information_schema",Condition.DISTINTO));
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
     * @param indexName
     * @param tableName
     * @param columnName
     * @return use insert(String indexName,String tableName,String columnName) instead
     * @throws java.sql.SQLException
     */
    public boolean insert(String indexName,String tableName,String columnName) throws SQLException{
        return this.createIndex(indexName, tableName, columnName);
    }
    /**
     * 
     * @param indexName
     * @param tableName
     * @param columnNames
     * @return
     * @throws java.sql.SQLException
     */
    public boolean insert(String indexName,String tableName,Vector<String> columnNames) throws SQLException{
        return this.createIndex(indexName, tableName, columnNames);
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
     * @deprecated use update(String newIndexName) instead
     */
    @Override
    @Deprecated
    public boolean update() throws SQLException{
        return false;
    }
    public boolean update(String newIndexName) throws SQLException{
        if(this.getRow().containsColumn("index_name")){
            return this.remaneIndex(this.getRow().getValue("index_name"), newIndexName);
        }
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private boolean createIndex(String indexName,String tableName,String columnName) throws SQLException{
        if((indexName!=null)&&(tableName!=null)&&(columnName!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE INDEX IF NOT EXISTS ");
            sb.append(indexName);
            sb.append(" ON ");
            sb.append(tableName);
            sb.append("(");
            sb.append(columnName);
            sb.append(");");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean createIndex(String indexName,String tableName,Vector<String> columnNames) throws SQLException{
        if((indexName!=null)&&(tableName!=null)&&(columnNames!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE INDEX IF NOT EXISTS ");
            sb.append(indexName);
            sb.append(" ON ");
            sb.append(tableName);
            sb.append("(");
            sb.append(fromVectorToString(columnNames));
            sb.append(");");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }    
    private boolean dropIndex(String indexName) throws SQLException{
        StringBuffer sb = new StringBuffer();
        sb.append("DROP INDEX IF EXISTS ");
        sb.append(indexName);
        sb.append(";");
        return this.getConector().execute(sb.toString());
    }
    private boolean remaneIndex(String oldIndexName,String newIndexName) throws SQLException{
        if((oldIndexName!=null)&&(newIndexName!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER INDEX ");
            sb.append(oldIndexName);
            sb.append(" RENAME TO ");
            sb.append(newIndexName);
            sb.append(");");
            return this.getConector().execute(sb.toString());
        }
        return false;        
    }

    private String fromVectorToString(Vector<String> vector){
        StringBuffer sb = new StringBuffer();
        for(String seleccionado:vector){
            sb.append(seleccionado);
            sb.append(",");
        }
        String cadena=sb.toString();
        if(cadena.endsWith(",")){
           cadena=cadena.substring(0,cadena.lastIndexOf(","));
        }
        return cadena;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 

    // </editor-fold> 
}
