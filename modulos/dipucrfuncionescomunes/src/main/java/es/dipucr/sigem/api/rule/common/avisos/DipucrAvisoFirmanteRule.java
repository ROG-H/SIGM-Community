package es.dipucr.sigem.api.rule.common.avisos;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

/**
 * Regla para el aviso a un firmante de circuito de firmas en el momento
 * que le llegue el turno. Esta regla soporta los siguientes avisos:
 * 
 * <ul>
 * <li>Correo electr�nico: cuando el tipo de notificaci�n es EM.</li> 
 * <li>SMS: cuando el tipo de notificaci�n es SM.</li>
 * </ul>
 * 
 * <p>Para el env�o de correos electr�nicos, es necesario definir las
 * siguientes variables del sistema en el Cat�logo de Procedimientos:</p>
 * 
 * <ul>
 * <li>AVISO_FIRMANTE_EMAIL_FROM: Direcci�n de correo electr�nico remitente.</li>
 * <li>AVISO_FIRMANTE_EMAIL_SUBJECT: Asunto del correo electr�nico.</li>
 * <li>AVISO_FIRMANTE_EMAIL_CONTENT: Contenido del correo electr�nico.</li>
 * </ul>
 *
 * <p>Para el env�o de SMS, es necesario definir las siguientes variables 
 * del sistema:</p>
 * 
 * <ul>
 * <li>AVISO_FIRMANTE_SMS_USER: Usuario del m�dulo de env�o de SMS.</li>
 * <li>AVISO_FIRMANTE_SMS_PASSWORD: Contrase�a del usuario del m�dulo de env�o de SMS.</li>
 * <li>AVISO_FIRMANTE_SMS_SRC: N�mero de tel�fono remitente.</li>
 * <li>AVISO_FIRMANTE_SMS_TXT: Contenido del mensaje.</li>
 * </ul>
 * 
 * <p>Los mensajes definidos para el asunto y contenido de los correos electr�nicos
 * y el texto de los SMS pueden utilizar variables. El formato de las variables es
 * ${NOMBRE_VARIABLE}. Las variables definidas son:</p>
 * 
 * <ul>
 * <li>NUMEXP: N�mero de expediente al que pertenece el documento.</li>
 * <li>DOCUMENT_NAME: Nombre del documento.</li>
 * <li>DOCUMENT_DESC: Descripci�n del documento.</li>
 * <li>DOCUMENT_DATE: Fecha del documento.</li>
 * </ul>
 * 
 */
public class DipucrAvisoFirmanteRule implements IRule {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(DipucrAvisoFirmanteRule.class);
	
    //--------------------------------------------------------------------------------------------------
    // C�digos de los tipos de notificaci�n.
    //--------------------------------------------------------------------------------------------------
	private static final String NOTIF_TYPE_EMAIL 			= "EM";
	private static final String NOTIF_TYPE_SMS 				= "SM";
    //--------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------
    // Variables Globales
    //--------------------------------------------------------------------------------------------------
//	private static final String EMAIL_FROM_VAR_NAME = 		"AVISO_FIRMANTE_EMAIL_FROM";
	private static final String EMAIL_SUBJECT_VAR_NAME = 	"AVISO_FIRMANTE_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = 	"AVISO_FIRMANTE_EMAIL_CONTENT";

	private static final String SMS_USER_VAR_NAME = 		"AVISO_FIRMANTE_SMS_USER";
	private static final String SMS_PASSWORD_VAR_NAME = 	"AVISO_FIRMANTE_SMS_PASSWORD";
	private static final String SMS_SRC_VAR_NAME = 			"AVISO_FIRMANTE_SMS_SRC";
	private static final String SMS_TXT_VAR_NAME = 			"AVISO_FIRMANTE_SMS_TXT";
    //--------------------------------------------------------------------------------------------------

	
	/**
	 * Constructor.
	 */
	public DipucrAvisoFirmanteRule() {
		super();
	}

