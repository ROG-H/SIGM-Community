/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
 * Solo podr� usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
 * V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.daos;

import org.apache.log4j.Logger;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;


/**
 * Servicios disponibles para la tabla usuarios.
 * 
 * @author cmorenog
 * 
 */
public class TramDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(TramDAO.class.getName());

    /**
     * Inserta un nuevo expedient.
     * 
     * @param nregistro
     *            Valor de descripcion de expedient.
     * 
     */
    public void insertExpedient(String nregistro) {

	try {

	    getSqlMapClientTemplate().insert("Tramitador.insertExpedient", nregistro);

	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.INSERT_EXPEDIENTE_ERROR, exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
    }
    
    /**
     * Inserta un nuevo expedient/procedimiento.
     * 
     * @param nreg
     *            Valor de descripcion de expedient.
     * @param cod_pcd
     *            Valor del c�digo de procedimiento.           
     * 
     */
    public void insertExpedientProcedure(String nreg, String cod_pcd) {

		try {
			java.util.HashMap<String, String> hashMap = new java.util.HashMap<String, String>();
			hashMap.put("nreg", nreg);
			hashMap.put("cod_pcd", cod_pcd);
		    getSqlMapClientTemplate().insert("Tramitador.insertExpedientProcedure", hashMap);
	
		}
		catch (Exception exception) {
		    LOG.error(ErrorConstants.INSERT_EXPEDIENTE_ERROR, exception);
		    Utils.redirectToErrorPage(null, null, exception);
		}
    }
    
    
    

    /**
     * Saber si existe un user con el mismo nombre.
     * 
     * @param nregistro
     *            user a buscar.
     * @return boolean si esiste el user.
     */
    public boolean existExpedient(String nregistro) {
	boolean result = false;
	try {
	    int count = (Integer) getSqlMapClientTemplate().queryForObject("Tramitador.existExpedient", nregistro);
	    if (count > 0) {
		result = true;
	    }

	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.QUERY_EXIST_EXPEDIENTE, exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
	return result;
    }
    
    /**
     * Saber si existe un registro con el n�mero y c�digo de procedimiento.
     * 
     * @param cod_pcd
     *            c�digo de procedimiento.
     * @param nreg
     *            n�mero de registro.
     * @return boolean si esiste el user.
     */
    public boolean existExpedientProcedure(String nreg, String cod_pcd) {
	boolean result = false;
	try {
		java.util.HashMap<String, String> hashMap = new java.util.HashMap<String, String>();
		hashMap.put("nreg", nreg);
		hashMap.put("cod_pcd", cod_pcd);
	    int count = (Integer) getSqlMapClientTemplate().queryForObject("Tramitador.existExpedientProcedure", hashMap);
	    if (count > 0) {
		result = true;
	    }

	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.QUERY_EXIST_EXPEDIENTE, exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
	return result;
    }


}