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
package es.accv.arangi.base.device.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.accv.arangi.base.util.Util;

/**
 * Encapsula unos datos, la representaci�n cifrada de estos y la llave de sesi�n utilizada 
 * durante el proceso de cifrado. Es posible acceder a los distintos elementos por
 * separado, aunque lo m�s habitual ser� usar los objetos de esta clase como soporte de la
 * informaci�n cifrada para ser descifrada con el m�todo decryptWithSessionKey de los 
 * {@link es.accv.arangi.base.device.DeviceManager DeviceManager}. <br>
 * 
 * Permite la exportaci�n a formato XML. <br>
 * 
 * Ejemplo de uso <br>
 * <code>
 * //Creamos el resultado<br>
 * KeyStoreManager manager = ...;<br>
 * byte[] document = ...;<br>
 * String alias = ...;<br>
 * EncryptWithSessionKeyResult result = manager.encryptWithSessionKey (document,alias);<br>
 * document = manager.decryptWithSessionKey (result,alias);<br>
 * </code>
 */
public class EncryptWithSessionKeyResult {
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(EncryptWithSessionKeyResult.class);
	
	/*
	 * Objeto documento que contiene el XML creado o cargado
	 */
	private Document document; 

	/*
	 * Nodo raiz del que colgar�n el resto de nodos que introduzcamos
	 */
	private Element raiz; 

   /**
	* Crea un Objeto de tipo EncryptWithSessionKeyResult. <br>
	* 
	* Crea un objeto XML que encapsula los datos que relacionan una operaci�n de encriptaci�n 
	* (llave sesi�n + datos Cifrados + random Data). 
	*
	* @param encryptedDocument Documento cifrado
	* @param sessionKey Llave sim�trica con la que se ha realizado el cifrado
	* @param randomData Datos aleatorios utilizados en el proceso de cifrado
	* @throws XMLDocumentException No se puede generar el XML que guarda la informaci�n. 	
	*/
	public EncryptWithSessionKeyResult (byte[] encryptedDocument, byte[] sessionKey, byte[] randomData) throws XMLDocumentException {
	
		logger.debug ("[EncryptWithSessionKeyResult]::Entrada::" + Arrays.asList(new Object[] { encryptedDocument, sessionKey, randomData }));
		
		Element nodoDatosEncriptados;
		Element nodoLlaveSesion;
		Element nodoRandomData;
		String lStrDatosEncriptados;
		String lStrLlaveSesion;
		String lStrRandomData;

		//-- Crear el documento
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{
			builder = dbf.newDocumentBuilder();
		}catch (ParserConfigurationException e){
			logger.info ("[EncryptWithSessionKeyResult]::No se puede obtener un builder para generar el XML", e);
			throw new XMLDocumentException("No se puede obtener un builder para generar el XML", e);
		}
		document = builder.newDocument();
		
		//-- Crear el nodo raiz 
		raiz=document.createElement ("MENSAJE_CIFRADO");
		document.appendChild(raiz);

		//-- Crear subnodo para los datos cifrados
		nodoDatosEncriptados=document.createElement ("DATOS_CIFRADOS");
		raiz.appendChild(nodoDatosEncriptados);
		lStrDatosEncriptados = Util.encodeBase64(encryptedDocument);
		nodoDatosEncriptados.setAttribute("CUERPO",lStrDatosEncriptados);

		//-- Crear subnodo para la llave de sesi�n
		nodoLlaveSesion=document.createElement ("LLAVE_SESION");
		raiz.appendChild(nodoLlaveSesion);
		lStrLlaveSesion = Util.encodeBase64(sessionKey);
		nodoLlaveSesion.setAttribute("CUERPO",lStrLlaveSesion);
		
		//-- Crear subnodo para random data
		nodoRandomData=document.createElement ("RANDOM_DATA");
		raiz.appendChild(nodoRandomData);
		lStrRandomData = Util.encodeBase64(randomData);
		nodoRandomData.setAttribute("CUERPO",lStrRandomData);
		
		logger.debug ("[EncryptWithSessionKeyResult]::Creado XML");
		
	} 
	
   /**
	* Crea un Objeto de tipo EncryptWithSessionKeyResult a partir de los bytes que en su
	* momento se obtuvieron con el m�todo {@link #getBytes() getBytes}.
	* 
	* @param result Array de bytes con el objeto a crear.
	* @throws XMLDocumentException No se puede generar el XML que guarda la informaci�n
	*/
	public EncryptWithSessionKeyResult (byte[] result) throws XMLDocumentException{
		
		logger.debug ("[EncryptWithSessionKeyResult(byte[])]::Entrada::" + Arrays.asList(new Object[] { result }));
		
		//-- Crear el documento
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{
			builder = dbf.newDocumentBuilder();
		}catch (ParserConfigurationException e){
			logger.info ("[EncryptWithSessionKeyResult(byte[])]::No se puede obtener un builder para generar el XML", e);
			throw new XMLDocumentException("No se puede obtener un builder para generar el XML", e);
		}
		document = builder.newDocument();
		
		//-- Cargar el XML a partir del par�metro
		try {
			document = builder.parse(new ByteArrayInputStream(result));
			logger.debug ("[EncryptWithSessionKeyResult(byte[])]::Se ha parseado el XML correctamente");
		} catch (SAXException e) {
			logger.info ("[EncryptWithSessionKeyResult(byte[])]::No se pueden parsear los datos pasados como par�metro para generar un XML ", e);
			throw new XMLDocumentException("No se pueden parsear los datos pasados como par�metro para generar un XML ", e);
		} catch (IOException e) {
			logger.info ("[EncryptWithSessionKeyResult(byte[])]::Error de entrada/salida parseando los datos pasados como par�metro", e);
			throw new XMLDocumentException("Error de entrada/salida parseando los datos pasados como par�metro", e);
		}
	} 
	
