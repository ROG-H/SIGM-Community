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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import es.mityc.javasign.xml.resolvers.IResourceData;
import es.mityc.javasign.xml.resolvers.ResourceDataException;

/**
 * Clase para poder validar firmas que hacen referencia a ficheros en disco
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class FileResourceData implements IResourceData {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(FileResourceData.class);
	
	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#canAccess(java.lang.String, java.lang.String)
	 */
	public boolean canAccess(String name, String baseURI) {
		if (name != null && name.indexOf("file:/") > -1) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#getAccess(java.lang.String, java.lang.String)
	 */
	public Object getAccess(String name, String baseURI) throws ResourceDataException {
		
		logger.debug("[FileResourceData.getAccess]::Entrada::" + Arrays.asList(new Object[] { name, baseURI }));
		
		//-- Obtener el fichero
		File file = new File(name.substring (name.indexOf("file:/") + 6).replaceAll("%20", " "));
		
		//-- Si el fichero existe devolver un FileInputStream
		if (file.exists()) {
			logger.debug("[FileResourceData.getAccess]::El fichero existe::" + file.getAbsolutePath());
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		
		logger.debug("[FileResourceData.getAccess]::El fichero no existe::" + file.getAbsolutePath());
		return null;
	}


}
