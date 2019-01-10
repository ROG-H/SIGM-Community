/**
 * @author Jos� Antonio Nogales Rinc�n
 *
 * Fecha de Creaci�n: 14-mar-2007
 */
package ieci.tecdoc.sgm.entidades.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ieci.tecdoc.sgm.base.dbex.DbConnection;
import ieci.tecdoc.sgm.base.dbex.DbUtil;
import ieci.tecdoc.sgm.base.dbex.DynamicFns;
import ieci.tecdoc.sgm.base.dbex.DynamicRow;
import ieci.tecdoc.sgm.base.dbex.DynamicRows;
import ieci.tecdoc.sgm.base.dbex.DynamicTable;
import ieci.tecdoc.sgm.core.db.DataSourceManager;
import ieci.tecdoc.sgm.entidades.beans.CriterioBusquedaEntidades;
import ieci.tecdoc.sgm.entidades.exception.EntidadException;

/**
 * Gestiona el acceso (inserciones, modificaciones, etc.) a la tabla de
 * asociaciones extensi�n-tipo mime.
 *
 */
public class EntidadTabla {

   private static final Logger logger = Logger.getLogger(EntidadTabla.class);

   private static final String SEPARATOR = ", ";
   private static final String TABLE_NAME = "sgm_adm_entidades";
   private static final String CN_ID = "id";
   private static final String CN_NOMBRECORTO = "nombreCorto";
   private static final String CN_NOMBRELARGO = "nombreLargo";
   private static final String CN_CODIGOINE = "codigo_ine";
   private static final String CN_PASSWORDENTIDAD = "password_entidad";
   private static final String CN_CIF = "cif";
   private static final String CN_DIR3 = "dir3";
   private static final String CN_SIA = "sia";
   private static final String CN_DEH = "deh";
   private static final String ALL_COLUMN_NAMES = CN_ID + SEPARATOR + CN_NOMBRECORTO + SEPARATOR + CN_NOMBRELARGO + SEPARATOR + CN_CODIGOINE + SEPARATOR + CN_PASSWORDENTIDAD + SEPARATOR + CN_CIF + SEPARATOR + CN_DIR3 + SEPARATOR + CN_SIA + SEPARATOR + CN_DEH;

   private static final String MAX_ID_ENTIDAD = "max (" + CN_ID + ")";


   /**
    * Constructor de la clase MimeTypeTable
    */
   public EntidadTabla() {
   }


   /**
    * Devuelve los nombres completos de todas las columnas mapeadas
    * actualizables, separados por comas.
    * @return El nombre de las columnas separados por comas.
    */
   public String getUpdateColumnNames() {

	  StringBuffer sbAux = new StringBuffer(CN_NOMBRECORTO).append(SEPARATOR).append(CN_NOMBRELARGO).append(SEPARATOR).append(CN_CODIGOINE).append(SEPARATOR).append(CN_PASSWORDENTIDAD).append(SEPARATOR).append(CN_CIF).append(SEPARATOR).append(CN_DIR3).append(SEPARATOR).append(CN_SIA).append(SEPARATOR).append(CN_DEH);

	  return sbAux.toString();
   }

   /**
    * Devuelve el nombre de la tabla
    * @return String Nombre de la tabla
    */
   public String getTableName() {
      return TABLE_NAME;
   }

   /**
    * Devuelve los nombres de las columnas
    * @return String Nombres de las columnas
    */
   public String getAllColumnNames() {
      return ALL_COLUMN_NAMES;
   }

   /**
    * Devuelve el nombre de la columna id
    * @return String Nombre de la columna id
    */
   public String getIdColumnName(){
     return CN_ID;
   }

   /**
    * Devuelve el nombre de la columna usuario
    * @return String Nombre de la columna usuario
    */
   public String getNombreCorteColumnName(){
     return CN_NOMBRECORTO;
   }

   /**
    * Devuelve el nombre de la columna contrase�a
    * @return String Nombre de la columna contrase�a
    */
   public String getNombreLargoColumnName(){
     return CN_NOMBRELARGO;
   }

   /**
    * Devuelve el nombre de la columna c�digo INE
    * @return String Nombre de la columna c�digo INE
    */
   public String getCodigoINEColumnName(){
     return CN_CODIGOINE;
   }
   
   /**
    * Devuelve el nombre de la columna para obtener el identificador
    * @return String Nombre de la columna para max identificador
    */
   public String getMaxIdEntidad(){
     return MAX_ID_ENTIDAD;
   }
   
   
   /**
    * Devuelve el nombre de la columna c�digo id
    * @return
    */
   public static String getCnId() {
	return CN_ID;
   }

