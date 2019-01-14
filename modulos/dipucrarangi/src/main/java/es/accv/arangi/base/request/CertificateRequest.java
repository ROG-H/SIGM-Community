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
package es.accv.arangi.base.request;

import java.io.IOException;
import java.security.PublicKey;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.exception.CertificateRequestException;

/**
 * Clase con los m�todos que debe cumplir una petici�n de certificado (CSR). Los 
 * tipos de CSR aceptados son:
 * <ul>
 * 	<li>PKCS#10: es el formato m�s utilizado y viene definido en la 
 * 	<a href="https://tools.ietf.org/html/rfc2986">RFC-2986</a>.</li>
 * 	<li>Certificate Request Message Format (CRMF): formato utilizado por las las 
 * 	librer�as criptogr�ficas javascript de Mozilla.</li>
 * 	<li>Signed Public Key And Challenge (SPKAC): formato generado por el tag
 * 	'keygen' de HTML5.</li>
 * </ul>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public abstract class CertificateRequest extends ArangiObject{

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CertificateRequest.class);
	
	//-- M�todos est�ticos
	
	/**
	 * Obtiene la instancia adecuada para el par�metro
	 * 
	 * @param certificateRequestPEMBase64 Petici�n de certificado en base64 o
	 * formato PEM (�ste �ltimo formato lo utiliza PKCS#10)
	 * @return Instancia adecuada para el par�metro
	 * @throws IOException El fichero pasado no tiene la estructura de una 
	 * 	petici�n de certificado reconocida (PKCS#10 o CRMF)
	 */
	public static CertificateRequest getInstance (String certificateRequestPEMBase64) throws IOException {
		logger.debug("[CertificateRequest.getInstance]::Entrada::" + certificateRequestPEMBase64);
		
		String errorPkcs10, errorCrmf;
		
		//-- Intentar como PKCS#10
		PKCS10Request pkcs10;
		try {
			pkcs10 = new PKCS10Request(certificateRequestPEMBase64);
			return pkcs10;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un PKCS#10: " + e.getMessage());
			errorPkcs10 = e.getMessage();
		}
		
		//-- Intentar como CRMF
		try {
			CRMFRequest crmf = new CRMFRequest(certificateRequestPEMBase64);
			return crmf;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un CRMF: " + e.getMessage());
			errorCrmf = e.getMessage();
		}
		
		//-- Intentar como SPKAC
		try {
			SPKACRequest spkac = new SPKACRequest(certificateRequestPEMBase64);
			return spkac;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un SPKAC: " + e.getMessage());
			errorCrmf = e.getMessage();
		}
		
		//-- No tiene ninguno de los formatos -> lanzar una excepci�n
		logger.debug("[CertificateRequest.getInstance]::El fichero no tiene un formato de petici�n de certificado reconocible");
		throw new IOException("El fichero no tiene un formato de petici�n de certificado reconocible.\nPKCS#10: " + errorPkcs10 + "\nCRMF: " + errorCrmf);
		
	}
	
	//-- M�todos abstractos
	
	/**
	 * Obtiene la clave publica contenida en la petici�n
	 * 
	 * @return Clave P�blica
	 * @throws CertificateRequestException Error obteniendo la clave p�blica. La 
	 * 	excepci�n que origin� el error vendr� anidada en �sta.
	 */
	public abstract PublicKey getPublicKey() throws CertificateRequestException;
	
	/**
	 * Obtiene el tama�o (n�mero de bits) de la clave p�blica de 
	 * la petici�n.
	 * 
	 * @return Tama�o de la clave p�blica 
	 * @throws CertificateRequestException Error obteniendo la clave p�blica. La 
	 * 	excepci�n que origin� el error vendr� anidada en �sta.
	 */
	public abstract int getPublicKeyLength() throws CertificateRequestException;
	
	/**
	 * Obtiene el contenido de la petici�n del certificado
	 * 
	 * @return Contenido de la petici�n del certificado
	 */
	public abstract byte[] getEncoded();

}
