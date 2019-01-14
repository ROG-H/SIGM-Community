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
package es.accv.arangi.base.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;

/**
 * Clase que representa a documentos en el sistema de archivos y en los que su 
 * hash ser� un resumen de todo su contenido. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class FileDocument extends Document {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(FileDocument.class);
	
	/**
	 * Fichero con el contenido del documento
	 */
	private File file;
	
	/**
	 * Inicializaci�n del objeto mediante un fichero
	 * 
	 * @param file Fichero con el contenido del documento
	 * @throws InitDocumentException El fichero es nulo o no existe
	 */
	public FileDocument (File file) throws InitDocumentException {
		logger.debug("[FileDocument]::Entrada::" + file);
		if (file == null) {
			logger.info("[FileDocument]::El fichero es nulo");
			throw new InitDocumentException ("El fichero es nulo");
		}
		if (!file.exists()) {
			logger.info("[FileDocument]::El fichero no existe en el sistema de archivos");
			throw new InitDocumentException ("El fichero no existe en el sistema de archivos");
		}
		
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash()
	 */
	public byte[] getHash() throws HashingException {
		return getHash (HashingAlgorithm.getDefault());
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash(java.lang.String)
	 */
	public byte[] getHash(String hashingAlgorithm) throws HashingException {
		
		logger.debug("[FileDocument.getHash]::Entrada::" + hashingAlgorithm);
		
		//-- Hacer un FileInputStream y utilizar un InputStreamDocument para obtener el hash
		InputStream is = null;
		try {
			is = new FileInputStream (this.file);
			InputStreamDocument isd = new InputStreamDocument (is);
			byte[] hash = isd.getHash(hashingAlgorithm);
			logger.debug ("[FileDocument.getHash]::FIN::Devolviendo: " + hash);
			
			return hash;
			
		} catch (FileNotFoundException e) {
			logger.info("[FileDocument.getHash]::El fichero no existe", e);
			throw new HashingException ("El fichero no existe", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("[FileDocument.getHash]::El fichero no existe", e);
					throw new HashingException ("No es posible cerrar el stream de lectura", e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getInputStream()
	 */
	public InputStream getInputStream() {
		try {
			return new FileInputStream (file);
		} catch (FileNotFoundException e) {
			logger.info("[FileDocument.getInputStream]::No es posible obtener un stream de lectura al fichero", e);
			return null;
		}
	}

}
