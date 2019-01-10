package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

/**
 * 
 * @author teresa
 * Inicia solicitud BOP primera del Procedimiento Aprobaci�n y Modificaci�n del Plan de Ordenaci�n
 *
 */

public class InitSolicitudBopPrimeraPlaneamientoRule extends InitSolicitudBopPlaneamientoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PLAN_POM";
        strBOPEntidad = "";
        strBOPUrgencia = "Normal";
        strBOPSumario = "Publicaci�n primera de expediente de Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal";
        strBOPObservaciones = "Solicitud autom�tica desde expediente: Aprobaci�n y Modificaci�n del Plan de Ordenaci�n Municipal";

        //La entidad es el ayuntamiento correspondiente
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            String numexpSolicitud = rulectx.getNumExp();
            
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, numexpSolicitud);
            
            if (expedientesRelacionados.isEmpty()) {
                return Boolean.TRUE;
            }
            
            String numexpEnt = expedientesRelacionados.get(0);
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexpEnt);
            Iterator<?> it = col.iterator();
            
            if (!it.hasNext()) {
                return Boolean.TRUE;
            }
            
            IItem entidad = (IItem)it.next();
            strBOPEntidad = entidad.getString("MUNICIPIO");
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido iniciar el tr�mite de publicaci�n.",e);
        }
        
        return true;
    }
}
