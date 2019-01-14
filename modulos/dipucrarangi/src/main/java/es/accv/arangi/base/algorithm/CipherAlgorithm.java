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
package es.accv.arangi.base.algorithm;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

/**
 * Clase con constantes que identifican a algoritmos de cifrado.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class CipherAlgorithm {

	/**
	 * Algoritmo de encriptado definido en FIPS PUB 186. 
	 */
	public final static String DSA	= "DSA";
	
	/**
	 * Algoritmo de encriptado RSA con relleno PKCS1. 
	 */
	public final static String RSA	= "RSA/ECB/PKCS1Padding";
	
	/*
	 * Mapa de para pasar de texto a OID
	 */
	private static HashMap mapOIDs = new HashMap ();
	
	/*
	 * Mapa de para pasar de OID a text
	 */
	private static HashMap mapReverseOIDs = new HashMap ();
	
	static {
		mapOIDs.put(DSA, "1.2.840.10040.4.1");
		mapOIDs.put(RSA, PKCSObjectIdentifiers.rsaEncryption.getId());
		
		mapReverseOIDs.put("1.2.840.10040.4.1", DSA);
		mapReverseOIDs.put(PKCSObjectIdentifiers.rsaEncryption.getId(), RSA);
	}

	/**
	 * Devuelve el algoritmo por defecto para cifrar (m�s utilizado)
	 * 
	 * @return RSA
	 */
	public static final String getDefault() {
		return RSA;
	}

	/**
	 * Devuelve el OID del algoritmo pasado como par�metro. Este algoritmo debe ser uno de
	 * los definidos como constantes en esta clase.
	 * 
	 * @param cipherSignatureAlgorithm Nombre del algoritmo
	 * @return OID del algoritmo o null si el algoritmo no existe
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getOID(String cipherSignatureAlgorithm) throws NoSuchAlgorithmException {
		String oid = (String) mapOIDs.get(cipherSignatureAlgorithm);
		
		if (oid == null) {
			throw new NoSuchAlgorithmException ("El algoritmo de cifrado '" + cipherSignatureAlgorithm + "' no existe dentro de Arangi");
		}
		
		return oid;
	}

	/**
	 * Devuelve el OID del algoritmo por defecto para encriptar (el m�s utilizado)
	 * 
	 * @return RSA OID
	 */
	public static final String getDefaultOID() {
		try {
			return getOID(getDefault());
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar
			return null;
		}
	}
	
	/**
	 * Devuelve el nombre del algoritmo en base a su OID. Este algoritmo debe ser uno de
	 * los definidos como constantes en esta clase.
	 * 
	 * @param oid OID del algoritmo
	 * @return Nombre del algoritmo
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getAlgorithmName(String oid) throws NoSuchAlgorithmException {
		String nombre = (String) mapReverseOIDs.get(oid);
		if (nombre == null) {
			throw new NoSuchAlgorithmException ("El oid '" + oid + "' no existe dentro de Arangi");
		}
		
		return nombre;
	}

}
