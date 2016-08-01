package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Asigna el n�mero de anuncio a asociar al expediente de BOP - Solicitud de inserci�n de anuncio.
 * Si el a�o actual coincide con el a�o de la tabla de validaci�n lo obtiene incrementando el �ltimo n�mero de anuncio utilizado y lo 
 * actualiza en la tabla de validaci�n global de contadores.
 * Si el a�o actual no coincide con el a�o de la tabla de validaci�n (a�o nuevo) asigna el nuevo a�o en la tabla de validaci�n, reinicia los
 * contadores y asigna el primer n�mero de anuncio del a�o.
 *
 */
public class GetNumAnuncioSolicitudRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		int longitudNumAnuncio = 10;
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
			// Abrir transacci�n
	        cct.beginTX();
	        
	        String numAnuncioSolicitud = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        
	        //Obtenemos el valor del A�o de la tabla de validaci�n global
	        strQuery = "WHERE VALOR = 'A�o'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        it = collection.iterator();
	        int iAnio = -1;
	        if (it.hasNext()){
	        	item = (IItem)it.next();
	        	iAnio = Integer.parseInt(item.getString("SUSTITUTO"));
	        }
	        
	        if (iAnio > -1){
		        //Lo comparamos con el a�o actual (si fueran distintos hay que reiniciar los contadores ya que acaba de comenzar un nuevo a�o)
		        Calendar gc = new GregorianCalendar();
		        int iAnioActual = gc.get(Calendar.YEAR);
		        
		        if(iAnio == iAnioActual){
			        strQuery = "WHERE VALOR = 'num_anuncio_sol'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        String auxNumAnuncioSolicitud = null;
			        int iNumAnuncioSolicitud =  -1;
			        if (it.hasNext()){
			        	item = (IItem)it.next();
			        	iNumAnuncioSolicitud = Integer.parseInt(item.getString("SUSTITUTO"))+1;
			        	auxNumAnuncioSolicitud = String.valueOf(iNumAnuncioSolicitud);
			        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumAnuncio d�gitos
			        	while (auxNumAnuncioSolicitud.length() < longitudNumAnuncio){
			        		auxNumAnuncioSolicitud = "0"+auxNumAnuncioSolicitud;
			        	}
			        	numAnuncioSolicitud = iAnio + auxNumAnuncioSolicitud;
			        	//Actualizar el �ltimo n�mero de anuncio utilizado en la tabla de validaci�n global
			        	item.set("SUSTITUTO", iNumAnuncioSolicitud);
			        	item.store(cct);
			        }
			        if (iNumAnuncioSolicitud > -1){
			        	
			        	strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
				        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
				        it = collection.iterator();
				        if (it.hasNext()){
				        	item = (IItem)it.next();
				        	//Actualizar el n�mero de anuncio en el expediente de Solicitud de anuncio
				        	item.set("NUM_ANUNCIO", numAnuncioSolicitud);
				        	item.store(cct);
				        }
			        }

		        }else{
		        	//Reiniciar los contadores ya que acaba de comenzar un nuevo a�o y posteriormente asignar el primer n�mero de anuncio
		        	//	al expediente actual
		        	//Asignar el valor del a�o nuevo en la tabla de validaci�n
		        	item.set("SUSTITUTO", iAnioActual);
		        	item.store(cct);
		        	
			        //Reiniciar el valor de los contadores en la tabla de validaci�n
			        strQuery = "WHERE VALOR = 'num_bop' OR VALOR = 'num_pagina' OR VALOR = 'num_anuncio' OR VALOR = 'num_anuncio_sol'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        while (it.hasNext()){
			        	item = (IItem)it.next();
			        	if(item.getString("VALOR").equalsIgnoreCase("num_anuncio_sol")){
			        		item.set("SUSTITUTO", 1);
			        		item.store(cct);
			        	}else{
			        		item.set("SUSTITUTO", 0);
			        		item.store(cct);
			        	}
			        }
			        
			        //Asignar el primer n�mero de anuncio
		        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumAnuncio d�gitos
		        	String auxNumAnuncioSolicitud = "1";
			        while (auxNumAnuncioSolicitud.length() < longitudNumAnuncio){
		        		auxNumAnuncioSolicitud = "0"+auxNumAnuncioSolicitud;
		        	}
			        
		        	strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
			        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
			        it = collection.iterator();
			        if (it.hasNext()){
			        	item = (IItem)it.next();
			        	item.set("NUM_ANUNCIO", iAnioActual + auxNumAnuncioSolicitud);
			        	item.store(cct);
			        }
			        
		        }
		        
	        }
	        
	        return numAnuncioSolicitud;
	        
	    } catch (Exception e) {
	    	
			// Si se produce alg�n error se hace rollback de la transacci�n
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				e1.printStackTrace();
			}
	    	
	        throw new ISPACRuleException("Error al obtener el n�mero de anuncio de solicitud.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
