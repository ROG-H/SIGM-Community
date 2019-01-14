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
package es.accv.arangi.base.mityc;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.CRL;
import es.accv.arangi.base.certificate.validation.OCSPClient;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.certificate.validation.ValidateCertificate;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.javasign.certificate.CertStatusException;
import es.mityc.javasign.certificate.ICertStatus;
import es.mityc.javasign.certificate.ICertStatusRecoverer;

/**
 * Clase que implementa el validador de certificados de las clases del MITyC.
 * Valida los certificados en base a un caList.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class CAListCertStatusRecover implements ICertStatusRecoverer {

	/**
	 * Logger de la clase
	 */
	private Logger logger = Logger.getLogger(CAListCertStatusRecover.class);
	
	/**
	 * CAList
	 */
	private CAList caList; 
	
	/**
	 * Flag que indica si la informaci�n de validaci�n puede ser obtenida mediante CRL
	 */
	private boolean allowCRLValidation;
	
	//-- Constructores
	
	/**
	 * Construye un validador en base a un <code>CAList</code>
	 */
	public CAListCertStatusRecover (CAList caList, boolean allowCRLValidation) {
		this.caList = caList;
		this.allowCRLValidation = allowCRLValidation;
	}
	
	//-- Implementaci�n de ICertStatusRecover
	
	/**
	 * Recupera el estado de la cadena de certificaci�n del certificado indicado.
	 * 
	 * @param certificate Certificado
	 * @return Estados de cada uno de los certificados de la cadena de confianza
	 */
	public List<ICertStatus> getCertChainStatus(X509Certificate certificate) throws CertStatusException {
		logger.debug("[CAListCertStatusRecover.getCertChainStatus]::Entrada::" + certificate.getSubjectDN());
		
		//-- Obtener el certificado de Arang�
		ValidateCertificate validateCertificate;
		try {
			validateCertificate = new ValidateCertificate(certificate, caList);
		} catch (CertificateCANotFoundException e) {
			logger.info("[CAListCertStatusRecover.getCertChainStatus]::El certificado no es de una CA de confianza", e);
			throw new CertStatusException("El certificado no es de una CA de confianza", e);
		} catch (NormalizeCertificateException e) {
			logger.info("[CAListCertStatusRecover.getCertChainStatus]::El certificado no se puede normalizar", e);
			throw new CertStatusException("El certificado no se puede normalizar", e);
		}
		
		//-- Obtener la cadena de validaci�n
		ValidateCertificate[] cadenaCertificados = validateCertificate.getCertificationChain();
		
		//-- Recorrer la cadena de validaci�n validando sus componentes (la cadena
		//-- incluye como primer elemento el certificado de firma)
		logger.debug("[CAListCertStatusRecover.getCertChainStatus]::Longitud de la cadena de certificaci�n: " + cadenaCertificados.length);
		List<ICertStatus> lStatus = new ArrayList<ICertStatus>();
		ICertStatus certStatus = getCertStatus (certificate);
		if (!certStatus.getStatus().equals(ICertStatus.CERT_STATUS.valid)) {
			logger.info("[CAListCertStatusRecover.getCertChainStatus]::El certificado '" + certificate.getSubjectDN() + "' no es v�lido");
			throw new CertStatusException("El certificado no es v�lido");
		}
		lStatus.add (certStatus);
		for (int i = 0; i < cadenaCertificados.length; i++) {
			certStatus = getCertStatus (cadenaCertificados[i].toX509Certificate());
			if (!certStatus.getStatus().equals(ICertStatus.CERT_STATUS.valid)) {
				logger.info("[CAListCertStatusRecover.getCertChainStatus]::El certificado '" + certificate.getSubjectDN() + "' no es v�lido");
				throw new CertStatusException("El certificado no es v�lido");
			}
			lStatus.add(certStatus);
		}
		
		return lStatus;
	}

	/**
	 * Recupera el estado de la cadena de certificaci�n del conjunto de certificados indicados.
	 * 
	 * @param certificates Certificados
	 * @return Estados de cada uno de los certificados de la cadena de confianza de cada uno
	 * 	de los certificados
	 */
	public List<List<ICertStatus>> getCertChainStatus(List<X509Certificate> certificates) throws CertStatusException {
		//-- Iterar sobre la lista y obtener los resultados
		List<List<ICertStatus>> lStatus = new ArrayList<List<ICertStatus>>();
		for (Iterator<X509Certificate> iterator = certificates.iterator(); iterator.hasNext();) {
			X509Certificate certificate = iterator.next();
			lStatus.add (getCertChainStatus(certificate));
		}
		
		return lStatus;
	}

	/**
	 * Recupera el estado del certificado indicado.
	 * 
	 * @param certificate Certificado
	 * @return Estado del certificado
	 */
	public ICertStatus getCertStatus(X509Certificate certificate) throws CertStatusException {
		
		logger.debug("[CAListCertStatusRecover.getCertStatus]::Entrada::" + certificate.getSubjectDN());
		
		//-- Obtener el certificado de Arang�
		ValidateCertificate validateCertificate;
		try {
			validateCertificate = new ValidateCertificate(certificate, caList);
		} catch (CertificateCANotFoundException e) {
			logger.info("[CAListCertStatusRecover.getCertStatus]::El certificado no es de una CA de confianza", e);
			throw new CertStatusException("El certificado no es de una CA de confianza", e);
		} catch (NormalizeCertificateException e) {
			logger.info("[CAListCertStatusRecover.getCertStatus]::El certificado no se puede normalizar", e);
			throw new CertStatusException("El certificado no se puede normalizar", e);
		}
		
		//-- Si el certificado es autofirmado es v�lido
		try {
			if (validateCertificate.isSelfSigned()) {
				logger.debug("[CAListCertStatusRecover.getCertStatus]::El certificado es autofirmado");
				return new ValidStatus (certificate);
			}
		} catch (NormalizeCertificateException e) {
			logger.info("[CAListCertStatusRecover.getCertStatus]::No se puede comprobar si el certificado es autofirmado", e);
			throw new CertStatusException("No se puede comprobar si el certificado es autofirmado", e);
		}
		
		//-- Obtener los OCSPs del certificado y con ellos validarlo. Se ir� buscando por 
		//-- todos los ocsps hasta que alguno devuelva v�lido o revocado. Si al final 
		//-- ninguno obtiene estas respuestas nos quedaremos con lo que respondi� el primero.
		OCSPClient[] ocspClients = validateCertificate.getOCSPClients();
		OCSPResponse ocspResponseTemporal = null;
		CertStatusException exception = null;
		for (int i = 0; i < ocspClients.length; i++) {
			OCSPResponse ocspResponse;
			try {
				ocspResponse = ocspClients[i].getOCSPResponse(validateCertificate);
			} catch (Exception e) {
				logger.info("[CAListCertStatusRecover.getCertStatus]::Error obteniendo la validaci�n del certificado mediante OCSP", e);
				exception = new CertStatusException("No se puede comprobar si el certificado es autofirmado", e);
				continue;
			}
			if (ocspResponse.getStatus() != ValidationResult.RESULT_VALID &&
					ocspResponse.getStatus() != ValidationResult.RESULT_CERTIFICATE_REVOKED) {
				logger.debug ("[CAListCertStatusRecover.getCertStatus]::OCSP " + i + " responde desconocido");
				if (ocspResponseTemporal == null) { ocspResponseTemporal = ocspResponse; }
				continue;
			}
			
			//-- la respuesta es v�lido o revocado
			logger.debug ("[CAListCertStatusRecover.getCertStatus]::OCSP " + i + " responde " + ocspResponse.getStatus());
			return new OCSPStatus(ocspResponse, certificate);
		}
		
		//-- Buscar por CRL
		if (this.allowCRLValidation) {
			CRL crl = null;
			try {
				crl = validateCertificate.getCRL();
			} catch (Exception e) {
				logger.debug("[CAListCertStatusRecover.getCertStatus]::CRL", e);
			}
	
			if (crl != null) {
				return new CRLStatus(crl, certificate);
			}
		}
		
		//-- Si se llega aqu� es que no se puede realizar la validaci�n ni por OCSP ni por CRL
		if (ocspResponseTemporal == null && exception != null) {
			throw exception;
		}

		//-- No se ha encontrado ninguna respuesta
		if (ocspResponseTemporal == null) {
			logger.debug ("[CAListCertStatusRecover.getCertStatus]::Certificado desconocido porque no tiene definidos " +
					"OCSPs en lo que validarse");
			return new UnknownStatus(certificate);
		}
		//-- No se ha encontrado ninguna respuesta que no sea desconocida
		logger.debug ("[CAListCertStatusRecover.getCertStatus]::Certificado desconocido");
		return new OCSPStatus(ocspResponseTemporal, certificate);
	}

	/**
	 * Recupera el estado del certificado indicado.
	 * 
	 * @param certificates Certificados
	 * @return Estados de los certificados
	 */
	public List<ICertStatus> getCertStatus(List<X509Certificate> certificates) throws CertStatusException {
		
		//-- Iterar sobre la lista y obtener los resultados
		List<ICertStatus> lStatus = new ArrayList<ICertStatus>();
		for (Iterator<X509Certificate> iterator = certificates.iterator(); iterator.hasNext();) {
			X509Certificate certificate = iterator.next();
			lStatus.add (getCertStatus(certificate));
		}
		
		return lStatus;
	}

}
