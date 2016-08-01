package com.tsol.modulos.buscador.actions;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tsol.modulos.buscador.beans.SearchBean;
import com.tsol.modulos.buscador.beans.SearchForm;
import com.tsol.modulos.buscador.dao.SearchDAO;
import com.tsol.modulos.buscador.utils.BuscadorDocsUtils;

public class SearchResultAction extends Action {

	private static final Logger LOGGER = Logger.getLogger(SearchResultAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		SearchForm searchForm = (SearchForm) form;

		// Componer el c�digo de cotejo
		String codCotejo = new StringBuffer()
			.append(searchForm.getCotejo1())
			.append(searchForm.getCotejo2())
			.append(searchForm.getCotejo3())
			.append(searchForm.getCotejo4())
			.toString();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.debug("C�digo de cotejo: [" + codCotejo + "]");
		}

		try {

			// API de sesi�n de iSPAC
			SessionAPI sessionAPI = SessionAPIFactory.getSessionAPI(request, response);

	        // Contexto para la auditor�a
			BuscadorDocsUtils.setAuditContext(request, sessionAPI);//[dipucr-Felipe #828 #1216]

			// Obtener la informaci�n del documento
			SearchBean searchBean = SearchDAO.searchDocument(sessionAPI.getClientContext(), codCotejo);
			request.setAttribute("searchBean", searchBean);

		} catch (Exception e) {
			LOGGER.error("Se ha producido un error inesperado", e);

			ActionErrors errors = new ActionErrors();
			errors.add("error", new ActionError("search.error"));

			saveErrors(request, errors);
		}

		return mapping.findForward("success");
	}
}
