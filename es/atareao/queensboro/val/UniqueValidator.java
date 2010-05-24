/*
 * UniqueValidator.java
 *
 * Created on 28 de agosto de 2007, 21:32
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
public class UniqueValidator extends DefaultValidator{
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private Vector<String> _columnNames;
    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /** Creates a new instance of LengthValidator */
    public UniqueValidator(WrapperTable<? extends WrapperTable> wrapper,Vector<String> columnNames) {
        this.setWrapper(wrapper);
        this.setColumnNames(columnNames);
        if(columnNames.size()>1){
            this.setMessage("Los valores introducidos en "+Convert.toString(columnNames,", ")+" no son válidos, ya existen.");
        }else{
            this.setMessage("El valor introducido en "+Convert.toString(columnNames,", ")+" no es válido, ya existe.");
        }
    }
    public UniqueValidator(WrapperTable<? extends WrapperTable> wrapper,String columnName) {
        Vector<String> columnNames=new Vector<String>();
        columnNames.add(columnName);
        this.setWrapper(wrapper);
        this.setColumnNames(columnNames);
        this.setMessage("El valor introducido en "+columnName+" no es válido, ya existe.");
    }
    
    //
    //********************************METODOS***********************************
    //
    public boolean validate() throws ValidationException {
        Vector<Condition> conditions=new Vector<Condition>();
        for(String columnName : this.getColumnNames()){
            conditions.add(new Condition(columnName,this.getWrapper().getValue(columnName)));
        }
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

    public Vector<String> getColumnNames() {
        return _columnNames;
    }

    public void setColumnNames(Vector<String> columnNames) {
        this._columnNames = columnNames;
    }
}
