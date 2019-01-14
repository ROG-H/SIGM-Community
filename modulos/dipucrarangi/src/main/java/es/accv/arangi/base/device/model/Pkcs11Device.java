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

import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenInfo;
import iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.accv.arangi.base.device.AbstractPkcs11Manager;
import es.accv.arangi.base.device.util.pkcs11.Pkcs11Util;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.util.Util;

/**
 * Clase que mantiene la informaci�n de un dispositivo PKCS#11. Un dispositivo puede
 * enontrarse en tres estados:<br><br>
 * <ul>
 * 	<li>Abierto con PIN: se pueden realizar todas las tareas de lectura y escritura</li>
 * 	<li>Abierto con PUK: s�lo se permiten realizar ciertas tareas. Normalmente un 
 * 	dispositivo se abre con PUK s�lo para desbloquear el PIN.</li>
 * 	<li>Sin abrir: en este estado se puede obtener informaci�n gen�rica del dispositivo,
 * 	como por ejemplo el n�mero de serie, y la lista de certificados que contiene</li>
 * </ul> <br><br>
 * 
 * Aunque se puede programar la inicializaci�n de un objeto de esta clase, lo normal
 * es que se trabaje con objetos creados por el m�todo getConnectedDevices de la clase
 * {@link AbstractPkcs11Manager AbstractPkcs11Manager}. El manager PKCS#11 tambi�n 
 * utiliza esta clase para abrir los dispositivos.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class Pkcs11Device {
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(Pkcs11Device.class);

	private Pkcs11Manufacturer manufacturer;
	private String moduleName;
	private Module module;
	private Token token;
	private TokenInfo tokenInfo;
	private Session session;
	private X509Certificate[] certificates;
	private String[] aliases;
	private Map<String,X509Certificate> mapCertificates;
	
	/**
	 * Constructor que carga los valores del dispositivo para el caso en que el dispositivo
	 * est� abierto.
	 * 
	 * @param token Token IAIK
	 * @throws OpeningDeviceException Excepci�n debida a que no se ha podido abrir una 
	 * 	sesi�n con el token
	 * @throws SearchingException Excepci�n producida mientras se obten�a la lista de
	 * 	certificados del dispositivo
	 * @throws LoadingObjectException Excepci�n obteniendo los campos de informaci�n del
	 * 	dispositivo
	 */
	public Pkcs11Device (boolean isOpened, Pkcs11Manufacturer manufacturer, String moduleName, Module module, Token token, TokenInfo tokenInfo, Session session) {
		this.manufacturer = manufacturer;
		this.moduleName = moduleName;
		this.module = module;
		this.token = token;
		this.tokenInfo = tokenInfo;
		
		//-- Si se ha abierto guardamos la sesi�n. Si no obtenemos la lista de certificados y
		//-- cerramos la sesi�n (ya que no se va a usar para nada m�s)
		if (isOpened) {
			this.session = session;
		} else {
			try {
				loadAliasAndCertificates (session);
				session.closeSession();
			} catch (Throwable e) {
				logger.info("[Pkcs11Manufacturer.open]::Excepci�n durante cierre de dispositivo. Ignorando...", e);
			}
		}
	}
	
	/**
	 * Obtiene el identificador del dispositivo.
	 * 
	 * @return ID del dispositivo
	 */
	public long getId() {
		return token.getTokenID();
	}
	
	/**
	 * Obtiene el nombre del m�dulo con el que se abri� el dispositivo.
	 * 
	 * @return Nombre del m�dulo del dispositivo
	 */
	public String getModuleName() {
		return moduleName;
	}
	
	/**
	 * Obtiene el m�dulo con el que se abri� el dispositivo.
	 * 
	 * @return M�dulo del dispositivo
	 */
	public Module getModule() {
		return module;
	}
	
	/**
	 * Obtiene el token de IAIK.
	 * 
	 * @return Token que representa al dispositivo
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Obtiene la informaci�n del token de IAIK.
	 * 
	 * @return Informaci�n del token que representa al dispositivo
	 */
	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}
	
	/**
	 * Obtiene la sesi�n. Si el dispositivo no est� abierto este valor
	 * ser� nulo.
	 * 
	 * @return Sesi�n en el dispositivo
	 */
	public Session getSession() {
		return session;
	}
	
	/**
	 * Carga una nueva sesi�n
	 * 
	 * @param session Sesi�n en el dispositivo
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	
	/**
	 * Obtiene los certificados contenidos en el dispositivo si
	 * este no est� abierto
	 * 
	 * @return Certificados contenidos en el dispositivo
	 */
	public X509Certificate[] getCertificates() {
		return certificates;
	}
	
	/**
	 * Obtiene un certificado a partir de su alias
	 * 
	 * @param alias Alias
	 * @return Certificado contenido en el alias
	 */
	public X509Certificate getCertificate (String alias) {
		return mapCertificates.get(alias);
	}

	/**
	 * Obtiene el nombre de los alias, el orden coincide con el
	 * de los certificados devueltos por {@link #getCertificates()}
	 * 
	 * @return Lista de alias
	 */
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Obtiene la etiqueta del dispositivo, normalmente relacionada con el m�dulo
	 * con el que se abri�.
	 * 
	 * @return Etiqueta del dispositivo
	 */
	public String getLabel() {
		return tokenInfo.getLabel().trim();
	}
	
	/**
	 * Obtiene el tama�o de la memoria de la tarjeta.
	 * 
	 * @return Tama�o de la memoria de la tarjeta
	 */
	public long getTotalMemory () {
		return tokenInfo.getTotalPublicMemory();
	}
	
	/**
	 * Obtiene el espacio libre de la tarjeta
	 * 
	 * @return Espacio libre en la tarjeta
	 */
	public long getFreeMemory () {
		return tokenInfo.getFreePublicMemory();
	}
	
	/**
	 * Obtiene el fabricante del dispositivo que ha inicializado esta clase
	 * 
	 * @return Fabricante del dispositivo
	 */
	public Pkcs11Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	/**
	 * Obtiene el ID del fabricante del dispositivo tal y como aparece registrado
	 * en el dispositivo
	 * 
	 * @return ID del fabricante del dispositivo tal y como aparece registrado
	 * en el dispositivo
	 */
	public String getManufacturerId() {
		return tokenInfo.getManufacturerID().trim();
	}
	
	/**
	 * Obtiene el modelo del dispositivo
	 * 
	 * @return Modelo del dispostivo
	 */
	public String getModel() {
		return tokenInfo.getModel().trim();
	}
	
	/**
	 * Obtiene el n�mero de serie del dispositivo
	 * 
	 * @return N�mero de serie del dispositivo
	 */
	public String getSerialNumber() {
		return tokenInfo.getSerialNumber().trim();
	}
	
	/**
	 * Obtiene la versi�n del firmware
	 * 
	 * @return Versi�n del firmware
	 */
	public String getFirmwareVersion() {
		return tokenInfo.getFirmwareVersion().getMajor() + "." + tokenInfo.getFirmwareVersion().getMinor();
	}
	
	/*
	 * Carga la lista completa de certificados y la lista de alias que existen dentro del dispositivo.
	 * 
	 * @throws SearchingException Error leyendo el dispositivo
	 */
	private void loadAliasAndCertificates(Session session) throws SearchingException {
		//-- Obtener la lista de todos los certificados
		mapCertificates = new HashMap<String, X509Certificate>();
		try {
			X509PublicKeyCertificate template = new X509PublicKeyCertificate();
			List result = Pkcs11Util.findAllObjects(session, template);
			List<X509Certificate> lX509Certificates = new ArrayList<X509Certificate> ();
			List<String> lAliases = new ArrayList<String> ();
			for (int f=0;f<result.size();f++) {
				X509PublicKeyCertificate iaikCert = (X509PublicKeyCertificate)result.get(f);
				X509Certificate certificate = Util.getCertificate(iaikCert.getValue().getByteArrayValue());
				String alias = iaikCert.getLabel().toString();
				
				lX509Certificates.add (certificate);
				lAliases.add(alias);
				mapCertificates.put(alias, certificate);
			}
			this.certificates =  lX509Certificates.toArray(new X509Certificate[0]);
			this.aliases =  lAliases.toArray(new String[0]);
		} catch (Exception e) {
			throw new SearchingException ("No ha sido posible obtener la lista de certificados del dispositivo", e);
		}

	}

}
