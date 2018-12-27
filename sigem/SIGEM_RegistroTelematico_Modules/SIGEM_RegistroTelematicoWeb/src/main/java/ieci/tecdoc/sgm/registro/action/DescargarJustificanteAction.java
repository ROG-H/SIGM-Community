package ieci.tecdoc.sgm.registro.action;

// [Josemi #545416] Recuperar justificante desde Reg. Tel y/o Reg. Presencial
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.registro.Document;
import ieci.tecdoc.sgm.core.services.registro.DocumentQuery;
import ieci.tecdoc.sgm.core.services.registro.Page;
import ieci.tecdoc.sgm.core.services.registro.RegisterWithPagesInfoPersonInfo;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;
import ieci.tecdoc.sgm.registro.util.Definiciones;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;

public class DescargarJustificanteAction extends RegistroWebAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		ServletOutputStream out = response.getOutputStream();

		try {
			// [Josemi #545416] Recuperar justificante desde Reg. Tel y/o Reg. Presencial
			// Codigo de acceso al justificante en el Registro Telematico
			HttpSession session = request.getSession();

			String sessionId = (String)session.getAttribute(Defs.SESION_ID);
			String numRegistro = (String)request.getParameter("numeroRegistro");

			ServicioRegistroTelematico oServicio = LocalizadorServicios.getServicioRegistroTelematico();

           	// Obtener el justificante a mostrar
			byte[] justificante = oServicio.obtenerJustificanteRegistro(sessionId, numRegistro, Misc.obtenerEntidad(request));

			
         
			response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
            response.setContentType("application/pdf");
            response.setHeader("Content-Transfer-Encoding", "binary");
           	response.setHeader("Content-Disposition", "attachment; filename=\"Justificante_" + numRegistro + ".pdf\"");
		
            response.setContentLength(justificante.length);
            out.write(justificante, 0, justificante.length);

        } catch(Exception e) {

    		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_OBTENER_JUSTIFICANTE);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.getMessage());

	    	return mapping.findForward("failure");
        } finally {
        	if (out != null) {
        		out.flush();
        		out.close();
        	}
        }

	    return null;
   	}
}
