package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IInboxAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowIntrayListAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		ClientContext cct = session.getClientContext();
		
		IInvesflowAPI invesflowAPI = session.getAPI();
		IInboxAPI inbox = invesflowAPI.getInboxAPI();

		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);

		// Se cambia el estado de tramitaci�n
    	Map params = request.getParameterMap();
    	IState state = managerAPI.enterState(getStateticket(request),
    			ManagerState.INTRAYLIST, params);
    	storeStateticket(state, response);

	    // Men�
		//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el �rea de trabajo por a�o de inicio de expediente.
	    request.setAttribute("menus", MenuFactory.getSingleMenu(cct, getResources(request), state));

		// Registros distribuidos
		request.setAttribute("IntraysList", inbox.getIntrays());
		
		return mapping.findForward("success");
	}
}
