package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

public class InitSolicitudBopInicialPresupuestoRule extends InitSolicitudBopPresupuestoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PRES_PRESUPUESTO";
        strBOPEntidad = "�REA DE HACIENDA y PROMOCI�N ECON�MICA";
        strBOPUrgencia = "Normal";
        strBOPSumario = "Aprobaci�n inicial del Presupuesto General";
        strBOPObservaciones = "Solicitud autom�tica desde expediente: Aprobaci�n Presupuesto General";

        //La entidad es el ayuntamiento correspondiente
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            String numexpSolicitud = rulectx.getNumExp();    
            
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, numexpSolicitud);
            
            if (expedientesRelacionados.isEmpty()) {
                return true;
            }
            
            String numexpEnt = expedientesRelacionados.get(0);
            
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexpEnt);
            Iterator<?> it = col.iterator();
            
            if (!it.hasNext()) {
                return true;
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
