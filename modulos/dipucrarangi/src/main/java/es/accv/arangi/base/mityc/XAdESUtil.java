/**
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

import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.signature.AlgorithmNotSuitableException;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.ts.ConstantesTSA;

/**
 * Utilidades XAdES
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 *
 */
public class XAdESUtil {

    /*
     * Pasar del algoritmo de Arang� al algoritmo que necesita MITyC
     */
	public static String getXAdESDigitalSignatureAlgorithm(String digitalSignatureAlgorithm) throws AlgorithmNotSuitableException {
		if (digitalSignatureAlgorithm == null) {
			digitalSignatureAlgorithm = DigitalSignatureAlgorithm.getDefault();
		}
		if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA1_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA1;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA256_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA256;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA384_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA384;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA512_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA512;
		} 
		
		//-- Algoritmo no definido para firma XAdES
		throw new AlgorithmNotSuitableException ("El algoritmo " + digitalSignatureAlgorithm + " no est� soportado para firma XAdES en Arang�");
	}

    /*
     * Pasar del algoritmo de Arang� al algoritmo que necesita MITyC
     */
	public static String getXAdESHashingAlgorithm(String tsaHashingAlgorithm) throws AlgorithmNotSuitableException {
		if (tsaHashingAlgorithm == null) {
			tsaHashingAlgorithm = HashingAlgorithm.getDefault();
		}
		if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA1)) {
			return ConstantesTSA.SHA1;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA256)) {
			return ConstantesTSA.SHA256;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA384)) {
			return ConstantesTSA.SHA384;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA512)) {
			return ConstantesTSA.SHA512;
		} 
		
		//-- Algoritmo no definido para firma XAdES
		throw new AlgorithmNotSuitableException ("El algoritmo " + tsaHashingAlgorithm + " no est� soportado para hashing en Arang�");
	}


}
