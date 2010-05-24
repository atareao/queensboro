/*
 * ConectorH2.java
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
import java.util.Vector;
/**
 *
 * @author Protactino
 */
public class ConectorH2 implements Conector{
    //
    //********************************CONSTANTES********************************
    //
    //
    //********************************CONSTANTES********************************
    //
    //
    //Connection Modes
    //
    private static final boolean DEBUG=true;
    /**
     * Bases de datos tipo fichero
     */
    public static final int MODE_FILE=0;
    /**
     * Bases de datos en memoria
     */
    public static final int MODE_MEMORY=1;
    /**
     * Bases de datos conexión vía TCP/IP
     */
    public static final int MODE_TCP=2;
    /**
     * Bases de datos conexión tipo SSL
     */
    public static final int MODE_SSL=3;
    //
    //Cipher Modes
    //
    /**
     * Base de datos sin cifrado
     */
    public static final int CIPHER_NO=0;
    /**
     * Base de datos con cifrado tipo AES
     */
    public static final int CIPHER_AES=1;
    /**
     * Base de datos con cifrado tipo XTEA
     */
    public static final int CIPHER_XTEA=2;
    //
    //Locking Modes
    //
    /**
     * Abre la base de datos sin "file locking".
     */
    public static final int FILE_LOCK_NO=0;
    /**
     * Abre la base de datos con un archivo que la protege
     */
    public static final int FILE_LOCK_FILE=1;
    /**
     * Este metodo dsolo se debe utilizar cuando se accede desde el mismo ordenador
     */
    public static final int FILE_LOCK_SOCKET=2;
    //
    //Create
    //
    /**
     * Permite crear una nueva base de datos
     */
    public static final int CREATE_YES=0;
    /**
     * No premite crear una nueva base de datos
     */
    public static final int CREATE_NO=1;
    //
    // *********************************CAMPOS*********************************
    //
    /**
     * Nombre de la base de datos
     */
    private String _DatabaseName;
    /**
     * Nombre del servidor donde está ubicada la base de datos
     */
    private String _Server;
    /**
     * Nombre del puerto del servidor donde está ubicada la base de datos
     */
    private String _Port;
    /**
     * Nombre del usuario
     */
    private String _User;
    /**
     * Contraseña del usuario
     */
    private char[] _Password;
    /**
     * Tipo de base de datos FILE|MEMORY|TCP|SSL
     */
    private int _Mode;
    /**
     * Tipo de cifrado de la base de datos NO|AES|XTEA
     */
    private int _Cipher;
    /**
     * Tipo de seguridad para indicar al resto de procesos que la base de datos está en
     * uso NO|FILE|SOCKET
     */
    private int _Lock;
    /**
     * Indica si en caso de no existir la base de datos se crea nueva
     */
    private int _Create;
    /**
     * <code>conexion</code> es la conexión utilizada para la base de datos
     * en cuestión
     */
    private Connection conexion;
    /**
     * Define el driver que se utiliza para realizar las distintas conexiones
     */
    private String driver;
    /**
     * Define la cadena de conexión que se utiliza para realizar las conexiones
     */
    private String url;
    /**
     * Constructor por defecto de la clase ConectorH2
     */    
    //
    //******************************CONSTRUCTORES*******************************
    //
    /**
     * Crea una nueva instancia de ConectorH2
     */
    public ConectorH2(){
        this.setDriver("org.h2.Driver");
        this.setUrl("jdbc:h2:test");
        this.activateDriver();
    }
    public ConectorH2(String userName,char[] password,String databaseName){
        this(userName,password,databaseName,CIPHER_NO,FILE_LOCK_FILE,CREATE_NO);
    }
    public ConectorH2(String userName,char[] password,String databaseName,int create){
        this(userName,password,databaseName,CIPHER_NO,FILE_LOCK_FILE,create);
    }
    public ConectorH2(String userName,char[] password,String databaseName,int lock,int create){
        this(userName,password,databaseName,CIPHER_NO,lock,create);
    }
    public ConectorH2(String userName,char[] password,String databaseName,int cipher,int lock,int create){
        this(userName,password,databaseName,false,cipher,lock,create);
    }
    public ConectorH2(String userName,char[] password,String databaseName,boolean memory,int cipher,int lock,int create){
        this.setCipher(cipher);
        this.setCreate(create);
        this.setDatabaseName(databaseName);
        this.setLock(lock);
        if(memory){
            this.setMode(ConectorH2.MODE_MEMORY);
        }else{
            this.setMode(ConectorH2.MODE_FILE);
        }
        this.setPassword(password);
        this.setPort(null);
        this.setServer(null);
        this.setUser(userName);
        this.createUrl();
        this.setDriver("org.h2.Driver");
        this.activateDriver();
    }
    public ConectorH2(String userName,char[] password,String databaseName,String server,String port){
        this(userName,password,databaseName,server,port,false,CIPHER_NO,FILE_LOCK_FILE,CREATE_NO);
    }   
    public ConectorH2(String userName,char[] password,String databaseName,String server,String port,int create){
        this(userName,password,databaseName,server,port,false,CIPHER_NO,FILE_LOCK_FILE,create);
    }   
    public ConectorH2(String userName,char[] password,String databaseName,String server,String port,int lock,int create){
        this(userName,password,databaseName,server,port,false,CIPHER_NO,lock,create);
    }   
    public ConectorH2(String userName,char[] password,String databaseName,String server,String port,int cipher,int lock,int create){
        this(userName,password,databaseName,server,port,false,cipher,lock,create);
    }   
    public ConectorH2(String userName,char[] password,String databaseName,String server,String port,boolean ssl,int cipher,int lock,int create){
        this.setCipher(cipher);
        this.setCreate(create);
        this.setDatabaseName(databaseName);
        this.setLock(lock);
        if(ssl){
            this.setMode(ConectorH2.MODE_SSL);
        }else{
            this.setMode(ConectorH2.MODE_TCP);
        }
        this.setPassword(password);
        this.setPort(port);
        this.setServer(server);
        this.setUser(userName);
        this.createUrl();
        this.setDriver("org.h2.Driver");
        this.activateDriver();
    }   
    //
    //**************************METODOS DE ACCESO*******************************
    /**
     * <code>getDatabaseName</code>Permite conocer la el nombre de la base de datos
     * @return nombre de la base de datos
     */
    @Override
    public String getDatabaseName() {
        return _DatabaseName;
    }

