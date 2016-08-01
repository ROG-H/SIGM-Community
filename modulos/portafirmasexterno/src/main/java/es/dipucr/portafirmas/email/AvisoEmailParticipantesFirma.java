package es.dipucr.portafirmas.email;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;


public class AvisoEmailParticipantesFirma  implements IRule {
	
	
	private static final Logger logger = Logger.getLogger(AvisoEmailParticipantesFirma.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try
		{
			/**
			 * Variables que se utilizar�n para insertar en la bbdd los datos 
			 * sobre el env�o correcto o incorrecto del email.
			 * */

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //---------------------------------------------------------------------------------------------- 
	        logger.warn("INICIO TrasladarSubsanacionInforme. ");
        	
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),  "ROL='INT'", "");
			
			if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
				for (int i=0; i<participantes.toList().size(); i++)
				{

					//[eCenpri-Felipe Ticket #306] Algunos servidores interpretan este from como span
					//String cCorreoOrigen = "SIGEM-DCR";					
					String cAsunto= "[SIGEM] Firma de documentos en el Portafirmas: "+rulectx.getNumExp();
					StringBuffer cContenido =  new StringBuffer();
					cContenido.append("La Diputaci�n Provincial de Ciudad Real ha configurado un nuevo servicio");
					cContenido.append("electr�nico que permite la firma telem�tica de convenios, contratos o documentos similares. ");
					cContenido.append("Con ello, se evita su remisi�n por correo ordinario y, por tanto, se ahorran gastos y reduces plazos de tramitaci�n. </br>");
					cContenido.append("Para firmar el documento, deber� seguir los siguientes paso:</br>");
					cContenido.append("1� Acceder al <a href='https://portafirmas.redsara.es/pf/'>Portafirmas</a>.");
					cContenido.append("(Es preciso validarse y firmar con dni-e o certificado FNMT).</br>");
					cContenido.append("2� El primer acceso requiere que se indique el email del firmante (en el que recibir� en lo sucesivo los avisos correspondientes).</br>");
					cContenido.append("3� Entrar a la aplicaci�n y firmar siguiendo las correspondientes ");
					cContenido.append("<a href='https://sede.dipucr.es/documentacion'>instrucciones</a></br>");
					cContenido.append("En caso de duda o dificultad puede llamar al tel�fono 926292575 extensi�n: 292, 311, 293, 367, 365.");
					cContenido.append(" O mandar una <a href='https://sede.dipucr.es/soporte'>incidencia</a> y le responder�n con la mayor brevedad posible.");
					
					IItem participante = (IItem) participantes.toList().get(i);
					String emailNotif = participante.getString("DIRECCIONTELEMATICA");
					
					if (emailNotif != null)
					{
						StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
						while (tokens.hasMoreTokens()) 
						{
							String cCorreoDestino = tokens.nextToken();	
							if (participante!=null)
							{
					        	if (!cCorreoDestino.equals("")) 
					        	{
					        		logger.warn("direccion telematica. "+cCorreoDestino);
						        	// Confeccionar el email
									MailUtil.enviarCorreo(rulectx, cCorreoDestino, cAsunto, cContenido.toString());	
					        	}
							}
						}
					}
				}
			}
			logger.warn("FIN TrasladarSubsanacionInforme. ");
			
			return new Boolean(true);
		}
		catch(Exception e) 
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
