package es.dipucr.sigem.api.rule.common.convocatorias;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class DipucrEnviaJustificacionConvAsoMail extends DipucrEnviaDocEmailConAcuse {

	private static final Logger logger = Logger.getLogger(DipucrEnviaJustificacionConvAsoMail.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		logger.info("INICIO - " + this.getClass().getName());
				
		VAR_EMAILS = "EMAIL_JUSTIF_SUBV_ASOCIA"; //Variable de sistema de la que tomamos los emails

		contenido = "Informe de Justificaci�n de Convocatoria de Subvenciones: \n";			
			
		asunto= "[AL-SIGM] Traslado del Informe de Justificaci�n. ";			
		conDocumento = true;

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

}
