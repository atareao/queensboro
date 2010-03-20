/*
 * ***********************Software description*********************************
 * FileDb.java
 * 
 * 
 * ***********************Software description*********************************
 * 
 * Copyright (C) 2008 - Lorenzo Carbonell
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

package es.atareao.queensboro.db;

//
//********************************IMPORTACIONES*********************************
//
import java.io.File;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import es.atareao.alejandria.gui.ErrorDialog;
import es.atareao.alejandria.lib.FileUtils;
import es.atareao.alejandria.lib.GeneradorUUID;
/**
 *
 * @author Lorenzo Carbonell
 */
public class FileDb {
    // <editor-fold defaultstate="collapsed" desc=" Constantes  "> 

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Constructores  "> 
    public FileDb(String user,char[] password,File file,String sql){
        this.setUser(user);
        this.setPassword(password);
        this.setFile(file);
        this.setSql(sql);
        this.setModificado(false);
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos  "> 
    public void create(){
        if(this.getFile()!=null){
            this.createFile(this.getFile());
        }
    }
    public void open(){
        if(this.getFile()!=null){
            this.openFile(this.getFile());
        }
    }
    public void close(){
        if(this.getFile()!=null){
            this.closeFile();
        }
        
    }
    public void save(){
        if(this.getFile()!=null){
            this.saveFile(this.getFile());
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos auxiliares  "> 
    private void createFile(File file){
        if(file.exists()){
            int ans=JOptionPane.showConfirmDialog(new JFrame(), "Ya existe una archivo con ese nombre, ¿Desea sobreescribirlo?", "Atención",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(JOptionPane.YES_OPTION==ans){
                if(!file.delete()){
                    return;
                }
            }else{
                return;
            }
        }
        try {
            this.setModificado(false);
            String sql=this.getSql();
            File parentdir=this.getTemporalDir();
            String db=this.getTemporalDb().getAbsolutePath();
            if(parentdir.exists()){
                FileUtils.deleteDirectory(parentdir);
            }
            this.setConector(new ConectorH2(this.getUser(), this.getPassword(), db,ConectorH2.CREATE_YES));
            this.getConector().openConnection();
            this.getConector().execute(sql);
            ((ConectorH2)this.getConector()).saveScript(file.getAbsolutePath());
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
    }

    private void openFile(File file){
        try {
            this.setModificado(false);
            File parentdir=this.getTemporalDir();
            String db=this.getTemporalDb().getAbsolutePath();
            if(parentdir.exists()){
                FileUtils.deleteDirectory(parentdir);
            }
            this.setConector(new ConectorH2(this.getUser(), this.getPassword(), db,ConectorH2.CREATE_YES));
            this.getConector().openConnection();
            ((ConectorH2)this.getConector()).loadScript(file.getAbsolutePath());
        } catch (SQLException ex) {
            ErrorDialog.manejaError(ex);
        }
    }
    public void closeFile(){
        if(this.isModificado()){
            if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(new JFrame(),"El archivo ha sido modificado, ¿Quiere guardarlo?", "Atención",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)){
                this.saveFile(this.getFile());
            }
        }
        if(this.getConector()!=null){
            try {
                this.getConector().closeConnection();
                if(this.getFile()!=null){
                    File tmpdir=this.getTemporalDir();
                    if(tmpdir.exists()){
                        FileUtils.deleteDirectory(tmpdir);
                    }
                }
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex,true);
            }
        }
        this.setFile(null);
        this.setConector(null);
        this.setModificado(false);
    }
    public void saveFile(File file){
        if(this.getConector()!=null){
            if(file.exists()){
                file.delete();
            }
            try {
                ((ConectorH2)this.getConector()).saveScript(file.getAbsolutePath());
                this.setModificado(false);
            } catch (SQLException ex) {
                ErrorDialog.manejaError(ex);
            }
        }
    }
    private File createTemporalDir(File file){
        return FileUtils.addPathFile(file.getParent(), "temporal_"+GeneradorUUID.crearUUID());
    }
    private File createTemporalDb(File file){
        return FileUtils.addPathFile(this.getTemporalDir().getAbsolutePath(),FileUtils.getFileNameWithoutExtension(file.getName()));
    }

    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Campos  "> 
    private Conector conector;
    private boolean _modificado;
    private File _file;
    private File _temporalDir;
    private File _temporalDb;
    private String _sql;
    private String _user;
    private char[] _password;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc=" Métodos de acceso  "> 

    public Conector getConector() {
        return conector;
    }

    public void setConector(Conector conector) {
        this.conector = conector;
    }

    public File getFile() {
        return _file;
    }

    public void setFile(File file) {
        this._file = file;
        if(file!=null){
            this.setTemporalDir(this.createTemporalDir(file));
            this.setTemporalDb(this.createTemporalDb(file));
        }else{
            this.setTemporalDb(null);
            this.setTemporalDir(null);
        }
    }

    public File getTemporalDir() {
        return _temporalDir;
    }

    public void setTemporalDir(File temporalDir) {
        this._temporalDir = temporalDir;
    }

    public File getTemporalDb() {
        return _temporalDb;
    }

    public void setTemporalDb(File temporalDb) {
        this._temporalDb = temporalDb;
    }

    public boolean isModificado() {
        return _modificado;
    }

    public void setModificado(boolean modificado) {
        this._modificado = modificado;
    }

    public String getSql() {
        return _sql;
    }

    public void setSql(String sql) {
        this._sql = sql;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        this._user = user;
    }

    public char[] getPassword() {
        return _password;
    }

    public void setPassword(char[] password) {
        this._password = password;
    }
    // </editor-fold> 
}
