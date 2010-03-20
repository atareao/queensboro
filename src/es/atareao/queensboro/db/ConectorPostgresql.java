/*
 * ConectorPostgresql.java
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

package es.atareao.queensboro.db;
//
//********************************IMPORTACIONES*********************************
//
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import es.atareao.alejandria.gui.ErrorDialog;
/**
 *
 * @author Protactino
 */
public class ConectorPostgresql implements Conector {
    //
    //********************************CONSTANTES********************************
    //
    private static final boolean DEBUG=true;
    public final static int BIG5=0;
    public final static int EUC_CN=1;
    public final static int EUC_JP=2;
    public final static int EUC_KR=3;
    public final static int EUC_TW=4;
    public final static int GB18030=5;
    public final static int GBK=6;
    public final static int ISO_8859_5=7;
    public final static int ISO_8859_6=8;
    public final static int ISO_8859_7=9;
    public final static int ISO_8859_8=10;
    public final static int JOHAB=11;
    public final static int KOI8=12;
    public final static int LATIN1=13;
    public final static int LATIN2=14;
    public final static int LATIN3=15;
    public final static int LATIN4=16;
    public final static int LATIN5=17;
    public final static int LATIN6=18;
    public final static int LATIN7=19;
    public final static int LATIN8=20;
    public final static int LATIN9=21;
    public final static int LATIN10=22;
    public final static int MULE_INTERNAL=23;
    public final static int SJIS=24;
    public final static int SQL_ASCII=25;
    public final static int UHC=26;
    public final static int UTF8=27;
    public final static int WIN866=28;
    public final static int WIN874=29;
    public final static int WIN1250=30;
    public final static int WIN1251=31;
    public final static int WIN1252=32;
    public final static int WIN1253=33;
    public final static int WIN1254=34;
    public final static int WIN1255=35;
    public final static int WIN1256=36;
    public final static int WIN1257=37;
    public final static int WIN1258=38;
            
    
    //
    // *********************************CAMPOS*********************************
    //
    /**
     * Nombre de la base de datos
     */
    private String _databaseName;
    /**
     * Nombre del servidor donde está ubicada la base de datos
     */
    private String _server;
    /**
     * Nombre del puerto del servidor donde está ubicada la base de datos
     */
    private String _port;
    /**
     * Nombre del usuario
     */
    private String _user;
    /**
     * Contraseña del usuario
     */
    private char[] _password;

    /**
     * <code>_conexion</code> es la conexión utilizada para la base de datos
     * en cuestión
     */
    private Connection _conexion;
    /**
     * Define el _driver que se utiliza para realizar las distintas _conexiones
     */
    private String _driver;
    /**
     * Define la cadena de conexión que se utiliza para realizar las _conexiones
     */
    private String _url;
    /**
     * Constructor por defecto de la clase ConectorPostgresql
     */    
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de ConectorPostgresql
     */
    public ConectorPostgresql(){
        this.setDriver("org.postgresql.Driver");
        this.setUrl("jdbc:postgresql:test");
        this.activateDriver();
    }

    public ConectorPostgresql(String server,String port,String databaseName,String userName,char[] password){
        this.setDriver("org.postgresql.Driver");
        this.setServer(server);
        this.setPort(port);
        this.setDatabaseName(databaseName);
        this.setUser(userName);
        this.setPassword(password);
        this.createUrl();
        this.activateDriver();
    }   
    //
    //**************************METODOS DE ACCESO*******************************
    //
    /**
     * <code>getDatabaseName</code>Permite conocer la el nombre de la base de datos
     * @return nombre de la base de datos
     */
    public String getDatabaseName() {
        return _databaseName;
    }

    /**
     * <code>setDatabaseName</code>Permite establecer el nombre de la base de datos
     * 
     * @param databaseName nombre de la base de datos
     */
    public void setDatabaseName(String databaseName) {
        this._databaseName = databaseName;
    }

    /**
     * <CODE>getServer</CODE>devuelve el servidor donde está ubicada la base de datos
     * @return <CODE>Server</CODE>, servidor donde está ubicada la base de datos
     */
    public String getServer() {
        return _server;
    }

    /**
     * <CODE>setServer</CODE>establece el servidor donde está ubicada la base de datos
     * 
     * @param server Servidor de base de datos
     */
    public void setServer(String server) {
        this._server = server;
    }
    /**
     * 
     * @return 
     */
    public String getPort() {
        return _port;
    }
    /**
     * <code>setPort</code> establece el puerto de conexión del servidor
     * 
     * 
     * @param port puerto de conexión
     */
    public void setPort(String port) {
        this._port = port;
    }
    /**
     * <code>getUser</code> devuelve el puerto de conexión del servidor
     * @return Port, es el puerto de conexión del servidor
     */
    public String getUser() {
        return _user;
    }
    /**
     * <code>setUser</code> establece el usuario que accede a la base de datos
     * @param user que es el usuario que accede a la base de datos, es
     * importante indicar el usuario ya que esto permite realizar un control
     * estricto de las acciones que puede realizar
     */
    public void setUser(String user) {
        this._user = user.toLowerCase();
    }
    /**
     * <code>getPassword</code> devuelve la contraseña especificada para el
     * usuario definido con <code>setUser</code>
     * @return password, contraseña valida para el usuario.
     */
    public char[] getPassword() {
        return _password;
    }
    /**
     * <code>setPassword</code> especifica una contraseña válida para el
     * usuario definido con <code>setUser</code>
     * @param password contraseña valida para el usuario.
     */
    public void setPassword(char[] password) {
        this._password = password;
    }

