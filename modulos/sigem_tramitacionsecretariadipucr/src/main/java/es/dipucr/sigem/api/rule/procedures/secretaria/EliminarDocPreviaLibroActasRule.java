package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;


/**
 * [eCenpri-Felipe #911]
 * Eliminaci�n del documento previo de Libro de Actas del primer tr�mite
 * @author Felipe
 * @since 20.06.13
 */
public class EliminarDocPreviaLibroActasRule implements IRule 
{
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * C�digo que ejecutar� la regla
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		LibroActas libro = new LibroActas();
		return libro.eliminarPreviaLibroActas(rulectx);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
