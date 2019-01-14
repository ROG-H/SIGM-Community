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
package es.accv.arangi.base.signature;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;

import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.ValidateCertificate;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.AlgorithmNotSuitableException;
import es.accv.arangi.base.exception.signature.InvalidCertificateException;
import es.accv.arangi.base.exception.signature.PDFDocumentException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase para manejar firmas en PDF seg�n la norma 
 * <a href="http://www.iso.org/iso/catalogue_detail.htm?csnumber=51502" target="estandar">ISO 32000-1</a><br><br>
 * 
 * Para evitar problemas de saturaci�n de memoria con ficheros PDF muy grandes,
 * esta clase siempre trabajar� sobre un objeto java.io.File. Si el objeto no se 
 * inicializa con un fichero se crear� un archivo temporal en la carpeta temporal 
 * de Arangi: {@link #getArangiTemporalFolder() getArangiTemporalFolder}.
 * 
 * Existen dos m�todos para obtener una firma PDF, dependiendo de si se desea 
 * una firma visible o invisible. En el caso de las firmas visibles hay que proporcionar 
 * al m�todo las coordenadas de las esquinas inferior izquierda y superior derecha, as� 
 * como el n�mero de p�gina donde se desea ubicar la firma. Tambi�n es posible asociar
 * una imagen a la firma.<br><br>
 * 
 * <code>
 * KeyStoreManager manager = new KeyStoreManager (...,...);<br>
 * String alias = ...;<br>
 * ByteArrayDocument documentPDF = new ByteArrayDocument (...);<br>
 * URL urlTSA = new URL (...);<br>
 * CAList caList = new CAList (...);<br><br>
 * 
 * //-- Firma invisible<br>
 * PDFSignature signatureInv = PDFSignature.sign (new KeyStoreManager[] {manager}, new String[] {alias}, documentPDF, urlTSA, caList, "Porque quiero firmarlo");<br><br>
 * 
 * //-- Firma visible<br>
 * PDFSignature signatureVis = PDFSignature.sign (new ACCVDeviceManager[] {manager}, new String[] {alias},documentPDF, urlTSA, caList, "Porque quiero firmarlo",
 *      true, Util.readStream(ClassLoader.getSystemResourceAsStream("signature/chip.gif")), 100, 100, 300, 200, 1);<br><br>
 * </code>
 * 
 * En la p�gina 1 de la segunda firma, en la ubicaci�n indicada por las coordenadas, se 
 * ver� la imagen chip.gif como una firma realizada sobre el PDF. El primer par�metro del
 * m�todo de firma es un array de managers, ya que es posible realizar varias firmas a 
 * la vez.<br><br>
 * 
 * Cabe la posibilidad de crear firmas sin sello de tiempo. Para ello se debe utilizar alguno 
 * de los m�todos a los que no hay que pasar la URL de un servidor de sello de tiempos.<br>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class PDFSignature extends BasePDFSignature {

	/*
	 * Constante para incluir como motivo de la firma cuando �ste no es pasado 
	 * como par�metro
	 */
	private static final String DEFAULT_SIGNATURE_REASON = "Firma realizada con Arangi (ACCV)";

	/*
	 * Logger de la clase 
	 */
	static Logger logger = Logger.getLogger(PDFSignature.class);
	
	//-- Constructores
	
	/**
	 * Inicializa el objeto con un fichero PDF firmado.
	 * 
	 * @param pdfFile Fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser le�do
	 * @throws SignatureNotFoundException El fichero es un PDF pero no est� firmado
	 */
	public PDFSignature (File pdfFile) throws PDFDocumentException, SignatureNotFoundException {
		initialize(pdfFile);
	}
	
	/**
	 * Inicializa el objeto con el contenido de un fichero PDF firmado.
	 * 
	 * @param pdfContentBytes Array de bytes con el contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser le�do
	 * @throws SignatureNotFoundException El fichero es un PDF pero no est� firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PDFSignature (byte[] pdfContentBytes) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
			Util.saveFile(fileTemp, pdfContentBytes);
		} catch (IOException e) {
			logger.info("[PDFSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en �l", e);
			throw e;
		}
		
		initialize(fileTemp);
	}
	
	/**
	 * Inicializa el objeto con un stream de lectura al contenido de un fichero PDF firmado.
	 * 
	 * @param streamPDF Stream de lectura al contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser le�do
	 * @throws SignatureNotFoundException El fichero es un PDF pero no est� firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PDFSignature (InputStream streamPDF) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		FileOutputStream fos = null;
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
            fos = new FileOutputStream(fileTemp);
            int c;
            while ((c = streamPDF.read()) != -1) {
               fos.write(c);
            }
		} catch (IOException e) {
			logger.info("[PDFSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en �l", e);
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		
		//-- Inicializar
		initialize(fileTemp);
	}
	
	/**
	 * Inicializa el objeto con un documento que debe contener un fichero PDF firmado.
	 * 
	 * @param document Documento con el contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser le�do
	 * @throws SignatureNotFoundException El fichero es un PDF pero no est� firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PDFSignature (IDocument document) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		FileOutputStream fos = null;
		InputStream fis = null;
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
			fis = document.getInputStream();
            fos = new FileOutputStream(fileTemp);
            int c;
            while ((c = fis.read()) != -1) {
               fos.write(c);
            }
		} catch (IOException e) {
			logger.info("[PDFSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en �l", e);
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		
		//-- Inicializar
		initialize(fileTemp);
	}
	
	/*
	 * Inicializa el objeto con un documento igual.
	 */
	protected PDFSignature (PDFSignature signature) {
		this.pdfFile = signature.pdfFile;
	}
	
	//-- M�todos est�ticos
	
	/**
	 * Obtiene un objeto {@link PDFSignature PDFSignature} tras firmar un documento PDF.
	 * La firma es invisible.
	 * 
	 * @param managers Dispositivos criptogr�fico que realizar�n la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma. Si no es null el certificado se validar�
	 * 	antes de la firma y, en �sta, se a�adir� la cadena de confianza.
	 * @param reason Texto que aparecer� junto a la firma como raz�n. Si se pasa un valor
	 * 	nulo se escribir� un texto por defecto.
	 * @return Documento PDF firmado, con sello de tiempos
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws HashingException Excepci�n obteniendo el hash que ser� sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptogr�fico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma est� revocado
	 */
	public static PDFSignature sign (DeviceManager managers[], String[] alias, IDocument pdfDocument, URL urlTimestamp, 
			CAList caList, String reason) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, HashingException, CertificateCANotFoundException, InvalidCertificateException {
		try {
			return sign (managers, alias, pdfDocument, null, urlTimestamp, null, null, caList, reason, false, null, -1, -1, -1, -1, 0);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no deber�a provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no deber�a provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PDFSignature PDFSignature} tras firmar un documento PDF.
	 * El servidor de la TSA requiere autenticaci�n y la firma obtenida es invisible.
	 * 
	 * @param managers Dispositivos criptogr�fico que realizar�n la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticaci�n.
	 * @param passwordTSA Contrase�a para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticaci�n.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma. Si no es null el certificado se validar�
	 * 	antes de la firma y, en �sta, se a�adir� la cadena de confianza.
	 * @param reason Texto que aparecer� junto a la firma como raz�n. Si se pasa un valor
	 * 	nulo se escribir� un texto por defecto.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws HashingException Excepci�n obteniendo el hash que ser� sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptogr�fico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma est� revocado
	 */
	public static PDFSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, URL urlTimestamp, 
			String userTSA, String passwordTSA, CAList caList, String reason) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, HashingException, CertificateCANotFoundException, InvalidCertificateException {
		try {
			return sign (managers, alias, pdfDocument, null, urlTimestamp, userTSA, passwordTSA, caList, reason, false, null, -1, -1, -1, -1, 0);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no deber�a provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no deber�a provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PDFSignature PDFSignature} tras firmar un documento PDF.
	 * La firma obtenida es invisible. La firma PDF no incluye sello de tiempos.
	 * 
	 * @param managers Dispositivos criptogr�fico que realizar�n la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma. Si no es null el certificado se validar�
	 * 	antes de la firma y, en �sta, se a�adir� la cadena de confianza.
	 * @param reason Texto que aparecer� junto a la firma como raz�n. Si se pasa un valor
	 * 	nulo se escribir� un texto por defecto.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws HashingException Excepci�n obteniendo el hash que ser� sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptogr�fico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma est� revocado
	 */
	public static PDFSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, 
			CAList caList, String reason) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, HashingException, CertificateCANotFoundException, InvalidCertificateException {
		try{
			return sign (managers, alias, pdfDocument, null, null, null, null, caList, reason, false, null, -1, -1, -1, -1, 0);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no deber�a provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no deber�a provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PDFSignature PDFSignature} tras firmar un documento PDF.<br><br>
	 * 
	 * Si la firma es visible se le puede asociar una imagen. El punto 0,0 de la p�gina 
	 * se encuentra en la esquina inferior izquierda de la misma. Un p�gina tiene 
	 * aproximadamente unas dimensiones de 580x850. <br><br>
	 * 
	 * La firma PDF no incluye sello de tiempos.
	 * 
	 * @param managers Dispositivos criptogr�fico que realizar�n la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma. Si no es null el certificado se validar�
	 * 	antes de la firma y, en �sta, se a�adir� la cadena de confianza.
	 * @param reason Texto que aparecer� junto a la firma como raz�n. Si se pasa un valor
	 * 	nulo se escribir� un texto por defecto.
	 * @param isVisible Si tiene un valor cierto se crear� una firma visible.
	 * @param image Imagen de la firma. Puede tener un valor nulo.
	 * @param llX Posici�n X de la esquina inferior izquierda de la firma en la p�gina (caso de ser visible)
	 * @param llY Posici�n Y de la esquina inferior izquierda de la firma en la p�gina (caso de ser visible) 
	 * @param urX Posici�n X de la esquina superior derecha de la firma en la p�gina (caso de ser visible)
	 * @param urY Posici�n Y de la esquina superior derecha de la firma en la p�gina (caso de ser visible)
	 * @param page P�gina en la que se situar� la firma si �sta es visible (1 es la primera p�gina)
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws HashingException Excepci�n obteniendo el hash que ser� sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptogr�fico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma est� revocado
	 */
	public static PDFSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, 
			CAList caList, String reason, boolean isVisible, byte[] image, float llX, float llY, float urX, 
			float urY, int page) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, HashingException, CertificateCANotFoundException, InvalidCertificateException {
		
		try {
			return sign(managers, alias, pdfDocument, null, null, null, null, caList, reason, isVisible, image, llX, llY, urX, urY, page);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no deber�a provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no deber�a provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PDFSignature PDFSignature} tras firmar un documento PDF.<br><br>
	 * 
	 * Si la firma es visible se le puede asociar una imagen. El punto 0,0 de la p�gina 
	 * se encuentra en la esquina inferior izquierda de la misma. Un p�gina tiene 
	 * aproximadamente unas dimensiones de 580x850. 
	 * 
	 * @param managers Dispositivos criptogr�fico que realizar�n la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
     * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticaci�n.
	 * @param passwordTSA Contrase�a para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticaci�n.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma. Si no es null el certificado se validar�
	 * 	antes de la firma y, en �sta, se a�adir� la cadena de confianza.
	 * @param reason Texto que aparecer� junto a la firma como raz�n. Si se pasa un valor
	 * 	nulo se escribir� un texto por defecto.
	 * @param isVisible Si tiene un valor cierto se crear� una firma visible.
	 * @param image Imagen de la firma. Puede tener un valor nulo.
	 * @param llX Posici�n X de la esquina inferior izquierda de la firma en la p�gina (caso de ser visible)
	 * @param llY Posici�n Y de la esquina inferior izquierda de la firma en la p�gina (caso de ser visible) 
	 * @param urX Posici�n X de la esquina superior derecha de la firma en la p�gina (caso de ser visible)
	 * @param urY Posici�n Y de la esquina superior derecha de la firma en la p�gina (caso de ser visible)
	 * @param page P�gina en la que se situar� la firma si �sta es visible (1 es la primera p�gina)
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws HashingException Excepci�n obteniendo el hash que ser� sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptogr�fico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma est� revocado
	 * @throws AlgorithmNotSuitableException El algoritmo de firma pasado no sirve para realizar la firma
	 */
	public static PDFSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, 
			String digitalSignatureAlgorithm, URL urlTimestamp, String userTSA, String passwordTSA, 
			CAList caList, String reason, boolean isVisible, byte[] image, 
			float llX, float llY, float urX, float urY, int page) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, HashingException, CertificateCANotFoundException, InvalidCertificateException, AlgorithmNotSuitableException {
		
		logger.debug("[PDFSignature.sign]::Entrada::" + Arrays.asList (new Object [] { managers, alias, pdfDocument, digitalSignatureAlgorithm, urlTimestamp, userTSA, passwordTSA, caList, reason, new Boolean (isVisible), image, new Float (llX), new Float (llY), new Float (urX), new Float (urY), new Integer (page) }));
		
		FileOutputStream fos = null;
		try {
		
			//-- Comprobar par�metros
			if (reason == null) {
				reason = DEFAULT_SIGNATURE_REASON;
			}
			
			//-- Cliente TSA (de iText)
			TSAClient tsc = null;
			if (urlTimestamp != null) {
				tsc = new TSAClientBouncyCastle(urlTimestamp.toString(), userTSA, passwordTSA);
			}
			
			//-- Obtener el fichero temporal donde se dejar� el resultado
			File fileResult1 = getFileTemp();
			Util.saveFile(fileResult1, pdfDocument.getInputStream());
			
			for (int i = 0; i < managers.length; i++) {
				
				//-- Obtener la clave privada
				PrivateKey pk = managers[i].getPrivateKey(alias[i]);
				if (pk == null) {
					logger.info("[PDFSignature.sign]::No se ha podido encontrar el alias o bien �ste no contiene una clave privada");
					throw new AliasNotFoundException ("No se ha podido encontrar el alias o bien �ste no contiene una clave privada");
				}
				
				//--Obtener el certificado y validarlo si el caList no es nulo
				X509Certificate x509Certificate = managers[i].getCertificate(alias[i]);
				ValidateCertificate validateCertificate = null;
				java.security.cert.Certificate[] chain;
				if (caList != null) {
					validateCertificate = new ValidateCertificate (x509Certificate, caList);
					int validationResult;
					if ((validationResult = validateCertificate.validate()) != ValidationResult.RESULT_VALID) {
						logger.info("[PDFSignature.sign]::El certificado de firma no es v�lido: " + validationResult);
						throw new InvalidCertificateException ("El certificado de firma no es v�lido: " + validationResult, validationResult);
					}
					chain = new java.security.cert.Certificate[] { x509Certificate, validateCertificate.getIssuerCertificate().toX509Certificate() };
				} else {
					chain = new java.security.cert.Certificate[] { x509Certificate };
				}
				
				//-- Leer el PDF y crear la firma. Dependiendo de si el PDF ya
				//-- est� firmado o no se crea el PdfStamper de una manera u otra
				PdfReader reader = new PdfReader(new FileInputStream(fileResult1));
				File fileResult2 = getFileTemp();
				fos = new FileOutputStream (fileResult2);
				AcroFields af = reader.getAcroFields();
				ArrayList names = af.getSignatureNames();
				PdfStamper stp;
				if (names == null || names.isEmpty()) {
					stp = PdfStamper.createSignature(reader, fos, '\0');
				} else {
					stp = PdfStamper.createSignature(reader, fos, '\0', null, true);
				}
				
				//-- Apariencia de la firma
				PdfSignatureAppearance sap = stp.getSignatureAppearance();
				sap.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
				sap.setReason(reason);
				if (isVisible) {
					sap.setVisibleSignature(new Rectangle(llX, llY, urX, urY), page, null);
					StringBuffer detail = new StringBuffer();
					if (validateCertificate != null) {
						detail.append(DIGITALLY_SIGNED_TEXT + ": " + validateCertificate.getCommonName() + "\n");
					}
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					detail.append("Fecha: " + sdf.format(new Date()) + "\n");
					if (reason != null && !reason.equals("")) {
						detail.append("Motivo: " + reason + "\n");
					}
					sap.setLayer2Text(detail.toString());
					if (image != null) {
						sap.setImage(Image.getInstance(image));
					}
				}
		
				//-- Cargar valores en el diccionario
				PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
				dic.setReason(sap.getReason());
				dic.setLocation(sap.getLocation());
				dic.setContact(sap.getContact());
				dic.setDate(new PdfDate(sap.getSignDate()));
				sap.setCryptoDictionary(dic);
				sap.setExternalDigest(new byte[128], null, "RSA"); 
		
				int contentEstimated = 15000;
				HashMap exc = new HashMap();
				exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
				sap.preClose(exc);
				
				//-- Algoritmos
				if (digitalSignatureAlgorithm == null) {
					digitalSignatureAlgorithm = DigitalSignatureAlgorithm.getDefault();
				}
				String hashingAlgorithm = DigitalSignatureAlgorithm.getHashingAlgorithm(digitalSignatureAlgorithm);
				if (hashingAlgorithm == null) {
					logger.info("No se puede tratar el algoritmo de firma " + digitalSignatureAlgorithm);
					throw new AlgorithmNotSuitableException("No se puede tratar el algoritmo de firma " + digitalSignatureAlgorithm);
				}
		
				//-- Construir el PKCS#7 (firmando con el manager de Arangi)
				Calendar cal = Calendar.getInstance();
				PdfPKCS7 pkcs7 = new PdfPKCS7(null, chain, null, hashingAlgorithm, CRYPTOGRAPHIC_PROVIDER_NAME, false);
				byte hash[] = new InputStreamDocument (sap.getRangeStream()).getHash(hashingAlgorithm);
				byte sh[] = pkcs7.getAuthenticatedAttributeBytes(hash, cal, null);
				byte[] signatureBytes = managers[i].signDocument(new ByteArrayInputStream (sh), alias[i], digitalSignatureAlgorithm);
				pkcs7.setExternalDigest(signatureBytes, sh, "RSA"); 
				byte[] encodedSig = pkcs7.getEncodedPKCS7(hash, cal, tsc, hashingAlgorithm.getBytes());
				
				//-- A�adir la firma al diccionario
				if (contentEstimated + 2 < encodedSig.length) {
					logger.info("[PDFSignature.sign]::No se puede a�adir la firma al PDF por falta de espacio en el mismo");
					throw new SignatureException ("No se puede a�adir la firma al PDF por falta de espacio en el mismo");
				}
		
				byte[] paddedSig = new byte[contentEstimated];
				System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
		
				PdfDictionary dic2 = new PdfDictionary();
				dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
				sap.close(dic2);
				
	            reader.close();
	            fos.close();
	            fos = null;
	            
	            fileResult1.delete();
	            fileResult1 = fileResult2;
			}
			
			try {
				return new PDFSignature (fileResult1);
			} catch (SignatureNotFoundException e) {
				//-- No se va a dar, el fichero se acaba de firmar
				return null;
			} catch (PDFDocumentException e) {
				//-- No se va a dar, el fichero se acaba de firmar
				return null;
			}
			
		} catch (LoadingObjectException e) {
			logger.info("[PDFSignature.sign]::No se ha podido obtener la clave privada para firma", e);
			throw new LoadingObjectException ("No se ha podido obtener la clave privada para firma", e);
		} catch (SearchingException e) {
			logger.info("[PDFSignature.sign]::No se ha podido encontrar la clave privada para firma", e);
			throw new LoadingObjectException ("No se ha podido encontrar la clave privada para firma", e);
		} catch (IOException e) {
			logger.info("[PDFSignature.sign]::No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
		} catch (DocumentException e) {
			logger.info("[PDFSignature.sign]::No se ha podido manejar alguna de las partes del fichero PDF", e);
			throw new PDFDocumentException ("No se ha podido manejar alguna de las partes del fichero PDF", e);
		} catch (InvalidKeyException e) {
			logger.info("[PDFSignature.sign]::La clave privada no es v�lida para realizar la firma", e);
			throw new SignatureException ("La clave privada no es v�lida para realizar la firma", e);
		} catch (NoSuchProviderException e) {
			logger.info("[PDFSignature.sign]::No se ha podido obtener el proveedor criptogr�fico", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			logger.info("[PDFSignature.sign]::No existen en el proveedor criptogr�fico los algoritmos de firma (SHA1WithRSA)", e);
			throw new SignatureException ("No existen en el proveedor criptogr�fico los algoritmos de firma (SHA1WithRSA)", e);
		} catch (CertificateCANotFoundException e) {
			logger.info("[PDFSignature.sign]::La lista de certificado de CA no contiene el emisor del certificado de firma", e);
			throw new CertificateCANotFoundException ("La lista de certificado de CA no contiene el emisor del certificado de firma", e);
		} catch (NormalizeCertificateException e) {
			logger.info("[PDFSignature.sign]::El certificado de la CA no es normalizable seg�n el proveedor criptogr�fico de Arangi", e);
			throw new CertificateCANotFoundException ("El certificado de la CA no es normalizable seg�n el proveedor criptogr�fico de Arangi", e);
		} 

	}
	
	//-- M�todos p�blicos
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(es.accv.arangi.base.certificate.validation.CAList)
	 */
	public ValidationResult[] isValid(CAList caList) throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PDFSignature.isValid]::Entrada::" + caList);
		
		return isValidCommon(caList, null);
	}

	/**
	 * En el caso de los PDF no tiene sentido realizar la validaci�n sobre un 
	 * documento que no sea el mismo PDF. Por ello, el resultado de este m�todo 
	 * ser� igual a llamar a {@link #isValid(CAList) isValid} con s�lo un par�metro. 
	 */
	public ValidationResult[] isValid(IDocument document, CAList caList)
			throws HashingException, SignatureException,
			NormalizeCertificateException {
		return isValid (caList);
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(java.util.List(es.accv.arangi.base.certificate.validation.service.CertificateValidationService))
	 */
	public ValidationResult[] isValid(List<CertificateValidationService> validationServices)
			throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PDFSignature.isValid]::Entrada::" + validationServices);
		
		return isValidCommon(null, validationServices);
		
	}

	/**
	 * En el caso de los PDF no tiene sentido realizar la validaci�n sobre un 
	 * documento que no sea el mismo PDF. Por ello, el resultado de este m�todo 
	 * ser� igual a llamar a {@link #isValid(List) isValid} con s�lo un par�metro. 
	 */
	public ValidationResult[] isValid(IDocument document, List<CertificateValidationService> validationServices)
			throws HashingException, SignatureException, NormalizeCertificateException {
		return isValid(validationServices);
	}

	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return "PDF";
	}
	
	/**
	 * Obtiene las fechas de las firmas. Las obtiene de los sellos de 
	 * tiempo o, si �stos no existen, de la fecha indicada en la misma
	 * firma.
	 * 
	 * @return Fechas de firmado
	 */
	public Date[] getSigningTimes () {
		logger.debug("[PDFSignature.getSigningTimes]::Entrada");
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pas� la validaci�n
			logger.info("[PDFSignature.getSigningTimes]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		
		AcroFields af = reader.getAcroFields();
		ArrayList names = getRealSignatureNames(af);
		Date[] dates = new Date[names.size()];
		for (int i = 0; i < names.size(); i++) {
			String name = (String)names.get(i);
			
			//-- Validar que el PKCS#7 se corresponde con el documento
			PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);

			//-- Comprobar que existe el sello de tiempos (no es obligatorio)
			if (pkcs7.getTimeStampDate() != null) {
				//-- hay sello de tiempos
				logger.debug("[PDFSignature.getSigningTimes]::" + i + ": Obteniendo fecha de sello de tiempos");
				dates[i] = pkcs7.getTimeStampDate().getTime();
			} else {
				//-- no hay sello de tiempos
				logger.debug("[PDFSignature.getSigningTimes]::" + i + ": Obteniendo fecha de la firma");
				dates[i] = pkcs7.getSignDate().getTime();
			}
		}

		logger.debug("[PDFSignature.getSigningTimes]:: Devolviendo " + dates.length + " fechas de la firma");
		return dates;		
	}
	
	//-- M�todos privados
	
	private void initialize (File pdfFile) throws PDFDocumentException, SignatureNotFoundException {
		logger.debug("[PDFSignature.initialize]::Entrada::" + pdfFile);
		
		//-- Comprobar que el fichero es un PDF firmado
		try {
			PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
			AcroFields af = reader.getAcroFields();
			ArrayList names = getRealSignatureNames(af);
			if (names == null || names.isEmpty()) {
				logger.info("[PDFSignature.initialize]::El documento PDF no est� firmado");
				throw new SignatureNotFoundException ("El documento PDF no est� firmado");
			}
			for (Iterator iterator = names.iterator(); iterator.hasNext();) {
				String name = (String) iterator.next();
				PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
			}
		} catch (IOException e) {
			logger.info("[PDFSignature.initialize]::No se ha podido leer el fichero PDF", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF", e);
		} 
		
		//-- Todo correcto, se inicializa
		this.pdfFile = pdfFile;

	}
	
	/*
	 * M�todo que realiza la validaci�n, tanto si se trata de validar con caList como
	 * con servicios de validaci�n
	 */
	private ValidationResult[] isValidCommon(CAList caList, List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PDFSignature.isValidCommon]::Entrada::" + caList);
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pas� la validaci�n
			logger.info("[PDFSignature.isValidCommon]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		AcroFields af = reader.getAcroFields();
		List<String> names = getRealSignatureNames(af);
		List<ValidationResult> results = new ArrayList<ValidationResult>(); 
		for (int i = 0; i < names.size(); i++) {
			String name = (String)names.get(i);
			
			//-- Validar que el PKCS#7 se corresponde con el documento
			PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
			Date fechaFirma = pkcs7.getSignDate().getTime();

			//-- Comprobar que existe el sello de tiempos (no es obligatorio)
			logger.debug("[PDFSignature.isValidCommon]::" + i + ": Obteniendo fecha de sello de tiempos");
			TimeStamp ts = null;
			if (pkcs7.getTimeStampDate() != null) {
				logger.debug("[PDFSignature.isValidCommon]::" + i + ": La firma no contiene sello de tiempos");
			
				try {
					//-- Obtener el sello de tiempos
					ts = new TimeStamp (pkcs7.getTimeStampToken().getEncoded());
					fechaFirma = ts.getTime();
				} catch (MalformedTimeStampException e) {
					logger.info("[PAdESLTVSignature.isValidCommon]::El sello de tiempos del documento no es correcto", e);
					throw new SignatureException ("El sello de tiempos del documento no es correcto", e);
				} catch (IOException e) {
					logger.info("[PAdESLTVSignature.isValidCommon]::Error de entrada/salida obteniendo el sello de tiempos", e);
					throw new SignatureException ("Error de entrada/salida obteniendo el sello de tiempos", e);
				}
			}
			
			//-- Validar la firma
			ValidationResult validationResult;
			try {
				if (pkcs7.verify()) {
					validationResult = new ValidationResult (pkcs7.getSigningCertificate(), fechaFirma, ts, null);
				} else {
					validationResult = new ValidationResult (ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA, pkcs7.getSigningCertificate(), fechaFirma, ts, null);
				}
			} catch (java.security.SignatureException e) {
				logger.info("[PDFSignature.isValidCommon]::Error en una de las firmas del PDF", e);
				throw new SignatureException ("Error en una de las firmas del PDF", e);
			}
			
			if (validationResult.isValid()) {
				//-- Validar el sello de tiempos (si existe)
				if (ts != null) {
					try {
						ts = new TimeStamp (pkcs7.getTimeStampToken().getEncoded());
						if (!ts.isValid()) {
							validationResult.setResult(ValidationResult.RESULT_INVALID_TIMESTAMP);
							logger.debug("[PDFSignature.isValidCommon]::" + i + ": Resultado validaci�n: El sello de tiempos no es v�lido");
						} else {
							//-- Si se ha definido un hueco para firmar pero no se ha rellenado llegamos hasta aqu� pero
							//-- no hay certificado de firma
							Certificate signingCertificate = getSigningCertificate(pkcs7.getCertificates());
							if (signingCertificate == null) {
								logger.info("Hay un hueco sin firma con el nombre '" + name + "'");
								continue;
							}

							//-- Obtener el certificado y validarlo cuando indica el sello de tiempos (siempre que no haya caducado)
							if (caList != null) {
								logger.debug("[PDFSignature.isValidCommon]::Se valida mediante CAList");
								ValidateCertificate certificate = new ValidateCertificate (signingCertificate.toDER(), caList);
								if (certificate.isActive()) {
									validationResult.setResult(certificate.validate(pkcs7.getTimeStampDate().getTime()));
								} else {
									validationResult.setResult(certificate.validate());
								}
							} else {
								logger.debug("[PDFSignature.isValidCommon]::Se valida mediante servicios de validaci�n");
								if (signingCertificate.isActive()) {
									validationResult.setResult(signingCertificate.validate(validationServices, pkcs7.getTimeStampDate().getTime()).getResult());
								} else {
									validationResult.setResult(signingCertificate.validate(validationServices).getResult());
								}
							}
							results.add(validationResult);
							logger.debug("[PDFSignature.isValidCommon]::" + i + ": Resultado validaci�n: " + validationResult.getResult());
						}
						
					} catch (CertificateCANotFoundException e) {
						logger.info("[PDFSignature.isValidCommon]::Certificado" + i + " no pertenece a ninguna de las CAs ");
						results.add(new ValidationResult(ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID, pkcs7.getSigningCertificate(), null, ts, null));
						continue;
					} catch (MalformedTimeStampException e) {
						logger.info("[PAdESLTVSignature.isValidCommon]::El sello de tiempos del documento no es correcto", e);
						throw new SignatureException ("El sello de tiempos del documento no es correcto", e);
					} catch (IOException e) {
						logger.info("[PAdESLTVSignature.isValidCommon]::Error de entrada/salida obteniendo el sello de tiempos", e);
						throw new SignatureException ("Error de entrada/salida obteniendo el sello de tiempos", e);
					}
				} else {
					results.add(validationResult);
					logger.debug("[PDFSignature.isValidCommon]::" + i + ": Resultado validaci�n si sello de tiempos: " + validationResult.getResult());
				}
			}

		}
		
		reader.close();
		
		return results.toArray(new ValidationResult[0]);
	}

}
