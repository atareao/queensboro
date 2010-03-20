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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
/**
 *
 * @author Protactino
 */
public class QueryInsert extends Query {
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private Row _row;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de QuerySelect
     */
    public QueryInsert(String schemaName,String tableName,Vector<Condition> conditions){
        this.setSchemaName(schemaName);
        this.setTableName(tableName);
        this.setRow(new Row());
    }
    public QueryInsert(String schemaName,String tableName){
        this(schemaName,tableName,new Vector<Condition>());
    }    

    public Row getRow() {
        return _row;
    }

    public void setRow(Row row) {
        this._row = row;
    }
    
    //
    //********************************METODOS***********************************
    //
    
    public void addColumnNameAndValue(String column,String columnValue){
        this.getRow().put(column, columnValue);
    }
    
    
    private String listaColumnasNamesAndValues(){
        this.getRow().remove("ID");  
        StringBuffer sbNames=new StringBuffer("(");
        StringBuffer sbValues=new StringBuffer("(");
        Enumeration<String> columnas=this.getRow().getColumnNames();
        while(columnas.hasMoreElements()){
            String columna=columnas.nextElement();
            sbNames.append(columna);
            sbValues.append(this.getRow().getInsertValue(columna));
            if(columnas.hasMoreElements()){
                sbNames.append(",");
                sbValues.append(",");
            }                
        }
        sbNames.append(")");
        sbValues.append(")");
        return sbNames.toString()+" VALUES "+sbValues.toString();
    }

    @Override
    public String toSql() {
        StringBuffer sb=new StringBuffer();
        sb.append("INSERT INTO ");
        sb.append(Nomenclator.stdName(getSchemaName()));
        sb.append(".");
        sb.append(Nomenclator.stdName(this.getTableName()));
        sb.append(" ");
        sb.append(this.listaColumnasNamesAndValues());
        sb.append(" ");
        if((this.getConditions()!=null)&&(this.getConditions().size()>0)){
            sb.append(conditionsToSql());
        }        
        sb.append(";");
        return sb.toString();
    }
}
