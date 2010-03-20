package es.atareao.queensboro.db;

/*
 * OrderExpression.java
 *
 * TODO: Descripcion
 *
 * Creado en 15 de octubre de 2006, 18:21
 *
 * Copyright (C) 15 de octubre de 2006, Protactino
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

/**
 *
 * @author Protactino
 */
public class Order {
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private String _column;
    private boolean _ascendent;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de OrderExpression
     */
    public Order(String column) {
        this.setColumn(column);
        this.setAscendent(true);
    }
    public Order(String column,boolean ascendent) {
        this.setColumn(column);
        this.setAscendent(ascendent);
    }
    public Order(String column,boolean ascendent,boolean nullsLast) {
        this.setColumn(column);
        this.setAscendent(ascendent);
    }
    
    //
    //**************************METODOS DE ACCESO*******************************
    //
    public boolean isAscendent() {
        return _ascendent;
    }

    public void setAscendent(boolean ascendent) {
        this._ascendent = ascendent;
    }

    public String getColumn() {
        return _column;
    }

    public void setColumn(String column) {
        this._column = Nomenclator.stdName(column);
    }
    //
    //********************************METODOS***********************************
    //
    public String toSql(){
        StringBuffer sb=new StringBuffer();
        sb.append(this.getColumn());
        if(this.isAscendent()){
            sb.append(" ASC");
        }else{
            sb.append(" DESC");
        }
        return sb.toString();
    }
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Order){
            Order oe=(Order)obj;
            if(this.toSql().equals(oe.toSql())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this._column != null ? this._column.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getColumn();
    }
   
}
