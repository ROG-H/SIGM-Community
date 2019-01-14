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
package es.accv.arangi.base.certificate.data;

import java.util.Map;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.validation.ServiceException;
import es.accv.arangi.base.exception.certificate.validation.ServiceNotFoundException;

/**
 * Interfaz que han de cumplir todas las clases que implementen un 
 * servicio de obtenci�n de datos de certificados. Lo m�s com�n ser�n 
 * servicios web para validaci�n de certificados.
 *  
 * @author <a href="mailto:jgutierrez@accv.es">Jos� Manuel Guti�rrez N��ez</a>
 */
public interface CertificateDataService {

	/**
	 * Obtiene los datos de un certificado mediante una llamada a un servicio externo.
	 * 
	 * @param certificate Certificado 
	 * @param extraParams Par�metros extra por si fueran necesarios para 
	 * 	realizar la obtenci�n
	 * @return Map con los valores obtenidos del certificado
	 * @throws ServiceNotFoundException El servicio no se encuentra disponible
	 * @throws ServiceException La llamada al servicio devuelve un error
	 */
	public Map<String,String> getData (Certificate certificate, 
			Map<String,Object> extraParams) throws ServiceNotFoundException, ServiceException;
	
}