    /**
     * <code>get_conexion</code> se utiliza para obtener la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @return _conexion, _conexion con la base de datos especificada. 
     */
    public Connection getConexion() {
        return _conexion;
    }
    /**
     * <code>set_conexion</code> se utiliza para definir la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @param conexion conexion con la base de datos especificada.
     */
    public void setConexion(Connection conexion) {
        this._conexion = conexion;
    }
    /**
     * <code>set_driver</code> define el _driver que se utiliza para realizar la 
     * conexión con la base de datos.
     * @param driver es el nombre del _driver que se utilizar para definir la 
     * conexión con la base de datos.
     */
    public void setDriver(String driver)
    {
        this._driver=driver;
    }
    /**
     * <code>get_driver</code> devuelve el nombre del _driver que se utiliza para
     * realizar la conexión con la base de datos.
     * @return _driver, es el nombre del _driver utilizado para definir la
     * conexión a la base de datos.
     */
    public String getDriver()
    {
        return this._driver;
    }    
    /**
     * <code>setUrl</code> define la cadena que se utiliza para realizar la
     * conexión con la base de datos
     * @param url cadena que se utiliza para realizar la conexión con la base de datos
     */
    public void setUrl(String url)
    {
        this._url=url;
    }
    /**
     * <code>getUrl</code> devuelve la cadena que se utilizar para realizar la
     * conexión con la base de datos
     * @return _url, cadena de conexión
     */
    public String getUrl()
    {
        return this._url;
    }