    /**
     * <code>setDatabaseName</code>Permite establecer el nombre de la base de datos
     * @param DatabaseName Nombre de la base de datos
     */
    @Override
    public void setDatabaseName(String DatabaseName) {
        this._DatabaseName = DatabaseName;
    }

    /**
     * <CODE>getServer</CODE>devuelve el servidor donde está ubicada la base de datos
     * @return <CODE>Server</CODE>, servidor donde está ubicada la base de datos
     */
    @Override
    public String getServer() {
        return _Server;
    }

    /**
     * <CODE>setServer</CODE>establece el servidor donde está ubicada la base de datos
     * @param Server <CODE>Server</CODE>servidor donde está ubicada la base de datos
     */
    @Override
    public void setServer(String Server) {
        this._Server = Server;
    }
    /**
     * 
     * @return 
     */
    @Override
    public String getPort() {
        return _Port;
    }
    /**
     * <code>setPort</code> establece el puerto de conexión del servidor
     * @param Port es el puerto de conexión del servidor
     */
    @Override
    public void setPort(String Port) {
        this._Port = Port;
    }
    /**
     * <code>getUser</code> devuelve el puerto de conexión del servidor
     * @return Port, es el puerto de conexión del servidor
     */
    @Override
    public String getUser() {
        return _User;
    }
    /**
     * <code>setUser</code> establece el usuario que accede a la base de datos
     * @param User que es el usuario que accede a la base de datos, es
     * importante indicar el usuario ya que esto permite realizar un control
     * estricto de las acciones que puede realizar
     */
    @Override
    public void setUser(String User) {
        this._User = User.toLowerCase();
    }
    /**
     * <code>getPassword</code> devuelve la contraseña especificada para el
     * usuario definido con <code>setUser</code>
     * @return password, contraseña valida para el usuario.
     */
    @Override
    public char[] getPassword() {
        return _Password;
    }
    /**
     * <code>setPassword</code> especifica una contraseña válida para el
     * usuario definido con <code>setUser</code>
     * @param Password contraseña valida para el usuario.
     */
    @Override
    public void setPassword(char[] Password) {
        this._Password = Password;
    }
    /**
     * 
     * @return 
     */
    public int getMode() {
        return _Mode;
    }

    /**
     * 
     * @param Mode 
     */
    public void setMode(int Mode) {
        this._Mode = Mode;
    }

    /**
     * 
     * @return 
     */
    public int getCipher() {
        return _Cipher;
    }

    /**
     * 
     * @param Cipher 
     */
    public void setCipher(int Cipher) {
        this._Cipher = Cipher;
    }

    /**
     * 
     * @return 
     */
    public int getLock() {
        return _Lock;
    }

    /**
     * 
     * @param Lock 
     */
    public void setLock(int Lock) {
        this._Lock = Lock;
    }

    /**
     * 
     * @param Create 
     */
    public void setCreate(int Create) {
        this._Create = Create;
    }
    /**
     * 
     * @return 
     */
    public int getCreate() {
        return _Create;
    }

