package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateActaBaseRule implements IRule {
    
    protected static final Logger LOGGER = Logger.getLogger(GenerateActaBaseRule.class);

    protected String strPrefijo                 = "Borrador de Acta de Pleno";
    protected String strNombreTramite           = strPrefijo;
    protected String strNombreCabecera          = strPrefijo + " - Cabecera";
    protected String strNombrePie               = strPrefijo + " - Pie";
    protected String strNombreCabeceraPropuesta = strPrefijo + " - Propuesta - Cabecera";
    protected String strNombrePiePropuesta      = strPrefijo + " - Propuesta - Pie";
    protected String strNombreRuegos            = strPrefijo + " - Ruegos y preguntas";
    
    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            // APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            
            // Variables
            IItem entityDocument = null;
            int documentTypeId = 0;
            int templateId = 0;
            int taskId = rulectx.getTaskId();
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            DecimalFormat df = new DecimalFormat("0000");
            int documentId = 0;
            Object connectorSession = null;
            String nombrePlantilla;
            List<?> list = null;
            Iterator<?> itProps = null;
            IItem iProp = null;
            int orden = 1;
            int nTotal = 0;
            String extracto = "";
            String debate = "";
            String acuerdos = "";
            int nSi = 0;
            int nNo = 0;
            int nAbs = 0;
            HashMap<Integer, String> orden2exp = new HashMap<Integer, String>();
            
            // Obtenci�n de plantillas asociadas al tr�mite
            IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
            
            if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()) {
                throw new ISPACInfo("No hay tipo de documento asociado al tr�mite");
            }

            // Generaci�n del documento por cada tipo de documento asociado
            Iterator<?> itTpDocs = taskTpDocCollection.iterator();
            
            while(itTpDocs.hasNext()) {
                IItem taskTpDoc = (IItem)itTpDocs.next();
                documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                
                // Comprobar que el tipo de documento tiene asociado una plantilla
                IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                
                if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()) {
                    throw new ISPACInfo("No hay plantilla asociada al tipo de documento");
                }
                
                IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                templateId = tpDocsTemplate.getInt("ID");

                // Si la plantilla es la de propuestas entonces hay que usarla
                // varias veces, una por cada propuesta. Usaremos variables de
                // sistema para pasarle datos al tag <ispactag sessionvar='var'/>
                nombrePlantilla = tpDocsTemplate.getString("NOMBRE");
                boolean esPropuesta = 
                    nombrePlantilla.compareTo(strNombreCabeceraPropuesta)==0 || 
                    nombrePlantilla.compareTo(strNombrePiePropuesta)==0 ;

                if ( esPropuesta ) {
                    list = SecretariaUtil.getPropuestasYUrgencias(cct, rulectx.getNumExp());
                    itProps = list.iterator();
                    orden = 1;
                }
                boolean seguir = true;
                
                while ( seguir ) {
                    seguir = false;
                    
                    try {
                        if ( esPropuesta ) {
                            iProp = (IItem)itProps.next();
                            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
                            boolean esAcuerdo = (strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0 );
                            String strCampo = esAcuerdo? "ACUERDOS":"DICTAMEN";
                            
                            if (iProp.get("EXTRACTO")!=null){
                                extracto = (String)iProp.get("EXTRACTO");
                            } else {
                                extracto = "";
                            }
                            if (iProp.get("DEBATE")!=null) {
                                debate = (String)iProp.get("DEBATE"); 
                            } else {
                                debate = "";
                            }
                            if (iProp.get(strCampo)!=null) {
                                acuerdos = (String)iProp.get(strCampo);
                            } else {
                                acuerdos = "";
                            }
                            debate = debate.replaceAll("\r\n", "\r"); //Evita saltos de l�nea duplicados
                            acuerdos = acuerdos.replaceAll("\r\n", "\r");
                            if (iProp.get("N_SI")!=null){
                                nSi = iProp.getInt("N_SI");
                            } else {
                                nSi=0;
                            }
                            if (iProp.get("N_NO")!=null){
                                nNo = iProp.getInt("N_NO"); 
                            } else {
                                nNo=0;
                            }
                            if (iProp.get("N_ABS")!=null){
                                nAbs = iProp.getInt("N_ABS"); 
                            } else {
                                nAbs=0;
                            }
                            cct.setSsVariable("EXTRACTO", extracto);
                            //cct.setSsVariable("DEBATE", debate);
                            //cct.setSsVariable("ACUERDOS", acuerdos);
                            setLongVariable(rulectx, "DEBATE", debate);
                            setLongVariable(rulectx, "ACUERDOS", acuerdos);
                            cct.setSsVariable("ORDEN", String.valueOf(orden));
                            cct.setSsVariable("N_SI", String.valueOf(nSi));
                            cct.setSsVariable("N_NO", String.valueOf(nNo));
                            cct.setSsVariable("N_ABS", String.valueOf(nAbs));
                            cct.setSsVariable("N_TOTAL", String.valueOf(nSi+nNo+nAbs));
                            //Guardo el numero de expediente para luego recuperar el word de contenido.
                            String numexpOrigen = iProp.getString("NUMEXP_ORIGEN");
                            
                            if ( numexpOrigen == null){
                                numexpOrigen = "no hay";
                            }
                            orden2exp.put(new Integer(orden), numexpOrigen);
                        }

                        connectorSession = gendocAPI.createConnectorSession();
                        // Abrir transacci�n para que no se pueda generar un documento sin fichero
                        cct.beginTX();
                    
                        entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                        documentId = entityDocument.getKeyInt();
        
                        // Generar el documento a partir de la plantilla
                        IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
                        entityTemplate.set(DocumentosUtil.EXTENSION, "doc");
                        entityTemplate.store(cct);
                        
                        if ( esPropuesta ){
                            // A�adimos el n�mero de orden al nombre del doc para identificarlo mejor.
                            String templateDescripcion = entityTemplate.getString(DocumentosUtil.DESCRIPCION);
                            templateDescripcion = templateDescripcion + " - " + df.format(orden);
                            entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                            entityTemplate.store(cct);
                            //Borramos las variables de sistema
                            cct.deleteSsVariable("EXTRACTO");
                            //cct.deleteSsVariable("DEBATE");
                            //cct.deleteSsVariable("ACUERDOS");
                            deleteLongVariable(rulectx, "DEBATE");
                            deleteLongVariable(rulectx, "ACUERDOS");
                            cct.deleteSsVariable("ORDEN");
                            cct.deleteSsVariable("N_SI");
                            cct.deleteSsVariable("N_NO");
                            cct.deleteSsVariable("N_ABS");
                            cct.deleteSsVariable("N_TOTAL");
                            //Preparamos la siguiente iteraci�n
                            orden++;
                            nTotal++;
                            seguir = esPropuesta && itProps.hasNext();
                        }
                        
                    } catch (Exception e) {
                        // Si se produce alg�n error se hace rollback de la transacci�n
                        cct.endTX(false);
                        
                        String message = "exception.documents.generate";
                        String extraInfo = null;
                        Throwable eCause = e.getCause();
                        
                        if (eCause instanceof ISPACException) {
                            if (eCause.getCause() instanceof NoConnectException) {
                                extraInfo = "exception.extrainfo.documents.openoffice.off";
                                
                            } else {
                                extraInfo = eCause.getCause().getMessage();
                            }
                            
                        } else if (eCause instanceof DisposedException) {
                            extraInfo = "exception.extrainfo.documents.openoffice.stop";
                            
                        } else {
                            extraInfo = e.getMessage();
                        }            
                        throw new ISPACInfo(message, extraInfo);
                        
                    } finally {
                        if (connectorSession != null) {
                            gendocAPI.closeConnectorSession(connectorSession);
                        }
                    }
                    
                    // Si ha sido correcto se hace commit de la transacci�n
                    cct.endTX(true);
                }
            }
            
            //Ahora hay que concatenar los archivos que hemos generado
            //
            //    Obtener cabecera y guardarla en repositorio temporal
            //    Bucle
            //        Obtener siguiente fichero
            //        Concatenar cabecera con fichero siguiente
            //    Guardar cabecera ya procesada en repositorio documental
            //    Borrar los documentos auxiliares

            //Obtiene la cabecera
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreCabecera, "");
            LOGGER.warn("strInfoPag "+strInfoPag +"strNombreCabecera "+strNombreCabecera);
            File file1 = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            LOGGER.warn("strInfoPag "+strInfoPag +"strNombreCabecera "+strNombreCabecera);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());

            //Obtiene e inserta cada uno de los fichero de propuesta (cabecera y pie)
            nTotal = nTotal/2; //Estaba duplicado el n�mero de propuestas porque hay dos documentos, cabecera y pie.
            String descr = "";
            File file = null;
            
            for ( int i=1 ; i<=nTotal ; i++) {
                descr = strNombreCabeceraPropuesta + " - " + df.format(i);
                strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), descr, "");
                file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                file.delete();
                
                insertContenidoPropuesta(rulectx, xComponent, (String)orden2exp.get(new Integer(i)));

                descr = strNombrePiePropuesta + " - " + df.format(i);
                strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), descr, "");
                file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                file.delete();
            }
            //Obtiene e inserta los ruegos y preguntas.
            strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreRuegos, "");
            file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();

            //Obtiene e inserta el pie del acta.
            strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombrePie, "");
            file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();
            
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '" + strNombreTramite + "'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            Iterator<?> it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int)file.length(), "application/msword", strNombreTramite);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '" + strPrefijo + " -%' OR DESCRIPCION LIKE 'Borrador de Acta de Pleno -%'";
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException(e);
            
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
        
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso.
    }

    private void insertContenidoPropuesta(IRuleContext rulectx, XComponent xComponent, String numexp) throws ISPACRuleException {
        try {
            IClientContext cct = rulectx.getClientContext();
            
            //Obtenemos los docuementos de la propuesta
            IItemCollection collection = DocumentosUtil.getDocumentos(cct, numexp);
            Iterator<?> it = collection.iterator();
            boolean found = false;
            
            while (it.hasNext() && !found) {
                IItem iDoc = (IItem)it.next();
                
                //El contenido de la propuesta tiene que estar en formato Word (.doc)
                String extension = iDoc.getString(DocumentosUtil.EXTENSION);
                
                if ( extension.toUpperCase().compareTo("DOC")==0) {
                    //En concreto busco documentos de tipo Anexo a la Solicitud (propuestas desde Registro Telem�tico)
                    //o de tipo Contenido de la propuesta (propuestas iniciadas desde escritorio de tramitaci�n)
                    String nombre = iDoc.getString("NOMBRE");
                    
                    if ( nombre.compareTo("Anexo a Solicitud" )== 0 || nombre.compareTo("Contenido de la propuesta") == 0) {
                        found = true;
                        String strInfoPag = iDoc.getString(DocumentosUtil.INFOPAG);
                        
                        if (strInfoPag != null) {
                            File file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
                            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                            file.delete();
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new ISPACRuleException(e);     
        }
    }
    
    private void setLongVariable(IRuleContext rulectx, String nombre, String valor) throws ISPACRuleException {
        try {
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            
            IItem entity = entitiesAPI.createEntity("TSOL_LONG_VARS", rulectx.getNumExp());
            entity.set("NOMBRE", nombre);
            entity.set("VALOR", valor);
            entity.store(cct);
        } catch(Exception e) {
            throw new ISPACRuleException(e);     
        }
    }
    
    private void deleteLongVariable(IRuleContext rulectx, String nombre) throws ISPACRuleException {
        try {
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            
            String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE='" + nombre + "'";
            entitiesAPI.deleteEntities("TSOL_LONG_VARS", strQuery);
        } catch(Exception e) {
            throw new ISPACRuleException(e);     
        }
    }
}
