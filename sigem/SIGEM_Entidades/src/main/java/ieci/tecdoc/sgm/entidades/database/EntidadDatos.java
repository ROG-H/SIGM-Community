/**
 * @author Marco Fiore
 * 
 * Fecha de Creaci�n: 10 - 01 - 2008
 */
package ieci.tecdoc.sgm.entidades.database;

import ieci.tecdoc.sgm.base.dbex.DbConnection;
import ieci.tecdoc.sgm.base.dbex.DbDeleteFns;
import ieci.tecdoc.sgm.base.dbex.DbInputStatement;
import ieci.tecdoc.sgm.base.dbex.DbOutputStatement;
import ieci.tecdoc.sgm.base.dbex.DynamicFns;
import ieci.tecdoc.sgm.base.dbex.DynamicRow;
import ieci.tecdoc.sgm.base.dbex.DynamicRows;
import ieci.tecdoc.sgm.base.dbex.DynamicTable;
import ieci.tecdoc.sgm.core.db.DataSourceManager;
import ieci.tecdoc.sgm.entidades.beans.Entidad;
import ieci.tecdoc.sgm.entidades.exception.EntidadException;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * Modela los datos asociados a un entidad de SIGEM.
 *  
 */
public class EntidadDatos extends Entidad implements Serializable {
   private static final Logger logger = Logger.getLogger(EntidadDatos.class);
   
   public EntidadDatos(){
	   super();
   }
   
   public EntidadDatos(Entidad entidad){
	   setIdentificador(entidad.getIdentificador());
	   setNombreCorto(entidad.getNombreCorto());
	   setNombreLargo(entidad.getNombreLargo());
	   setCodigo_ine(entidad.getCodigo_ine());
	   setPassword_entidad(entidad.getPassword_entidad());
	   setCif(entidad.getCif());
	   setDir3(entidad.getDir3());
	   setSia(entidad.getSia());
	   setDeh(entidad.getDeh());
   }
   /**
    * Recupera todos los valores de los par�metros de la sentencia
    * de consulta que se pasa como par�metro.
    *
    * @param statement Sentencia sql precompilada.
    * @param idx Indice de posici�n del primer par�metro que se recoge
    * de la consulta.
    * @return Indice de posici�n del �ltimo par�metro recogido
    * @throws DbExcepcion Si se produce alg�n error.
    */
   public Integer loadAllValues(DbOutputStatement statement, Integer idx)
   throws Exception {

      int index = idx.intValue();

      setIdentificador(statement.getShortText(index ++));
      setNombreCorto(statement.getShortText(index ++));
      setNombreLargo(statement.getShortText(index ++));
      setCodigo_ine(statement.getShortText(index++));
      setPassword_entidad(statement.getShortText(index++));
      setCif(statement.getShortText(index++));
      setDir3(statement.getShortText(index++));
	  setSia(statement.getShortText(index++));
	  setDeh(statement.getShortText(index++));
      return new Integer(index);
   }

   public Integer loadIdValues(DbOutputStatement statement, Integer idx)
   	throws Exception {

      int index = idx.intValue();

      setIdentificador(statement.getShortText(index ++));
      
      return new Integer(index);
   }
   
