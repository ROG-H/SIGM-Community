package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * 
 * @author teresa
 * @date 30/03/2010
 * @prop�sito Modificar el campo Transici�n de un expediente con el valor de la transici�n anterior
 *  y restar la ejecuci�n de la �ltima transici�n
 */
public class DeleteStatusTaskPlaneamientoRule implements IRule {
    
    private static String strEntidad = "PLAN_POM";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            IItem exp = null;
            String numExp = rulectx.getNumExp();
            IItemCollection collExps = entitiesAPI.getEntities(strEntidad, numExp);
            Iterator<?> itExps = collExps.iterator();
            String transicion = null;
            String transicionAnterior = null;
            
            if (itExps.hasNext()){
                exp = (IItem)itExps.next();
                transicionAnterior = exp.getString("TRANSICION_ANTERIOR");
                transicion = exp.getString("TRANSICION");
                if (ValidateStatusTaskPlaneamientoRule.isTramiteIniciado(transicion)){
                    if (transicionAnterior != null){
                        
                        //String inicio = transicion.substring(0, 5);
                        String inicio = ValidateStatusTaskPlaneamientoRule.getPrimerTramite(transicion);
                        //String fin = transicion.substring(6, 11);
                        String fin = ValidateStatusTaskPlaneamientoRule.getSegundoTramite(transicion);
                        
                        IItem itTransicion = null;
                        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin +"'";
                        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
                        Iterator<?> itTransiciones = collTransiciones.iterator();
                        if (itTransiciones.hasNext()){
                            //Decrementar el n�mero de ejecuciones de esa transici�n
                            itTransicion = (IItem)itTransiciones.next();
                            int ejecuciones = itTransicion.getInt("EJECUCIONES");
                            itTransicion.set("EJECUCIONES", ejecuciones-1);
                            itTransicion.store(cct);
                            
                            //Actualizar la transici�n actual (la transici�n anterior no se actualiza)
                            transicion = transicionAnterior;
                            exp.set("TRANSICION", transicion);
                            exp.store(cct);
                        }else{
                            throw new ISPACInfo("No se ha encontrado la �ltima transici�n");
                        }
                    }else{
                        throw new ISPACInfo("Estado anterior no v�lido");
                    }
                }else{
                    throw new ISPACInfo("Estado err�neo del expediente");
                }
            }else{
                throw new ISPACInfo("No se ha inicializado el estado del expediente");
            }
            
        }catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}
