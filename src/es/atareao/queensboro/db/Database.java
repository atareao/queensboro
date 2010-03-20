package es.atareao.queensboro.db;

/*
 * ***********************Software description*********************************
 * Database.java
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



//

import java.sql.SQLException;

//********************************IMPORTACIONES*********************************
//
/**
 *
 * @author Lorenzo Carbonell
 */
public class Database {
    
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public Database(String databaseName,Conector conector){
        this.setDatabaseName(databaseName);
        this.setConector(conector);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    /**
     * Enters a new schema into the current database. The schema name must be distinct from
     * the name of any existing schema in the current database.
     * A schema is essentially a namespace: it contains named objects (tables, data types, functions, and operators)
     * whose names may duplicate those of other objects existing in other schemas. Named objects are
     * accessed either by “qualifying” their names with the schema name as a prefix, or by setting a search path
     * that includes the desired schema(s). A CREATE command specifying an unqualified object name creates
     * the object in the current schema (the one at the front of the search path, which can be determined with the
     * function current_schema).
     * Optionally, CREATE SCHEMA can include subcommands to create objects within the new schema. The
     * subcommands are treated essentially the same as separate commands issued after creating the schema,
     * except that if the AUTHORIZATION clause is used, all the created objects will be owned by that user.
     * @param schemaName The name of a schema to be created. If this is omitted, the user name is used as the schema name.
     * The name cannot begin with pg_, as such names are reserved for system schemas.
     * @param ownerName The name of the user who will own the schema. If omitted, defaults to the user executing the command.
     * Only superusers may create schemas owned by users other than themselves.
     * @return true if the new schema is created
     * @throws java.sql.SQLException raise an exception if an error ocurs
     */
    public boolean createSchema(String schemaName,String ownerName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("CREATE SCHEMA ");
        sb.append(schemaName);
        if(ownerName!=null){
            sb.append(" AUTHORIZATION ");
            sb.append(ownerName);
        }
        return this.getConector().execute(sb.toString());        
    }
    /**
     * Enters a new schema into the current database. The schema name must be distinct from
     * the name of any existing schema in the current database.
     * A schema is essentially a namespace: it contains named objects (tables, data types, functions, and operators)
     * whose names may duplicate those of other objects existing in other schemas. Named objects are
     * accessed either by “qualifying” their names with the schema name as a prefix, or by setting a search path
     * that includes the desired schema(s). A CREATE command specifying an unqualified object name creates
     * the object in the current schema (the one at the front of the search path, which can be determined with the
     * function current_schema).
     * Optionally, CREATE SCHEMA can include subcommands to create objects within the new schema. The
     * subcommands are treated essentially the same as separate commands issued after creating the schema,
     * except that if the AUTHORIZATION clause is used, all the created objects will be owned by that user.
     * @param schemaName The name of a schema to be created. If this is omitted, the user name is used as the schema name.
     * The name cannot begin with pg_, as such names are reserved for system schemas.
     * @return true if the new schema is created
     * @throws java.sql.SQLException raise an exception if an error ocurs
     */
    public boolean createSchema(String schemaName) throws SQLException{
        return this.createSchema(schemaName,null);
    }
    /**
     * Changes the definition of a schema.
     * You must own the schema to use ALTER SCHEMA. To rename a schema you must also have the CREATE
     * privilege for the database. To alter the owner, you must also be a direct or indirect member of the new
     * owning role, and you must have the CREATE privilege for the database. (Note that superusers have all
     * these privileges automatically.)
     * @param schemaName The name of an existing schema.
     * @param ownerName The new owner of the schema.
     * @return true if the owner of the schema is changed
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean changeSchemaOwner(String schemaName,String ownerName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER SCHEMA ");
        sb.append(schemaName);
        sb.append(" OWNER TO ");
        sb.append(ownerName);
        return this.getConector().execute(sb.toString());        
    }
    /**
     * Changes the definition of a schema.
     * You must own the schema to use ALTER SCHEMA. To rename a schema you must also have the CREATE
     * privilege for the database. To alter the owner, you must also be a direct or indirect member of the new
     * owning role, and you must have the CREATE privilege for the database. (Note that superusers have all
     * these privileges automatically.)
     * @param oldSchemaName The name of an existing schema.
     * @param newSchemaName The new name of the schema. The new name cannot begin with pg_, as such names are reserved for
     * system schemas.
     * @return true if the name of the schema was changed
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean changeSchemaName(String oldSchemaName,String newSchemaName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("ALTER SCHEMA ");
        sb.append(oldSchemaName);
        sb.append(" RENAME TO ");
        sb.append(newSchemaName);
        return this.getConector().execute(sb.toString());                
    }
    /**
     * removes schemas from the database.
     * A schema can only be dropped by its owner or a superuser. Note that the owner can drop the schema (and
     * thereby all contained objects) even if he does not own some of the objects within the schema.
     * Automatically drop objects (tables, functions, etc.) that are contained in the schema.
     * @param schemaName The name of a schema.
     * @return true if the schema was removed
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean dropSchema(String schemaName) throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("DROP SCHEMA IF EXISTS ");
        sb.append(schemaName);
        sb.append(" CASCADE ");
        return this.getConector().execute(sb.toString());                
        
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 
    private String _databaseName;
    private Conector _conector;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 
    public String getDatabaseName() {
        return _databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this._databaseName = databaseName;
    }

    public Conector getConector() {
        return _conector;
    }

    public void setConector(Conector conector) {
        this._conector = conector;
    }
    // </editor-fold> 
}
