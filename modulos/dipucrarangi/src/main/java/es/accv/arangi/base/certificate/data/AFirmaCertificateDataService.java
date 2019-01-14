/**
 * LICENCIA LGPL:
 * 
 * Esta librer�a es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los t�rminos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versi�n 2.1 de 
 * la Licencia, o (a su elecci�n) cualquier versi�n posterior.
 * 
 * Esta librer�a se distribuye con la esperanza de que sea �til, pero SIN 
 * NINGUNA GARANT�A; tampoco las impl�citas garant�as de MERCANTILIDAD o 
 * ADECUACI�N A UN PROP�SITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para m�s detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librer�a; si no es as�, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5� Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnolog�a y Certificaci�n Electr�nica
 */
package es.accv.arangi.base.certificate.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.ServiceException;
import es.accv.arangi.base.exception.certificate.validation.ServiceNotFoundException;
import es.accv.arangi.base.util.Util;

/**
 * Clase que implementa la obtenci�n de datos de certificados mediante llamadas
 * a los servicios web de &#64;Firma (puede verse la documentaci�n en la zona
 * segura de la p�gina web de la ACCV: 
 * <a href="https://www.accv.es:8445/secure_area/descargas/descargas/docsatFirma/bienvenida.htm" target="zonaSegura">
 * https://www.accv.es:8445/secure_area/descargas/descargas/docsatFirma/bienvenida.htm</a>).
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 *
 */
public class AFirmaCertificateDataService implements CertificateDataService{

	/**
	 * URL del servicio web de &#64;Firma en explotaci�n
	 */
	public static final String PRODUCTION_URL = "http://afirma.accv.es/afirmaws/services/ObtenerInfoCertificado";
	
	/**
	 * URL del servicio web de &#64;Firma en test
	 */
	public static final String TEST_URL = "http://preafirma.accv.es/afirmaws/services/ObtenerInfoCertificado";
	
	/*
	 * Nombre de la plantilla para generar llamadas sin securizar
	 */
	private static final String TEMPLATE_WITHOUT_SECURITY = "es/accv/arangi/base/template/arangi-afirma_data_template.xml";

	/*
	 * Nombre de la plantilla para generar llamadas mediante usuario y contrase�a
	 */
	private static final String TEMPLATE_USER_PASSWORD = "es/accv/arangi/base/template/arangi-afirma_data_secure_template.xml";

	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(AFirmaCertificateDataService.class);
	
	/*
	 * URL de acceso a los servicios web de &#64;Firma
	 */
	private URL url;
	
	/*
	 * Identificador de la aplicaci�n en &#64;Firma
	 */
	private String idAplicacion;
	
	/*
	 * Usuario 
	 */
	private String user;
	
	/*
	 * Contrase�a
	 */
	private String password;
	
	//-- Constructores	
	
	/**
	 * Constructor por defecto: si se usa este constructor ser� necesario inicializar
	 * el objeto.
	 */
	public AFirmaCertificateDataService() {
		super();
	}
	
	/**
	 * Constructor en el que pasar la informaci�n necesaria para crear 
	 * este objeto.
	 * 
	 * @param url URL al servico web de &#64;Firma. Los posibles valores se pueden
	 * 	encontrar en los campos est�ticos de esta clase PRODUCTION_URL y
	 *  TEST_URL.
	 * @param idAplicacion ID de su aplicaci�n. Este valor se le entreg� en
	 * 	el momento en que su aplicaci�n fue dada de alta en la plataforma de
	 * 	&#64;Firma.
	 * @param user Nombre de usuario para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contrase�a.
	 * @param password Contrase�a para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contrase�a.
	 */
	public AFirmaCertificateDataService(URL url, String idAplicacion, String user,
			String password) {
		super();
		initialize(url, idAplicacion, user, password);
	}
	
	/**
	 * Inicializa el objeto
	 * 
	 * @param url URL al servico web de &#64;Firma. Los posibles valores se pueden
	 * 	encontrar en los campos est�ticos de esta clase PRODUCTION_URL y
	 *  TEST_URL.
	 * @param idAplicacion ID de su aplicaci�n. Este valor se le entreg� en
	 * 	el momento en que su aplicaci�n fue dada de alta en la plataforma de
	 * 	&#64;Firma.
	 * @param user Nombre de usuario para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contrase�a.
	 * @param password Contrase�a para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contrase�a.
	 */
	public void initialize (URL url, String idAplicacion, String user, String password) {
		this.url = url;
		this.idAplicacion = idAplicacion;
		this.user = user;
		this.password = password;
	}

