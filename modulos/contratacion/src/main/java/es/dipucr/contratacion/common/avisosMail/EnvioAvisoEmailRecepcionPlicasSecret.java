package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioAvisoEmailRecepcionPlicasSecret extends DipucrEnviaDocEmailConAcuse {

	/**
	 * [Ticket #486 TCG](SIGEM creaci�n m�todo gen�rico para el env�o de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailRecepcionPlicasSecret.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			VAR_EMAILS = "EMAIL_RECEPCION_PLICAS";
	        conDocumento = false;
			contenido = "Aprobado nuevo expediente de contrataci�n "+rulectx.getNumExp()+" inicio recepci�n de Plicas.";
			asunto = "Aprobado nuevo expediente de contrataci�n "+rulectx.getNumExp()+" inicio recepci�n de Plicas.";
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepci�n en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepci�n en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}
