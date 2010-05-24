/*
 * WrapperH2.java
 *
 * Created on 6 de agosto de 2007, 18:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.queensboro.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import es.atareao.queensboro.val.ValidationException;
import es.atareao.queensboro.val.Validator;
/**
 *
 * @param G 
 * @author Propietario
 */
public class WrapperTable<G extends WrapperTable> implements Comparable{
    //
    //********************************CONSTANTES********************************
    //

    //
    // *********************************CAMPOS*********************************    
    //
    private Conector _conector;
    private String _schemaName;
    private String _tableName;
    private Row _row;
    private Vector<Validator> _validators;
    private String _id;
    private Vector<String> _selected;
    private String _text;
    protected Class<? extends G> _wrapperClass;
    //
    //******************************CONSTRUCTORES*******************************
    //
    public WrapperTable(){
        this.setRow(new Row());
        this.setValidators(new Vector<Validator>());
        this.setSelected(new Vector<String>());
    }
    /**
     * A new instance for wrapper
     * @param wrapperClass 
     * @param conector the connector for this database
     * @param schemaName the name of the schema
     * @param tableName the name of the table
     * @param id the selected register
     * @throws java.sql.SQLException raises an exception when an error ocurs
     */
    public WrapperTable(Class<? extends G> wrapperClass,Conector conector,String schemaName,String tableName,String id) throws SQLException{
        this._wrapperClass=wrapperClass;
        this.setConector(conector);
        this.setSchemaName(schemaName);
        this.setTableName(tableName);
        this.setId(id);
        this.setRow(new Row());
        this.setValidators(new Vector<Validator>());
        this.setSelected(new Vector<String>());
    }
    /**
     * A new instance for wrapper
     * @param wrapperClass 
     * @param conector the conector for this database
     * @param schemaName the schema name
     * @param tableName the table name
     * @throws java.sql.SQLException raises an exception when an error ocurs
     */
    public WrapperTable(Class<? extends G> wrapperClass,Conector conector,String schemaName,String tableName) throws SQLException{
        this(wrapperClass,conector,schemaName,tableName,"");
    }
    
    //
    //******************************INICIALIZADOR*******************************
    //

