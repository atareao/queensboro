/*
 * OnlyOneValidator.java
 *
 * Created on 16 de marzo de 2010, 21:32
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
//***********************************PAQUETE************************************
//

package es.atareao.queensboro.val;

import java.sql.SQLException;
import java.util.Vector;
//
import es.atareao.queensboro.db.Condition;
import es.atareao.queensboro.db.WrapperTable;
//********************************IMPORTACIONES*********************************
import es.atareao.alejandria.lib.Convert;
//

/**
 *
 * @author Propietario
 */
public class OnlyOneValidator extends DefaultValidator{
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private String _columnName;
    private String _value;
    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /** Creates a new instance of LengthValidator */
    public OnlyOneValidator(WrapperTable<? extends WrapperTable> wrapper,String columnName,String value) {
        this.setWrapper(wrapper);
        this.setColumnName(columnName);
        this.setValue(value);
        this.setMessage("El valor introducido en "+columnName+" no es válido, sólo puede existir uno.");
    }
    
    //
    //********************************METODOS***********************************
    //
    @Override
    public boolean validate() throws ValidationException {

        Vector<Condition> conditions=new Vector<Condition>();
        if(!this.getWrapper().getValue(_columnName).equals(_value)){
            return true;
        }
        conditions.add(new Condition(_columnName,this.getWrapper().getValue(_columnName)));
        if(this.getWrapper().getId().length()>0){
            conditions.add(new Condition("ID",this.getWrapper().getId(),Condition.DISTINTO));
        }
        try {
            if(!this.getWrapper().hasAny(conditions)){
                return true;
            }else{
                throw(new ValidationException(this.getMessage()));
            }
        } catch (SQLException ex) {
            throw(new ValidationException(this.getMessage()));
        }
    }
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    //
    //**************************METODOS DE ACCESO*******************************
    //
    /**
     * @return the _columnName
     */
    public String getColumnName() {
        return _columnName;
    }

    /**
     * @param columnName the _columnName to set
     */
    public void setColumnName(String columnName) {
        this._columnName = columnName;
    }

    /**
     * @return the _value
     */
    public String getValue() {
        return _value;
    }

    /**
     * @param value the _value to set
     */
    public void setValue(String value) {
        this._value = value;
    }
}
