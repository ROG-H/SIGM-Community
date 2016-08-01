package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISearchAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.tx.TXFaseDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXProcesoDAO;
import ieci.tdw.ispac.ispaclib.search.objects.impl.SearchExpState;
import ieci.tdw.ispac.ispaclib.search.objects.impl.SearchInfo;
import ieci.tdw.ispac.ispaclib.search.vo.SearchResultVO;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.SearchForm;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.manager.ISPACRewrite;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class RelateExpedientAction extends BaseDispatchAction {
	
	protected static final String LIST_RELATIONS_KEY = "LIST_RELATIONS";

    public ActionForward form(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

    	ClientContext cct = session.getClientContext();
    	
    	IInvesflowAPI invesflowAPI = session.getAPI();

		ISPACRewrite ispacPath = new ISPACRewrite(getServlet()
				.getServletContext());

		String xml = ispacPath.rewriteRealPath("xml/relateExpedientForm.xml");
		String xsl = ispacPath.rewriteRealPath("xsl/SearchForm.xsl");
		
		//////////////////////////////////////////////
		// Formulario de b�squeda
		ISearchAPI searchAPI = invesflowAPI.getSearchAPI();
		
		String frm = searchAPI.buildHTMLSearchForm(new File(xml), xsl, ResourceBundle.getBundle(BUNDLE_NAME, cct.getLocale()), cct.getLocale(), request.getParameterMap());
		request.setAttribute("Form", frm);

		return mapping.findForward("form");
	}

    public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		SearchForm searchForm = (SearchForm) form;

		IInvesflowAPI invesFlowAPI = session.getAPI();
		ISearchAPI searchAPI = invesFlowAPI.getSearchAPI();
//		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());

//		// Estado actual
//		IState state = managerAPI.currentState(getStateticket(request));

		ISPACRewrite ispacPath = new ISPACRewrite(getServlet()
				.getServletContext());

		String xml = ispacPath.rewriteRealPath("xml/relateExpedientForm.xml");

		int domain = searchForm.getDomain();
		try{
			SearchInfo searchinfo = searchAPI.getSearchInfoPath(xml, domain,
					SearchExpState.ALL);
	
			setQuery(searchinfo, searchForm);
			
	//		// A�adir el identificador del procedimiento
	//		searchinfo.setFieldValueForEntity("spac_expedientes", "ID_PCD", String.valueOf(state.getPcdId()));
	//		searchinfo.setFieldOperatorForEntity("spac_expedientes", "ID_PCD", "=");
			
			//MQE #142 Modificamos para que muestre todos los expedietes sin tener en cuenta los permisos
//			SearchResultVO searchResultVO= searchAPI.getLimitedSearchResults(searchinfo);
			SearchResultVO searchResultVO= searchAPI.getLimitedSearchResultsRelateExpedient(searchinfo);
			//MQE fin modificaciones ticket #142
			List lResults = CollectionBean.getBeanList(searchResultVO.getResultados());
			if(lResults.size()< searchResultVO.getNumTotalRegistros()){ 
					request.setAttribute("maxResultados", String.valueOf(searchResultVO.getNumMaxRegistros())); 
					request.setAttribute("numTotalRegistros", String.valueOf(searchResultVO.getNumTotalRegistros()));
	
			} 
			
			request.setAttribute("ResultsList", lResults);
	
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			BeanFormatter formatter = factory.getFormatter(xml);
	
			request.setAttribute("Formatter", formatter);
	
			return mapping.findForward("results");
			}catch(ISPACInfo e){
						e.setRefresh(false);
						throw e;
			}
	}

    public ActionForward enter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		 IInvesflowAPI invesFlowAPI = session.getAPI();
		 IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		
		 // Establecer el n�mero de expediente seleccionado en la b�squeda
		 String numexp = request.getParameter("numexp");
		 if (StringUtils.isNotBlank(numexp)) {
			 
			 SearchForm searchForm = (SearchForm) form;
			 searchForm.setProperty("NUMEXP_HIJO", numexp);
		 }
		 
		 // Obtener las relaciones entre expedientes
		 IItemCollection itemCol = entitiesAPI.queryEntities(SpacEntities.SPAC_TBL_007, " WHERE VIGENTE = 1 ORDER BY VALOR");
		 request.setAttribute(LIST_RELATIONS_KEY, CollectionBean.getBeanList(itemCol));

		return mapping.findForward("enter");
	}

    public ActionForward relate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		ClientContext cct = session.getClientContext();

		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(
				cct);

		SearchForm searchForm = (SearchForm) form;

		// Estado actual
		IState state = managerAPI.currentState(getStateticket(request));
		String numexp = state.getNumexp();

		// Validar el formulario
		//#1023 * SIGEM Hist�ricos de expedientes, tr�mites, documentos e intervinientes.
		ActionMessages errors = validate(searchForm, entitiesAPI, numexp, session);
		if (errors.isEmpty()) {

			// Ejecuci�n en un contexto transaccional
			boolean bCommit = false;
			
			try {
				// Abrir transacci�n
				cct.beginTX();

				// Generar la relaci�n entre expedientes
				IItem expRelacionados = entitiesAPI
						.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
				expRelacionados.set("NUMEXP_PADRE", numexp);
				expRelacionados.set("NUMEXP_HIJO", searchForm
						.getProperty("NUMEXP_HIJO"));

				String relation = searchForm.getProperty("RELACION");
				if (StringUtils.isEmpty(relation)) {

					IItem relationType = entitiesAPI.getEntity(
							SpacEntities.SPAC_TBL_007, Integer
									.parseInt(searchForm
											.getProperty("TIPO_RELACION")));
					relation = relationType.getString("VALOR");
				}
				expRelacionados.set("RELACION", relation);

				expRelacionados.store(cct);

				// Si todo es correcta se hace commit de la transacci�n
				bCommit = true;
			}
			finally {
				cct.endTX(bCommit);
			}
			
		    //[eCenpri-Manu Ticket#129] INICIO - ALSIGM3 Crear nuevo evento al relacionar expediente.
			//Ejecutar evento de sistema al relacionar expedientes.
			
			//Se construye el contexto de ejecuci�n de eventos.
			EventManager eventmgr = new EventManager(cct, null);
			
			eventmgr.newContext();
			TXTransactionDataContainer dataContainer = new TXTransactionDataContainer(cct);
			//Buscamos el expediente
        	int idProcess = state.getProcessId();
        	int idProcedure = state.getPcdId();
        	int idStage = state.getStageId();
        	
			TXProcesoDAO process = dataContainer.getProcess(idProcess);
			TXFaseDAO txStage = dataContainer.getStage(idStage);
			
			eventmgr.getRuleContextBuilder().addContext(process);
			eventmgr.getRuleContextBuilder().addContext(txStage);
			
			int eventObjectType = EventsDefines.EVENT_OBJ_PROCEDURE;
			
			eventmgr.processSystemEvents( eventObjectType, EventsDefines.EVENT_EXEC_RELACIONAR);
	
			//Ejecutar evento al iniciar proceso.
			eventmgr.processEvents( eventObjectType, idProcedure, EventsDefines.EVENT_EXEC_RELACIONAR);
		    //[eCenpri-Manu Ticket#129] FIN - ALSIGM3 Crear nuevo evento al relacionar expediente.

			return mapping.findForward("success");
		}
		else {
			saveErrors(request, errors);

			// Obtener las relaciones entre expedientes
			IItemCollection itemCol = entitiesAPI.queryEntities(
					SpacEntities.SPAC_TBL_007,
					" WHERE VIGENTE = 1 ORDER BY VALOR");
			request.setAttribute(LIST_RELATIONS_KEY, CollectionBean
					.getBeanList(itemCol));

			return mapping.findForward("enter");
		}
	}

    /**
	 * #1023 SIGEM Hist�ricos de expedientes, tr�mites, documentos e intervinientes.
	 * 
	 * @param entityForm
	 * @param entitiesAPI
	 * @param numexp
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private ActionMessages validate(SearchForm entityForm,
			IEntitiesAPI entitiesAPI, String numexp, SessionAPI session) throws Exception {

		ActionMessages errors = new ActionMessages();

		// Expediente
		String numexpHijo = entityForm.getProperty("NUMEXP_HIJO").replaceAll(
				"'", "").trim();
		if (StringUtils.isEmpty(numexpHijo)) {

			errors.add("", new ActionMessage(
					"forms.expRelacionar.error.expedient.empty"));
		} else if (StringUtils.equalsIgnoreCase(numexpHijo, numexp)) {
			errors.add("", new ActionMessage(
					"forms.expRelacionar.error.expedient.repeated"));
		} else {
			IItem expedient = ExpedientesUtil.getExpediente(session.getClientContext(), numexpHijo);
			if (expedient == null) {

				errors.add("", new ActionMessage(
						"forms.expRelacionar.error.expedient.noExist"));
			}
		}
		entityForm.setProperty("NUMEXP_HIJO", numexpHijo);

		// Relacion
		String relationType = entityForm.getProperty("TIPO_RELACION");
		if (StringUtils.isEmpty(relationType)) {

			String relation = entityForm.getProperty("RELACION");
			if (StringUtils.isEmpty(relation)) {

				errors.add("", new ActionMessage(
						"forms.expRelacionar.error.relation.empty"));
			}
		}

		return errors;
	}
}
