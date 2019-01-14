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

import es.accv.arangi.base.exception.DocumentException;

/**
 * Excepcion cuando se trata de cofirmar un documento con una firma que no se corresponde
 * con dicho documento.
 * 
 * @author <a href="mailto:nmari@accv.es">N�ria Empar Mar� Aguilar</a>
 */
public class NoCoincidentDocumentException extends DocumentException {

	private static final long serialVersionUID = 2757900829219192666L;
	
	public NoCoincidentDocumentException() {
		super();
	}

	public NoCoincidentDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCoincidentDocumentException(String message) {
		super(message);
	}

	public NoCoincidentDocumentException(Throwable cause) {
		super(cause);
	}


	
}
