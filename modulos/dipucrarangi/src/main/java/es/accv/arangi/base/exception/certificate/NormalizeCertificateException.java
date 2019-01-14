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
package es.accv.arangi.base.exception.certificate;

import es.accv.arangi.base.exception.CertificateException;


/**
 * Excepciones debidas a errores durante el proceso de normalizar el certificado para
 * poder trabajar con �l desde el proveedor criptogr�fico de Arangi. Los errores pueden 
 * producirse debido a la codificaci�n del certificado o a que el proveedor no est� 
 * correctamente instalado.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class NormalizeCertificateException extends CertificateException {

	private static final long serialVersionUID = 1L;

	public NormalizeCertificateException() {
		super();
	}

	public NormalizeCertificateException(String message, Throwable cause) {
		super(message, cause);
	}

	public NormalizeCertificateException(String message) {
		super(message);
	}

	public NormalizeCertificateException(Throwable cause) {
		super(cause);
	}

}
