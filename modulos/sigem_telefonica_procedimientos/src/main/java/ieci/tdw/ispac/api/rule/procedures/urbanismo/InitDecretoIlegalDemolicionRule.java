package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalDemolicionRule extends InitDecretoIlegalRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_ILEGAL";
        strExtracto = "Resoluci�n de orden de demolici�n";
        strEstado = "ESTADO_DEMOLICION";
        strQueryDocumento = "DESCRIPCION = 'Decreto de demolici�n - Obra ilegal'";        
        return true;
    }
}
