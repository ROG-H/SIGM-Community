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
package es.accv.arangi.base.exception.certificate.validation;

import es.accv.arangi.base.exception.CertificateException;

/**
 * El objeto que se intenta pasar como respuesta OCSP no lo es o es una respuesta
 * OCSP que no se corresponde a la <a href="http://www.ietf.org/rfc/rfc2560.txt" target="rfc">RFC-2560</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class MalformedOCSPResponseException extends CertificateException {

	public MalformedOCSPResponseException() {
		super();
	}

	public MalformedOCSPResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedOCSPResponseException(String message) {
		super(message);
	}

	public MalformedOCSPResponseException(Throwable cause) {
		super(cause);
	}

}
