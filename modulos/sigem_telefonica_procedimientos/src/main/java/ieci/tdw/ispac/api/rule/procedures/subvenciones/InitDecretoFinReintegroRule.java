package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoFinReintegroRule extends InitDecretoSubvRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_REINTEGRO";
		STR_extracto = "Resoluci�n de reintegro de subvenci�n";
        return true;
    }
}
