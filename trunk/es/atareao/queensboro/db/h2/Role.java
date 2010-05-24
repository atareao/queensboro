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
public class Role extends WrapperTable<Role>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Role(){
        super();
    }
    public Role(Conector conector,String id) throws SQLException{
        super(Role.class,conector,"information_schema","roles",id);
    }    
    public Role(Conector conector) throws SQLException{
        super(Role.class,conector,"information_schema","roles");
    }    
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    @Override
    public String toString() {
        return this.getValue("name");
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
            String roleName=this.getValue("name");
            return this.dropRole(roleName);
        }
        return false;
    }
    /**
     * Delete a register
     * @param roleName 
     */
    @Override
    public boolean delete(String roleName) throws SQLException{
        return this.delete(new Condition("name",roleName,Condition.IGUAL));
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
        for(Role seleccionado:this.find(conditions)){
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
        for(Role seleccionado:this.find()){
            seleccionado.delete();
        }
        return true;
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
     */
    @Override
    public boolean insert() throws SQLException{
        String roleName=this.getValue("name");
        if(roleName!=null){
            this.createRole(roleName);
        }
        return false;
    }
    /**
     * 
     * @param roleName 
     * @return use insert(String indexName,String tableName,String columnName) instead
     * @throws java.sql.SQLException
     */
    public boolean insert(String roleName) throws SQLException{
        return this.createRole(roleName);
    }
    /**
     * Update the values for current register. If ID is suplied, the current register
     * is set to ID
     * @param row 
     * @throws java.sql.SQLException raises an exception if an error ocurs
     * @deprecated  not use
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
     * @deprecated not use
     */
    @Override
    @Deprecated
    public boolean update() throws SQLException{
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private boolean createRole(String roleName) throws SQLException{
        if(roleName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE ROLE IF NOT EXISTS ");
            sb.append(roleName);
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean dropRole(String roleName) throws SQLException{
        if(roleName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("DROP ROLE IF EXISTS ");
            sb.append(roleName);
            sb.append(";");
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