    //
    //********************************METODOS***********************************
    //
    @Override
    public String toString() {
        return this.getValue("nombre");
    }
    //
    //****************************METODOS SELECTED******************************
    //
    /**
     * If the register is selected
     * @param element the ID of teh element we know if is selected
     * @return true if the element is selected
     */
    public boolean isSelected(String element){
        return this.getSelected().contains(element);
    }
    /**
     * Remove all the registers that are selected
     * @return true if all the registers were removed
     * @throws java.sql.SQLException raises an exception when ocurs an error
     */
    public boolean removeSelected() throws SQLException{
        for(String seleccionado:this.getSelected()){
            this.delete(new Condition("id",seleccionado));
        }
        return true;
    }
    /**
     * Select a register
     * @param element ID for the register that we want to select
     * @return true if the register is selected
     * @throws java.sql.SQLException raises an exception if ocurs an error
     */
    public boolean select(String element) throws SQLException{
        if(!this.isSelected(element)){
            if(this.hasAny(new Condition("id",element))){
                this.getSelected().add(element);
                return true;
            }
        }
        return false;
    }
    /**
     * Select all the registers in the table
     * @return true if all the registers were selected
     * @throws java.sql.SQLException raises an exception if ocurs an errer
     */
    public boolean selectAll() throws SQLException{
        this.unselectAll();
        for(G seleccionado:this.find()){
            this.getSelected().add(seleccionado.getValue("id"));
        }
        return true;
    }
    /**
     * Unslect all the registers selected in the table
     * @return true if all the registers were unselected
     */
    public boolean unselectAll(){
        this.getSelected().removeAllElements();
        return true;
    }
    /**
     * unselect a register if the register was selected
     * @param element ID for the register that we want to unselect
     * @return true if the register was unselect
     */
    public boolean unSelect(String element){
        if(this.isSelected(element)){
            this.getSelected().remove(element);
            return true;
        }
        return false;
    }
    //
    //*****************************METODOS SQL*********************************
    //
    /**
     * Count the number of the retisters in the table
     * @return number of the registers on the table
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public long count() throws SQLException{
        QueryCount queryCount =new QueryCount(this.getSchemaName(),this.getTableName());
        return this.count(queryCount);
    }
    /**
     * Count the number of register that  meet the condition suplied
     * @param condition condition that must meet 
     * @return the number of the registers that meet the suplied condition
     * @throws java.sql.SQLException raises an exception happens
     */
    public long count(Condition condition) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(condition);
        QueryCount queryCount =new QueryCount(this.getSchemaName(),this.getTableName(),conditions);
        return this.count(queryCount);
    }
    /**
     * Count the number of register that meet the conditions suplied
     * @param conditions the conditions that must meet the retiststers
     * @return the number of registers that meet the suplied conditions
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public long count(Vector<Condition> conditions) throws SQLException{
        QueryCount queryCount =new QueryCount(this.getSchemaName(),this.getTableName(),conditions);
        return this.count(queryCount);
    }
    /**
     * Count the number of register that meet the conditions suplied
     * @param queryCount condition that must meet the registers
     * @return the number of registers that meet the suplied condition
     * @throws java.sql.SQLException raises an eception if an error happens
     */
    public long count(QueryCount queryCount) throws SQLException{
        return this.getConector().executeQueryCount(queryCount);
    }
    public G createWrapper(){
        G wrapper;
        try{
            wrapper=_wrapperClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        wrapper.setConector(this.getConector());
        wrapper.setSchemaName(this.getSchemaName());
        wrapper.setTableName(this.getTableName());
        return wrapper;
    }
    /**
     * Delete a register
     * @param id the ID of the register that we want to remove
     * @return true if the register was removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean delete(String id) throws SQLException{
        QueryDelete queryDelete=new QueryDelete(this.getSchemaName(),this.getTableName());
        queryDelete.addCondition("id",id,Condition.IGUAL,Condition.AND);
        return this.delete(queryDelete);        
    }
    /**
     * Delete the current register
     * @return true if the current register was removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean delete() throws SQLException{
        QueryDelete queryDelete=new QueryDelete(this.getSchemaName(),this.getTableName());
        queryDelete.addCondition("id",this.getId(),Condition.IGUAL,Condition.AND);
        return this.delete(queryDelete);
    }
    /**
     * Delete all the registers than meet with the suplied condition
     * @param condition the condition that registers must meet to be deleted
     * @return true if the registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
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
    public boolean delete(Vector<Condition> conditions) throws SQLException{
        QueryDelete queryDelete=new QueryDelete(this.getSchemaName(),this.getTableName(),conditions);
        return this.delete(queryDelete);
        
    }
    /**
     * Delete all the registers than meet with the suplied conditions
     * @param queryDelete the conditions that registers must meet to be deleted
     * @return true if the registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean delete(QueryDelete queryDelete) throws SQLException{
        return this.getConector().executeQueyDelete(queryDelete);
    }
    /**
     * Delete all registers
     * @return true if all registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean deleteAll() throws SQLException{
        QueryDelete queryDelete=new QueryDelete(this.getSchemaName(),this.getTableName());
        return this.getConector().executeQueyDelete(queryDelete);
    }
    /**
     * Find all the registers ordered by orderColumn in ascendent order
     * @param orderColumnName the name of the column to order the results
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(String orderColumnName) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return find(conditions,orderColumnName,true);
    }
    /**
     * Find all the registers ordered by orderColumn
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true for organize all the register ascending
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(String orderColumnName,boolean ascendent) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return find(conditions,orderColumnName,ascendent);
    }
    /**
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true for organize all the register ascending
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Condition condition,String orderColumnName,boolean ascendent) throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(condition);
        return find(conditions,orderColumnName,ascendent);
    }
    /** 
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
     public Vector<G> find(Condition condition,String orderColumnName) throws SQLException{
        return find(condition,orderColumnName,true);
    }
     /**
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
      */
     public Vector<G> find(Condition condition) throws SQLException{
        return find(condition,"id",true);
    }
    
     /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true for organize all the register ascending
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
      */
     public Vector<G> find(Vector<Condition> conditions,String orderColumnName,boolean ascendent) throws SQLException{
        QuerySelect querySelect=new QuerySelect(this.getSchemaName(),this.getTableName());
        querySelect.setConditions(conditions);
        querySelect.addOrder(orderColumnName,ascendent);
        Vector<G> resultado=new Vector<G>();
        ResultSet rs=this.getConector().executeQuery(querySelect.toSql());
        while(rs.next()){
            resultado.add(fromResultSetToWrapper(rs));        
        }
        return resultado;        
    }
     /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
     public Vector<G> find(Vector<Condition> conditions,String orderColumnName) throws SQLException{
        return this.find(conditions,orderColumnName,true);
    }
     /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Vector<Condition> conditions) throws SQLException{
        return this.find(conditions,"id",true);
    }
     /**
     * Find all the registers that has the current ID
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        conditions.add(new Condition("id",this.getId()));
        return this.find(conditions,"id",true);
    }
    /**
     * Returns all the elements for the table
     * @return all the elements for the table
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> findAll() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        //conditions.add(this.getCondition("1","1"));
        return this.find(conditions,"id",true);
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true if the results are ordered ascendint
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition,String orderColumnName,boolean ascendent) throws SQLException{
        Vector<G> res=this.find(condition,orderColumnName,ascendent);
        if((res!=null)&&(res.size()>0)){
            return res.firstElement();
        }
        return null;
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition,String orderColumnName) throws SQLException{
        return this.findFirst(condition,orderColumnName,true);
    }
    /**
     * Find the register that meet with the current ID
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst() throws SQLException{
        return this.findFirst(new Condition("id",this.getFirstId()),"id",true);
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition) throws SQLException{
        return this.findFirst(condition,"id",true);
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true if the results are ordered ascendint
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions,String orderColumnName,boolean ascendent) throws SQLException{
        return this.findFirst(conditions,orderColumnName,ascendent);
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions,String orderColumnName) throws SQLException{
        return this.findFirst(conditions,orderColumnName);
    }
    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions) throws SQLException{
        return this.find(conditions).firstElement();
    }
    /**
     * If there are registers that meet with the supplied conditions
     * @param conditions the conditions that must meet the registers
     * @return true if there are registers that meet with the supplied conditions
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean hasAny(Vector<Condition> conditions) throws SQLException{
        long contador=count(conditions);
        if (contador>0) {
            return true;
        }
        return false;
    }
    /**
     * If there are registers that meet with the supplied conditions
     * @param condition the condition that must meet the registers
     * @return true if there are registers that meet with the supplied conditions
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */    
    public boolean hasAny(Condition condition) throws SQLException{
        long contador=count(condition);
        if (contador>0) {
            return true;
        }
        return false;
    }
    /**
     * Get the first ID in the table
     * @return the first ID in the table
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public String getFirstId() throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT MIN(id) FROM ");
        sb.append(this.getSchemaName());
        sb.append(".");
        sb.append(this.getTableName());
        sb.append(";");
        ResultSet rs=this.getConector().executeQuery(sb.toString());
        rs.next();
        return rs.getString(1);
    }
    /**
     * Get the last inserted ID
     * @return the last inserted ID
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public long getLastInsertId() throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT MAX(id) FROM ");
        sb.append(this.getSchemaName());
        sb.append(".");
        sb.append(this.getTableName());
        sb.append(";");
        ResultSet rs=this.getConector().executeQuery(sb.toString());
        rs.next();
        return rs.getLong(1);        
    }
    /**
     * Get the last inserted Item
     * @return the last inserted Item
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public G getLastInserted() throws SQLException{
        long id=this.getLastInsertId();
        if(id>0){
            String strId=String.valueOf(id);
            G g=this.createWrapper();
            g.setId(strId);
            g.read();
            return g;
        }
        return null;
    }
    /**
     * Insert a new  register in the table. Id is inserted
     * @param row 
     * @return 
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    protected boolean insertUid(Row row) throws SQLException{
        QueryInsert queryInsert=new QueryInsert(this.getSchemaName(),this.getTableName());
        queryInsert.setRow(row);
        return this.getConector().executeQueryInsert(queryInsert);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insertUid() throws SQLException{
        return this.insertUid(this.getRow());
    }    
    /**
     * Insert a new  register in the table. If ID is supplied, is not inserted
     * @param row 
     * @return 
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    protected boolean insert(Row row) throws SQLException{
        QueryInsert queryInsert=new QueryInsert(this.getSchemaName(),this.getTableName());
        if(row.containsColumn("id")){
            row.remove("id");
        }
        queryInsert.setRow(row);
        if(this.getConector().executeQueryInsert(queryInsert)){
            //If there is any autoincrement field
            G g=this.getLastInserted();
            this.setRow(g.getRow());
            this.setId(g.getId());
            return true;
        }
        return false;
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert() throws SQLException{
        return this.insert(this.getRow());
    }
    
    /**
     * List all the registers in the table
     * @return the registers in the table
     * @throws java.sql.SQLException raises an expcetion if an error happens
     */
    public Vector<G> list() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return this.find(conditions,"id",true);
    }
    
    /**
     * Update the values for current register. If ID is suplied, the current register
     * is set to ID
     * @param row 
     * @return 
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    protected boolean update(Row row) throws SQLException{
        QueryUpdate queryUpdate=new QueryUpdate(this.getSchemaName(),this.getTableName());
        if(row.containsColumn("id")){
            this.setId(row.getValue("id"));
            row.remove("id");
        }
        queryUpdate.setRow(row);
        queryUpdate.addCondition("id",this.getId());
        return this.getConector().executeQueryUpdate(queryUpdate);
    }
    /**
     * Update the values for current register.
     * @return true if the register is updated
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */    
    public boolean update() throws SQLException{
        return this.update(this.getRow());
    }
     /**
      * Read the values for register ID
      * @param id the register that we want to read the values
      * @return the values for the register
      * @throws java.sql.SQLException raises an exception if an error happens
      */
     public Row read(String id) throws SQLException{
        QuerySelect querySelect=new QuerySelect(this.getSchemaName(),this.getTableName());
        querySelect.addCondition("id",id,Condition.IGUAL,Condition.AND);
        Vector<Row> result=this.getConector().executeQuerySelect(querySelect);
        if((result!=null)&&(result.size()>0)){
            return result.firstElement();
        }
        return null;
    }
     /**
      * Read the values for register ID
      * @return the values for the register
      * @throws java.sql.SQLException raises an exception if an error happens
      */
    public boolean read() throws SQLException{
        Row row=read(this.getId());
        if(row!=null){
            this.setRow(read(this.getId()));
            return true;
        }
        return false;
    }
    /**
     * Read a value from a register
     * @param id the register from we want to read a value
     * @param columnName the name of the column we want to know the value
     * @return the value for the column
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public String readValue(String id,String columnName) throws SQLException{
        if(id!=null){
            Row row=read(id);
            if(row.containsColumn(columnName)){
                return row.getValue(columnName);
            }
        }
        return "";
    }
    /**
     * Get the value for a column in the current object
     * @param columnName tha name of the column we want to know the value
     * @return the value we want to know
     */
    public String getValue(String columnName){
        return this.getRow().getValue(columnName);
    }
    /**
     * Set the value for a column
     * @param columnName the name of the column
     * @param value tha value for this column
     */
    public void setValue(String columnName,String value){
        if(this.getRow().containsColumn(columnName)){
            this.getRow().remove(columnName);
        }
        if(value==null) {
            value = "";
        }
        value=value.replaceAll("\"","`");
        value=value.replaceAll("'","`");
        this.getRow().put(columnName,value);
    }
    
    /**
     * Validate the values in the object
     * @return true if the validation is ok
     * @throws es.atareao.queensboro.val.ValidationException raises an exception if an error ocurs
     */
    public boolean validate() throws ValidationException{
        Iterator iterator=this.getValidators().iterator();
        while(iterator.hasNext()){
            Validator validator=(Validator)iterator.next();
            if(validator.validate()==false){
                throw new ValidationException(validator.getMessage());
            }
        }
        return true;
    }
    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @param orders the orders to order the result
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    public Vector<G> view(String viewName, Vector<Condition> conditions, Vector<Order> orders) throws SQLException{
        Vector<G> resultado=new Vector<G>();
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(viewName);
        sb.append(" ");
        if((conditions!=null)&&(conditions.size()>0)){
            sb.append(this.Condition(conditions));
        }
        if((orders!=null)&&(orders.size()>0)){
            sb.append(this.orderToSql(orders));
        }
        sb.append(";");
        ResultSet rs=this.getConector().executeQuery(sb.toString());
        while(rs.next()){
            resultado.add(fromResultSetToWrapper(rs));        
        }
        return resultado;
    }
    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @param order the order to order the result
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */    
    public Vector<G> view(String viewName, Vector<Condition> conditions, Order order) throws SQLException{
        Vector<Order> orders=new Vector<Order>();
        orders.add(order);
        return view(viewName,conditions,orders);
    }
    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */    
    public Vector<G> view(String viewName, Vector<Condition> conditions) throws SQLException{
        return view(viewName,conditions,new Vector<Order>());
    }
    /**
     * Get a view for the table, selection all registers in the table
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
     public Vector<G> view() throws SQLException{
        Vector<Condition> conditions=new Vector<Condition>();
        return this.find(conditions,"id",true);
    }
     /**
      * Adds a validator to validate the update or insertion
      * @param validator the validator to add
      */
     public void addValidator(Validator validator){
        this.getValidators().add(validator);
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    public QuerySelect getQuerySelect(){
        return new QuerySelect(this.getSchemaName(),this.getTableName());
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    /**
     * Convert from resultset to hashtable
     * @param rs resultset to convert
     * @return the hasttable obteneid
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    protected Hashtable<String,String> fromResultSetToHashTable(ResultSet rs) throws SQLException{
        Hashtable<String,String> resultado=new Hashtable<String,String>();
        ResultSetMetaData rsmd=rs.getMetaData();
        int columnas=rsmd.getColumnCount();
        for(int contador=1;contador<=columnas;contador++){
            String columnName=rsmd.getColumnName(contador);
            if(rs.getString(contador)!=null){
                resultado.put(columnName,rs.getString(contador));
            }else{
                resultado.put(columnName,"");
            }
        }
        return resultado;
    }
    protected Row fromResultSetToRow(ResultSet rs) throws SQLException{
        Row row=new Row();
        ResultSetMetaData rsmd=rs.getMetaData();
        int columnas=rsmd.getColumnCount();
        for(int contador=1;contador<=columnas;contador++){
            String columnName=rsmd.getColumnName(contador);
            if(rs.getString(contador)!=null){
                row.put(columnName,rs.getString(contador));
            }else{
                row.put(columnName,"");
            }
        }
        return row;
    }
    protected G fromResultSetToWrapper(ResultSet rs) throws SQLException{
        Row row=fromResultSetToRow(rs);
        G wrapper=this.createWrapper();
        wrapper.setId(row.getValue("id"));
        wrapper.setRow(row);
        return wrapper;
    }    
    /**
     * Transform a vector of orders in a string
     * @param orders the vector of orders
     * @return an string that contains all the orders
     */
    protected String orderToSql(Vector<Order> orders){
        if((orders!=null)&&(orders.size()>0)){
            StringBuffer sb=new StringBuffer();
            sb.append(" ORDER BY ");
            Iterator iterator=orders.iterator();
            while(iterator.hasNext()){
                Order oe=(Order)iterator.next();
                sb.append(oe.toSql());
                if(iterator.hasNext()){
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }
    /**
     * Transform a vector of conditions in a string
     * @param conditions the vector of conditions to transform 
     * @return the string that contains all the conditions
     */
    protected String Condition(Vector<Condition> conditions){
        if((conditions!=null)&&(conditions.size()>0)){
            StringBuffer sb=new StringBuffer();
            sb.append(" WHERE ");
            Iterator iterator=conditions.iterator();
            if(iterator.hasNext()){
                sb.append(((Condition)iterator.next()).toSql());
            }
            while(iterator.hasNext()){
                Condition wc=(Condition)iterator.next();
                sb.append(wc.conditionToSql());
                sb.append(wc.toSql());
            }
            return sb.toString();
        }
        return "";
    }
    
    //
    //**************************METODOS DE ACCESO*******************************
    //
    /**
     * Get the conector for the database
     * @return the conector for the database
     */
    public Conector getConector() {
        return _conector;
    }

    /**
     * Set the conector for the database
     * @param conector the conector for the database
     */
    public void setConector(Conector conector) {
        this._conector = conector;
    }
    /**
     * Get the current ID
     * @return the current ID
     */
    public String getId() {
        return _id;
    }

    /**
     * Set the ID for the current register
     * @param id the ID for the current register
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Get the values for the current register
     * @return the values for the current register
     */
    public Row getRow() {
        return _row;
    }
    /**
     * Get the values for the current register
     * @param row 
     */
    public void setRow(Row row) {
        this._row = row;
    }
    /**
     * Get the validators for the current register
     * @return the validators for the current register
     */
    private Vector<Validator> getValidators() {
        return _validators;
    }
    /**
     * Set the validators for the current register
     * @param validators the validator for the current register
     */
    private void setValidators(Vector<Validator> validators) {
        this._validators = validators;
    }
    /**
     * Get the selected registers
     * @return the selected registers
     */
    public Vector<String> getSelected() {
        return _selected;
    }

    /**
     * Set the selected registers
     * @param selected the selected registers
     */
    protected void setSelected(Vector<String> selected) {
        this._selected = selected;
    }
    /**
     * Get the name of the schema
     * @return the name of the schema
     */
    public String getSchemaName() {
        return _schemaName;
    }

    /**
     * Set the name of the schema
     * @param schemaName the name of the schema
     */
    protected void setSchemaName(String schemaName) {
        this._schemaName = schemaName;
    }

    /**
     * Get the name of the current table
     * @return the name of the currente table
     */
    public String getTableName() {
        return _tableName;
    }

    /**
     * Set tha name of the current table
     * @param tableName the name of the current table
     */
    protected void setTableName(String tableName) {
        this._tableName = tableName;
    }
    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(((WrapperTable)o).toString());
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
}
