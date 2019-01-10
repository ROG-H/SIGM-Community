package ieci.tecdoc.sgm.consulta_telematico.utils;

import ieci.tecdoc.sgm.base.miscelanea.Goodies;
import ieci.tecdoc.sgm.base.xml.core.XmlDocument;
import ieci.tecdoc.sgm.base.xml.core.XmlElement;
import ieci.tecdoc.sgm.base.xml.core.XmlElements;
import ieci.tecdoc.sgm.consulta_telematico.plugin.ConfigLoader;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.catalogo.OrganoDestinatario;
import ieci.tecdoc.sgm.core.services.catalogo.ServicioCatalogoTramites;
import ieci.tecdoc.sgm.core.services.catalogo.Tramite;
import ieci.tecdoc.sgm.core.services.catalogo.Tramites;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.telematico.Registro;
import ieci.tecdoc.sgm.core.services.telematico.RegistroConsulta;
import ieci.tecdoc.sgm.core.services.telematico.RegistroDocumento;
import ieci.tecdoc.sgm.core.services.telematico.RegistroDocumentos;
import ieci.tecdoc.sgm.core.services.telematico.Registros;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.registro.util.Definiciones;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class);

	public static List obtenerListado(Registros registros, ServicioRegistroTelematico oServicioRT, Entidad entidad) throws Exception {

		ServicioCatalogoTramites oServicioCatalogo = LocalizadorServicios.getServicioCatalogoTramites();

		List listado = new ArrayList();
		if (registros == null || registros.count() == 0)
			return listado;

		for (int i = 0; i < registros.count(); i++) {

			Registro registro = (Registro) registros.get(i);

//				try {
//					// Solicitud de Registro
//					// Obtener la solicitud no firmada ya que siempre existe para todos los tr�mites
//					// y la solicitud firmada s�lo existe en los tr�mites que requieren firma
//					// byte[] solicitud = oServicioRT.obtenerPeticionRegistro(registro.getRegistryNumber(), entidad);
//					byte[] solicitud = oServicioRT.obtenerDocumento(registro.getRegistryNumber(), Definiciones.REGISTRY_REQUEST_NOTSIGNED_CODE, entidad);
//
//					if (solicitud != null) {
//
//						// Obtener los datos del XML de la solicitud
//						XmlDocument xmlDoc = new XmlDocument();
//
//			        	xmlDoc.createFromStringText(Goodies.fromUTF8ToStr(solicitud));
//			        	// Obtener la descripci�n del asunto en la solicitud de registro
//			        	XmlElement rootElement = xmlDoc.getRootElement();
//			        	XmlElement descElement = null;
//
//			        	if (registro.getTopic() == null) {
//			        		// Subsanaci�n
//			        		registro.setTopic("SUBSANACION");
//
//			        		descElement = rootElement.getDescendantElement(Definiciones.XPATH_GENERIC_DATA + "/" +
//			        															  Definiciones.TOPIC + "/" +
//			        															  Definiciones.DESCRIPTION);
//
//			        		registro.setName(descElement.getValue());
//			        	}
//
//			        	XmlElement destino = rootElement.getDescendantElement(Definiciones.XPATH_GENERIC_DATA + "/" +
//								  Definiciones.ADDRESSEE + "/" +
//								  Definiciones.CODE);
//
//		        		if(destino != null) {
//			        		try{
//			        			OrganoDestinatario organo = oServicioCatalogo.getAddressee(destino.getValue(), entidad);
//			        			registro.setAddressee(organo.getDescription());
//			        		}catch (Exception e) {
//			        			logger.warn("No se ha podido obtener el organo destinatario de la solicitud: "+registro.getRegistryNumber());
//							}
//		        		}
//
//			        	descElement = rootElement.getDescendantElement(Definiciones.XPATH_SPECIFIC_DATA + "/" +
//								  Definiciones.IDIOMA);
//
//			        	if(descElement != null) {
//			        		registro.setIdioma(descElement.getValue());
//			        	}
//
//			        	listado.add(registros.get(i));
//					}
//			        else {
//			        	logger.warn("No se ha podido obtener la solicitud del registro: "+registro.getRegistryNumber());
//			        }
//				}
//				catch (Exception e) {
//					logger.error("Error al obtener la solicitud del registro: "+registro.getRegistryNumber(), e);
//				}
			listado.add(registro);
			}

		return listado;
	}

	public static List obtenerListado(RegistroDocumentos registroDocumentos, XmlElement solicitudRegistro) {

		List listado = new ArrayList();
		if (registroDocumentos == null || registroDocumentos.count() == 0)
			return listado;

		// Obtener los documentos de la solicitud de registro
		Map docsMap = new HashMap();

		XmlElement docsElement = solicitudRegistro.getDescendantElement(Definiciones.XPATH_DOCUMENTS);
		XmlElements xmlElements = docsElement.getChildElements();
		for(int i = 1; i <= xmlElements.getCount(); i++) {

			XmlElement docElement = xmlElements.getItem(i);

			String code = docElement.getChildElement(Definiciones.CODE).getValue();
			String name = docElement.getChildElement(Definiciones.NAME).getValue()
						+ " (" + docElement.getChildElement(Definiciones.DESCRIPTION).getValue() + ")";
			String extension = docElement.getChildElement(Definiciones.EXTENSION).getValue();

			Documento doc = new Documento();
			doc.setCode(code);
			doc.setName(name);
			doc.setExtension(extension);

			docsMap.put(code, doc);
		}

		for (int i = 0; i < registroDocumentos.count(); i++) {

			RegistroDocumento regDoc = registroDocumentos.get(i);
			String code = regDoc.getCode();

			Documento doc = (Documento) docsMap.get(code);
			if (doc != null) {

			}
			else {

				doc = new Documento();
				doc.setCode(code);
				doc.setName(code);
				if (code.equals(Definiciones.REGISTRY_RECEIPT_CODE)) {
					doc.setExtension("pdf");
				}
				else {
					doc.setExtension("xml");
				}
			}

			doc.setRegistryNumber(regDoc.getRegistryNumber());
			doc.setGuid(regDoc.getGuid());

			listado.add(doc);
		}

		return listado;
	}

	public static Entidad obtenerEntidad (String idEntidad) {
		Entidad entidad = new Entidad();
		entidad.setIdentificador(idEntidad);
		return entidad;
	}

	public static List obtenerTramites(Tramites tramites) {
		List listado = new ArrayList();
		listado.add(new TramiteInfo());

		if(tramites == null || tramites.count() == 0)
			return listado;

		for (int i=0; i<tramites.count(); i++)
			listado.add(new TramiteInfo((Tramite)tramites.get(i)));

		return listado;
	}

	public static int obtenerOperadorRegistro(String operador) {
		int iOperador;

		if (RegistroConsulta.CONTIENE.equals(operador))
			iOperador = RegistroConsulta.COD_CONTIENE;
		else if (RegistroConsulta.EMPIEZA.equals(operador))
			iOperador = RegistroConsulta.COD_EMPIEZA;
		else if (RegistroConsulta.TERMINA.equals(operador))
			iOperador = RegistroConsulta.COD_TERMINA;
		else if (">".equals(operador))
			iOperador = RegistroConsulta.COD_MAYOR;
		else if ("<".equals(operador))
			iOperador = RegistroConsulta.COD_MENOR;
		else
			iOperador = RegistroConsulta.COD_IGUAL;

		return iOperador;
	}

	/**
	 * Obtener la ruta a la carpeta con los recursos del tr�mite.
	 *
	 * @param rutaInicial Path del servidor.
	 * @param asunto C�digo de tr�mite.
	 * @param aplicacion Aplicaci�n con los tr�mites en el servidor.
	 * @param carpeta Carpeta de tr�mites.
	 * @param idEntidad Identificador de la entidad.
	 *
	 * @return Ruta absoluta a la carpeta con los recursos del tr�mite.
	 */
	public static String obtenerRuta(String rutaInicial, String asunto, String aplicacion, String carpeta, String idEntidad) {

		final String separador = System.getProperty("file.separator");

		SigemConfigFilePathResolver pathResolver = SigemConfigFilePathResolver.getInstance();
		String ruta = null;

		int index2 = asunto.indexOf("##");
		String id = asunto;
		if (index2 != -1) {
			id = asunto.substring(0, index2);
		}

		// Ruta establecida a partir de la carpeta de tr�mites
		StringBuffer rutaCarpetaTramite = new StringBuffer();
		rutaCarpetaTramite.append(carpeta)
						  .append(separador)
						  .append(idEntidad)
						  .append(separador)
						  .append(id)
						  .append(separador);

		// Obtener la ruta a partir de la configuraci�n de la aplicaci�n recibida como par�metro
		Resource directoryConfig = pathResolver.loadResource(rutaCarpetaTramite.toString(), aplicacion);
		if (directoryConfig != null) {
			try {
				ruta = directoryConfig.getFile().getAbsolutePath() + separador;
			} catch (IOException e) {
			}
		}

		if (ruta == null) {
			// Obtener la ruta a partir de la configuraci�n de la propia aplicaci�n
			directoryConfig = pathResolver.loadResource(rutaCarpetaTramite.toString(), ConfigLoader.CONFIG_SUBDIR);
			if (directoryConfig != null) {
				try {
					ruta = directoryConfig.getFile().getAbsolutePath() + separador;
				} catch (IOException e) {
				}
			}
		}

		if (ruta == null) {
			// Obtener la ruta a partir de la ruta inicial del path del servidor
			// Necesario para Websphere, ya que la ruta inicial tomada a partir del servidor es diferente.
			rutaInicial = rutaInicial.replaceAll("ConsRegTelWeb", "RegistroTelWeb");
			int index1 = rutaInicial.lastIndexOf(separador);

			String rutaParcial = rutaInicial.substring(0, index1) + separador;
			if (!StringUtils.isEmpty(aplicacion)) {
				rutaParcial+= aplicacion + separador;
			}

			ruta = rutaParcial + rutaCarpetaTramite.toString();

			// Comprobar si existe la ruta a partir de la ruta inicial del path del servidor
			File directory = new File(ruta);
			if ((directory == null) ||
				(!directory.exists())) {

				logger.info("Los recursos del tramite en '" + rutaCarpetaTramite.toString() +
							"' no se encuentran disponibles en el servidor ni en la configuraci�n de las aplicaciones");
			}
		}

		return ruta;
	}
}