	/**
	* Devuelve el XML al que envuelve este objeto en formato array de bytes. Dicho
	* XML contiene la informaci�n cifrada, la clave de sesi�n sim�trica y los
	* datos aleatorios.
	*
	* @return XML en formato array de bytes. 
	* @throws XMLDocumentException Error realizando la transformaci�n del objeto a XML
	*/
	public byte[] getBytes() throws XMLDocumentException {

		logger.debug ("[EncryptWithSessionKeyResult.getBytes]::Entrada");

		//-- Objeto transformer
		TransformerFactory transformerFactory;
		transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			logger.info ("[EncryptWithSessionKeyResult.getBytes]::Error configurando el transformador XML", e);
			throw new XMLDocumentException("Error configurando el transformador XML", e);
		}
		
		//-- Objeto salida
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DOMSource origen = new DOMSource(document);
		StreamResult destino = new StreamResult(baos);
		
		//-- Transformar y devolver
		try {
			transformer.transform(origen, destino);
		} catch (TransformerException e) {
			logger.info ("[EncryptWithSessionKeyResult.getBytes]::Error transformando el objeto a XML", e);
			throw new XMLDocumentException("Error transformando el objeto a XML", e);
		}
		
		logger.debug ("[EncryptWithSessionKeyResult.getBytes]::Se ha realizado la transformaci�n correctamente");
		return baos.toByteArray();
	} 
	
	/**
	* Obtiene la llave de sesi�n almacenada en este objeto.
	*
	* @return La llave de sesi�n como array de Bytes. 
	 * @throws XMLDocumentException Error obteniendo la llave de sesi�n
	*/
	public byte[] getSessionKey() throws XMLDocumentException {
	
		logger.debug ("[EncryptWithSessionKeyResult.getSessionKey]::Entrada");

		try{
			//-- Posicionarse en el nodo LlaveSesion
			NodeList nodos = document.getElementsByTagName("LLAVE_SESION");
			Element elemento=(Element)nodos.item(0);
			
			//-- leer el atributo CUERPO, decodificar el base64 y devolver
			String llaveSesion = elemento.getAttribute("CUERPO");
			logger.debug ("[EncryptWithSessionKeyResult.getSessionKey]::Devolviendo la llave de sesi�n::" + llaveSesion);
			return Util.decodeBase64(llaveSesion);
		}catch (Exception e){
			logger.info ("[EncryptWithSessionKeyResult.getSessionKey]::Error obteniendo la llave de sesi�n", e);
			throw new XMLDocumentException("Error obteniendo la llave de sesi�n", e);
		}
	} 


 	/**
	* Obtiene los datos cifrados almacenados en este objeto.
	*
	* @return Datos cifrados como array de bytes.
 	 * @throws XMLDocumentException Error obteniendo los datos cifrados
	*/
	public byte[] getEncryptedDocument() throws XMLDocumentException {
		
		logger.debug ("[EncryptWithSessionKeyResult.getEncryptedDocument]::Entrada");

		try{
			//-- Posicionarse en el nodo LlaveSesion
			NodeList nodos = document.getElementsByTagName("DATOS_CIFRADOS");
			Element elemento=(Element)nodos.item(0);
			
			//-- leer el atributo CUERPO, decodificar el base64 y devolver
			String llaveSesion = elemento.getAttribute("CUERPO");
			logger.debug ("[EncryptWithSessionKeyResult.getEncryptedDocument]::Devolviendo los datos cifrados::" + llaveSesion);
			return Util.decodeBase64(llaveSesion);

		}catch (Exception e){
			logger.info ("[EncryptWithSessionKeyResult.getEncryptedDocument]::Error obteniendo los datos cifrados", e);
			throw new XMLDocumentException("Error obteniendo los datos cifrados", e);
		}
	} 
	
 	/**
	* Obtiene los datos aleatorios almacenados en este objeto.
	*
	* @return Datos aleatorios como array de bytes.
 	 * @throws XMLDocumentException Error obteniendo los datos aleatorios
	*/
	public byte[] getRandomData() throws XMLDocumentException {
		
		logger.debug ("[EncryptWithSessionKeyResult.getRandomData]::Entrada");

		try{
			//-- Posicionarse en el nodo LlaveSesion
			NodeList nodos = document.getElementsByTagName("RANDOM_DATA");
			Element elemento=(Element)nodos.item(0);
			
			//-- leer el atributo CUERPO, decodificar el base64 y devolver
			String llaveSesion = elemento.getAttribute("CUERPO");
			logger.debug ("[EncryptWithSessionKeyResult.getRandomData]::Devolviendo los datos aleatorios::" + llaveSesion);
			return Util.decodeBase64(llaveSesion);

		}catch (Exception e){
			logger.info ("[EncryptWithSessionKeyResult.getRandomData]::Error obteniendo los datos aleatorios", e);
			throw new XMLDocumentException("Error obteniendo los datos aleatorios", e);
		}
	} 
	

}
