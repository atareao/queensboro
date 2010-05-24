/*
 * LengthValidator.java
 *
 * Created on 28 de agosto de 2007, 21:33
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
import es.atareao.alejandria.val.SimpleLengthValidator;
//

/**
 *
 * @author Propietario
 */
public class LengthValidator extends DefaultColumnValidator {
    //
    //********************************CONSTANTES********************************
    //
    
    //
    // *********************************CAMPOS*********************************
    //
    private SimpleLengthValidator _slv;
    //
    //******************************CONSTRUCTORES*******************************
    //
    
    /** Creates a new instance of LengthValidator */
    public LengthValidator(WrapperTable<? extends WrapperTable> wrapper,String columnName,int length,boolean biggerThan) {
        this.setWrapper(wrapper);
        this.setColumnName(columnName);
        this.setMessage(createMessage(columnName,biggerThan,length));
        this.setSlv(new SimpleLengthValidator(this.getWrapper().getValue(this.getColumnName()),length,biggerThan));
    }
    public LengthValidator(WrapperTable<? extends WrapperTable> wrapper,String columnName,int length) {
        this(wrapper,columnName,length,true);
    }
    //
    //********************************METODOS***********************************
    //
    public boolean validate() throws ValidationException {
        if(this.getSlv().validate()){
            return true;
        }
        throw(new ValidationException(this.getMessage()));
    }
    
    //
    //**************************METODOS AUXILIARES******************************
    //
    private static String createMessage(String columnName,boolean biggerThan,int length){
        String message="";
        if(biggerThan){
            message="El valor introducido en "+columnName+" no es válido, tiene que ser mayor que "+Integer.toString(length)+".";
        }else{
            message="El valor introducido en "+columnName+" no es válido, tiene que ser menor que "+Integer.toString(length)+".";
        }
        return message;
    }
    //
    //**************************METODOS DE ACCESO*******************************
    //
    public SimpleLengthValidator getSlv() {
        return _slv;
    }

    public void setSlv(SimpleLengthValidator slv) {
        this._slv = slv;
    }
}
