package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @prop�sito Al iniciar el tr�mite "Preparaci�n de firmas", se crea un documento para este tr�mite
 *                 Este documento apuntar� al mismo documento Decreto, en el repositorio, que el del tr�mite "Creaci�n de Decreto"
 *                 Es decir, las modificaciones del Decreto desde ambos tr�mites, se har�n sobre el mismo documento del repositorio.
 */
public class CreateTaskDocumentRule implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            
            // Soluci�n, para que en un tr�mite salgan documentos de otros tr�mites: 
            // Al iniciar el tr�mite Preparaci�n de firmas, se crea un nuevo registro en la tabla spac_dt_documentos.
            // Es decir, "anexamos un documento". Y luego le damos los mismos valores que el registro del tr�mite Creaci�n de Decreto.
            // Por lo tanto, existe un documento anexado para cada uno de los dos tr�mites,
            // pero en realidad, el repositorio documental, son el mismo documento.
            // Y lo que cambies del CONTENIDO del documento desde el tr�mite Preparaci�n de firmas, se cambia en el 
            //  CONTENIDO del documento del tr�mite Creaci�n del Decreto.
            
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

            // Obtener el Decreto adjuntado en la fase de Inicio, para copiar: infopag, descripci�n, idplantilla, extensi�n
            // Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado s�lo un doc (ValidateNumDocsTramiteRule)
            
            // Obtener el documento que tiene hasta el momento el expediente
            //IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "", "FDOC DESC");
            // Obtener el documento Decreto del expediente
            IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Decreto'", "FDOC DESC");
            IItem decreto = null;
            if (documentsCollection!=null && documentsCollection.next()){
                decreto = (IItem)documentsCollection.iterator().next();
            }
                
            // Crear el documento del mismo tipo que el Decreto del tr�mite Creaci�n del Decreto
            IItem nuevoDecreto = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(),decreto.getInt("ID_TPDOC"));
            
            // Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
            if (decreto!=null){
                String infopag = decreto.getString(DocumentosUtil.INFOPAG);
                String infopagrde = decreto.getString(DocumentosUtil.INFOPAG_RDE);
                String repositorio = decreto.getString(DocumentosUtil.REPOSITORIO);
                //String codVerificacion = decreto.getString(DocumentosUtil.COD_VERIFICACION);
                String codCotejo = decreto.getString(DocumentosUtil.COD_COTEJO);
                String descripcion = decreto.getString(DocumentosUtil.DESCRIPCION);
                String extension = decreto.getString(DocumentosUtil.EXTENSION);            
                String extensionRDE = decreto.getString("EXTENSION_RDE");//2.0
                int idPlantilla = decreto.getInt(DocumentosUtil.ID_PLANTILLA);

                nuevoDecreto.set(DocumentosUtil.INFOPAG, infopag);
                nuevoDecreto.set(DocumentosUtil.INFOPAG_RDE, infopagrde);
                nuevoDecreto.set(DocumentosUtil.REPOSITORIO, repositorio);
                //nuevoDecreto.set(DocumentosUtil.COD_VERIFICACION, codVerificacion);
                nuevoDecreto.set(DocumentosUtil.COD_COTEJO, codCotejo);
                nuevoDecreto.set(DocumentosUtil.DESCRIPCION, descripcion);
                nuevoDecreto.set(DocumentosUtil.EXTENSION, extension);
                nuevoDecreto.set("EXTENSION_RDE", extensionRDE);//2.0
                if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
                    nuevoDecreto.set(DocumentosUtil.ID_PLANTILLA, idPlantilla);
                }
                nuevoDecreto.store(rulectx.getClientContext());
            }
            
            // Otra opci�n (SIN IMPLEMENTAR) es anexar el documento en la Fase de Inicio.
            // Luego al crear el tr�mite de la fase firmas, asignarle el documento anterior
            // Cambiar de estado del documento a 'no firmado' para volverlo a firmar
            // Al cerrar el tr�mite volver a asignar el documento a su tr�mite inicial (Fase de Introducci�n)
            // Entonces, si luego vieramos los tr�mites: Creaci�n de tr�mites s� que tiene documento, 
            // pero Preparaci�n de firmas no tiene nada adjuntado
            
        }catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}
