package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acci�n para dar de alta un hito del estado de un expediente 
 * en la Consulta Telem�tica.
 * 
 */
public class EstablecerHitoActualNombreAction extends SigemBaseAction {

	//========================================================================
	// Constantes de los nombres de los par�metros de la regla
	//========================================================================
	private static final String CODIGO 				= "CODIGO";
	private static final String DESCRIPCION 		= "DESCRIPCION";
	private static final String PASO_A_HISTORICO 	= "PASO_A_HISTORICO";
	private static final String ENVIAR_DOCUMENTOS 	= "ENVIAR_DOCUMENTOS";
	//========================================================================
	
	/** Logger de la clase. */
    private static final Logger logger = 
    	Logger.getLogger(EstablecerHitoActualNombreAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = 
    	Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public EstablecerHitoActualNombreAction() {
    	super();
    }
    
    /**
     * Ejecuta la acci�n.
     * @param rctx Contexto de ejecuci�n de la regla
     * @param attContext Atributos con informaci�n extra, utilizados dentro de 
     * la ejecuci�n de la regla.
     * @return true si la ejecuci�n termina correctamente, false en caso 
     * contrario.
     * @throws ActionException si ocurre alg�n error.
     */
    public boolean execute(RuleContext rctx, AttributeContext attContext) 
    		throws ActionException {
    	
        if (logger.isInfoEnabled()) {
            logger.info("Acci�n [" + this.getClass().getName() + "] en ejecuci�n");
        }

        HitoExpediente hito = new HitoExpediente();
        ExpedienteVO expediente = null;
        try {
        	Expediente existeExp = null;
        	
	        String numExp = rctx.getIdObjeto();
	        ConsultaTelematicaService serviceExp = new ConsultaTelematicaService();
	        expediente = serviceExp.getExpediente(numExp);
	        
    		ServicioConsultaExpedientes consultaExp = LocalizadorServicios.getServicioConsultaExpedientes();

    		try{
    			existeExp = consultaExp.obtenerDetalle(expediente.getCnum(), getEntidad());
    		}
    		catch(Exception e){}
    		if( existeExp != null){
		        	
	        	hito = establecerHitoActual(rctx, attContext);
	
		        // Comprobar si hay que enviar los documentos asociados al hito
		        if ("S".equalsIgnoreCase(attContext.getAttribute(ENVIAR_DOCUMENTOS))) {
		        	anexarFicheros(rctx, hito);
		        }
    		}
		    // Log del hito
		    logOk(hito);
        } catch (Throwable e) {
        	setInfo("Error al establecer hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
        }
        
        return true;
    }
    
    private HitoExpediente establecerHitoActual(RuleContext rctx, AttributeContext attContext) throws SigemException, ActionException {
    	
    	// Informaci�n del hito
    	HitoExpediente hito = new HitoExpediente();
		hito.setGuid(new Guid().toString());
		hito.setNumeroExpediente(rctx.getIdObjeto());
		hito.setCodigo(StringUtils.nullToEmpty(attContext.getAttribute(CODIGO)));
		hito.setFecha(TypeConverter.toString(rctx.getFecha(), "yyyy-MM-dd"));
		
		ClientContext cct = new ClientContext();
		cct.setAPI(new InvesflowAPI(cct));
		
		IItem exp;
		try {
			exp = ExpedientesUtil.getExpediente(cct, rctx.getIdObjeto());	
		
			hito.setDescripcion(substituteParams(attContext.getAttribute(DESCRIPCION), getMessageParams(cct, rctx, exp)));
		} catch (ISPACException e) {
			setInfo("Error al componer el mensaje del hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
		}
		
		hito.setInformacionAuxiliar("");
		
		// Paso a hist�rico del hito anterior
        boolean pasoAHistorico = "S".equalsIgnoreCase(attContext.getAttribute(PASO_A_HISTORICO));

        // Establece el hito actual en Consulta Telem�tica
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
		consulta.establecerHitoActual(hito, new FicherosHito(), pasoAHistorico, getEntidad());
		
		// Devolver la informaci�n del hito creado
		return consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
    }
    
    private void anexarFicheros(RuleContext rctx, HitoExpediente hito) 
    		throws Exception {
    	
        ConsultaTelematicaService service = new ConsultaTelematicaService();
        FicherosHito ficheros = null;

        // Obtener el identificador del tr�mite
    	int idTramite = TypeConverter.parseInt((String) rctx.getProperties().get("id_tramite"), -1);
    	int idFase = TypeConverter.parseInt((String) rctx.getProperties().get("id_fase"), -1);

        try {

			// Llamada al API de Consulta Telem�tica
			ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();

	        // Obtener los ficheros asociados al hito en tramitaci�n
	        if (idTramite > 0) {
		        ficheros = service.getFicherosTramite(hito.getGuid(), rctx.getIdObjeto(), idTramite);
	        } else if (idFase > 0) {
		        ficheros = service.getFicherosFase(hito.getGuid(), rctx.getIdObjeto(), idFase);
	        } else {
	        	logger.warn("No se ha encontrado ning�n identificador de fase o tr�mite");
	        }

	        // Anexar los ficheros al hito actual en CT
	        if ((ficheros != null) && ficheros.count() > 0) {
	        	consulta.anexarFicherosHitoActual(ficheros, getEntidad());
	        }

        } catch (Exception e) {
        	logger.error("Error al anexar ficheros al hito: " + hito.getGuid(), e);
        	
        	try {
	        	// Eliminar los ficheros anexados
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw e;
        }
    }
    
    /**
     * Muestra un log del hito.
     * @param hito Datos del hito.
     */
    private static void logOk(HitoExpediente hito) {
    	
    	if (hito!= null && CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
	        // Informaci�n del hito
	        StringBuffer hitoInfo = new StringBuffer().append("- HITO ESTADO: ").append(toString(hito));
	        
	        // Log del resultado de la acci�n
	        CONSULTA_TELEMATICA.info("Alta de hito actual:\n"  + hitoInfo.toString());
    	}
    }

    /**
     * Muestra un log de error.
     * @param hito Datos del hito.
     */
    private static void logError(HitoExpediente hito, Throwable e) {
    	
        // Informaci�n del hito
        StringBuffer hitoInfo = new StringBuffer().append("- HITO ESTADO: ").append(toString(hito));
        
        // Log del error
        CONSULTA_TELEMATICA.error("Error en la acci�n " + EstablecerHitoActualNombreAction.class.getName() + ":\n" + hitoInfo.toString(), e);
    }
    
    /**
     * Obtiene una cadena con la informaci�n del hito.
     * @param hito Informaci�n del hito.
     * @return Cadena con la informaci�n del hito.
     */
    private static String toString(HitoExpediente hito) {
    	if (hito != null) {
	    	return new StringBuffer()
	    		.append("guid=[").append(hito.getGuid()).append("]")
	    		.append(", numeroExpediente=[").append(hito.getNumeroExpediente())
	    		.append("]")
	    		.append(", codigo=[").append(hito.getCodigo()).append("]")
	    		.append(", fecha=[").append(hito.getFecha()).append("]")
	    		.append(", descripcion=[").append(hito.getDescripcion()).append("]")
	    		.append(", informacionAuxiliar=[")
	    		.append(hito.getInformacionAuxiliar()).append("]")
	    		.toString();
    	} else {
    		return null;
    	}
    }
    
    protected static Map<String, String> getMessageParams(ClientContext cct, RuleContext rctx, IItem exp) throws ISPACException {
    	
    	Map<String, String> parameters = new HashMap<String, String>();
  
    	if (exp != null) {
    		String numexp = exp.getString("NUMEXP");
    		// Informaci�n del EXPEDIENTE
	    	parameters.put("NUMEXP", numexp); // N�mero de expediente
	    	parameters.put("CODPROCEDIMIENTO", exp.getString("CODPROCEDIMIENTO")); // C�digo del procedimiento
			parameters.put("NOMBREPROCEDIMIENTO", exp.getString("NOMBREPROCEDIMIENTO")); // Nombre del procedimiento
			parameters.put("NREG", exp.getString("NREG")); // N�mero de registro del expediente
			parameters.put("FREG", exp.getString("FREG")); // Fecha de registro del expediente
			parameters.put("ASUNTO", exp.getString("ASUNTO")); // Asunto del expediente
			parameters.put("FAPERTURA", exp.getString("FAPERTURA")); // Fecha de apertura del expediente

			// Informaci�n del DESTINATARIO
			parameters.put("NDOC_DEST", exp.getString("NIFCIFTITULAR")); // NIF/CIF del destinatario
			parameters.put("NOMBRE_DEST", exp.getString("IDENTIDADTITULAR")); // Identificaci�n del destinatario
			parameters.put("DOMICILIO_DEST", exp.getString("DOMICILIO")); // Domicilio del destinatario
			parameters.put("DIRECCIONTELEMATICA_DEST", exp.getString("DIRECCIONTELEMATICA")); // DEU del destinatario
			
			int idTramite = TypeConverter.parseInt((String) rctx.getProperties().get("idtramite"), -1);
	    	int idFase = TypeConverter.parseInt((String) rctx.getProperties().get("idfase"), -1);
	    	
	    	String nombreFase = "";
	    	if(idFase>0){
	    		nombreFase = FasesUtil.getNombreFase(cct, idFase);
	    	}
	    	else{
	    		nombreFase = FasesUtil.getNombreFaseActiva(cct, numexp);				
	    	}
	    	parameters.put("NOMBREFASE", nombreFase);
	    	
	    	String nombreTramite = "";
	    	if(idTramite > 0){
	    		IItem tramite = TramitesUtil.getPTramiteById(cct, idTramite);
	    		if(tramite != null) nombreTramite = tramite.getString("NOMBRE");
	    		
	    	}
	    	parameters.put("NOMBRETRAMITE", nombreTramite);
    	}
		
		return parameters;
    }
}