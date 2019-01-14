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
package es.accv.arangi.base.exception.device;

import es.accv.arangi.base.exception.DeviceException;

/**
 * La DLL de IAIK que permite trabajar con dispositivos PKCS#11 no se encuentra
 * en el path del equipo y es imposible guardarla en la carpeta temporal de
 * Arangi para ser utilizada desde all�.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class IAIKDLLNotFoundException extends DeviceException {

	private static final long serialVersionUID = 1L;

	public IAIKDLLNotFoundException() {
		super();
	}

	public IAIKDLLNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IAIKDLLNotFoundException(String message) {
		super(message);
	}

	public IAIKDLLNotFoundException(Throwable cause) {
		super(cause);
	}

}
