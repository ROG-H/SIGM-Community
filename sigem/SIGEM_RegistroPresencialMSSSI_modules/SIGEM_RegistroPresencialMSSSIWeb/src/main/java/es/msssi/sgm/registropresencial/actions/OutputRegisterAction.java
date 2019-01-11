/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import core.web.server.bean.ResponseValue;
import core.web.server.bean.ServerResponse;
import core.web.server.bean.StatusInfo;
import ieci.tecdoc.sgm.backoffice.utils.Utilidades;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;



import org.springframework.context.ApplicationContext;

import servcert.msssi.es.tipos.PeticionValidacionType;
import servcert.msssi.es.tipos.RespuestaValidacionType;
import beans.DocumentArchiveResponse;
import beans.DocumentDownloadResponse;
import beans.RemoteSignBeginResponse;
import beans.RemoteSignEndResponse;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Idocarchhdr;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxPK;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.DtrFdrResults;
import com.ieci.tecdoc.common.isicres.UpdHisFdrResults;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.usecase.book.xml.AsocRegsResults;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import core.tools.Conversor;
import core.tools.FileNameUtils;
import core.tools.codec.Base64Codec;
import core.tools.hasher.HashType;
import core.tools.hasher.Hasher;
import es.ieci.tecdoc.isicres.api.business.manager.impl.RegistroManagerImpl;
import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralSalidaVO;
import es.mscbs.wsclient.servcert.facade.ServcertWSService;
import es.mscbs.wsclient.servcert.utils.ServcertClientUtils;
import es.msssi.sgm.registropresencial.beans.AsocRegisterBean;
import es.msssi.sgm.registropresencial.beans.Configuration;
import es.msssi.sgm.registropresencial.beans.ContentTypeEnum;
import es.msssi.sgm.registropresencial.beans.ContentTypeIRPermitEnum;
import es.msssi.sgm.registropresencial.beans.FieldsCopyOutputRegEnum;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.InterestedBo;
import es.msssi.sgm.registropresencial.businessobject.OutputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.connector.AntivirusConnector;
import es.msssi.sgm.registropresencial.connector.PFEConnector;
import es.msssi.sgm.registropresencial.daos.UserDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPOutputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.UtilsHash;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.service.SIGMServiceManager;

/**
 * Action de registro de salida.
 *
 * @author cmorenog
 */
