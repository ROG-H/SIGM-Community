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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.FileDocument;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que se encarga de realizar firmas en formato CMS de acuerdo a la
 * <a href="http://tools.ietf.org/rfc/rfc3852.txt" target="rfc">RFC-3852</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class CMSSignature extends CMSPKCS7Signature{

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CMSSignature.class);
	
	/**
	 * OID para CMS
	 */
	public static final String OID_FORMATO_FIRMA = "1.2.840.113549.1.7.2";
	
	/**
	 * Obtiene la firma de un fichero.
	 * 
	 * @param fileSignature Fichero con la firma en formato CMS
	 * @throws IOException Error leyendo el fichero o la firma proporcionada no parece estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptogr�fico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(File fileSignature) throws NormalizeCertificateException, SignatureException, IOException {
		super(fileSignature);
	}

	/**
	 * Obtiene la firma de un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a la firma en formato CMS
	 * @throws IOException Error leyendo el stream de lectura o la firma proporcionada no parece 
	 * 	estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptogr�fico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(InputStream isSignature) throws IOException,
			NormalizeCertificateException, SignatureException {
		super(isSignature);
	}

	/**
	 * Obtiene la firma de un array de bytes.
	 * 
	 * @param signature Firma en formato CMS
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptogr�fico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(byte[] signature) throws NormalizeCertificateException, SignatureException {
		super(signature);
	}

	/**
	 * Construye un firma en formato CMS en base a los bytes de las firmas y
	 * los certificados con los que se realizaron �stas, con el algoritmo de 
	 * firma indicado. El documento se a�adira a la firma (attached).
	 * 
	 * @param signatureBytes Bytes de la firma
	 * @param certificates Certificados con los que se realiz� la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(byte[][] signatureBytes, Certificate[] certificates,
			IDocument document, String[] digitalSignatureAlgorithms) throws SignatureException {
		super(signatureBytes, certificates, document, digitalSignatureAlgorithms);
	}

	/**
	 * Construye un firma en formato CMS en base a los bytes de las firmas y
	 * los certificados con los que se realizaron �stas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA). El documento se a�adira a la firma 
	 * (attached).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realiz� la firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(byte[][] signatureBytes, Certificate[] certificates,
			IDocument document) throws SignatureException {
		super(signatureBytes, certificates, document);
	}

	/**
	 * Construye un firma en formato CMS en base a los bytes de las firmas y
	 * los certificados con los que se realizaron �stas, con los algoritmos de 
	 * firma indicados. 
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realiz� la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(byte[][] signatureBytes, Certificate[] certificates,
			String[] digitalSignatureAlgorithms) throws SignatureException{
		super(signatureBytes, certificates, digitalSignatureAlgorithms);
	}

	/**
	 * Construye una firma en formato CMS en base a los bytes de las firmas y
	 * los certificados con los que se realizaron �stas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realiz� la firma
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSSignature(byte[][] signatureBytes, Certificate[] certificates)
			throws SignatureException {
		super(signatureBytes, certificates);
	}
	
	/*
	 * Constructor protegido de la clase
	 * 
	 * @param signature Firma CMS
	 * @param certificates Certificados con los que se realizaron las firmaa
	 * @param document Documento a firmar
	 */
	protected CMSSignature(byte[] signature, Certificate[] certificates, IDocument document, boolean isAttached) {
		super (signature, certificates, document, isAttached);
	}

	/**
	 * Comprueba que las firmas son correctas en firmas attached, sin validar los certificados 
	 * de las mismas.<br><br>
	 * 
	 * Las firmas CMS producidas por IExplorer tienen la particularidad de que, en un paso
	 * previo a la firma, se realiza una transformaci�n del documento a formato 
	 * <a href="http://www.unicode.org/">Unicode</a>. En este m�todo se probar� la validaci�n
	 * con el documento original y, si este se da por no v�lido, se probar� con el documento
	 * en formato Unicode.<br><br>
	 * 
	 * IMPORTANTE: este m�todo s�lo puede ser utilizado si la firma es attached (el documento
	 * que origin� la firma se incluye en �sta). Si no es as� utilizar este mismo m�todo pero 
	 * pas�ndole el documento que origin� la firma.
	 * 
	 * @return Para cada certificado cierto si la firma es correcta
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo m�todo pero pas�ndole el documento que origin� la
	 * 	firma
	 */
	public ValidationResult[] isValidSignatureOnly () throws HashingException, SignatureException, NoDocumentToSignException {
		if (this.document == null) {
			throw new NoDocumentToSignException ("La firma no es attached por lo que no se puede realizar la " +
					"validaci�n si no se facilita el documento que origin� la firma");
		}
		return isValidSignatureOnly(this.document);
	}
	
	/**
	 * Comprueba que las firmas son correctas, sin validar los certificados de las mismas.<br><br>
	 * 
	 * Las firmas CMS producidas por IExplorer tienen la particularidad de que, en un paso
	 * previo a la firma, se realiza una transformaci�n del documento a formato 
	 * <a href="http://www.unicode.org/">Unicode</a>. En este m�todo se probar� la validaci�n
	 * con el documento original y, si este se da por no v�lido, se probar� con el documento
	 * en formato Unicode.
	 * 
	 * @param document Documento que origin� la firma
	 * @return Para cada certificado cierto si la firma es correcta
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 */
	public ValidationResult[] isValidSignatureOnly(IDocument document) throws HashingException, SignatureException {
		
		logger.debug ("[CMSSignature.isValidSignatureOnly]::Entrada::" + document);
		
		//-- Obtener la validaci�n con el documento original
		ValidationResult[] result = super.isValidSignatureOnly(document);
		
		//-- Comprobar si todas las firmas son v�lidas
		boolean allValid = true;
		for (int i = 0; i < result.length; i++) {
			if (!result [i].isValid()) {
				allValid = false;
				break;
			}
		}
		if (allValid) {
			//-- Todo es v�lido, as� que el documento no estaba en Unicode
			return result;
		}
		
		//-- Obtener el documento en Unicode
		File fileUnicode = null;
		ValidationResult[] result2;
		try {
			fileUnicode = Util.toUnicode(document.getInputStream());
			IDocument unicodeDocument = new FileDocument (fileUnicode);
			result2 = super.isValidSignatureOnly(unicodeDocument);
		} catch (InitDocumentException e) {
			// No se debe dar porque el fichero se cre� en el m�todo toUnicode
			throw new HashingException ("No existe el fichero temporal unicode", e);
		} finally {
			if (fileUnicode != null) {
				fileUnicode.delete();
			}
		}
		
		//-- Juntar los dos arrays
		for (int i = 0; i < result.length; i++) {
			if (!result [i].isValid() && result2[i].isValid()) {
				logger.debug ("[CMSSignature.isValidSignatureOnly]::La firma era v�lida en unicode");
				result[i] = result2[i];
			}
		}
		
		return result;
		
	}

	/**
	 * Obtiene un objeto {@link CMSSignature CMSSignature}. Los arrays que se pasan como
	 * par�metro deben tener el mismo tama�o. Dado un elemento i del array de managers, se 
	 * debe corresponder con el elemento i de los alias.<br><br>
	 * 
	 * La firma se realizar� con el algoritmo SHA1withRSA.
	 * 
	 * @param managers Dispositivos criptogr�ficos
	 * @param alias Alias donde se encuentrann las claves privada dentro de los dispositivos
	 * @param document Documento a firmar
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versi�n en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 * @throws SearchingException No se ha podido encontrar el certificado asociado a la clave 
	 * 	privada
	 */
	public static CMSSignature sign (DeviceManager[] managers, String[] alias, IDocument document, boolean isAttached) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException, SearchingException {
		String[] digitalSignatureAlgorithms = new String[managers.length]; 
		for (int i = 0; i < digitalSignatureAlgorithms.length; i++) {
			digitalSignatureAlgorithms[i] = DigitalSignatureAlgorithm.SHA1_RSA;
		}
		return sign(managers, alias, document, digitalSignatureAlgorithms, isAttached);
	}
	
	/**
	 * Obtiene un objeto {@link CMSSignature CMSSignature}. Los arrays que se pasan como
	 * par�metro deben tener el mismo tama�o. Dado un elemento i del array de managers, se 
	 * debe corresponder con el elemento i de los alias y de los algoritmos de firma
	 * 
	 * @param managers Dispositivos criptogr�ficos
	 * @param alias Alias donde se encuentrann las claves privada dentro de los dispositivos
	 * @param document Documento a firmar
	 * @param digitalSignatureAlgorithms Algoritmos de firma a utilizar
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versi�n en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 * @throws SearchingException No se ha podido encontrar el certificado asociado a la clave 
	 * 	privada
	 */
	public static CMSSignature sign (DeviceManager[] managers, String[] alias, IDocument document, String[] digitalSignatureAlgorithms, boolean isAttached) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException, SearchingException {
		
		logger.debug ("[CMSSignature.sign]::Entrada::" + Arrays.asList(new Object[] { managers, alias, document, digitalSignatureAlgorithms, new Boolean (isAttached) }));
		
		//-- Obtener la lista de bytes de firma y certificados con los que se realizan
		ArrayList alSignatureBytes = new ArrayList ();
		ArrayList alCertificates = new ArrayList ();
		for (int i = 0; i < managers.length; i++) {
			//-- Obtener la firma
			alSignatureBytes.add(managers[i].signDocument(document, alias[i]));
			
			//-- Obtener el certificado
			try {
				alCertificates.add (new Certificate (managers[i].getCertificate(alias[i])));
			} catch (NormalizeCertificateException e) {
				logger.info("[CMSSignature.sign]::El certificado de la firma no ha podido ser normalizado a un formato reconocido " +
						"por el proveedor criptogr�fico de Arangi ", e);
				throw new SignatureException ("El certificado de la firma no ha podido ser normalizado a un formato reconocido por el " +
						"proveedor criptogr�fico de Arangi ", e);
			}
		}
		byte[][] signaturesBytes = (byte[][])alSignatureBytes.toArray(new byte[0][0]);
		Certificate [] certificates = (Certificate[]) alCertificates.toArray (new Certificate[0]);
		
		//-- Obtener la firma CMS
		byte[] cmsSignature;
		if (isAttached) {
			cmsSignature = createPKCS7CMS (signaturesBytes, certificates, document, digitalSignatureAlgorithms, OID_FORMATO_FIRMA);
		} else {
			cmsSignature = createPKCS7CMS (signaturesBytes, certificates, null, digitalSignatureAlgorithms, OID_FORMATO_FIRMA);
		}
		return new CMSSignature (cmsSignature, certificates, document, isAttached);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.CMSPKCS7Signature#getOIDFormatoFirma()
	 */
	protected String getOIDFormatoFirma() {
		return OID_FORMATO_FIRMA;
	}
	
	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return "CMS";
	}
}
