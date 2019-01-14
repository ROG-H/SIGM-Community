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
import java.io.OutputStream;
import java.util.List;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;

/**
 * Interfaz que deben cumplir todos las clases que representan firmas digitales.<br><br>
 * 
 * En la medida de lo posible se ha intentado distinguir entre los bytes de la
 * firma, resultantes de los procesos de hashing m�s encriptado, de las firmas
 * digitales donde se guardan, adem�s de estos bytes de firma, los certificados, 
 * sellos de tiempo y cualquier otra informaci�n que sirva a su validaci�n. Las 
 * clases que implementan esta interfaz representan a los segundos. Los m�todos 
 * de firma de las clases que extienden {@link es.accv.arangi.base.device.DeviceManager DeviceManager} 
 * devuelven s�lo los bytes de la firma.  
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public interface ISignature {

	/**
	 * Devuelve los certificados con los que se ha realizado la firma
	 * 
	 * @return Certificados con los que se ha realizado la firma
	 */
	public Certificate[] getCertificates ();
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached, sin validar los certificados 
	 * de las mismas.<br><br>
	 * 
	 * IMPORTANTE: este m�todo s�lo puede ser utilizado si la firma es attached (el documento
	 * que origin� la firma se incluye en �sta). Si no es as� utilizar este mismo m�todo pero 
	 * pas�ndole el documento que origin� la firma.
	 * 
	 * @return Para cada certificado resultado de validar la firma
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo m�todo pero pas�ndole el documento que origin� la
	 * 	firma
	 */
	public ValidationResult[] isValidSignatureOnly () throws HashingException, SignatureException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached y sus certificados son v�lidos. <br><br>
	 * 
	 * Sobre la validaci�n de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, �ste s�lo ser� valido mientras el certificado no
	 * 	haya caducado. Despu�s, es imposible obtener la informaci�n de revocaci�n por lo
	 * 	que este m�todo devolver� un resultado falso</li>
	 * 	<li>Si la firma incluye informaci�n de revocaci�n (CRLs o respuestas OCSP) �sta 
	 * 	si que se tendr� en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * </ul><br><br>
	 * 
	 * IMPORTANTE: este m�todo s�lo puede ser utilizado si la firma es attached (el documento
	 * que origin� la firma se incluye en �sta). Si no es as� utilizar este mismo m�todo pero 
	 * pas�ndole el documento que origin� la firma.
	 * 
	 * @param caList Lista de certificados de CA admitidos por la aplicaci�n que usa
	 * 	Arangi
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	v�lido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptogr�fico de Arang� o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo m�todo pero pas�ndole el documento que origin� la
	 * 	firma
	 */
	public ValidationResult[] isValid (CAList caList) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached y sus certificados son v�lidos. <br><br>
	 * 
	 * Sobre la validaci�n de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, �ste s�lo ser� valido mientras el certificado no
	 * 	haya caducado. Despu�s, es imposible obtener la informaci�n de revocaci�n por lo
	 * 	que este m�todo devolver� un resultado falso</li>
	 * 	<li>Si la firma incluye informaci�n de revocaci�n (CRLs o respuestas OCSP) �sta 
	 * 	si que se tendr� en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * <li>La validaci�n se llevar� a cabo a trav�s de los servicios de validaci�n
	 * 	pasados como par�metro.</li>
	 * </ul><br><br>
	 * 
	 * IMPORTANTE: este m�todo s�lo puede ser utilizado si la firma es attached (el documento
	 * que origin� la firma se incluye en �sta). Si no es as� utilizar este mismo m�todo pero 
	 * pas�ndole el documento que origin� la firma.
	 * 
	 * @param validationServices Lista de servicios de validaci�n
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	v�lido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptogr�fico de Arang� o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo m�todo pero pas�ndole el documento que origin� la
	 * 	firma
	 */
	public ValidationResult[] isValid (List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas, sin validar los certificados de las mismas.
	 * 
	 * @param document Documento que origin� la firma
	 * @return Para cada certificado resultado de validar la firma
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 */
	public ValidationResult[] isValidSignatureOnly (IDocument document) throws HashingException, SignatureException;
	
	/**
	 * Comprueba que las firmas son correctas y sus certificados son v�lidos.<br><br> 
	 * 
	 * Sobre la validaci�n de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, �ste s�lo ser� valido mientras el certificado no
	 * 	haya caducado. Despu�s, es imposible obtener la informaci�n de revocaci�n por lo
	 * 	que este m�todo devolver� un resultado falso</li>
	 * 	<li>Si la firma incluye informaci�n de revocaci�n (CRLs o respuestas OCSP) �sta 
	 * 	si que se tendr� en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * </ul>
	 * 
	 * @param document Documento que origin� la firma
	 * @param caList Lista de certificados de CA admitidos por la aplicaci�n que usa
	 * 	Arangi
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	v�lido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptogr�fico de Arangi o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public ValidationResult[] isValid (IDocument document, CAList caList) throws HashingException, SignatureException, NormalizeCertificateException;
	
	/**
	 * Comprueba que las firmas son correctas y sus certificados son v�lidos.<br><br> 
	 * 
	 * Sobre la validaci�n de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, �ste s�lo ser� valido mientras el certificado no
	 * 	haya caducado. Despu�s, es imposible obtener la informaci�n de revocaci�n por lo
	 * 	que este m�todo devolver� un resultado falso</li>
	 * 	<li>Si la firma incluye informaci�n de revocaci�n (CRLs o respuestas OCSP) �sta 
	 * 	si que se tendr� en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * <li>La validaci�n se llevar� a cabo a trav�s de los servicios de validaci�n
	 * 	pasados como par�metro.</li>
	 * </ul>
	 * 
	 * @param document Documento que origin� la firma
	 * @param validationServices Lista de servicios de validaci�n
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	v�lido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptogr�fico de Arangi o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public ValidationResult[] isValid (IDocument document, List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException;
	
	/**
	 * Guarda la firma en disco
	 * 
	 * @param file Fichero donde se guardar� la firma
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (File file) throws IOException;
	
	/**
	 * Guarda la firma en un stream de escritura.
	 * 
	 * @param out Stream de escritura
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (OutputStream out) throws IOException;
	
	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType ();
}
