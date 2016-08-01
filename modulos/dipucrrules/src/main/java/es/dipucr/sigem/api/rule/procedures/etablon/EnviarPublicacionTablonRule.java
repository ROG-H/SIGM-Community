package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.rrhh.RespuestaSolicitudLicenciasRule;
import es.dipucr.tablon.services.TablonWSProxy;


/**
 * [eCenpri-Felipe ticket #504]
 * Regla para enviar la publicaci�n al tabl�n y
 * cerrar el expediente
 * @author Felipe
 * @since 08.03.2012
 */
public class EnviarPublicacionTablonRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(RespuestaSolicitudLicenciasRule.class);
	
	protected static final String _TRAMITE_FIRMAS = "Publicar en el tabl�n"; //TODO Constants
	protected static final String _DOC_PUBLICACION = "eTablon - Publicaci�n";
	protected static final String _COD_TRAM_CERTIFICADO = "ETABLON_CERT";
	
	public static String _MENSAJE_FIRMADO = "Publicaci�n aceptada";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			//Comprobamos que haya documento y este este firmado
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			IItem itemDocumento = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_PUBLICACION);
			
			if (null == itemDocumento){
				rulectx.setInfoMessage("No se puede terminar tr�mite. Es necesario " +
					"crear el documento de publicaci�n y que el mismo est� firmado.");
				return false;
			}
			
			String estado = itemDocumento.getString("ESTADOFIRMA");
			if (estado.equals(SignStatesConstants.SIN_FIRMA)){
				rulectx.setInfoMessage("No se puede terminar tr�mite. " +
						"El documento de publicaci�n debe estar firmado.");
				return false;
			}
			
			return true;
		}
		catch (Exception e) {
        	logger.error("Error al validar si la publicaci�n del tabl�n est� firmada en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al validar si la publicaci�n del tabl�n est� firmada en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
	}
	
	/**
	 * Generaci�n de la fase, el tr�mite y env�o al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			//*********************************************
			//Env�o de la publicaci�n a eTabl�n
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			String numexp = rulectx.getNumExp();
			IItem itemDocumento = DocumentosUtil.getPrimerDocumentByNombre(numexp, rulectx, _DOC_PUBLICACION);
			int idDoc = itemDocumento.getKeyInt();
			String estado = itemDocumento.getString("ESTADOFIRMA");
			
			IItemCollection collection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
			IItem itemPublicacion = (IItem)collection.iterator().next();
			
			String strMotivo = null;
			boolean bFirmado = false;
			boolean bWScorrecto = false;
			
			if (estado.equals(SignStatesConstants.FIRMADO)){
				bFirmado = true;
			}
//			else if (estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
//				bFirmado = true;
//				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
//			}
			else if (estado.equals(SignStatesConstants.RECHAZADO)){
				bFirmado = false;
				strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
				TablonUtils.generarAvisoUsuario(rulectx, "eTabl�n: Su publicaci�n ha sido rechazada", 
						strMotivo, itemDocumento, itemPublicacion);
				ExpedientesUtil.avanzarFase(cct, numexp); //Cerramos el expediente
			}
			
			if (bFirmado){
				//Sacamos los datos de la publicaci�n
				String titulo = itemPublicacion.getString("TITULO");
				String descripcion = itemPublicacion.getString("DESCRIPCION");
				String codCategoria = itemPublicacion.getString("COD_CATEGORIA");
				String codServicio = itemPublicacion.getString("COD_SERVICIO");
				Date dFechaIniVigencia = itemPublicacion.getDate("FECHA_INI_VIGENCIA");
				//[eCenpri-Felipe #621] Si la fecha firma es posterior al 
				//inicio de vigencia, esta ser� la fecha de inicio de vigencia
				Date dFechaFirma = itemDocumento.getDate("FFIRMA");
				if (dFechaFirma.after(dFechaIniVigencia)){
					dFechaIniVigencia = dFechaFirma;
				}
				Date dFechaFinVigencia = itemPublicacion.getDate("FECHA_FIN_VIGENCIA");
				Calendar calendarIniVigencia = Calendar.getInstance();
				calendarIniVigencia.setTime(dFechaIniVigencia);
				//[eCenpri-Felipe #621] Por si acaso hemos tomado la fecha de firma, limpiamos la hora
				calendarIniVigencia.set(Calendar.HOUR_OF_DAY, 0);
				calendarIniVigencia.set(Calendar.MINUTE, 0);
				calendarIniVigencia.set(Calendar.SECOND, 0);
				Calendar calendarFinVigencia = Calendar.getInstance();
				calendarFinVigencia.setTime(dFechaFinVigencia);
				String servicioOtros = itemPublicacion.getString("SERVICIO_OTROS");
				String categoriaOtros = itemPublicacion.getString("CATEGORIA_OTROS");
				
				//Sacamos la fecha de firma
//				Date dFechaFirma = itemDocumento.getDate("FFIRMA"); //[eCenpri-Felipe #621]
				Calendar calendarFechaFirma = Calendar.getInstance();
				calendarFechaFirma.setTime(dFechaFirma);
				
				//Obtenemos el CVE
				String cve = itemDocumento.getString("COD_COTEJO");
				
				//Id transacci�n
				String strQuery = "WHERE ID_DOCUMENTO = " + idDoc;
				collection = entitiesAPI.queryEntities(SpacEntities.SPAC_ID_TRANSACCION_FIRMA, strQuery);
				String idTransaccion = "";
				String hash = "";
				if (collection.next()){
					IItem itemTransaccion = (IItem)collection.iterator().next();
					idTransaccion = itemTransaccion.getString("ID_TRANSACCION");
					hash = itemTransaccion.getString("HASH");
				}
				
				//Obtenemos el fichero
				String strInfopagRde = itemDocumento.getString("INFOPAG_RDE");
				File filePublicacion = DocumentosUtil.getFile(cct, strInfopagRde, null, null);
				DataHandler dhPublicacion = new DataHandler(new FileDataSource(filePublicacion));
				
				//Hacemos la petici�n al servicio web
				TablonWSProxy wsTablon = new TablonWSProxy();
				String codEntidad = EntidadesAdmUtil.obtenerEntidad((ClientContext)cct);
				bWScorrecto = wsTablon.insertarPublicacion(codEntidad, titulo, descripcion, calendarFechaFirma, codServicio, 
						codCategoria, calendarIniVigencia, calendarFinVigencia, cve, hash, idTransaccion, numexp, 
						servicioOtros, categoriaOtros, dhPublicacion);
				
				if (bWScorrecto){
					TablonUtils.generarAvisoUsuario(rulectx, "eTabl�n: Su documento ha sido publicado.", 
							null, itemDocumento, itemPublicacion);
					TramitesUtil.crearTramite(_COD_TRAM_CERTIFICADO, rulectx);
				}
				else{
					throw new Exception("Error en el servicio web de la aplicaci�n eTabl�n.");
				}
			}
		}
		catch (Exception e) {
        	logger.error("Error al insertar la publicaci�n en el tabl�n en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar la publicaci�n en el tabl�n en el expediente: " + rulectx.getNumExp() + "." + e.getMessage(), e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
