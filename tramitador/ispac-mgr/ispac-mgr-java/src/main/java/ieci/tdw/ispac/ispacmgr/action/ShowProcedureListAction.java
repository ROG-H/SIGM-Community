package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISearchAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.SearchForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.IWorklist;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;
import ieci.tdw.ispac.ispacweb.manager.ISPACRewrite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowProcedureListAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request, 
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		
		IClientContext cct = session.getClientContext();
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI((ClientContext) cct);
		
		Map<?, ?> params = request.getParameterMap();
		
		//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
		int iAnio = 0;
		String[] anios = (String[]) params.get("anio");
		if(anios != null){
			String anio = anios[0];
			if(StringUtils.isNotEmpty(anio) && StringUtils.isNumeric(anio)){
				iAnio = Integer.parseInt(anio);
			}
		}
		
		request.getSession().setAttribute("stageId", "0");
		request.getSession().removeAttribute("stagePcdIdActual");
		//Eliminamos la busqueda de expedientes de sesi�n
		request.getSession().removeAttribute("numExpsSearch");
		//Si el usuario ha establecido valores se han de tener en cuenta para mostrar los valores introducido en caso de error o retorno
		Map<String, String> values = null;
		Map<?, ?> operators = null;
		SearchForm searchForm = (SearchForm) request.getSession().getAttribute(ActionsConstants.FORM_SEARCH_RESULTS);
		if(searchForm!=null){
			request.setAttribute("formSelect", searchForm.getIdxml()+"");
			values = searchForm.getValuesMap();
			operators=searchForm.getOperatorsMap();
		}
		
		// Ahora el formulario de b�squeda est� en sesi�n y se mantienen los par�metros de la �ltima b�squeda realizada
		request.getSession().removeAttribute(ActionsConstants.FORM_SEARCH_RESULTS);
		
		// Formulario de b�squeda avanzada
		if ((request.getParameter("search") != null) && 
			(request.getParameter("search").equals("true"))) {
			
			///////////////////////////////////////////////
			// Cambio del estado de tramitaci�n
			IState state = managerAPI.enterState(getStateticket(request), ManagerState.SEARCH, params);
			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
			if(anios != null){
				state.setAnio(iAnio);
			}
			storeStateticket(state, response);
			
		    ///////////////////////////////////////////////
		    //Menus
			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
			request.setAttribute("menus", MenuFactory.getSingleMenu((ClientContext) cct, getResources(request), state));
			
			//////////////////////////////////////////////
			// Formulario de b�squeda
			ISearchAPI searchAPI = invesflowAPI.getSearchAPI();
			
			List<?> forms = (List<?>) request.getAttribute("formList");
			if (forms == null) {
				
				IItemCollection icForms = searchAPI.getSearchForms();
				forms = CollectionBean.getBeanList(icForms);
				if (forms == null || forms.isEmpty()){
					///////////////////////////////////////////////
					// Cambio del estado de tramitaci�n
					state = managerAPI.enterState(getStateticket(request), ManagerState.PROCEDURELIST, params);

					//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
					if(anios != null){
						state.setAnio(iAnio);
					}
					
					storeStateticket(state, response);
					//Introducimos el ticket como atributo para que al recoger la excepci�n obtenga el estado 'PROCEDURELIST' en lugar de 'SEARCH'
					setStateticket(request, state);
					throw new ISPACInfo("exception.searchforms.any");
				}
				request.setAttribute("formList", forms);
			}

			String formSelect = (String) request.getAttribute("formSelect");
			if(StringUtils.isEmpty(formSelect)){
				formSelect = (String) request.getParameter("formSelect");
				request.setAttribute("urlForm", formSelect);
			}
			if (formSelect == null) {
				
				ItemBean bean = (ItemBean) forms.get(0);
				formSelect = bean.getProperty("ID").toString();
			}
			request.setAttribute("formSelect", formSelect);

			// Obtenci�n del formulario seleccionado
			ISPACRewrite ispacpath = new ISPACRewrite(getServlet().getServletContext());
			String xslurl = ispacpath.rewriteRealPath("xsl/SearchForm.xsl");

			String frm = "";
			if (formSelect != null) {
				
				try {
					
					// Parametrizamos los formularios de b�squeda con el nombre de usuario. Permite restringir los procedimientos [Josemi #595908]
					if (values == null) {
					   values = new HashMap<String, String>();
					}
					values.put("USERNAME_PARAM", session.getUserName());
					Responsible user = cct.getUser();					

					values.put("USER_UID_PARAM", "'" + user.getUID() + "'");
					values.put("USER_DEPT_UID_PARAM", "'" + user.getOrgUnit().getUID() + "'");
							
					Collection<?> groups = user.getUserGroups();
					String userGroups = "";
					if ((groups != null) && !groups.isEmpty()) {
						for (Iterator<?> it = groups.iterator(); it.hasNext();) {
							Responsible group = (Responsible) it.next();
							if (StringUtils.isNotBlank(userGroups)) {
								userGroups += ",";
							}
							userGroups += "'" + DBUtil.replaceQuotes(group.getUID()) + "'";
						}
					}
					
					if(StringUtils.isEmpty(userGroups)){
						userGroups = "''";
					}
					values.put("USER_GROUPS_UID_PARAM", userGroups);
					
					frm = searchAPI.buildHTMLSearchForm(Integer.parseInt(formSelect), xslurl, ResourceBundle.getBundle(BUNDLE_NAME, cct.getLocale()), cct.getLocale(), params,values,operators);
				}
				catch (ISPACException e) {
					//frm = "<b>Error:</b>El formulario de consulta no est&aacute; bien definido";
					IItem iForm = searchAPI.getSearchForm(Integer.parseInt(formSelect));
					throw new ISPACInfo("exception.searchforms.generate", new String[] {iForm.getString("DESCRIPCION")});
				}
			}
			request.setAttribute("Form", frm);

			return mapping.findForward("search");
		}
		// Pantalla principal
		else {
			
			///////////////////////////////////////////////
			// Cambio del estado de tramitaci�n
			IState state = managerAPI.enterState(getStateticket(request), ManagerState.PROCEDURELIST, params);

			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
			if(anios != null){
				state.setAnio(iAnio);
			}
			
			storeStateticket(state, response);


			IWorklist managerwl = managerAPI.getWorklistAPI();
			
			// Responsabilidades del usuario conectado
			String resp = managerwl.getRespString(state);
//			
//			// Obtenemos el listado de los procedimientos
//			IItemCollection itcPcd = managerwl.getProcedures(state, resp);
//			if (itcPcd.next()) {
//				
//				List listProc = CollectionBean.getBeanList(itcPcd);
//				request.setAttribute("ProceduresList", listProc);
//	
//				// Mapa de procedimientos con los expedientes en las fases para cada procedimiento
//				Map mapStages = SpacMgr.getStagesProcs(session, itcPcd, resp);
//				request.setAttribute("StagesProcMap", mapStages);
//			}
//			
//			// Obtenemos el listado de los tr�mites
//			IItemCollection itcTask = managerwl.getProcedureTasks(state, resp);
//			if (itcTask.next()) {
//				
//				List tasksList = CollectionBean.getBeanList(itcTask);
//				request.setAttribute("TasksList", tasksList);
//				
//				// Obtenemos el listado de los subprocesos
//				IItemCollection itcSubPcd = managerwl.getSubProcedures(state, resp);
//				/*
//				List listSubProc = CollectionBean.getBeanList(itcSubPcd);
//				request.setAttribute("SubProceduresList", listSubProc);
//				*/			
//				
//				if (itcSubPcd.next()) {
//					
//					// Mapa de subprocesos con las actividades para cada subproceso
//					Map mapActivities = SpacMgr.getStagesProcs(session, itcSubPcd, resp);
//					request.setAttribute("ActivitiesProcMap", mapActivities);
//				}
//			}
//
//			// A�adimos a la lista de tramites el nombre del procedimiento
//			// String con los identificadores de los tramites separados por una coma
//			/*
//			String idsTasks = CollectionBean.getBeanMap(itcTask, "ID_CTTRAMITE")
//					.keySet().toString();
//			idsTasks = idsTasks.substring(1, idsTasks.length() - 1);
//
//			// Si existe algun tr�mite...
//			if (StringUtils.isNotBlank(idsTasks)) {
//				
//				// Obtenemos los procedimientos a los que pertenecen los tramites
//				// para a�adir al tramite informacion del procedimiento
//				IEntitiesAPI entapi = cct.getAPI().getEntitiesAPI();
//				Map mapTableEntity = new HashMap();
//				mapTableEntity.put("TASK", "SPAC_P_TRAMITES");
//				mapTableEntity.put("PCD", "SPAC_CT_PROCEDIMIENTOS");
//				IItemCollection pcds = entapi.queryEntities(mapTableEntity,
//						"WHERE TASK.ID_PCD = PCD.ID AND TASK.ID IN (" + idsTasks
//								+ ")");
//
//				// A�adimos a cada agrupacion de tramites el nombre del
//				// procedimiento al que pertenecen
//				Map mapPcds = CollectionBean.getBeanMap(pcds, "TASK:ID");
//				for (Iterator iter = tasksList.iterator(); iter.hasNext();) {
//					ItemBean itemTask = (ItemBean) iter.next();
//					String pcd = "";
//					if (mapPcds.get(itemTask.getString("ID_TRAMITE")) != null)
//						pcd = ((ItemBean) mapPcds.get(itemTask
//								.getString("ID_TRAMITE"))).getString("PCD:NOMBRE");
//					itemTask.setProperty("NOMBRE_PCD", pcd);
//				}
//			}
//			*/
//
//			// A�adimos a la lista de tramites el nombre del procedimiento
//			// Si existe algun tr�mite...
//			/*
//			if (!ieci.tdw.ispac.ispaclib.utils.CollectionUtils.isEmpty(groupTasksList)) {
//				// Obtenemos los procedimientos a los que pertenecen los tramites
//				// para a�adir al tramite informacion del procedimiento
//				IEntitiesAPI entapi = cct.getAPI().getEntitiesAPI();
//				Map mapTableEntity = new HashMap();
//				mapTableEntity.put("TASK", "SPAC_P_TRAMITES");
//				mapTableEntity.put("PCD", "SPAC_CT_PROCEDIMIENTOS");
//	
//				// String con los identificadores de los tramites de las agrupaciones separados por una coma
//				String idsTramitesGroupTasks = CollectionBean.getBeanMap(itcGroupTask, "ID_CTTRAMITE")
//						.keySet().toString();
//				idsTramitesGroupTasks = idsTasks.substring(1, idsTasks.length() - 1);
//	
//				IItemCollection pcds = entapi.queryEntities(mapTableEntity,
//						"WHERE TASK.ID_PCD = PCD.ID AND TASK.ID IN (" + idsTramitesGroupTasks
//								+ ")");
//	
//				// A�adimos a cada agrupacion de tramites el nombre del
//				// procedimiento al que pertenecen
//				Map mapPcds = CollectionBean.getBeanMap(pcds, "TASK:ID");
//				for (Iterator iter = groupTasksList.iterator(); iter.hasNext();) {
//					ItemBean itemGroupTask = (ItemBean) iter.next();
//					String pcd = "";
//					if (mapPcds.get(itemGroupTask.getString("ID_TRAMITE")) != null)
//						pcd = ((ItemBean) mapPcds.get(itemGroupTask
//								.getString("ID_TRAMITE"))).getString("PCD:NOMBRE");
//					itemGroupTask.setProperty("NOMBRE_PCD", pcd);
//				}
//			}
//			*/
//			
//			// Elementos en la Bandeja de entrada
//			IInboxAPI ib = invesflowAPI.getInboxAPI();
//			IItemCollection icInbox = ib.getInbox(resp);
//			List inboxList = CollectionBean.getBeanList(icInbox);
//			request.setAttribute("InboxItems", inboxList);

		    ///////////////////////////////////////////////
		    //Menus
			//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
			request.setAttribute("menus", 
					MenuFactory.getInboxMenu(session.getClientContext(), getResources(request),resp, state));
			
//		    ///////////////////////////////////////////////
//		    // Formateadores
//			// FormatterManager.storeBFShowProcedureListAction(request, getPath());
//			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
//			BeanFormatter formatter = factory
//					.getFormatter(getISPACPath("/digester/proceduresformatter.xml"));
//			request.setAttribute("FormatterPcd", formatter);
//			BeanFormatter formatter2 = factory
//					.getFormatter(getISPACPath("/digester/tasksformatter.xml"));
//			request.setAttribute("FormatterTask", formatter2);
			
			// Per�odo de recarga de la bandeja de entrada
			request.setAttribute("reloadPeriod", ConfigurationMgr.getVarGlobal(cct, 
					ConfigurationMgr.INBOX_RELOAD_PERIOD, 
					ConfigurationMgr.DEFAULT_INBOX_RELOAD_PERIOD));
			
			return mapping.findForward("success");
		}
	}
	
}