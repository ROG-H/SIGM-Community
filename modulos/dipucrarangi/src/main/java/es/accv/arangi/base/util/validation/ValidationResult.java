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
package es.accv.arangi.base.util.validation;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;

import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.timestamp.TimeStamp;

/**
 * Clase que representa el resultado de una validaci�n. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class ValidationResult {

	/**
	 * Resultado de la validaci�n: resultado v�lido.
	 */
	public static final int RESULT_VALID									= 0;

	/**
	 * Resultado de la validaci�n: el certificado no pertenece a la lista de CAs de
	 * confianza.
	 */
	public static final int RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS		= 1;
	
	/**
	 * Resultado de la validaci�n: el certificado no ha comenzado su periodo de validez 
	 * o ya ha expirado.
	 */
	public static final int RESULT_CERTIFICATE_NOT_ACTIVE					= 2;

	/**
	 * Resultado de la validaci�n: el certificado est� revocado.
	 */
	public static final int RESULT_CERTIFICATE_REVOKED						= 3;

	/**
	 * Resultado de la validaci�n: el certificado es desconocido.
	 */
	public static final int RESULT_CERTIFICATE_UNKNOWN						= 4;

	/**
	 * Resultado de la validaci�n: el certificado no puede ser validado.
	 */
	public static final int RESULT_CERTIFICATE_CANNOT_BE_VALIDATED			= 5;

	/**
	 * Resultado de la validaci�n: la cadena de confianza del certificado 
	 * no es v�lida.
	 */
	public static final int RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID		= 6;

	/**
	 * Resultado de la validaci�n: los datos no se corresponden con el hash de la firma
	 */
	public static final int RESULT_SIGNATURE_NOT_MATCH_DATA					= 7;
	
	/**
	 * Alguno de los hash de las referencias que luego se firman no se corresponde con
	 * la parte del XML a que hacen referencia. 
	 */
	public static final int RESULT_REFERENCE_DIGEST_NOT_MATCH_DATA			= 8;

	/**
	 * El resultado no es v�lido
	 */
	public static final int RESULT_INVALID									= 9;

	/**
	 * El sello de tiempos no es v�lido
	 */
	public static final int RESULT_INVALID_TIMESTAMP						= 10;

	/**
	 * El item de validaci�n (respuesta OCSP o CRL) no es v�lido: su firma no es correcta
	 * o, en el caso de CRLs, su certificado de firma no pertenece a las autoridades
	 * de certificaci�n de confianza.
	 */
	public static final int RESULT_INVALID_VALIDITY_ITEM 					= 11;

	/**
	 * La fecha de fin de validez del item de validaci�n (respuesta OCSP o CRL) es
	 * anterior a la fecha del sello de tiempos de la firma. 
	 */
	public static final int RESULT_TIMESTAMP_AFTER_VALIDITY_ITEM 			= 12;
	

	/**
	 * Resultado de la validaci�n
	 */
	boolean valid;
	
	/**
	 * Resultado de la validaci�n como valor entero: si el mayor que 0 el resultado de la
	 * validaci�n es false
	 */
	int result;
	
	/**
	 * Texto explicativo (en castellano) del resultado
	 */
	String resultText;
	
	/**
	 * Fecha de la firma
	 */
	Date date;
	
	/**
	 * Certificado de la firma
	 */
	X509Certificate certificate;
	
	/**
	 * Mapa de campos del certificado (se obtendr� s�lo en algunos casos)
	 */
	Map<String,String> certificateFields;
	
	/**
	 * Sello de tiempos de la firma
	 */
	TimeStamp timeStamp;
	
	/**
	 * Respuestas OCSP asociadas al certificado de la firma y su cadena de
	 * confianza
	 */
	OCSPResponse[] ocspResponses;
	
	/**
	 * Validaci�n de contrafirmas
	 */
	ValidationResult[] counterSignatures;

	/**
	 * Textos
	 */
	static String[] textos;
	
	/**
	 * Constructor de la clase para un resultado v�lido
	 * 
	 * @param certificate Certificado con el que se realiz� la firma
	 */
	public ValidationResult(X509Certificate certificate) {
		super();
		this.valid = true;
		this.result = RESULT_VALID;
		this.resultText = getTextos() [RESULT_VALID];
		this.certificate = certificate;
	}

	/**
	 * Constructor de la clase para cualquier tipo de resultado
	 * 
	 * @param result Resultado de la validaci�n como valor entero
	 * @param certificate Certificado con el que se realiz� la firma
	 */
	public ValidationResult(int result, X509Certificate certificate) {
		super();
		this.valid = (result == RESULT_VALID);
		this.result = result;
		this.resultText = getTextos() [result];
		this.certificate = certificate;
	}

	/**
	 * Constructor de la clase para cualquier tipo de resultado con un
	 * texto diferente del est�ndar para ese resultado
	 * 
	 * @param result Resultado de la validaci�n como valor entero
	 * @param resultText Texto del resultado
	 * @param certificate Certificado con el que se realiz� la firma
	 */
	public ValidationResult(int result, String resultText, X509Certificate certificate) {
		super();
		this.valid = (result == RESULT_VALID);
		this.result = result;
		this.resultText = resultText;
		this.certificate = certificate;
	}

	/**
	 * Constructor de la clase para un resultado v�lido
	 * 
	 * @param certificate Certificado con el que se realiz� la firma
	 * @param date Fecha de la firma (si se a�ade un sello de tiempos esta
	 * 	fecha ser� la del sello de tiempos.
	 * @param timeStamp Sello de tiempos de la firma
	 * @param ocspResponses Respuestas OCSP asociadas al certificado de firma
	 * 	y su cadena de certificaci�n.
	 */
	public ValidationResult(X509Certificate certificate, Date date, TimeStamp timeStamp, OCSPResponse[] ocspResponses) {
		super();
		this.valid = true;
		this.result = RESULT_VALID;
		this.resultText = getTextos() [RESULT_VALID];
		this.certificate = certificate;
		this.date = date;
		this.timeStamp = timeStamp;
		if (timeStamp != null) {
			date = timeStamp.getTime();
		}
		this.ocspResponses = ocspResponses;
	}

	/**
	 * Constructor de la clase para cualquier tipo de resultado
	 * 
	 * @param result Resultado de la validaci�n como valor entero
	 * @param certificate Certificado con el que se realiz� la firma
	 * @param date Fecha de la firma (si se a�ade un sello de tiempos esta
	 * 	fecha ser� la del sello de tiempos.
	 * @param timeStamp Sello de tiempos de la firma
	 * @param ocspResponses Respuestas OCSP asociadas al certificado de firma
	 * 	y su cadena de certificaci�n.
	 */
	public ValidationResult(int result, X509Certificate certificate, Date date, TimeStamp timeStamp, OCSPResponse[] ocspResponses) {
		super();
		this.valid = (result == RESULT_VALID);
		this.result = result;
		this.resultText = getTextos() [result];
		this.certificate = certificate;
		this.date = date;
		this.timeStamp = timeStamp;
		if (timeStamp != null) {
			date = timeStamp.getTime();
		}
		this.ocspResponses = ocspResponses;
	}

	/**
	 * Constructor de la clase para cualquier tipo de resultado con un
	 * texto diferente del est�ndar para ese resultado
	 * 
	 * @param result Resultado de la validaci�n como valor entero
	 * @param resultText Texto del resultado
	 * @param certificate Certificado con el que se realiz� la firma
	 * @param date Fecha de la firma (si se a�ade un sello de tiempos esta
	 * 	fecha ser� la del sello de tiempos.
	 * @param timeStamp Sello de tiempos de la firma
	 * @param ocspResponses Respuestas OCSP asociadas al certificado de firma
	 * 	y su cadena de certificaci�n.
	 */
	public ValidationResult(int result, String resultText, X509Certificate certificate, Date date, TimeStamp timeStamp, OCSPResponse[] ocspResponses) {
		super();
		this.valid = (result == RESULT_VALID);
		this.result = result;
		this.resultText = resultText;
		this.certificate = certificate;
		this.date = date;
		this.timeStamp = timeStamp;
		if (timeStamp != null) {
			date = timeStamp.getTime();
		}
		this.ocspResponses = ocspResponses;
	}

	//-- M�todos p�blicos
	
	/**
	 * Completa los campos de esta clase si se han usado los constructores simples.
	 * 
	 * @param date Fecha de la firma (si se a�ade un sello de tiempos esta
	 * 	fecha ser� la del sello de tiempos.
	 * @param timeStamp Sello de tiempos de la firma
	 * @param ocspResponses Respuestas OCSP asociadas al certificado de firma
	 * 	y su cadena de certificaci�n.
	 */
	public void completeValidationResult(Date date, TimeStamp timeStamp, OCSPResponse[] ocspResponses) {
		this.date = date;
		this.timeStamp = timeStamp;
		if (timeStamp != null) {
			date = timeStamp.getTime();
		}
		this.ocspResponses = ocspResponses;
	}


	
	/**
	 * Devuelve la validez de la firma
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Devuelve el resultado de la validaci�n seg�n las constantes
	 * definidas en esta clase.
	 */
	public int getResult() {
		return result;
	}

	/**
	 * Carga el valor resultado
	 */
	public void setResult (int result) {
		this.valid = (result == RESULT_VALID);
		this.result = result;
		this.resultText = getTextos() [result];
	}
	
	/**
	 * Devuelve el resultado de la validaci�n como un texto en castellano.
	 */
	public String getResultText() {
		return resultText;
	}
	
	/**
	 * Devuelve la fecha de la firma (si �sta existe). Si existe un sello de
	 * tiempos ser� la fecha de dicho sello.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Devuelve el certificado que realiz� la firma
	 */
	public X509Certificate getCertificate() {
		return certificate;
	}

	/**
	 * Devuelve el sello de tiempos asociado a la firma o null si �ste no existe.
	 */
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Devuelve las respuestas OCSP asociadas a la firma
	 */
	public OCSPResponse[] getOcspResponses() {
		return ocspResponses;
	}
	
	/**
	 * Devuelve la validaci�n de las contrafirmas
	 */
	public ValidationResult[] getCounterSignatures() {
		return counterSignatures;
	}

	/**
	 * Carga el valor de la validaci�n de las contrafirmas 
	 */
	public void setCounterSignatures(ValidationResult[] counterSignatures) {
		this.counterSignatures = counterSignatures;
	}
	
	/**
	 * Devuelve los campos del certificado (si existen) 
	 */
	public Map<String,String> getCertificateFields () {
		return this.certificateFields;
	}

	/**
	 * Carga los campos del certificado
	 */
	public void setCertificateFields (Map<String,String> certificateFields) {
		this.certificateFields = certificateFields;
	}
	
	/**
	 * Dado un resultado (como entero) devuelve un texto explicativo en espa�ol.
	 * 
	 * @param result Resultado de una validaci�n
	 * @return Texto para el resultado
	 */
	public static String getText (int result) {
		if (result < 0 || result >= getTextos().length) {
			return "";
		}
		
		return getTextos()[result];
	}

	//-- M�todos privados
	
	private static String[] getTextos () {
		if (textos == null) {
			textos = new String [] {
				"Resultado v�lido",
				"El certificado no pertenece a la lista de Autoridades de Certificaci�n de confianza",
				"El certificado no ha comenzado su periodo de validez o ya ha expirado",
				"El certificado est� revocado",
				"El certificado es desconocido para el servidor OCSP que ha de validarlo",
				"El certificado no puede ser validado: no se ha encontrado un servidor OCSP o la direcci�n de una CRL",
				"La cadena de confianza del certificado no es v�lida",
				"Los datos no se corresponden con el hash de la firma",
				"Alguno de los hash de las referencias que luego se firman no se corresponde con la parte del XML a " +
					"que hacen referencia",
				"El resultado no es v�lido",
				"El sello de tiempos no es correcto",
				"El item de validaci�n (respuesta OCSP o CRL) no es v�lido",
				"La fecha de fin de validez del item de validaci�n (respuesta OCSP o CRL) es anterior a la fecha del sello de tiempos de la firma"
			};
		}
		
		return textos;
	}

}
