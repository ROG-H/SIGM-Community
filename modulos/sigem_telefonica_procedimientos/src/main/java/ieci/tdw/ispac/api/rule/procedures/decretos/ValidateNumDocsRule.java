package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @date 05/12/2008
 * @prop�sito Valida que s�lo se haya adjuntado un documento en el tr�mite actual
 */
public class ValidateNumDocsRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
			
			if (itemCollection!=null && itemCollection.toList().size()>1){
				rulectx.setInfoMessage("S�lo se puede adjuntar un Decreto para el tr�mite actual");
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el n�mero de documentos del tr�mite actual", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
