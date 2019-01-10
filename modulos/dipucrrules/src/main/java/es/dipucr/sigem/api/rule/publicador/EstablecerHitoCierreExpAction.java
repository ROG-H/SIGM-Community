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
import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.ct.GestorConsulta;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acci�n para dar de alta un hito del estado de un expediente 
 * en la Consulta Telem�tica.
 * 
 */
public class EstablecerHitoCierreExpAction extends SigemBaseAction {

	//========================================================================
	// Constantes de los nombres de los par�metros de la regla
	//========================================================================
	private static final String CODIGO 				= "CODIGO";
	//========================================================================
	
	/** Logger de la clase. */
    private static final Logger logger = Logger.getLogger(EstablecerHitoCierreExpAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public EstablecerHitoCierreExpAction() {
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
    public boolean execute(RuleContext rctx, AttributeContext attContext) throws ActionException {
    	
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
	        	
	        	// Establecer el hito actual
	        	hito = establecerHitoActual(rctx, attContext);
        	
	        	cerrarExpediente(hito.getNumeroExpediente(), getEntidad());
	        }

	        // Log del hito
	        logOk(hito);
        } catch (Exception e) {
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
		
			hito.setDescripcion("El expediente: " + exp.getString("NUMEXP")  + " - " + exp.getString("ASUNTO") + " ha sido cerrado.");
		} catch (ISPACException e) {
			setInfo("Error al componer el mensaje del hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
		}
		
		hito.setInformacionAuxiliar("");
		
		// Paso a hist�rico del hito anterior
        boolean pasoAHistorico = true;

        // Establece el hito actual en Consulta Telem�tica
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
		consulta.establecerHitoActual(hito, new FicherosHito(), pasoAHistorico, getEntidad());
		
		// Devolver la informaci�n del hito creado
		return consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
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
        CONSULTA_TELEMATICA.error("Error en la acci�n " + EstablecerHitoCierreExpAction.class.getName() + ":\n" + hitoInfo.toString(), e);
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
    
    public void cerrarExpediente(String numeroExpediente, Entidad entidad)
			throws ConsultaExpedientesException {
		try {
			GestorConsulta.actualizarEstado(numeroExpediente, Expediente.COD_ESTADO_EXPEDIENTE_FINALIZADO, entidad.getIdentificador());
		} catch (ConsultaExcepcion e) {
			logger.error("Error cerrando expediente.", e);
			throw getConsultaExpedientesException(e);
		} catch (Exception e) {
			logger.error("Error cerrando expediente.", e);
			throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
		}
	}
    
    private ConsultaExpedientesException getConsultaExpedientesException(
			ConsultaExcepcion poException) {
		if (poException == null) {
			return new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION);
		}
		StringBuffer cCodigo = new StringBuffer(ConstantesServicios.SERVICE_QUERY_EXPS_ERROR_PREFIX);
		cCodigo.append(String.valueOf(poException.getErrorCode()));
		return new ConsultaExpedientesException(Long.valueOf(cCodigo.toString()).longValue(), poException);

	}
}