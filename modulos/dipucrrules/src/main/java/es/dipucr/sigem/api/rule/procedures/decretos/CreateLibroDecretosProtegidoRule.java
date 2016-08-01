package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import com.lowagie.text.pdf.PdfWriter;


/**
 * [eCenpri-Felipe ticket #164]
 * Clase para la generaci�n del libro de decretos
 * Se rehace la clase de Telef�nica por tratarse de un c�digo ininteligible
 * Se pasa el c�digo a una Regla en vez de un action, por ser m�s c�modo 
 * @author Felipe
 * @since 05.10.2010
 */
public class CreateLibroDecretosProtegidoRule implements IRule 
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
		return libro.generarLibro(rulectx, true, "sigem", PdfWriter.HideToolbar);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
