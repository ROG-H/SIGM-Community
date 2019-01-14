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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;

import es.accv.arangi.base.document.URLDocument;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.util.Util;
import es.mityc.javasign.xml.resolvers.IResourceData;
import es.mityc.javasign.xml.resolvers.ResourceDataException;

/**
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 *
 */
public class URLResourceData implements IResourceData {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(URLResourceData.class);
	
	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#canAccess(java.lang.String, java.lang.String)
	 */
	public boolean canAccess(String name, String baseURI) {
		if (name != null && (name.startsWith("http://") || name.startsWith("https://"))) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#getAccess(java.lang.String, java.lang.String)
	 */
	public Object getAccess(String name, String baseURI)
			throws ResourceDataException {
		
		logger.debug("[URLResourceData.getAccess]::Entrada::" + Arrays.asList(new Object[] { name, baseURI }));
		
		//-- Obtener la URL
		URL url;
		try {
			url = new URL(name);
		} catch (MalformedURLException e1) {
			logger.debug("[URLResourceData.getAccess]::La URL '" + name + "' no existe");
			return null;
		}
		
		//-- Obtener un objeto document y de �l obtener el hash
		URLDocument urlDocument;
		try {
			urlDocument = new URLDocument(url);
			return Util.readStream(new BufferedInputStream(urlDocument.getInputStream()));
		} catch (InitDocumentException e) {
			logger.debug("[URLResourceData.getAccess]::No es posible acceder a la URL: " + url, e);
			throw new ResourceDataException("No es posible acceder a la URL: " + url, e);
		} catch (IOException e) {
			logger.debug("[URLResourceData.getAccess]::No es posible leer la URL: " + url, e);
			throw new ResourceDataException("No es posible leer la URL: " + url, e);
		} 
	}


}
