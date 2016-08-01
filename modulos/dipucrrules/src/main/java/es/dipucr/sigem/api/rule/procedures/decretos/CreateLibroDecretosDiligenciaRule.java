package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;


/**
 * [eCenpri-Felipe ticket #903]
 * Clase para la generaci�n de la diligencia previa al libro de decretos
 * @author Felipe
 * @since 06.06.13
 */
public class CreateLibroDecretosDiligenciaRule implements IRule 
{
	/**
	 * Clase de libro de decretos
	 */
	private LibroDecretos libro = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validaci�n y obtenci�n de los par�metros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		libro = new LibroDecretos();
		return libro.validarFechas(rulectx);
	}
	
	/**
	 * Generaci�n del libro de decretos
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return libro.generarDiligencia(rulectx);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
