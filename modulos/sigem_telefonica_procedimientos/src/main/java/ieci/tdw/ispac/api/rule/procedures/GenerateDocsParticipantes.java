package ieci.tdw.ispac.api.rule.procedures;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * Regla  gen�rica utilizada para generar un documento a partir de una plantilla 
 * personalizado para cada Participante del expediente.
 * Esta regla funcionar� en tr�mites que s�lo tengan configurada una plantilla. S� el
 * tr�mite tiene m�s de una habr� que construir una regla espec�fica para seleccionar
 * la plantilla adecuada.
 */
public class GenerateDocsParticipantes implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            //----------------------------------------------------------------------------------------------

            //Variables
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");

            //Obtener participantes del expediente actual
            IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, rulectx.getNumExp());
            
            //Comprobar que hay alg�n participante para el cual generar su comunicado
            if (participantes!=null && participantes.toList().size()>=1) {
                //Comprobar que hay alguna plantilla asociada al tr�mite
                IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
                
                if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()) {
                    throw new ISPACInfo("No hay tipo de documento asociado al tr�mite");
                    
                } else {
                    //Tomamos el primer (y deber�a ser el �nico) tipo de documento
                    IItem taskTpDoc = (IItem)taskTpDocCollection.iterator().next();
                    String strTpDocName = (String)taskTpDoc.get("CT_TPDOC:NOMBRE");                    
                    int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                    IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                    
                    if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()) {
                        throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
                        
                    } else {
                        //Tomamos la primera plantilla (tambi�n deber�a ser la �nica)
                        IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                        String strTemplateName = (String)tpDocsTemplate.get("NOMBRE");
                        
                        //Hay una plantilla as� que podemos recorrer la lista de
                        //participantes para generar los documentos
                        for (int i=0;i<participantes.toList().size();i++) {
                            IItem participante = (IItem) participantes.toList().get(i);
                            
                            if (participante!=null) {
                                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                                
                                CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, participante.getString("NDOC"));
                                
                                DocumentosUtil.borraParticipanteSsVariable(cct);
                            }
                        }
                    }
                }
            }            
        } catch(ISPACException e) {
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
