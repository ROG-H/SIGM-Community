/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.isicres.session.folder.FolderAsocSession;

import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;


/**
 * Clase que implementa IGenericBo que contiene los m�todos relacionados con los
 * registros asociados.
 * 
 * @author eacuna
 */
public class AsocRegisterBo implements IGenericBo, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(AsocRegisterBo.class.getName());
    
    /**
     * Asocia un registro de entrada a una salida
     * @param sessionID - Sesi�n del usuario logado.
     * @param entidad - Entidad del usuario logado.
     * @param idArchPrim - Identificador del libro de entrada
     * @param idFdrPrim - FolderId del registro de entrada.
     * @param idArchSec - Identificador del libro de salida
     * @param idFdrSec - FolderId del registro de salida.
     * @throws RPRegisterException - Excepci�n lanzada en caso de error.
     */
    public static void saveAsocRegister(String sessionID, String entidad,
			int idArchPrim, int idFdrPrim, int idArchSec, int idFdrSec) throws RPRegisterException {
    	try 
    	{
			FolderAsocSession.saveAsocRegFdr(sessionID, entidad, idArchPrim, 
					idFdrPrim, idArchSec, idFdrSec);
			
		} catch (Exception exception) {
			LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, exception);
		    throw new RPRegisterException(RPRegisterErrorCode.SAVE_ASSOC_REGISTER_ERROR,
			    ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE, exception);
		}
    	
    }
}
