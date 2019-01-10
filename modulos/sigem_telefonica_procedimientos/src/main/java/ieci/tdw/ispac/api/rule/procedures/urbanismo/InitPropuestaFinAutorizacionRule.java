package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaFinAutorizacionRule extends InitPropuestaSegregacionRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_AUTORIZACION";
        strExtracto = "Resoluci�n de licencia de autorizaci�n";
        return true;
    }
}
