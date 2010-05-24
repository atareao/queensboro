/*
 * ValidationException.java
 *
 * Created on 29 de agosto de 2007, 7:14
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

/**
 *
 * @author Propietario
 */
public class ValidationException extends Exception{
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    
    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /** Creates a new instance of ValidationException */
    public ValidationException(String message, Throwable cause) {
        super(message,cause);
    }
    public ValidationException(String message){
        super(message);
    }
    public ValidationException(Throwable cause) {
        super(cause);
    }
    public ValidationException() {
        super();
    }
    //
    //********************************METODOS***********************************
    //
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    
    //
    //**************************METODOS DE ACCESO*******************************
    //
    
}
