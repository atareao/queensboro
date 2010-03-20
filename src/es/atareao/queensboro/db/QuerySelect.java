package es.atareao.queensboro.db;

/*
 * QuerySelect.java
 *
 * TODO: Descripcion
 *
 * Creado en 4 de septiembre de 2006, 18:30
 *
 * Copyright (C) 4 de septiembre de 2006, Protactino
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */


//
//********************************IMPORTACIONES*********************************
//
import java.util.Iterator;
import java.util.Vector;
/**
 *
 * @author Protactino
 */
public class QuerySelect extends Query{
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private Vector<String> _columns;
    private Vector<Order> _orders;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de QuerySelect
     * @param schemaName
     * @param tableName 
     */
    public QuerySelect(String schemaName,String tableName){
        this.setSchemaName(schemaName);
        this.setTableName(tableName);
        this.setColumnNames(new Vector<String>());
        this.setConditions(new Vector<Condition>());
        this.setOrders(new Vector<Order>());
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //

    public Vector<String> getColumns() {
        return _columns;
    }

    public void setColumnNames(Vector<String> columns) {
        this._columns = columns;
    }

    public Vector<Order> getOrders() {
        return _orders;
    }

    public void setOrders(Vector<Order> orders) {
        this._orders = orders;
    }
    
    //
    //********************************METODOS***********************************
    //
    public void addOrder(String column,boolean ascendent,boolean nullsLast){
        this.getOrders().add(new Order(column,ascendent,nullsLast));
    }
    public void addOrder(String columnName,boolean ascendent){
        this.addOrder(columnName,ascendent,false);
    }
    public void addOrder(String columnName){
        this.addOrder(columnName,true,false);
    }
    public boolean addColumn(String column){
        return this.getColumns().add(column);
    }
    public String toSql(){
        StringBuffer sb=new StringBuffer();
        sb.append("SELECT ");
        sb.append(listaColumnas());
        sb.append(" FROM ");
        sb.append(Nomenclator.stdName(this.getSchemaName()));
        sb.append(".");
        sb.append(Nomenclator.stdName(this.getTableName()));
        if((this.getConditions()!=null)&&(this.getConditions().size()>0)){
            sb.append(this.conditionsToSql());
        }
        if((this.getOrders()!=null)&&(this.getOrders().size()>0)){
            sb.append(this.ordersToSql());
        }
        sb.append(";");
        return sb.toString();
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    
    private String listaColumnas(){
        StringBuffer sb=new StringBuffer();
        if((this.getColumns()!=null)&&(this.getColumns().size()>0)){
            Iterator iterator=this.getColumns().iterator();
            while(iterator.hasNext()){
                sb.append(Nomenclator.stdName((String)iterator.next()));
                if(iterator.hasNext()){
                    sb.append(",");
                }
            }
        }else{
            sb.append("*");
        }
        return sb.toString();
    }
    private String ordersToSql(){
        if((this.getOrders()!=null)&&(this.getOrders().size()>0)){
            StringBuffer sb=new StringBuffer();
            sb.append(" ORDER BY ");
            Iterator iterator=this.getOrders().iterator();
            while(iterator.hasNext()){
                Order order=(Order)iterator.next();
                sb.append(order.toSql());
                if(iterator.hasNext()){
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }
}
