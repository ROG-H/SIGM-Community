package ieci.tecdoc.sgm.registro.action;

import ieci.tecdoc.sgm.base.base64.Base64Util;
import ieci.tecdoc.sgm.base.miscelanea.Goodies;
import ieci.tecdoc.sgm.base.xml.core.XmlDocument;
import ieci.tecdoc.sgm.base.xml.core.XmlElement;
import ieci.tecdoc.sgm.base.xml.core.XmlTransformer;
import ieci.tecdoc.sgm.base.xml.lite.XmlTextBuilder;
import ieci.tecdoc.sgm.core.config.impl.spring.MultiEntityContextHolder;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.firma.ServicioFirmaDigital;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ResultadoValidacion;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ServicioCriptoValidacion;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper;
import ieci.tecdoc.sgm.registro.util.Definiciones;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;

import java.security.cert.X509Certificate;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import es.gob.afirma.transformers.TransformersFacade;

public class RegistrarSolicitudAction extends RegistroWebAction {

    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {

    	HttpSession session = request.getSession();

	    try {
	    	String tramiteId = (String)session.getAttribute(Defs.TRAMITE_ID);
	    	String oficina = (String)session.getAttribute(Defs.OFICINA);
	    	String sessionId = (String)session.getAttribute(Defs.SESION_ID);

	    	String refresco = (String)request.getParameter(Defs.REFRESCO);
	    	boolean bRefresco = true;
	    	if (refresco !=null && !"".equals(refresco)) {
	    		bRefresco = new Boolean(refresco).booleanValue();
	    	}

	    	if (bRefresco) {
			   	//Se borra el fichero xml de solicitud que puede consultar el usuario.
			   	//No se si se deber�a borrar aqu� o una vez finalizado todo el proceso de registro
		    	String tmpXmlPath = (String)session.getServletContext().getAttribute(Defs.PLUGIN_TMP_PATH_XML);
		    	String separador = System.getProperty("file.separator");
			   	String ruta_xml = session.getServletContext().getRealPath("") + separador + tmpXmlPath + separador + sessionId + "_" + tramiteId + ".xml";
			   	File borrar_xml = new File(ruta_xml);
			   	borrar_xml.delete();

		        // Comprobar si la solicitud se ha firmado (configuraci�n establecida para el tr�mite)
			   	// para entonces validar el certificado con el que se ha firmado
			   	boolean bFirma = true;
			   	String firmar_solicitud = (String)session.getAttribute(Defs.FIRMAR_SOLICITUD);
			   	if (firmar_solicitud == null || firmar_solicitud.equals("")) {
			   		firmar_solicitud = "1";
			   	}
			   	if (firmar_solicitud.equals("0")) {
			   		bFirma = false;
			   	}

	     	   	//Se crea el XML firmado por el usuario
			   	String firma = (String)request.getParameter(Defs.FIRMA);
			   	String xml_request = (String)session.getAttribute(Defs.REQUEST);
			   	byte[] registryReq = Base64Util.decode(xml_request);
			   	XmlDocument xmlDoc = new XmlDocument();
			   	XmlElement elem, elem2, root;
			   	xmlDoc.createFromStringText(Goodies.fromUTF8ToStr(registryReq));
		        root = xmlDoc.getRootElement();
		        elem = root.getChildElement(Definiciones.SIGNATURE);
		        // La firma no se inserta utilizando el value del XmlElement ya que
		        // los saltos de l�nea aparecer�an como &#13; en el XML de la solicitud de registro firmada
		        // Espacio en blanco en el nodo <Firma/> para que se abra y se cierre <Firma> </Firma>
		        // al convertirlo a cadena
		        elem.setValue(" ");
		        
		        //INICIO [eCenpri-Felipe #457]
		        elem2 = root.getChildElement(Definiciones.ID_TRANSACCION);
		        //[Manu Ticket #1090] - INICIO Poner en marcha la opci�n Consulta de Expedientes.
		        if(elem2!= null){
		        	elem2.setValue(" ");
		        }
		        else{
		        	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		        	domFactory.setIgnoringComments(true);
		        	Document doc = xmlDoc.getDomDocument(); 

		        	NodeList nodes = doc.getElementsByTagName(Definiciones.REQUEST_HEADER);
		        	
		        	Text a = doc.createTextNode(" "); 
		        	Element p = doc.createElement(Definiciones.ID_TRANSACCION); 
		        	p.appendChild(a);
		        	
		        	XmlTextBuilder x = new XmlTextBuilder();
		        	x.addSimpleElement(Definiciones.ID_TRANSACCION, " ");
		        	nodes.item(0).appendChild(p);		        	
		        }
				//[Manu Ticket #1090] - FIN Poner en marcha la opci�n Consulta de Expedientes.
		        //FIN [eCenpri-Felipe #457]
		        
		        xml_request = new String(xmlDoc.getStringText(false));

		        // Incrustar la firma en la solicitud de registro en el nodo <Firma> correspondiente
		        // que ser� el �ltimo nodo <Firma> de la <Solicitud_Registro>
		        int indexBeginSignature = xml_request.lastIndexOf("<"+Definiciones.SIGNATURE+">");
		        int indexEndSignature = xml_request.lastIndexOf("</"+Definiciones.SIGNATURE+">");

		        xml_request = xml_request.substring(0, indexBeginSignature + Definiciones.SIGNATURE.length() + 2)
		        			+ firma
		        			+ xml_request.substring(indexEndSignature, xml_request.length());
		        
		        //INICIO [eCenpri-Felipe #457]
		        //INICIO [eCenpri-Agustin S3#351] Comprobacion del formulario firmado y si lo ha firmado el mismo que se autentico
		        //Incrustar el idTransaccion
		        if(bFirma){
				   	
					// Validar el certificado con el que se ha firmado
					ServicioCriptoValidacion oServicioCriptoValidacion = LocalizadorServicios.getServicioCriptoValidacion();
					   	
					//[DipuCR-Agustin #115] como no obtengo el certificado de autofirma en principio solo utilizo el filtro para firmar y no valido
					//[DipuCR-Agustin #341] validar certificado con el que se realizo la firma del formulario
					String certificateSerialNumber = (String)request.getParameter(Defs.CERTIFICADO_SELECCIONADO_SERIALNUMBER);
					String certificateSubject = (String)request.getParameter(Defs.CERTIFICADO_SELECCIONADO_SUBJECT);
					
					
					//Comento la parte de validacion de la firma del formulario para subir a produccion
//					ResultadoValidacion resultadoValidacion = oServicioCriptoValidacion.validateSignature(firma);
//					
//					//Si el certificado no es valido
//					if (resultadoValidacion.getResultadoValidacion().equals(ResultadoValidacion.VALIDACION_ERROR)) {
//
//					    	session.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_CERTIFICADO_FIRMA);
//					    	session.setAttribute(Defs.MENSAJE_ERROR_DETALLE, resultadoValidacion.getMensajeValidacion());
//
//					    	return mapping.findForward("failure");
//					}
//					
//					//Si es valido vamos a comprobar que tiene el mismo numero de serie que con el que se autentico o pertenece al mismo sujeto
//					Map<String, Object>[ ] signReports = (Map[ ]) resultadoValidacion.getPropertiesResult().get(TransformersFacade.getInstance().getParserParameterValue("IndividualSignatureReport"));
//					Map<String, Object> individualSignReport = signReports[0];
//					// comprobamos valores de la informaci�n del certificado (tipo mapa)
//					Map<String, String> certificateInfo = (Map<String, String>) individualSignReport.get(TransformersFacade.getInstance().getParserParameterValue("ReadableCertificateInfo"));
//					
//					if(!certificateSerialNumber.equals(certificateInfo.get("serialNumber"))){
//						if(!certificateSubject.equals(certificateInfo.get("subject"))){
//							
//							session.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO_FIRMA);
//					    	session.setAttribute(Defs.MENSAJE_ERROR_DETALLE, resultadoValidacion.getMensajeValidacion());
//
//					    	return mapping.findForward("failure");
//							
//						}
//						
//					}
				   	
			        
			        //[Agus Ticket #1072] Sustituyo el idTransaon por el tiempoenmiliseg+idTramite	        

					//			        String idTransaction = firmaDigital.registrarFirma
					//		        	(Base64Util.decode(firma), Base64Util.decode(certificadoFirma), Base64Util.decode(hashSolicitud));

				    Date date = new Date(); 
			        String idTransaction = String.valueOf(date.getTime());
			        idTransaction.concat(tramiteId);
				   	//[Agus Ticket #1072]
			        
			        int indexBeginIdTransaction = xml_request.indexOf("<"+Definiciones.ID_TRANSACCION+">");
			        int indexEndIdTransaction = xml_request.indexOf("</"+Definiciones.ID_TRANSACCION+">");
			        
			        xml_request = xml_request.substring(0, indexBeginIdTransaction + Definiciones.ID_TRANSACCION.length() + 2)
			        			+ idTransaction
			        			+ xml_request.substring(indexEndIdTransaction, xml_request.length());
		        }
		        //FIN [eCenpri-Felipe #457]

		        String datosEspecificos = (String)session.getAttribute(Defs.DATOS_ESPECIFICOS);
		        if (datosEspecificos == null) {
		        	datosEspecificos = new String("");
		        }

		        Entidad entidad = Misc.obtenerEntidad(request);
		        Locale idioma = LocaleFilterHelper.getCurrentLocale(request);
			   	if (idioma == null || idioma.getLanguage() == null) {
			   		idioma = request.getLocale();
			   	}

		        // Plantilla de justificante de registro
			   	String plantillaPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "plantilla_", "jasper");
		   		if (plantillaPath == null) {

		   			request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado la plantilla del justificante de registro para este tr�mite");
			   		return mapping.findForward("failure");
			   	}

		   		String certificado = (String)session.getServletContext().getAttribute(Defs.PLUGIN_CERTIFICADO);
		        byte[] registryRequest = Goodies.fromStrToUTF8(xml_request);

		        ServicioRegistroTelematico oServicio = LocalizadorServicios.getServicioRegistroTelematico();

		        // Registrar la solicitud de registro
		    	registryRequest = oServicio.registrar(sessionId, registryRequest, datosEspecificos,	idioma.getLanguage(), oficina, plantillaPath, certificado, Misc.obtenerEntidad(request));

			   	// Obtener la ruta de la xsl de informaci�n de registro
			   	String xslPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "informacion_registro_", "xsl");
			   	if (xslPath == null) {

			   		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_ENVIO_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado el formulario para este tr�mite");
			   		return mapping.findForward("failure");
			   	}

			   	String xml_registryRequest = Goodies.fromUTF8ToStr(registryRequest);

			   	// Presentar la informaci�n del registro realizado (XSL + XML)
			   	XmlDocument xmlDocReg = new XmlDocument();
			   	xmlDocReg.createFromStringText(xml_registryRequest);
			   	String xml_w_xsl = XmlTransformer.transformXmlDocumentToHtmlStringTextUsingXslFile(xmlDocReg, xslPath);
			   	session.setAttribute(Defs.INFORMACION_REGISTRO, xml_w_xsl);
			   	session.setAttribute(Defs.JUSTIFICANTE_REGISTRO, xml_registryRequest);
	    	}
	    	else {
	    		Entidad entidad = Misc.obtenerEntidad(request);
	    		Locale idioma = LocaleFilterHelper.getCurrentLocale(request);
			   	if (idioma == null || idioma.getLanguage() == null) {
			   		idioma = request.getLocale();
			   	}

			   	// Obtener la ruta de la xsl de informaci�n de registro
			   	String xslPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "informacion_registro_", "xsl");
			   	if (xslPath == null) {

			   		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_ENVIO_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado el formulario para este tr�mite");
			   		return mapping.findForward("failure");
			   	}

			   	String justificante = (String)session.getAttribute(Defs.JUSTIFICANTE_REGISTRO);
			   	XmlDocument xmlDocReg = new XmlDocument();
			   	xmlDocReg.createFromStringText(justificante);
			   	String xml_w_xsl = XmlTransformer.transformXmlDocumentToHtmlStringTextUsingXslFile(xmlDocReg, xslPath);
			   	session.setAttribute(Defs.INFORMACION_REGISTRO, xml_w_xsl);

	    		return mapping.findForward("refrescar");
	    	}
	    }
	    catch(Exception e) {

	    	session.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
	    	session.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());

	    	return mapping.findForward("failure");
   		}

	    return mapping.findForward("getJustificante");
   	}

}