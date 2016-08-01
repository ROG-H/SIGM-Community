package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia propuesta aprobaci�n definitiva Procedimiento Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal
 *
 */

public class InitPropuestaDefinitivaPlaneamientoRule extends InitPropuestaPlaneamientoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PLAN_POM";
		STR_extracto = "Resoluci�n de aprobaci�n definitiva del procedimiento de Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal";
        return true;
    }
}
