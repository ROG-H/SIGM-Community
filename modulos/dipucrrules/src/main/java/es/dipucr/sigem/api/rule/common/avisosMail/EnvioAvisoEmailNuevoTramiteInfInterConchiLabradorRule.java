package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;

public class EnvioAvisoEmailNuevoTramiteInfInterConchiLabradorRule extends DipucrEnviaDocEmailConAcuse{

	/**
	 * (SIGEM creaci�n m�todo gen�rico para el env�o de avisos por mail)
	 * [Manu Ticket #1181] Procedimiento de Recursos Administrativos de Servicios Sociales
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailNuevoTramiteInfInterConchiLabradorRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			numexp = rulectx.getNumExp();
				
			VAR_EMAILS = "Concepcion_Labrador_Moreno@dipucr.es";
			nombreNotif = "Concepci�n Labrador";
			contenido = "Tiene un nuevo Tr�mite de Informe Econ�mico en SIGEM a visualizar con el n�mero de expediente " + numexp;
			asunto = "[AL-SIGM] Tiene un nuevo Tr�mite de Informe Econ�mico en SIGEM a visualizar con el n�mero de expediente " + numexp;
			
			conDocumento = false;
		} catch (ISPACException e) {
			 logger.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + numexp + ". "+ e.getMessage(), e);
		}
		
		return true;
	}
}
