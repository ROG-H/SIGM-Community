package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.Pago;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.consulta.SolicitudAportacionDocumentacion;
import ieci.tecdoc.sgm.core.services.consulta.Subsanacion;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.publicador.vo.TasaVO;

/**
 * <p>
 * Acci�n para crear una solicitud de subsanaci�n en la Consulta Telem�tica.
 * </p>
 * 
 * <p>
 * Se deben especificar los siguientes atributos en el XML de configuraci�n de la regla
 * de publicaci�n:
 * <ul>
 * <li>MENSAJE_SUBSANACION: Mensaje de subsanaci�n que aparecer� en la aplicaci�n Consulta de Expedientes.</li>
 * <li>MENSAJE_PAGO: Mensaje de pago que aparecer� en la aplicaci�n Consulta de Expedientes.</li>
 * <li>TASA_[id]: Informaci�n de la tasa de pago.</li>
 * </ul>
 * </p>
 * <p>
 * Este es un ejemplo del XML de configuraci�n de la regla de publicaci�n: 
 * <pre>
 * <?xml version='1.0' encoding='ISO-8859-1'?>
 * <attributes>
 * 	<attribute name='TASA_1'>
 * 		<tax>
 * 			<name>Resguardo del pago de tasa</name>
 * 			<labels>
 * 				<label locale='es'>Resguardo del pago de tasa</label>
 * 				<label locale='eu'>Tasaren ordainagiria</label>
 * 				<label locale='ca'>Protegeixo del pagament de taxa</label>
 * 				<label locale='gl'>Resgardo do pago de taxa</label>
 * 			</labels>
 * 			<import>1000</import>
 * 			<sender_entity_id>000000</sender_entity_id>
 * 			<self_settlement_id>100</self_settlement_id>
 * 		</tax>
 * 	</attribute>
 * 	<attribute name='MENSAJE_SUBSANACION'>
 * 		<labels>
 * 			<label locale='es'>Se le ha notificado la necesidad de realizar una subsanaci�n de la documentaci�n aportada al expediente ${NUMEXP} del procedimiento ${NOMBREPROCEDIMIENTO}.</label>
 * 			<label locale='eu'>${NOMBREPROCEDIMIENTO} prozeduraren ${NUMEXP} espedientean aurkeztutako dokumentazioa zuzentzeko beharra dagoela jakinarazi zaizu.</label>
 * 			<label locale='ca'>Se us ha notificat la necessitat d\u0027esmenar la documentacio aportada a l\u0027expedient ${NUMEXP} del procediment ${NOMBREPROCEDIMIENTO}.</label>
 * 			<label locale='gl'>Notificouselle a necesidade de realizar unha reparaci�n da documentaci�n achegada ao expediente ${NUMEXP} do procedemento ${NOMBREPROCEDIMIENTO}.</label>
 * 		</labels>
 * 	</attribute>
 * 	<attribute name='MENSAJE_PAGO'>
 * 		<labels>
 * 			<label locale='es'>Durante la tramitaci�n de su expediente ${NUMEXP} de ${NOMBREPROCEDIMIENTO} se le comunica que es necesario que acredite el pago de la tasa de ${NOMBRE_PAGO} por un valor de ${IMPORTE_PAGO} euros.</label>
 * 			<label locale='eu'>${NOMBREPROCEDIMIENTO}(e)ko ${NUMEXP} espedientea bideratzen ari dela, jakinarazten zaizu ${NOMBRE_PAGO}ren tasa (${IMPORTE_PAGO} eurokoa) ordaindu izana ziurtatu behar duzula.</label>
 * 			<label locale='ca'>Durant la tramitacio del vostre expedient ${NUMEXP} de ${NOMBREPROCEDIMIENTO} se us comunica que acrediteu el pagament de la taxa de ${NOMBRE_PAGO} per un valor de ${IMPORTE_PAGO} euros.</label>
 * 			<label locale='gl'>Durante a tramitaci�n do seu expediente ${NUMEXP} de ${NOMBREPROCEDIMIENTO} comunicar�selle que &eacute; necesario que acredite o pagamento da taxa de ${NOMBRE_PAGO} por un valor de ${IMPORTE_PAGO} euros.</label>
 * 		</labels>
 * 	</attribute>
 * </attributes>
 * </pre>
 * </p>
 * 
 * <p>
 * A la hora de componer los textos, se pueden usar los siguientes par�metros:
 * <ul>
 * <li>NUMEXP: N�mero de expediente.</li>
 * <li>CODPROCEDIMIENTO: C�digo del procedimiento.</li>
 * <li>NOMBREPROCEDIMIENTO: Nombre del procedimiento.</li>
 * <li>NREG: N�mero de registro del expediente.</li>
 * <li>FREG: Fecha de registro del expediente.</li>
 * <li>ASUNTO: Asunto del expediente.</li>
 * <li>FAPERTURA: Fecha de apertura del expediente.</li>
 * <li>NOMBRE_DOC: Nombre del tipo de documento.</li>
 * <li>NDOC_DEST: NIF/CIF del destinatario.</li>
 * <li>NOMBRE_DEST: Identificaci�n del destinatario.</li>
 * <li>DOMICILIO_DEST: Domicilio del destinatario.</li>
 * <li>DIRECCIONTELEMATICA_DEST: Direcci�n telem�tica del destinatario.</li>
 * <li>NOMBRE_PAGO: Nombre del pago.</li>
 * <li>IMPORTE_PAGO: Importe del pago.</li>
 * </ul>
 * 
 * </p>
 * 
 */
