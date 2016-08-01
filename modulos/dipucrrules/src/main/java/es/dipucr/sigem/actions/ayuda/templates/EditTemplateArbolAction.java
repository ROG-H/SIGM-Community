package es.dipucr.sigem.actions.ayuda.templates;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITemplateAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.dao.cat.CTTemplate;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.util.DocumentUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditTemplateArbolAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(EditTemplateArbolAction.class);
	
	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		
		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] {ISecurityAPI.FUNC_INV_TEMPLATES_EDIT});

		String template = request.getParameter("template");

		if (!StringUtils.isEmpty(template)) {
		
			IInvesflowAPI invesFlowAPI = session.getAPI();
	    	ITemplateAPI templateAPI = invesFlowAPI.getTemplateAPI();
	    	CTTemplate ctTemplate = templateAPI.getTemplate(Integer.parseInt(template));
	    	
			String usuario = (String) request.getAttribute("User");
			String plantilla = ctTemplate.getName();
        	String cod_plantilla = ctTemplate.getString("TEMPLATE:COD_PLANT");

        	logger.info("USUARIO:'" + usuario + "' - PLANTILLA [C�DIGO] '" + plantilla + "' [" + cod_plantilla + "] - MODO EDICI�N.");
        	
        	if (StringUtils.equalsIgnoreCase(ctTemplate.getMimetype(),"application/vnd.oasis.opendocument.text") && !ConfigurationMgr.getVarGlobalBoolean(session.getClientContext(), ConfigurationMgr.USE_ODT_TEMPLATES, false)){
	    		throw new ISPACInfo(getResources(request).getMessage("exception.template.odt.disabled"));
	    	}
	    	
			DocumentUtil.viewDocument(getServlet().getServletContext(),
									  request,
									  response,
									  "template",
									  session,
									  template,
									  null,
									  ctTemplate.getMimetype(), //"application/msword",
									  null,
									  false,
									  false);
		}

	    return null;
	}
	
}