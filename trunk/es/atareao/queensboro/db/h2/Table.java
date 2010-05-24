/*
 * ***********************Software description*********************************
 * Schema.java
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
public class Table extends WrapperTable<Table>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Table(){
        super();
    }
    public Table(Conector conector,String id) throws SQLException{
        super(Table.class,conector,"information_schema","tables ",id);
    }    
    public Table(Conector conector) throws SQLException{
        super(Table.class,conector,"information_schema","tables");
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
        String schemaName=this.getValue("schema_name");
        String tableName=this.getValue("table_name");
        return this.dropTable(schemaName,tableName);
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
        for(Table seleccionado:this.find(conditions)){
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
     */
    @Override
    protected boolean insert(Row row) throws SQLException{
        String schema_name=row.getValue("schema_name");
        String table_name=row.getValue("table_name");
        if(schema_name!=null){
            if(table_name!=null) {
                return this.createTable(schema_name, table_name);
            }
        }
        return false;
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    public boolean insert() throws SQLException{
        return this.insert(this.getRow());
    }
    /**
     * Update the values for current register. If ID is suplied, the current register
     * is set to ID
     * @param row 
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    @Override
    protected boolean update(Row row) throws SQLException{
        if(row.containsColumn("schema_name")){
            if(row.containsColumn("schema_owner")) {
                if(this.delete(row.getValue("schema_name"))){
                    return this.insert(row);
                }
            }
        }
        return false;
    }
    /**
     * Update the values for current register.
     * @return true if the register is updated
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */    
    @Override
    public boolean update() throws SQLException{
        return this.update(this.getRow());
    }

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    /**
     * Creates a new schema. The current user owner must have admin rights.If no authorization is specified, the current user is used.
     * @param schemaName the name of the new schema
     * @param ownerUserName the name of the propietary
     * @return true if the schema was created
     */
    private boolean createTable(String schemaName,String tableName) throws SQLException{
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE CACHED TABLE IF NOT EXISTS ");
        sb.append(schemaName);
        sb.append(".");
        sb.append(tableName);
        sb.append(" (ID IDENTITY)");
        return this.getConector().execute(sb.toString());
        
    }
    private boolean dropTable(String schemaName,String tableName) throws SQLException{
        StringBuffer sb = new StringBuffer();
        sb.append("DROP TABLE IF EXISTS ");
        sb.append(schemaName);
        sb.append(".");
        sb.append(tableName);
        return this.getConector().execute(sb.toString());
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 

    // </editor-fold> 
}
