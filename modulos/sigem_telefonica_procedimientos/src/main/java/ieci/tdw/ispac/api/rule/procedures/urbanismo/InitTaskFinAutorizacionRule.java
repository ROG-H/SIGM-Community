package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinAutorizacionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_AUTORIZACION";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Providencia - Autorizaci�n diversa' OR " +
			"DESCRIPCION = 'Informe t�cnico - Autorizaci�n diversa' OR " +
			"DESCRIPCION = 'Informe jur�dico - Autorizaci�n diversa'" ;		
        return true;
    }
}
