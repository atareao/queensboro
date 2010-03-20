/*
 * ***********************Software description*********************************
 * Sequence.java
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
public class Sequence extends WrapperTable<Sequence>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Sequence(){
        super();
    }
    public Sequence(Conector conector,String id) throws SQLException{
        super(Sequence.class,conector,"information_schema","sequences",id);
    }    
    public Sequence(Conector conector) throws SQLException{
        super(Sequence.class,conector,"information_schema","sequences");
    }    
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    @Override
    public String toString() {
        return this.getValue("sequence_name");
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
            String sequenceName=this.getValue("sequence_name");
            return this.dropSequence(sequenceName);
        }
        return false;
    }
    /**
     * Delete a register
     * @param sequenceName 
     */
    @Override
    public boolean delete(String sequenceName) throws SQLException{
        return this.delete(new Condition("sequence_name",sequenceName,Condition.IGUAL));
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
        for(Sequence seleccionado:this.find(conditions)){
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
        return this.delete(new Condition("sequence_name","information_schema",Condition.DISTINTO));
    }

    /**
     * Insert a new  register in the table. If ID is supplied, is not inserted
     * @param row 
     * @return 
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    @Override
    protected boolean insert(Row row) throws SQLException{
        if(row.containsColumn("sequence_name")){
            String newSequenceName=row.getValue("sequence_name");
            if(row.containsColumn("current_value")) {
                String start=row.getValue("current_value");
                if(row.containsColumn("increment")){
                    String increment=row.getValue("increment");
                    if(row.containsColumn("cache")){
                        String cache=row.getValue("cache");
                        return this.createSequence(newSequenceName,start,increment,cache);
                    }else{
                        return this.createSequence(newSequenceName,start,increment);
                    }
                }else{
                    return this.createSequence(newSequenceName,start);
                }
            }else{
                return this.createSequence(newSequenceName);
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
    public boolean insert(String sequenceName) throws SQLException{
        return this.createSequence(sequenceName);
    }
    public boolean insert(String sequenceName,String start,String increment) throws SQLException{
        return this.createSequence(sequenceName, start, increment);
    }
    /**
     * Update the values for current register. If ID is suplied, the current register
     * is set to ID
     * @param row 
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    @Override
    protected boolean update(Row row) throws SQLException{
        if(row.containsColumn("sequence_name")){
            String cv=row.getValue("current_value");
            String inc=row.getValue("increment");
            return this.alterSequence(row.getValue("sequence_name"),cv,inc);
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
    public boolean update(String restart,String increment) throws SQLException{
        if(this.getRow().containsColumn("sequence_name")){
            return this.alterSequence(this.getRow().getValue("sequence_name"),restart,increment);
        }
        return false;
    }
    public boolean restart(String restart) throws SQLException{
        if(this.getRow().containsColumn("sequence_name")){
            return this.alterSequence(this.getRow().getValue("sequence_name"),restart,null);
        }
        return false;
    }
    public boolean increment(String increment) throws SQLException{
        if(this.getRow().containsColumn("sequence_name")){
            return this.alterSequence(this.getRow().getValue("sequence_name"),null,increment);
        }
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private boolean createSequence(String newSequenceName,String start,String increment,String cache) throws SQLException{
        if(newSequenceName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE SEQUENCE IF NOT EXISTS ");
            sb.append(newSequenceName);
            if(start!=null){
                sb.append(" START WITH ");
                sb.append(start);
            }
            if(increment!=null){
                sb.append(" INCREMENT BY ");
                sb.append(increment);
            }
            if(cache!=null){
                sb.append(" CACHE ");
                sb.append(cache);
            }
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean createSequence(String newSequenceName,String start,String increment) throws SQLException{
        return this.createSequence(newSequenceName,start,increment,null);
    }
    private boolean createSequence(String newSequenceName,String start) throws SQLException{
        return this.createSequence(newSequenceName,start,null,null);
    }
    private boolean createSequence(String newSequenceName) throws SQLException{
        return this.createSequence(newSequenceName,null,null,null);
    }
    private boolean dropSequence(String sequenceName) throws SQLException{
        StringBuffer sb = new StringBuffer();
        sb.append("DROP SEQUENCE IF EXISTS ");
        sb.append(sequenceName);
        sb.append(";");
        return this.getConector().execute(sb.toString());
    }
    private boolean alterSequence(String sequenceName,String restart,String increment) throws SQLException{
        if((sequenceName!=null)&&((restart!=null)||(increment!=null))){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER SEQUENCE ");
            sb.append(sequenceName);
            if(restart!=null){
                sb.append(" RESTART WITH ");
                sb.append(restart);
            }
            if(increment!=null){
                sb.append(" INCREMENT BY ");
                sb.append(increment);
            }
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
