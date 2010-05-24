/*
 * Validator.java
 *
 * Created on 28 de agosto de 2007, 21:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.atareao.queensboro.val;

import es.atareao.queensboro.db.WrapperTable;

/**
 *
 * @author Propietario
 */
public interface Validator {
    WrapperTable<? extends WrapperTable> getWrapper();

    void setWrapper(WrapperTable<? extends WrapperTable> wrapper);

    boolean validate() throws ValidationException;

    String getMessage();
    
}
