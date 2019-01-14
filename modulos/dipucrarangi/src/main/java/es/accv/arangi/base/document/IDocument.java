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

import java.io.InputStream;

import es.accv.arangi.base.exception.document.HashingException;


/**
 * Interfaz que representa a un documento que va a ser firmado.<br><br>
 * Los procesos de firma requieren que el documento pueda resumirse en un hash,
 * que no siempre representa al documento completo: caso de PDF o firma en XML.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public interface IDocument {

	/**
	 * M�todo que obtiene el hash del documento con el algoritmo de hashing
	 * por defecto: SHA-1.
	 * 
	 * @return Resultado de aplicar el algoritmo SHA-1 al contenido del documento.
	 * @throws HashingException Error en el proceso de hashing
	 */
	public byte[] getHash () throws HashingException;
	
	/**
	 * M�todo que obtiene el hash del documento con el algoritmo de hashing
	 * pasado como par�metro.
	 * 
	 * @param hashingAlgorithm Algoritmo de hashing empleado para obtener el hash. Pueden
	 * encontrarse las constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.HashingAlgorithm HashingAlgorithm}
	 * @return Resultado de aplicar el algoritmo indicado al contenido del documento.
	 * @throws HashingException Error en el proceso de hashing
	 */
	public byte[] getHash (String hashingAlgorithm) throws HashingException;
	
	/**
	 * M�todo que devuelve un stream de lectura al contenido del documento.
	 * 
	 * @return Stream de lectura. En alguna situaci�n se puede obtener un valor nulo si
	 * es imposible obtener el stream de lectura.
	 */
	public InputStream getInputStream ();

}
