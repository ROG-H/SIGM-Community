package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;

public class EnvioAvisoEmailNuevoTramiteInfInterEloyContratacionRule extends DipucrEnviaDocEmailConAcuse{

	/**
	 * (SIGEM creaci�n m�todo gen�rico para el env�o de avisos por mail)
	 * [Manu Ticket #1181] Procedimiento de Recursos Administrativos de Servicios Sociales
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailNuevoTramiteInfInterEloyContratacionRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			numexp = rulectx.getNumExp();
				
			VAR_EMAILS = "eloy_sanz@dipucr.es";
			nombreNotif = "Eloy Sanz Ruedas";
			contenido = "Tiene hecho la reserva de cr�dito del expediente: " + numexp;
			asunto = "[AL-SIGM] Tiene hecho la reserva de cr�dito del expediente: " + numexp;
			
			conDocumento = false;
		} catch (ISPACException e) {
			 logger.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + numexp + ". "+ e.getMessage(), e);
		}
		
		return true;
	}
}
