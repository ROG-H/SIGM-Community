package ieci.tdw.ispac.api.rule.procedures.sancionador;

import java.util.Iterator;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia solicitud BOP inicial del Procedimiento Sancionador 
 *
 */

public class InitSolicitudBopInicialSancionadorRule extends InitSolicitudBopSancionadorRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_SANCIONADOR";
        strBOPEntidad = "";
        strBOPUrgencia = "Normal";
        strBOPSumario = "Aprobaci�n inicial de la Cuenta General";
        strBOPObservaciones = "Solicitud autom�tica desde expediente: Procedimiento Sancionador";

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
