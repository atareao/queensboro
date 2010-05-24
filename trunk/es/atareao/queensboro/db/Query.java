package es.atareao.queensboro.db;

/*
 * ***********************Software description*********************************
 * NewClass.java
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

import java.util.Iterator;
import java.util.Vector;

//********************************IMPORTACIONES*********************************
//
/**
 *
 * @author Lorenzo Carbonell
 */
public abstract class Query {


    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    public abstract String toSql();
    protected String conditionsToSql(){
        if((this.getConditions()!=null)&&(this.getConditions().size()>0)){
            StringBuffer sb=new StringBuffer();
            sb.append(" WHERE ");
            Iterator iterator=this.getConditions().iterator();
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
    
    /**
     * Add a new condition for the query, the operator will be IGUAL and the condition AND
     * @param column The name of the column
     * @param value The value for the column
     */
    public void addCondition(String column,String value){
        this.addCondition(column,value,Condition.IGUAL,Condition.AND);
    }
    /**
     * Add a new condition for the query, the condition will be AND
     * @param column The name of the column
     * @param value The value of the column
     * @param operator The operator IGUAL,COMO,...
     */
    public void addCondition(String column,String value,int operator){
        this.addCondition(column,value,operator,Condition.AND);
    }
    /**
     * Add a new condition for the query
     * @param column The name of the column
     * @param value The value for the column
     * @param operator The operator IGUAL,COMO,...
     * @param condition AND / OR
     */
    public void addCondition(String column,String value,int operator,int condition){
        this.getConditions().add(new Condition(column,value,operator,condition));
    }
    /**
     * Add a new condition for the query
     * @param Condition The condition
     */
    public void addCondition(Condition Condition){
        this.getConditions().add(Condition);    
    }
    @Override
    public String toString() {
        return toSql();
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 
    private String _tableName;
    private String _schemaName;
    private Vector<Condition> _conditions;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 
    public String getSchemaName() {
        return _schemaName;
    }

    public String getTableName() {
        return _tableName;
    }

    public void setSchemaName(String schemaName) {
        this._schemaName = schemaName;
    }

    public void setTableName(String tableName) {
        this._tableName = tableName;
    }
    public Vector<Condition> getConditions() {
        return _conditions;
    }

    public void setConditions(Vector<Condition> conditions) {
        this._conditions = conditions;
    }    
    // </editor-fold> 
}
