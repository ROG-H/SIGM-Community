package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaIlegalParalizacionRule extends InitPropuestaIlegalRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_ILEGAL";
        strExtracto = "Resoluci�n de paralizaci�n de obras";
        strEstado = "ESTADO_PARALIZACION";
        strQueryDocumento = "DESCRIPCION = 'Decreto de paralizaci�n de obra - Obra ilegal'";        
        return true;
    }
}
