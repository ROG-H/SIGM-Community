package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;


/**
 * @author teresa
 * @date 18/11/2009
 * @prop�sito Si se ha especificado un motivo de rechazo en el tr�mite de Elaboraci�n del Pliego de cl�usulas Econ�mico-Administrativas
 * cuando se termina dicho tr�mite se genera autom�ticamente el tr�mite de Prescripciones t�cnicas y se env�a un correo de notificaci�n
 * a los trasladados.
 */
public class CreateTaskPrescripcionesTecnicasRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CreateTaskPrescripcionesTecnicasRule.class);

	protected String STR_TramPrescripcionesTecnicas = "Pres tecnicas";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Obtenemos los datos del expediente para comprobar si se debe crear o no el tr�mite de "Prescripciones t�cnicas"
			String motivoRechazo = null;
			String strQuery = "WHERE NUMEXP = '"+ rulectx.getNumExp() +"'";
			logger.debug("strQuery: "+strQuery);
	        //IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("SGN_NEGOCIADO", strQuery);
	        Iterator it = collection.iterator();
	        if (it.hasNext()){
	        	motivoRechazo = ((IItem)it.next()).getString("MOTIVO_RECHAZO");
	        }else{
	        	throw new ISPACInfo("No se han encontrado datos espec�ficos del expediente: "+rulectx.getNumExp());
	        }
	        
	        //Se inicia el tr�mite de "Prescripciones t�cnicas" si el campo "Motivo de rechazo" no est� vac�o
	        if (motivoRechazo != null && !motivoRechazo.equals("")){
	        	
	        	int stageId = rulectx.getStageId();//Id de la fase en el proceso (en ejecuci�n)
				int taskIdCat = 0;
				
				//Obtener el taskId (id del tr�mite) del tr�mite de "Prescripciones t�cnicas"
				
				//Obtenemos el id del tr�mite de "Prescripciones t�cnicas" en el Cat�logo a partir de su c�digo
				strQuery = "WHERE COD_TRAM = '"+ STR_TramPrescripcionesTecnicas +"'";
				logger.debug("strQuery: "+strQuery);
		        //IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
				collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	taskIdCat = ((IItem)it.next()).getInt("ID");
		        }else{
		        	throw new ISPACInfo("No se ha encontrado el tr�mite de Prescripciones t�cnicas.");
		        }
				
				//Obtenemos el id de la "Fase elaboraci�n pliego" en el procedimiento, no en el cat�logo
		        int idStagePcd = 0;
		        strQuery = "WHERE ID = '"+ stageId +"'";
				logger.debug("strQuery: "+strQuery);
		        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_FASES, strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	idStagePcd = ((IItem)it.next()).getInt("ID_FASE");
		        }else{
		        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
		        }
		        
				//Obtenemos el id del tr�mite de "Prescripciones t�cnicas" en el procedimiento, no en el cat�logo
		        int idTaskPcd = 0;
		        strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = '"+ taskIdCat +"'";
				logger.debug("strQuery: "+strQuery);
		        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	idTaskPcd = ((IItem)it.next()).getInt("ID");
		        }else{
		        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
		        }
		        
		        
				tx.createTask(stageId, idTaskPcd);
				
				
				
				//Una vez creado el tr�mite de "Prescripciones t�cnicas" se env�a un correo de notificaci�n
				//simpleMail();
				
				// 1. Obtener Participantes del expediente actual, con relaci�n "Trasladado"
				String numExp = rulectx.getNumExp();
	 			String sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '"+numExp+"' ORDER BY ID";
	 			logger.debug("sqlQueryPart: "+sqlQueryPart);
				IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);

				
				// Para cada participante seleccionado enviar email
				for (int i=0; i<participantes.toList().size(); i++)
				{
					IItem participante = (IItem) participantes.toList().get(i);
					{
						String direccionesCorreo = participante.getString("DIRECCIONTELEMATICA");
						if (direccionesCorreo != null)
						{
							StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
							while (tokens.hasMoreTokens()) 
							{
								String strDestinatario = tokens.nextToken();	
								if (participante!=null)
								{
									if (!strDestinatario.equals("")) 
									{
										EnviarCorreo(rulectx, strDestinatario, motivoRechazo, null);
										logger.debug("Correo enviado a los trasladados");
									}
								}
							}
						}

					}
				
				
				
				}		
				
					
	        
	        }

		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;

	}

	private void EnviarCorreo(IRuleContext rulectx, String strTo, String motivoRechazo, File attachment) throws ISPACException 
	{
		try
		{
			//Se cambia la configuraci�n del correo de ApplicationResources.properties a su archivo de configuraci�n por entidad
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			
			String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
			String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);
			// Variables comunes para el env�o de email
			//String cCorreoOrigen = "SIGEM-DCR";
			String strFrom = correoConfig.get(CorreoConfiguration.DECR_FROM);
			
			String strAsunto = "[SIGEM] Rechazo expediente de Convocatoria de contrataci�n "+rulectx.getNumExp();
			String strContenido = strAsunto + "</br></br>" + "El <b>motivo del rechazo</b> es el siguiente: </br>" + motivoRechazo;
			
			// Configurar correo
	        Properties props = new Properties();
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.host", strHost);
	        props.put("mail.smtp.port", strPort);
	        props.put("mail.smtp.auth", "true");
	        Authenticator auth = new SMTPAuthenticator();
	        Session mailSession = Session.getDefaultInstance(props, auth);
	        //mailSession.setDebug(true);
	        Transport transport = mailSession.getTransport();

			// Definir mensaje
			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(strFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
			message.setSubject(strAsunto);
			message.setContent(strContenido, "text/html; charset=ISO-8859-1");

			// Env�o del mensaje
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
		}
		catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("Error al enviar el correo electr�nico de Convocatoria",e);
        }
	}
	
	
    private class SMTPAuthenticator extends javax.mail.Authenticator {
    	public PasswordAuthentication getPasswordAuthentication() {
        	String username = Messages.getString("cUsr_mail");
        	String password = Messages.getString("cPwd_mail");
        	return new PasswordAuthentication(username, password);
        }
    }
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}


}
