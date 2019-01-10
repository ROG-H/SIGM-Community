package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitFilter;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.ProcessSignProperties;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.SignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesError;
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDatosFirma;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

/**
 * Action para la firma de documentos en bloque.
 * 
 * @author Iecisa
 * @version $Revision$
 * [eCenpri-Agustin #871] FIRMA 3 FASES
 */
@SuppressWarnings("restriction")
public class SignDocumentsAction extends BaseDispatchAction {
	
	protected static final String SIGN_DOCUMENT_IDS_PARAM = "SIGN_DOCUMENT_IDS"; 
	protected static final String SIGN_DOCUMENT_HASHCODES_PARAM = "SIGN_DOCUMENT_HASHCODES";
	
	protected static final String ERROR_TITULO = "firmar.titulo";
	protected static final String ERROR_TEXTO = "firmar.texto";
	protected static final String ERROR_MAPPING = "nofirmar";
	/**
	 * Muestra las opciones disponibles para firmar los documentos.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward defaultExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		// Identificadores de los documentos seleccionados
		String[] documentIds = ((SignForm) form).getMultibox();

		if (ArrayUtils.isEmpty(documentIds)) {
			LOGGER.warn("No se ha seleccionado ning�n documento");
			throw new ISPACInfo(getResources(request).getMessage(
					"forms.listdoc.firmarDocumentos.empty"), false);
		}

		// Almacenar los identificadores de los documentos para el tratamiento posterior
		storeDocumentIds(session, documentIds);
		
		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");
		
		return mapping.findForward("selectOption");
	}


	/**
	 * Muestra la p�gina para firmar los documentos seleccionados.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		//C�digo de entidad
		ClientContext cct = session.getClientContext();
		String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();

		//INICIO [eCenpri-Felipe #818]
		FirmaLotesError error = FirmaLotesUtil.getErrorDeshabilitar(codEntidad);
		if (null != error){
			request.setAttribute(ERROR_TITULO, error.getTitulo());
			request.setAttribute(ERROR_TEXTO, error.getTexto());
			return mapping.findForward(ERROR_MAPPING);
		}
		//FIN [eCenpri-Felipe #818]
		
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Informaci�n del estado
		getCurrentState(request, session);
		
		// Obtener la informaci�n de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.append(" ORDER BY ID")//[dipucr-Felipe #184]
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		
		//Informacion del firmante
		InfoFirmante infoFirmante = FirmaLotesUtil.getInfoFirmante(cct, codEntidad);
		
		//INICIO [dipucr-Felipe 3#231]
		error = FirmaLotesUtil.getErrorFirmanteNoConfigurado(codEntidad, infoFirmante);
	    if (null != error) {
			request.setAttribute(ERROR_TITULO, error.getTitulo());
			request.setAttribute(ERROR_TEXTO, error.getTexto());
			return mapping.findForward(ERROR_MAPPING);
		}
	    //FIN [dipucr-Felipe 3#231]
		
		//Lista de documentos con su id temporal
		List<String> hashCodes = new ArrayList<String>();
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		int count = 0;
		
		ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();
		Signbatch signbatch = FirmaLotesUtil.getSignBatch(cct, peticion);
		
		LOGGER.info("Se van a realizar la Prefirma");
		while (iterator.hasNext()) {
			
			///////////// SIGUIENTE DOCUMENTO DE LA LISTA DEL PANEL DE TRAMITE
			//Informaci�n del documento
			IItem document = iterator.next();
			
			//PRE-FIRMA
			// Obtengo documento y agrego entidad junto con la informacion del firmante
			SignDocument signDocument = new SignDocument(document);
			signDocument.setEntityId(codEntidad);
			signDocument.addFirmante(infoFirmante);
//			String idDoc = String.valueOf(signDocument.getItemDoc().getKeyInt());
			
			//Genero documento temporal
			String docRef = signAPI.presign(signDocument, true);
			LOGGER.info("Prefirma realizada, el iddocumento temporal es: "+docRef);			
			
			//Agrego a la lista de documentos la referencia del documento temporal
			//Existe otra lista con las referencias los documentos originales
			hashCodes.add(docRef);
			
			///////////// AJM CONSTRUIR NODOS XML SINGLESIGN
			//[dipucr-Felipe #791] Recuperar el singleSign del documento con sus EXTRAPARAMS
			String extraparams = FirmaLotesUtil.getFirmaExtraParams(codEntidad, cct, infoFirmante, 
					signDocument.getDocumentType(), docRef, 1, 1, new Date());
			Signbatch.Singlesign singlesign = FirmaLotesUtil.getSingleSign(cct, peticion, codEntidad, count, extraparams);
			signbatch.getSinglesign().add(singlesign);			
			
			count++;
		}
		
		///////////// AJM CONSTRUIR NODO XML ROOT SIGNBATCH
		//File file = new File("lotes_firma3.xml");		
		JAXBContext jaxbContext;
		try {
			
			jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
		
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				
			//Obtener XML en un String
			java.io.StringWriter sw = new StringWriter();
			//Indico esto por que no se cual es la funcion del atributo standalone asi que lo pongo a false 
			//para que no aparezca, asi esta en la demo de integracion del servidor de firma
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.marshal(signbatch, sw);				
			result = result.concat(sw.toString());
			
		} catch (JAXBException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
		}

		// Almacenar los hash de los documentos para el tratamiento posterior
		storeDocumentHashCodes(session, hashCodes);
		
		request.setAttribute("hashCodes",hashCodes.toArray(new String[hashCodes.size()]));
		//Seteamos los parametros en la firma tres fases
		SignForm signForm = ((SignForm) form);
		signForm.setSerialNumber(infoFirmante.getNumeroSerie());
		signForm.setListaDocs(result);
		signForm.setCodEntidad(codEntidad);
		
		return mapping.findForward("sign");
	}


	/**
	 * Firma almacena la firma de los documentos seleccionados y muestra una
	 * pantalla de resumen.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward sign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		

		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Informaci�n del estado
		IState state = getCurrentState(request, session);

		SignForm signForm = (SignForm) form;
		//String[] signs = signForm.getSigns().split("!");
		String[] hashCodes = retrieveDocumentHashCodes(session);
		
		//TODO en principio no recojo las firmas, seguramente en el futuro no haga falta
		//Dejo este if por si luego quisiera entrarlas como metadatos
		//if (ArrayUtils.isEmpty(signs)
		//		|| (StringUtils.isBlank(signForm.getSignCertificate()) || "undefined"
		//				.equalsIgnoreCase(signForm.getSignCertificate()))) {
		//	return mapping.findForward("failure");
		//}

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la informaci�n de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.append(" ORDER BY ID")//[dipucr-Felipe #184]
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		int count = 0;
		//[DipuCR-Agustin] #150 Agregar metadatos
		String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
		
		while (iterator.hasNext()) {
			
			// Informaci�n del documento
			IItem document = iterator.next();

			try {

				// Comprobar que el documento no est� firmado o en un circuito de firma
				String estadoFirma = document.getString("ESTADOFIRMA");
				if (!StringUtils.isBlank(estadoFirma)
						&& !SignStatesConstants.SIN_FIRMA.equals(estadoFirma)) {
					throw new ISPACInfo(
							"exception.signProcess.cannotSignDocument", true);
				}

				SignDocument signDocument = new SignDocument(document);
				signDocument.setIdPcd(state.getPcdId());
				signDocument.setNumExp(state.getNumexp());
				//signDocument.addSign(signs[count]);
		        signDocument.addCertificate(signForm.getSignCertificate());
		        signDocument.setHash(hashCodes[count]);

	        	//signAPI.sign(signDocument, true);
		        signAPI.postsign(signDocument, hashCodes[count] , true);

				documents_ok.add(new ItemBean(document));				
				
				//[DipuCR-Agustin] #150 Agregar metadatos
				GestorMetadatos.storeMetada(session, signDocument, autorCert, null);
				
				//[dipucr-Felipe #817]
				GestorDatosFirma.storeDatosFirma(session.getClientContext(), signDocument, null);

			} catch (ISPACException e) {
				LOGGER.error(
						"Error al firmar el documento:"
								+ document.getKeyInt() + " - "
								+ document.getString("NOMBRE") + " ["
								+ document.getString("DESCRIPCION") + "]", e);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("documento", new ItemBean(document));
				map.put("error", e.getExtendedMessage(request.getLocale()));

				documents_ko.add(map);
			}
			
			count++;
		}
		
		signForm.clean();

		request.setAttribute("DOCUMENTS_OK", documents_ok);
		request.setAttribute("DOCUMENTS_KO", documents_ko);
			
		return mapping.findForward("success");
	}

	/**
	 * Muestra el listado de circuitos de firma.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {

		// Informaci�n del estado
		IState state = getCurrentState(request, session);

		// Filtro obtener los circuitos de firma gen�ricos definidos en el
		// sistema y los espec�ficos del procedimiento del expediente
		SignCircuitFilter filter = new SignCircuitFilter();
		filter.setPcdId(state.getPcdId());
		filter.setTaskPcdId(state.getTaskPcdId()); //[eCenpri-Felipe #592]

		
		filter.setIdSistema(ProcessSignConnectorFactory.getInstance()
				.getProcessSignConnector().getIdSystem());
		filter.setDefaultPortafirmas(ProcessSignConnectorFactory.getInstance()
				.isDefaultConnector());

		// Obtenemos los circuitos de firma
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IItemCollection itemcol = signAPI.getCircuits(filter);
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_LIST,
				CollectionBean.getBeanList(itemcol));

		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");
		
		//INICIO [eCenpri-Felipe #592] Obtenemos los circuitos del tr�mite
		IItemCollection itemcolTram = signAPI.getCircuitsTramite(filter);
		@SuppressWarnings("rawtypes")
		List listTram = CollectionBean.getBeanList(itemcolTram);
		request.setAttribute(ActionsConstants.SIGN_CIRCUITTRAM_LIST, listTram);
		//FIN [eCenpri-Felipe #592]

		return mapping.findForward("selectCircuit");
	}	
	
	/**
	 * Muestra el formulario para introducir la informaci�n necesaria del
	 * circuito de firmas.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward setPropertiesSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {
		
		// Informaci�n del estado
		getCurrentState(request, session);

		// Reiniciar el formulario
		SignForm formulario = (SignForm) form;
		formulario.resetProperties();
		
		// Obtenemos identificador del circuito de firma a iniciar
		int circuitId = Integer.parseInt(request
				.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_ID, circuitId);
		
		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");

		return mapping.findForward("showProperties");
	}

	/**
	 * Inicia los circuitos de firma de los documentos.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {
		
		// Obtenemos identificador del circuito de firma a iniciar
		int circuitId = Integer.parseInt(request
				.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));

		// Propiedades del circuito de firmas
		SignForm formulario = (SignForm) form;
		ProcessSignProperties properties = new ProcessSignProperties(
				formulario.getSubject(), formulario.getFstart(),
				formulario.getFexpiration(), formulario.getContent(),
				formulario.getLevelOfImportance());
		
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Guardar el estado en el ClientContext
		getCurrentState(request, session);

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la informaci�n de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		while (iterator.hasNext()) {
			
			// Informaci�n del documento
			IItem document = iterator.next();

			try {

				// Comprobar que el documento no est� firmado o en un circuito de firma
				String estadoFirma = document.getString("ESTADOFIRMA");
				if (!StringUtils.isBlank(estadoFirma)
						&& !SignStatesConstants.SIN_FIRMA.equals(estadoFirma)) {
					throw new ISPACInfo(
							"exception.signProcess.cannotSignDocument", true);
				}
				
				// Iniciar el circuito de firmas
				signAPI.initCircuitPortafirmas(circuitId, document.getKeyInt(), properties);

				documents_ok.add(new ItemBean(document));

			} catch (ISPACException e) {
				LOGGER.error(
						"Error al iniciar el circuito de firmas para el documento:"
								+ document.getKeyInt() + " - "
								+ document.getString("NOMBRE") + " ["
								+ document.getString("DESCRIPCION") + "]", e);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("documento", new ItemBean(document));
				map.put("error", e.getExtendedMessage(request.getLocale()));

				documents_ko.add(map);
			}
		}

		request.setAttribute("CIRCUIT_DOCUMENTS_OK", documents_ok);
		request.setAttribute("CIRCUIT_DOCUMENTS_KO", documents_ko);

		return mapping.findForward("success");
	}

	private IState getCurrentState(HttpServletRequest request, SessionAPI session) throws ISPACException {
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		return managerAPI.currentState(getStateticket(request));
	}
	
	private void storeDocumentIds(SessionAPI session, String[] documentIds) throws ISPACException {
		
		// Almacenar los identificadores de los documentos para el tratamiento posterior
		ClientContext ctx = session.getClientContext();
		ctx.setSsVariable(SIGN_DOCUMENT_IDS_PARAM,
				StringUtils.join(documentIds, ","));
	}
	
	private String[] retrieveDocumentIds(SessionAPI session) throws ISPACException {

		// Recuperar los identificadores de los documentos seleccionados
		ClientContext ctx = session.getClientContext();
		String documentIdsString = ctx.getSsVariable(SIGN_DOCUMENT_IDS_PARAM);
		
		return StringUtils.split(documentIdsString, ",");
	}

	private void storeDocumentHashCodes(SessionAPI session, List<String> hashCodes) throws ISPACException {
		
		// Almacenar los hash de los documentos para el tratamiento posterior
		ClientContext ctx = session.getClientContext();
		ctx.setSsVariable(SIGN_DOCUMENT_HASHCODES_PARAM,
				StringUtils.join(hashCodes, ","));
	}
	
	private String[] retrieveDocumentHashCodes(SessionAPI session) throws ISPACException {

		// Recuperar los identificadores de los documentos seleccionados
		ClientContext ctx = session.getClientContext();
		String hashCodesString = ctx.getSsVariable(SIGN_DOCUMENT_HASHCODES_PARAM);
		
		return StringUtils.split(hashCodesString, ",");
	}

}