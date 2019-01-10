package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRegisterAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sicres.ISicresConnector;
import ieci.tdw.ispac.ispaclib.sicres.RegisterHelper;
import ieci.tdw.ispac.ispaclib.sicres.vo.DocumentInfo;
import ieci.tdw.ispac.ispaclib.sicres.vo.Organization;
import ieci.tdw.ispac.ispaclib.sicres.vo.Register;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterData;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterInfo;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterOffice;
import ieci.tdw.ispac.ispaclib.sicres.vo.RegisterType;
import ieci.tdw.ispac.ispaclib.sicres.vo.ThirdPerson;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.sellar.action.SellarDocumentos;
import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSV;

/**
 * Action para crear registros de salida en SICRES.
 *
 */
public class InsertOutputRegistryAction extends RegistryBaseAction {
	
    protected final static String ERROR_VAR = "ERROR";
    protected final static String ERROR_REGISTRO = "ERROR_REGISTRO";
    

	public ActionForward defaultExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
  	    IInvesflowAPI invesFlowAPI = session.getAPI();
		IRegisterAPI registerAPI = invesFlowAPI.getRegisterAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

		// Comprobar si el conector con SICRES est� configurado
		if (!registerAPI.existConnector()) {
			throw new ISPACInfo("exception.sicres.notConfigured",false);
		}
		
		// Comprobar si el usuario tiene permisos para registrar de salida
		if (!registerAPI.canRegister(RegisterType.SALIDA)) {
			throw new ISPACInfo("exception.sicres.outputRegisterNotAllowed",false);
		}
		
		// Nombre del campo que contiene los campos de entrada
        String field = request.getParameter("field");
        String[] fields = field.split(";");
        	        
        // fields[0] -> Id de documento
        String documentId = request.getParameter(fields[0]);
        
  		// Obtener el documento
  		IItem document = entitiesAPI.getEntity(SpacEntities.SPAC_DT_DOCUMENTOS,	Integer.parseInt(documentId));
        if (!StringUtils.isEmpty(document.getString("NREG"))) {
            throw new ISPACInfo("exception.documents.register.output",false);
        }
        
        // fields[1] -> Destino
        String pDestiny = request.getParameter(fields[1]);
        String destiny = document.getString("DESTINO");
        
        if ((StringUtils.isEmpty(pDestiny)) && (StringUtils.isEmpty(destiny))) {
            throw new ISPACInfo("exception.documents.register.output.selectDestiny",false);
        } else if (!pDestiny.equals(destiny)) {
        	throw new ISPACInfo("exception.documents.register.output.saveDocument",false);
        }
        
        request.setAttribute("action", "insertOutputRegistry.do");
        
