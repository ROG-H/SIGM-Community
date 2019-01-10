package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinReintegroRule extends InitTaskAcuerdoDecretoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_REINTEGRO";
        strQueryDocumentos = 
            "DESCRIPCION = 'Propuesta de resoluci�n del expediente' OR " +
            "DESCRIPCION = 'Informe sin alegaciones' OR " +
            "DESCRIPCION = 'Informe con alegaciones'" ;        
        return true;
    }
}
