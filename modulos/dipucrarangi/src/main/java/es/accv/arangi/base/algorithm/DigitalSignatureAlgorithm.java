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
 * Clase con constantes que identifican a algoritmos de firma digital. Dado
 * que la firma digital es la aplicaci�n de un algoritmo de cifrado tras la
 * ejecuci�n de un algoritmo de digest, los algoritmos que se identifican
 * en esta clase son la combinaci�n de todos los m�todos de digest con 
 * todos los m�todos de cifrado.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class DigitalSignatureAlgorithm {

	/**
	 * Algoritmo que utiliza al algoritmo MD2 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String MD2_RSA	= "MD2withRSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo MD5 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String MD5_RSA	= "MD5withRSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo SHA-1 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String SHA1_RSA	= "SHA1withRSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo SHA1 para obtener el digest y el
	 * algoritmo DSA para cifrar.
	 */
	public final static String SHA1_DSA	= "SHA1withDSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo SHA-256 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String SHA256_RSA	= "SHA256withRSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo SHA-384 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String SHA384_RSA	= "SHA384withRSA";
	
	/**
	 * Algoritmo que utiliza al algoritmo SHA-512 para obtener el digest y el
	 * algoritmo RSA para cifrar.
	 */
	public final static String SHA512_RSA	= "SHA512withRSA";
	
	/*
	 * Mapa de para pasar de texto a OID
	 */
	private static HashMap mapOIDs = new HashMap ();
	
	/*
	 * Mapa de para pasar de OID a text
	 */
	private static HashMap mapReverseOIDs = new HashMap ();
	
	static {
		mapOIDs.put(MD2_RSA, "1.2.840.113549.1.1.2");
		mapOIDs.put(MD5_RSA, "1.2.840.113549.1.1.4");
		mapOIDs.put(SHA1_RSA, "1.2.840.113549.1.1.5");
		mapOIDs.put(SHA1_DSA, "1.2.840.10040.4.3");
		mapOIDs.put(SHA256_RSA, PKCSObjectIdentifiers.sha256WithRSAEncryption.getId());
		mapOIDs.put(SHA384_RSA, PKCSObjectIdentifiers.sha384WithRSAEncryption.getId());
		mapOIDs.put(SHA512_RSA, PKCSObjectIdentifiers.sha512WithRSAEncryption.getId());
		
		mapReverseOIDs.put("1.2.840.113549.1.1.2", MD2_RSA);
		mapReverseOIDs.put("1.2.840.113549.1.1.4", MD5_RSA);
		mapReverseOIDs.put("1.2.840.113549.1.1.5", SHA1_RSA);
		mapReverseOIDs.put("1.2.840.10040.4.3", SHA1_DSA);
		mapReverseOIDs.put(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId(), SHA256_RSA);
		mapReverseOIDs.put(PKCSObjectIdentifiers.sha384WithRSAEncryption.getId(), SHA384_RSA);
		mapReverseOIDs.put(PKCSObjectIdentifiers.sha512WithRSAEncryption.getId(), SHA512_RSA);
	}
	
	/**
	 * Devuelve el algoritmo por defecto para firmar (m�s utilizado)
	 * 
	 * @return SHA1_RSA
	 */
	public static final String getDefault() {
		return SHA1_RSA;
	}
	
	/**
	 * Devuelve el algoritmo de hashing empleado por el algoritmo de firma
	 * 
	 * @param digitalSignatureAlgorithm Algoritmo de firma
	 * @return Algoritmo de hashing del algoritmo de firma
	 * @throws NoSuchAlgorithmException El algotitmo de firma no existe en Arangi
	 */
	public static final String getHashingAlgorithm (String digitalSignatureAlgorithm) throws NoSuchAlgorithmException {
		
		if (digitalSignatureAlgorithm.equals(MD2_RSA)) {
			return HashingAlgorithm.MD2;
		}
		if (digitalSignatureAlgorithm.equals(MD5_RSA)) {
			return HashingAlgorithm.MD5;
		}
		if (digitalSignatureAlgorithm.equals(SHA1_RSA)) {
			return HashingAlgorithm.SHA1;
		}
		if (digitalSignatureAlgorithm.equals(SHA1_DSA)) {
			return HashingAlgorithm.SHA1;
		}
		if (digitalSignatureAlgorithm.equals(SHA256_RSA)) {
			return HashingAlgorithm.SHA256;
		}
		if (digitalSignatureAlgorithm.equals(SHA384_RSA)) {
			return HashingAlgorithm.SHA384;
		}
		if (digitalSignatureAlgorithm.equals(SHA512_RSA)) {
			return HashingAlgorithm.SHA512;
		}
		
		throw new NoSuchAlgorithmException ("No existe el algoritmo de firma '" + digitalSignatureAlgorithm + "' en Arangi");
	}
	
	/**
	 * Devuelve el algoritmo de cifrado empleado por el algoritmo de firma
	 * 
	 * @param digitalSignatureAlgorithm Algoritmo de firma
	 * @return Algoritmo de cifrado del algoritmo de firma
	 * @throws NoSuchAlgorithmException El algotitmo de firma no existe en Arangi
	 */
	public static final String getCipherAlgorithm (String digitalSignatureAlgorithm) throws NoSuchAlgorithmException {
		
		if (digitalSignatureAlgorithm.equals(MD2_RSA)) {
			return CipherAlgorithm.RSA;
		}
		if (digitalSignatureAlgorithm.equals(MD5_RSA)) {
			return CipherAlgorithm.RSA;
		}
		if (digitalSignatureAlgorithm.equals(SHA1_RSA)) {
			return CipherAlgorithm.RSA;
		}
		if (digitalSignatureAlgorithm.equals(SHA1_DSA)) {
			return CipherAlgorithm.DSA;
		}
		if (digitalSignatureAlgorithm.equals(SHA256_RSA)) {
			return CipherAlgorithm.RSA;
		}
		if (digitalSignatureAlgorithm.equals(SHA384_RSA)) {
			return CipherAlgorithm.RSA;
		}
		if (digitalSignatureAlgorithm.equals(SHA512_RSA)) {
			return CipherAlgorithm.RSA;
		}
		
		throw new NoSuchAlgorithmException ("No existe el algoritmo de firma '" + digitalSignatureAlgorithm + "' en Arangi");
	}
	
	/**
	 * Devuelve el OID del algoritmo pasado como par�metro. Este algoritmo debe ser uno de
	 * los definidos como constantes en esta clase.
	 * 
	 * @param digitalSignatureAlgorithm Nombre del algoritmo
	 * @return OID del algoritmo o null si el algoritmo no existe
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getOID(String digitalSignatureAlgorithm) throws NoSuchAlgorithmException {
		String oid = (String) mapOIDs.get(digitalSignatureAlgorithm);
		if (oid == null) {
			throw new NoSuchAlgorithmException ("El algoritmo de firma '" + digitalSignatureAlgorithm + "' no existe dentro de Arangi");
		}
		
		return oid;
	}
	
	/**
	 * Devuelve el OID del algoritmo por defecto para firma (el m�s utilizado)
	 * 
	 * @return SHA1WithRSA OID
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
