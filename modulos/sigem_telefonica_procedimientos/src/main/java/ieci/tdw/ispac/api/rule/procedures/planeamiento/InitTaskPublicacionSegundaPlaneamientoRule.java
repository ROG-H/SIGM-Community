package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia tarea de Publicaci�n segunda del Procedimiento Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal
 *
 */

public class InitTaskPublicacionSegundaPlaneamientoRule extends InitTaskPublicacionPlaneamientoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PLAN_POM";
		STR_template = "Anuncio BOP aprobaci�n provisional POM";		
        return true;
    }
}
