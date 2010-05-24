package es.atareao.queensboro.db;

/*
 * QuerySelect.java
 *
 * Copyright (c) 2010 Lorenzo Carbonell
 * email: lorenzo.carbonell.cerezo@gmail.com
 * website: http://www.atareao.es
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



//
//********************************IMPORTACIONES*********************************
//
import java.util.Vector;
/**
 *
 * @author Protactino
 */
public class QueryDelete extends Query{
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de QuerySelect
     */
    public QueryDelete(String schemaName,String tableName,Vector<Condition> conditions){
        this.setSchemaName(schemaName);
        this.setTableName(tableName);
        this.setConditions(conditions);
    }
    public QueryDelete(String schemaName,String tableName){
        this(schemaName,tableName,new Vector<Condition>());
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //
    
    //
    //********************************METODOS***********************************
    //

    public String toSql(){
        StringBuffer sb=new StringBuffer();
        sb.append("DELETE FROM ");
        sb.append(Nomenclator.stdName(this.getSchemaName()));
        sb.append(".");
        sb.append(Nomenclator.stdName(this.getTableName()));
        if((this.getConditions()!=null)&&(this.getConditions().size()>0)){
            sb.append(conditionsToSql());
        }        
        sb.append(";");
        return sb.toString();
    }
    //
    //**************************METODOS AUXILIARES******************************
    //

}
