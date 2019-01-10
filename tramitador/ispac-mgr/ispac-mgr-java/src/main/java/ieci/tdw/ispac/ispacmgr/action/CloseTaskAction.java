package ieci.tdw.ispac.ispacmgr.action;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class CloseTaskAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form, 
									   HttpServletRequest request,
									   HttpServletResponse response, 
									   SessionAPI session) throws Exception	{
		
		ClientContext cct = session.getClientContext();
		
    	IInvesflowAPI invesflowAPI = session.getAPI();
    	ITXTransaction tx = invesflowAPI.getTransactionAPI();
    	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		// Estado del contexto de tramitaci�n
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));
		
		

    	String idsTask = request.getParameter("idsTask");
		String[] taskids = idsTask.split("-");

    	int ntaskid = 0;
    	int nIdProcess = 0;
    	
		StringBuffer message = new StringBuffer();
		String[] args = new String[taskids.length * 2];

		ITask task = null;
    	for (int i = 0; i < taskids.length; i++) {
    		
    		
			ntaskid = Integer.parseInt(taskids[i]);
			task = invesflowAPI.getTask(ntaskid);

			// Comprobar que el usuario tenga permisos para cerrar el tr�mite
			if(!invesflowAPI.getWorkListAPI().isInResponsibleList(task.getString("ID_RESP"), task)) {
				message.append(getMessage(request, cct.getLocale(), "errors.close.task.resp", new Object[]{task.getString("NUMEXP")})+"<br/>");
				continue;
			}
			
			// [DipuCR-Agustin #559] Comprobar que el tramite no tiene notificaciones electr�nicas pendientes en Notifica
			if(!(ConfigurationMgr.getVarGlobal(cct, Constants.NOTIFICA.API_KEY_NOTIFICA).equals("")))
			{
	        	String strQuery = "WHERE TRAMITE="+ntaskid+" AND CORREO_CADUCIDAD_ENVIADA IS NULL";
	        	IItemCollection collection  = entitiesAPI.queryEntities("DPCR_ACUSES_NOTIFICA", strQuery);
	        	Iterator<IItem> it = collection.iterator();
		        it = collection.iterator();
		        if (it.hasNext()){
		        	message.append(getMessage(request, cct.getLocale(), "errors.close.task.notifica", new Object[]{task.getString("NOMBRE")})+"<br/>");
					continue;
		        }
				
			}

			// En el caso de cerrar un solo tr�mite se intentar�
			// seguir trabajando con el expediente
			if (taskids.length == 1) {
	        	nIdProcess = task.getInt("ID_EXP");
			}
			
			try {
				
				// Informaci�n del tr�mite en el contexto
				StateContext stateContext = cct.getStateContext();
				stateContext.setNumexp(task.getString("NUMEXP"));
				stateContext.setProcessId(nIdProcess);
				stateContext.setTaskCtlId(task.getInt("ID_CTTRAMITE"));
				stateContext.setTaskPcdId(task.getInt("ID_TRAMITE"));
				stateContext.setTaskId(task.getKeyInt());
				stateContext.setPcdId(task.getInt("ID_PCD"));
				stateContext.setStageId(task.getInt("ID_FASE_EXP"));
				stateContext.setStagePcdId(task.getInt("ID_FASE_PCD"));

				tx.closeTask(ntaskid);
			}
			catch (ISPACInfo e) {

				String msg = e.getExtendedMessage(request.getLocale());
				if (msg != null) {
					
					if (task == null) {
						task = invesflowAPI.getTask(ntaskid);
					}
					
					int imsg = 2 * i;
					msg = StringUtils.replace(msg, "{0}", "{" + imsg + "}");
					args[imsg] = task.getString("NOMBRE");
					imsg++;
					msg = StringUtils.replace(msg, "{1}", "{" + imsg + "}");
					args[imsg] = task.getString("NUMEXP");					
				}
				
				message.append(msg)
					   .append("<br/><br/>");
			}
		}
    	
		if (message.length() > 0) {
			throw new ISPACInfo(message.toString(), args);
		}	
    	
		/*ISPACInfo info=null;
		if (message.length() > 0) {
			info=new ISPACInfo(message.toString(), args,false);
			request.getSession().setAttribute("infoAlert", info);
		}*/

		// En el caso de cerrar un solo tr�mite se intentar�
		// seguir trabajando con el expediente
		if (nIdProcess > 0) {
			
			// Tr�mite de subproceso
			/*
			if (task.isComplex()) {
				return NextActivity.afterCloseComplexTask(request, currentstate, invesflowAPI, mapping);
			}
			*/
			
			// Tr�mite
        	return NextActivity.afterCloseTask(nIdProcess, currentstate, invesflowAPI, mapping);
		}

		return NextActivity.refresh(request, mapping, currentstate);
	}

}