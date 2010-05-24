package es.atareao.queensboro.db;

/*
 * ***********************Software description*********************************
 * Conector.java
 * 
 * 
 * ***********************Software description*********************************
 * 
 * Copyright (C) 2007 - Lorenzo Carbonell
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
 * 
 * **************************Software License***********************************
 * 
 */



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Propietario
 */
public interface Conector {


    /**
     * Activa el driver. Se hace al crear el objeto connector de forma automática
     */
    void activateDriver();

    /**
     * <code>closeConnection</code> se utiliza para cerrar la conexión con la base de
     * datos. En caso de error, lanza una excepción a la pila, pero no sale.
     *
     * @throws java.sql.SQLException si existe algún problema
     */
    void closeConnection() throws SQLException;

    /**
     * Ejecuta una sentencia sql devolviendo un Resultet
     * @param sql sentencia que se ejecuta
     * @return Resultset con los resultados obtenidos de la sentencia
     * @throws java.sql.SQLException si falla la ejecución
     */
    ResultSet executeQuery(String sql) throws SQLException;

    long executeQueryCount(QueryCount queryCount) throws SQLException;

    boolean executeQueryInsert(QueryInsert queryInsert) throws SQLException;

    Vector<Row> executeQuerySelect(QuerySelect querySelect) throws SQLException;

    boolean executeQueryUpdate(QueryUpdate queryUpdate) throws SQLException;

    boolean executeQueyDelete(QueryDelete queryDelete) throws SQLException;

    /**
     * Ejecuta una sentencia sql para actualizar un registro
     * @param sql sentencia que se ejecuta para actualizar el registro
     * @return un entero que indica el número de registros modificados
     * @throws java.sql.SQLException
     */
    int executeUpdate(String sql) throws SQLException;

    /**
     * <code>get_conexion</code> se utiliza para obtener la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @return _conexion, _conexion con la base de datos especificada.
     */
    Connection getConexion();

    /**
     * <code>getDatabaseName</code>Permite conocer la el nombre de la base de datos
     * @return nombre de la base de datos
     */
    String getDatabaseName();

    /**
     * <code>get_driver</code> devuelve el nombre del _driver que se utiliza para
     * realizar la conexión con la base de datos.
     * @return _driver, es el nombre del _driver utilizado para definir la
     * conexión a la base de datos.
     */
    String getDriver();

    /**
     * <code>getPassword</code> devuelve la contraseña especificada para el
     * usuario definido con <code>setUser</code>
     * @return password, contraseña valida para el usuario.
     */
    char[] getPassword();

    /**
     *
     * @return
     */
    String getPort();

    /**
     * <CODE>getServer</CODE>devuelve el servidor donde está ubicada la base de datos
     * @return <CODE>Server</CODE>, servidor donde está ubicada la base de datos
     */
    String getServer();

    /**
     * <code>getUrl</code> devuelve la cadena que se utilizar para realizar la
     * conexión con la base de datos
     * @return _url, cadena de conexión
     */
    String getUrl();

    /**
     * <code>getUser</code> devuelve el puerto de conexión del servidor
     * @return Port, es el puerto de conexión del servidor
     */
    String getUser();

    /**
     * <code>openConnection</code> realiza la conexión a la base de datos especificada
     * con las distintas propiedades indicadas. En caso de error no se conecta,
     * lanza un mensaje de error, pero no sale de la máquina virtual de java.
     * <b><i>Antes de realizar la conexión hay que definir todas las propiedades
     * de la base de datos que pretendemos abrir.</i></b>
     * @throws java.sql.SQLException Error
     */
    void openConnection() throws SQLException;

    /**
     * <code>set_conexion</code> se utiliza para definir la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @param conexion conexion con la base de datos especificada.
     */
    void setConexion(Connection conexion);

    /**
     * <code>setDatabaseName</code>Permite establecer el nombre de la base de datos
     *
     * @param databaseName nombre de la base de datos
     */
    void setDatabaseName(String databaseName);

    /**
     * <code>set_driver</code> define el _driver que se utiliza para realizar la
     * conexión con la base de datos.
     * @param driver es el nombre del _driver que se utilizar para definir la
     * conexión con la base de datos.
     */
    void setDriver(String driver);

    /**
     * <code>setPassword</code> especifica una contraseña válida para el
     * usuario definido con <code>setUser</code>
     * @param password contraseña valida para el usuario.
     */
    void setPassword(char[] password);

    /**
     * <code>setPort</code> establece el puerto de conexión del servidor
     *
     *
     * @param port puerto de conexión
     */
    void setPort(String port);

    /**
     * <CODE>setServer</CODE>establece el servidor donde está ubicada la base de datos
     *
     * @param server Servidor de base de datos
     */
    void setServer(String server);

    /**
     * <code>setUrl</code> define la cadena que se utiliza para realizar la
     * conexión con la base de datos
     * @param url cadena que se utiliza para realizar la conexión con la base de datos
     */
    void setUrl(String url);

    /**
     * <code>setUser</code> establece el usuario que accede a la base de datos
     * @param user que es el usuario que accede a la base de datos, es
     * importante indicar el usuario ya que esto permite realizar un control
     * estricto de las acciones que puede realizar
     */
    void setUser(String user);

    public boolean execute(String toString) throws SQLException;
}
