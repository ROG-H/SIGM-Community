package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Ticket #32 - Aviso fin circuito de firma
 * @author Felipe-ecenpri
 * @since 03.06.2010
 */
public class FirmaUtil {
	
	private static final Logger logger = Logger.getLogger(FirmaUtil.class);

	/**
	 * Constantes
	 */
	public static String _ESTADO_FIRMADO = "02";
	public static String _ESTADO_FIRMADO_REPARO = "03";
	public static String _ESTADO_RECHAZADO = "04";
	
	
	/**
	 * 
	 * @param entitiesAPI
	 * @param numExpResolucion
	 * @param doc
	 * @return
	 * @throws ISPACException
	 * @author AWW-�ngel Palaz�n
	 */
	public static boolean isFirma(IEntitiesAPI entitiesAPI,
			String numExpResolucion, int doc) throws ISPACException {

		IItemCollection collection = entitiesAPI.getDocuments(numExpResolucion, "ID = "	+ doc, "");
		Iterator<?> it = collection.iterator();
		IItem item = null;

		if (it.hasNext()) {
			item = (IItem) it.next();
			String estadoFirma = item.getString("ESTADOFIRMA");
			if (!"02".equals(estadoFirma)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * [ecenpri-Felipe Ticket #39]
	 * @since 04.08.2010
	 * @author Felipe
	 * 
	 * @prop�sito: Valida que un cierto documento del tr�mite est� firmado
	 * Si el documento no existe o no est� firmado, no deja terminar el tr�mite
	 * @param rulectx: Contaxto
	 * @throws ISPACRuleException 
	 */
	public static boolean validarDocumentoFirmado(IRuleContext rulectx, String nombreDocumento)
			throws ISPACRuleException
	{
		
		try{
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			int taskId = rulectx.getTaskId();
			
			IItemCollection docsCollection = entitiesAPI.getTaskDocuments(numexp, taskId);
			
			if (docsCollection==null || docsCollection.toList().size()==0){
				rulectx.setInfoMessage("No se puede cerrar el tr�mite ya que no se ha generado " +
						"el documento \"" + nombreDocumento + "\"");
				return false;
			}				
			// Comprobar que se haya firmado el documento
			
			//El documento estar� firmado o firmado con reparo
			String sqlQuery = "ID_TRAMITE = " + taskId +" AND NOMBRE = '" + nombreDocumento + "'" +
				" AND ESTADOFIRMA in ('" + _ESTADO_FIRMADO + "','" + _ESTADO_FIRMADO_REPARO + "')";
			IItemCollection itemCollection = entitiesAPI.getDocuments(numexp, sqlQuery, "");
			
			if (!itemCollection.next()){
				rulectx.setInfoMessage("No se puede cerrar el tr�mite " +
						" ya que el documento \"" + nombreDocumento + "\" est� sin firmar");
				return false;
			}else{
				return true;
			}
		
		} catch (Exception e) {
			logger.error("Error al comprobar la firma del documento " + nombreDocumento, e);
	        throw new ISPACRuleException("Error al comprobar el estado de la firma de los documentos", e);
	    } 
		
	}
}
