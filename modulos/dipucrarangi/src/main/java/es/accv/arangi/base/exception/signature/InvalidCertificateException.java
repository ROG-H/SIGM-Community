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
package es.accv.arangi.base.exception.signature;

/**
 * Se trata de realizar una firma longeva con un certificado revocado o caducado. 
 * La firma longeva incluye informaci�n de validaci�n, y �sta no puede indicar que 
 * el certificado est� revocado.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class InvalidCertificateException extends es.accv.arangi.base.exception.SignatureException {

	private static final long serialVersionUID = 1L;
	
	private int errorNumber; 

	public InvalidCertificateException(int errorNumber) {
		super();
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(String message, int errorNumber, Throwable cause) {
		super(message, cause);
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(String message, int errorNumber) {
		super(message);
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(Throwable cause, int errorNumber) {
		super(cause);
		this.errorNumber = errorNumber;
	}

	/**
	 * Obtiene el n�mero del error que ocasion� la excepci�n. Las constantes que 
	 * determinan el resultado de una validaci�n se encuentran en
	 * {@link es.accv.arangi.base.util.validation.ValidationResult ValidationResult}
	 * @return n�mero del error
	 */
	public int getErrorNumber() {
		return errorNumber;
	}

}
