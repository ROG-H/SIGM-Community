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
package es.accv.arangi.base.signature.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.errores.InvalidInfoNodeException;

/**
 * Clase que contiene la informaci�n del tag DataObjectFormat de XAdES seg�n
 * el punto 7.2.5 del est�ndar.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class XAdESDataObjectFormat {
	
	private String description;
	private ObjectIdentifier objectIdentifier;
	private String mimeType;
	private URI encoding;
	
	/**
	 * Constructor. Cualquiera de los campos se puede dejar a null si no se quiere
	 * que se incluya.
	 * 
	 * @param description Informaci�n relacionada con los datos firmados
	 * @param objectIdentifier Identificador que indica el tipo de los datos firmados
	 * @param mimeType Tipo MIME de los datos firmados
	 * @param encoding Formato de codificaci�n de los datos firmados
	 */
	public XAdESDataObjectFormat(String description,
			ObjectIdentifier objectIdentifier, String mimeType, URI encoding) {
		super();
		this.description = description;
		this.objectIdentifier = objectIdentifier;
		this.mimeType = mimeType;
		this.encoding = encoding;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ObjectIdentifier getObjectIdentifier() {
		return objectIdentifier;
	}

	public void setObjectIdentifier(ObjectIdentifier objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public URI getEncoding() {
		return encoding;
	}

	public void setEncoding(URI encoding) {
		this.encoding = encoding;
	}
	
}
