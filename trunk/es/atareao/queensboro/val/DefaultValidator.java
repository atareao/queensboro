/*
 * DefaultValidator.java
 *
 * Created on 29 de agosto de 2007, 7:38
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
//********************************IMPORTACIONES*********************************
//
import es.atareao.queensboro.db.WrapperTable;


/**
 *
 * @author Propietario
 */
public abstract class DefaultValidator implements Validator {
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private WrapperTable<? extends WrapperTable> _wrapper;
    private String _message;
    
    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /**
     * Creates a new instance of DefaultValidator
     */
    //
    //********************************METODOS***********************************
    //
    public abstract boolean validate() throws ValidationException;
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    
    //
    //**************************METODOS DE ACCESO*******************************
    //

    protected void setMessage(String message) {
        this._message = message;
    }

    public String getMessage() {
        return _message;
    }

    public WrapperTable<? extends WrapperTable> getWrapper() {
        return _wrapper;
    }


    public void setWrapper(WrapperTable<? extends WrapperTable> wrapper) {
        this._wrapper = wrapper;
    }
}
