package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraPRENotificacionesConvocatoriasSub2AsociGrupos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraPRENotificacionesConvocatoriasSub2AsociGrupos.class);

    protected String refTablas = "%TABLA1%,%TABLA2%";

    protected String plantilla = "";
    protected String tipoDocumento = "";
    
    String infoPag;
    int idPlantilla;
    int documentTypeId = 0;
    IItem processTask;
    int templateId = 0;
    
    String sFileTemplate;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla espec�fica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla espec�fica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();
            String extractoDecreto = "";
            
            Object connectorSession = null;
            
            connectorSession = genDocAPI.createConnectorSession();
            processTask = entitiesAPI.getTask(rulectx.getTaskId());

            int idTramCtl = processTask.getInt("ID_TRAM_CTL");

            IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
            Iterator<?> it = taskTpDocCollection.iterator();
            while (it.hasNext()) {
                IItem taskTpDoc = (IItem) it.next();
                if ((taskTpDoc.getString("CT_TPDOC:NOMBRE").trim()).equalsIgnoreCase((tipoDocumento).trim())) {
                    documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                }
            }
            
            IItemCollection plantillaPreNotificacionesCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Informe del servicio'", "FDOC DESC");
            Iterator<?> plantillaPreNotificacionesIterator = plantillaPreNotificacionesCollection.iterator();
            if(plantillaPreNotificacionesIterator.hasNext()){
                IItem plantillaPreNotificaciones = (IItem) plantillaPreNotificacionesIterator.next();
                
                infoPag = plantillaPreNotificaciones.getString("INFOPAG");
                idPlantilla = plantillaPreNotificaciones.getInt("ID");
                
                sFileTemplate = getFile(rulectx.getClientContext().getAPI().getGenDocAPI(), connectorSession, infoPag, templateId, idPlantilla);
            }

            ArrayList<String> expedientesResolucion = new ArrayList<String>();

            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();
            if (expRelIt.hasNext()) {
                while (expRelIt.hasNext()) {
                    IItem expRel = (IItem) expRelIt.next();
                    // Solo trabajamos con aquellos expedientes en estado
                    // RESOLUCION - RS
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");

                    IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);

                    if (expHijo != null && "RS".equals(expHijo.get("ESTADOADM"))) {
                        expedientesResolucion.add(numexpHijo);
                    }
                }
            }

            strQuery = "WHERE NUMEXP_PADRE='" + numexp + "' ORDER BY SUBSTRING(NUMEXP_HIJO,\"position\"(NUMEXP_HIJO::TEXT, '/'::TEXT)+1)::INTEGER DESC";
            IItemCollection expRelCol2 = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt2 = expRelCol2.iterator();
            if (expRelIt2.hasNext()) {
                while (expRelIt2.hasNext() && "".equals(extractoDecreto)) {
                    IItem expRel2 = (IItem) expRelIt2.next();
                    // Solo trabajamos con aquellos expedientes en estado
                    // RESOLUCION - RS
                    String numexpHijo = expRel2.getString("NUMEXP_HIJO");

                    if ("".equals(extractoDecreto)) {
                        IItemCollection expHijoCol2 = entitiesAPI.getEntities("SGD_DECRETO", numexpHijo);
                        Iterator<?> expHijoIt = expHijoCol2.iterator();
                        if (expHijoIt.hasNext()){
                            extractoDecreto = ((IItem) expHijoIt.next()).getString("EXTRACTO_DECRETO");
                        }
                    }
                }
            }

            if (!expedientesResolucion.isEmpty()) {
                for (int i = 0; i < expedientesResolucion.size(); i++) {
                    generaNotificacion(expedientesResolucion.get(i), rulectx, entitiesAPI, genDocAPI, procedureAPI, extractoDecreto);
                }
            }
            if (StringUtils.isNotEmpty(sFileTemplate)) {
                DocumentosUtil.deleteFile(sFileTemplate);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, IProcedureAPI procedureAPI, String extractoDecreto) {
        try {
            IItem entityDocument = null;
            int documentId = 0;
            int taskId = rulectx.getTaskId();
            IClientContext cct = rulectx.getClientContext();

            String nombre = "";
            String dirnot = "";
            String cPostal = "";
            String localidad = "";
            String caut = "";
            String recurso = "";
            String idExt = "";
            
            Object connectorSession = null;
            
            cct.beginTX();
            // Comprobamos que haya encontrado el Tipo de documento
            if (documentTypeId != 0) {
                // Comprobar que el tipo de documento tiene asociado una
                // plantilla
                IItemCollection tpDocsTemplatesCollection = (IItemCollection) procedureAPI.getTpDocsTemplates(documentTypeId);
                if (tpDocsTemplatesCollection == null || tpDocsTemplatesCollection.toList().isEmpty()) {
                    throw new ISPACInfo(Messages.getString(ConstantesString.LOGGER_ERROR + ".decretos.acuses.tpDocsTemplates"));
                } else {
                    Iterator<?> docs = tpDocsTemplatesCollection.iterator();
                    boolean encontrado = false;
                    while (docs.hasNext() && !encontrado) {
                        IItem tpDocsTemplate = (IItem) docs.next();
                        if (((String) tpDocsTemplate.get("NOMBRE")).trim().equalsIgnoreCase(plantilla.trim())) {
                            templateId = tpDocsTemplate.getInt("ID");
                            encontrado = true;
                        }
                    }

                    // Recuperamos el participante del expediente que estamos
                    // resolviendo
                    IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, numexp, "", "");
                    Iterator<?> participantesIterator = participantesCollection.iterator();
                    if (participantesIterator.hasNext()) {
                        IItem participante = (IItem) participantesIterator.next();
                        if (participante != null) {
                            // A�adir a la session los datos para poder utilizar
                            // <ispactag sessionvar='var'> en la plantilla
                            if ((String) participante.get("NOMBRE") != null) {
                                nombre = (String) participante.get("NOMBRE");
                            } else {
                                nombre = "";
                            }
                            if ((String) participante.get("DIRNOT") != null) {
                                dirnot = (String) participante.get("DIRNOT");
                            } else {
                                dirnot = "";
                            }
                            if ((String) participante.get("C_POSTAL") != null) {
                                cPostal = (String) participante.get("C_POSTAL");
                            } else {
                                cPostal = "";
                            }
                            if ((String) participante.get("LOCALIDAD") != null) {
                                localidad = (String) participante.get("LOCALIDAD");
                            } else {
                                localidad = "";
                            }
                            if ((String) participante.get("CAUT") != null) {
                                caut = (String) participante.get("CAUT");
                            } else {
                                caut = "";
                            }
                            if ((String) participante.get("RECURSO") != null) {
                                recurso = (String) participante.get("RECURSO");
                            } else {
                                recurso = "";
                            }
                            /**
                             * INICIO[Teresa] Ticket#106#: a�adir el campo
                             * id_ext
                             * **/
                            if ((String) participante.get("ID_EXT") != null) {
                                idExt = (String) participante.get("ID_EXT");
                            } else {
                                idExt = "";
                            }
                            /**
                             * FIN[Teresa] Ticket#106#: a�adir el campo id_ext
                             * **/

                            // Obtener el sustituto del recurso en la tabla
                            // SPAC_VLDTBL_RECURSOS
                            String sqlQueryPart = "WHERE VALOR = '" + recurso + "'";
                            IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
                            if (colRecurso.iterator().hasNext()) {
                                IItem iRecurso = (IItem) colRecurso.iterator().next();
                                recurso = iRecurso.getString("SUSTITUTO");
                            }
                            /**
                             * INICIO ##Ticket #172 SIGEM decretos y secretaria,
                             * modificar el recurso
                             * **/
                            if ("".equals(recurso)) {
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
                            } else {
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
                            }

                            cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
                            cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
                            cct.setSsVariable("NOMBRETRABAJADOR", nombre);
                            cct.setSsVariable("NOMBREBENEFICIARIO", idExt);
                            cct.setSsVariable("NUMEXPSOLICITUD", numexp);
                            cct.setSsVariable("RECURSO", recurso);

                            cct.setSsVariable("NOMBRE", nombre);
                            cct.setSsVariable("DIRNOT", dirnot);
                            cct.setSsVariable("C_POSTAL", cPostal);
                            cct.setSsVariable("LOCALIDAD", localidad);
                            cct.setSsVariable("CAUT", caut);

                            String estadoAdm = "";

                            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);

                            if (expediente != null) {
                                estadoAdm = expediente.getString("ESTADOADM");
                            }

                            String textoResol = "";

                            if ("RS".equals(estadoAdm)) {
                                textoResol = "Esta Vicepresidencia propone la concesi�n de subvenci�n, conforme al siguiente detalle: ";
                            } else {
                                textoResol = "Esta Vicepresidencia propone la denegaci�n de la siguiente solicitud, por el motivo que se indica: ";
                            }

                            cct.setSsVariable("TEXTO_RESOLUCION", textoResol);
                            cct.setSsVariable("EXTRACTO_DECRETO", extractoDecreto);

                            connectorSession = genDocAPI.createConnectorSession();

                            entityDocument = genDocAPI.createTaskDocument(taskId, documentTypeId);
                            documentId = entityDocument.getKeyInt();

                            // Generar el documento a partir la plantilla
                            IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);

                            // Referencia al fichero del documento en el gestor
                            // documental
                            String docref = entityTemplate.getString("INFOPAG");
                            String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
                            entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
                            String templateDescripcion = entityTemplate.getString("DESCRIPCION");
                            templateDescripcion = templateDescripcion + " - " + numexp;
                            entityTemplate.set("DESCRIPCION", templateDescripcion);
                            entityTemplate.set("DESTINO", nombre);
                            entityTemplate.set("DESTINO_ID", idExt);

                            entityTemplate.store(cct);

                            cct.deleteSsVariable("ANIO");
                            cct.deleteSsVariable("NRESOLUCIONPARCIAL");
                            cct.deleteSsVariable("NOMBRE_TRAMITE");
                            cct.deleteSsVariable("NOMBRETRABAJADOR");
                            cct.deleteSsVariable("NOMBREBENEFICIARIO");
                            cct.deleteSsVariable("NUMEXPSOLICITUD");
                            cct.deleteSsVariable("RECURSO");

                            cct.deleteSsVariable("NOMBRE");
                            cct.deleteSsVariable("DIRNOT");
                            cct.deleteSsVariable("C_POSTAL");
                            cct.deleteSsVariable("LOCALIDAD");
                            cct.deleteSsVariable("CAUT");

                            cct.deleteSsVariable("TEXTO_RESOLUCION");
                            cct.deleteSsVariable("EXTRACTO_DECRETO");

                            if (null != refTablas && !"".equals(refTablas)) {
                                insertaTablas(genDocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, numexp);
                            }
                        }
                    }
                }
            }
            cct.endTX(true);
        } catch (ISPACRuleException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }
    }

    public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String string, IEntitiesAPI entitiesAPI, String numexp) {

        Object connectorSession = null;
        OpenOfficeHelper ooHelper = null;
        try {
            // Abre el documento
            String extension = "odt";
            String fileName = FileTemporaryManager.getInstance().newFileName("." + extension);

            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;

            OutputStream out = new FileOutputStream(fileName);
            connectorSession = gendocAPI.createConnectorSession();
            gendocAPI.getDocument(connectorSession, docref, out);
            File file = new File(fileName);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + fileName);

            String[] refTabla = refTablas.split(",");
            for (int i = 0; i < refTabla.length; i++) {
                insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp);
            }

            // Guarda el documento
            String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
            fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
            String mime = "application/vnd.oasis.opendocument.text";
            OpenOfficeHelper.saveDocument(xComponent, "file://" + fileNameOut, "");
            File fileOut = new File(fileNameOut);
            InputStream in = new FileInputStream(fileOut);
            gendocAPI.setDocument(connectorSession, documentId, docref, in, (int) (fileOut.length()), mime);

            // Borra archivos temporales
            file.delete();
            fileOut.delete();
            DocumentosUtil.deleteFile(fileName);
            if (in != null){
                in.close();
            }
            if (out != null){
                out.close();
            }
            ooHelper.dispose();
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } catch (java.lang.Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
        String ayuntamiento = "";
        String nreg = "";
        String cif = "";
        String motivoDenegacion = "";
        String cifAsociacion = "";
        String nombreAsociacion = "";

        try {
            if ("%TABLA1%".equals(refTabla)){
                ayuntamiento = "";
                nreg = "";
                cif = "";
                cifAsociacion = "";
                nombreAsociacion = "";
                motivoDenegacion = "";
                
                String estadoAdm = "";
                IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
                if (expediente != null) {
                    estadoAdm = expediente.getString("ESTADOADM");
                }
                String tituloCol4 = "";
    
                if ("RC".equals(estadoAdm)) {
                    tituloCol4 = "MOTIVO DENEGACI�N";
                } else {
                    tituloCol4 = "IMPORTE";
                }
    
                int numFilas = 1;
    
                ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                nreg = expediente.getString("NREG");
                cif = expediente.getString("NIFCIFTITULAR");
    
                if (ayuntamiento == null){
                    ayuntamiento = "";
                }
                if (cif == null){
                    cif = "";
                }
    
                // Busca la posici�n de la tabla y coloca el cursor ah�
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                XTextTable xTable = null;
    
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    
                    Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", numexp).iterator();
                     if (expResolucion.hasNext()) {
                         IItem resolucion = (IItem) expResolucion.next();
                        
                         motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO");
                         cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION");
                         nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION");
                     }
                     
                    if (motivoDenegacion == null){
                        motivoDenegacion = "";
                    }
                    
                    if(StringUtils.isNotEmpty(cifAsociacion)){
        
                        // Inserta una tabla de 4 columnas y tantas filas
                        // como nuevas liquidaciones haya mas una de cabecera
                        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                        Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                        xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
        
                        // A�adimos 3 filas m�s para las dos de la cabecera de la tabla
                        // y uno para la celda final
                        if ("RC".equals(estadoAdm)) {
                            xTable.initialize(numFilas + 1, 5);
                        } else {
                            xTable.initialize(numFilas + 1, 4);
                        }
                        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                        xText.insertTextContent(xSearchTextRange, xTextContent, false);
        
                        if ("RS".equals(estadoAdm)) {
                            colocaColumnas1(xTable);
                        } else {
                            colocaColumnas2(xTable);
                        }
        
                        // Rellena la cabecera de la tabla
                        setHeaderCellText(xTable, "A1", "N�M. REGISTRO");
                        setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                        setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACI�N");
                        setHeaderCellText(xTable, "D1", "C.I.F");
                        if ("RC".equals(estadoAdm)) {
                            setHeaderCellText(xTable, "E1", tituloCol4);
                        }
        
                        if ("RS".equals(estadoAdm)) {
                            setCellText(xTable, "A" +  (2), nreg);
                            setCellText(xTable, "B" +  (2), ayuntamiento);
                            setCellText(xTable, "C" +  (2), nombreAsociacion);
                            setCellText(xTable, "D" +  (2), cifAsociacion);
        
                        } else if ("RC".equals(estadoAdm)) {
                            setCellText(xTable, "A" +  (2), nreg);
                            setCellText(xTable, "B" +  (2), ayuntamiento);
                            setCellText(xTable, "C" +  (2), nombreAsociacion);
                            setCellText(xTable, "D" +  (2), cifAsociacion);
                            setCellText(xTable, "E" +  (2), motivoDenegacion);
                        } else {
                            setCellText(xTable, "A" +  (2), "");
                            setCellText(xTable, "B" +  (2), "");
                            setCellText(xTable, "C" +  (2), "");
                            setCellText(xTable, "D" +  (2), "");
                            setCellText(xTable, "E" +  (2), "");
                        }
                    }
                 }
            }
            if ("%TABLA2%".equals(refTabla)){
                ayuntamiento = "";
                nreg = "";
                cif = "";
                cifAsociacion = "";
                nombreAsociacion = "";
                motivoDenegacion = "";
                
                String estadoAdm = "";
                IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp);
                if (expediente != null) {
                    estadoAdm = expediente.getString("ESTADOADM");
                }
                String tituloCol4 = "";
    
                if ("RC".equals(estadoAdm)) {
                    tituloCol4 = "MOTIVO DENEGACI�N";
                } else {
                    tituloCol4 = "IMPORTE";
                }
    
                int numFilas = 1;
    
                ayuntamiento = expediente.getString("IDENTIDADTITULAR");
                nreg = expediente.getString("NREG");
                cif = expediente.getString("NIFCIFTITULAR");
    
                if (ayuntamiento == null){
                    ayuntamiento = "";
                }
                if (cif == null){
                    cif = "";
                }
    
                // Busca la posici�n de la tabla y coloca el cursor ah�
                // Usaremos el localizador %TABLA1%
                XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
                XText xText = xTextDocument.getText();
                XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, component);
                XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
                xSearchDescriptor.setSearchString(refTabla);
                XInterface xSearchInterface = null;
                XTextRange xSearchTextRange = null;
                xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
                XTextTable xTable = null;
    
                if (xSearchInterface != null) {
                    // Cadena encontrada, la borro antes de insertar la tabla
                    xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
                    xSearchTextRange.setString("");
                    
                    Iterator<?> expResolucion = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", numexp).iterator();
                    if (expResolucion.hasNext()) {
                        IItem resolucion = (IItem) expResolucion.next();
                       
                        motivoDenegacion = resolucion.getString("MOTIVO_RECHAZO2");
                        cifAsociacion = resolucion.getString("CIFGRUPOASOCIACION2");
                        nombreAsociacion = resolucion.getString("NOMBREGRUPOASOCIACION2");
                    }
                    
                    if (motivoDenegacion == null){
                        motivoDenegacion = "";
                    }
                    
                    if(StringUtils.isNotEmpty(cifAsociacion)){
        
                        // Inserta una tabla de 4 columnas y tantas filas
                        // como nuevas liquidaciones haya mas una de cabecera
                        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
                        Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
                        xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
        
                        // A�adimos 3 filas m�s para las dos de la cabecera de la tabla
                        // y uno para la celda final
                        if ("RC".equals(estadoAdm)) {
                            xTable.initialize(numFilas + 1, 5);
                        } else {
                            xTable.initialize(numFilas + 1, 4);
                        }
                        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
                        xText.insertTextContent(xSearchTextRange, xTextContent, false);
        
                        if ("RS".equals(estadoAdm)) {
                            colocaColumnas1(xTable);
                        } else {
                            colocaColumnas2(xTable);
                        }
        
                        // Rellena la cabecera de la tabla
                        setHeaderCellText(xTable, "A1", "N�M. REGISTRO");
                        setHeaderCellText(xTable, "B1", "AYUNTAMIENTO");
                        setHeaderCellText(xTable, "C1", "GRUPO / ASOCIACI�N");
                        setHeaderCellText(xTable, "D1", "C.I.F");
                        if ("RC".equals(estadoAdm)) {
                            setHeaderCellText(xTable, "E1", tituloCol4);
                        }
        
                        if ("RS".equals(estadoAdm)) {
                            setCellText(xTable, "A" +  (2), nreg);
                            setCellText(xTable, "B" +  (2), ayuntamiento);
                            setCellText(xTable, "C" +  (2), nombreAsociacion);
                            setCellText(xTable, "D" +  (2), cifAsociacion);
        
                        } else if ("RC".equals(estadoAdm)) {
                            setCellText(xTable, "A" +  (2), nreg);
                            setCellText(xTable, "B" +  (2), ayuntamiento);
                            setCellText(xTable, "C" +  (2), nombreAsociacion);
                            setCellText(xTable, "D" +  (2), cifAsociacion);
                            setCellText(xTable, "E" +  (2), motivoDenegacion);
                        } else {
                            setCellText(xTable, "A" +  (2), "");
                            setCellText(xTable, "B" +  (2), "");
                            setCellText(xTable, "C" +  (2), "");
                            setCellText(xTable, "D" +  (2), "");
                            setCellText(xTable, "E" +  (2), "");
                        }
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void setHeaderCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName));

        // Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));
        xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short) 60));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short) 60));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
        xCPS.setPropertyValue("BackColor", Integer.valueOf(0xC0C0C0));

        // Texto de la celda
        xCellText.setString(strText);
    }

    private void setCellText(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
        XCell xCell = xTextTable.getCellByName(cellName);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

        // Propiedades
        XTextCursor xTC = xCellText.createTextCursor();
        XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
        xTPS.setPropertyValue("CharFontName", "Arial");
        xTPS.setPropertyValue("CharHeight", new Float(8.0));
        xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
        xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
        xTPS.setPropertyValue("ParaTopMargin", new Short((short) 0));
        xTPS.setPropertyValue("ParaBottomMargin", new Short((short) 0));
        XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
        xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

        // Texto de la celda
        xCellText.setString(strText);
    }

    private void colocaColumnas1(XTextTable xTextTable) {

        XPropertySet xPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);

        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = (Integer) xPS.getPropertyValue("Width");

            short sTableColumnRelativeSum = (Short) xPS.getPropertyValue("TableColumnRelativeSum");

            // Get table column separators
            Object xObj = xPS.getPropertyValue("TableColumnSeparators");

            TableColumnSeparator[] xSeparators = (TableColumnSeparator[]) UnoRuntime.queryInterface(TableColumnSeparator[].class, xObj);

            // Calculamos el tama�o que le queremos dar a la celda
            // Se empieza colocando de la �ltima a la primera
            double dRatio = (double) sTableColumnRelativeSum / (double) iWidth;
            double dRelativeWidth = (double) 15000 * dRatio;

            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;

            // Set set new position for all column separators
            // N�mero de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 47500 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            // Do not forget to set TableColumnSeparators back! Otherwise, it
            // doesn't work.
            xPS.setPropertyValue("TableColumnSeparators", xSeparators);
        } catch (UnknownPropertyException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (WrappedTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void colocaColumnas2(XTextTable xTextTable) {

        XPropertySet xPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);

        // Get table Width and TableColumnRelativeSum properties values
        int iWidth;
        try {
            iWidth = (Integer) xPS.getPropertyValue("Width");

            short sTableColumnRelativeSum = (Short) xPS.getPropertyValue("TableColumnRelativeSum");

            // Get table column separators
            Object xObj = xPS.getPropertyValue("TableColumnSeparators");

            TableColumnSeparator[] xSeparators = (TableColumnSeparator[]) UnoRuntime.queryInterface(TableColumnSeparator[].class, xObj);

            // Calculamos el tama�o que le queremos dar a la celda
            // Se empieza colocando de la �ltima a la primera
            double dRatio = (double) sTableColumnRelativeSum / (double) iWidth;
            double dRelativeWidth = (double) 30000 * dRatio;

            // Last table column separator position
            double dPosition = sTableColumnRelativeSum - dRelativeWidth;

            // Set set new position for all column separators
            // N�mero de separadores
            int i = xSeparators.length - 1;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 15000 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 30000 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            i--;
            dRelativeWidth = (double) 15000 * dRatio;
            dPosition -= dRelativeWidth;
            xSeparators[i].Position = (short) Math.ceil(dPosition);

            // Do not forget to set TableColumnSeparators back! Otherwise, it
            // doesn't work.
            xPS.setPropertyValue("TableColumnSeparators", xSeparators);
        } catch (UnknownPropertyException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (WrappedTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (PropertyVetoException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el fichero correspondiente al infoPag indicado 
     *
     * @param rulectx
     * @param infoPag
     * @param templateId
     * @return 
     * @throws ISPACException
     */
    private String getFile(IGenDocAPI gendocAPI, Object connectorSession, String infoPag, int templateId, int idPlantilla) throws ISPACException{
        
        // API
        //IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

        //Object connectorSession = null;
        try {
            connectorSession = gendocAPI.createConnectorSession();
            File file = null;
            try{
                String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPag));
                
                FileTemplateManager templateManager = null;
//                FileTemporaryManager temporaryManager = null;
                // Obtiene el manejador de plantillas
                templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
                // Obtiene el manejador de ficheros temporales
//                temporaryManager = FileTemporaryManager.getInstance();
                
                //Se almacena documento
                String fileName = FileTemporaryManager.getInstance().newFileName("." +extension);
                //fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
                
                //String fileNamePath = FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + fileName;
                String fileNamePath = templateManager.getFileMgrPath() + "/" + fileName;
                
                // Nombre de la plantilla
                String sName = Integer.toString(templateId) + "." + extension;
                
                //Control de plantillas por multientidad
                OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
                if (info != null){
                    String organizationId = info.getOrganizationId();
                    //Se a�ade el numExp al nombre de la plantilla para evitar colisiones al generar notificaciones simultaneamente desde
                    //dos expedientes distintos de la misma entidad
                    sName = organizationId + "_" + idPlantilla + "_" + sName; 
                }
                
                OutputStream out = new FileOutputStream(fileNamePath);
                gendocAPI.getDocument(connectorSession, infoPag, out);
                file = new File(fileNamePath);
                //File file2 = new File(FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sName);
                File file2 = new File(templateManager.getFileMgrPath() + "/" + sName);
                file.renameTo(file2);
                file.delete();
                                
                
                OutputStream out2 = new FileOutputStream(fileNamePath);
                gendocAPI.getDocument(connectorSession, infoPag, out2);
                File file3 = new File(fileNamePath);
                File file4 = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sName);
                file3.renameTo(file4);
                file3.delete();
                                
                return sName;
            } catch (FileNotFoundException e) {
                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al intentar obtener el documento, no existe.", e);
            }
        }finally {
            if (connectorSession != null) {
                gendocAPI.closeConnectorSession(connectorSession);
            }
        }
    } 
}