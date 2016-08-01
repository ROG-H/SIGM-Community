package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskResolucionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_CONVOCATORIA";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Propuesta de resoluci�n convocatoria' OR " +
			"DESCRIPCION = 'Informe del servicio resoluci�n convocatoria' OR " +
			"DESCRIPCION = 'Informe intervenci�n resoluci�n convocatoria' OR " +		
			"NOMBRE = 'Informe jur�dico'";
        return true;
    }
}
