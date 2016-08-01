/**
 * @author Javier Septi�n Arceredillo
 * 
 * Fecha de Creaci�n: 11-may-2007
 */
package ieci.tecdoc.sgm.ct.database;

import ieci.tecdoc.sgm.base.dbex.DbConnection;
import ieci.tecdoc.sgm.base.dbex.DbDeleteFns;
import ieci.tecdoc.sgm.base.dbex.DbInputStatement;
import ieci.tecdoc.sgm.base.dbex.DbOutputStatement;
import ieci.tecdoc.sgm.base.dbex.DynamicFns;
import ieci.tecdoc.sgm.base.dbex.DynamicRow;
import ieci.tecdoc.sgm.base.dbex.DynamicRows;
import ieci.tecdoc.sgm.base.dbex.DynamicTable;
import ieci.tecdoc.sgm.ct.database.datatypes.Expediente;
import ieci.tecdoc.sgm.ct.database.datatypes.ExpedienteImpl;
import ieci.tecdoc.sgm.ct.database.datatypes.Expedientes;
import ieci.tecdoc.sgm.ct.database.exception.DbErrorCodes;
import ieci.tecdoc.sgm.ct.database.exception.DbException;
import ieci.tecdoc.sgm.ct.exception.ConsultaCodigosError;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * Modela una fila de la tabla de expedientes del CT.
 *  
 */
public class ExpedienteDatos extends ExpedienteImpl implements Serializable{
  private static final Logger logger = Logger.getLogger(ExpedienteDatos.class);
  protected boolean isDebugeable = true;
  
  /**
   * Constructor de clase
   */
  	public ExpedienteDatos() {
	}
  	
  	/**
     * Constructor de clase
     * @param exp Expediente del que toma las propiedades
     */
	public ExpedienteDatos(Expediente exp) {
		
		this.setNumero(exp.getNumero());
		this.setProcedimiento(exp.getProcedimiento());
		this.setFechaInicio(exp.getFechaInicio());
		this.setNumeroRegistro(exp.getNumeroRegistro());
		this.setFechaRegistro(exp.getFechaRegistro());
		this.setInformacionAuxiliar(exp.getInformacionAuxiliar());
		this.setAportacion(exp.getAportacion());
		this.setCodigoPresentacion(exp.getCodigoPresentacion());
		this.setEstado(exp.getEstado());
		/**
		 * INICIO [Ticket 431] SIGEMCONSULTAMIEMBROS
		 * **/
		this.setFechaConvocatoria(exp.getFechaConvocatoria());
		this.setHoraConvocatoria(exp.getHoraConvocatoria());
		/**
		 * FIN [Ticket 431] SIGEMCONSULTAMIEMBROS
		 * **/
	}
  
  /**
   * Recupera todos los valores de los par�metros de la sentencia
   * de consulta que se pasa como par�metro.
   *
   * @param statement Sentencia sql precompilada.
   * @param idx Indice de posici�n del primer par�metro que se recoge
   * de la consulta.
   * @return Indice de posici�n del �ltimo par�metro recogido
   * @throws DbException Si se produce alg�n error.
   */
  public Integer cargarColumnas(DbOutputStatement statement, Integer idx)
  throws DbException {
     if (isDebugeable)
       logger.debug("cargarColumnas >> statement: " + statement.toString() + " idx entrada: " + idx);
    
     int index = idx.intValue();
     
     try{
    	 
    	numero = statement.getShortText(index ++);
 		procedimiento = statement.getShortText(index ++);
 		fechaInicio = statement.getDateTime(index ++);
 		numeroRegistro = statement.getShortText(index ++);
 		fechaRegistro = statement.getDateTime(index ++);
 		informacionAuxiliar = statement.getShortText(index ++);
 		aportacion = statement.getShortText(index ++);
 		codigoPresentacion = statement.getShortText(index ++);
 		estado = statement.getShortText(index ++);
       
     }catch(Exception e){
       throw new DbException(DbErrorCodes.CT_OBTENER_TODOS_LOS_VALORES, e.getCause());
     }
     
     if (isDebugeable)
       logger.debug("cargarColumnas << Numero: " + numero);
     
     return new Integer(index);
  }
  
  
  /**
   * Genera la sentencia de inserci�n de un expediente.
   *
   * @param statement Sentencia sql precompilada.
   * @param idx Indice de posici�n del primer par�metro que se recoge
   * de la consulta.
   * @return Indice de posici�n del �ltimo par�mtro recogido
   * @throws DbException Si se produce alg�n error.
   */
 
