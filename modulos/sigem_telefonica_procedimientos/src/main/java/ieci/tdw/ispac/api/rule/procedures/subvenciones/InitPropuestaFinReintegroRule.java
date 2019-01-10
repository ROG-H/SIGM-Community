package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaFinReintegroRule extends InitPropuestaSubvRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_REINTEGRO";
        strExtracto = "Propuesta de resoluci�n de reintegro de subvenci�n";
        return true;
    }
}