   /**
    *  Devuelve el nombre de la columna c�digo Nombrecorto
    * @return
    */
	public static String getCnNombrecorto() {
		return CN_NOMBRECORTO;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Nombrelargo
	 * @return
	 */
	public static String getCnNombrelargo() {
		return CN_NOMBRELARGO;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Codigoine
	 * @return
	 */
	public static String getCnCodigoine() {
		return CN_CODIGOINE;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Passwordentidad
	 * @return
	 */
	public static String getCnPasswordentidad() {
		return CN_PASSWORDENTIDAD;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Cif
	 * @return
	 */
	public static String getCnCif() {
		return CN_CIF;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Dir3
	 * @return
	 */
	public static String getCnDir3() {
		return CN_DIR3;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Sia
	 * @return
	 */
	public static String getCnSia() {
		return CN_SIA;
	}
	
	/**
	 *  Devuelve el nombre de la columna c�digo Deh
	 * @return
	 */
	public static String getCnDeh() {
		return CN_DEH;
	}


/**
    * Devuelve la clausula de consulta por id
    * @param id Valor del campo id
    * @return String Clausula de consulta por id
    */
   public String getById(String id) {
	   StringBuffer sbAux = new StringBuffer("WHERE ").append(CN_ID).append(" = '").append(DbUtil.replaceQuotes(id)).append("'");
	   return sbAux.toString();
   }

   public String orderByNombre() {
	   StringBuffer sbAux = new StringBuffer("ORDER BY ").append(CN_NOMBRELARGO);
	   return sbAux.toString();
   }

   public String getFindQual(CriterioBusquedaEntidades poCriterio){
	   StringBuffer sbQual = null;
	   if(poCriterio != null){
		   // nombre corto
		   if(	(poCriterio.getNombreCorto() != null)
					&&(!"".equals(poCriterio.getNombreCorto()))
			   ){
				   if(sbQual == null){
					   sbQual = new StringBuffer(" WHERE ");
				   }
				   sbQual.append("UPPER (" + getNombreCorteColumnName() + ")")
				   			.append(" LIKE '%")
				   			.append(DbUtil.replaceQuotes(poCriterio.getNombreCorto()))
				   			.append("%' ");
			   }
		   // nombre corto
		   if(	(poCriterio.getNombreLargo() != null)
					&&(!"".equals(poCriterio.getNombreLargo()))
			   ){
				   if(sbQual == null){
					   sbQual = new StringBuffer(" WHERE ");
				   }else{
					   sbQual.append(" OR ");
				   }
				   sbQual.append("UPPER (" + getNombreLargoColumnName() + ")")
				   			.append(" LIKE '%")
				   			.append(DbUtil.replaceQuotes(poCriterio.getNombreLargo()))
				   			.append("%' ");
			   }
		   if(sbQual == null){
			   return "";
		   }else{
			   return sbQual.toString();
		   }
	   }else{
		   return "";
	   }
   }

	  public List buscarEntidades(CriterioBusquedaEntidades poCriterio) throws EntidadException{
		  DynamicTable tableInfo = new DynamicTable();
		  DynamicRows rowsInfo = new DynamicRows();
		  DynamicRow rowInfo = new DynamicRow();
		  EntidadTabla table = new EntidadTabla();
		  EntidadDatos entidad;
		  List list = new ArrayList();
		  int size;
		  DbConnection dbConn =  new DbConnection();

	      if(logger.isDebugEnabled()){
	    	  logger.debug("Buscando entidades...");
	      }

		  try {
			  dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
			  tableInfo.setTableObject(table);
			  tableInfo.setClassName(EntidadTabla.class.getName());
			  tableInfo.setTablesMethod("getTableName");
			  tableInfo.setColumnsMethod("getAllColumnNames");
			  rowInfo.setClassName(EntidadDatos.class.getName());
		      rowInfo.setValuesMethod("loadAllValues");
		      rowsInfo.add(rowInfo);
		      DynamicFns.selectMultiple(dbConn, getFindQual(poCriterio), false, tableInfo, rowsInfo);
		      size = rowInfo.getRowCount();
		      for (int i = 0; i < size; i++) {
		    	  entidad = (EntidadDatos) rowInfo.getRow(i);
		    	  list.add(entidad);
		      }
		      if(logger.isDebugEnabled()){
		    	  logger.debug("Busqueda de entidades finalizada.");
		      }
		  }catch(Exception e){
			  logger.error("Error buscando entidades.", e);
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
		  return list;
	  }


	  public List obtenerEntidades() throws EntidadException{
		  DynamicTable tableInfo = new DynamicTable();
		  DynamicRows rowsInfo = new DynamicRows();
		  DynamicRow rowInfo = new DynamicRow();
		  EntidadTabla table = new EntidadTabla();
		  EntidadDatos entidad;
		  List list = new ArrayList();
		  int size;
		  DbConnection dbConn =  new DbConnection();

	      if(logger.isDebugEnabled()){
	    	  logger.debug("Obteniendo entidades...");
	      }

		  try {
			  dbConn.open(DBSessionManager.getSession(DataSourceManager.ADMINISTRACION_DATASOURCE_NAME));
			  tableInfo.setTableObject(table);
			  tableInfo.setClassName(EntidadTabla.class.getName());
			  tableInfo.setTablesMethod("getTableName");
			  tableInfo.setColumnsMethod("getAllColumnNames");
			  rowInfo.setClassName(EntidadDatos.class.getName());
		      rowInfo.setValuesMethod("loadAllValues");
		      rowsInfo.add(rowInfo);
		      DynamicFns.selectMultiple(dbConn, orderByNombre(), false, tableInfo, rowsInfo);
		      size = rowInfo.getRowCount();
		      for (int i = 0; i < size; i++) {
		    	  entidad = (EntidadDatos) rowInfo.getRow(i);
		    	  list.add(entidad);
		      }
		      if(logger.isDebugEnabled()){
		    	  logger.debug("Obtencion de entidades finalizada.");
		      }
		  }catch(Exception e){
			  logger.error("Error obteniendo entidades.", e);
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
		  return list;
	  }

}