	/**
	 * Inicializa la regla.
	 * 
	 * @param rulectx
	 *            Contexto de la regla.
	 * @return true si la inicializaci�n se ha ejecutado correctamente.
	 * @throws ISPACRuleException
	 *             si ocurre alg�n error.
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * Valida la regla.
	 * 
	 * @param rulectx
	 *            Contexto de la regla.
	 * @return true si la regla se ha validado.
	 * @throws ISPACRuleException
	 *             si ocurre alg�n error.
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * Ejecuta la regla.
	 * 
	 * @param rulectx
	 *            Contexto de la regla.
	 * @return Objeto de respuesta de la regla.
	 * @throws ISPACRuleException
	 *             si ocurre alg�n error.
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		int circuitId = 0;
		int stepId = 0;
		String tipoNotif = "";
		
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			ISignAPI signAPI = invesflowAPI.getSignAPI();

			circuitId = rulectx.getInt("ID_CIRCUITO");
			stepId = rulectx.getInt("ID_PASO");
			
			// Informaci�n del paso
			IItem step = signAPI.getCircuitStep(circuitId, stepId);
			tipoNotif = step.getString("TIPO_NOTIF");
			
			if (StringUtils.isBlank(tipoNotif)) {
				// nada
			} else if (NOTIF_TYPE_EMAIL.equals(tipoNotif)) {
				sendEmail(rulectx, step);
			} else if (NOTIF_TYPE_SMS.equals(tipoNotif)) {
				sendSms(rulectx, step);
			} else {
				logger.warn("Tipo de notificaci�n no soportado por la regla: " + tipoNotif);
			}
			
		} catch (Exception e) {
			logger.error("Error en la regla AvisoFirmanteRule en el id_circuito: " + circuitId);
			logger.error("Error en la regla AvisoFirmanteRule en el step: " + stepId);
			logger.error("Error en la regla AvisoFirmanteRule en el tipoNotif: " + tipoNotif);
			logger.error("Error en la regla AvisoFirmanteRule " + e.getMessage(), e);
		}
		
		return null;
	}

	/**
	 * El sistema invoca esta funci�n si se encuentra con alg�n problema durante
	 * la ejecuci�n del evento. Permitir�a deshacer operaciones realizadas en el
	 * m�todo execute().
	 * 
	 * @param rulectx
	 *            Contexto de la regla.
	 * @throws ISPACRuleException
	 *             si ocurre alg�n error.
	 */
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * [eCenpri-Felipe #809]
	 * Nuevo m�todo para que env�e bien fuera de nuestros servidores
	 * @param rulectx
	 * @param step
	 * @throws ISPACException
	 * @throws SigemException
	 */
	protected void sendEmail(IRuleContext rulectx, IItem step) throws ISPACException, SigemException {
		
		String dirNotif = step.getString("DIR_NOTIF");
		
		String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

		Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
		List<Object[]> imagenes = new ArrayList<Object[]>();
		imagenes.add(imagen);
		
		if (StringUtils.isNotBlank(dirNotif)) {

			MailUtil.enviarCorreoConAcusesYVariables(rulectx, dirNotif, EMAIL_SUBJECT_VAR_NAME,
					EMAIL_CONTENT_VAR_NAME, getVariables(rulectx), null, null, dirNotif, false, imagenes);
		}
	}
	
	//[eCenpri-Felipe #809] Se sustituye este m�todo por el anterior
//	protected void sendEmail(IRuleContext rulectx, IItem step) throws ISPACException, SigemException {
//		String dirNotif = step.getString("DIR_NOTIF");
//		if (StringUtils.isNotBlank(dirNotif)) {
//
//			IClientContext ctx = rulectx.getClientContext();
//			
//			String from = ConfigurationMgr.getVarGlobal(ctx, EMAIL_FROM_VAR_NAME);
//			String subject = ConfigurationMgr.getVarGlobal(ctx, EMAIL_SUBJECT_VAR_NAME);
//			String content = ConfigurationMgr.getVarGlobal(ctx, EMAIL_CONTENT_VAR_NAME);
//			
//			Map variables = getVariables(rulectx);
//
//			if (StringUtils.isNotBlank(subject)) {
//				subject = StringUtils.replaceVariables(subject, variables);
//			}
//
//			if (StringUtils.isNotBlank(content)) {
//				content = StringUtils.replaceVariables(content, variables);
//			}
//
//			ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
//			svc.sendMail(from, new String[] { dirNotif }, null, null, subject, content, null);
//		}
//	}

	protected void sendSms(IRuleContext rulectx, IItem step) throws ISPACException, SigemException {
		String dirNotif = step.getString("DIR_NOTIF");
		if (StringUtils.isNotBlank(dirNotif)) {

			IClientContext ctx = rulectx.getClientContext();
			
			String user = ConfigurationMgr.getVarGlobal(ctx, SMS_USER_VAR_NAME);
			String pwd = ConfigurationMgr.getVarGlobal(ctx, SMS_PASSWORD_VAR_NAME);
			String src = ConfigurationMgr.getVarGlobal(ctx, SMS_SRC_VAR_NAME);
			String txt = ConfigurationMgr.getVarGlobal(ctx, SMS_TXT_VAR_NAME);

			if (StringUtils.isNotBlank(txt)) {
				txt = StringUtils.replaceVariables(txt, getVariables(rulectx));
			}

			ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
			svc.sendSMS(user, pwd, src, dirNotif, txt);
		}
	}
	
	protected Map getVariables(IRuleContext rulectx) {
		
		Map variables = new HashMap();
		logger.info("******************METODO GETVARIABLES*******************");
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			ISignAPI signAPI = invesflowAPI.getSignAPI();
	
			int instancedStepId = rulectx.getInt("ID_INSTANCIA_PASO");
			IItem instancedStep = signAPI.getStepInstancedCircuit(instancedStepId);
			
			int documentId = instancedStep.getInt("ID_DOCUMENTO");
			IItem document = invesflowAPI.getEntitiesAPI().getDocument(documentId);
			logger.info("[AvisoFirmanteRule: getVariables()]id_documento. "+documentId);
			logger.info("[AvisoFirmanteRule: getVariables()]numexp. "+document.getString("NUMEXP"));
			logger.info("[AvisoFirmanteRule: getVariables()]descripcion. "+document.getString("DESCRIPCION"));
			
			
			variables.put("NUMEXP", document.getString("NUMEXP"));
			variables.put("DOCUMENT_NAME", document.getString("NOMBRE"));
			variables.put("DOCUMENT_DESC", document.getString("DESCRIPCION"));
			variables.put("DOCUMENT_DATE", TypeConverter.toString(document.getDate("FDOC"), TypeConverter.TIMESTAMPFORMAT));
			
			//Campo Asunto del expediente
			 IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		      IItem expediente = ExpedientesUtil.getExpediente(ctx, document.getString("NUMEXP"));
		      StringBuffer asunto = new StringBuffer("");
		      
		      if (expediente != null) {
		    	  asunto.append(expediente.getString("ASUNTO"));
		    	  asunto.append("\r");
		      }
		      
	        logger.info("[AvisoFirmanteRule: getVariables()]asunto. "+asunto.toString());
	        variables.put("ASUNTO", asunto.toString());
			
		} catch (Exception e) {
			logger.warn("Error al obtener la informaci�n del documento", e);
		}
		
		return variables;
	}
	
}

