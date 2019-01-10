package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @prop�sito Valida que no se haya creado anteriormente un tr�mite de Preparaci�n de firmas en la fase actual
 * y que se haya cerrado el primer tr�mite de Creaci�n del Decreto
 */
public class ValidateTaskPreparacionRule implements IRule{
    

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());
            
            // Se compara el tama�o > 2, ya que itemCollection contiene el elemento que estamos intentando crear 
            if (itemCollection!=null && itemCollection.toList().size()>3){
                rulectx.setInfoMessage("Existe m�s de un tr�mite asociado a la fase actual");
                return false;
                
            } else {
                Iterator<?> iteratorCollection = itemCollection.iterator();
                IItem task = null;
                boolean creacionCerrado = false;
                int numCreacion = 0;
                int numPreparacion = 0;
                while (iteratorCollection.hasNext()){
                    task = (IItem)iteratorCollection.next();
                    
                    if (task.getString("NOMBRE").equalsIgnoreCase(TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"))){
                        numCreacion++;
                        if (task.getInt("ESTADO") == 3){
                            creacionCerrado = true;
                        }else{
                            rulectx.setInfoMessage("No se ha terminado el tr�mite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                        }
                    }else if ("Preparaci�n de firmas y traslado del Decreto".equalsIgnoreCase(task.getString("NOMBRE"))){
                        numPreparacion++;
                    }
                }
                
                if (creacionCerrado && numCreacion == 1 && numPreparacion == 1){
                    return true;    
                }else{
                    if (!creacionCerrado && numCreacion == 1){
                        rulectx.setInfoMessage("No se ha terminado el tr�mite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                    }else if (numCreacion == 0){
                        rulectx.setInfoMessage("No se ha creado el tr�mite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                    }else if (numCreacion > 1 || numPreparacion > 1){
                        rulectx.setInfoMessage("S�lo se puede crear un tr�mite de cada tipo");
                    }
                    return false;
                }
                
                
            }
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el n�mero de tr�mites de la fase actual", e);
        } 
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
