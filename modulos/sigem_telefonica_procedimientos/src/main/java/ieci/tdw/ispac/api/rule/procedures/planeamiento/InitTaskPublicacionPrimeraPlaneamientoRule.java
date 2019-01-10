package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia tarea de Publicaci�n primera del Procedimiento Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal
 *
 */

public class InitTaskPublicacionPrimeraPlaneamientoRule extends InitTaskPublicacionPlaneamientoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PLAN_POM";
        strTemplate = "Anuncio BOP avance POM";        
        return true;
    }
}
