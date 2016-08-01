package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalDemolicionRule extends InitDecretoIlegalRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_ILEGAL";
		STR_extracto = "Resoluci�n de orden de demolici�n";
		STR_estado = "ESTADO_DEMOLICION";
		STR_queryDocumento = "DESCRIPCION = 'Decreto de demolici�n - Obra ilegal'";		
        return true;
    }
}
