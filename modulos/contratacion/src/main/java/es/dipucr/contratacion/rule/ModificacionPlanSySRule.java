package es.dipucr.contratacion.rule;

import java.sql.Timestamp;
import java.util.Date;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ModificacionPlanSySRule extends PlanSeguridadSaludRule{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
		
			//calculo el n�mero de certificaci�n de obra por el n�mero de expedientes relacionados que son Certificaci�n de obra
			String strQuery = "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "' AND RELACION LIKE 'Modificaci�n del Plan de SyS - %'";
			IItemCollection collectExpRel = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
			relacion = "Modificaci�n del Plan de SyS - "+(collectExpRel.toList().size()+1)+" - "+FechasUtil.getFormattedDate(new Date());
			
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
			
		return true;
	}
}
