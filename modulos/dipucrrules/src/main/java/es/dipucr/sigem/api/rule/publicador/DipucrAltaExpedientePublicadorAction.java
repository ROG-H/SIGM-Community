package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Interesado;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoVO;

/**
 * Acci�n para dar de alta un expediente en la Consulta Telem�tica.
 * 
 */
public class DipucrAltaExpedientePublicadorAction extends SigemBaseAction {

    private static final Logger logger = Logger.getLogger(DipucrAltaExpedientePublicadorAction.class);

    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    public DipucrAltaExpedientePublicadorAction() {
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

        ExpedienteVO expediente = null;
        InteresadoVO interesado = null;
        
        try {
	        
        	// N�mero de expediente
	        String numExp = rctx.getIdObjeto();
        
	        // Servicio de acceso a la informaci�n de la Consulta Telem�tica
	        ConsultaTelematicaService service = new ConsultaTelematicaService();
	        
	        // Informaci�n del expediente
	        expediente = service.getExpediente(numExp);
	        if (expediente != null) {
	        	expediente.setCaportacion(attContext.getAttribute("APORTACION"));
	        	expediente.setCcodpres(attContext.getAttribute("CODPRES"));
	        	expediente.setCinfoaux("");
	        } else {
	        	throw new ActionException("No se ha encontrado el expediente: " + numExp);
	        }
	        	
        	// Informaci�n del interesado principal del expediente
        	interesado = service.getInteresadoExpediente(numExp);
	        
	        // Creaci�n del expediente en la Consulta Telem�tica
	        creaExpediente(expediente, interesado);

	        // Log del expediente e interesados
	        logOk(expediente, interesado);
	        
        } catch (ActionException e) {
        	setInfo("Error en el alta de expediente: " + e.toString());
        	logError(expediente, interesado, e);
        	throw e;
        } catch (Throwable e) {
        	setInfo("Error en el alta de expediente: " + e.toString());
        	logError(expediente, interesado, e);
            throw new ActionException(e);
        }
        
        return true;
    }

    /**
     * Muestra un log del expediente e interesado principal.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     */
    private static void logOk(ExpedienteVO expediente, 
    		InteresadoVO interesado) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
	        // Informaci�n completa del expediente
	        StringBuffer expInfo = new StringBuffer();
	    
	        // A�adir informaci�n del expediente
	        expInfo.append("- EXPEDIENTE:\n");
	        expInfo.append("\t").append(expediente).append("\n");
	        
	        // A�adir informaci�n de los interesados
	        expInfo.append("- INTERESADO:\n");
        	expInfo.append("\t").append(interesado);
	        
	        // Log del resultado de la acci�n
	        CONSULTA_TELEMATICA.info("Alta de expediente:\n" 
	        		+ expInfo.toString());
    	}
    }

    /**
     * Muestra un log de error del expediente e interesado principal.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     * @param e Excepci�n capturada.
     */
    private static void logError(ExpedienteVO expediente, 
    		InteresadoVO interesado, Throwable e) {
    	
        // Informaci�n completa del expediente
        StringBuffer expInfo = new StringBuffer();
    
        // A�adir informaci�n del expediente
        expInfo.append("- EXPEDIENTE:\n");
        expInfo.append("\t").append(expediente).append("\n");
        
        // A�adir informaci�n de los interesados
        expInfo.append("- INTERESADO:\n");
    	expInfo.append("\t").append(interesado);
        
        // Log del error
        CONSULTA_TELEMATICA.error("Error en la acci�n " 
        		+ DipucrAltaExpedientePublicadorAction.class.getName() + ":\n" 
        		+ expInfo.toString(), e);
    }

    /**
     * Crea un nuevo expediente en la Consulta Telem�tica.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     * @throws Exception si ocurre alg�n error.
     */
    private void creaExpediente(ExpedienteVO expediente, InteresadoVO interesado) throws Exception {

    	// Informaci�n del expediente
    	Expediente ctexp = getCTExpediente(expediente);
    	
    	// Informaci�n del interesado principal
    	Interesado ctinteresado = getCTInteresado(interesado);
		
		// Llamada al API de Consulta Telem�tica
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
		try{
			consulta.eliminarInteresadoExpediente(expediente.getCnum(), EntidadHelper.getEntidad());
		}
		catch(Exception e){}
		Expediente existeExp = null;
		try{
			existeExp = consulta.obtenerDetalle(expediente.getCnum(), getEntidad());
		}
		catch(Exception e){}
		if( existeExp == null)
			consulta.nuevoExpediente(ctexp, ctinteresado, getEntidad());
    }
    
    private static Expediente getCTExpediente(ExpedienteVO exp) {
    	Expediente ctexp = null;
    	
    	if (exp != null) {
    		ctexp = new Expediente();
        	ctexp.setNumero(exp.getCnum());
    		ctexp.setProcedimiento(exp.getCproc());
    		ctexp.setFechaInicio(exp.getCfhinicio());
    		ctexp.setNumeroRegistro(exp.getCnumregini());
    		ctexp.setFechaRegistro(exp.getCfhregini());
    		ctexp.setInformacionAuxiliar(
    				StringUtils.nullToEmpty(exp.getCinfoaux()));
    		ctexp.setAportacion(exp.getCaportacion());
    		ctexp.setCodigoPresentacion(StringUtils.defaultString(
    				exp.getCcodpres(), "N"));
    		ctexp.setEstado(Expediente.COD_ESTADO_EXPEDIENTE_INICIADO);
    		//ctexp.setNotificacion();
    	}
    	
    	return ctexp;
    }
    
    private static Interesado getCTInteresado(InteresadoVO interesado) {
    	Interesado ctinteresado = null;
    	
		if (interesado != null) {
			ctinteresado = new Interesado();
			ctinteresado.setNumeroExpediente(interesado.getCnumexp());
			ctinteresado.setNIF(interesado.getCnif());
			ctinteresado.setNombre(interesado.getCnom());
			ctinteresado.setPrincipal("S");
			ctinteresado.setInformacionAuxiliar(
					StringUtils.nullToEmpty(interesado.getCinfoaux()));
			//ctinteresado.setExpedientes();
		}

		return ctinteresado;
    }
}