    /**
     * <code>getConexion</code> se utiliza para obtener la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @return conexion, conexion con la base de datos especificada. 
     */
    @Override
    public Connection getConexion() {
        return conexion;
    }
    /**
     * <code>setConexion</code> se utiliza para definir la conexión realizada
     * con la base de datos especificada en las propiedades.
     * @param conexion conexion con la base de datos especificada.
     */
    @Override
    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    /**
     * <code>setDriver</code> define el driver que se utiliza para realizar la 
     * conexión con la base de datos.
     * @param driver es el nombre del driver que se utilizar para definir la 
     * conexión con la base de datos.
     */
    @Override
    public void setDriver(String driver)
    {
        this.driver=driver;
    }
    /**
     * <code>getDriver</code> devuelve el nombre del driver que se utiliza para
     * realizar la conexión con la base de datos.
     * @return driver, es el nombre del driver utilizado para definir la
     * conexión a la base de datos.
     */
    @Override
    public String getDriver()
    {
        return this.driver;
    }    
    /**
     * <code>setUrl</code> define la cadena que se utiliza para realizar la
     * conexión con la base de datos
     * @param url cadena que se utiliza para realizar la conexión con la base de datos
     */
    @Override
    public void setUrl(String url)
    {
        this.url=url;
    }
    /**
     * <code>getUrl</code> devuelve la cadena que se utilizar para realizar la
     * conexión con la base de datos
     * @return url, cadena de conexión
     */
    @Override
    public String getUrl()
    {
        return this.url;
    }

