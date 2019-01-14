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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import es.accv.arangi.base.exception.CertificateRequestException;
import es.accv.arangi.base.util.Util;

/**
 * Representaci�n de una petici�n de certificado en formato PKCS#10 seg�n la 
 * <a href="http://tools.ietf.org/html/rfc2986">RFC-2986</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 *
 */
public class PKCS10Request extends CertificateRequest {
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(PKCS10Request.class);
	
	PKCS10CertificationRequest pkcs10Request;
	
	//-- Constructor
	
	/**
	 * Constructor al que se le pasa el fichero PKCS#0 en formato PEM o su 
	 * representaci�n en base64
	 * 
	 * @param pkcs10PEM PKCS#10
	 * @throws IOException Excepci�n si el PKCS#10 no tiene la estructura
	 * 	correcta
	 */
	public PKCS10Request (String pkcs10PEM) throws IOException {
		super();
		
	    logger.debug("[PKCS10Request]::Entrada::" + pkcs10PEM);
	    
	    if ( pkcs10PEM == null || pkcs10PEM.equals("") ) {
	      throw new IOException("El fichero PKCS#10 es nulo o est� vac�o");
	    }
	    
	    //-- Obtener el contenido del PEM
	    byte[] bytesPkcs10 = Util.getBytesFromPEM(pkcs10PEM);
	    logger.debug("[PKCS10Request]::Obtenido el contenido del PKCS#10: " + bytesPkcs10); 

	    try {
	    	pkcs10Request = new PKCS10CertificationRequest(bytesPkcs10);
	    } catch (Exception e) {
	    	throw new IOException("El fichero no parece un PKCS#10 v�lido");
	    }

	}

	//-- Implementaci�n de la clase abstracta
	
	@Override
	public PublicKey getPublicKey() throws CertificateRequestException {
		try {
			org.bouncycastle.asn1.pkcs.RSAPublicKey bcPk = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance (pkcs10Request.getSubjectPublicKeyInfo().parsePublicKey());
			RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(bcPk.getModulus(), bcPk.getPublicExponent());
			KeyFactory fact = KeyFactory.getInstance("RSA");
			return fact.generatePublic(pubSpec);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[PKCS10Request.getPublicKey]::No existe el algoritmo de la clave p�blica", e);
			throw new CertificateRequestException ("No existe el algoritmo de la clave p�blica", e);
		} catch (InvalidKeySpecException e) {
			logger.info("[PKCS10Request.getPublicKey]::La clave p�blica no es v�lida", e);
			throw new CertificateRequestException ("La clave p�blica no es v�lida", e);
		} catch (IOException e) {
			logger.info("[PKCS10Request.getPublicKey]::No se puede leer la clave del PKCS#10", e);
			throw new CertificateRequestException ("No se puede leer la clave del PKCS#10", e);
		}
	}
	
	@Override
	public int getPublicKeyLength () throws CertificateRequestException {
		return ((RSAPublicKey)getPublicKey()).getModulus().bitLength();
	}

	@Override
	public byte[] getEncoded() {
		try {
			return pkcs10Request.getEncoded();
		} catch (IOException e) {
			logger.info("[PKCS10Request.getEncoded]::No se puede pasar el PKCS#10 a array de bytes", e);
			return null;
		}
	}

}
