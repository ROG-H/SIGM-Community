package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * [Ticket #990 Teresa] CONTRATACION-SIGEM a�adir tr�mite para almacenar documento de 
 * solicitud de informe t�cnico en el procedimiento de petici�n
 * **/
public class AnadirDocSolInfTecnPCPeticionContratacion extends AnadirDocExpedienteTramite{

	private static final Logger logger = Logger.getLogger(AnadirDocSolInfTecnPCPeticionContratacion.class);
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

	        //Obtengo el numexp del procedimiento de Petici�n de contrataci�n
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petici�n Contrato'";
	        logger.warn("numexpContratacion "+rulectx.getNumExp());
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPeticionContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        
		}
		 catch(Exception e) {
			 logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
	     }

		codigoTramite = "SOLINFTECN";
		nombreDescripcionDoc = "Solicitud de Informe sobre Documentaci�n T�cnica";
		return true;
	}

}
