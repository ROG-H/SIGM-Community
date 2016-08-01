package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.bop.BopFacturasUtil;

/**
 * Asigna el n�mero de anuncio a asociar al expediente de BOP - Solicitud de inserci�n de anuncio.
 * Si el a�o actual coincide con el a�o de la tabla de validaci�n lo obtiene incrementando el �ltimo n�mero de anuncio utilizado y lo 
 * actualiza en la tabla de validaci�n global de contadores.
 * Si el a�o actual no coincide con el a�o de la tabla de validaci�n (a�o nuevo) asigna el nuevo a�o en la tabla de validaci�n, reinicia los
 * contadores y asigna el primer n�mero de anuncio del a�o.
 *
 */
public class GetNumFacturaBopRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(GetNumFacturaBopRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
			// Abrir transacci�n
	        cct.beginTX();
	        
	        String numFactura = null;
	        String numFactura_aux = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator<?> it = null;
	        IItem itemSolicitud = null;
	        String numexp = null;
	        String tipoLiquidacion = null;//[dipucr-Felipe #1311]
	        
	        //INICIO [20.05.2010 eCenpri-Felipe; Ticket#21]
	        //Se cambia por completo el m�todo para que tire de la clase com�n FacturasUtil
	        //Se inserta la factura en la tabla BOP_FACTURAS
	        
	        //Obtenemos el n�mero de expediente
	        numexp = cct.getStateContext().getNumexp();
	        
	        // Se comprueba si ya tiene n�mero de factura el anuncio
	        // En caso afirmativo, se devuelve ese n�mero de factura
	        strQuery = "WHERE NUMEXP = '"+ numexp +"'";
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext()){
	        	itemSolicitud = (IItem)it.next();
	        	numFactura_aux = itemSolicitud.getString("NUM_FACTURA");
	        	
	        	if ((numFactura_aux != null) && (!numFactura_aux.equals("")))
	        	{
	        		logger.warn("N�mero de factura ya existente. Se pone el mismo: " + numFactura_aux);
	        		return numFactura_aux;
	        	}
	        	//Si no tiene n�mero de factura se genera uno nuevo
	        	else{
	        		//Obtenemos el n�mero de factura
		        	tipoLiquidacion = itemSolicitud.getString("TIPO_FACTURACION"); //[dipucr-Felipe #1311]
	    	        numFactura = BopFacturasUtil.getNumFactura(rulectx, tipoLiquidacion);
	    	        logger.warn("Se genera el nuevo n�mero de factura: " + numFactura);
	    	        
    	           	itemSolicitud.set("NUM_FACTURA", numFactura);
    	        	itemSolicitud.store(cct);
    	        	
    	        	IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
    	        	
    	        	//Se inserta un registro con el n�mero de factura en la tabla bop_facturas
		        	BopFacturasUtil.insertarFactura(cct, entitiesAPI, numFactura, "Factura sin cr�dito individual", numexp,
		        			itemExpediente.getString("NIFCIFTITULAR"), itemExpediente.getString("IDENTIDADTITULAR"),
		        			itemSolicitud.getDouble("COSTE"), itemSolicitud.getDouble("COSTE_TOTAL"), new Date());
	        	}
	        }
	        //FIN [20.05.2010 eCenpri-Felipe; Ticket#21]
	        
	        return numFactura;
	        
	    } catch (Exception e) {
	    	
			// Si se produce alg�n error se hace rollback de la transacci�n
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error("Error al obtener el n�mero de factura. " + e1.getMessage(), e1);
			}
	    	
	        throw new ISPACRuleException("Error al obtener el n�mero de factura.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
