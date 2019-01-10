package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchPostalAddressesThirdPartyAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		IInvesflowAPI invesflowAPI = session.getAPI();
		IThirdPartyAPI thirdPartyAPI = invesflowAPI.getThirdPartyAPI();
		
		EntityForm defaultForm = (EntityForm) form;

		// Nombre del campo que contiene el valor de la b�squeda.
		String field = request.getParameter("field");

		//Identificador externo del tercero
		String externId = defaultForm.getProperty(field);
		
		if (StringUtils.isEmpty(externId)) {
			
			throw new ISPACInfo("exception.info.emptyExternId",false);
		}

		if (thirdPartyAPI == null) {
			throw new ISPACInfo("exception.thirdparty.notConfigured",false);
		}
		
		// Se buscan las direcciones postales para el tercero segun su identificador
		IPostalAddressAdapter [] direcciones = thirdPartyAPI.lookupPostalAddresses(externId);		
		
		if (ArrayUtils.isEmpty(direcciones)) {
			//...mostramos un mensaje informando del suceso
		    if (LOGGER.isInfoEnabled()) {
			    LOGGER.info("No se han encontrado direcciones para el tercero con identificador '" + externId + "'");
		    }
			throw new ISPACInfo("exception.info.postal.noAddresses",false);
		}
			
		request.setAttribute(ActionsConstants.THIRDPARTY_POSTAL_ADDRESSES_LIST, direcciones);
		return mapping.findForward("success");
	}
}