@SuppressWarnings("unchecked")
public class SolicitudSubsanacionAction extends SigemBaseAction {

	/** Logger de la clase. */
    private static final Logger logger = 
    	Logger.getLogger(SolicitudSubsanacionAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = 
    	Logger.getLogger("CONSULTA_TELEMATICA");

    public static final String TIMESTAMPFORMAT_INV = "yyyy-MM-dd HH:mm:ss";
    
    /** Propiedades de la entidad DOCUMENTO. */
    private static final Properties DOC_PROPS = new Properties();
    static {
		int ordinal = 0;
		DOC_PROPS.add( new Property(ordinal++, "ID", Types.VARCHAR));
		DOC_PROPS.add( new Property(ordinal++, "DOCUMENTO", Types.VARCHAR));
		DOC_PROPS.add( new Property(ordinal++, "PENDIENTE", Types.VARCHAR));
    }

    /** Contexto del cliente. */
    private ClientContext clientContext = null;

	/** Lista de solicitudes de subsanaci�n. */
	private List<Subsanacion> solicitudesSubsanacion = new ArrayList<Subsanacion>();

    
    /**
     * Constructor.
     * 
     */
    public SolicitudSubsanacionAction() {
    	super();
    	clientContext = new ClientContext();
    	clientContext.setAPI(new InvesflowAPI(clientContext));
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

        try {
        	// Obtener las solicitudes a partir de los documentos requeridos
	        getSolicitudes(rctx, attContext);
        	
        	// Alta de las solicitudes de subsanaci�n
        	altaSolicitudesSubsanacion();
        	
        } catch (ActionException e) {
        	setInfo("Error en el alta de solicitud de subsanaci�n para el expediente "
        			+ rctx.getIdObjeto() + ": " + e.toString());
            throw e;
        } catch (Throwable e) {
        	setInfo("Error en el alta de solicitud de subsanaci�n para el expediente "
        			+ rctx.getIdObjeto() + ": " + e.toString());
            throw new ActionException(e);
        }
        
        return true;
    }
    
    private IItem getExpediente(String numExp) throws ISPACException {

		// API de entidades
		IEntitiesAPI entitiesAPI = clientContext.getAPI().getEntitiesAPI();

		// Obtener la informaci�n del expediente
		IItem exp = entitiesAPI.getExpedient(numExp);
		if (exp == null) {
			throw new ActionException("No se ha encontrado el expediente: "
					+ numExp);
		}
		
		return exp;
    }
    
    private HitoExpediente getHitoExpediente(String numExp) 
    		throws SigemException, ActionException {

    	// API de acceso a Consulta Telem�tica
		ServicioConsultaExpedientes consulta = 
			LocalizadorServicios.getServicioConsultaExpedientes();

		// Informaci�n del hito actual del expediente en CT
        HitoExpediente hito = consulta.obtenerHitoEstado(numExp, getEntidad());
		if (hito == null) {
			throw new ActionException(
					"No existe hito actual en Consulta Telem�tica para el expediente " 
					+ numExp);
		}
		
		return hito;
    }
    
    private void getSolicitudes(RuleContext rctx, AttributeContext attContext)
    		throws SigemException, ISPACException {
    	
    	// Informaci�n del expediente
    	IItem exp = getExpediente(rctx.getIdObjeto());

		// Informaci�n del hito actual del expediente en CT
        HitoExpediente hito = getHitoExpediente(rctx.getIdObjeto());

    	// Componer las solicitudes
        List<Subsanacion> docNames = new ArrayList<Subsanacion>();
        String genericDocId = null;

        solicitudesSubsanacion.add(createSubsanacion(rctx, attContext, genericDocId, docNames, hito.getGuid(), getMessageParams(exp, null, null)));
    }

    private Subsanacion createSubsanacion(RuleContext rctx, AttributeContext attContext, String genericDocId, List<Subsanacion> docNames, String idHito, Map<String, String> params) throws ISPACException {
    	
    	Subsanacion subsanacion = new Subsanacion();

    	subsanacion.setIdentificador(new Guid().toString());
    	subsanacion.setIdDocumento(genericDocId);
    	
    	// Nombre del tipo de documento
    	if (!CollectionUtils.isEmpty(docNames)) {
    		params.put("NOMBRE_DOC", ArrayUtils.join(docNames.toArray(new String[docNames.size()]), ", "));
    	} else {
    		params.put("NOMBRE_DOC", "");
    	}
    	
    	// Mensaje para el ciudadano
    	String message = (String) attContext.getProperties().get("MENSAJE_SUBSANACION");
    	if (StringUtils.isNotBlank(message)) {
    		subsanacion.setMensajeParaElCiudadano(substituteParams(message, params));
    	}
    	
    	subsanacion.setFecha(TypeConverter.toString(new Date(), TIMESTAMPFORMAT_INV));
    	subsanacion.setNumeroExpediente(rctx.getIdObjeto());
    	subsanacion.setIdentificadorHito(idHito);

    	return subsanacion;
    }

    private void altaSolicitudesSubsanacion() throws SigemException {
    	
    	if (!CollectionUtils.isEmpty(solicitudesSubsanacion)) {

        	// API de acceso a Consulta Telem�tica
    		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();

    		Subsanacion subsanacion;
    		for (int i = 0; i < solicitudesSubsanacion.size(); i++) {
    			subsanacion = solicitudesSubsanacion.get(i);
    			
    	    	try {
    				// Alta de la solicitud de subsanaci�n
    				consulta.altaSolicitudSubsanacion(subsanacion, getEntidad());

    		        // Log de la subsanaci�n
    		        logOk(subsanacion);
    		        
    	        } catch (Exception e) {
    	        	logError(subsanacion, e);
    	        }
    		}
    	}
    }
    
    /**
     * Compone los par�metros para los mensajes de aviso.
     * @param exp Informaci�n del expediente.
     * @param doc Informaci�n del documento.
     * @param tasa Informaci�n de la tasa de pago.
     * @return Par�metros para los mensajes de aviso.
     * @throws ISPACException si ocurre alg�n error.
     */
    protected static Map<String, String> getMessageParams(IItem exp, IItem doc, TasaVO tasa) throws ISPACException {
    	
    	Map<String, String> parameters = new HashMap<String, String>();
    	
    	String nombrePago = null;
    	String importePago = null;
    	
    	if (tasa != null) {
    		nombrePago = tasa.getEtiquetas();
    		importePago = tasa.getImporte();
    	}

    	if (exp != null) {
    		
    		// Informaci�n del EXPEDIENTE
	    	parameters.put("NUMEXP", exp.getString("NUMEXP")); // N�mero de expediente
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
    	}
				
		// Informaci�n del DOCUMENTO
		if (doc != null) {
			parameters.put("NOMBRE_DOC", doc.getString("DOCUMENTO")); // Nombre del tipo de documento
		}

    	// Informaci�n del pago
		parameters.put("NOMBRE_PAGO", StringUtils.nullToEmpty(nombrePago)); // Nombre del pago
		parameters.put("IMPORTE_PAGO", StringUtils.nullToEmpty(importePago)); // Importe del pago
		
		return parameters;
    }

    /**
     * Muestra un log terminaci�n correcta.
     * @param solicitud Informaci�n de la solicitud.
     */
    private static void logOk(SolicitudAportacionDocumentacion solicitud) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
    		if (solicitud instanceof Subsanacion) {

    			// Informaci�n de la subsanaci�n
		        StringBuffer info = new StringBuffer()
		        	.append("- SUBSANACI�N: ")
		        	.append(toString(solicitud));
		        
		        // Log del resultado de la acci�n
		        CONSULTA_TELEMATICA.info("Solicitud de Subsanaci�n realizada:\n" 
		        		+ info.toString());

    		} else if (solicitud instanceof Pago) {

    			// Informaci�n del pago
		        StringBuffer info = new StringBuffer()
		        	.append("- PAGO: ")
		        	.append(toString(solicitud));
		        
		        // Log del resultado de la acci�n
		        CONSULTA_TELEMATICA.info("Solicitud de Pago realizada:\n" 
		        		+ info.toString());

    		}
    	}
    }

    /**
     * Muestra un log de error.
     * @param solicitud Informaci�n de la solicitud.
     * @param e Excepci�n capturada.
     */
    private static void logError(SolicitudAportacionDocumentacion solicitud, 
    		Exception e) {
    	
    	if (solicitud instanceof Subsanacion) {

    		// Informaci�n de la subsanaci�n
	        StringBuffer info = new StringBuffer()
	        	.append("- SUBSANACI�N: ")
	        	.append(toString(solicitud));
	        
	        // Log del error
	        CONSULTA_TELEMATICA.error("Error en la Solicitud de Subsanaci�n:\n" 
	        		+ info.toString(), e);
	        
    	} else if (solicitud instanceof Pago) {

    		// Informaci�n del pago
	        StringBuffer info = new StringBuffer()
	        	.append("- PAGO: ")
	        	.append(toString(solicitud));
	        
	        // Log del error
	        CONSULTA_TELEMATICA.error("Error en la Solicitud de Pago:\n" 
	        		+ info.toString(), e);
    	}
    }

    private static String toString(SolicitudAportacionDocumentacion solicitud) {
    	if (solicitud != null) {
    		StringBuffer info = new StringBuffer()
	    		.append("id=[")
	    		.append(solicitud.getIdentificador()).append("]")
	    		.append(", documentoId=[")
	    		.append(solicitud.getIdDocumento()).append("]")
	    		.append(", mensaje=[")
	    		.append(solicitud.getMensajeParaElCiudadano()).append("]")
	    		.append(", hitoId=[")
	    		.append(solicitud.getIdentificadorHito()).append("]")
	    		.append(", fechaSubsanacion=[")
	    		.append(solicitud.getFecha()).append("]")
	    		.append(", expediente=[")
	    		.append(solicitud.getNumeroExpediente()).append("]");
    		
    		if (solicitud instanceof Pago) {
    		    info.append(", entidadEmisoraId=[")
		    		.append(((Pago) solicitud).getEntidadEmisoraId()).append("]")
		    		.append(", autoliquidacionId=[")
		    		.append(((Pago) solicitud).getAutoliquidacionId()).append("]")
		    		.append(", importe=[")
		    		.append(((Pago) solicitud).getImporte()).append("]");
    		}
    		
    		return info.toString();
    	} else {
    		return null;
    	}
    }

}