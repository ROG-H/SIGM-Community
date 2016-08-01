package ieci.tdw.ispac.api.rule.procedures.subvencionNominativaEntidadesLocales;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 20/03/2009
 * @prop�sito Valida que se haya creado almenos uno de los tr�mites de pago antes de crear el tr�mite de Informe final
 */
public class ValidateTasksPagoRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());
			//IItemCollection itemCollection = entitiesAPI.getExpedientTasks(rulectx.getNumExp());
			
			if (itemCollection != null){ 
				IItem item = null;
				String nombre = null;
				int posicion = -1;
				Iterator it = itemCollection.iterator();
				while (it.hasNext()){
					item = (IItem)it.next();
					nombre = item.getString("NOMBRE");
					posicion = nombre.indexOf("Pago");
					if (posicion > -1){
						//Se ha creado un tr�mite de Pago de la subvenci�n
						return true;
					}
				}
				rulectx.setInfoMessage("Debe crear previamente un tr�mite de Pago de la subvenci�n.");
				return false;
			}else{
				rulectx.setInfoMessage("Debe crear previamente un tr�mite de Pago de la subvenci�n.");
				return false;
			}
			
			
			/*
			// Se compara el tama�o > 1, ya que itemCollection contiene el elemento que estamos intentando crear 
			if (itemCollection!=null && itemCollection.toList().size()>1){
				rulectx.setInfoMessage("S�lo se puede crear un tr�mite para la fase actual");
				return false;
			}else{
				return true;
			}
			*/
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar los tr�mites de pago de la subvenci�n", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
