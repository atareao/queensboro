package es.atareao.queensboro.db;

/*
 * Condition.java
 *
 * Created on 13-nov-2007, 21:00:52
 * 
 * This code is copyright (c) Lorenzo Carbonell 2007
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
 * and open the template in the editor.
 */


//
//********************************IMPORTACIONES*********************************
//

/**
 *
 * @author Protactino
 */
public class Condition {
    //
    //********************************CONSTANTES********************************
    //
    public static final int IGUAL=0;
    public static final int MENOR=1;
    public static final int MAYOR=2;
    public static final int MENOROIGUAL=3;
    public static final int MAYOROIGUAL=4;
    public static final int DISTINTO=5;
    public static final int COMO=6;
    public static final int NOCOMO=7;
    public static final int NULO=8;
    public static final int NONULO=9;
    public static final int AND=10;
    public static final int OR=11;
    //
    // *********************************CAMPOS*********************************
    //
    private int _operator;
    private int _condition;
    private String _column;
    private String _value;
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de WhereExpression
     * @param column 
     * @param value 
     * @param operator 
     * @param condition 
     */
    public Condition(String column,String value,int operator,int condition){
        this.setColumn(column);
        this.setValue(value);
        this.setOperator(operator);
        this.setCondition(condition);
    }

    public Condition(String column,String value,int operator){
        //this(column,value,Condition.AND);
        this.setColumn(column);
        this.setValue(value);
        this.setOperator(operator);
        this.setCondition(Condition.AND);
    }
    
    public Condition(String column,String value){
        this.setColumn(column);
        this.setValue(value);
        this.setOperator(Condition.IGUAL);
        this.setCondition(Condition.AND);
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //

    public int getOperator() {
        return _operator;
    }

    public void setOperator(int operator) {
        this._operator = operator;
    }

    public int getCondition() {
        return _condition;
    }

    public void setCondition(int condition) {
        this._condition = condition;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        this._value = Nomenclator.stdValue(value);
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
        sb.append(Nomenclator.stdName(this.getColumn()));
        sb.append(Condition.operatorToString(this.getOperator()));
        sb.append(Nomenclator.insValue(this.getValue()));
        return sb.toString();
    }
    @Override
    public String toString(){
        return this.getColumn();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Condition){
            Condition we=(Condition)obj;
            if(this.toSql().equals(we.toSql())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this._column != null ? this._column.hashCode() : 0);
        return hash;
    }
    
    public String conditionToSql(){
        switch (this.getCondition()){
            case AND:
                return " AND ";
            case OR:
                return " OR ";
        }
        return null;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
   
    private static String operatorToString(int operator){
        switch (operator){
            case IGUAL:
                return " = ";
            case MENOR:
                return " < ";
            case MAYOR:
                return " > ";
            case MENOROIGUAL:
                return " <= ";
            case MAYOROIGUAL:
                return " >= ";
            case DISTINTO:
                return " != ";
            case COMO:
                return " LIKE ";
            case NOCOMO:
                return " NOT LIKE ";
            case NULO:
                return " IS NULL ";
            case NONULO:
                return " IS NOT NULL ";
        }
        return "";
    }

    
}
