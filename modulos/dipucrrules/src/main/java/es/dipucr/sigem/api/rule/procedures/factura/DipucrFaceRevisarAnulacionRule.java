package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;


/**
 * #1112 Procedimiento SIGEM para Factur@-FACe - Anulaci�n
 * Valida si se acepta o rechaza la anulaci�n de la factura
 * @author dipucr-Felipe
 * @since 27.05.2014
 */
public class DipucrFaceRevisarAnulacionRule implements IRule 
{
	private static final Logger logger = Logger.getLogger(DipucrFaceRevisarAnulacionRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validamos que se hayan rellenado los datos de aceptaci�n/rechazo
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			IItemCollection collection = null;	
			String numexp = rulectx.getNumExp();
			
			//Obtenemos los datos de la revision de facturas
			collection = entitiesAPI.getEntities("ANULACION_FACTURAE", numexp);
			if (!collection.next()){
				rulectx.setInfoMessage("No se puede terminar el tr�mite. Es necesario " +
						"rellenar los datos de aceptaci�n/rechazo en la pesta�a 'Anulaci�n Facturae'.");
				return false;
			}
			
			IItem itemRevisarAnulacion = (IItem) collection.iterator().next();
			//Dependiendo de si la anulaci�n ha sido o no aceptada
			String aceptadaAnulacion = itemRevisarAnulacion.getString("ACEPTAR_ANULACION");
			if (StringUtils.isEmpty(aceptadaAnulacion)){
				rulectx.setInfoMessage("No se puede terminar el tr�mite. Es necesario rellenar " +
						"los campos obligatorios en la pesta�a 'Anulaci�n Facturae'.");
				return false;
			}
			else{				
				String comentarios = itemRevisarAnulacion.getString("COMENTARIOS");
				if (aceptadaAnulacion.equals("NO") && StringUtils.isEmpty(comentarios)){
					rulectx.setInfoMessage("Si la anulaci�n no se acepta es necesario rellenar "
							+ "comentarios adicionales en la pesta�a 'Anulaci�n Facturae'.");
					return false;
				}
			}	
		}
		catch (Exception e) {
			String error = "Error al revisar la anulaci�n de la factura en el expediente: " 
						+ rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return true;
		
	}
	
	/**
	 * Revisa el campo Aceptar/Rechazar y en funci�n de esto realiza ciertas tareas
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			//*********************************************
			
			String numexp = rulectx.getNumExp();

			//Obtenemos los datos de la anulaci�n
			IItem itemAnulacion = DipucrFaceFacturasUtil.getAnulacionEntity(cct, numexp);
			String nreg = itemAnulacion.getString("NREG_FACE");
			
			//Dependiendo de si la anulaci�n ha sido o no aceptada
			String aceptadaAnulacion = itemAnulacion.getString("ACEPTAR_ANULACION");
			String comentarios = itemAnulacion.getString("COMENTARIOS");
			if (null == comentarios) comentarios = "";
			
			//ACEPTADA ANULACI�N
			if (aceptadaAnulacion.equals("SI")){
				DipucrFaceFacturasUtil.cambiarEstadoAnulacionFactura
					(cct, nreg, EstadosFace.ACEPTADA_ANULACION, comentarios);
				
				//TODO: Paramos todos los circuitos de firma en el expediente padre??
			}
			//NO ACEPTADA
			else{
				DipucrFaceFacturasUtil.cambiarEstadoAnulacionFactura
					(cct, nreg, EstadosFace.RECHAZADA_ANULACION, comentarios);
			}
			
//			ExpedientesUtil.avanzarFase(cct, numexp);
		}
		catch (Exception e) {
			String error = "Error al revisar la anulaci�n de la factura en el expediente: " 
						+ rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