		return mapping.findForward("form");
	}

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		ClientContext cct = session.getClientContext();
		
		///////////////////////////////////////////////
		// Se obtiene el estado de tramitaci�n
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
   	    IState currentState = managerAPI.currentState(getStateticket(request));
   	    
  	    IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IRegisterAPI registerAPI = invesFlowAPI.getRegisterAPI();
		
		// Comprobar si el conector con SICRES est� configurado
		if (!registerAPI.existConnector()) {
			throw new ISPACInfo("exception.sicres.notConfigured");
		}
		
		// Comprobar si el usuario tiene permisos para registrar de salida
		if (!registerAPI.canRegister(RegisterType.SALIDA)) {
			throw new ISPACInfo("exception.sicres.outputRegisterNotAllowed");
		}
		
		EntityForm defaultForm = (EntityForm) form;
		
		// Validar la informaci�n del formulario
		ActionMessages errors = defaultForm.validate(mapping, request);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward("form");
		}
		
    	String documentId = defaultForm.getProperty("SPAC_DT_DOCUMENTOS:ID");

  		// Obtener el documento
  		IItem document = entitiesAPI.getEntity(SpacEntities.SPAC_DT_DOCUMENTOS,	Integer.parseInt(documentId));
  		
		//[Manu Ticket #913] SIGEM firma registro rechazado
  		if(document != null && StringUtils.isNotEmpty(document.getString("ESTADOFIRMA")) && !document.getString("ESTADOFIRMA").equals("04")){
  		
	  		List<String> docIds = new ArrayList<String>();
	  		docIds.add(String.valueOf(document.getKeyInt()));
	  		DocumentInfo[] documentInfo = getDocuments(session, docIds);
	  		
	  		
			String destinyId = defaultForm.getProperty("SPAC_DT_DOCUMENTOS:DESTINO_ID");
			String destinyName = defaultForm.getProperty("SPAC_DT_DOCUMENTOS:DESTINO");
			ThirdPerson destiny = new ThirdPerson(destinyId, destinyName);
	
			/**
			 * [Teresa Ticket #607] INICIO Adjuntar en el registro el asunto del expediente.
			 * **/	
			
			IItem expediente = ExpedientesUtil.getExpediente(cct, currentState.getNumexp());
			String asuntoExpediente = expediente.getString("ASUNTO");
	       
			/**
			 * [Teresa Ticket #607] FIN Adjuntar en el registro el asunto del expediente.
			 * **/
	
			String summary = defaultForm.getProperty("SPAC_DT_DOCUMENTOS:NOMBRE") + "  -  Ref.Exp.: " + currentState.getNumexp() + " - Asunto Exp.: " + asuntoExpediente;
	    	// Generar el registro de salida
	        Register register = generateRegister(defaultForm, currentState, documentInfo, destiny, summary);
	        RegisterInfo registerInfo = registerAPI.insertRegister(register);
	        
	        if (registerInfo != null) {
	        	List<IItem> documentList = new ArrayList<IItem>();
	        	documentList.add(document);
	        	setDocumentsData(session, documentList, registerInfo, false);      
	        	
				//[Manu Ticket #107] - INICIO - ALSIGM3 Registrar salida, comunicaci�n con Comparece y Gesti�n de Representantes
		        SellarDocumentos sellarDocumentos = new SellarDocumentos(cct, document.getInt("ID_TRAMITE"), documentId);
		        sellarDocumentos.sellarDocumentos();
	        	//[Manu Ticket #107] - FIN - ALSIGM3 Registrar salida, comunicaci�n con Comparece y Gesti�n de Representantes

	        	register = registerAPI.readRegister(registerInfo);
	        	
	        	//Se crea el registro en la entidad que contiene el listado de apuntes vinculados con un expediente  
	        	RegisterHelper.insertRegistroES(cct, register, destiny, document.getString("NUMEXP"), document.getInt("ID_TRAMITE"));
	        	
	        	
	            // Establecer los datos para la vista
	            viewRegister(request, register);
	            request.setAttribute("NUMERO_REGISTRO", registerInfo.getRegisterNumber());
	            
	        } else {
	            request.setAttribute(ERROR_VAR, ERROR_REGISTRO);
	            //[Manu Ticket #110] - INICIO - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION
	      		document.set("ESTADONOTIFICACION", "ER");
	      		//[Manu Ticket #110] - FIN - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION	      		
	        }
			
			/*
			 * Nombre de la variable de sesi�n que mantiene los par�metros
			 * del tag
			 */
			String parameters = request.getParameter("parameters");
	    	
	        // Obtiene los parametros del tag y los salva en la request
	        Map<?, ?> sParameters = (Map<?, ?>)request.getSession().getAttribute(parameters);
	        if (sParameters != null) {
	            request.setAttribute("parameters", sParameters);
	        }
  		}
		//[Manu Ticket #913] SIGEM firma registro rechazado
  		else{
  			throw new ISPACInfo("errors.register.rechazado");
  		}

        return mapping.findForward("success");
	}

	
	protected void setDocumentsData(SessionAPI session, List<IItem> documents, RegisterInfo registerInfo, boolean updateDestiny) throws ISPACException{
		ClientContext cct = session.getClientContext();
		
		//[Manu # 625] SIGEM CVE Consulta de documentos - A�adir campos para registros de salida
		ServicioGestionCSV servicioGestionCSV;
		try {
			servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();
		
	  	    IInvesflowAPI invesFlowAPI = session.getAPI();
			IRegisterAPI registerAPI = invesFlowAPI.getRegisterAPI();		
	
	    	// Obtener toda la informaci�n del registro generado
	  		Register register = registerAPI.readRegister(registerInfo);
	  		ThirdPerson destiny = null;
	        if (updateDestiny && register.getRegisterData().getParticipants() != null && register.getRegisterData().getParticipants().length > 0){
	        	destiny = register.getRegisterData().getParticipants()[0];
	        }
	
			
			for (Iterator<IItem> iterator = documents.iterator(); iterator.hasNext();) {
				IItem document = (IItem) iterator.next();
				
	      		// Obtener el documento
	      		document.set("NREG", registerInfo.getRegisterNumber());
	      		document.set("FREG", registerInfo.getRegisterDate().getTime());
	      		
	      		//[Manu Ticket #110] - INICIO - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION
	      		document.set("ESTADONOTIFICACION", "PR");
	      		//[Manu Ticket #110] - FIN - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION	      		
	      		
	            // ORIGEN
	            if (register.getOriginOrganization() != null) {
	            	document.set("ORIGEN_ID", (register.getOriginOrganization().getId() == null) ? "" : register.getOriginOrganization().getId());
	            	document.set("ORIGEN", (register.getOriginOrganization().getName() == null) ? "" : register.getOriginOrganization().getName());
	            	document.set("ORIGEN_TIPO", ISicresConnector.ORGANIZATION_TYPE);
	            }
	    
	            if (destiny != null){
	            	document.set("DESTINO_ID", destiny.getId());
	            	document.set("DESTINO", destiny.getName());
	            }
	            
	          //[Manu # 625] SIGEM CVE Consulta de documentos - A�adir campos para registros de salida
		        InfoDocumentoCSV docCSV = servicioGestionCSV.getServicioDocumentos().getInfoDocumentoByCSV(document.getString("COD_COTEJO"));
		        docCSV.setNumeroRegistro(registerInfo.getRegisterNumber());
		        docCSV.setFechaRegistro(registerInfo.getRegisterDate().getTime());
		        docCSV.setOrigenRegistro( (register.getOriginOrganization().getName() == null) ? "" : register.getOriginOrganization().getName());
		        docCSV.setDestinoRegistro(document.getString("DESTINO"));
		        servicioGestionCSV.getServicioDocumentos().updateInfoDocumento(docCSV);
		      //[Manu # 625] SIGEM CVE Consulta de documentos - A�adir campos para registros de salida
		        
	      		// Guardar el documento con los datos de registro
	      		document.store(cct);			
			}
		} catch (SigemException e) {
			logger.error("ERROR. " + e.getMessage(), e);
		}
	      //[Manu # 625] SIGEM CVE Consulta de documentos - A�adir campos para registros de salida

	}
	
	protected void checkRegisterInfo(EntityForm defaultForm) {
		
		if (StringUtils.isBlank(defaultForm.getProperty("SPAC_DT_DOCUMENTOS:ID"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("SPAC_DT_DOCUMENTOS:DESTINO_ID"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("SPAC_DT_DOCUMENTOS:DESTINO"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("SPAC_DT_DOCUMENTOS:NOMBRE"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("BOOK_ID"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("OFFICE_CODE"))) {
			
		}
		
		if (StringUtils.isBlank(defaultForm.getProperty("ORG_UNIT_CODE"))) {
			
		}
		
	}
	
	protected Register generateRegister(EntityForm defaultForm, IState state, DocumentInfo[] documentInfo, ThirdPerson destiny, String summary) throws ISPACException {

		Register register = new Register();

		RegisterData registerData = new RegisterData(new ThirdPerson[] { destiny }, null,summary , documentInfo);

		register.setRegisterData(registerData);
		
		RegisterInfo originalRegister = new RegisterInfo();
		originalRegister.setBookId(defaultForm.getProperty("BOOK_ID"));
		originalRegister.setRegisterOffice(
				new RegisterOffice(defaultForm.getProperty("OFFICE_CODE"), null));
		register.setOriginalRegister(originalRegister);
		
		Organization originOrganization = new Organization();
		originOrganization.setCode(defaultForm.getProperty("ORG_UNIT_CODE"));
		register.setOriginOrganization(originOrganization);

		return register;
	}
	
	protected DocumentInfo[] getDocuments(SessionAPI session, List<String> documentIds) throws NumberFormatException, ISPACException{
  	    if (documentIds == null || documentIds.size() == 0){
  	    	return null;
  	    }
  	    	
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
		
		DocumentInfo[] documentInfo = null;
		//Se comprueba si por configuracion se indica que se tiene que enviar el contenido del documento a registro
  		if (StringUtils.equalsIgnoreCase("true", ISPACConfiguration.getInstance().get(ISPACConfiguration.SICRES_SEND_DOCUMENTS_CONTENT))){
	        
  			documentInfo = new DocumentInfo[documentIds.size()];
			Object connectorSession = null;
			try {
				connectorSession = genDocAPI.createConnectorSession();
				int i = 0;
				for (Iterator<String> iterator = documentIds.iterator(); iterator.hasNext();i++) {
					IItem document = entitiesAPI.getEntity(SpacEntities.SPAC_DT_DOCUMENTOS,	Integer.parseInt((String)iterator.next()));
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					//[DipuCR-Agustin] #149 registrar salida pdf no odt, indicar INFOPAG_RDE					
					genDocAPI.getDocument(connectorSession, document.getString(DocumentosUtil.INFOPAG_RDE), out);
					String mimetype = genDocAPI.getMimeType(connectorSession, document.getString(DocumentosUtil.INFOPAG_RDE));
					
					String tipoDocumental = "";
					try{
						IItem item = entitiesAPI.getEntity(SpacEntities.SPAC_CT_TPDOC, document.getInt(DocumentosUtil.ID_TPDOC));
						if(null != item){
							tipoDocumental = item.getString("TIPO");
						}
					} catch (Exception e){
						logger.error("ERROR. Capturamos todos los errores para que no falle al registrar. " + e.getMessage(), e);
					}
					if(StringUtils.isEmpty(tipoDocumental)){
						tipoDocumental = "TD07 - Notificaci�n.";
					}
					
					String tipoFirma = "TF06 - PAdES.";
					String csv = document.getString(DocumentosUtil.COD_COTEJO);
					
					documentInfo[i] = new DocumentInfo(String.valueOf(document.getKeyInt()),String.valueOf(document.getKeyInt()) +"."+MimetypeMapping.getExtension(mimetype),out.toByteArray(), document.getDate(DocumentosUtil.FDOC), tipoDocumental, tipoFirma, csv);        	
				}
			
			}finally {
				if (connectorSession != null) {
					genDocAPI.closeConnectorSession(connectorSession);
				}
			}
		}
  		return documentInfo;
	}
}