  public Integer insertar(DbInputStatement statement,Integer idx) throws DbException {
	  
    if (isDebugeable)
     logger.debug("insert >> statement: " + statement.toString());
    
    int index = 1;
    
    try{
    	statement.setShortText(index ++, numero);
 		statement.setShortText(index ++, procedimiento);
 		statement.setDateTime(index ++, fechaInicio);
 		statement.setShortText(index ++, numeroRegistro);
 		statement.setDateTime(index ++, fechaRegistro);
 		statement.setShortText(index ++, informacionAuxiliar);
 		statement.setShortText(index ++, aportacion);
 		statement.setShortText(index ++, codigoPresentacion);
 		statement.setShortText(index ++, estado);
    }catch(Exception e){
      throw new DbException(DbErrorCodes.CT_INSERTAR_TODOS_LOS_VALORES, e.getCause());
    }
    return new Integer(index);
  }
  
  
	/**
   * Genera la sentencia de actualizaci�n de estado de un expediente.
   *
   * @param statement Sentencia sql precompilada.
   * @param idx Indice de posici�n del primer par�metro que se recoge
   * de la consulta.
   * @return Indice de posici�n del �ltimo par�mtro recogido
   * @throws Exception Si se produce alg�n error.
   */
  public Integer updateStatus(DbInputStatement statement, Integer idx) throws Exception {
     int index = idx.intValue();
     
     statement.setShortText(index++, estado);
     
     return new Integer(index);
  }
  
  
  /**
   * Carga los valores del expediente
   *
   * @throws ConsultaExcepcion Si se produce alg�n error.
   */
  public void load(String entidad) throws ConsultaExcepcion {

	  if (numero == null || numero.equals(""))
		  throw new ConsultaExcepcion(ConsultaCodigosError.EC_NUMERO_EXPEDIENTE_INCORRECTO);
	  DynamicTable tableInfo = new DynamicTable();
	  DynamicRows rowsInfo = new DynamicRows();
	  DynamicRow rowInfo = new DynamicRow();
	  ExpedientesTabla tablaExpedientes = new ExpedientesTabla();
	  DbConnection dbConn = new DbConnection();

	  if (isDebugeable)
		  logger.debug("load >> numero: " + numero);

	  boolean codigoIncorrecto = false;

	  try {
		  dbConn.open(DBSessionManager.getSession(entidad));
		  tableInfo.setTableObject(tablaExpedientes);
		  tableInfo.setClassName(ExpedientesTabla.class.getName());
		  tableInfo.setTablesMethod("getNombreTabla");
		  tableInfo.setColumnsMethod("getNombresColumnas");
		  rowInfo.setClassName(ExpedienteDatos.class.getName());
		  rowInfo.setValuesMethod("cargarColumnas");
		  rowInfo.addRow(this);
		  rowsInfo.add(rowInfo);

		  if (!DynamicFns.select(dbConn, tablaExpedientes.getClausulaPorNumero(numero), tableInfo, rowsInfo, false)) {
			  codigoIncorrecto = true;
		  }
	  } catch (Exception e) {
		  throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTE);
	  } finally {
		  try{
			  if (dbConn.existConnection())
				  dbConn.close();
		  }catch(Exception ee){
			  DbException DbEx = new DbException(DbErrorCodes.CT_CERRAR_CONEXION);
			  logger.warn(this, DbEx.getCause());
		  }

		  if (codigoIncorrecto)
			  throw new ConsultaExcepcion(ConsultaCodigosError.EC_NUMERO_EXPEDIENTE_INCORRECTO);
	  }
  }
  
  
  /**
   * A�ade un expediente.
   *
   * @throws ConsultaExcepcion Si se produce alg�n error.
   * @throws DbException Si se produce alg�n error.
   */
  public void add(String entidad) throws ConsultaExcepcion, DbException {
	  
     DynamicTable tableInfo = new DynamicTable();
     DynamicRows rowsInfo = new DynamicRows();
     DynamicRow rowInfo = new DynamicRow();
     ExpedientesTabla table = new ExpedientesTabla();
     DbConnection dbConn = new DbConnection();
     
     if (numero == null || numero.equals(""))
         throw new ConsultaExcepcion(ConsultaCodigosError.EC_NUMERO_EXPEDIENTE_INCORRECTO);

     logger.debug("add >> Numero de expediente: " + numero);
     
     try {
    	 dbConn.open(DBSessionManager.getSession(entidad));
        tableInfo.setTableObject(table);
        tableInfo.setClassName(ExpedientesTabla.class.getName());
        tableInfo.setTablesMethod("getNombreTabla");
        tableInfo.setColumnsMethod("getNombresColumnas");
        
        rowInfo.addRow(this);
        rowInfo.setClassName(ExpedienteDatos.class.getName());
        rowInfo.setValuesMethod("insert");
        rowsInfo.add(rowInfo);
        
        DynamicFns.insert(dbConn, tableInfo, rowsInfo);
     } catch (Exception e) {
        throw new DbException(DbErrorCodes.CT_ANIADIR_VALOR, e.getCause());
     } finally {
       try{
         if (dbConn.existConnection())
           dbConn.close();
       }catch(Exception ee){
    	   DbException DbEx = new DbException(DbErrorCodes.CT_CERRAR_CONEXION);
    	   logger.warn(this, DbEx.getCause());
       }
     }
  }
  
  
  /**
   * Borra los expedientes asociados a un guid.
   * @throws ConsultaExcepcion Si se produce alg�n error.
   * @throws DbException Si se produce alg�n error.
   */
  public void delete(String entidad) 
    throws ConsultaExcepcion, DbException {
	  
     ExpedientesTabla table = new ExpedientesTabla();
     
     logger.debug("delete >> numero de expediente: " + numero);
     
     if (numero == null || numero.equals(""))
       throw new ConsultaExcepcion(ConsultaCodigosError.EC_NUMERO_EXPEDIENTE_INCORRECTO);
     
     DbConnection dbConn = new DbConnection();
     
     try {
    	 dbConn.open(DBSessionManager.getSession(entidad));
        DbDeleteFns.delete(dbConn, table.getNombreTabla(), table.getClausulaPorNumero(numero));
        
     } catch (Exception e) {
    	 throw new DbException(DbErrorCodes.CT_BORRAR_TODOS_LOS_VALORES, e.getCause());
     } finally {
       try{
         if (dbConn.existConnection())
           dbConn.close();
       }catch(Exception ee){
    	   DbException DbEx = new DbException(DbErrorCodes.CT_CERRAR_CONEXION);
    	   logger.warn(this, DbEx.getCause());
       }
     }
  }
  
  /**
   * Recupera la informaci�n del conjunto de expedientes de un interesado
   * realizando una busqueda por fechas.
   * <br/>
   * Existen tres tipos de b�squeda 
   * que vendr�n definidas por el cuarto par�metro de entrada ('operadorClausula')
   * Este puede tomar los siguientes valores:
   * <br/> 
   * Operador			Significado
   *<br/>
   * 
   *  >: Mayor que una fecha<br/>
   *  =: Igual a una fecha<br/>
   *  entre: Rango entre dos fechas<br/>
   *
   *
   * @param NIF Se corresponde con el identificador del interesado.
   * @param fecha Fecha de b�squeda.
   * @param fechaHasta Fecha posterior de b�squeda cuando se hace
   * en un intervalo entre dos fechas (solo cuando el operadorClausula viene con valor 'entre').
   * @param operadorClausula Delimita el tipo  de b�squeda
   * @return Expedientes Lista de expedientes resultante de la consulta.
   * @throws Exception Si se produce alg�n error.
   */
  
  public Expedientes cargarExpedientesPorNIFYFecha(String NIF, String fecha, String fechaHasta, String operadorClausula, String entidad) throws Exception {
	  
	  
	 logger.debug("cargarExpedientesPorNIFYFecha>>> fechaDesde:" + fecha + 
			 ",fechaHasta:" + fechaHasta +",operadorClausula:" + operadorClausula);
	 
	 Expedientes expedientes = new Expedientes();
     DynamicTable tableInfo = new DynamicTable();
     DynamicRows rowsInfo = new DynamicRows();
     DynamicRow rowInfoExpediente = new DynamicRow();
     ExpedientesTabla table = new ExpedientesTabla();
     DbConnection dbConn = new DbConnection();
     
     try {
        dbConn.open(DBSessionManager.getSession(entidad));
        
        tableInfo.setTableObject(table);
        tableInfo.setClassName(ExpedientesTabla.class.getName());
        tableInfo.setTablesMethod("getTablaMixta");
        tableInfo.setColumnsMethod("getNombresColumnasConAlias");
        
        rowInfoExpediente.setClassName(ExpedienteDatos.class.getName());
        rowInfoExpediente.setValuesMethod("cargarColumnas");
        rowInfoExpediente.addRow(this);
        rowsInfo.add(rowInfoExpediente);
        
        String clausulaCompleta = null;
        if ("entre".equals(operadorClausula))
        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechas(fecha, fechaHasta,dbConn);
        else if (">".equals(operadorClausula))
        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechaMayorQue(fecha,dbConn);
        else if ("<".equals(operadorClausula))
        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechaMenorQue(fecha,dbConn);
        else if ("=".equals(operadorClausula))
        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFecha(fecha,dbConn);
        else
        	clausulaCompleta = table.getClausulaMixta(NIF);
        	
        if (!DynamicFns.selectMultiple(dbConn,clausulaCompleta, false, tableInfo, rowsInfo)) {
        	//throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
  		}
        
        int size = rowInfoExpediente.getRowCount();
   
        for (int counter = 1; counter < size; counter++) {
        	Expediente expediente = (Expediente)rowInfoExpediente.getRow(counter);
            expedientes.add(expediente);
         }
      
     } catch (Exception e) {
    	 throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
     } finally {
    	 if (dbConn.existConnection())
             dbConn.close();
     }
     
     return expedientes;
  }
  
  
  /**
   * Recupera la informaci�n del conjunto de expedientes de un interesado
   * realizando una busqueda por estado.
   *
   * @param NIF Se corresponde con el identificador del interesado.
   * @param estado estado del expediente
   * @return Expedientes Lista de expedientes resultante de la consulta.
   * @throws Exception Si se produce alg�n error.
   */
  
  public Expedientes cargarExpedientesPorNIFYEstado(String NIF, String estado, String entidad) throws Exception {
	  
	  
	 logger.debug("cargarExpedientesPorNIFYFecha>>> estado:" + estado);
	 
	 Expedientes expedientes = new Expedientes();
     DynamicTable tableInfo = new DynamicTable();
     DynamicRows rowsInfo = new DynamicRows();
     DynamicRow rowInfoExpediente = new DynamicRow();
     ExpedientesTabla table = new ExpedientesTabla();
     DbConnection dbConn = new DbConnection();
     
     try {
        dbConn.open(DBSessionManager.getSession(entidad));
        
        tableInfo.setTableObject(table);
        tableInfo.setClassName(ExpedientesTabla.class.getName());
        tableInfo.setTablesMethod("getTablaMixta");
        tableInfo.setColumnsMethod("getNombresColumnasConAlias");
        
        rowInfoExpediente.setClassName(ExpedienteDatos.class.getName());
        rowInfoExpediente.setValuesMethod("cargarColumnas");
        rowInfoExpediente.addRow(this);
        rowsInfo.add(rowInfoExpediente);
        
        String clausulaCompleta = null;
       	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorEstado(estado);
        	
        if (!DynamicFns.selectMultiple(dbConn,clausulaCompleta, false, tableInfo, rowsInfo)) {
        	//throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
  		}
        
        int size = rowInfoExpediente.getRowCount();
   
        for (int counter = 1; counter < size; counter++) {
        	Expediente expediente = (Expediente)rowInfoExpediente.getRow(counter);
            expedientes.add(expediente);
         }
      
     } catch (Exception e) {
    	 throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
     } finally {
    	 if (dbConn.existConnection())
             dbConn.close();
     }
     
     return expedientes;
  }
  
  
  /**
   * Recupera la informaci�n del conjunto de expedientes de un interesado
   * realizando una busqueda por fechas y por estado.
   * <br/>
   * Existen tres tipos de b�squeda 
   * que vendr�n definidas por el cuarto par�metro de entrada ('operadorClausula')
   * Este puede tomar los siguientes valores:
   * <br/> 
   * Operador			Significado
   *<br/>
   * 
   *  >: Mayor que una fecha<br/>
   *  =: Igual a una fecha<br/>
   *  entre: Rango entre dos fechas<br/>
   *
   *
   * @param NIF Se corresponde con el identificador del interesado.
   * @param fecha Fecha de b�squeda.
   * @param fechaHasta Fecha posterior de b�squeda cuando se hace
   * en un intervalo entre dos fechas (solo cuando el operadorClausula viene con valor 'entre').
   * @param operadorClausula Delimita el tipo  de b�squeda
   * @param estado Estado del expediente
   * @return Expedientes Lista de expedientes resultante de la consulta.
   * @throws Exception Si se produce alg�n error.
   */
  
  public Expedientes cargarExpedientesPorNIFEstadoYFechas(String NIF, String fecha, String fechaHasta, String operadorClausula, String estado, String entidad) throws Exception {
	  
	  
	  logger.debug("cargarExpedientesPorNIFYFecha>>> fechaDesde:" + fecha + 
				 ",fechaHasta:" + fechaHasta +",operadorClausula:" + operadorClausula + ",estado:" + estado);
		 
		 Expedientes expedientes = new Expedientes();
	     DynamicTable tableInfo = new DynamicTable();
	     DynamicRows rowsInfo = new DynamicRows();
	     DynamicRow rowInfoExpediente = new DynamicRow();
	     ExpedientesTabla table = new ExpedientesTabla();
	     DbConnection dbConn = new DbConnection();
	     
	     try {
	        dbConn.open(DBSessionManager.getSession(entidad));
	        
	        tableInfo.setTableObject(table);
	        tableInfo.setClassName(ExpedientesTabla.class.getName());
	        tableInfo.setTablesMethod("getTablaMixta");
	        tableInfo.setColumnsMethod("getNombresColumnasConAlias");
	        
	        rowInfoExpediente.setClassName(ExpedienteDatos.class.getName());
	        rowInfoExpediente.setValuesMethod("cargarColumnas");
	        rowInfoExpediente.addRow(this);
	        rowsInfo.add(rowInfoExpediente);
	        
	        String clausulaCompleta = null;
	        if (operadorClausula.equals("entre"))
	        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechas(fecha, fechaHasta,dbConn);
	        else if (operadorClausula.equals(">"))
	        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechaMayorQue(fecha,dbConn);
	        else if (operadorClausula.equals("<"))
	        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFechaMenorQue(fecha,dbConn);
	        else
	        	clausulaCompleta = table.getClausulaMixta(NIF) + table.getClausulaPorFecha(fecha,dbConn);
	        
       	clausulaCompleta += table.getClausulaPorEstado(estado);
        	
        if (!DynamicFns.selectMultiple(dbConn,clausulaCompleta, false, tableInfo, rowsInfo)) {
        	//throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
  		}
        
        int size = rowInfoExpediente.getRowCount();
   
        for (int counter = 1; counter < size; counter++) {
        	Expediente expediente = (Expediente)rowInfoExpediente.getRow(counter);
            expedientes.add(expediente);
         }
      
     } catch (Exception e) {
    	 throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES);
     } finally {
    	 if (dbConn.existConnection())
             dbConn.close();
     }
     
     return expedientes;
  }
    
  
  /**
	 * A�ade un registro.
	 *
	 * @param dbCon
	 *            Conexion a base de datos.
	 * @throws Exception
	 *             Si se produce alg�n error.
	 */
	public void nuevo(String entidad) throws Exception {
		DynamicTable tableInfo = new DynamicTable();
		DynamicRows rowsInfo = new DynamicRows();
		DynamicRow rowInfo = new DynamicRow();
		ExpedientesTabla table = new ExpedientesTabla();
		DbConnection dbConn = new DbConnection();

		logger.debug("nuevo");

		try {
			dbConn.open(DBSessionManager.getSession(entidad));

			tableInfo.setTableObject(table);
			tableInfo.setClassName(ExpedientesTabla.class.getName());
			tableInfo.setTablesMethod("getNombreTabla");
			tableInfo.setColumnsMethod("getNombresColumnas");

			rowInfo.addRow(this);
			rowInfo.setClassName(ExpedienteDatos.class.getName());
			rowInfo.setValuesMethod("insertar");
			rowsInfo.add(rowInfo);

			DynamicFns.insert(dbConn, tableInfo, rowsInfo);

		} catch (Exception exc) {
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_INSERTAR_EXPEDIENTE, exc.getCause());
		} finally {
			if (dbConn.existConnection())
	             dbConn.close();
		}
	}

	/**
	 * Borra el registro cuyo n�mero de registro que se pasa como par�metro.
	 * 
	 * @param dbCon
	 *            Objeto conexi�n a base de datos.
	 * @throws Exception
	 *             Si se produce alg�n error.
	 */
	public void eliminar(DbConnection dbCon, String entidad) throws Exception {
	
		ExpedientesTabla table = new ExpedientesTabla();
		DbConnection dbConn = new DbConnection();
		
		logger.debug("eliminar");

		try {
			dbConn.open(DBSessionManager.getSession(entidad));

			DbDeleteFns.delete(dbConn, table.getNombreTabla(), table.getClausulaPorNumero(getNumero()));

		} catch (Exception exc) {
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_ELIMINAR_EXPEDIENTE, exc.getCause());
		} finally {
			if (dbConn.existConnection())
	             dbConn.close();
		}
	}

	  /**
	    * Actualiza el estado del registro cuyo n�mero de registro
	    * se pasa como par�metro.
	    *
	    * @param registryNumber N�mero de registro.
	    * @throws Exception Si se produce alg�n error.
	    */
	   public void actulizarEstado(String entidad) throws Exception {
	      DynamicTable tableInfo = new DynamicTable();
	      DynamicRows rowsInfo = new DynamicRows();
	      DynamicRow rowInfo = new DynamicRow();
	      ExpedientesTabla table = new ExpedientesTabla();
	      DbConnection dbConn = new DbConnection();

	      logger.debug("actualizar estado <-- numero expediente: " + numero + ", estado: " + estado);
		     
		  try {
		     dbConn.open(DBSessionManager.getSession(entidad));
	         
	         tableInfo.setTableObject(table);
	         tableInfo.setClassName(ExpedientesTabla.class.getName());
	         tableInfo.setTablesMethod("getNombreTabla");
	         tableInfo.setColumnsMethod("getNombreColumnaEstado");
	         
	         rowInfo.addRow(this);
	         rowInfo.setClassName(ExpedienteDatos.class.getName());
	         rowInfo.setValuesMethod("updateStatus");
	         rowsInfo.add(rowInfo);
	         
	         DynamicFns.update(dbConn, table.getClausulaPorNumero(numero), tableInfo, rowsInfo);
	         
		  } catch (Exception exc) {
				throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES, exc.getCause());
			} finally {
				if (dbConn.existConnection())
		             dbConn.close();
			}
	   }

}