   /**
    * Genera la sentencia de inserci�n de un mime type.
    *
    * @param statement Sentencia sql precompilada.
    * @param idx Indice de posici�n del primer par�metro que se recoge
    * de la consulta.
    * @return Indice de posici�n del �ltimo par�mtro recogido
    * @throws Exception Si se produce alg�n error.
    */
   public Integer insert(DbInputStatement statement, Integer idx)
   throws Exception {

     int index = idx.intValue();

     statement.setShortText(index++, getIdentificador());
     statement.setShortText(index ++, getNombreCorto());
     statement.setShortText(index++, getNombreLargo());
     statement.setShortText(index++, getCodigo_ine());
     statement.setShortText(index++, getPassword_entidad());
     statement.setShortText(index++, getCif());
     statement.setShortText(index++, getDir3());
     statement.setShortText(index++, getSia());
     statement.setShortText(index ++, getDeh());     
     
     return new Integer(index);
   }
   
   
	  public Integer update(DbInputStatement statement, Integer idx)
	  throws Exception {

	    int index = idx.intValue();
	    statement.setShortText(index++, getNombreCorto());
	    statement.setShortText(index++, getNombreLargo());
	    statement.setShortText(index++, getCodigo_ine());
	    statement.setShortText(index++, getPassword_entidad());
	    statement.setShortText(index++, getCif());
	    statement.setShortText(index++, getDir3());
	    statement.setShortText(index++, getSia());
	    statement.setShortText(index ++, getDeh());	   
	    
	     return new Integer(index);
	  }
   
   
   /**
    * Realiza la consulta por Entidad / contrase�a.
    *
    * @param extension Extension.
    * @throws DbExcepcion Si se produce alg�n error.
    */
   public void load(String identificador) throws Exception {
      DynamicTable tableInfo = new DynamicTable();
      DynamicRows rowsInfo = new DynamicRows();
      DynamicRow rowInfo = new DynamicRow();
      EntidadTabla table = new EntidadTabla();
      DbConnection dbConn = new DbConnection();
      
      if(logger.isDebugEnabled()){
    	  logger.debug("Obteniendo datos de entidad...");
      }
      
      try {
         dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
         
         tableInfo.setTableObject(table);
         tableInfo.setClassName(EntidadTabla.class.getName());
         tableInfo.setTablesMethod("getTableName");
         tableInfo.setColumnsMethod("getAllColumnNames");
         
         rowInfo.addRow(this);
         rowInfo.setClassName(EntidadDatos.class.getName());
         rowInfo.setValuesMethod("loadAllValues");
         rowsInfo.add(rowInfo);
         
         if (!DynamicFns.select(dbConn, table.getById(identificador), tableInfo, rowsInfo)) {
            throw new EntidadException(EntidadException.EC_NOT_FOUND);
         }
         if(logger.isDebugEnabled()){
        	 logger.debug("Datos de entidad obtenidos.");
         }
      } catch (Exception e) {
        throw new EntidadException(EntidadException.EC_GENERIC_ERROR, e.getCause());
      } finally {
        try{
          if (dbConn.existConnection()){
              dbConn.close();        	  
              if(logger.isDebugEnabled()){
            	  logger.debug("Sesi�n cerrada.");
              }
          }
        }catch(Exception e){ 
        	logger.error("Error cerrando sesi�n.", e);
        	throw new EntidadException(EntidadException.EC_CLOSE_CONNECTION, e.getCause());
        }
      }
   }
   

   /**
    * A�ade un usuario.
    *
    * @throws Exception Si se produce alg�n error.
    */
   public void add() throws EntidadException {
      DynamicTable tableInfo = new DynamicTable();
      DynamicRows rowsInfo = new DynamicRows();
      DynamicRow rowInfo = new DynamicRow();
      EntidadTabla table = new EntidadTabla();
      DbConnection dbConn = new DbConnection();
      
      logger.debug("A�adiendo entidad...");
      
      try {
    	  dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
         
         tableInfo.setTableObject(table);
         tableInfo.setClassName(EntidadTabla.class.getName());
         tableInfo.setTablesMethod("getTableName");
         tableInfo.setColumnsMethod("getAllColumnNames");
         
         rowInfo.addRow(this);
         rowInfo.setClassName(EntidadDatos.class.getName());
         rowInfo.setValuesMethod("insert");
         rowsInfo.add(rowInfo);
         
         DynamicFns.insert(dbConn, tableInfo, rowsInfo);
         logger.debug("Entidad a�adida.");         
      } catch (Exception e) {
    	  logger.error("Error a�adiendo entidad.", e);
         throw new EntidadException(EntidadException.EC_INSERT, e.getCause());
      } finally {
         try{
          if (dbConn.existConnection()){
              dbConn.close();
        	  if(logger.isDebugEnabled()){
            	  logger.debug("Sesi�n cerrada.");
              }
          }
        }catch(Exception e){
        	logger.error("Error cerrando sesi�n.", e);        	
        	throw new EntidadException(EntidadException.EC_CLOSE_CONNECTION, e.getCause());
        }
      }
   }
   

