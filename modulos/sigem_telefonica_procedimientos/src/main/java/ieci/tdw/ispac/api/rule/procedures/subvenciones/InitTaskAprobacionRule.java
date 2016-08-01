package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskAprobacionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_CONVOCATORIA";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Propuesta de aprobaci�n de convocatoria' OR " +
			"DESCRIPCION = 'Informe jur�dico aprobaci�n convocatoria' OR " +
			"DESCRIPCION = 'Informe intervenci�n aprobaci�n convocatoria'" ;		
        return true;
    }
}