    //
    //********************************METODOS***********************************
    //
    @Override
    public void activateDriver()
    {
        try
        {
            if(ConectorH2.DEBUG) {
                out("ACTIVANDO DRIVER");
            }
            Class.forName(this.getDriver());
            if(ConectorH2.DEBUG) {
                out("DRIVER ACTIVADO");
            }
            if(ConectorH2.DEBUG) {
                out("DRIVER UTILIZADO: ");
            }
            if(ConectorH2.DEBUG) {
                out(this.getDriver());
            }
        }
        catch(ClassNotFoundException e)
        {
            ConectorH2.manejaError(e);
        }
    }
    /**
     * <code>openConnection</code> realiza la conexión a la base de datos especificada
     * con las distintas propiedades indicadas. En caso de error no se conecta,
     * lanza un mensaje de error, pero no sale de la máquina virtual de java.
     * <b><i>Antes de realizar la conexión hay que definir todas las propiedades
     * de la base de datos que pretendemos abrir.</i></b> 
     */
    @Override
    public void openConnection() throws SQLException{
        if(ConectorH2.DEBUG) {
            out("ABRIENDO CONEXIÓN");
        }
        this.setConexion(DriverManager.getConnection(this.getUrl(),this.getUser(),new String(this.getPassword())));
        if(ConectorH2.DEBUG) {
            out("CONEXIÓN ABIERTA");
        }
    }
    /**
     * <code>closeConnection</code> se utiliza para cerrar la conexión con la base de
     * datos. En caso de error, lanza una excepción a la pila, pero no sale.
     *
     */
    @Override
    public void closeConnection() throws SQLException{
        if(ConectorH2.DEBUG) {
            out("SHUTDOWN");
        }
        this.shutDown();
        if(ConectorH2.DEBUG) {
            out("CERRANDO CONEXIÓN");
        }
        this.getConexion().close();
        if(ConectorH2.DEBUG) {
            out("CONEXIÓN CERRADA");
        }
    }
    public void shutDown() throws SQLException
    {
        this.getConexion().createStatement().execute("SHUTDOWN");
    } 
    /**
     * Ejecuta una sentencia sql
     * @param sql es la sentencia que se pasa para ejecutar
     * @return true si el resutltado es correcto
     * @throws java.sql.SQLException si falla la ejecución
     */
    @Override
    public boolean execute(String sql) throws SQLException
    {
        boolean resultado=false;
        if(ConectorH2.DEBUG){
            ConectorH2.out("EJECUTANDO:");
            ConectorH2.out(sql);
        }
        this.getConexion().createStatement().execute(sql);
        resultado=true;
        if(ConectorH2.DEBUG) {
            ConectorH2.out("RESULTADO: " + resultado);
        }
        return resultado;
    }
    /**
     * Ejecuta una sentencia sql devolviendo un Resultet
     * @param sql sentencia que se ejecuta
     * @return Resultset con los resultados obtenidos de la sentencia
     * @throws java.sql.SQLException si falla la ejecución
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException
    {
        boolean resultado=false;
        ResultSet rset=null;
        
        if(ConectorH2.DEBUG){
            ConectorH2.out("EJECUTANDO:");
            ConectorH2.out(sql);
        }
        rset=this.getConexion().createStatement().executeQuery(sql);
        if(rset.getWarnings()==null){
            resultado=true;
        }
        if(ConectorH2.DEBUG) {
            ConectorH2.out("RESULTADO: " + resultado);
        }
        return rset;
    }
    @Override
    public long executeQueryCount(QueryCount queryCount) throws SQLException{
        ResultSet rs=this.executeQuery(queryCount.toSql());
        rs.next();
        return rs.getLong(1);        
    }
    @Override
    public boolean executeQueyDelete(QueryDelete queryDelete) throws SQLException{
        if(this.executeUpdate(queryDelete.toSql())>0){
            return true;
        }
        return false;
    }
    @Override
    public boolean executeQueryInsert(QueryInsert queryInsert) throws SQLException{
        if(this.executeUpdate(queryInsert.toSql())>0){        
            return true;
        }
        return false;
    }
    @Override
    public boolean executeQueryUpdate(QueryUpdate queryUpdate) throws SQLException{
        if(this.executeUpdate(queryUpdate.toSql())>0){        
            return true;
        }
        return false;
        
    }
    @Override
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
    @Override
    public int executeUpdate(String sql) throws SQLException{
        boolean resultado=false;
        if(ConectorH2.DEBUG){
            ConectorH2.out("EJECUTANDO:");
            ConectorH2.out(sql);
        }
        int filas=this.getConexion().createStatement().executeUpdate(sql);
        if(filas>-1){
            resultado=true;
        }
        if(ConectorH2.DEBUG) {
            ConectorH2.out("RESULTADO: " + resultado);
        }

        return filas;
    }    
    
    /**
     * Creates a SQL script with or without the insert statements.
     * This command can be used to create a backup of the database.
     * For long term storage, it is more portable than file based backup.
     * This command locks objects while it is running.
     * @param fileName
     * @return
     * @throws java.sql.SQLException
     */
    public boolean saveScript(String fileName) throws SQLException{
        if(fileName!=null){
            StringBuffer sb=new StringBuffer();
            sb.append("SCRIPT TO '");
            sb.append(fileName);
            sb.append("' COMPRESSION LZF CIPHER AES PASSWORD '9m3no5NU6ve0';");
            return this.execute(sb.toString());
        }
        return false;
    }
    /**
     * Runs a SQL script from a file. The script is a text file containing SQL 
     * statements; each statement must end with ';'.This command can be used to 
     * restore a database from a backup.The compression algorithm must match to 
     * the one used when creating the script. Instead of a file, an URL may be 
     * used.Admin rights are required to execute this command. 
     * @param fileName
     * @return
     * @throws java.sql.SQLException
     */
    public boolean loadScript(String fileName) throws SQLException{
        if(fileName!=null){
            StringBuffer sb=new StringBuffer();
            sb.append("RUNSCRIPT FROM '");
            sb.append(fileName);
            sb.append("' COMPRESSION LZF CIPHER AES PASSWORD '9m3no5NU6ve0';");
            return this.execute(sb.toString());
        }
        return false;
    }
    //
    //**************************METODOS AUXILIARES******************************
    //
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
    private void createUrl(){
        StringBuffer sb=new StringBuffer("jdbc:h2:");
        switch (this.getMode()){
            case MODE_FILE:
                sb.append("file:");
                break;
            case MODE_MEMORY:
                sb.append("mem:");
                break;
            case MODE_SSL:
                sb.append("ssl://");
                sb.append(this.getServer());
                sb.append(":");
                sb.append(this.getPort());
                sb.append("/");
                break;
            case MODE_TCP:
                sb.append("tcp://");
                sb.append(this.getServer());
                sb.append(":");
                sb.append(this.getPort());
                sb.append("/");
                break;
        }
        sb.append(this.getDatabaseName());        
        switch(this.getCipher()){
            case CIPHER_NO:
                break;
            case CIPHER_AES:
                sb.append(";CIPHER=AES");
                break;
            case CIPHER_XTEA:
                sb.append(";CIPHER=XTEA");
                break;
        }
        switch(this.getLock()){
            case FILE_LOCK_NO:
                sb.append(";FILE_LOCK=NO");
                break;
            case FILE_LOCK_FILE:
                sb.append(";FILE_LOCK=FILE");
                break;
            case FILE_LOCK_SOCKET:
                sb.append(";FILE_LOCK=SOCKET");
                break;
        }
        switch(this.getCreate()){
            case CREATE_NO:
                sb.append(";IFEXISTS=TRUE");
                break;
        }
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