	/**
	 * Obtiene los datos de un certificado mediante una llamada a un servicio externo.
	 * 
	 * @param certificate Certificado 
	 * @param extraParams Par�metros extra por si fueran necesarios para 
	 * 	realizar la obtenci�n
	 * @return Map con los valores obtenidos del certificado
	 * @throws ServiceNotFoundException El servicio no se encuentra disponible
	 * @throws ServiceException La llamada al servicio devuelve un error
	 */
	public Map<String,String> getData (Certificate certificate, 
			Map<String,Object> extraParams) throws ServiceNotFoundException, ServiceException {
		
		logger.debug("[AFirmaCertificateDataService.getData]::Entrada::" + Arrays.asList(new Object[] { certificate, extraParams }));
		
		//-- Obtener el template
		InputStream isTemplate;
		if (this.user != null && this.password != null) {
			isTemplate = new Util().getClass().getClassLoader().getResourceAsStream(TEMPLATE_USER_PASSWORD);
		} else {
			isTemplate = new Util().getClass().getClassLoader().getResourceAsStream(TEMPLATE_WITHOUT_SECURITY);
		}

		//-- Obtener los par�metros
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (this.user != null && this.password != null) {
			parameters.put("user", this.user);
			parameters.put("password", this.password);
		} 
		try {
			parameters.put("certificate", Util.encodeBase64(certificate.toDER()));
		} catch (NormalizeCertificateException e) {
			//-- El certificado ya se normaliz� al entrar, no se dar� el error
			logger.info ("[AFirmaCertificateDataService.getData]", e);
		}
		parameters.put("idAplicacion", this.idAplicacion);

		//-- Obtener el mensaje
		String message;
		try {
			message = Util.fillTemplate(isTemplate, parameters);
		} catch (IOException e) {
			logger.info ("[AFirmaCertificateDataService.getData]::Error construyendo el mensaje", e);
			throw new ServiceException("Error construyendo el mensaje", e);
		}
		logger.debug("[AFirmaCertificateDataService.getData]::Se ha obtenido el mensaje a enviar a @Firma: " + message);
		
		//-- Enviar el mensaje
		StringBuffer respuesta = Util.sendPost(message, url);
		logger.debug("[AFirmaCertificateDataService.getData]::Se ha obtenido la respuesta de @Firma: " + respuesta);

		//-- Variables para obtener el resultado
		Map<String, String> campos = null;
		
		//-- Comprobar si es mensaje de error
		if (respuesta.indexOf("codigoError") > -1) {
				logger.info("[AFirmaCertificateValidationService.validate]::La respuesta de @Firma es de error");
				throw new ServiceException(respuesta.substring(respuesta.indexOf("codigoError&gt;") + 15, respuesta.indexOf("&lt;/codigoError")) + " - " +
						respuesta.substring(respuesta.indexOf("descripcion&gt;") + 15, respuesta.indexOf("&lt;/descripcion")));
		}
		
		//-- Mensaje OK, extraer la informaci�n
		logger.debug("[AFirmaCertificateDataService.getData]::La respuesta de @Firma no es de error");
		String xml = respuesta.substring(respuesta.indexOf("&lt;?xml"), respuesta.indexOf("</ObtenerInfoCertificadoReturn>"));
		xml = xml.replaceAll("\\&lt;", "<").replaceAll("\\&gt;", ">");
		
		Document doc;
		XPath xpath;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));
			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
		} catch (Exception e) {
			logger.info("[AFirmaCertificateDataService.getData]::Error obteniendo el documento DOM o el XPath", e);
			throw new ServiceException("Error obteniendo el documento DOM o el XPath", e);
		}

		//-- Obtener los campos
		try {
			campos = new HashMap<String, String>();
			XPathExpression expr = xpath.compile("//*[local-name()='ResultadoProcesamiento']/*[local-name()='InfoCertificado']/*[local-name()='Campo']");
			NodeList fieldNodes = (NodeList) expr.evaluate (doc, XPathConstants.NODESET);
			for (int i=0;i<fieldNodes.getLength();i++) {
				Node fieldNode = fieldNodes.item(i);
				String nombre = fieldNode.getChildNodes().item(0).getTextContent();
				String valor = fieldNode.getChildNodes().item(1).getTextContent();
				campos.put(nombre, valor);
			}
			logger.debug("[AFirmaCertificateDataService.getData]::Se han obtenido " + campos.size() + " campos");

		} catch (Exception e) {
			logger.info("[AFirmaCertificateDataService.getData]::Error obteniendo los campos del XML de respuesta", e);
		}
		
		return campos;
	}
	
	/**
	 * Obtiene la URL de los servicios web de &#64;Firma en producci�n
	 * 
	 * @return URL
	 */
	public static URL getProductionURL () {
		try {
			return new URL(PRODUCTION_URL);
		} catch (MalformedURLException e) {
			// Siempore estar� bien formada
			return null;
		}
	}

	/**
	 * Obtiene la URL de los servicios web de &#64;Firma en test
	 * 
	 * @return URL
	 */
	public static URL getTestURL () {
		try {
			return new URL(TEST_URL);
		} catch (MalformedURLException e) {
			// Siempore estar� bien formada
			return null;
		}
	}


}
