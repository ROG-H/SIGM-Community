package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * [eCenpri-Felipe ticket #1039]
 * Regla que controla, al iniciar el expediente, que no existan 
 * m�s expedientes abiertos del mismo tipo
 * @author Felipe
 * @since 24.01.14
 */
public class DipucrControlarExpedienteUnicoRule implements IRule
{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	IClientContext cct = rulectx.getClientContext();
        IInvesflowAPI invesflowAPI = cct.getAPI();
    	
    	try{
        	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
        	
    		//Sacamos el id del procedimiento en el cat�logo
        	IItem itemExpedient = entitiesAPI.getExpedient(rulectx.getNumExp());
    		int idPcd = itemExpedient.getInt("ID_PCD");
    		
    		//Vemos que no hay ning�n procedimiento abierto
    		//La forma m�s segura es comprobar que no existe ning�n registro para
    		//ese id_pcd en la tabla spac_fases. Otra opci�n hubiera sido mirar en
    		//spac_expedientes por fcierre, o en spac_procesos con state = 1
    		String query = "WHERE ID_PCD = " + idPcd;
    		IItemCollection collection = entitiesAPI.queryEntities("SPAC_FASES", query);
    		
    		//Teniendo en cuenta la fase del procedimiento que estamos creando (1), no puede haber m�s
    		if (collection.toList().size() > 1){
    			rulectx.setInfoMessage("S�lo es posible crear un expediente de este tipo");
    			return false;
    		}
    		else{
    			return true;
    		}
    	}
    	catch(Exception e){
    		throw new ISPACRuleException("Error al comprobar que el expediente es �nico", e);
    	}
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {        
    	return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {

    }
}
