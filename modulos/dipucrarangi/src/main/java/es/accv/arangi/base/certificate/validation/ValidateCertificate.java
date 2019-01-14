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
package es.accv.arangi.base.certificate.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.CertificateFieldException;
import es.accv.arangi.base.exception.certificate.InvalidCRLException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que a�ade la funcionalidad de validaci�n a la clase 
 * {@link es.accv.arangi.base.certificate.Certificate Certificate}.<br><br>
 * 
 * Para realizar la validaci�n se siguen los siguientes pasos:<br>
 * 
 * <ol>
 * 	<li>Si el certificado no est� dentro de su periodo de validez no es v�lido.</li>
 * 	<li>Si el certificado est� autofirmado es v�lido.</li>
 * 	<li>Si el objeto {@link CAList CAList} contiene un fichero de validaci�n XML 
 * 		({@link ValidationXML ValidationXML}) se comprueba si la Autoridad de 
 * 		Certificaci�n (CA) emisora existe en �l. Si es as� se valida contra el OCSP 
 * 		y/o la CRL que se indiquen. De esta forma obtendremos si el certificado es 
 * 		v�lido o no.</li>
 * 	<li>Si el certificado tiene extensi�n Authority Information Access (AIA) y en ella
 * 		hay definido uno o varios OCSP, se realiza la validaci�n contra �l/ellos.</li>
 * 	<li>Si el certificado tiene extensi�n CRL Distribution Point, se realiza la 
 * 		validaci�n contra la CRL en �l definida.</li>
 * 	<li>Si se llega a este paso sin haber podido realizar la validaci�n se devolver�
 * 		un resultado indicando que el certificado no se puede validar.</li>
 * 	<li>Si el certificado ha resultado v�lido, se validar�n los certificados que forman 
 * 		parte de su cadena de confianza siguiendo los pasos anteriores. Si alguno de
 * 		ellos resulta no v�lido se devolver� un resultado indicando que la cadena de
 * 		confianza del certificado no es v�lida.</li>
 * </ol><br><br>
 * 
 * El objeto {@link CAList CAList} con el que se inicializa esta clase es fundamental
 * durante el proceso de validaci�n. Adem�s de la funci�n ya indicada de poder contener
 * un fichero de validaci�n XML, la lista de certificados de CA que contiene permite a
 * esta clase obtener la cadena de confianza de los certificados a validar. De esta forma
 * tambi�n es posible controlar cu�les son las CAs con las que se va a trabajar, no 
 * permitiendo que se validen en el sistema certificados de los que se desconoce su
 * procedencia.<br><br>
 * 
 * Un ejemplo de uso de esta clase ser�a:<br><br>
 * 
 * <code>
 * 	CAList caList = new CAList (new File ("c:/certificates/caCertificates"));<br>
 * 	X509Certificate certificate = Util.getCertificate(new File ("c:/certificates/myCertificate.cer"));<br>
 *  ValidateCertificate validateCertificate = new ValidateCertificate (certificate, caList);<br>
 * 	int result = validateCertificate.validate ();<br>
 * 	System.out.println ("V�lido Hoy?: " + CertificateValidator.getString (result));<br><br>
 * 	
 * 	// validaci�n hist�rica
 * 	SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
 * 	result = validateCertificate.validate (dateFormat.parse ("01/01/2008"));
 * 	System.out.println ("V�lido el 01/01/2008?: " + CertificateValidator.getString (result));<br><br>
 * </code>
 * 
 * La mayor�a de Autoridades de Certificaci�n eliminan la informaci�n de revocaci�n
 * de los certificados caducados de sus CRLs. En estos casos la validaci�n hist�rica no
 * es posible y Arangi devolver� que el certificado era v�lido, ya que tanto la CRL como
 * el OCSP devuelven este resultado. La �nica forma de realizar validaci�n hist�rica ser�a 
 * dentro de un proceso de firma longeva.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class ValidateCertificate extends Certificate {
	
	/*
	 * Class logger
	 */
	private static Logger logger = Logger.getLogger(ValidateCertificate.class);
	
	/*
	 * Certificate that this object envelopes
	 */
	private X509Certificate issuerCertificate;
	
	/*
	 * List of CA certificates
	 */
	CAList caList;

	/*
	 * Validation XML
	 */
	ValidationXML validationXML;
	
	/*
	 * Trace object
	 */
	ValidatingTrace trace = new ValidatingTrace();
	
	/*
	 * OCSP
	 */
	OCSPClient ocsp;

	/*
	 * CRL
	 */
	CRL crl;
	
	//-- Constructor
	
	/**
	 * Constructor en base a un objeto java.security.cert.X509Certificate.
	 * 
	 * @param certificate Certificado X.509 v3
	 * @param caList Lista de certificados de CA
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(X509Certificate certificate, CAList caList) throws CertificateCANotFoundException, NormalizeCertificateException{
		super (certificate);
		initialize (caList, null, null, null);
	}
	
	/**
	 * Constructor en base a un fichero.
	 * 
	 * @param fileCertificate Fichero que contiene el certificado (tal cual o en formato PEM).
	 * @param caList Lista de certificados de CA
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 * @throws FileNotFoundException El fichero no existe
	 */
	public ValidateCertificate(File fileCertificate, CAList caList) throws CertificateCANotFoundException, NormalizeCertificateException, FileNotFoundException{
		super (fileCertificate);
		initialize (caList, null, null, null);
	}
	
	/**
	 * Constructor en base a un stream de lectura.
	 * 
	 * @param is Stream de lectura que contiene el certificado (tal cual o en formato PEM).
	 * @param caList Lista de certificados de CA
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(InputStream is, CAList caList) throws CertificateCANotFoundException, NormalizeCertificateException{
		super (is);
		initialize (caList, null, null, null);
	}
	
	/**
	 * Constructor en base a un array de bytes.
	 * 
	 * @param contenidoCertificado Array de bytes que es el contenido del certificado (tal cual o en formato PEM).
	 * @param caList Lista de certificados de CA
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(byte[] contenidoCertificado, CAList caList) throws CertificateCANotFoundException, NormalizeCertificateException{
		super (contenidoCertificado);
		initialize (caList, null, null, null);
	}
	
	/**
	 * Constructor en base a un objeto java.security.cert.X509Certificate que permite obtener
	 * la traza de validaci�n.
	 * 
	 * @param certificate Certificado X.509 v3
	 * @param caList Lista de certificados de CA
	 * @param trace Traza de los pasos dados durante la validaci�n
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(X509Certificate certificate, CAList caList, ValidatingTrace trace) throws CertificateCANotFoundException, NormalizeCertificateException{
		super (certificate);
		initialize (caList, null, null, trace);

	}
	
	/**
	 * Constructor en base a un objeto java.security.cert.X509Certificate. Se le puede
	 * indicar el OCSP y/o la CRL que se utilizan para realizar la validaci�n. Este
	 * constructor es interesante para realizar validaciones de certificados de CA,
	 * que en muchas ocasiones no contienen informaci�n de validaci�n.
	 * 
	 * @param certificate Certificado X.509 v3
	 * @param caList Lista de certificados de CA
	 * @param ocsp Cliente OCSP donde realizar la validaci�n (puede ser nulo)
	 * @param crl CRL donde realizar la validaci�n (puede ser nula)
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(X509Certificate certificate, CAList caList, OCSPClient ocsp, CRL crl) throws CertificateCANotFoundException, NormalizeCertificateException{
		super (certificate);
		initialize (caList, ocsp, crl, null);
	}
	
	/**
	 * Constructor en base a un objeto java.security.cert.X509Certificate que permite obtener
	 * la traza de validaci�n. Se le puede indicar el OCSP y/o la CRL que se utilizan para 
	 * realizar la validaci�n. Este constructor es interesante para realizar validaciones de 
	 * certificados de CA, que en muchas ocasiones no contienen informaci�n de validaci�n.
	 * 
	 * @param certificate Certificado X.509 v3
	 * @param caList Lista de certificados de CA
	 * @param ocsp Cliente OCSP donde realizar la validaci�n (puede ser nulo)
	 * @param crl CRL donde realizar la validaci�n (puede ser nula)
	 * @param trace Traza de los pasos dados durante la validaci�n
	 * 
	 * @throws CertificateCANotFoundException El certificado no pertenece a ninguna CA conocida
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptogr�fico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public ValidateCertificate(X509Certificate certificate, CAList caList, OCSPClient ocsp, CRL crl, ValidatingTrace trace) throws CertificateCANotFoundException, NormalizeCertificateException{
		super(certificate);
		initialize (caList, ocsp, crl, null);
	}
	
	/**
	 * Devuelve la lista de certificados de CA que requiere este certificado
	 * 
	 * @return lista de certificados de CA que requiere este certificado
	 */
	public CAList getCertificationChainAsCAList () {
		ValidateCertificate[] arrayCertificates = getCertificationChain();
		CAList caList = new CAList();
		for (int i = 0; i < arrayCertificates.length; i++) {
			caList.addCACertificate(arrayCertificates[i].toX509Certificate());
		}
		
		return caList;
	}
	
	/**
	 * Valida el certificado y su cadena de confianza.
	 * 
	 * @return Resultado de la validaci�n seg�n las constantes definidas en {@link CertificateValidator CertificateValidator}
	 */
	public int validate () {
		this.trace = new ValidatingTrace();
		return validate (this.trace);
	}
	
	/**
	 * Valida el certificado y su cadena de confianza en un momento del tiempo. Hay que 
	 * tener en cuenta lo indicado en la introducci�n de esta clase sobre la validaci�n 
	 * hist�rica.
	 * 
	 * @param validationDate Fecha donde se evaluar� la validaci�n
	 * @return Resultado de la validaci�n seg�n las constantes definidas en {@link CertificateValidator CertificateValidator}
	 */
	public int validate (Date validationDate) {
		this.trace = new ValidatingTrace();
		return validate (validationDate, this.trace);
	}
	
	/**
	 * Valida el certificado y su cadena de confianza, permitiendo obtener la traza de 
	 * la validaci�n.
	 * 
	 * @param externalTrace Traza de los pasos dados durante la validaci�n
	 * @return Resultado de la validaci�n seg�n las constantes definidas en {@link CertificateValidator CertificateValidator}
	 */
	public int validate (ValidatingTrace externalTrace) {
		this.trace = externalTrace;
		return validate (new Date(), this.trace);
	}
		
	/**
	 * Valida el certificado y su cadena de confianza en un momento del tiempo, permitiendo 
	 * obtener la traza de la validaci�n. Hay que tener en cuenta lo indicado en la 
	 * introducci�n de esta clase sobre la validaci�n hist�rica.
	 * 
	 * @param validationDate Fecha donde se evaluar� la validaci�n
	 * @param externalTrace Traza de los pasos dados durante la validaci�n
	 * @return Resultado de la validaci�n seg�n las constantes definidas en {@link CertificateValidator CertificateValidator}
	 */
	public int validate (Date validationDate, ValidatingTrace externalTrace) {
		
		logger.debug ("[ValidateCertificate.validate] :: Start :: " + Arrays.asList(new Object[] { validationDate, externalTrace }));
		
		//-- Load trace
		this.trace = externalTrace;
		if (this.trace == null) { this.trace = new ValidatingTrace(); }
		
		//-- If certificate is self signed return valid certificate
		try {
			if (isSelfSigned()) {
				//-- Check if the certificate has expired
				if (certificate.getNotBefore().after(validationDate)) {
					logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() + 
						" will not active until " + dateFormat.format(certificate.getNotBefore()) + ".");
					trace.add(certificate, "Certificate will not active until " + dateFormat.format(certificate.getNotBefore()));
					return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
				}
				if (certificate.getNotAfter().before(validationDate)) {
					logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() + 
						" has expired in " + dateFormat.format(certificate.getNotBefore()) + ".");
					trace.add(certificate, "Certificate has expired in " + dateFormat.format(certificate.getNotBefore()));
					return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
				}
				
				logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() +  
						" is valid and self signed. End of validation process.");
				trace.add(certificate, "Certificate is self signed, end of chain validation ");
				return ValidationResult.RESULT_VALID;
			}
		} catch (NormalizeCertificateException e1) {
			// Esta excepci�n ya se habr� dado en la inicializaci�n, por lo que no se habr�a iniciado este objeto
		}
		
		//-- Validate certificate
		int result = validateCertificate (validationDate);
		if (result != ValidationResult.RESULT_VALID) {
			//-- Result not valid
			logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() +  
				" is not valid. Result=" + result);
			return result;
		}
		
		//-- Validate chain validation
		ValidateCertificate issuer;
		try {
			issuer = new ValidateCertificate (this.issuerCertificate, this.caList, this.ocsp, this.crl);
		} catch (CertificateCANotFoundException e) {
			logger.debug ("[ValidateCertificate.validate] :: Certificate with issuerDN=" + certificate.getIssuerDN().toString() + 
				" not belong to the list of trusted CAs.");
			trace.add(certificate, "Certificate issuer is not in the chain validation: " + this.issuerCertificate.getSubjectDN().toString());
			return ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS;
		} catch (Exception e) {
			logger.debug ("[ValidateCertificate.validate] :: Error creating Certificate with subject DN=" + 
				certificate.getSubjectDN().toString() + ".", e);
			trace.add(certificate, "Load issuer certificate (" + this.issuerCertificate.getSubjectDN().toString() +") launch an exception: " + e.getMessage());
			return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED ;
		}
		result = issuer.validate(validationDate, trace);
		if (result != ValidationResult.RESULT_VALID) {
			logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() +  
				" has a chain validation not valid. Result=" + result);
			trace.add(certificate, "Validation response" + result + ": " + CertificateValidator.getString (result));
			return ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
		}
		
		//-- Ok, certificate and chain validation are valid
		logger.debug ("[ValidateCertificate.validate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() +  
			" and its chain validation are valid.");
		trace.add(certificate, "Validation response" + result + ": " + CertificateValidator.getString (result));
		return ValidationResult.RESULT_VALID;
	}
	
	/**
	 * M�todo que obtiene el certificado emisor.
	 * 
	 * @return Certificado emisor de este certificado.
	 */
	public ValidateCertificate getIssuerCertificate () {
		
		logger.debug ("[ValidateCertificate.getIssuerCertificate] :: Start");
		
		try {
			return new ValidateCertificate (this.issuerCertificate, this.caList, this.ocsp, this.crl);
		} catch (Exception e) {
			// No se va a dar ya que la cadena de confianza se verifica en la inicializaci�n
			// del objeto
			logger.info("[ValidateCertificate.getIssuerCertificate]::No se ha podido obtener el emisor del certificado", e);
			return null;
		} 
	}
	
	/**
	 * M�todo que obtiene la cadena de confianza del certificado. El primer
	 * elemento del array ser� el emisor del certificado.
	 * 
	 * @return Cadena de confianza de este certificado
	 */
	public List<ValidateCertificate> getCertificationChainAsList () {
		
		logger.debug ("[ValidateCertificate.getCertificationChainAsList] :: Start");
		
		List certificateChain = new ArrayList ();
		ValidateCertificate certificate = this;
		while (true) {
			ValidateCertificate issuer = certificate.getIssuerCertificate();
			if (issuer.equals(certificate)) {
				break;
			}
			certificateChain.add (issuer);
			certificate = issuer;
		}
		
		return certificateChain;
	}
	
	/**
	 * M�todo que obtiene la cadena de confianza del certificado. El primer
	 * elemento del array ser� el propio certificado.
	 * 
	 * @return Cadena de confianza del certificado (incluyendo el propio certificado)
	 */
	public List<ValidateCertificate> getCompleteCertificationChainAsList () {
		
		logger.debug ("[ValidateCertificate.getCompleteCertificationChain] :: Start");
		
		List certificateChain = new ArrayList ();
		ValidateCertificate certificate = this;
		certificateChain.add(certificate);
		while (true) {
			ValidateCertificate issuer = certificate.getIssuerCertificate();
			if (issuer.equals(certificate)) {
				break;
			}
			certificateChain.add (issuer);
			certificate = issuer;
		}
		
		return certificateChain;
	}
	
	/**
	 * M�todo que obtiene la cadena de confianza del certificado. El primer
	 * elemento del array ser� el emisor del certificado.
	 * 
	 * @return Cadena de confianza de este certificado
	 */
	public ValidateCertificate[] getCertificationChain () {
		
		logger.debug ("[ValidateCertificate.getCertificationChain] :: Start");
		
		return (ValidateCertificate[])getCertificationChainAsList().toArray(new ValidateCertificate[0]);
	}
	
	/**
	 * M�todo que obtiene la cadena de confianza del certificado. El primer
	 * elemento del array ser� el propio certificado.
	 * 
	 * @return Cadena de confianza del certificado (incluyendo el propio certificado)
	 */
	public ValidateCertificate[] getCompleteCertificationChain () {
		
		logger.debug ("[ValidateCertificate.getCertificationChain] :: Start");
		
		return (ValidateCertificate[])getCompleteCertificationChainAsList().toArray(new ValidateCertificate[0]);
	}
	
	/**
	 * M�todo que obtiene la cadena de confianza del certificado. El primer
	 * elemento del array ser� el propio certificado.
	 * 
	 * @return Cadena de confianza del certificado (incluyendo el propio certificado)
	 */
	public X509Certificate[] getCompleteCertificationChainAsX509Array () {
		
		logger.debug ("[ValidateCertificate.getCompleteCertificationChainAsX509Array] :: Start");
		
		List<ValidateCertificate> lChain = getCompleteCertificationChainAsList();
		X509Certificate[] arrayCertificates = new X509Certificate[lChain.size()];
		int i = 0;
		for (Iterator<ValidateCertificate> iterator = lChain.iterator(); iterator.hasNext();) {
			ValidateCertificate validateCertificate = iterator.next();
			arrayCertificates[i] = validateCertificate.toX509Certificate();
			i++;
		}
		
		return arrayCertificates;
	}
	
	/**
	 * M�todo que obtiene todos los clientes OCSP que pueden validar este 
	 * certificado. <br><br>
	 * En un primer paso comprueba si existe en el caList un* fichero de validaci�n, 
	 * si es as� y en �l se encuentran las URLs de los OCSPs para validar este 
	 * certificado se devuelven.<br><br>
	 * 
	 * Si no es as� se devuelven los clientes OCSP que apuntan a las URLs indicadas
	 * en la extensi�n Authority Information Access (AIA) del certificado.
	 * 
	 * @return Clientes OCSP para validar este certificado
	 */
	public OCSPClient[] getOCSPClients () {
		
		logger.debug ("[ValidateCertificate.getOCSPClients]::Entrada");
		
		List lOCSPClients = new ArrayList ();
		
		//-- Comprobar si hay cliente OCSP por el validationXML
		if (this.validationXML != null) {
			String issuerCommonName = getCommonNameOrDN (this.issuerCertificate);
			
			//-- Validate with OCSPs
			if (this.validationXML.getOCSPList(issuerCommonName) != null && 
					!this.validationXML.getOCSPList(issuerCommonName).isEmpty()) {
				
				for (Iterator iterator = this.validationXML.getOCSPList(issuerCommonName).iterator(); iterator
						.hasNext();) {
					
					lOCSPClients.add (iterator.next());
				
				}
				
				//-- Si se han encontrado se devuelven
				if (!lOCSPClients.isEmpty()) {
					return (OCSPClient[]) lOCSPClients.toArray(new OCSPClient[0]);
				}
			}
		}
		
		//-- Obtenerlos de la clase padre
		return super.getOCSPClients();

	}
	
	/**
	 * M�todo que obtiene la traza de los pasos dados durante la validaci�n. Lo normal
	 * es que se llame a este m�todo tras la validaci�n si se desea obtener una traza
	 * de lo sucedido.
	 * 
	 * @return Traza con los pasos dados durante la validaci�n
	 */
	public ValidatingTrace getTrace () {
		return this.trace;
	}

	/**
	 * Obtiene un objeto CRL de acuerdo al valor de la extensi�n CRL Distribution Point
	 * del certificado. Si hay m�s de una CRL definida, el m�todo devuelve la primera
	 * a la que puede tener acceso.
	 * 
	 * @return CRL para validar el certificado
	 * @throws Exception No se puede obtener la CRL
	 */
	public CRL getCRL() throws CertificateFieldException, InvalidCRLException {
		
		logger.debug("[ValidateCertificate.getCRL]:: Init");
		
		//-- Try to get CRL if the certificate indicates how to obtain it with a URL
		String crlURLs [] = null;
		try {
			crlURLs = getCrlUrls();
		} catch (CertificateFieldException e) {
			trace.add(certificate, "Cannot read CRL Distribution Point URL from certificate");
			throw e;
		}
		
		if (crlURLs == null || crlURLs.length == 0) {
			logger.debug ("[ValidateCertificate.getCRL_URLs] :: CRL Distribution Point URL is not found in certificate");
			trace.add(certificate, "CRL Distribution Point URL is not found in certificate");
			throw new CertificateFieldException ("CRL Distribution Point URL is not found in certificate");
		}
		
		return getCRL (crlURLs);
	}
		
	//-- Private methods
	
	/*
	 * Method that validates the certificate (doen't validate its chain validation). The steps 
	 * to validate:
	 * <ul>
	 * 	<li>Try to get the OCSP from the Authority Information Access (AIA): validate with OCSP</li>
	 *  <li>Try to get the CRL from the CRL Distribution Point: validate with CRL</li>
	 * </ul>
	 * Validation date indicates when the time the validation occurs.
	 * 
	 * @param validationDate Validation date
	 * @return Validation value
	 */
	private int validateCertificate (Date validationDate) {
		
		logger.debug ("[ValidateCertificate.validateCertificate] :: Start :: " + validationDate);
		
		//-- Check if the certificate has expired
		if (certificate.getNotBefore().after(validationDate)) {
			logger.debug ("[ValidateCertificate.validateCertificate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() + 
				" will not active until " + dateFormat.format(certificate.getNotBefore()) + ".");
			trace.add(certificate, "Certificate will not active until " + dateFormat.format(certificate.getNotBefore()));
			return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
		}
		if (certificate.getNotAfter().before(validationDate)) {
			logger.debug ("[ValidateCertificate.validateCertificate] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() + 
				" has expired in " + dateFormat.format(certificate.getNotBefore()) + ".");
			trace.add(certificate, "Certificate has expired in " + dateFormat.format(certificate.getNotBefore()));
			return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
		}
		
		int result = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;

		//-- Try to validate with service
		
		
		//-- Try to validate with a ValidationXML
		try {
			result = validateWithValidationXML (validationDate);
		}catch (Exception e) {
			trace.add(certificate, "Error validating with a validation XML");
			logger.debug ("[ValidateCertificate.validateCertificate] :: Error validating with a validation XML", e);
		}
		
		if (result == ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED) {
			//-- Try to validate with the OCSP whose URL can be in the certificate
			try {
				result = validateWithOCSPInformation (validationDate);
			}catch (Exception e) {
				trace.add(certificate, "Error validating with OCSP");
				logger.debug ("[ValidateCertificate.validateCertificate] :: Error validating with OCSP", e);
			}
		}
		
		if (result == ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED) {
			//-- Try to validate with the CRL whose URL can be in the certificate
			try {
				result = validateWithCRLInformation (validationDate);
			}catch (Exception e) {
				trace.add(certificate, "Error validating with CRL");
				logger.debug ("[ValidateCertificate.validateCertificate] :: Error validating with CRL", e);
			}
		}
		
		if (result != ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED) {
			logger.debug ("[ValidateCertificate.validateCertificate] :: Certificate validated :: result=" + result);
			return result;
		} 
		
		logger.debug ("[ValidateCertificate.validateCertificate] :: Cannot validate certificate");
		trace.add(certificate, "Certificate cannot be validated");
		return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED ;
	}
	
	/*
	 * Validate with information stored in a validation XML
	 * Validation date indicates when the time the validation occurs.
	 * 
	 * @param validationDate Validation date
	 * @return Validation result
	 */
	private int validateWithValidationXML(Date validationDate) {
		
		logger.debug("[ValidateCertificate.validateWithValidationXML]:: Init :: " + validationDate);

		//-- If the validation XML is not found returns
		if (this.validationXML != null) {
			String issuerCommonName = getCommonNameOrDN (this.issuerCertificate);
			
			//-- Validate with OCSPs
			if (this.validationXML.getOCSPList(issuerCommonName) != null && 
					!this.validationXML.getOCSPList(issuerCommonName).isEmpty()) {
				
				for (Iterator iterator = this.validationXML.getOCSPList(issuerCommonName).iterator(); iterator
						.hasNext();) {
					
					OCSPClient ocsp = (OCSPClient) iterator.next();
					logger.debug ("[ValidateCertificate.validateWithValidationXML] :: validating in OCSP " + ocsp.getURL());
					try {
						int result = ocsp.validate(this, getIssuerCertificate(), validationDate);
						trace.add(certificate, "OCSP (" + ocsp.getURL() + ") returns response " + result + ": " + CertificateValidator.getString (result));
						this.ocsp = ocsp;
						return result;
					} catch (Exception e) {
						logger.debug ("[ValidateCertificate.validateWithValidationXML] :: Cannot validate certificate in OCSP " + ocsp.getURL(), e);
						trace.add(certificate, "OCSP (" + ocsp.getURL() + ") launch an exception: " + e.getMessage());
					}
				}
				
			}

			//-- Validate with CRLs
			if (this.validationXML.getCRLList(issuerCommonName) != null && 
					!this.validationXML.getCRLList(issuerCommonName).isEmpty()) {
				
				try {
					CRL crl = getCRL((String[]) this.validationXML.getCRLList(issuerCommonName).toArray(new String [0])); 
					
					int result = validateInCRL (crl, validationDate);
					trace.add(certificate, "CRL returns response " + result + ": " + CertificateValidator.getString (result));
					this.crl = crl;
					return result;
				} catch (Exception e) {
					logger.debug ("[ValidateCertificate.validateWithCRLInformation] :: Cannot validate certificate in CRL", e);
					trace.add(certificate, "Manage CRL throws an exception: " + e.getMessage());
				}
				
			}

		}
		
		return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
	}

	/*
	 * Validate certificate whith the OCSP's URLs stored in the authority information
	 * access (IAI) extension of the X.509 certificate.
	 * Validation date indicates when the time the validation occurs.
	 * 
	 * @param validationDate Validation date
	 * @return Validation result
	 */
	private int validateWithOCSPInformation(Date validationDate) {
		
		logger.debug("[ValidateCertificate.validateWithOCSPInformation]:: Init :: " + validationDate);
		
		//-- Try to get the OCSP from the Authority Information Access (AIA)
		String urlsOCSP [] = null;
		try {
			urlsOCSP = getOcspUrls();
		} catch (Exception e) {
			trace.add(certificate, "Cannot read OCSP URL from certificate");
		}
		
		//-- If OCSP URL is not found it is possible that the OCSP is passed in constructor
		if ((urlsOCSP == null || urlsOCSP.length == 0) && this.ocsp != null) {
			trace.add(certificate, "Using OCSP from child certificate: " + ocsp.getURL());
			try {
				int result = this.ocsp.validate(this, getIssuerCertificate(), validationDate);
				trace.add(certificate, "OCSP (" + ocsp.getURL() + ") returns response " + result + ": " + CertificateValidator.getString (result));

				//-- If result is certificate unknown we try to validate with CRL
				if (result != ValidationResult.RESULT_CERTIFICATE_UNKNOWN) {
					logger.debug ("[ValidateCertificate.validateWithOCSPInformation] :: Certificate with subject DN=" + certificate.getSubjectDN().toString() + 
							" has a result in OCSP " + this.ocsp.getURL() + " = " + result);
					return result;
				}
			} catch (Exception e) {
				logger.debug ("[ValidateCertificate.validateWithOCSPInformation] :: Cannot validate certificate in OCSP " + this.ocsp.getURL(), e);
				trace.add(certificate, "OCSP (" + ocsp.getURL() + ") launch an exception: " + e.getMessage());
			}
		} else {
			//-- OCSPs URLs are in the AIA from certificate
			if (urlsOCSP != null) {
				for (int i=0;i<urlsOCSP.length;i++) {
					try {
						OCSPClient ocsp = new OCSPClient (new URL (urlsOCSP[i]));
						int result = ocsp.validate(this, getIssuerCertificate(), validationDate);
						trace.add(certificate, "OCSP (" + ocsp.getURL() + ") returns response " + result + ": " + CertificateValidator.getString (result));
						this.ocsp = ocsp;
						return result;
					} catch (Exception e) {
						logger.debug ("[ValidateCertificate.validateWithOCSPInformation] :: Cannot validate certificate in OCSP " + urlsOCSP[i], e);
						trace.add(certificate, "OCSP (" + urlsOCSP[i] + ") launch an exception: " + e.getMessage());
					}
				}
			}
		}
		
		return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
	}

	/*
	 * Validate certificate whith the CRL's URLs stored in the CRL distribution point
	 * extension of the X.509 certificate.
	 * Validation date indicates when the time the validation occurs.
	 * 
	 * @param validationDate Validation date
	 * @return Result of validation
	 */
	private int validateWithCRLInformation(Date validationDate) {
		
		logger.debug("[ValidateCertificate.validateWithCRLInformation]:: Init :: " + validationDate);
		
		//-- Try to get the CRL from the CRL Distribution Point
		CRL crl = null;
		try {
			crl = getCRL();
		} catch (Exception e) {
			logger.debug ("[ValidateCertificate.validateWithCRLInformation] :: cannot obtain a CRL");
		}
		
		//-- If does not get the CRL URL it is possible that the CRL is passed in constructor
		if (crl == null && this.crl != null) {
			trace.add(certificate, "Using CRL from child certificate");
			crl = this.crl;
		} 
		
		//-- validate
		if (crl != null) {
			try {
				int result = validateInCRL (crl, validationDate);
				trace.add(certificate, "CRL returns response " + result + ": " + CertificateValidator.getString (result));
				this.crl = crl;
				return result;
			} catch (Exception e) {
				logger.debug ("[ValidateCertificate.validateWithCRLInformation] :: Cannot validate certificate in CRL", e);
				trace.add(certificate, "Manage CRL throws an exception: " + e.getMessage());
			}
		}
		
		return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
	}

	/*
	 * Method that validates the certificate against the crl passed as parameter
	 * Validation date indicates when the time the validation occurs.
	 * 
	 * @param validationDate Validation date
	 * @param crl CRL used to validate the certificate
	 */
	private int validateInCRL (CRL crl, Date validationDate) throws Exception {

		logger.debug("[ValidateCertificate.validateInCRL]:: Init :: " + Arrays.asList(new Object [] { crl, validationDate }));
		
		//-- If we have CRLs check that the certificate is not revoked
		if (crl == null) {
			logger.debug("[ValidateCertificate.validateInCRL] :: CRL is a null value");
			throw new Exception ("CRL is a null value");
		}
		
		//-- Check if certificate is in CRL. If it is in we check that revocation date is before validation date
		Date revocationDate = crl.getRevocationDate(this);
		if (revocationDate != null && (revocationDate.before(validationDate) || revocationDate.equals(validationDate))) {
			return ValidationResult.RESULT_CERTIFICATE_REVOKED;
		} else {
			return ValidationResult.RESULT_VALID;
		}
	}
	
	/*
	 * Gets the CRL object from an array of CRL's URL. If there are more than one URL, 
	 * it returns the first it can get. 
	 * 
	 * @return Certificate's CRL 
	 * @throws InvalidCRLException If it cannot get the CRL
	 */
	private CRL getCRL(String[] crlURLs) throws InvalidCRLException {
		
		logger.debug("[ValidateCertificate.getCRL]:: Init :: " + crlURLs);
		
		if (crlURLs != null) {
			for (int i=0;i<crlURLs.length;i++) {
				try {
					trace.add(certificate, "Get CRL from " + crlURLs[i]);
					logger.debug("[ValidateCertificate.getCRL]:: Get CRL from " + crlURLs[i]);
					
					return new CRL (new URL (crlURLs[i]), this.caList);

				} catch (Exception e) {
					logger.debug("[ValidateCertificate.getCRL]:: Error getting CRL from " + crlURLs [i]);
					trace.add(certificate, "Get CRL (" + crlURLs[i] + ") launch an exception: ");
				} 
			}
			
			//-- Cannot get the CRL
			logger.debug ("[ValidateCertificate.getCRL_URLs] :: Cannot get the CRL from any URL in the certificate");
			trace.add(certificate, "Cannot get any " + crl);
			throw new InvalidCRLException ("Cannot get the CRL from any URL in the certificate");
		}
		
		//-- Cannot get URL for a CRL
		logger.debug ("[ValidateCertificate.getCRL_URLs] :: Cannot get the CRL's URL in the certificate");
		trace.add(certificate, "Cannot get the CRL's URL in the certificate");
		throw new InvalidCRLException ("Cannot get the CRL's URL in the certificate");
	}
	
	/*
	 * M�todo que inicializa el objeto tras ser insertado el certificado en el mismo por
	 * la clase padre.
	 */
	private void initialize (CAList caList, OCSPClient ocsp, CRL crl, ValidatingTrace trace) throws CertificateCANotFoundException, NormalizeCertificateException{

		if (caList == null) {
   			logger.info ("[ValidateCertificate.initialize] :: La lista de CAs es nula");
   			caList = new CAList ();
		}
		
		//-- Load values into fields
		this.caList = caList;
		this.validationXML = caList.getValidationXML();
		this.ocsp = ocsp;
		this.crl = crl;
		if (trace != null) {
			this.trace = trace;
		}
		
		//-- Check if certificate is self signed
		if (!isSelfSigned()) {
			//-- Comprueba si se puede obtener la cadena de confianza, empezando por el primer elemento
			if (!caList.isOwned(certificate)) {
	   			//-- The issuer is unknown
	   			logger.info ("[ValidateCertificate.validate] :: Falta alguno de los certificados de la cadena de certificaci�n en la lista de certificados de CA");
	   			throw new CertificateCANotFoundException ("Falta alguno de los certificados de la cadena de certificaci�n en la lista de certificados de CA");
			}
		
			//-- Comprobar recursivamente el resto de elementos
			try {
				new ValidateCertificate (caList.getCACertificate(certificate), caList);
			} catch (NormalizeCertificateException e) {
	   			logger.info ("[ValidateCertificate.validate] :: Alguno de los certificados de la cadena de certificaci�n no ha podido ser normalizado", e);
	   			throw new CertificateCANotFoundException ("Alguno de los certificados de la cadena de certificaci�n no ha podido ser normalizado", e);
			}
			
			//-- Load certificate's issuer
			this.issuerCertificate = caList.getCACertificate(certificate);
		} else {
			
			//-- Comprobar que el certificado pertenece a la CAList
			if (!caList.contains(this.certificate)) {
	   			logger.info ("[ValidateCertificate.validate] :: El certificado es autofirmado y no pertenece a la lista de CAs permitidas");
	   			throw new CertificateCANotFoundException ("El certificado es autofirmado y no pertenece a la lista de CAs permitidas");
			}
			
			//-- The issuer of certificate is itself
			this.issuerCertificate = this.certificate;
		}
	}
	
	/*
	 * Gets the common name of the certificate
	 * 
	 * @param certificate Certificate
	 * @return Common name of the certificate
	 */
	private static String getCommonNameOrDN(X509Certificate certificate) {

		String dn = certificate.getSubjectDN().toString();
		StringTokenizer st = new StringTokenizer (dn, ",");
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			if (token.startsWith("CN=")) {
				return token.substring(3);
			}
		}
		
		return dn;
	}

	

}
