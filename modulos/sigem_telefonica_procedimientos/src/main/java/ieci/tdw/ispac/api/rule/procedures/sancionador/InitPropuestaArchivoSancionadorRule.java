package ieci.tdw.ispac.api.rule.procedures.sancionador;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia propuesta archivo Procedimiento Sancionador
 *
 */

public class InitPropuestaArchivoSancionadorRule extends InitPropuestaSancionadorRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_SANCIONADOR";
		STR_extracto = "Resoluci�n de archivo del procedimiento Sancionador";
        return true;
    }
}