   /**
    * Borra todos las extensiones asociadas a un mime type.
    *
    * @param mimeType Mime type.
    * @throws Exception Si se produce alg�n error.
    */
   public void delete() throws EntidadException {
	   EntidadTabla table = new EntidadTabla();
	   DbConnection dbConn = new DbConnection();
	   if(logger.isDebugEnabled()){
		   logger.debug("Eliminando entidad...");		   
	   }
	   try {
		   dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
		   DbDeleteFns.delete(dbConn, table.getTableName(), table.getById(getIdentificador()));
		   if(logger.isDebugEnabled()){
			   logger.debug("Entidad eliminada.");		   
		   }		   
	   } catch (Exception e) {
		   throw new EntidadException(EntidadException.EC_DELETE);
	   } finally {
         try{
          if (dbConn.existConnection()){
              dbConn.close();
        	  if(logger.isDebugEnabled()){
            	  logger.debug("Sesi�n cerrada.");
              }              
          }
        }catch(Exception e){
        	logger.error("Error cerrando sesi�n.", e);        	
        	throw new EntidadException(EntidadException.EC_CLOSE_CONNECTION, e.getCause());
        }
        
      }
   }
   
	  public void update() throws EntidadException {
		     DynamicTable tableInfo = new DynamicTable();
		     DynamicRows rowsInfo = new DynamicRows();
		     DynamicRow rowInfo = new DynamicRow();
		     EntidadTabla table = new EntidadTabla();
		     DbConnection dbConn = new DbConnection();
		     
		     try {
		    	 dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
		        tableInfo.setTableObject(table);
		        tableInfo.setClassName(EntidadTabla.class.getName());
		        tableInfo.setTablesMethod("getTableName");
		        tableInfo.setColumnsMethod("getUpdateColumnNames");
		        
		        rowInfo.addRow(this);
		        rowInfo.setClassName(EntidadDatos.class.getName());
		        rowInfo.setValuesMethod("update");
		        rowsInfo.add(rowInfo);
		        
		        DynamicFns.update(dbConn, table.getById(getIdentificador()), tableInfo, rowsInfo);
		     } catch (Exception e) {
		        throw new EntidadException(EntidadException.EC_UPDATE);
		     } finally {
		       try{
		         if (dbConn.existConnection()){
			           dbConn.close();		        	 
		        	  if(logger.isDebugEnabled()){
		            	  logger.debug("Sesi�n cerrada.");
		              }
		         }

		       }catch(Exception ee){
		          logger.error("Error cerrando sesi�n.", ee);		    	   
		    	  throw new EntidadException(EntidadException.EC_CLOSE_CONNECTION, ee);
		       }
		     }
		  }	  
   
	  
	  
	  public void obtenerIdentificadorEntidad() throws EntidadException{
		  DynamicTable tableInfo = new DynamicTable();
		  DynamicRows rowsInfo = new DynamicRows();
		  DynamicRow rowInfo = new DynamicRow();
		  EntidadTabla table = new EntidadTabla();
		  DbConnection dbConn =  new DbConnection();
		  
	      if(logger.isDebugEnabled()){
	    	  logger.debug("Obteniendo identificador entidad...");
	      }
		  
		  try {
			  dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
			  tableInfo.setTableObject(table);
			  tableInfo.setClassName(EntidadTabla.class.getName());
			  tableInfo.setTablesMethod("getTableName");
			  tableInfo.setColumnsMethod("getMaxIdEntidad");
			  rowInfo.setClassName(EntidadDatos.class.getName());
		      rowInfo.setValuesMethod("loadIdValues");
		      rowsInfo.add(rowInfo);
		      DynamicFns.select(dbConn, null, tableInfo, rowsInfo);
		      
		      if(logger.isDebugEnabled()){
		    	  logger.debug("Obtencion de identificador entidad.");
		      }		      
		  }catch(Exception e){
			  logger.error("Error obteniendo identificador de entidad.", e);
			  throw new EntidadException(EntidadException.EC_GENERIC_ERROR, e);
		  } finally {
		       try{
		    		   if (dbConn.existConnection()){
		    			   dbConn.close();
		    	           if(logger.isDebugEnabled()){
		    	        	   logger.debug("Sesi�n cerrada.");
		    	           }
		    		   }
		    			   
			   }catch(Exception ee){
				   throw new EntidadException(EntidadException.EC_CLOSE_CONNECTION, ee);
			   }
		  }
	  }	  
}