public class OutputRegisterAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OutputRegisterAction.class.getName());
    private boolean readOnly = false;
    private boolean readOnlyIR = false;
    private boolean enableCompulsaValue = false;
    private OutputRegisterBo outputRegisterBo;
    private RegisterBo registerBo;
    private RegisterDocumentsBo registerDocumentsBo;
    private OutputRegisterBean outputRegisterBean;
    private ScrRegstate book;
    private Integer registerCopy;
    private String registerNumCopy;
    private List<UpdHisFdrResults> listUpdates;
    private List<DtrFdrResults> listHistDistribution;
    private List<IntercambioRegistralEntradaVO> listHistInputInterchange;
    private List<IntercambioRegistralSalidaVO> listHistOutputInterchange;
    private List<Axdoch> listDocuments;
    private RegInterchangeBo regInterchangeBo = null;
    /**
     * Implementaci�n de las funcionalidades de distribuci�n.
     */
    private DistributionBo distributionBo;
    /**
     * lista de departamentos para la distribuci�n.
     */
    private List<Iuserdepthdr> listDepartament = null;
    /**
     * Origen del formulario. B:B�squeda de registros D:Distribuci�n
     * IR:Intercambio Registral
     */
    private String origen = "B";
    private List<AxSf> asocReg;
    private AxSf asocRegPrim;
    private Map<String, Idocarchhdr> idocs;
    private AsocRegisterBean asocRegisterBean = new AsocRegisterBean();
    private int myCurrentTab = 0;
    private String selectedDocSIR = null;
    private String validateDocument = null;
    private String contentDocument = null;
    private String signDocument = null;
    private String certificateDocument = null;
    DocumentoElectronicoAnexoVO documentoElectronico = new DocumentoElectronicoAnexoVO();
    private Axdoch selectDocument;
    private String listNameDocument = null;
    // DISTRIBUCION
    /**
     * tipo de destino seleccionado en la distribuci�n.
     */
    private int typeDestinoRedis = 2;
    /**
     * Motivo de la distribuci�n.
     */
    private String motivoDistribucion = null;
    /**
     * departamento seleccionado en la distribuci�n.
     */
    private Iuserdepthdr selectdestinoRedisDepartamentos = null;
    private boolean canDistr = true;
    private UploadedFile fileUpload = null;
    private boolean isOfficial = true;
    private ScrOrg selectedDestOrg;
    private ScrOrg selectedOriginOrg;
    private boolean saveInterested = false;
    ContextoAplicacionVO contextoAplicacion = null;
    private AntivirusConnector antivirusConnector;
    private static ApplicationContext appContext;

    // Cliente Firma
    private UploadedFile file;
    private String fileBase64;
    private String fileName;
    private String jsonServerResponse;
    private String certChain;
    private String csv;
    private byte[] signedData = null;
    private String fileNameDig = null;
    /**
     * Indica si se ha activado el check de documentos leidos.
     */
    private boolean readCheck;
    private String taskId;

    
    static {
        appContext =
                RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
                        .getApplicationContext();
    }
    
    /**
     * Constructor. Valida el registro.
     *
     * @throws SessionException    si ha habido un problema de sesi�n.
     * @throws ValidationException Si ha habido un problema de validaci�n de par�metros.
     */
    public OutputRegisterAction() throws SessionException, ValidationException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(
                    SessionException.ERROR_SESSION_EXPIRED);
        }
        try {
            validateIdBook(
                    book.getId(), useCaseConf);
        } catch (BookException bookException) {
            LOG.error(
                    ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(
                    null, bookException, null);
        }
        if (regInterchangeBo == null) {
            regInterchangeBo = new RegInterchangeBo();
        }
        if (facesContext.getExternalContext().getFlash().get(
                "registerSelect") != null) {
            outputRegisterBean = new OutputRegisterBean();
            outputRegisterBean.setFdrid((Integer) FacesContext.getCurrentInstance()
                    .getExternalContext().getFlash().get(
                            "registerSelect"));
            if (facesContext.getExternalContext().getFlash().get("distribucionIn") != null) {
                origen = "DI";
            } else {
                if (facesContext.getExternalContext().getFlash().get("distribucionOut") != null) {
                    origen = "DO";
                } else {
                    if (facesContext.getExternalContext().getFlash().get(
                            "intercambio") != null) {
                        origen = "IR";
                    } else {
                        if (facesContext.getExternalContext().getFlash().get(
                                "intercambioR") != null) {
                            origen = "I";
                        }
                    }
                }
            }
        }

	/* EDICION O CONSULTA */
        if (outputRegisterBean != null &&
                outputRegisterBean.getFdrid() != null) {
            validateRegister(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid());
            loadOutputRegister();
        } else {
        /* COPIAR */
            if (facesContext.getExternalContext().getFlash().get(
                    "registerCopy") != null) {

                registerCopy = ((Integer) facesContext.getExternalContext().getFlash().get(
                        "registerCopy"));
                registerNumCopy = ((String) facesContext.getExternalContext().getFlash().get(
                        "registerNumCopy"));
                try {
                    copyOutputRegister();
                } catch (SessionException sessionException) {
                    LOG.error(
                            ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
                    Utils.redirectToErrorPage(
                            null, sessionException, null);
                }
            }
        }
        if (outputRegisterBean != null && outputRegisterBean.getFld8() != null && !"1".equals(outputRegisterBean.getFld8().getScrTypeadm().getId())) {
            isOfficial = false;
        }

        //miramos si es funcionario
        // Recuperamos la cach�
        try {
            CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
                    useCaseConf.getSessionID());
            enableCompulsaValue = (Boolean)cacheBag.get(ConstantesGestionUsuariosBackOffice.PARAMETRO_FUNCIONARIO_HABILITADO);
        } catch (Exception e) {
            LOG.error("Error al recoger Parametro de funcionario habilitado", e);
        }
    }

    /**
     * Borra los datos del interesado y crea uno vac�o.
     */
    public void reinit() {
        Interesado interesado = new Interesado();
        interesado.setTipo(useCaseConf.getInterestedType());
        outputRegisterBean.setInteresado(interesado);
    }

    @PreDestroy
    public void destroy1() {
        deleteSession("reportsLabelAction");
        if (outputRegisterBean != null) {
            RegistroManagerImpl registroManagerImpl = new RegistroManagerImpl();
            UsuarioVO usuario = new UsuarioVO();
            if (outputRegisterBean.getFdrid() != null) {
                usuario =
                        (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                .getSessionMap().get("USERVO");
                IdentificadorRegistroVO identificadorRegistro =
                        new IdentificadorRegistroVO(String.valueOf(outputRegisterBean.getFdrid()), String.valueOf(book.getIdocarchhdr().getId()));
                registroManagerImpl.unlockRegistro(usuario, identificadorRegistro);
            }
        }
    }

    /**
     * guarda en sesion el valor por defecto del tipo de interesado.
     */
    public void saveInterestedTypeDefault() {
        if (outputRegisterBean.getInteresado() != null) {
            useCaseConf.setInterestedType(outputRegisterBean.getInteresado().getTipo());
            saveUseCaseConf(useCaseConf);
        }
        saveInterested = true;
    }

    /**
     * guarda en sesion el valor por defecto del tipo de interesado.
     */
    public void saveInterested() {
        saveInterested = true;
    }

    /**
     * Crea un nuevo Bean y un nuevo Business Object de registro de salida.
     */
    @PostConstruct
    public void create() {
        if (outputRegisterBo == null) {
            outputRegisterBo = new OutputRegisterBo();
        }
        if (outputRegisterBean == null) {
            outputRegisterBean = new OutputRegisterBean();
        }
    }

    /**
     * Limpia el formulario y la tabla de resultados.
     */
    public void limpiar() {
        init();
        deleteSession("reportsLabelAction");
        Application application = facesContext.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(
                facesContext, facesContext.getViewRoot().getViewId());
        facesContext.setViewRoot(viewRoot);
        facesContext.renderResponse(); // Optional
    }

    /**
     * Da de alta un nuevo registro.
     *
     * @throws BookException       si ha habido alg�n problema con el libro de registro.
     * @throws SessionException    si ha habido alg�n problema con la sesi�n.
     * @throws ValidationException si ha habido alg�n problema con en la validaci�n.
     */
    public void saveOutputRegister()
            throws BookException, SessionException, ValidationException {

        if (book == null) {
            throw new SessionException(
                    SessionException.ERROR_SESSION_EXPIRED);
        }
        // validaciones de seguridad
        try {
            ValidationBo.validationSecurityUser(
                    book.getId(), null, useCaseConf);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                    rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpGenericException, null, null);
        }
        deleteSession("reportsLabelAction");
        loadDocSirSelected();
        if (outputRegisterBean.getFdrid() != null) {
            try {
                Integer idOffice = null;
                if (saveInterested) {
                    try {
                        contextoAplicacion =
                                ContextoAplicacionUtil
                                        .getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
                                                .getExternalContext().getRequest());
                        BaseOficinaVO oficina = contextoAplicacion.getOficinaActual();
                        idOffice = Integer.parseInt(oficina.getId());
                    } catch (TecDocException rpGenericException) {
                        idOffice = null;
                        LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE + ". C�digo: "
                                + rpGenericException.getCode() + " . Mensaje: "
                                + rpGenericException.getMessage());
                    }
                }
                OutputRegisterBo.saveOrUpdateFolder(
                        useCaseConf, book.getId(), outputRegisterBean.getFdrid(), null,
                        outputRegisterBean, outputRegisterBean.getInteresados(), null, saveInterested, idOffice);
                saveInterested = false;
            } catch (RPOutputRegisterException rpOutputRegisterException) {
                LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpOutputRegisterException.getCode().getCode() + " . Mensaje: " +
                        rpOutputRegisterException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpOutputRegisterException, null, null);
            } catch (RPGenericException rpGenericException) {
                LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                        rpGenericException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpGenericException, null, null);
            }
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Se ha guardado el Registro Correctamente",
                            "Se ha guardado el Registro Correctamente"));
        } else {
            try {
                outputRegisterBean.setFdrid(OutputRegisterBo.saveOrUpdateFolder(
                        useCaseConf, book.getId(), null, null, outputRegisterBean,
                        outputRegisterBean.getInteresados(), null));
            } catch (RPOutputRegisterException rpOutputRegisterException) {
                LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpOutputRegisterException.getCode().getCode() + " . Mensaje: " +
                        rpOutputRegisterException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpOutputRegisterException, null, null);
            } catch (RPGenericException rpGenericException) {
                LOG.error(ErrorConstants.SAVE_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                        rpGenericException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpGenericException, null, null);
            }
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Se ha creado el Registro Correctamente.",
                            "Se ha creado el Registro Correctamente."));
        }
        loadOutputRegister();
    }

    /**
     * Carga un registro.
     */
    public void loadOutputRegister() {
        try {
            init();
            deleteSession("reportsLabelAction");
            book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                    KeysRP.J_BOOK);
            if (book == null) {
                throw new SessionException(
                        SessionException.ERROR_SESSION_EXPIRED);
            }

	    /* MODIFICACION */
            if (outputRegisterBean.getFdrid() != null) {
                Boolean readOnlyFlash = false;
                if (facesContext.getExternalContext().getFlash().get(
                        "readOnly") != null) {
                    readOnlyFlash = (Boolean) facesContext.getExternalContext().getFlash().get(
                            "readOnly");
                }
                if (registerBo == null) {
                    registerBo = new RegisterBo();
                }
                if (outputRegisterBo == null) {
                    outputRegisterBo = new OutputRegisterBo();
                }
                try {
                    readOnly = registerBo.readOnly(
                            readOnlyFlash, outputRegisterBean.getFdrid(), useCaseConf, book.getId());
                    distributionBo = new DistributionBo();
                    try {
                        canDistr =
                                distributionBo
                                        .canDistributionRegister(useCaseConf, outputRegisterBean
                                                .getFdrid(), book.getIdocarchhdr().getId());
                    } catch (RPDistributionException e) {
                        LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". C�digo: "
                                + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
                    }
                    LOG.info("Cargando datos del registro " +
                            outputRegisterBean.getFdrid());
                    outputRegisterBean = outputRegisterBo.loadOutputRegisterBean(
                            useCaseConf, book, outputRegisterBean.getFdrid());
                    loadSelectedDocSIR();
                    try {
                        readOnlyIR =
                                regInterchangeBo.isIntercambioRegistralNotEdit(
                                        Integer.toString(book.getId()),
                                        Integer.toString(outputRegisterBean.getFdrid()),
                                        String.valueOf(outputRegisterBean.getFld5().getId()));
                    } catch (RPRegistralExchangeException e1) {
                        LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE + ". C�digo: "
                                + e1.getCode().getCode() + " . Mensaje: " + e1.getShortMessage());
                    }
                    if (outputRegisterBean != null && outputRegisterBean.getFld7() != null) {
                        selectedOriginOrg = outputRegisterBean.getFld7();
                    }
                    if (outputRegisterBean != null && outputRegisterBean.getFld8() != null) {
                        selectedDestOrg = outputRegisterBean.getFld8();
                    }
                } catch (RPOutputRegisterException rpOutputRegisterException) {
                    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE +
                            ". C�digo: " + rpOutputRegisterException.getCode().getCode() +
                            " . Mensaje: " + rpOutputRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(
                            rpOutputRegisterException, null, null);
                } catch (RPRegisterException rpRegisterException) {
                    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE +
                            ". C�digo: " + rpRegisterException.getCode().getCode() + " . Mensaje: " +
                            rpRegisterException.getShortMessage());
                    Utils.redirectToErrorPage(
                            rpRegisterException, null, null);
                } catch (RPGenericException rpGenericException) {
                    LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE +
                            ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                            rpGenericException.getShortMessage());
                    Utils.redirectToErrorPage(
                            rpGenericException, null, null);
                }
            }
        } catch (SessionException sessionException) {
            LOG.error(
                    ErrorConstants.LOAD_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(
                    null, sessionException, null);
        }
    }

    /**
     * Obtiene los documentos asociados a un registro.
     */
    public void getRegisterAttachedDocuments() {
        boolean openFolderDtr = ("D".equals(origen))
                ? true : false;
        listDocuments = new ArrayList<Axdoch>();
        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        try {
            LOG.info("Cargando documentos asociados al registro " +
                    outputRegisterBean.getFdrid());
            listDocuments = registerDocumentsBo.getDocumentsBasicInfo(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid(), openFolderDtr);
            listNameDocument = "[";
            boolean primero = true;
            for (Axdoch doch : listDocuments) {
                if (primero) {
                    primero = false;
                } else {
                    listNameDocument += ",";
                }
                listNameDocument += "{\"NameDoc\":\"" + doch.getName() + "\"}";
            }
            listNameDocument += "]";
        } catch (RPRegisterException rPRegisterException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
        }
    }

    /**
     * Obtiene el hist�rico de un registro.
     */
    public void getRegisterHistoricalUpdates() {
        listUpdates = new ArrayList<UpdHisFdrResults>();
        try {
            LOG.info("Cargando hist�rico del registro " +
                    outputRegisterBean.getFdrid());
            listUpdates = registerBo.getUpdHisFdrResults(
                    useCaseConf, book, outputRegisterBean.getFdrid(), outputRegisterBean.getFld1());
        } catch (RPRegisterException rpRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpRegisterException.getCode().getCode() + " . Mensaje: " +
                    rpRegisterException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpRegisterException, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                    rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpGenericException, null, null);
        }
    }

    /**
     * Obtiene el hist�rico de distribuci�n de un registro.
     */
    public void getRegisterHistDistribution() {
        listHistDistribution = new ArrayList<DtrFdrResults>();
        try {
            LOG.info("Cargando hist�rico de distribuci�n del registro " +
                    outputRegisterBean.getFdrid());
            distributionBo = new DistributionBo();
            listHistDistribution = distributionBo.getHistDistribution(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid());
        } catch (BookException bookException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(
                    null, bookException, null);
        } catch (SessionException sessionException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(
                    null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(
                    null, validationException, null);
        }
    }

    /**
     * Obtiene el hist�rico de entrada y salida del Intercambio Registral.
     */
    public void getRegisterHistInputAndOutputInterchange() {
        listHistInputInterchange = new ArrayList<IntercambioRegistralEntradaVO>();
        listHistOutputInterchange = new ArrayList<IntercambioRegistralSalidaVO>();
        try {
            init();
            book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                    KeysRP.J_BOOK);
            if (book == null) {
                throw new SessionException(
                        SessionException.ERROR_SESSION_EXPIRED);
            }

            if (regInterchangeBo == null) {
                regInterchangeBo = new RegInterchangeBo();
            }
            LOG.info("Cargando hist�rico de Intecambio Registral del registro " +
                    outputRegisterBean.getFdrid());
            listHistInputInterchange =
                    regInterchangeBo.getHistorialIntercambioRegistralEntrada(
                            String.valueOf(book.getId()),
                            String.valueOf(outputRegisterBean.getFdrid()), String.valueOf(outputRegisterBean.getFld5().getId()));
            listHistOutputInterchange =
                    regInterchangeBo.getHistorialIntercambioRegistralSalida(
                            String.valueOf(book.getId()),
                            String.valueOf(outputRegisterBean.getFdrid()), String.valueOf(outputRegisterBean.getFld5().getId()));
        } catch (RPRegistralExchangeException rPRegisterException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
        } catch (SessionException sessionException) {
            LOG.error(
                    ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(
                    null, sessionException, null);
        }
    }

    /**
     * Obtiene los registros asociados a un registro.
     */
    @SuppressWarnings("unchecked")
    public void getRegisterAssociatedRegisters() {
        try {
            LOG.info("Cargando registros asociados al registro " +
                    outputRegisterBean.getFdrid());
            Object[] regAsoc = registerBo.getAsocReg(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid());
            if (regAsoc != null && (!((Map<AxPK, AxSf>) regAsoc[1]).isEmpty()
                    || !((Map<AxPK, AxSf>) regAsoc[2]).isEmpty())) {
                idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                asocReg = registerBo.getListAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[1], idocs);
                asocRegPrim = registerBo.getAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[2], idocs);
            }
        } catch (RPRegisterException rpRegisterException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpRegisterException.getCode().getCode() + " . Mensaje: " +
                    rpRegisterException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpRegisterException, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                    rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpGenericException, null, null);
        }
    }

    private void loadDocSirSelected() {
        if (selectedDocSIR != null && !"".equals(selectedDocSIR)) {
            outputRegisterBean.setFld504(null);
            outputRegisterBean.setFld505(null);
            outputRegisterBean.setFld506(null);
            if (selectedDocSIR.equals("fld504")) {
                outputRegisterBean.setFld504(1);
            } else {
                if (selectedDocSIR.equals("fld505")) {
                    outputRegisterBean.setFld505(1);
                } else {
                    if (selectedDocSIR.equals("fld506")) {
                        outputRegisterBean.setFld506(1);
                    }
                }
            }
        }
    }

    private void loadSelectedDocSIR() {
        if (outputRegisterBean != null) {
            if (outputRegisterBean.getFld504() != null) {
                selectedDocSIR = "fld504";
            } else {
                if (outputRegisterBean.getFld505() != null) {
                    selectedDocSIR = "fld505";
                } else {
                    if (outputRegisterBean.getFld506() != null) {
                        selectedDocSIR = "fld506";
                    }
                }
            }
        }
    }

    /**
     * Copia un registro.
     *
     * @throws SessionException si ha habido alg�n problema con la sesi�n.
     */
    public void copyOutputRegister()
            throws SessionException {
        init();
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(
                    SessionException.ERROR_SESSION_EXPIRED);
        }

        if (registerCopy != null) {
            if (outputRegisterBo == null) {
                outputRegisterBo = new OutputRegisterBo();
            }
            try {
                //loadDocSirSelected();

                Configuration configuration =
                        (Configuration) facesContext.getExternalContext().getSessionMap().get(KeysRP.CONFIGURATIONUSER);
                String[] fieldsCopy = null;
                if (configuration != null) {
                    fieldsCopy = configuration.getFieldOutputSelected();
                }
                outputRegisterBean = outputRegisterBo.copyOutputRegisterBean(
                        useCaseConf, book, registerCopy, fieldsCopy, false);
                loadSelectedDocSIR();
                FacesContext.getCurrentInstance().addMessage(
                        "Registro Copiado", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Se han copiado los siguientes datos del registro " +
                                registerNumCopy + listField(fieldsCopy), "Se han copiado los siguientes datos del registro " +
                                registerNumCopy + listField(fieldsCopy)));

                if (outputRegisterBean != null && outputRegisterBean.getFld7() != null) {
                    selectedOriginOrg = outputRegisterBean.getFld7();
                }
                if (outputRegisterBean != null && outputRegisterBean.getFld8() != null) {
                    selectedDestOrg = outputRegisterBean.getFld8();
                }
            } catch (RPOutputRegisterException rpOutputRegisterException) {
                LOG.error(ErrorConstants.COPY_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpOutputRegisterException.getCode().getCode() + " . Mensaje: " +
                        rpOutputRegisterException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpOutputRegisterException, null, null);
            } catch (RPGenericException rpGenericException) {
                LOG.error(ErrorConstants.COPY_OUTPUT_REGISTER_ERROR_MESSAGE +
                        ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                        rpGenericException.getShortMessage());
                Utils.redirectToErrorPage(
                        rpGenericException, null, null);
            }
        }
    }

    /**
     * lista la descripci�n de los campos copiados.
     *
     * @param fieldsCopy array con los campos
     * @return string con las descripciones concatenadas.
     */
    private String listField(String[] fieldsCopy) {
        ResourceRP resourceRP = ResourceRP.getInstance(useCaseConf.getLocale());
        String result = "";
        if (fieldsCopy != null && fieldsCopy.length > 0) {
            result = ":<br/>";
            boolean first = true;
            String text = null;
            for (String field : fieldsCopy) {
                text = resourceRP.getProperty(FieldsCopyOutputRegEnum.getFieldsCopy(field).getName());
                if (first) {
                    result += text;
                    first = false;
                } else {
                    result += ", " + text;
                }
            }
        }
        return result;
    }

    /**
     * borra un registro asociado.
     *
     * @throws SessionException si ha habido alg�n problema con la sesi�n.
     */
    @SuppressWarnings("unchecked")
    public void deleteAssociation()
            throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(
                    SessionException.ERROR_SESSION_EXPIRED);
        }
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        if (registerBo == null) {
            registerBo = new RegisterBo();
        }
        Integer idReg = new Integer(
                params.get("idReg"));
        Integer idLibro = new Integer(
                params.get("idLibro"));
        try {
            registerBo.deleteAssociation(
                    useCaseConf, idLibro, idReg);
            Object[] regAsoc = registerBo.getAsocReg(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid());
            if (regAsoc != null) {
                idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                asocReg = registerBo.getListAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[1], idocs);
                asocRegPrim = registerBo.getAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[2], idocs);
            }
            FacesContext.getCurrentInstance().addMessage(
                    "Asociar Registros",
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Se ha desasociado el registro correctamente",
                            "Se ha desasociado el registro correctamente"));
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
            Utils.redirectToErrorPage(
                    e, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.DELETE_ASOC_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                    rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpGenericException, null, null);
        }
    }

    /**
     * asocia un registro.
     *
     * @throws SessionException si ha habido alg�n problema con la sesi�n.
     */
    @SuppressWarnings("unchecked")
    public void saveAsocRegister()
            throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
                KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(
                    SessionException.ERROR_SESSION_EXPIRED);
        }

        if (registerBo == null) {
            registerBo = new RegisterBo();
        }
        try {
            List<AsocRegsResults> listAsoc = registerBo.saveAssociation(
                    useCaseConf, asocRegisterBean, outputRegisterBean.getFdrid(), book.getId());

            if (listAsoc == null ||
                    listAsoc.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(
                        "Asociar Registros",
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "No se ha encontrado el registro a asociar",
                                "No se ha encontrado el registro a asociar"));
            } else {

            Object[] regAsoc = registerBo.getAsocReg(
                    useCaseConf, book.getId(), outputRegisterBean.getFdrid());
            if (regAsoc != null) {
                idocs = (Map<String, Idocarchhdr>) regAsoc[0];
                asocReg = registerBo.getListAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[1], idocs);
                asocRegPrim = registerBo.getAsoReg(
                        (Map<AxPK, AxSf>) regAsoc[2], idocs);
                FacesContext.getCurrentInstance().addMessage(
                        "Asociar Registros",
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "Se ha asociado el registro correctamente",
                                "Se ha asociado el registro correctamente"));
            }
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
            Utils.redirectToErrorPage(
                    e, null, null);
        } catch (RPGenericException rpGenericException) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE +
                    ". C�digo: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
                    rpGenericException.getShortMessage());
            Utils.redirectToErrorPage(
                    rpGenericException, null, null);
        }
    }

    /**
     * Redirecciona a la p�gina de intercambioRegistral.
     */
    public void goRegInterchangeForm() {
        deleteSession("reportsLabelAction");

        if (comprobarFormatoAnexos()) {
            if (comprobarInteresadosIR()) {
                if (comprobarSopFisicoIR()) {
                    Map<String, Object> parameter = new HashMap<String, Object>();
                    parameter.put(
                            "registerId", outputRegisterBean.getFdrid());
                    if (outputRegisterBean.getFld504() != null) {
                        parameter.put("docFisica", 1);
                    }
                    if (outputRegisterBean.getFld505() != null) {
                        parameter.put("docFisica", 2);
                    }
                    if (outputRegisterBean.getFld506() != null) {
                        parameter.put("docFisica", 3);
                    }
                    Utils.navigate(
                            parameter, false, "searchDestinationRegInterchange.xhtml");
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            "Intercambio Registral",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "No se puede enviar el registro. El campo Documentaci�n Soporte es obligatorio para los env�os por intercambio registral.",
                                    "No se puede enviar el registro. El campo Documentaci�n Soporte es obligatorio para los env�os por intercambio registral."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        "Intercambio Registral",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "No se puede enviar el registro. No dispone de interesados v�lidos. Recuerde que debe guardar el registro si ha realizado cambios.",
                                "No se puede enviar el registro. No dispone de interesados v�lidos. Recuerde que debe guardar el registro si ha realizado cambios."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    "Intercambio Registral",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "No se puede enviar el registro. Contiene documentos adjuntos en formatos .zip, .gzip o .rar.",
//                            "No se puede enviar el registro. Contiene documentos adjuntos en formatos .zip, .gzip o .rar."));
                    		"No se puede enviar el registro. Para los documentos adjuntos s�lo se permiten los formatos .pptx, .jpg, .jpeg, .txt, .xml, .xsig, .xlsx, .odg, .odt, .ods, .pdf, .odp, .png, .svg, .tiff, .docx, .rtf.",
                            "No se puede enviar el registro. Para los documentos adjuntos s�lo se permiten los formatos .pptx, .jpg, .jpeg, .txt, .xml, .xsig, .xlsx, .odg, .odt, .ods, .pdf, .odp, .png, .svg, .tiff, .docx, .rtf."));
        }
    }

    /**
     * Comprobar si el registro tiene interesados y si son v�lidos.
     *
     * @return true si los interesados son v�lidos.
     */
    private boolean comprobarSopFisicoIR() {
        init();
        boolean result = true;
        if (outputRegisterBean.getFld504() == null
                && outputRegisterBean.getFld505() == null && outputRegisterBean.getFld506() == null) {
            result = false;
        }
        return result;
    }

    /**
     * Comprobar si el registro tiene interesados y si son v�lidos.
     *
     * @return true si los interesados son v�lidos.
     */
    private boolean comprobarInteresadosIR() {
        init();
        boolean result = true;
        InterestedBo interestedBo = new InterestedBo();
        List<Interesado> interadosCon =
                interestedBo.getAllInterested(book.getId(), outputRegisterBean.getFdrid(), useCaseConf);
        if (interadosCon != null &&
                !interadosCon.isEmpty()) {
            for (Interesado inter : interadosCon) {
                if (inter.getDocIndentidad() == null || "".equals(inter.getDocIndentidad())
                        || inter.getTipodoc() == null || "".equals(inter.getTipodoc())) {
                    result = false;
                } else {
                    if (inter.getRepresentante() != null && !"N".equals(inter.getRepresentante().getTipo())) {
                        if (inter.getRepresentante().getDocIndentidad() == null
                                || "".equals(inter.getRepresentante().getDocIndentidad())
                                || inter.getRepresentante().getTipodoc() == null
                                || "".equals(inter.getRepresentante().getTipodoc())) {
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Comprobar si el registro tiene documentos con formato admitidos.
     *
     * @return true si los documentos cumple los requisitos.
     */
    private boolean comprobarFormatoAnexos() {
        init();


        if (listDocuments == null) {
            getRegisterAttachedDocuments();
        }
        return validateAttachedDocuments();
    }


    /**
     * Obtiene los documentos asociados a un registro.
     */
    public boolean validateAttachedDocuments() {
        boolean result = true;

        for (Axdoch doch : listDocuments) {

            List<Axpageh> pages = doch.getPages();
            for (Axpageh page : pages) {
                String extension = FolderFileSession.getExtensionFile(doch.getName(), book.getId().toString(), doch.getFdrid(), page.getPageId(), useCaseConf.getEntidadId());
                if (ContentTypeIRPermitEnum.getIfPresentExt(extension.toUpperCase()) == null) {
                    LOG.info("Content Type del documento no permitido para intercambio registral " + extension);
                    result = false;
                }

                String mimeType = page.getMimeType();

                if ((mimeType != null) && result) {
                    if (ContentTypeIRPermitEnum.getIfPresentMime(mimeType.toUpperCase()) == null) {
                        LOG.info("Content Type del documento no permitido para intercambio registral " + mimeType);
                        result = false;
                    }
                }
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    /**
     * Adjunta un documento al registro.
     *
     * @param event Evento de Faces con la informaci�n del proceso.
     */
    public void upload() {
        JSONParser parser = new JSONParser();

        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        deleteSession("reportsLabelAction");
        try {
            boolean exist = false;
            for (Axdoch doc : listDocuments) {
                if (doc.getName().equals(documentoElectronico.getName())) {
                    exist = true;
                }
            }
            if (!exist) {
                if (documentoElectronico.getName().length() <= 80) {
                    AxSf registerData = Utils.mappingOutputRegisterToAxSF(outputRegisterBean);
                    //identificadorDocumentoElectronicoAnexoVO
                    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO =
                            new IdentificadorDocumentoElectronicoAnexoVO();
                    identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String.valueOf(book.getIdocarchhdr().getId())));
                    identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String.valueOf(outputRegisterBean.getFdrid())));
                    documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);

                    //CODE NAME
                    String codeName = ("codeName" + documentoElectronico.getName());
                    codeName = StringUtils.abbreviate(codeName, 21);
                    documentoElectronico.setCodeName(codeName);

                    //extension
                    String extension = documentoElectronico.getName().substring(documentoElectronico.getName().lastIndexOf(".") + 1,
                            documentoElectronico.getName().length());
                    documentoElectronico.setExtension(extension);

                    //contenido
                    DocumentoElectronicoAnexoContenidoVO contenido = new DocumentoElectronicoAnexoContenidoVO();
                    // Contenido del fichero
                    JSONArray arrayContent = (JSONArray) parser.parse(contentDocument);

                    LinkedList<byte[]> blocksContent = new LinkedList<byte[]>();
                    JSONObject blockContent = null;
                    for (int i = 0; i < arrayContent.size(); i++) {
                        blockContent = (JSONObject) arrayContent.get(i);
                        blocksContent.add(Base64.decodeBase64((String) blockContent.get("block")));
                    }
                    ByteArrayOutputStream osContent = new ByteArrayOutputStream();
                    for (byte[] b : blocksContent) {
                        osContent.write(b, 0, b.length);
                    }
                    byte[] content = osContent.toByteArray();
                    osContent.close();
                    // ANTIVIRUS
            		    /*try 
            		    {
                		antivirusConnector = (AntivirusConnector) RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext().getBean("antivirusConnector");
            			antivirusConnector.scan(documentoElectronico.getName(), content);
            		    } catch (RPGenericException e) {
            			LOG.error(ErrorConstants.SCAN_FILE_ANTIVIRUS_CALL_ERROR_MESSAGE + ". C�digo: "
                			    + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
                		Utils.redirectToErrorPage(e, null, null);
            		    }*/

                    contenido.setContent(content);

                    documentoElectronico.setContenido(contenido);

                    //TIPO MIME
                    // MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
                    // documentoElectronico.setMimeType(mimeTypesMap.getContentType());
                    try {
                        if (ContentTypeEnum.valueOf(extension.toUpperCase()) != null
                                && ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType().length() <= 20) {
                            documentoElectronico.setMimeType(ContentTypeEnum.valueOf(extension.toUpperCase()).getContentType());
                        }
                    } catch (Exception e) {
                    }
                    //TIPO DOCUMENTO
                    documentoElectronico.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO);

                    if (validateDocument != null) {
                        documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(validateDocument.substring(1, 2))));
                    }

                    //datos hash
                    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento = new DocumentoElectronicoAnexoDatosFirmaVO();

                    String hashDocumento = UtilsHash.getBase64Sring(Hasher.generateHash(content, HashType.SHA1));
                    datosFirmaDocumento.setHashAlg(HashType.SHA1.getValue());
                    datosFirmaDocumento.setHash(hashDocumento);

                    documentoElectronico.setDatosFirma(datosFirmaDocumento);
                    //FIRMAS
                    List<DocumentoElectronicoAnexoVO> firmas = new ArrayList<DocumentoElectronicoAnexoVO>();
                    DocumentoElectronicoAnexoVO firmaDocumento = new DocumentoElectronicoAnexoVO();
                    if (signDocument != null && !"".equals(signDocument)) {
                        IdentificadorDocumentoElectronicoAnexoVO idFima = new IdentificadorDocumentoElectronicoAnexoVO();

                        idFima.setIdLibro(identificadorDocumentoElectronicoAnexoVO.getIdLibro());
                        idFima.setIdRegistro(identificadorDocumentoElectronicoAnexoVO.getIdRegistro());
                        firmaDocumento.setId(idFima);
                        String codeNameFirma = "codeNameSign" + documentoElectronico.getName();
                        codeNameFirma = StringUtils.abbreviate(codeNameFirma, 21);
                        firmaDocumento.setCodeName(codeNameFirma);

                        //contenido
                        DocumentoElectronicoAnexoContenidoVO contenidoFirma = new DocumentoElectronicoAnexoContenidoVO();
                        JSONArray arraySignContent = (JSONArray) parser.parse(signDocument);

                        LinkedList<byte[]> blocksSignContent = new LinkedList<byte[]>();
                        JSONObject blockSignContent = null;
                        for (int i = 0; i < arraySignContent.size(); i++) {
                            blockSignContent = (JSONObject) arraySignContent.get(i);
                            blocksSignContent.add(Base64.decodeBase64((String) blockSignContent.get("block")));
                        }
                        ByteArrayOutputStream osSignContent = new ByteArrayOutputStream();
                        for (byte[] b : blocksSignContent) {
                            osSignContent.write(b, 0, b.length);
                        }
                        byte[] signcontent = osSignContent.toByteArray();
                        osSignContent.close();
                        contenidoFirma.setContent(signcontent);
                        firmaDocumento.setContenido(contenidoFirma);
                        //extension firma
                        String extensionFirma = "csig";
                        firmaDocumento.setExtension(extensionFirma);

                        //datos hash
                        DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma = new DocumentoElectronicoAnexoDatosFirmaVO();
                        String hashDocumentoFirma = UtilsHash.getBase64Sring(Hasher.generateHash(signcontent, HashType.SHA1));
                        datosFirmaDocumentoFirma.setHashAlg(HashType.SHA1.getValue());
                        datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        //NOMBRE FICHERO FIRMA

                        String nameFirma = "FIRMA.csig";
                        firmaDocumento.setName(nameFirma);
                        firmaDocumento.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
                        firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.ORIGINAL);

                        if (certificateDocument != null) {
                            datosFirmaDocumentoFirma.setCertificado(certificateDocument);
                        }
                              /* if (anexoVerificado.getFirma().getFirma() != null){
                                   String firma=new String(anexoVerificado.getFirma().getFirma());
                                   datosFirmaDocumentoFirma.setFirma(firma);
                               }*/
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        firmas.add(firmaDocumento);
                        documentoElectronico.setFirmas(firmas);


                    }
        		/*else {
        		    FacesContext.getCurrentInstance().addMessage(
        			    null,
        			    new FacesMessage(FacesMessage.SEVERITY_ERROR,
        				    "No se ha podido adjuntar el documento. Extensi�n no v�lida: "
        					    + documentoElectronico.getName(),
        				    "No se ha podido adjuntar el documento. Extensi�n no v�lida: "
        					    + documentoElectronico.getName()));
        		}*/
                    UsuarioVO usuario =
                            (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                    .getSessionMap().get("USERVO");
                    DocumentoElectronicoAnexoVO documentSaved =
                            registerDocumentsBo.saveDocuments(useCaseConf.getSessionID(), book.getId(),
                                    outputRegisterBean.getFdrid(), documentoElectronico, registerData,
                                    useCaseConf.getLocale(), useCaseConf.getEntidadId(), usuario);
                    if (documentSaved != null) {
                        getRegisterAttachedDocuments();
                        checkSize();
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "Se ha adjuntado el documento " + documentoElectronico.getName()
                                                + " correctamente", "Se ha adjuntado el documento "
                                        + documentoElectronico.getName() + " correctamente"));
                    }
                } else {
                    FacesContext
                            .getCurrentInstance()
                            .addMessage(
                                    null,
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_ERROR,
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 64 caracteres: "
                                                    + documentoElectronico.getName(),
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 64 caracteres: "
                                                    + documentoElectronico.getName()));
                }
            } else {
                ValidationBo.showDialog("Adjuntar", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe"));
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". C�digo: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
            Utils.redirectToErrorPage(e, null, null);
        } catch (ParseException e) {
            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ".  Mensaje: " + e.getMessage());

        } catch (IOException e) {
            LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ".  Mensaje: " + e.getMessage());
        } finally {
            documentoElectronico = new DocumentoElectronicoAnexoVO();
            validateDocument = null;
            contentDocument = null;
            signDocument = null;
            certificateDocument = null;

        }
    }


    /**
     * Alterna entre paneles del acorde�n de la p�gina mediante el evento
     * tabChangeEvent de Ajax.
     *
     * @param tabChangeEvent Evento de cambio de panel.
     */
    public void changeListener(
            TabChangeEvent tabChangeEvent) {
        deleteSession("reportsLabelAction");
        String tabId = tabChangeEvent.getTab().getId();
        if ("outputRegisterData".equals(tabId)) {
            this.setMyCurrentTab(0);
            if (outputRegisterBean == null) {
                loadOutputRegister();
            }
        } else if ("outputRegisterAttachedDocuments".equals(tabId)) {
            this.setMyCurrentTab(1);
            if (listDocuments == null) {
                getRegisterAttachedDocuments();
            }
        } else if ("outputRegisterHistorical".equals(tabId)) {
            this.setMyCurrentTab(2);
            // if (listUpdates == null) {
            getRegisterHistoricalUpdates();
            // }
        } else if ("outputRegisterDistributionHistorical".equals(tabId)) {
            this.setMyCurrentTab(NUMBER3);
            if (listHistDistribution == null) {
                getRegisterHistDistribution();
            }
        } else if ("outputRegisterInterchangeHistorical".equals(tabId)) {
            this.setMyCurrentTab(NUMBER4);
            if (listHistInputInterchange == null &&
                    listHistOutputInterchange == null) {
                getRegisterHistInputAndOutputInterchange();
            }
        } else if ("outputRegisterAssociatedRegisters".equals(tabId)) {
            this.setMyCurrentTab(NUMBER5);
            if (asocReg == null) {
                getRegisterAssociatedRegisters();
            }
        }
    }

    /**
     * borra el documento del registro.
     */
    public void deleteDocument() {
        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        try {
            deleteSession("reportsLabelAction");
            LOG.info("borramos el documento registro " + outputRegisterBean.getFdrid());
            if (selectDocument != null) {
                registerDocumentsBo.deleteDocument(useCaseConf, book.getId(),
                        outputRegisterBean.getFdrid(), selectDocument.getId().intValue());
                getRegisterAttachedDocuments();
            }
        } catch (RPRegisterException rPRegisterException) {
            LOG.error(ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, rPRegisterException);
        }
    }

    /**
     * Obtiene el valor del par�metro readOnly.
     *
     * @return readOnly valor del campo a obtener.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Guarda el valor del par�metro readOnlyIR.
     *
     * @param readOnlyIR valor del campo a guardar.
     */
    public void setReadOnlyIR(boolean readOnlyIR) {
        this.readOnlyIR = readOnlyIR;
    }

    /**
     * Obtiene el valor del par�metro readOnlyIR.
     *
     * @return readOnlyIR valor del campo a obtener.
     */
    public boolean isReadOnlyIR() {
        return readOnlyIR;
    }
    /**
     * Guarda el valor del par�metro readOnly.
     *
     * @param readOnly valor del campo a guardar.
     */
    public void setReadOnly(
            boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Obtiene el valor del par�metro registerCopy.
     *
     * @return registerCopy valor del campo a obtener.
     */
    public Integer getRegisterCopy() {
        return registerCopy;
    }

    /**
     * Guarda el valor del par�metro registerCopy.
     *
     * @param registerCopy valor del campo a guardar.
     */
    public void setRegisterCopy(
            Integer registerCopy) {
        this.registerCopy = registerCopy;
    }

    /**
     * Obtiene el valor del par�metro outputRegisterBo.
     *
     * @return outputRegisterBo valor del campo a obtener.
     */
    public OutputRegisterBo getOutputRegisterBo() {
        return outputRegisterBo;
    }

    /**
     * Guarda el valor del par�metro outputRegisterBo.
     *
     * @param outputRegisterBo valor del campo a guardar.
     */
    public void setInputRegisterBo(
            OutputRegisterBo outputRegisterBo) {
        this.outputRegisterBo = outputRegisterBo;
    }

    /**
     * Obtiene el valor del par�metro outputRegisterBean.
     *
     * @return outputRegisterBean valor del campo a obtener.
     */
    public OutputRegisterBean getOutputRegisterBean() {
        return outputRegisterBean;
    }

    /**
     * Guarda el valor del par�metro outputRegisterBean.
     *
     * @param outputRegisterBean valor del campo a guardar.
     */
    public void setOutputRegisterBean(
            OutputRegisterBean outputRegisterBean) {
        this.outputRegisterBean = outputRegisterBean;
    }

    /**
     * Obtiene el valor del par�metro listUpdates.
     *
     * @return listUpdates valor del campo a obtener.
     */
    public List<UpdHisFdrResults> getListUpdates() {
        return listUpdates;
    }

    /**
     * Guarda el valor del par�metro listUpdates.
     *
     * @param listUpdates valor del campo a guardar.
     */
    public void setListUpdates(
            List<UpdHisFdrResults> listUpdates) {
        this.listUpdates = listUpdates;
    }

    /**
     * Obtiene el valor del par�metro listHistDistribution.
     *
     * @return listHistDistribution valor del campo a obtener.
     */
    public List<DtrFdrResults> getListHistDistribution() {
        return listHistDistribution;
    }

    /**
     * Guarda el valor del par�metro listHistDistribution.
     *
     * @param listHistDistribution valor del campo a guardar.
     */
    public void setListHistDistribution(
            List<DtrFdrResults> listHistDistribution) {
        this.listHistDistribution = listHistDistribution;
    }

    /**
     * Obtiene el valor del par�metro origen.
     *
     * @return origen valor del campo a obtener.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Guarda el valor del par�metro origen.
     *
     * @param origen valor del campo a guardar.
     */
    public void setOrigen(
            String origen) {
        this.origen = origen;
    }

    /**
     * Obtiene el valor del par�metro asocReg.
     *
     * @return asocReg valor del campo a obtener.
     */
    public List<AxSf> getAsocReg() {
        return asocReg;
    }

    /**
     * Guarda el valor del par�metro asocReg.
     *
     * @param asocReg valor del campo a guardar.
     */
    public void setAsocReg(
            List<AxSf> asocReg) {
        this.asocReg = asocReg;
    }

    /**
     * Obtiene el valor del par�metro asocRegPrim.
     *
     * @return asocRegPrim valor del campo a obtener.
     */
    public AxSf getAsocRegPrim() {
        return asocRegPrim;
    }

    /**
     * Guarda el valor del par�metro asocRegPrim.
     *
     * @param asocRegPrim valor del campo a guardar.
     */
    public void setAsocRegPrim(
            AxSf asocRegPrim) {
        this.asocRegPrim = asocRegPrim;
    }

    /**
     * Obtiene el valor del par�metro idocs.
     *
     * @return idocs valor del campo a obtener.
     */
    public Map<String, Idocarchhdr> getIdocs() {
        return idocs;
    }

    /**
     * Guarda el valor del par�metro idocs.
     *
     * @param idocs valor del campo a guardar.
     */
    public void setIdocs(
            Map<String, Idocarchhdr> idocs) {
        this.idocs = idocs;
    }

    /**
     * Obtiene el valor del par�metro asocRegisterBean.
     *
     * @return asocRegisterBean valor del campo a obtener.
     */
    public AsocRegisterBean getAsocRegisterBean() {
        return asocRegisterBean;
    }

    /**
     * Guarda el valor del par�metro asocRegisterBean.
     *
     * @param asocRegisterBean valor del campo a guardar.
     */
    public void setAsocRegisterBean(
            AsocRegisterBean asocRegisterBean) {
        this.asocRegisterBean = asocRegisterBean;
    }

    /**
     * Obtiene el valor del par�metro listHistInputInterchange.
     *
     * @return listHistInputInterchange valor del campo a obtener.
     */
    public List<IntercambioRegistralEntradaVO> getListHistInputInterchange() {
        return listHistInputInterchange;
    }

    /**
     * Guarda el valor del par�metro listHistInputInterchange.
     *
     * @param listHistInputInterchange valor del campo a guardar.
     */
    public void setListHistInputInterchange(
            List<IntercambioRegistralEntradaVO> listHistInputInterchange) {
        this.listHistInputInterchange = listHistInputInterchange;
    }

    /**
     * Obtiene el valor del par�metro listHistOutputInterchange.
     *
     * @return listHistOutputInterchange valor del campo a obtener.
     */
    public List<IntercambioRegistralSalidaVO> getListHistOutputInterchange() {
        return listHistOutputInterchange;
    }

    /**
     * Guarda el valor del par�metro listHistOutputInterchange.
     *
     * @param listHistOutputInterchange valor del campo a guardar.
     */
    public void setListHistOutputInterchange(
            List<IntercambioRegistralSalidaVO> listHistOutputInterchange) {
        this.listHistOutputInterchange = listHistOutputInterchange;
    }

    /**
     * Obtiene el valor del par�metro listDocuments.
     *
     * @return listDocuments valor del campo a obtener.
     */
    public List<Axdoch> getListDocuments() {
        return listDocuments;
    }

    /**
     * Guarda el valor del par�metro listDocuments.
     *
     * @param listDocuments valor del campo a guardar.
     */
    public void setListDocuments(
            List<Axdoch> listDocuments) {
        this.listDocuments = listDocuments;
    }

    /**
     * Obtiene el valor del par�metro myCurrentTab.
     *
     * @return myCurrentTab valor del campo a obtener.
     */
    public int getMyCurrentTab() {
        return myCurrentTab;
    }

    /**
     * Guarda el valor del par�metro myCurrentTab.
     *
     * @param myCurrentTab valor del campo a guardar.
     */
    public void setMyCurrentTab(
            int myCurrentTab) {
        this.myCurrentTab = myCurrentTab;
    }

    /**
     * Obtiene el valor del par�metro registerNumCopy.
     *
     * @return registerNumCopy valor del campo a obtener.
     */
    public String getRegisterNumCopy() {
        return registerNumCopy;
    }

    /**
     * Guarda el valor del par�metro registerNumCopy.
     *
     * @param registerNumCopy valor del campo a guardar.
     */
    public void setRegisterNumCopy(
            String registerNumCopy) {
        this.registerNumCopy = registerNumCopy;
    }

    /**
     * M�todo que actualiza el campo origen con el valor seleccionado en la
     * b�squeda avanzada de organismos.
     */
    public void updateOrigin() {
        ScrOrg unidad = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
                "UNITSDIALOG") != null) {
            unidad =
                    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                            .get(
                                    "UNITSDIALOG");
            resetValues("accordion:outputRegisterDataForm:fld7");
            getOutputRegisterBean().setFld7(unidad);
            selectedOriginOrg = unidad;
        }
    }

    /**
     * M�todo que actualiza el campo destino con el valor seleccionado en la
     * b�squeda avanzada de organismos.
     */
    public void updateDestination() {
        ScrOrg unidad = null;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
                "UNITSDIALOG") != null) {
            unidad =
                    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                            .get(
                                    "UNITSDIALOG");
            resetValues("accordion:outputRegisterDataForm:fld8");
            getOutputRegisterBean().setFld8(unidad);
            selectedDestOrg = unidad;
        }
    }

    /**
     * Redirecciona a la p�gina de copiar desde el bot�n.
     */
    public void onRowSelectNavigateBottomCopy() {
        destroy1();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("registerCopy", outputRegisterBean.getFdrid());
        parameter.put("registerNumCopy", outputRegisterBean.getFld1());
        Utils.navigate(parameter, false, "outputRegister.xhtml");
    }

    /**
     * Redirecciona a la p�gina de nuevo desde el bot�n.
     */
    public void onRowSelectNavigateBottomNuevo() {
        clearView();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerCopy");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerSelect");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("readOnly");
        Utils.navigate("outputRegister.xhtml");
    }



    /**
     * Borra los datos de la distribuci�n y crea una vac�o.
     */
    @SuppressWarnings("unchecked")
    public void reinitDis() {
        try {
            deleteSession("reportsLabelAction");
            if (outputRegisterBean.getFld8() != null && outputRegisterBean.getFld8().getId() != null) {
                List<Integer> orgIds = new ArrayList<Integer>();
                orgIds.add(outputRegisterBean.getFld8().getId());
                this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
                        typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
                this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament,
                        outputRegisterBean.getFld8().getId()));
            } else {
                this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
                        typeDestinoRedis, useCaseConf.getEntidadId(), null);
                this.setSelectdestinoRedisDepartamentos(null);
            }
        } catch (SessionException sessionException) {
            LOG.error(
                    ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(
                    null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(
                    ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(
                    null, validationException, null);
        }
    }

    /**
     * M�todo que distribuye de los elementos seleccionados.
     *
     * @throws SessionException errror de sessi�n.
     */
    public void distribuir() throws SessionException {
        init();
        deleteSession("reportsLabelAction");
        ScrRegstate book =
                (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
        if (book == null) {
            throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
        }
        if (distributionBo == null) {
            distributionBo = new DistributionBo();
        }
        try {

            Integer userId = selectdestinoRedisDepartamentos.getId();

            List<Integer> ids = new ArrayList<Integer>();
            ids.add(outputRegisterBean.getFdrid());
            String result =
                    distributionBo.createDistribution(useCaseConf, ids, motivoDistribucion,
                            typeDestinoRedis, userId, book.getId());
            // distribucion
            distributionBo = new DistributionBo();
            listHistDistribution =
                    distributionBo.getHistDistribution(useCaseConf, book.getId(),
                            outputRegisterBean.getFdrid());
            if ("".equals(result.trim())) {
                result = "Se han distribuido los registros correctamente.";
                canDistr = false;
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formLink");
            }
            result = result.replaceAll(";", "<br/>");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            RequestContext.getCurrentInstance().update("messagesform:messages");
        } catch (RPDistributionException e) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
            Utils.redirectToErrorPage(e, null, null);
        } catch (RPGenericException e) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
            Utils.redirectToErrorPage(e, null, null);
        } catch (BookException bookException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, bookException);
            Utils.redirectToErrorPage(null, bookException, null);
        } catch (SessionException sessionException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, sessionException);
            Utils.redirectToErrorPage(null, sessionException, null);
        } catch (ValidationException validationException) {
            LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, validationException);
            Utils.redirectToErrorPage(null, validationException, null);
        }
    }

    /**
     * Gestiona el upload del fichero de la peticion.
     *
     * @param event FileUploadEvent.
     */
    public void handleFileUpload(FileUploadEvent event) {
        this.file = event.getFile();
        this.fileName = FileNameUtils.getName(event.getFile().getFileName());
        boolean exist = false;
        for (Axdoch doc : listDocuments) {
            if (doc.getName().equals(documentoElectronico.getName())) {
                exist = true;
            }
        }
        if (exist) {
            this.file = null;
            this.fileName = null;
        }
    }

    /**
     * Primer paso de firma de nueva peticion.
     */
    public void signRequestFirstStep() {
        byte[] byteArrayFile = null;

        if (file != null) {
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se ha seleccionado ning�n fichero",
                                    "No se ha seleccionado ning�n fichero"));
        }
        // Comprobamos si el archivo cargado es un documento digitalizado
        if (fileBase64 != null && fileBase64.length() > 0) {
            byteArrayFile = Conversor.base64ToBytes(fileBase64);
        } else {
            byteArrayFile = this.file.getContents();
        }
        try {
        	
            MessageDigest gen = MessageDigest.getInstance("SHA-512");
            byte[] hash = gen.digest(byteArrayFile);

            DocumentArchiveResponse documentArchiveResponse =
                    PFEConnector.documentArchive(fileName + ".xsig", hash);
            if (documentArchiveResponse != null && documentArchiveResponse.getDocumentId() != null) {
                csv = documentArchiveResponse.getDocumentId();
            }

            ServerResponse serverResponse = new ServerResponse(this.jsonServerResponse);
            RemoteSignBeginResponse remoteSignBeginResponse = null;
            ResponseValue responseValue = new ResponseValue();
            if (serverResponse.isError()) {
                ServerResponse.addError(serverResponse, ErrorConstants.PF_SIGN_BEGIN_ERROR_CODE,
                        ErrorConstants.INVALID_USER_ID_MSG);
            } else {
            	
            	// Se valida el nombre del fichero para que no contenga caracteres especiales no v�lidos
            	if(!Utilidades.isValidNameFile(fileName)) 
            	{
            		LOG.error("El nombre asignado al fichero a anexar no es v�lido: " + fileName);
                    lanzarError(responseValue, serverResponse, 
               			 "El nombre del fichero no es v�lido.");
            	}
            	
                if (certChain != null && !"".equals(certChain)) {
                    String cerChainClean = this.certChain;
                    

                    /*byte encodedCert[] = Base64.decodeBase64(cerChainClean);
                    java.io.ByteArrayInputStream inputStream  =  new java.io.ByteArrayInputStream(encodedCert);

                    java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate)certFactory.generateCertificate(inputStream);
                    String cn = cert.getSubjectDN().getName();
                    String dni = Utilidades.getDni(cn);
                    
                    IUserUserHdr iuseruserhdr = SIGMServiceManager.getiUserUserHdrService().getByName(useCaseConf.getUserName());
                    UserDAO userDAO = (UserDAO) appContext.getBean("userDAO");
                    String userDni = userDAO.getUserDni(new Integer(String.valueOf(iuseruserhdr.getId())).intValue());
                    */
                    
                    // El certificado con el que se firma el documento debe ser el mismo que
                    // con el que se identific� en la aplicaci�n.
                    PeticionValidacionType petCert = new PeticionValidacionType();
                    byte encodedCert[] = Base64.decodeBase64(cerChainClean);
                    petCert.setCertificadoBytes(encodedCert);
                    petCert.setInformacionCompleta(true);
                    petCert.setIdApp((String) appContext.getBean("IDAPP_SERVCERT"));
                    RespuestaValidacionType respCert = new ServcertWSService().verificaCertificado(petCert);
                    
                    String error = ServcertClientUtils.getErrorMsg(respCert);
                    if (StringUtils.isNotBlank(error)) 
                    {
                      LOG.error("Se ha producido un error en la validaci�n del certificado: " + error);
                      lanzarError(responseValue, serverResponse, 
                 			 "El certificado con el que firma el documento no es v�lido: " + error);
                    }
                    else
                    {
                    	String dni = ServcertClientUtils.getUpperPaddedNIF(respCert);
                    
                    	IUserUserHdr iuseruserhdr = SIGMServiceManager.getiUserUserHdrService().getByName(useCaseConf.getUserName());
                        UserDAO userDAO = (UserDAO) appContext.getBean("userDAO");
                        String userDni = userDAO.getUserDni(new Integer(String.valueOf(iuseruserhdr.getId())).intValue());
                    	
                        while (userDni.length() < 9) 
                        	userDni = '0' + userDni;
                        
	                    if(LOG.isInfoEnabled()) 
	                    {
	                    	LOG.info("Comprobamos el firmante ["+dni+"] con el que accede a la app [" + userDni + "]");
	                    }
	                    if(StringUtils.isBlank(dni) || StringUtils.isBlank(userDni) ||
	                    		!dni.toUpperCase().trim().equals(userDni.trim().toUpperCase()))
	                    {
	                    	 // ERROR DE VALIDACI�N
	                    	 LOG.error("Error firma DNI usuario: " + dni.trim().toUpperCase() + " <=> DNI certificado: " + userDni.trim().toUpperCase());                 	 
	                    	 lanzarError(responseValue, serverResponse, 
	                    			 "El certificado con el que firma el documento debe ser el mismo que con el que accedi� a la aplicaci�n.");
	                    	 
	                    }
                    }
                    
                    cerChainClean = this.certChain.substring(1, this.certChain.length() - 1);
                    cerChainClean = cerChainClean.replaceAll("\"", "");
                    remoteSignBeginResponse =
                            PFEConnector.remoteSignBegin(new String[]{this.csv},
                                    cerChainClean.split(","), "INTERNALLYDETACHED",
                                    "XAdES_BES", "XAdES_AGE");
                    if (remoteSignBeginResponse != null && remoteSignBeginResponse.getErrorCode() == null) {
                        this.taskId = remoteSignBeginResponse.getTaskId();
                        responseValue.setTaskData(new String(Base64Codec.encode(remoteSignBeginResponse.getTaskData())));
                        responseValue.setTaskId(this.taskId);
                        responseValue.setSecuredTaskData(new String(Base64Codec.encode(remoteSignBeginResponse.getSecuredTaskData())));
                    } else {
                        responseValue.setError(true);
                        responseValue.setErrorCode(remoteSignBeginResponse.getErrorCode());

                        throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE, ErrorConstants.PF_SIGN_BEGIN_ERROR_CODE);
                    }
                }
            }
            serverResponse.addResponseValue(responseValue);
            this.jsonServerResponse = serverResponse.toJSON().toString();
        } catch (RPRegisterException rpregisterException) {

        } catch (Exception e) {
            LOG.error("error generico: " + e.getMessage(), e);
        }

    }

    /**
     * Segundo paso de firma de peticion.
     */
    public void signRequestSecondStep() {
        ServerResponse serverResponse = new ServerResponse(this.jsonServerResponse);

        byte[] taskData = null;

        try {

            if (!serverResponse.getStatusInfo().isError()) {
                taskData = Base64Codec.decode(serverResponse.getResponseValues().get(0).getTaskData().getBytes());
                RemoteSignEndResponse sr =
                        PFEConnector.remoteSignEnd(serverResponse.getResponseValues().get(0).getTaskId(), taskData);
                if (sr != null && sr.getErrorCode() == null) {
                    DocumentDownloadResponse documentDownloadResponse = PFEConnector.documentDownload(csv, false);
                    this.signedData = documentDownloadResponse.getFileData();
                    uploadClient();

                } else {
                    LOG.error(ErrorConstants.PF_SIGN_END_ERROR_CODE + ". " + sr.getErrorMessage());
                    throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_END_ERROR_CODE, ErrorConstants.PF_SIGN_END_ERROR_CODE);
                }

            } else {
                LOG.error(ErrorConstants.PF_SIGN_END_ERROR_CODE, serverResponse.getStatusInfo().getErrorCause());
                throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_END_ERROR_CODE, ErrorConstants.PF_SIGN_END_ERROR_CODE);
            }
            this.jsonServerResponse = serverResponse.toJSON().toString();
        } catch (RPRegisterException e) {

        }

    }

    /**
     * Adjunta un documento al registro.
     */
    public void uploadClient() {
        LOG.info("Adjuntando documento ");

        if (registerDocumentsBo == null) {
            registerDocumentsBo = new RegisterDocumentsBo();
        }
        if (file != null) {
            documentoElectronico.setName(file.getFileName());
        } else {
            documentoElectronico.setName(fileName);
        }
        deleteSession("reportsLabelAction");
        try {
            boolean exist = false;
            for (Axdoch doc : listDocuments) {
                if (doc.getName().equals(documentoElectronico.getName().substring(0, documentoElectronico.getName().lastIndexOf(".")))) {
                    exist = true;
                }
            }
            if (!exist) {
                if (documentoElectronico.getName().length() <= 80) {
                    AxSf registerData = Utils.mappingOutputRegisterToAxSF(outputRegisterBean);
                    // identificadorDocumentoElectronicoAnexoVO
                    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO =
                            new IdentificadorDocumentoElectronicoAnexoVO();
                    identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String
                            .valueOf(book.getIdocarchhdr().getId())));
                    identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String
                            .valueOf(outputRegisterBean.getFdrid())));
                    documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);

                    // CODE NAME
                    String codeName = ("codeName" + documentoElectronico.getName());
                    codeName = StringUtils.abbreviate(codeName, 21);
                    documentoElectronico.setCodeName(codeName);

                    // extension
                    String extension =
                            documentoElectronico.getName().substring(
                                    documentoElectronico.getName().lastIndexOf(".") + 1,
                                    documentoElectronico.getName().length());
                    documentoElectronico.setExtension(extension);

                    // contenido
                    DocumentoElectronicoAnexoContenidoVO contenido =
                            new DocumentoElectronicoAnexoContenidoVO();
                    // Contenido del fichero


                    // ANTIVIRUS
		   /* try {
			antivirusConnector =
				(AntivirusConnector) RegistroPresencialMSSSIWebSpringApplicationContext
					.getInstance().getApplicationContext()
					.getBean("antivirusConnector");
			if (file != null){
			    antivirusConnector.scan(documentoElectronico.getName(), file.getContents());
			}else {
			    antivirusConnector.scan(documentoElectronico.getName(), Conversor.base64ToBytes(fileBase64));
			}
		    }
		    catch (RPGenericException e) {
			LOG.error(ErrorConstants.SCAN_FILE_ANTIVIRUS_CALL_ERROR_MESSAGE
				+ ". C�digo: " + e.getCode().getCode() + " . Mensaje: "
				+ e.getMessage());
			Utils.redirectToErrorPage(e, null, null);
		    }*/
                    if (file != null) {
                        contenido.setContent(file.getContents());
                    } else {
                        contenido.setContent(Conversor.base64ToBytes(fileBase64));
                    }
                    documentoElectronico.setContenido(contenido);


                    try {
                        if (ContentTypeEnum.valueOf(extension.toUpperCase()) != null
                                && ContentTypeEnum.valueOf(extension.toUpperCase())
                                .getContentType().length() <= 20) {
                            documentoElectronico.setMimeType(ContentTypeEnum.valueOf(
                                    extension.toUpperCase()).getContentType());
                        }
                    } catch (Exception e) {
                    }
                    // TIPO DOCUMENTO
                    documentoElectronico
                            .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO);

                    if (validateDocument != null) {
                        documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO
                                .getEnum(Integer.valueOf(validateDocument.substring(1, 2))));
                    }

                    // datos hash
                    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento =
                            new DocumentoElectronicoAnexoDatosFirmaVO();

                    String hashDocumento = null;
                    if (file != null) {
                        hashDocumento = UtilsHash.getBase64Sring(Hasher.generateHash(file.getContents(), HashType.SHA1));
                    } else {
                        hashDocumento = UtilsHash.getBase64Sring(Hasher.generateHash(Conversor.base64ToBytes(fileBase64), HashType.SHA1));
                    }
                    datosFirmaDocumento.setHash(hashDocumento);
                    datosFirmaDocumento.setHashAlg(HashType.SHA1.getValue());
                    documentoElectronico.setDatosFirma(datosFirmaDocumento);
                    // FIRMAS
                    List<DocumentoElectronicoAnexoVO> firmas =
                            new ArrayList<DocumentoElectronicoAnexoVO>();
                    DocumentoElectronicoAnexoVO firmaDocumento = new DocumentoElectronicoAnexoVO();
                    if (this.signedData != null && !"".equals(this.signedData)) {
                        IdentificadorDocumentoElectronicoAnexoVO idFima =
                                new IdentificadorDocumentoElectronicoAnexoVO();

                        idFima.setIdLibro(identificadorDocumentoElectronicoAnexoVO.getIdLibro());
                        idFima.setIdRegistro(identificadorDocumentoElectronicoAnexoVO
                                .getIdRegistro());
                        firmaDocumento.setId(idFima);
                        String codeNameFirma = "codeNameSign" + documentoElectronico.getName();
                        codeNameFirma = StringUtils.abbreviate(codeNameFirma, 21);
                        firmaDocumento.setCodeName(codeNameFirma);

                        // contenido
                        DocumentoElectronicoAnexoContenidoVO contenidoFirma =
                                new DocumentoElectronicoAnexoContenidoVO();

                        contenidoFirma.setContent(this.signedData);
                        firmaDocumento.setContenido(contenidoFirma);
                        // extension firma
                        String extensionFirma = "xsig";
                        firmaDocumento.setExtension(extensionFirma);

                        // datos hash
                        DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma =
                                new DocumentoElectronicoAnexoDatosFirmaVO();
                        String hashDocumentoFirma =
                                UtilsHash.getBase64Sring(Hasher.generateHash(this.signedData,
                                        HashType.SHA1));
                        datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
                        datosFirmaDocumentoFirma.setHashAlg(HashType.SHA1.getValue());
                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        // NOMBRE FICHERO FIRMA

                        String nameFirma = "FIRMA.xsig";
                        firmaDocumento.setName(nameFirma);
                        firmaDocumento
                                .setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
                        firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.ORIGINAL);

                        if (certificateDocument != null) {
                            datosFirmaDocumentoFirma.setCertificado(certificateDocument);
                        }

                        firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                        firmas.add(firmaDocumento);
                        documentoElectronico.setFirmas(firmas);

                    }

                    UsuarioVO usuario =
                            (UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
                                    .getSessionMap().get("USERVO");
                    DocumentoElectronicoAnexoVO documentSaved =
                            registerDocumentsBo.saveDocuments(useCaseConf.getSessionID(),
                                    book.getId(), outputRegisterBean.getFdrid(),
                                    documentoElectronico, registerData, useCaseConf.getLocale(),
                                    useCaseConf.getEntidadId(), usuario);
                    if (documentSaved != null) {
                        getRegisterAttachedDocuments();
                        checkSize();
                        FacesContext.getCurrentInstance()
                                .addMessage(
                                        null,
                                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente",
                                                "Se ha adjuntado el documento "
                                                        + documentoElectronico.getName()
                                                        + " correctamente"));
                    }
                } else {
                    FacesContext
                            .getCurrentInstance()
                            .addMessage(
                                    null,
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_ERROR,
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName(),
                                            "No se ha podido adjuntar el documento. El nombre del fichero no puede tener mas de 80 caracteres: "
                                                    + documentoElectronico.getName()));
                }
            } else {
                FacesContext
                        .getCurrentInstance()
                        .addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe",
                                        "No se ha podido adjuntar el documento. El nombre del fichero ya existe"));
            }
        } catch (RPRegisterException e) {
            LOG.error(ErrorConstants.SAVE_ASOC_REGISTER_ERROR_MESSAGE + ". C�digo: "
                    + e.getCode().getCode() + " . Mensaje: " + e.getMessage());
            Utils.redirectToErrorPage(e, null, null);
        } finally {
            documentoElectronico = new DocumentoElectronicoAnexoVO();
            validateDocument = null;
            contentDocument = null;
            signDocument = null;
            certificateDocument = null;
            signedData = null;
            file = null;
            fileName = null;
            readCheck = false;
            fileBase64 = null;
            fileNameDig = null;
        }
    }

    private void checkSize()
    {
        Integer totalSize=0;
        for (Axdoch doch : listDocuments) {
            totalSize=+doch.getPages().get(0).getFileSize();
        }
        if(totalSize>=15100000)
        {
            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El tama�o de la suma de todos los documentos ha sobrepasado los 15MB","El tama�o de la suma de todos los documentos ha sobrepasado los 15MB"));
        }
    }


    public void enableSign() {
        if (file == null && fileBase64 == null) {
            this.readCheck = false;
            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se ha podido adjuntar el documento. No se ha seleccionado ning�n fichero",
                                    "No se ha podido adjuntar el documento. No se ha seleccionado ning�n fichero"));
        }
    }

    /**
     * M�todo que lanza un error en caso de no utilizar el mismo certificado con el que se accede a la aplicaci�n para firmar un documento
     * o bien este certificado no es v�lido. Tambi�n se puede utilizar este m�todo para cualquier tipo de error que se produzca al anexar un documento. 
     * @param responseValue - ResponseValue responseValue
     * @param serverResponse - ServerResponse serverResponse
     * @param msg - Mensaje del error
     * @throws RPRegisterException - Excepci�n lanzada
     */
    protected void lanzarError(ResponseValue responseValue, ServerResponse serverResponse, String msg) throws RPRegisterException {
     	
     	RequestContext.getCurrentInstance().addCallbackParam("ERROR", true);
    	 	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de Validaci�n",
    			 	styleErrorIni + msg + styleErrorEnd);
         RequestContext.getCurrentInstance().showMessageInDialog(message);
    	 
         responseValue.setError(true);
         responseValue.setErrorCode(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE.getCode());
         
         serverResponse.addResponseValue(responseValue);
         StatusInfo statusInfo = new StatusInfo();
         statusInfo.setError(true);
         serverResponse.setStatusInfo(statusInfo);
         this.jsonServerResponse = serverResponse.toJSON().toString();
         
    	 	throw new RPRegisterException(RPRegisterErrorCode.PF_SIGN_BEGIN_ERROR_CODE, ErrorConstants.INVALID_USER_SING_DOCUMENTO);
     } 
    
    public void uploadFileBase64() {
        if (fileNameDig != null) {
            fileName = fileNameDig;
        }
    }

    /**
     * Obtiene el valor del par�metro selectedDestOrg.
     *
     * @return selectedDestOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedDestOrg() {
        return selectedDestOrg;
    }

    /**
     * Guarda el valor del par�metro selectedDestOrg.
     *
     * @param selectedDestOrg valor del campo a guardar.
     */
    public void setSelectedDestOrg(ScrOrg selectedDestOrg) {
        this.selectedDestOrg = selectedDestOrg;
    }

    /**
     * Obtiene el valor del par�metro selectedOriginOrg.
     *
     * @return selectedOriginOrg valor del campo a obtener.
     */
    public ScrOrg getSelectedOriginOrg() {
        return selectedOriginOrg;
    }

    /**
     * Guarda el valor del par�metro selectedOriginOrg.
     *
     * @param selectedOriginOrg valor del campo a guardar.
     */
    public void setSelectedOriginOrg(ScrOrg selectedOriginOrg) {
        this.selectedOriginOrg = selectedOriginOrg;
    }

    /**
     * @return the saveInterested
     */
    public boolean isSaveInterested() {
        return saveInterested;
    }

    /**
     * @param saveInterested the saveInterested to set
     */
    public void setSaveInterested(boolean saveInterested) {
        this.saveInterested = saveInterested;
    }


    public String getCertChain() {
        return certChain;
    }

    /**
     * Establece el valor de certChain.
     *
     * @param certChain Valor de certChain.
     */
    public void setCertChain(String certChain) {
        this.certChain = certChain;
    }

    /**
     * Devuelve el valor de jsonServerResponse.
     *
     * @return valor de jsonServerResponse.
     */
    public String getJsonServerResponse() {
        return jsonServerResponse;
    }

    /**
     * Establece el valor de jsonServerResponse.
     *
     * @param jsonServerResponse valor de jsonServerResponse.
     */
    public void setJsonServerResponse(String jsonServerResponse) {
        this.jsonServerResponse = jsonServerResponse;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the fileBase64
     */
    public String getFileBase64() {
        return fileBase64;
    }

    /**
     * @param fileBase64 the fileBase64 to set
     */
    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }

    /**
     * Resetea valores al cancelar.
     */
    public void onCancel() {
        this.readCheck = false;
    }

    /**
     * Retorna el valor del check de documentos leidos.
     *
     * @return Valor del check de documentos leidos.
     */
    public boolean isReadCheck() {
        return readCheck;
    }

    /**
     * Establece el valor del check de documentos leidos.
     *
     * @param readCheck Valor del check de documentos leidos.
     */
    public void setReadCheck(boolean readCheck) {
        this.readCheck = readCheck;
    }

    /**
     * @return the fileNameDig
     */
    public String getFileNameDig() {
        return fileNameDig;
    }

    /**
     * @param fileNameDig the fileNameDig to set
     */
    public void setFileNameDig(String fileNameDig) {
        this.fileNameDig = fileNameDig;
    }


    /**
     * Obtiene el valor del par�metro canDistr.
     *
     * @return canDistr valor del campo a obtener.
     */
    public boolean getCanDistr() {
        return canDistr;
    }

    /**
     * Guarda el valor del par�metro canDistr.
     *
     * @param canDistr valor del campo a guardar.
     */
    public void setCanDistr(boolean canDistr) {
        this.canDistr = canDistr;
    }

    /**
     * Obtiene el valor del par�metro selectedDocSIR.
     *
     * @return selectedDocSIR valor del campo a obtener.
     */
    public String getSelectedDocSIR() {
        return selectedDocSIR;
    }

    /**
     * Guarda el valor del par�metro selectedDocSIR.
     *
     * @param selectedDocSIR valor del campo a guardar.
     */
    public void setSelectedDocSIR(String selectedDocSIR) {
        this.selectedDocSIR = selectedDocSIR;
    }


    public String getContentDocument() {
        return contentDocument;
    }

    /**
     * Guarda el valor del par�metro contentDocument.
     *
     * @param contentDocument valor del campo a guardar.
     */
    public void setContentDocument(String contentDocument) {
        this.contentDocument = contentDocument;
    }

    /**
     * Obtiene el valor del par�metro documentoElectronico.
     *
     * @return documentoElectronico valor del campo a obtener.
     */
    public DocumentoElectronicoAnexoVO getDocumentoElectronico() {
        return documentoElectronico;
    }

    /**
     * Guarda el valor del par�metro documentoElectronico.
     *
     * @param documentoElectronico valor del campo a guardar.
     */
    public void setDocumentoElectronico(DocumentoElectronicoAnexoVO documentoElectronico) {
        this.documentoElectronico = documentoElectronico;
    }

    /**
     * Obtiene el valor del par�metro validateDocument.
     *
     * @return validateDocument valor del campo a obtener.
     */
    public String getValidateDocument() {
        return validateDocument;
    }

    /**
     * Guarda el valor del par�metro validateDocument.
     *
     * @param validateDocument valor del campo a guardar.
     */
    public void setValidateDocument(String validateDocument) {
        this.validateDocument = validateDocument;
    }

    /**
     * Obtiene el valor del par�metro signDocument.
     *
     * @return signDocument valor del campo a obtener.
     */
    public String getSignDocument() {
        return signDocument;
    }

    /**
     * Guarda el valor del par�metro signDocument.
     *
     * @param signDocument valor del campo a guardar.
     */
    public void setSignDocument(String signDocument) {
        this.signDocument = signDocument;
    }

    /**
     * Obtiene el valor del par�metro certificateDocument.
     *
     * @return certificateDocument valor del campo a obtener.
     */
    public String getCertificateDocument() {
        return certificateDocument;
    }

    /**
     * Guarda el valor del par�metro certificateDocument.
     *
     * @param certificateDocument valor del campo a guardar.
     */
    public void setCertificateDocument(String certificateDocument) {
        this.certificateDocument = certificateDocument;
    }

    /**
     * Obtiene el valor del par�metro selectDocument.
     *
     * @return selectDocument valor del campo a obtener.
     */
    public Axdoch getSelectDocument() {
        return selectDocument;
    }

    /**
     * Guarda el valor del par�metro selectDocument.
     *
     * @param selectDocument valor del campo a guardar.
     */
    public void setSelectDocument(Axdoch selectDocument) {
        this.selectDocument = selectDocument;
    }

    /**
     * Obtiene el valor del par�metro listNameDocument.
     *
     * @return listNameDocument valor del campo a obtener.
     */
    public String getListNameDocument() {
        return listNameDocument;
    }

    /**
     * Guarda el valor del par�metro listNameDocument.
     *
     * @param listNameDocument valor del campo a guardar.
     */
    public void setListNameDocument(String listNameDocument) {
        this.listNameDocument = listNameDocument;
    }

    public void deleteLabel() {
        init();
        deleteSession("reportsLabelAction");
    }

    /**
     * Obtiene el valor del par�metro motivoDistribucion.
     *
     * @return motivoDistribucion valor del campo a obtener.
     */
    public String getMotivoDistribucion() {
        return motivoDistribucion;
    }

    /**
     * Guarda el valor del par�metro motivoDistribucion.
     *
     * @param motivoDistribucion valor del campo a guardar.
     */
    public void setMotivoDistribucion(String motivoDistribucion) {
        this.motivoDistribucion = motivoDistribucion;
    }

    /**
     * Obtiene el valor del par�metro selectdestinoRedisDepartamentos.
     *
     * @return selectdestinoRedisDepartamentos valor del campo a obtener.
     */
    public Iuserdepthdr getSelectdestinoRedisDepartamentos() {
        return selectdestinoRedisDepartamentos;
    }

    /**
     * Guarda el valor del par�metro selectdestinoRedisDepartamentos.
     *
     * @param selectdestinoRedisDepartamentos valor del campo a guardar.
     */
    public void setSelectdestinoRedisDepartamentos(Iuserdepthdr selectdestinoRedisDepartamentos) {
        this.selectdestinoRedisDepartamentos = selectdestinoRedisDepartamentos;
    }

    /**
     * Obtiene el valor del par�metro fileUpload.
     *
     * @return fileUpload valor del campo a obtener.
     */
    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    /**
     * Guarda el valor del par�metro fileUpload.
     *
     * @param fileUpload valor del campo a guardar.
     */
    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * Obtiene el valor del par�metro listDepartament.
     *
     * @return listDepartament valor del campo a obtener.
     */
    public List<Iuserdepthdr> getListDepartament() {
        return listDepartament;
    }

    /**
     * Guarda el valor del par�metro listDepartament.
     *
     * @param listDepartament valor del campo a guardar.
     */
    public void setListDepartament(List<Iuserdepthdr> listDepartament) {
        this.listDepartament = listDepartament;
    }

    /**
     * Obtiene el valor del par�metro isOfficial.
     *
     * @return isOfficial valor del campo a obtener.
     */
    public boolean getIsOfficial() {
        return isOfficial;
    }

    /**
     * Guarda el valor del par�metro isOfficial.
     *
     * @param isOfficial valor del campo a guardar.
     */
    public void setIdOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    /**
     * Metodo que se lanza al seleccionar un organismo en el destino.
     *
     * @param event evento select.
     */
    public void onItemSelectDestinationOrg(SelectEvent event) {
        selectedDestOrg = (ScrOrg) event.getObject();
    }

    /**
     * Metodo que se lanza al cambiar un organismo en el destino.
     *
     * @param event evento change.
     */
    public void onChangeDestinationOrg(AjaxBehaviorEvent event) {
        if (((AutoComplete) event.getSource()).getItemValue() == null) {
            selectedDestOrg = null;
        } else {
            selectedDestOrg = (ScrOrg) ((AutoComplete) event.getSource()).getItemValue();
        }
    }

    /**
     * Metodo que se lanza al seleccionar un organismo en el origen.
     *
     * @param event evento select.
     */
    public void onItemSelectOriginOrg(SelectEvent event) {
        selectedOriginOrg = (ScrOrg) event.getObject();
    }

    /**
     * Metodo que se lanza al cambiar un organismo en el origen.
     *
     * @param event evento change.
     */
    public void onChangeOriginOrg(AjaxBehaviorEvent event) {
        if (((AutoComplete) event.getSource()).getItemValue() == null) {
            selectedOriginOrg = null;
        } else {
            selectedOriginOrg = (ScrOrg) ((AutoComplete) event.getSource()).getItemValue();
        }
    }

    public boolean isEnableCompulsaValue() {
        return enableCompulsaValue;
    }

    public void setEnableCompulsaValue(boolean enableCompulsaValue) {
        this.enableCompulsaValue = enableCompulsaValue;
    }

}