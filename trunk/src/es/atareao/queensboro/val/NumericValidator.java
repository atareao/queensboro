/*
 * NumericValidator.java
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

//
import es.atareao.queensboro.db.WrapperTable;
//********************************IMPORTACIONES*********************************
import es.atareao.alejandria.val.SimpleNumericValidator;
//

/**
 *
 * @author Propietario
 */
public class NumericValidator  extends DefaultColumnValidator {
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //

    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /** Creates a new instance of LengthValidator */
    public NumericValidator(WrapperTable<? extends WrapperTable> wrapper,String columnName) {
        this.setWrapper(wrapper);
        this.setColumnName(columnName);
        this.setMessage("El valor introducido en "+this.getColumnName()+" no es válido, tiene que ser un número.");
    }
    
    //
    //********************************METODOS***********************************
    //
    public boolean validate() throws ValidationException {
        String cadena=this.getWrapper().getValue(this.getColumnName());
        SimpleNumericValidator snv=new SimpleNumericValidator(cadena);
        if(snv.validate()){
            return true;
        }
        throw(new ValidationException(this.getMessage()));
    }
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    
    //
    //**************************METODOS DE ACCESO*******************************
    //

}
