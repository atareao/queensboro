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
import es.atareao.queensboro.db.WrapperTemporalView;

/**
 *
 * @author Lorenzo Carbonell
 */
public class Column extends WrapperTemporalView<Column>{
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 
    public static final int INT  = 0;
    public static final int BOOLEAN  = 1;
    public static final int TINYINT  = 2;
    public static final int SMALLINT  = 3;
    public static final int BIGINT  = 4;
    public static final int IDENTITY  = 5;
    public static final int DECIMAL  = 6;
    public static final int DOUBLE  = 7;
    public static final int REAL  = 8;
    public static final int TIME  = 9;
    public static final int DATE  = 10;
    public static final int TIMESTAMP  = 11;
    public static final int BINARY  = 12;
    public static final int OTHER  = 13;
    public static final int VARCHAR  = 14;
    public static final int VARCHAR_IGNORECASE  = 15;
    public static final int CHAR  = 16;
    public static final int BLOB  = 17;
    public static final int CLOB  = 18;
    public static final int UUID  = 19;
    public static final int ARRAY = 20;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Column(){
        super();
    }
    public Column(Conector conector,String schema,String table,String id) throws SQLException{
        super(Column.class,conector,createSql(schema, table),id);
        this.setSchema(schema);
        this.setTable(table);
    }    
    public Column(Conector conector,String schema,String table) throws SQLException{
        super(Column.class,conector,createSql(schema, table),"");
        this.setSchema(schema);
        this.setTable(table);
    }    
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    @Override
    public String toString() {
        return this.getValue("column_name");
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
        public boolean delete() throws SQLException{
        return this.dropColumn(this.getValue("column_name"));
    }
    /**
     * Delete a register
     * @param columnName 
     * @return
     * @throws java.sql.SQLException 
     */
    public boolean delete(String columnName) throws SQLException{
        return this.delete(new Condition("column_name",columnName,Condition.IGUAL));
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
        for(Column seleccionado:this.find(conditions)){
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
    public boolean delete(QueryDelete queryDelete) throws SQLException{
        return delete(queryDelete.getConditions());
    }
    /**
     * Delete all registers
     * @return true if all registers were removed
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean deleteAll() throws SQLException{
        for(Column seleccionado:this.find()){
            seleccionado.delete();
        }
        return true;
    }

    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @param precision 
     * @param scale 
     * @param def 
     * @param notNull 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type, int precision,int scale,String def,boolean notNull) throws SQLException{
        return this.addColumn(columnName, type,precision,scale,def,notNull);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @param precision 
     * @param def 
     * @param notNull 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type, int precision,String def,boolean notNull) throws SQLException{
        return this.addColumn(columnName, type,precision,def,notNull);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @param def 
     * @param notNull 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type,String def,boolean notNull) throws SQLException{
        return this.addColumn(columnName, type,def,notNull);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @param def 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type,String def) throws SQLException{
        return this.addColumn(columnName, type,def);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @param notNull 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type,boolean notNull) throws SQLException{
        return this.addColumn(columnName, type,notNull);
    }
    /**
     * Insert a new register in the table. The values are in teh object
     * @param columnName 
     * @param type 
     * @return true if the register is created
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public boolean insert(String columnName,int type) throws SQLException{
        return this.addColumn(columnName, type);
    }
    public boolean rename(String newColumnName) throws SQLException{
        String columnName=this.getValue("column_name");
        if(columnName!=null){
            return this.renameColumn(columnName, newColumnName);
        }
        return false;
    }
    public boolean setDefault(String def) throws SQLException{
        String columnName=this.getValue("column_name");
        if(columnName!=null){
            return this.alterColumnSetDefault(columnName, def);
        }
        return false;
    }
    public boolean setNotNull(boolean notNull) throws SQLException{
        String columnName=this.getValue("column_name");
        if(columnName!=null){
            return this.alterColumnSetNotNull(columnName, notNull);
        }
        return false;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private static String createSql(String schema,String table){
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT *, ORDINAL_POSITION AS ID FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='");
        sb.append(schema.toUpperCase());
        sb.append("' AND TABLE_NAME='");
        sb.append(table);
        sb.append("' ");
        return sb.toString();

    }
    private boolean addColumn(String columnName,int type, int precision,int scale,String def,boolean notNull) throws SQLException{
        if(type==DECIMAL){
            StringBuffer sb=new StringBuffer();
            sb.append("ALTER TABLE ");
            sb.append(this.getSchema());
            sb.append(".");
            sb.append(this.getTable());
            sb.append(" ADD ");
            sb.append(columnName);
            sb.append(" DECIMAL (");
            sb.append(precision);
            sb.append(",");
            sb.append(scale);
            sb.append(") ");
            if(def!=null){
                sb.append("DEFAULT ");
                sb.append(def);
            }
            if(notNull){
                sb.append("NOT NULL");
            }
            sb.append(";");
            return this.getConector().execute(sb.toString());
        }
        return false;
    }
    private boolean addColumn(String columnName,int type, int precision,String def,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ADD ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        sb.append(" (");
        sb.append(precision);
        sb.append(") ");
        if(def!=null){
            sb.append("DEFAULT ");
            sb.append(def);
        }
        if(notNull){
            sb.append("NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());
    }
    private boolean addColumn(String columnName,int type,String def,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ADD ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        sb.append(" ");
        if(def!=null){
            sb.append("DEFAULT ");
            sb.append(def);
        }
        if(notNull){
            sb.append("NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());
    }
    private boolean addColumn(String columnName,int type,String def) throws SQLException{
        return addColumn(columnName,type,def,false);
    }
    private boolean addColumn(String columnName,int type,boolean notNull) throws SQLException{
        return addColumn(columnName,type,null,notNull);
    }
    private boolean addColumn(String columnName,int type) throws SQLException{
        return addColumn(columnName,type,null,false);
    }
    private boolean dropColumn(String columnName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" DROP COLUMN ");
        sb.append(columnName);
        sb.append(";");
        return this.getConector().execute(sb.toString());
        
    }
    private boolean renameColumn(String columnName,String newColumnName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" RENAME TO ");
        sb.append(newColumnName);
        sb.append(";");
        return this.getConector().execute(sb.toString());
        
    }
    private boolean alterColumn(String columnName,int type, int precision,int scale,String def,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        sb.append(" (");
        sb.append(precision);
        sb.append(",");
        sb.append(scale);
        sb.append(") ");
        if(def!=null){
            sb.append("DEFAULT ");
            sb.append(def);
        }
        if(notNull){
            sb.append("NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumn(String columnName,int type, int precision,String def,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        sb.append(" (");
        sb.append(precision);
        sb.append(") ");
        if(def!=null){
            sb.append("DEFAULT ");
            sb.append(def);
        }
        if(notNull){
            sb.append("NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumn(String columnName,int type,String def,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        if(def!=null){
            sb.append(" DEFAULT ");
            sb.append(def);
        }
        if(notNull){
            sb.append(" NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumn(String columnName,int type,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        if(notNull){
            sb.append(" NOT NULL");
        }
        sb.append(";");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumn(String columnName,int type) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" ");
        sb.append(fromTypeToString(type));
        sb.append(";");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumnSetDefault(String columnName,String def) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" SET DEFAULT '");
        sb.append(def);
        sb.append("';");
        return this.getConector().execute(sb.toString());        
    }
    private boolean alterColumnSetNotNull(String columnName,boolean notNull) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER TABLE ");
        sb.append(this.getSchema());
        sb.append(".");
        sb.append(this.getTable());
        sb.append(" ALTER COLUMN ");
        sb.append(columnName);
        sb.append(" SET ");
        if(notNull){
            sb.append("NOT");
        }
        sb.append("NULL;");
        return this.getConector().execute(sb.toString());        
        
    }
    
    private int formStringToType(String type){
        if (type.equals("INT")) {
            return INT;
        }
        if (type.equals("BOOLEAN")) {
            return BOOLEAN;
        }
        if (type.equals("TINYINT")) {
            return TINYINT;
        }
        if (type.equals("SMALLINT")) {
            return SMALLINT;
        }
        if (type.equals("BIGINT")) {
            return BIGINT;
        }
        if (type.equals("IDENTITY")) {
            return IDENTITY;
        }
        if (type.equals("DECIMAL")) {
            return DECIMAL;
        }
        if (type.equals("DOUBLE")) {
            return DOUBLE;
        }
        if (type.equals("REAL")) {
            return REAL;
        }
        if (type.equals("TIME")) {
            return TIME;
        }
        if (type.equals("DATE")) {
            return DATE;
        }
        if (type.equals("TIMESTAMP")) {
            return TIMESTAMP;
        }
        if (type.equals("BINARY")) {
            return BINARY;
        }
        if (type.equals("OTHER")) {
            return OTHER;
        }
        if (type.equals("VARCHAR")) {
            return VARCHAR;
        }
        if (type.equals("VARCHAR_IGNORECASE")) {
            return VARCHAR_IGNORECASE;
        }
        if (type.equals("CHAR")) {
            return CHAR;
        }
        if (type.equals("BLOB")) {
            return BLOB;
        }
        if (type.equals("CLOB")) {
            return CLOB;
        }
        if (type.equals("UUID")) {
            return UUID;
        }
        if (type.equals("ARRAY")) {
            return ARRAY;
        }
        return -1;
    }
    private String fromTypeToString(int type){
        switch (type){
            case INT: 
                return "INT";
            case BOOLEAN: 
                return "BOOLEAN";
            case TINYINT: 
                return "TINYINT";
            case SMALLINT: 
                return "SMALLINT";
            case BIGINT: 
                return "BIGINT";
            case IDENTITY: 
                return "IDENTITY";
            case DECIMAL: 
                return "DECIMAL";
            case DOUBLE: 
                return "DOUBLE";
            case REAL: 
                return "REAL";
            case TIME: 
                return "TIME";
            case DATE: 
                return "DATE";
            case TIMESTAMP: 
                return "TIMESTAMP";
            case BINARY: 
                return "BINARY";
            case OTHER: 
                return "OTHER";
            case VARCHAR: 
                return "VARCHAR";
            case VARCHAR_IGNORECASE: 
                return "VARCHAR_IGNORECASE";
            case CHAR: 
                return "CHAR";
            case BLOB: 
                return "BLOB";
            case CLOB: 
                return "CLOB";
            case UUID: 
                return "UUID";
            case ARRAY: 
                return "ARRAY";
                
        }
        return null;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 
    private String _schema;
    private String _table;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 
    public String getSchema() {
        return _schema;
    }

    public void setSchema(String schema) {
        this._schema = schema;
    }

    public String getTable() {
        return _table;
    }

    public void setTable(String table) {
        this._table = table;
    }
    // </editor-fold> 
}
