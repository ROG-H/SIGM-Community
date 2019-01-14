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

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.document.HashingException;

/**
 * Documento en el que su hash ser� un resumen de todo su contenido.<br><br>
 * 
 * Se inicializa con un stream de lectura, lo que supone que si se obtiene su 
 * hash el objeto queda inhabilitado para ser utilizado una segunda vez. La 
 * ventaja de utilizar este objeto frente a {@link ByteArrayDocument} se debe a 
 * que el contenido del documento no se situar� en memoria (salvo que el stream 
 * con el que se inicializa apunte ya a un objeto en memoria). <br><br>
 * 
 * Si el stream de lectura es un java.io.FileInputStream es mejor utilizar la
 * clase {@link FileDocument FileDocument}, que no carga la memoria y evita el 
 * problema de la reutilizaci�n de los m�todos de esta clase.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class InputStreamDocument extends Document {
	
	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(InputStreamDocument.class);
	
	/*
	 * Stream de lectura
	 */
	protected InputStream is;
	
	/**
	 * Inicializa el objeto pas�ndole un stream de lectura
	 * 
	 * @param is Stream de lectura al contenido del documento
	 */
	public InputStreamDocument (InputStream is) {
		logger.debug("[InputStreamDocument]::Entrada::" + is);
		
		this.is = is;
	}
	

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash()
	 */
	public byte[] getHash() throws HashingException {
		return getHash(HashingAlgorithm.getDefault());
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash(java.lang.String)
	 */
	public byte[] getHash(String hashingAlgorithm) throws HashingException {
		
		logger.debug("[InputStreamDocument.getHash]::Entrada::" + hashingAlgorithm);
		
		//-- Hacer hash por partes mediante el inputStream
		try {
			MessageDigest md = MessageDigest.getInstance(hashingAlgorithm, CRYPTOGRAPHIC_PROVIDER);
			DigestInputStream dis = new DigestInputStream(is, md);
			byte[] buffer = new byte[1024];
			while (true) {
				int n;
				n = dis.read(buffer);
				if (n < 0)
					break;
			}
			dis.on(false);
			
			byte[] hash = dis.getMessageDigest().digest();
			logger.debug ("[InputStreamDocument.getHash]::FIN::Devolviendo: " + hash);

			return hash;
			
		} catch (NoSuchAlgorithmException e) {
			logger.info("[InputStreamDocument.getHash]::No existe en el sistema el algoritmo de hashing '" + hashingAlgorithm + "'", e);
			throw new HashingException ("No existe en el sistema el algoritmo de hashing '" + hashingAlgorithm + "'", e);
		} catch (IOException e) {
			logger.info("[InputStreamDocument.getHash]::Excepci�n leyendo el stream de lectura", e);
			throw new HashingException ("Excepci�n leyendo el stream de lectura", e);
		}
	}

	/**
	 * M�todo que devuelve un stream de lectura al contenido del documento. Si
	 * anteriormente se ha generado el hash de este documento, el stream de
	 * lectura habr� sido le�do, por lo que ser� imposible realizar una segunda
	 * lectura.
	 * 
	 * @return Stream de lectura.
	 */
	public InputStream getInputStream() {
		
		logger.debug("[InputStreamDocument.getInputStream]::Entrada");
		
		return is;
	}

}
