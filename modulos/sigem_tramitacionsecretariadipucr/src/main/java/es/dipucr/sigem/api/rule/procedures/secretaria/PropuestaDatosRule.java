package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class PropuestaDatosRule implements IRule {

	private static final Logger logger = Logger.getLogger(PropuestaDatosRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
	       	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Obtener la sesion del expediente
			String organo = SecretariaUtil.getOrgano(rulectx);
			
			//Obtener el listado de propuestas
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() +"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        
	        Iterator it = collection.iterator();
	
	        while (it.hasNext()) {
	        	IItem item = ((IItem)it.next());
				if(organo.equals("JGOB")){
					
		            String orden = item.getString("ORDEN");
		            
		            StringBuffer result_vota = new StringBuffer("");
		            StringBuffer debate = new StringBuffer("");
		            StringBuffer informe = new StringBuffer("");
		            StringBuffer dictamen = new StringBuffer("");
		            
		            if(orden.equals("0010")){
		            	//Modificacion de la propuesta borrador
		            	String sesion = SecretariaUtil.getSesion(rulectx);
		        		//logger.warn("sesion "+sesion);
		        		if(sesion.equals("ORD") || sesion.equals("EXTR")){
			            	result_vota.append("No habi�ndose formulado observaci�n o sugerencia alguna, la Junta de Gobierno, en votaci�n ordinaria " +
			            			"y por .... unanimidad de los asistentes, con .... nueve votos favorables, ninguno en contra y ninguna abstenci�n, acuerda" +
			            			" dar su aprobaci�n al acta de la mencionada sesi�n, .... sin enmienda alguna, procediendo su definitiva transcripci�n " +
			            			"reglamentaria conforme a lo dispuesto en el art. 199 del Reglamento de Organizaci�n, Funcionamiento y R�gimen " +
			            			"Jur�dico de la Entidades Locales y normas concordantes.");
		        		}
		        		if(sesion.equals("EXUR")){
		        			result_vota.append("La Junta de Gobierno, en votaci�n ordinaria y por ... unanimidad de los asistentes, con ... " +
		        					"nueve votos favorables, ninguno en contra" +
		        					" y ninguna abstenci�n, acuerda ratificar la urgencia de la sesi�n.");
		        		}
		            	item.set("RESULTADO_VOTACION", result_vota);
		            }
		            else{
		            	//Modificar el resto de propuesta 
		            	
		            	//Columna Informe
		            	informe.append("Visto el informe emitido el d�a .... 20 de abril del 2010 por la ....(Jefe de Servicio de ...., " +
		            			"la Intervenci�n de Fondos...)(En su caso: debe tacharse el p�rrafo si no procede).");
		            	
		            	//columna dictamen
		            	dictamen.append("Visto el dictamen emitido el d�a veinte de abril de 2010 por la Comisi�n Informativa permanente " +
	        			"de Infraestructura. (En su caso: debe tacharse el p�rrafo si no procede).");
		            	
		            	//columan debate
		            	debate.append("Ning�n integrante de la Junta de Gobierno solicita la apertura de debate. (En su caso: debe tacharse el p�rrafo si " +
		            			"no procede) o bien.\n\tAbierto el debate, ....(aqu� ir�a el texto del debate si existiera).");
		            	
		            	//columna resultado de la votacion
		            	result_vota.append("Sometido el asunto a votaci�n, la Junta de Gobierno, en votaci�n ordinaria y por unanimidad de los asistentes, " +
		            			"con ....nueve votos a favor, .... ning�n voto en contra y ....ninguna abstenci�n, adopta el siguiente acuerdo:\n" +
		            			"\t Aprobar la transcrita propuesta, sin enmienda alguna.");
		            	
		            	item.set("INFORMES", informe.toString());
		            	item.set("DICTAMEN", dictamen.toString());
		            	item.set("DEBATE", debate.toString());
		            	item.set("RESULTADO_VOTACION", result_vota);
		            }
	
		            item.store(cct);
				}
				if(organo.equals("PLEN")){
					
				}
				if(organo.equals("MESA")){
					
				}
				if(organo.equals("COMI")){
					
				}
	        }
			return new Boolean(true);
		
    	} catch(Exception e) {
    		logger.error("No se ha podido iniciar la propuesta. " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        }
    }

	public void cancel(IRuleContext arg0) throws ISPACRuleException {	
		
	}
}
