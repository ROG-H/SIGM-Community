package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinCalificacionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_CALIFICACION";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Providencia - Calificaci�n urban�stica' OR " +
			"DESCRIPCION = 'Informe t�cnico - Calificaci�n urban�stica' OR " +
			"DESCRIPCION = 'Informe jur�dico - Calificaci�n urban�stica'" ;		
        return true;
    }
}
