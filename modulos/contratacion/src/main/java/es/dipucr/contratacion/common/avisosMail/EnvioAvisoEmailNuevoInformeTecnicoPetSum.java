package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioAvisoEmailNuevoInformeTecnicoPetSum extends DipucrEnviaDocEmailConAcuse {

	/**
	 * [Ticket #486 TCG](SIGEM creaci�n m�todo gen�rico para el env�o de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailNuevoInformeTecnicoPetSum.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
			 
			VAR_EMAILS = "EMAILS_DEPCONTRA_PETSUM";
			 
			contenido = "Tiene un nuevo tr�mite de Informe T�cnico en la Petici�n de Contrataci�n con el n�mero de expediente "+rulectx.getNumExp();
			asunto = "Tiene un nuevo tr�mite de Informe T�cnico en la Petici�n de Contrataci�n con el n�mero de expediente "+rulectx.getNumExp();
			
			conDocumento = false;
				 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepci�n en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepci�n en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
	
}