    //
    //********************************METODOS***********************************
    //
    /**
     * Activa el driver. Se hace al crear el objeto connector de forma automática
     */
    public void activateDriver()
    {
        try
        {
            if(ConectorPostgresql.DEBUG) out("ACTIVANDO DRIVER");
            Class.forName(this.getDriver());
            if(ConectorPostgresql.DEBUG) out("DRIVER ACTIVADO");
            if(ConectorPostgresql.DEBUG) out("DRIVER UTILIZADO: ");
            if(ConectorPostgresql.DEBUG) out(this.getDriver());
        }
        catch(ClassNotFoundException e)
        {
            ConectorPostgresql.manejaError(e);
        }
    }
    /**
     * Prueba la conexión
     * @return false si la conexión no fue exitosa
     */
    public boolean testConnection(){
        try {
            this.openConnection();
            this.closeConnection();
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex,false);
            return false;
        }
        return true;
    }
    /**
     * <code>openConnection</code> realiza la conexión a la base de datos especificada
     * con las distintas propiedades indicadas. En caso de error no se conecta,
     * lanza un mensaje de error, pero no sale de la máquina virtual de java.
     * <b><i>Antes de realizar la conexión hay que definir todas las propiedades
     * de la base de datos que pretendemos abrir.</i></b> 
     * @throws java.sql.SQLException Error 
     */
    public void openConnection() throws SQLException{
        if(ConectorPostgresql.DEBUG) out("ABRIENDO CONEXIÓN");
        this.setConexion(DriverManager.getConnection(this.getUrl(),this.getUser(),new String(this.getPassword())));
        if(ConectorPostgresql.DEBUG) out("CONEXIÓN ABIERTA");
    }
    /**
     * <code>closeConnection</code> se utiliza para cerrar la conexión con la base de
     * datos. En caso de error, lanza una excepción a la pila, pero no sale.
     *
     * @throws java.sql.SQLException si existe algún problema
     */
    public void closeConnection() throws SQLException{
        if(ConectorPostgresql.DEBUG) out("CERRANDO CONEXIÓN");
        this.getConexion().close();
        if(ConectorPostgresql.DEBUG) out("CONEXIÓN CERRADA");
    }
    /**
     * Ejecuta una sentencia sql
     * @param sql es la sentencia que se pasa para ejecutar
     * @return true si el resutltado es correcto
     * @throws java.sql.SQLException si falla la ejecución
     */
    public boolean execute(String sql) throws SQLException
    {
        boolean resultado=false;
        if(ConectorPostgresql.DEBUG){
            ConectorPostgresql.out("EJECUTANDO:");
            ConectorPostgresql.out(sql);
        }
        this.getConexion().createStatement().execute(sql);
        resultado=true;
        if(ConectorPostgresql.DEBUG) ConectorPostgresql.out("RESULTADO: "+resultado);
        return resultado;
    }
    /**
     * Ejecuta una sentencia sql devolviendo un Resultet
     * @param sql sentencia que se ejecuta
     * @return Resultset con los resultados obtenidos de la sentencia
     * @throws java.sql.SQLException si falla la ejecución
     */
    public ResultSet executeQuery(String sql) throws SQLException
    {
        boolean resultado=false;
        ResultSet rset=null;
        
        if(ConectorPostgresql.DEBUG){
            ConectorPostgresql.out("EJECUTANDO:");
            ConectorPostgresql.out(sql);
        }
        rset=this.getConexion().createStatement().executeQuery(sql);
        if(rset.getWarnings()==null){
            resultado=true;
        }
        if(ConectorPostgresql.DEBUG) ConectorPostgresql.out("RESULTADO: "+resultado);
        return rset;
    }
    public long executeQueryCount(QueryCount queryCount) throws SQLException{
        ResultSet rs=this.executeQuery(queryCount.toSql());
        rs.next();
        return rs.getLong(1);        
    }
    public boolean executeQueyDelete(QueryDelete queryDelete) throws SQLException{
        if(this.executeUpdate(queryDelete.toSql())>0){
            return true;
        }
        return false;
    }
    public boolean executeQueryInsert(QueryInsert queryInsert) throws SQLException{
        if(this.executeUpdate(queryInsert.toSql())>0){        
            return true;
        }
        return false;
    }
    public boolean executeQueryUpdate(QueryUpdate queryUpdate) throws SQLException{
        if(this.executeUpdate(queryUpdate.toSql())>0){        
            return true;
        }
        return false;
        
    }
    public Vector<Row> executeQuerySelect(QuerySelect querySelect) throws SQLException{
        Vector<Row> resultado=new Vector<Row>();
        ResultSet rs=this.executeQuery(querySelect.toSql());
        while(rs.next()){
            resultado.add(fromResultSetToRow(rs));        
        }
        return resultado;        
    }
    /**
     * Ejecuta una sentencia sql para actualizar un registro
     * @param sql sentencia que se ejecuta para actualizar el registro
     * @return un entero que indica el número de registros modificados
     * @throws java.sql.SQLException
     */
    public int executeUpdate(String sql) throws SQLException{
        boolean resultado=false;
        if(ConectorPostgresql.DEBUG){
            ConectorPostgresql.out("EJECUTANDO:");
            ConectorPostgresql.out(sql);
        }
        int filas=this.getConexion().createStatement().executeUpdate(sql);
        if(filas>-1){
            resultado=true;
        }
        if(ConectorPostgresql.DEBUG) ConectorPostgresql.out("RESULTADO: "+resultado);

        return filas;
    }    
    /**
     * CREATE DATABASE creates a new PostgreSQL database.
     * To create a database, you must be a superuser or have the special CREATEDB privilege. See CREATE
     * USER.
     * Normally, the creator becomes the owner of the new database. Superusers can create databases owned by
     * other users, by using the OWNER clause. They can even create databases owned by users with no special
     * privileges. Non-superusers with CREATEDB privilege can only create databases owned by themselves.
     * By default, the new database will be created by cloning the standard system database template1. A
     * different template can be specified by writing TEMPLATE name. In particular, by writing TEMPLATE
     * template0, you can create a virgin database containing only the standard objects predefined by your
     * version of PostgreSQL. This is useful if you wish to avoid copying any installation-local objects that may
     * have been added to template1.
     * @param databaseName The name of a database to create.
     * @param ownerName The name of the database user who will own the new database, or DEFAULT to use the default
     * (namely, the user executing the command).
     * @param characterSet Character set encoding to use in the new database. Specify a string constant (e.g., ’SQL_ASCII’),
     * or an integer encoding number, or DEFAULT to use the default encoding (namely, the encoding of the
     * template database)
     * @return true si se ha creado la base de datos
     * @throws java.sql.SQLException si falla la creación de la base de datos
     */
    public boolean createDatabase(String databaseName,String ownerName,int characterSet) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("CREATE DATABASE ");
        sb.append(databaseName);
        sb.append(" WITH ");
        if((ownerName!=null)&&(ownerName.length()>0)){
            sb.append("OWNER = ");
            sb.append(ownerName);
        }
        sb.append(" ENCODING = ");
        sb.append(getCharacterSetName(characterSet));
        return this.execute(sb.toString());
        
    }
    /**
     * CREATE DATABASE creates a new PostgreSQL database.
     * To create a database, you must be a superuser or have the special CREATEDB privilege. See CREATE
     * USER.
     * Normally, the creator becomes the owner of the new database. Superusers can create databases owned by
     * other users, by using the OWNER clause. They can even create databases owned by users with no special
     * privileges. Non-superusers with CREATEDB privilege can only create databases owned by themselves.
     * By default, the new database will be created by cloning the standard system database template1. A
     * different template can be specified by writing TEMPLATE name. In particular, by writing TEMPLATE
     * template0, you can create a virgin database containing only the standard objects predefined by your
     * version of PostgreSQL. This is useful if you wish to avoid copying any installation-local objects that may
     * have been added to template1.
     * @param databaseName The name of a database to create.
     * @param characterSet Character set encoding to use in the new database. Specify a string constant (e.g., ’SQL_ASCII’),
     * or an integer encoding number, or DEFAULT to use the default encoding (namely, the encoding of the
     * template database)
     * @return true si se ha creado la base de datos
     * @throws java.sql.SQLException si falla la creación de la base de datos
     */
    public boolean createDatabase(String databaseName,int characterSet) throws SQLException{
        return this.createDatabase(databaseName,null, characterSet);
    }
    /**
     * CREATE DATABASE creates a new PostgreSQL database.
     * To create a database, you must be a superuser or have the special CREATEDB privilege. See CREATE
     * USER.
     * Normally, the creator becomes the owner of the new database. Superusers can create databases owned by
     * other users, by using the OWNER clause. They can even create databases owned by users with no special
     * privileges. Non-superusers with CREATEDB privilege can only create databases owned by themselves.
     * By default, the new database will be created by cloning the standard system database template1. A
     * different template can be specified by writing TEMPLATE name. In particular, by writing TEMPLATE
     * template0, you can create a virgin database containing only the standard objects predefined by your
     * version of PostgreSQL. This is useful if you wish to avoid copying any installation-local objects that may
     * have been added to template1.
     * @param databaseName The name of a database to create.
     * @return true si se ha creado la base de datos
     * @throws java.sql.SQLException si falla la creación de la base de datos
     */
    public boolean createDatabase(String databaseName) throws SQLException{
        return this.createDatabase(databaseName,null, UTF8);
    }
    /**
     * DROP DATABASE drops a database. It removes the catalog entries for the database and deletes the directory
     * containing the data. It can only be executed by the database owner. Also, it cannot be executed while you
     * or anyone else are connected to the target database. (Connect to postgres or any other database to issue
     * this command.)
     * DROP DATABASE cannot be undone. Use it with care!
     * @param databaseName The name of the database to remove.
     * @return true si se ha eliminado la base de datos
     * @throws java.sql.SQLException si falla el borrado de la base de datos
     */
    public boolean dropDatabase(String databaseName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("DROP DATABASE IF EXISTS ");
        sb.append(databaseName);
        return this.execute(sb.toString());
    }
    /**
     * Changes the name of the database. Only the database owner or a superuser can rename a
     * database; non-superuser owners must also have the CREATEDB privilege. The current database cannot be
     * renamed. (Connect to a different database if you need to do that.)
     * @param oldDatabaseName the old name of the database to rename
     * @param newDatabaseName the new name of the database 
     * @return true if the database is renamed
     * @throws java.sql.SQLException raise an exception if an error ocurs
     */
    public boolean renameDatabase(String oldDatabaseName,String newDatabaseName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER DATABASE ");
        sb.append(oldDatabaseName);
        sb.append(" RENAME TO ");
        sb.append(newDatabaseName);
        return this.execute(sb.toString());        
    }
    /**
     * Change the session default for a run-time configuration variable for a PostgreSQL
     * database. Whenever a new session is subsequently started in that database, the specified value
     * becomes the session default value. The database-specific default overrides whatever setting is present in
     * postgresql.conf or has been received from the postgres command line. Only the database owner or
     * a superuser can change the session defaults for a database. Certain variables cannot be set this way, or can
     * only be set by a superuser.
     * @param databaseName The name of the database
     * @param encoding The encoding type
     * @return true if the encoding type is changed
     * @throws java.sql.SQLException raise an exception if an error ocurrs
     */
    public boolean changeDatabaseEncoding(String databaseName, int encoding) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER DATABASE ");
        sb.append(databaseName);
        sb.append(" SET ENCODING TO ");
        sb.append(getCharacterSetName(encoding));
        return this.execute(sb.toString());        
    }
    /**
     * Changes the owner of the database. To alter the owner, you must own the database and also
     * be a direct or indirect member of the new owning role, and you must have the CREATEDB privilege. (Note
     * that superusers have all these privileges automatically.)
     * @param databaseName The name of the database.
     * @param ownerName The new owner name 
     * @return true if the owner is changed
     * @throws java.sql.SQLException raise an exception if an error ocurrs
     */
    public boolean changeDatabaseOwner(String databaseName, String ownerName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER DATABASE ");
        sb.append(databaseName);
        sb.append(" OWNER TO ");
        sb.append(ownerName);
        return this.execute(sb.toString());        
    }
    /**
     * Adds a new role to a PostgreSQL database cluster. A role is an entity that can own database
     * objects and have database privileges; a role can be considered a “user”, a “group”, or both depending on
     * how it is used. You must have CREATEROLE privilege or be a database superuser to use this command.
     * @param roleName The name of the new role.
     * @param superuser These clauses determine whether the new role is a “superuser”, who can override all access restrictions
     * within the database. Superuser status is dangerous and should be used only when really needed. 
     * You must yourself be a superuser to create a new superuser. If not specified, NOSUPERUSER is the
     * default.
     * @param createdb These clauses define a role’s ability to create databases. If CREATEDB is specified, the role being
     * defined will be allowed to create new databases. Specifying NOCREATEDB will deny a role the ability
     * to create databases. If not specified, NOCREATEDB is the default.
     * @param createrole These clauses determine whether a role will be permitted to create new roles (that is, execute CREATE
     * ROLE). A role with CREATEROLE privilege can also alter and drop other roles. If not specified,
     * NOCREATEROLE is the default.
     * @param inherit These clauses determine whether a role “inherits” the privileges of roles it is a member of. A role
     * with the INHERIT attribute can automatically use whatever database privileges have been granted to
     * all roles it is directly or indirectly a member of. Without INHERIT, membership in another role only
     * grants the ability to SET ROLE to that other role; the privileges of the other role are only available
     * after having done so. If not specified, INHERIT is the default.
     * @param login These clauses determine whether a role is allowed to log in; that is, whether the role can be given
     * as the initial session authorization name during client connection. A role having the LOGIN attribute
     * can be thought of as a user. Roles without this attribute are useful for managing database privileges,
     * but are not users in the usual sense of the word. If not specified, NOLOGIN is the default, except when
     * CREATE ROLE is invoked through its alternate spelling CREATE USER.
     * @param password Sets the role’s password. (A password is only of use for roles having the LOGIN attribute, but you
     * can nonetheless define one for roles without it.) If you do not plan to use password authentication
     * you can omit this option. If no password is specified, the password will be set to null and password
     * authentication will always fail for that user. A null password can optionally be written explicitly as
     * PASSWORD NULL.
     * @param encrypted These key words control whether the password is stored encrypted in the system catalogs. (If neither
     * is specified, the default behavior is determined by the configuration parameter password_encryption.)
     * If the presented password string is already in MD5-encrypted format, then it is stored encrypted
     * as-is, regardless of whether ENCRYPTED or UNENCRYPTED is specified (since the system cannot decrypt
     * the specified encrypted password string). This allows reloading of encrypted passwords during
     * dump/restore
     * @param inRole The IN ROLE clause lists one or more existing roles to which the new role will be immediately added
     * as a new member. (Note that there is no option to add the new role as an administrator; use a separate
     * GRANT command to do that.)
     * @param role The ROLE clause lists one or more existing roles which are automatically added as members of the
     * new role. (This in effect makes the new role a “group”.)
     * @param admin The ADMIN clause is like ROLE, but the named roles are added to the new role WITH ADMIN OPTION,
     * giving them the right to grant membership in this role to others.
     * @return true si se ha creado el nuevo Rol
     * @throws java.sql.SQLException si falla la sentencia sql
     */
    public boolean createRole(String roleName,boolean superuser,boolean createdb,boolean createrole,boolean inherit,boolean login,String password,boolean encrypted,Vector<String> inRole,Vector<String>role,Vector<String>admin) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("CREATE ROLE ");        
        sb.append(roleName);
        sb.append(" WITH ");
        if(!superuser) sb.append("NO");
        sb.append("SUPERUSER ");
        if(!createdb) sb.append("NO");
        sb.append("CREATEDB ");
        if(!createrole) sb.append("NO");
        sb.append("CREATEROLE ");        
        if(!inherit) sb.append("NO");
        sb.append("INHERIT ");        
        if(!login) sb.append("NO");
        sb.append("LOGIN ");        
        if((login)&&(password!=null)){
            sb.append("PASSWORD");
            sb.append(password);
            sb.append(" ");
        }
        if(!encrypted) sb.append("UN");
        sb.append("ENCRYPTED ");
        if(inRole!=null){
            sb.append("IN ROLE ");
            sb.append(vectorToString(inRole));
            sb.append(" ");
        }
        if(role!=null){
            sb.append("ROLE ");
            sb.append(vectorToString(role));
            sb.append(" ");
        }
        if(admin!=null){
            sb.append("ADMIN ");
            sb.append(vectorToString(inRole));
            sb.append(" ");
        }   
        return this.execute(sb.toString());
    }
    /**
     * ALTER ROLE changes the attributes of a PostgreSQL role.
     * The first variant of this command listed in the synopsis can change many of the role attributes that can
     * be specified in CREATE ROLE, which see for details. (All the possible attributes are covered, except that
     * there are no options for adding or removing memberships; use GRANT and REVOKE for that.) Attributes
     * not mentioned in the command retain their previous settings. Database superusers can change any of these
     * settings for any role. Roles having CREATEROLE privilege can change any of these settings, but only for
     * non-superuser roles. Ordinary roles can only change their own password.
     * The second variant changes the name of the role. Database superusers can rename any role. Roles having
     * CREATEROLE privilege can rename non-superuser roles. The current session user cannot be renamed.
     * (Connect as a different user if you need to do that.) Because MD5-encrypted passwords use the role name
     * as cryptographic salt, renaming a role clears its password if the password is MD5-encrypted.
     * The third and the fourth variant change a role’s session default for a specified configuration variable.
     * Whenever the role subsequently starts a new session, the specified value becomes the session default,
     * overriding whatever setting is present in postgresql.conf or has been received from the postgres
     * command line. (For a role without LOGIN privilege, session defaults have no effect.) Ordinary roles
     * can change their own session defaults. Superusers can change anyone’s session defaults. Roles having
     * CREATEROLE privilege can change defaults for non-superuser roles. Certain variables cannot be set this
     * way, or can only be set if a superuser issues the command.
     * @param roleName The name of the new role.
     * @param superuser These clauses determine whether the new role is a “superuser”, who can override all access restrictions
     * within the database. Superuser status is dangerous and should be used only when really needed. 
     * You must yourself be a superuser to create a new superuser. If not specified, NOSUPERUSER is the
     * default.
     * @param createdb These clauses define a role’s ability to create databases. If CREATEDB is specified, the role being
     * defined will be allowed to create new databases. Specifying NOCREATEDB will deny a role the ability
     * to create databases. If not specified, NOCREATEDB is the default.
     * @param createrole These clauses determine whether a role will be permitted to create new roles (that is, execute CREATE
     * ROLE). A role with CREATEROLE privilege can also alter and drop other roles. If not specified,
     * NOCREATEROLE is the default.
     * @param inherit These clauses determine whether a role “inherits” the privileges of roles it is a member of. A role
     * with the INHERIT attribute can automatically use whatever database privileges have been granted to
     * all roles it is directly or indirectly a member of. Without INHERIT, membership in another role only
     * grants the ability to SET ROLE to that other role; the privileges of the other role are only available
     * after having done so. If not specified, INHERIT is the default.
     * @param login These clauses determine whether a role is allowed to log in; that is, whether the role can be given
     * as the initial session authorization name during client connection. A role having the LOGIN attribute
     * can be thought of as a user. Roles without this attribute are useful for managing database privileges,
     * but are not users in the usual sense of the word. If not specified, NOLOGIN is the default, except when
     * CREATE ROLE is invoked through its alternate spelling CREATE USER.
     * @param password Sets the role’s password. (A password is only of use for roles having the LOGIN attribute, but you
     * can nonetheless define one for roles without it.) If you do not plan to use password authentication
     * you can omit this option. If no password is specified, the password will be set to null and password
     * authentication will always fail for that user. A null password can optionally be written explicitly as
     * PASSWORD NULL.
     * @param encrypted These key words control whether the password is stored encrypted in the system catalogs. (If neither
     * is specified, the default behavior is determined by the configuration parameter password_encryption.)
     * If the presented password string is already in MD5-encrypted format, then it is stored encrypted
     * as-is, regardless of whether ENCRYPTED or UNENCRYPTED is specified (since the system cannot decrypt
     * the specified encrypted password string). This allows reloading of encrypted passwords during
     * dump/restore
     * @return true si se modifica el Role
     * @throws java.sql.SQLException si se produce algún error en la ejecuón
     */
    public boolean alterRole(String roleName,boolean superuser,boolean createdb,boolean createrole,boolean inherit,boolean login,String password,boolean encrypted) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER ROLE ");        
        sb.append(roleName);
        sb.append(" WITH ");
        if(!superuser) sb.append("NO");
        sb.append("SUPERUSER ");
        if(!createdb) sb.append("NO");
        sb.append("CREATEDB ");
        if(!createrole) sb.append("NO");
        sb.append("CREATEROLE ");        
        if(!inherit) sb.append("NO");
        sb.append("INHERIT ");        
        if(!login) sb.append("NO");
        sb.append("LOGIN ");        
        if((login)&&(password!=null)){
            sb.append("PASSWORD");
            sb.append(password);
            sb.append(" ");
        }
        if(!encrypted) sb.append("UN");
        sb.append("ENCRYPTED ");

        return this.execute(sb.toString());
    }    /**
     * DROP ROLE removes the specified role(s). To drop a superuser role, you must be a superuser yourself; to
     * drop non-superuser roles, you must have CREATEROLE privilege.
     * A role cannot be removed if it is still referenced in any database of the cluster; an error will be raised if so.
     * Before dropping the role, you must drop all the objects it owns (or reassign their ownership) and revoke
     * any privileges the role has been granted. The REASSIGN OWNED and DROP OWNED commands can be
     * useful for this purpose.
     * However, it is not necessary to remove role memberships involving the role; DROP ROLE automatically
     * revokes any memberships of the target role in other roles, and of other roles in the target role. The other
     * roles are not dropped nor otherwise affected.
     * @param roleName The name of the role to remove.
     * @return true si se ha eliminado el Role
     * @throws java.sql.SQLException si falla la eliminación del Role
     */
    public boolean dropRole(String roleName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("DROP ROLE IF EXISTS ");
        sb.append(roleName);
        return this.execute(sb.toString());        
    }
    /**
     * Changes the name of the role. Database superusers can rename any role. Roles having
     * CREATEROLE privilege can rename non-superuser roles. The current session user cannot be renamed.
     * (Connect as a different user if you need to do that.) Because MD5-encrypted passwords use the role name
     * as cryptographic salt, renaming a role clears its password if the password is MD5-encrypted.
     * @param oldRoleName The old name of the role to rename.
     * @param newRoleName The new name of the role.
     * @return true if the role is renamed.
     * @throws java.sql.SQLException if it raised any exception.
     */
    public boolean renameRole(String oldRoleName,String newRoleName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER ROLE ");
        sb.append(oldRoleName);
        sb.append(" RENAME TO ");
        sb.append(newRoleName);
        return this.execute(sb.toString());        
    }
    /**
     * Adds a new user to a PostgreSQL database cluster. A role is an entity that can own database
     * objects and have database privileges; a role can be considered a “user”, a “group”, or both depending on
     * how it is used. You must have CREATEROLE privilege or be a database superuser to use this command.
     * @param userName The name of the new role.
     * @param superuser These clauses determine whether the new role is a “superuser”, who can override all access restrictions
     * within the database. Superuser status is dangerous and should be used only when really needed. 
     * You must yourself be a superuser to create a new superuser. If not specified, NOSUPERUSER is the
     * default.
     * @param createdb These clauses define a role’s ability to create databases. If CREATEDB is specified, the role being
     * defined will be allowed to create new databases. Specifying NOCREATEDB will deny a role the ability
     * to create databases. If not specified, NOCREATEDB is the default.
     * @param createrole These clauses determine whether a role will be permitted to create new roles (that is, execute CREATE
     * ROLE). A role with CREATEROLE privilege can also alter and drop other roles. If not specified,
     * NOCREATEROLE is the default.
     * @param inherit These clauses determine whether a role “inherits” the privileges of roles it is a member of. A role
     * with the INHERIT attribute can automatically use whatever database privileges have been granted to
     * all roles it is directly or indirectly a member of. Without INHERIT, membership in another role only
     * grants the ability to SET ROLE to that other role; the privileges of the other role are only available
     * after having done so. If not specified, INHERIT is the default.
     * @param login These clauses determine whether a role is allowed to log in; that is, whether the role can be given
     * as the initial session authorization name during client connection. A role having the LOGIN attribute
     * can be thought of as a user. Roles without this attribute are useful for managing database privileges,
     * but are not users in the usual sense of the word. If not specified, NOLOGIN is the default, except when
     * CREATE ROLE is invoked through its alternate spelling CREATE USER.
     * @param password Sets the role’s password. (A password is only of use for roles having the LOGIN attribute, but you
     * can nonetheless define one for roles without it.) If you do not plan to use password authentication
     * you can omit this option. If no password is specified, the password will be set to null and password
     * authentication will always fail for that user. A null password can optionally be written explicitly as
     * PASSWORD NULL.
     * @param encrypted These key words control whether the password is stored encrypted in the system catalogs. (If neither
     * is specified, the default behavior is determined by the configuration parameter password_encryption.)
     * If the presented password string is already in MD5-encrypted format, then it is stored encrypted
     * as-is, regardless of whether ENCRYPTED or UNENCRYPTED is specified (since the system cannot decrypt
     * the specified encrypted password string). This allows reloading of encrypted passwords during
     * dump/restore
     * @param inRole The IN ROLE clause lists one or more existing roles to which the new role will be immediately added
     * as a new member. (Note that there is no option to add the new role as an administrator; use a separate
     * GRANT command to do that.)
     * @param role The ROLE clause lists one or more existing roles which are automatically added as members of the
     * new role. (This in effect makes the new role a “group”.)
     * @param admin The ADMIN clause is like ROLE, but the named roles are added to the new role WITH ADMIN OPTION,
     * giving them the right to grant membership in this role to others.
     * @return true si se ha creado el nuevo Rol
     * @throws java.sql.SQLException si falla la sentencia sql
     */
    public boolean createUser(String userName,boolean superuser,boolean createdb,boolean createrole,boolean inherit,boolean login,String password,boolean encrypted,Vector<String> inRole,Vector<String>role,Vector<String>admin) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("CREATE USER ");        
        sb.append(userName);
        sb.append(" WITH ");
        if(!superuser) sb.append("NO");
        sb.append("SUPERUSER ");
        if(!createdb) sb.append("NO");
        sb.append("CREATEDB ");
        if(!createrole) sb.append("NO");
        sb.append("CREATEROLE ");        
        if(!inherit) sb.append("NO");
        sb.append("INHERIT ");        
        if(!login) sb.append("NO");
        sb.append("LOGIN ");        
        if((login)&&(password!=null)){
            sb.append("PASSWORD");
            sb.append(password);
            sb.append(" ");
        }
        if(!encrypted) sb.append("UN");
        sb.append("ENCRYPTED ");
        if(inRole!=null){
            sb.append("IN ROLE ");
            sb.append(vectorToString(inRole));
            sb.append(" ");
        }
        if(role!=null){
            sb.append("ROLE ");
            sb.append(vectorToString(role));
            sb.append(" ");
        }
        if(admin!=null){
            sb.append("ADMIN ");
            sb.append(vectorToString(inRole));
            sb.append(" ");
        }   
        return this.execute(sb.toString());
    }    
    /**
     * Changes the attributes of a PostgreSQL USER.
     * The first variant of this command listed in the synopsis can change many of the role attributes that can
     * be specified in CREATE ROLE, which see for details. (All the possible attributes are covered, except that
     * there are no options for adding or removing memberships; use GRANT and REVOKE for that.) Attributes
     * not mentioned in the command retain their previous settings. Database superusers can change any of these
     * settings for any role. Roles having CREATEROLE privilege can change any of these settings, but only for
     * non-superuser roles. Ordinary roles can only change their own password.
     * The second variant changes the name of the role. Database superusers can rename any role. Roles having
     * CREATEROLE privilege can rename non-superuser roles. The current session user cannot be renamed.
     * (Connect as a different user if you need to do that.) Because MD5-encrypted passwords use the role name
     * as cryptographic salt, renaming a role clears its password if the password is MD5-encrypted.
     * The third and the fourth variant change a role’s session default for a specified configuration variable.
     * Whenever the role subsequently starts a new session, the specified value becomes the session default,
     * overriding whatever setting is present in postgresql.conf or has been received from the postgres
     * command line. (For a role without LOGIN privilege, session defaults have no effect.) Ordinary roles
     * can change their own session defaults. Superusers can change anyone’s session defaults. Roles having
     * CREATEROLE privilege can change defaults for non-superuser roles. Certain variables cannot be set this
     * way, or can only be set if a superuser issues the command.
     * @param userName The name of the new role.
     * @param superuser These clauses determine whether the new role is a “superuser”, who can override all access restrictions
     * within the database. Superuser status is dangerous and should be used only when really needed. 
     * You must yourself be a superuser to create a new superuser. If not specified, NOSUPERUSER is the
     * default.
     * @param createdb These clauses define a role’s ability to create databases. If CREATEDB is specified, the role being
     * defined will be allowed to create new databases. Specifying NOCREATEDB will deny a role the ability
     * to create databases. If not specified, NOCREATEDB is the default.
     * @param createrole These clauses determine whether a role will be permitted to create new roles (that is, execute CREATE
     * ROLE). A role with CREATEROLE privilege can also alter and drop other roles. If not specified,
     * NOCREATEROLE is the default.
     * @param inherit These clauses determine whether a role “inherits” the privileges of roles it is a member of. A role
     * with the INHERIT attribute can automatically use whatever database privileges have been granted to
     * all roles it is directly or indirectly a member of. Without INHERIT, membership in another role only
     * grants the ability to SET ROLE to that other role; the privileges of the other role are only available
     * after having done so. If not specified, INHERIT is the default.
     * @param login These clauses determine whether a role is allowed to log in; that is, whether the role can be given
     * as the initial session authorization name during client connection. A role having the LOGIN attribute
     * can be thought of as a user. Roles without this attribute are useful for managing database privileges,
     * but are not users in the usual sense of the word. If not specified, NOLOGIN is the default, except when
     * CREATE ROLE is invoked through its alternate spelling CREATE USER.
     * @param password Sets the role’s password. (A password is only of use for roles having the LOGIN attribute, but you
     * can nonetheless define one for roles without it.) If you do not plan to use password authentication
     * you can omit this option. If no password is specified, the password will be set to null and password
     * authentication will always fail for that user. A null password can optionally be written explicitly as
     * PASSWORD NULL.
     * @param encrypted These key words control whether the password is stored encrypted in the system catalogs. (If neither
     * is specified, the default behavior is determined by the configuration parameter password_encryption.)
     * If the presented password string is already in MD5-encrypted format, then it is stored encrypted
     * as-is, regardless of whether ENCRYPTED or UNENCRYPTED is specified (since the system cannot decrypt
     * the specified encrypted password string). This allows reloading of encrypted passwords during
     * dump/restore
     * @return true si se modifica el Role
     * @throws java.sql.SQLException si se produce algún error en la ejecuón
     */
    public boolean alterUser(String userName,boolean superuser,boolean createdb,boolean createrole,boolean inherit,boolean login,String password,boolean encrypted) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER USER ");        
        sb.append(userName);
        sb.append(" WITH ");
        if(!superuser) sb.append("NO");
        sb.append("SUPERUSER ");
        if(!createdb) sb.append("NO");
        sb.append("CREATEDB ");
        if(!createrole) sb.append("NO");
        sb.append("CREATEROLE ");        
        if(!inherit) sb.append("NO");
        sb.append("INHERIT ");        
        if(!login) sb.append("NO");
        sb.append("LOGIN ");        
        if((login)&&(password!=null)){
            sb.append("PASSWORD");
            sb.append(password);
            sb.append(" ");
        }
        if(!encrypted) sb.append("UN");
        sb.append("ENCRYPTED ");

        return this.execute(sb.toString());
    }    /**
     * Removes the specified user. To drop a superuser role, you must be a superuser yourself; to
     * drop non-superuser roles, you must have CREATEROLE privilege.
     * A role cannot be removed if it is still referenced in any database of the cluster; an error will be raised if so.
     * Before dropping the role, you must drop all the objects it owns (or reassign their ownership) and revoke
     * any privileges the role has been granted. The REASSIGN OWNED and DROP OWNED commands can be
     * useful for this purpose.
     * However, it is not necessary to remove role memberships involving the role; DROP ROLE automatically
     * revokes any memberships of the target role in other roles, and of other roles in the target role. The other
     * roles are not dropped nor otherwise affected.
     * @param userName The name of the role to remove.
     * @return true si se ha eliminado el Role
     * @throws java.sql.SQLException si falla la eliminación del Role
     */
    public boolean dropUser(String userName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("DROP USER IF EXISTS ");
        sb.append(userName);
        return this.execute(sb.toString());        
    }
    /**
     * Changes the name of the role. Database superusers can rename any role. Roles having
     * CREATEROLE privilege can rename non-superuser roles. The current session user cannot be renamed.
     * (Connect as a different user if you need to do that.) Because MD5-encrypted passwords use the role name
     * as cryptographic salt, renaming a role clears its password if the password is MD5-encrypted.
     * @param oldUserName The old name of the role to rename.
     * @param newUserName The new name of the role.
     * @return true if the role is renamed.
     * @throws java.sql.SQLException if it raised any exception.
     */
    public boolean renameUser(String oldUserName,String newUserName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER USER ");
        sb.append(oldUserName);
        sb.append(" RENAME TO ");
        sb.append(newUserName);
        return this.execute(sb.toString());        
    }
    public boolean isUserInRole(String userName,String roleName) throws SQLException{
        QueryCount queryCount=new QueryCount("information_schema","applicable_roles");
        queryCount.addCondition("role_name",roleName,Condition.IGUAL,Condition.AND);
        queryCount.addCondition("grantee",userName,Condition.IGUAL,Condition.AND);
        long contador= this.executeQueryCount(queryCount);
        if (contador>0) return true;
        return false;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
    /**
     * Convert from resultset to hashtable
     * @param rs resultset to convert
     * @return the hasttable obteneid
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    protected Hashtable<String,String> fromResultSetToHashTable(ResultSet rs) throws SQLException{
        Hashtable<String,String> resultado=new Hashtable<String,String>();
        ResultSetMetaData rsmd=rs.getMetaData();
        int columnas=rsmd.getColumnCount();
        for(int contador=1;contador<=columnas;contador++){
            String columnName=rsmd.getColumnName(contador);
            if(rs.getString(contador)!=null){
                resultado.put(columnName,rs.getString(contador));
            }else{
                resultado.put(columnName,"");
            }
        }
        return resultado;
    }
    protected Row fromResultSetToRow(ResultSet rs) throws SQLException{
        Row row=new Row();
        ResultSetMetaData rsmd=rs.getMetaData();
        int columnas=rsmd.getColumnCount();
        for(int contador=1;contador<=columnas;contador++){
            String columnName=rsmd.getColumnName(contador);
            if(rs.getString(contador)!=null){
                row.put(columnName,rs.getString(contador));
            }else{
                row.put(columnName,"");
            }
        }
        return row;
    }
    
    private static String vectorToString(Vector<String> lista){
        StringBuffer sb=new StringBuffer();
        Iterator<String> iterator=lista.iterator();
        while(iterator.hasNext()){
            sb.append(iterator.next());
            if(iterator.hasNext()){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    private static String getCharacterSetName(int characterSet){
        switch(characterSet){
            case BIG5:
                return "BIG5";
            case EUC_CN:
                return "EUC_CN";
            case EUC_JP:
                return "EUC_JP";
            case EUC_KR:
                return "EUC_KR";
            case EUC_TW:
                return "EUC_TW";
            case GB18030:
                return "GB18030";
            case GBK:
                return "GBK";
            case ISO_8859_5:
                return "ISO_8859_5";
            case ISO_8859_6:
                return "ISO_8859_6";
            case ISO_8859_7:
                return "ISO_8859_7";
            case ISO_8859_8:
                return "ISO_8859_8";
            case JOHAB:
                return "JOHAB";
            case KOI8:
                return "KOI8";
            case LATIN1:
                return "LATIN1";
            case LATIN2:
                return "LATIN2";
            case LATIN3:
                return "LATIN3";
            case LATIN4:
                return "LATIN4";
            case LATIN5:
                return "LATIN5";
            case LATIN6:
                return "LATIN6";
            case LATIN7:
                return "LATIN7";
            case LATIN8:
                return "LATIN8";
            case LATIN9:
                return "LATIN9";
            case LATIN10:
                return "LATIN10";
            case MULE_INTERNAL:
                return "MULE_INTERNAL";
            case SJIS:
                return "SJIS";
            case SQL_ASCII:
                return "SQL_ASCII";
            case UHC:
                return "UHC";
            case UTF8:
                return "UTF8";
            case WIN866:
                return "WIN866";
            case WIN874:
                return "WIN874";
            case WIN1250:
                return "WIN1250";
            case WIN1251:
                return "WIN1251";
            case WIN1252:
                return "WIN1252";
            case WIN1253:
                return "WIN1253";
            case WIN1254:
                return "WIN1254";
            case WIN1255:
                return "WIN1255";
            case WIN1256:
                return "WIN1256";
            case WIN1257:
                return "WIN1257";
            case WIN1258:
                return "WIN1258";          
        }    
        return null;
    }
    private void createUrl(){
        //jdbc:postgresql://host:port/database
        StringBuffer sb=new StringBuffer("jdbc:postgresql://");
        sb.append(this.getServer());
        sb.append(":");
        sb.append(this.getPort());
        sb.append("/");
        sb.append(this.getDatabaseName());
        this.setUrl(sb.toString());
    }    
    /**
     * <code>manejaError</code> se utiliza para tratar errores, imprime el tipo
     * de error, lo vuelca en la pila de errores y sale del sistema.
     * @param exc es el error a tratar.
     */
    protected static void manejaError(Exception exc)
    {
        manejaError(exc,true);
    }
    /**
     * <code>manejaError</code> se utiliza para tratar errores, imprime el tipo
     * de error, lo vuelca en la pila de errores y sale del sistema en función
     * del parámetro salir.
     * @param exc es el error a tratar.
     * @param salir indica si sale del sistema.
     */
    protected static void manejaError(Exception exc,boolean salir)
    {
        out(exc.toString());
        exc.printStackTrace();
        if(salir)
        {
            System.exit(1001);
        }
    }
    /**
     * 
     * @param exc 
     * @param salir 
     */
    protected static void manejaError(SQLException exc,boolean salir)
    {
        int errorCode=1000;
        out("Error nº: "+exc.getErrorCode());
        out(exc.toString());
        exc.printStackTrace();
        while((exc=exc.getNextException())!=null)
        {
            out("Error nº: "+exc.getErrorCode());
            out(exc.toString());
            exc.printStackTrace();
            errorCode=exc.getErrorCode();
        }
        if(salir)
        {
            System.exit(errorCode);
        }
    }
    /**
     * <code>out</code> imprime una cadena de texto en pantalla 
     * @param cadena de texto a imprimir.
     */
    protected static void out(String cadena){
    // Wrap in a new writer stream to obtain 16 bit capability.
    System.out.println(cadena);
    /*
    OutputStreamWriter osw = new OutputStreamWriter (System.out);
    BufferedWriter bw=new BufferedWriter(osw);
    PrintWriter pw = new PrintWriter (bw, true);
    pw.println(cadena);
    pw.flush();
    pw.close();
        try {
            bw.close();
            osw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
     */
    }
}
