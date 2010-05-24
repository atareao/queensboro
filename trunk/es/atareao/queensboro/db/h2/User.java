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
import es.atareao.alejandria.lib.Convert;
import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.db.Conector;
import es.atareao.queensboro.db.QueryDelete;
import es.atareao.queensboro.db.Row;
import es.atareao.queensboro.db.WrapperTable;

/**
 *
 * @author Lorenzo Carbonell
 */
public class User extends WrapperTable<User>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public User(){
        super();
    }
    public User(Conector conector,String id) throws SQLException{
        super(User.class,conector,"information_schema","users",id);
    }    
    public User(Conector conector) throws SQLException{
        super(User.class,conector,"information_schema","users");
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
        return this.dropUser(this.getValue("name"));
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
        for(User seleccionado:this.find(conditions)){
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
        for(User seleccionado:this.find()){
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
        String userName=this.getValue("name");
        boolean admin=Convert.toBoolean(this.getValue("admin"));
        return this.createUser(userName,null, admin);
    }
    /**
     * 
     * @param password
     * @return
     * @throws java.sql.SQLException
     */
    public boolean insert(String password) throws SQLException{
        String userName=this.getValue("name");
        boolean admin=Convert.toBoolean(this.getValue("admin"));
        if(userName!=null){
            return this.createUser(userName,password, admin);
        }
        return false;
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
     */
    @Override
    public boolean update() throws SQLException{
        boolean retorno=false;
        if(this.getId()!=null){
            String userName=this.readValue(this.getId(),"name");
            String newUserName=this.getValue("name");
            if(!userName.equals(newUserName)){
                retorno=this.rename(userName, newUserName);
            }
            String admin=this.readValue(this.getId(),"admin");
            String newAdmin=this.getValue("admin");
            if(!admin.equals(newAdmin)){
                retorno=this.setUserAdmin(this.getValue("name"), Convert.toBoolean(newAdmin));
            }
            return retorno;
        }
        return false;
    }
    public boolean update(String password) throws SQLException{
        boolean retorno=false;
        if(this.getId()!=null){
            String userName=this.readValue(this.getId(),"name");
            String newUserName=this.getValue("name");
            if(!userName.equals(newUserName)){
                retorno=this.rename(userName, newUserName);
            }
            String admin=this.readValue(this.getId(),"admin");
            String newAdmin=this.getValue("admin");
            if(!admin.equals(newAdmin)){
                retorno=this.setUserAdmin(this.getValue("name"), Convert.toBoolean(newAdmin));
            }
            if(password!=null){
                retorno=this.setPassword(this.getValue("name"), password);
            }
            return retorno;
        }
        return false;
        
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private boolean createUser(String userName,String password,boolean admin) throws SQLException{
        if(userName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE USER IF NOT EXISTS ");
            sb.append(userName);
            if(password!=null){
                sb.append(" PASSWORD ");
                sb.append(password);
            }
            if(admin){
                sb.append(" ADMIN");
            }
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean dropUser(String userName) throws SQLException{
        if(userName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("DROP USER IF EXISTS ");
            sb.append(userName);
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean setUserAdmin(String userName,boolean admin) throws SQLException{
        if(userName!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER USER ");
            sb.append(userName);
            sb.append(" ADMIN ");
            if(admin){
                sb.append(" TRUE");
            }else{
                sb.append(" FALSE");
            }
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean rename(String userName,String newUserName) throws SQLException{
        if((userName!=null)&&(newUserName!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER USER ");
            sb.append(userName);
            sb.append(" RENAME TO ");
            sb.append(newUserName);
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean setPassword(String userName,String password) throws SQLException{
        if((userName!=null)&&(password!=null)){
            StringBuffer sb = new StringBuffer();
            sb.append("ALTER USER ");
            sb.append(userName);
            sb.append(" SET PASSWORD ");
            sb.append(password);
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
