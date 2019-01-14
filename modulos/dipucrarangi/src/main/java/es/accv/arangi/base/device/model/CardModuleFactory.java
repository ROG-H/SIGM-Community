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
package es.accv.arangi.base.device.model;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Los distintos modelos de dispositivos registran en esta clase su m�dulo asociado.
 * Accediendo al m�todo getInstance se obtienen estos m�dulos con los que se pueden
 * abrir los dispositivos.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class CardModuleFactory {

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CardModuleFactory.class);
	
	static HashMap hmModules;
	
	/**
	 * Registra un nuevo m�dulo para tratar un tipo de dispositivo
	 * 
	 * @param deviceType Nombre del tipo de dispositivo
	 * @param cardModule M�dulo 
	 */
	public static void register (String deviceType, Pkcs11Manufacturer cardModule) {
		getModules().put (deviceType, cardModule);
	}
	
	/**
	 * Obtiene un m�dulo para tratar con el tipo de dispositivo pasado como
	 * par�metro.
	 * 
	 * @param deviceType Tipo de dispositivo
	 * @return Modulo o null si no existe ning�n m�dulo registrado para el
	 * tipo de dispositivo
	 */
	public static Pkcs11Manufacturer getInstance (String deviceType) {
		return (Pkcs11Manufacturer) getModules().get(deviceType);
	}

	/*
	 * Obtiene el mapa de m�dulos
	 */
	private static HashMap getModules() {
		if (hmModules == null) {
			hmModules = new HashMap ();
		}
		return hmModules;
	}
}
