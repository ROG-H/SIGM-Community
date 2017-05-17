package ieci.tecdoc.sgm.registro.action;

import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ResultadoValidacion;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ServicioCriptoValidacion;
import ieci.tecdoc.sgm.registro.utils.Defs;

import java.security.Principal;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.gob.afirma.utils.UtilsBase64;


public class SeleccionarCertificadoAction extends RegistroWebAction {
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		X509Certificate certificate=null;		
		ServicioCriptoValidacion oServicioCriptoValidacion;
		try {
			oServicioCriptoValidacion = LocalizadorServicios.getServicioCriptoValidacion();
		} catch (SigemException e1) {
			request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_VALIDAR_USUARIO);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e1.toString());
	   		return mapping.findForward("failure");		}
		
		String certificadoId = (String)request.getSession().getAttribute(Defs.CERTIFICADO_ID);
				
		try{

			if (certificadoId==null || certificadoId.equals("")){
				request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
		    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: Al obtener el certificado de usuario");
				return mapping.findForward("failure");
			}
			
			certificate = getX509Certificate(request, certificadoId);
			
			if(certificate == null){
				request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
				request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: Al obtener el certificado de usuario");
				return mapping.findForward("failure");
			}			
			
			//Validacion del certificado, si no salta expcepcion sigue adelante y guarda en sesion el certificado
			oServicioCriptoValidacion.validateCertificate(new String(UtilsBase64.encodeBytes(certificate.getEncoded())));			
			
		}catch(Exception e){
			request.setAttribute(Defs.MENSAJE_ERROR, Defs.CERT_NO_VALIDO);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());
	   		return mapping.findForward("failure");
	   	}
		request.getSession().setAttribute(Defs.CERTIFICADO_SELECCIONADO, certificate);
		request.getSession().setAttribute(Defs.CERTIFICADO_SELECCIONADO, certificate.getSerialNumber());
		request.getSession().setAttribute(Defs.CERTIFICADO_SELECCIONADO, certificate.getSubjectDN());
		Principal p = certificate.getSubjectDN();
		p.getName();
		//request.setAttribute(Defs.TRAMITE_ID, tramiteId);
	   	return mapping.findForward("success");
	}
	
	
	public static X509Certificate getX509Certificate(HttpServletRequest request, String serialNumber) {
		String attributeName = "javax.servlet.request.X509Certificate";

		Object obj = request.getAttribute(attributeName);
		X509Certificate certificate = null;
		if (obj instanceof java.security.cert.X509Certificate[]) {
			java.security.cert.X509Certificate[] certArray = (java.security.cert.X509Certificate[]) obj;
			for(int i=0; i<certArray.length; i++)
				if(certArray[i].getSerialNumber().toString().equals(serialNumber))
					return certArray[i];
			return null;
		}

		if (obj instanceof java.security.cert.X509Certificate) {
			certificate = (X509Certificate) obj;
			if(certificate.getSerialNumber().toString().equals(serialNumber))
				return certificate;
			else return null;
		}

		return null;
	}
}
