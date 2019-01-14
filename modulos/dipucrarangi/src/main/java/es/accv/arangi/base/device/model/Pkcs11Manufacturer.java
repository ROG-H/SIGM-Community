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
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.TokenInfo;
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;
import iaik.pkcs.pkcs11.wrapper.PKCS11Exception;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.device.AbstractPkcs11Manager;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.IAIKDLLNotFoundException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.IncorrectPUKException;
import es.accv.arangi.base.exception.device.LockedPINException;
import es.accv.arangi.base.exception.device.ModuleNotFoundException;
import es.accv.arangi.base.exception.device.NoSuitableDriversException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;

/**
 * Clase que representa a un fabricante PKCS#11. Lo normal es que cada fabricante
 * distribuya �nicamente un m�dulo (DLL) para tratar con sus dispositivos. En ese caso
 * basta con inicializar esta clase con el nombre de dicho m�dulo y llamar a los 
 * m�todos que obtienen objetos {@link Pkcs11Device Pkcs11Device}.<br><br>
 * 
 * Para casos m�s especiales, como cuando un fabricante distribuye varios modelos de
 * dispositivos con sus m�dulos correspondientes, se puede extender esta clase y 
 * reescribir en su caso los m�todos que puedan verse afectados.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public abstract class Pkcs11Manufacturer extends ArangiObject {

	/**
	 * Logger de la clase
	 */
	private static Logger logger = Logger.getLogger(Pkcs11Manufacturer.class);

	/**
	 * Nombre de la librer�a PKCS#11
	 */
	protected String pkcs11Lib;
	
	/**
	 * Path a las librer�as PKCS#11 del fabricante (en el caso de Siemens son 2 - una por versi�n -)
	 */
	protected Map<String,String> pkcs11LibPaths;
	
	/**
	 * Path a la librer�a PKCS#11. En el caso de Siemens este valor se carga al crear la instancia
	 */
	protected String pkcs11LibPath;
	
	/**
	 * Nombre del fabricante
	 */
	protected String manufacturerName;
	
	/**
	 * Fichero DLL de IAIK
	 */
	protected File iaikDLLFile;

	/**
	 * Inicializa un fabricante de PKCS#11.
	 * 
	 * @param manufacturerName Nombre del fabricante
	 * @param pkcs11Lib Nombre de la librer�a PKCS#11
	 * @throws IAIKDLLNotFoundException No se ha encontrado la DLL de IAIK
	 * @throws NoSuitableDriversException El manufacturer no dispone de drivers para
	 * 	funcionar en el entorno (Java 32 o 64 bits)
	 */
	public Pkcs11Manufacturer (String manufacturerName, String pkcs11Lib) 
			throws IAIKDLLNotFoundException, NoSuitableDriversException  {
		
		//-- Comprobar si funciona para el entorno (32 o 64 bits)
		if (System.getProperty("os.arch") != null && System.getProperty("os.arch").indexOf("64") > -1 &&
				!runInX64()) {
			logger.info("El sistema es de 64 bits y el fabricante " + manufacturerName + " no dispone de drivers");
			throw new NoSuitableDriversException("El sistema es de 64 bits y el fabricante " + manufacturerName + " no dispone de drivers");
		}
		if ((System.getProperty("os.arch") == null || System.getProperty("os.arch").indexOf("64") == -1) &&
				!runInX86()) {
			logger.info("El sistema es de 32 bits y el fabricante " + manufacturerName + " no dispone de drivers");
			throw new NoSuitableDriversException("El sistema es de 32 bits y el fabricante " + manufacturerName + " no dispone de drivers");
		}
		
		this.manufacturerName = manufacturerName;
		this.pkcs11Lib = pkcs11Lib;
		this.iaikDLLFile = AbstractPkcs11Manager.loadIAIKDllFile();
		
		//-- Guardar y cargar las librer�as PKCS11 de la tarjeta
		saveAndLoadLibraries ();
	}
	
	/**
	 * Obtiene un objeto dispositivo usando la implementaci�n del PKCS#11 
 	 * del constructor. En caso de que hayan varios dispositivos conectados se 
 	 * elegir� el primero de ellos. Este m�todo se puede usar para el caso 
 	 * m�s habitual: que s�lo exista un dispositivo PKCS#11 conectado.
	 * 
	 * @param pin PIN para abrir el dispositivo
	 * @param isPUK Flag que indica si el pin hay que tratarlo como el PUK del
	 * 	dispositivo
	 * @return Dispositivo abierto con PIN o PUK
	 * @throws OpeningDeviceException No ha sido posible abrir el dispositivo
	 * @throws LockedPINException El PIN del dispositivo est� bloqueado
	 * @throws IncorrectPUKException El PUK es incorrecto
	 * @throws IncorrectPINException  El PIN es incorrecto
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	para tratar el dispositivo
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	liber�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 */
	public Pkcs11Device getInstance (String pin, boolean isPUK) throws DeviceNotFoundException, ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException {
		return getInstance(-1, pin, isPUK);
	}
	
	/**
	 * Obtiene un objeto dispositivo usando la implementaci�n del PKCS#11 
 	 * del constructor. Concretamente obtiene el dispositivo cuyo ID coincide
 	 * con el par�metro "deviceId".
	 * 
	 * @param deviceId ID del dispositivo
	 * @param pin PIN para abrir el dispositivo
	 * @param isPUK Flag que indica si el pin hay que tratarlo como el PUK del
	 * 	dispositivo
	 * @return Dispositivo abierto con PIN o PUK
	 * @throws OpeningDeviceException No ha sido posible abrir el dispositivo
	 * @throws LockedPINException El PIN del dispositivo est� bloqueado
	 * @throws IncorrectPUKException El PUK es incorrecto
	 * @throws IncorrectPINException  El PIN es incorrecto
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	para tratar el dispositivo
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	liber�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 */
	public Pkcs11Device getInstance (long deviceId, String pin, boolean isPUK) throws DeviceNotFoundException, ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException {
		return getInstance(true, deviceId, pin, isPUK);
	}
	
	/**
	 * Obtiene un objeto dispositivo usando la implementaci�n del PKCS#11 
 	 * del constructor. En caso de que hayan varios dispositivos conectados se 
 	 * elegir� el primero de ellos. Este m�todo se puede usar para el caso 
 	 * m�s habitual: que s�lo exista un dispositivo PKCS#11 conectado.<br><br>
 	 * 
 	 * El dispositivo obtenido no est� abierto con PIN ni PUK, por lo que su
 	 * utilizaci�n se limitar� a obtener informaci�n de los certificados que
 	 * almacena e informaci�n general, como su n�mero de serie.
	 * 
	 * @return Dispositivo sin abrir
	 * @throws OpeningDeviceException No ha sido posible obtener una sesi�n en
	 * 	el dispositivo
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	para tratar el dispositivo
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	librer�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 */
	public Pkcs11Device getInstance () throws DeviceNotFoundException, ModuleNotFoundException, OpeningDeviceException  {
		return getInstance(-1);
	}
	
	/**
	 * Obtiene un objeto dispositivo usando la implementaci�n del PKCS#11 
 	 * del constructor. Concretamente obtiene el dispositivo cuyo ID coincide
 	 * con el par�metro "deviceId".<br><br>
	 * 
 	 * El dispositivo obtenido no est� abierto con PIN ni PUK, por lo que su
 	 * utilizaci�n se limitar� a obtener informaci�n de los certificados que
 	 * almacena e informaci�n general, como su n�mero de serie.
	 *
	 * @param deviceId ID del dispositivo
	 * @throws OpeningDeviceException No ha sido posible obtener una sesi�n en
	 * 	el dispositivo
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	para tratar el dispositivo
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	librer�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 */
	public Pkcs11Device getInstance (long deviceId) throws DeviceNotFoundException, ModuleNotFoundException, OpeningDeviceException {
		try {
			return getInstance(false, deviceId, null, false);
		} catch (IncorrectPINException e) {
			//-- No tiene sentido porque no se abre con pin
			return null;
		} catch (IncorrectPUKException e) {
			//-- No tiene sentido porque no se abre con puk
			return null;
		} catch (LockedPINException e) {
			//-- No tiene sentido porque no se abre con pin
			return null;
		}
	}
	
	/**
	 * Abre un nuevo dispositivo abri�ndolo con el PIN (o el PUK).
	 * 
	 * @param device Dispositivo que se desea abrir 
	 * @param pin PIN
	 * @param isPUK Determina si el PIN es realmente el PUK del dispositivo
	 * @return Dispositivo con las mismas caracter�sticas que el pasado como par�metro pero
	 *  con una sesi�n abierta con PIN (o PUK)
	 * @throws OpeningDeviceException No ha sido posible abrir el dispositivo
	 * @throws LockedPINException El PIN del dispositivo est� bloqueado
	 * @throws IncorrectPUKException El PUK es incorrecto
	 * @throws IncorrectPINException  El PIN es incorrecto
	 */
	public Pkcs11Device open (Pkcs11Device device, String pin, boolean isPUK) throws IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException {
		
		logger.debug ("[Pkcs11Manufacturer.open]::Entrada::" + Arrays.asList (new Object[] { device, isPUK }));
		
		//-- Clonar
		Pkcs11Device resultDevice = new Pkcs11Device(true, device.getManufacturer(), device.getModuleName(), device.getModule(), 
				device.getToken(), device.getTokenInfo(), device.getSession());
		
		//-- Abrir una nueva sesi�n
		resultDevice.setSession (open (device.getToken(), pin, isPUK));
		
		//-- Devolver el dispositivo
		logger.debug ("[Pkcs11Manufacturer.open]::Se ha abierto el dispositivo");
		return resultDevice;
	}
	
	/**
	 * M�todo que obtiene informaci�n de los dispositivos conectados para el
	 * fabricante
	 * 
	 * @return Lista de objetos de tipo Pkcs11Device
	 * @throws ModuleNotFoundException No se puede cargar el m�dulo
	 * @throws DeviceNotFoundException No se ha obtenido ning�n dispositivo para el m�dulo
	 * @throws OpeningDeviceException No se puede obtener una sesi�n en el dispositivo
	 */
	public List<Pkcs11Device> getConnectedDevices () throws ModuleNotFoundException, DeviceNotFoundException, OpeningDeviceException  {
		
		logger.debug("[Pkcs11Manufacturer.getConnectedDevices]::Entrada::" + this.pkcs11Lib);
		
		//-- Buscar elementos para cada liber�a
		List<Pkcs11Device> lDevices = new ArrayList<Pkcs11Device>();
		//-- Obtener el m�dulo
		Module module = null;
		try {
			module = Module.getInstance(this.pkcs11Lib, iaikDLLFile.getAbsolutePath());
		} catch (IOException e) {
			logger.debug ("[Pkcs11Manufacturer.getConnectedDevices]::No ha sido posible cargar el m�dulo '" + this.pkcs11Lib + "'::" + e.getMessage());
			throw new ModuleNotFoundException ("El m�dulo '" + pkcs11Lib + "' no puede ser cargado.", e);
		} catch (Throwable e) {
			logger.debug ("[Pkcs11Manufacturer.getConnectedDevices]::No ha sido posible cargar el m�dulo '" + this.pkcs11Lib + "'::" + e.getMessage());
			throw new ModuleNotFoundException ("El m�dulo '" + pkcs11Lib + "' no puede ser cargado.", e);
		}
		
		//-- Obtener los tokens para el m�dulo
		Token[] tokens;
		try {
			tokens = getTokens(module, getTreatableManufacturerIds());
		} catch (DeviceNotFoundException e) {
			logger.debug ("[Pkcs11Manufacturer.getConnectedDevices]::No ha sido posible obtener la lista de dispositivos conectados para el m�dulo '" + this.pkcs11Lib + "'::" + e.getMessage());
			throw (e);
		}
		
		//-- Para cada token: abrir sesi�n
		for (int i = 0; i < tokens.length; i++) {
			try {
				lDevices.add (new Pkcs11Device (false, this, this.pkcs11Lib, module, tokens[i], tokens[i].getTokenInfo(), getSession(tokens[i])));
			} catch (TokenException e) {
				// -- no se puede obtener la informaci�n del token
				logger.debug("[Pkcs11Manufacturer.getConnectedDevices]::No se puede obtener informaci�n del token de " + this.pkcs11Lib, e);
			}  catch (OpeningDeviceException e) {
				// -- no se puede abrir la sesi�n en el token
				logger.debug("[Pkcs11Manufacturer.getConnectedDevices]::No se puede abrir la sesi�n en el token de " + this.pkcs11Lib, e);
			}
		}
			
		//-- Devolver tabla
		return lDevices;
			
	}

	/**
	 * Comprueba si el m�dulo del fabricante est� disponible en el equipo
	 * 
	 * @return Cierto si el m�dulo est� presente
	 */
	public boolean isModulePresent () {
		logger.debug("[Pkcs11Manufacturer.isModulePresent]::Entrada");
		//-- Obtener el m�dulo
		try {
			Module.getInstance(this.pkcs11Lib, this.iaikDLLFile.getAbsolutePath());
		} catch (IOException e) {
			//-- El m�dulo no est� presente
			logger.debug("[Pkcs11Manufacturer.isModulePresent]::El m�dulo " + this.pkcs11Lib + " no est� presente");
			return false;
		} 

		//-- El m�dulo est� presente
		logger.debug("[Pkcs11Manufacturer.isModulePresent]::El m�dulo " + this.pkcs11Lib + " est� presente");
		return true;
	}
	
	//-- M�todos abstractos
	
	/**
	 * Lista con los nombres de las librer�as que necesita el
	 * fabricante para funcionar en entornos de 32 bits.
	 */
	protected abstract String[] getX86LibrariesNames();
	
	/**
	 * Lista con los nombres de las librer�as que necesita el
	 * fabricante para funcionar en entornos de 64 bits.
	 */
	protected abstract String[] getX64LibrariesNames();
	
	/**
	 * Lista con los nombres de los recursos que han de dejarse
	 * junto a las librer�as en entornos de 32 bits.
	 */
	protected abstract String[] getX86ResourcesNames();
	
	/**
	 * Lista con los nombres de los recursos que han de dejarse
	 * junto a las librer�as en entornos de 64 bits.
	 */
	protected abstract String[] getX64ResourcesNames();
	
	/**
	 * Obtiene la colecci�n que contiene los nombres de los fabricantes que se tratar�n. 
	 * Se hace porque algunos m�dulos leen varios tipos de tarjeta, y en algunos casos no 
	 * interesa que un cierto m�dulo trate con m�s tipos de tarjeta que aquellos para
	 * los que se va a utilizar. 	 
	 */
	protected abstract Set getTreatableManufacturerIds();
	
	/**
	 * Indica si el manufacturer que extiende esta clase tiene drivers para funcionar
	 * con sistemas operativos y Java de 32 bits.
	 */
	protected abstract boolean runInX86();

	/**
	 * Indica si el manufacturer que extiende esta clase tiene drivers para funcionar
	 * con sistemas operativos y Java de 64 bits.
	 */
	protected abstract boolean runInX64();

	/**
	 * Obtiene el tama�o de los pines de los dispositivos tratados por el fabricante.
	 */
	public abstract int getPinLength ();
	
	/**
	 * Obtiene el tama�o de los puks de los dispositivos tratados por el fabricante.
	 */
	public abstract int getPukLength ();

	//-- GET/SET
	
	/**
	 * Obtiene el nombre de la librer�a que abri� el dispositivo
	 */
	public String getPkcs11Lib() {
		return pkcs11Lib;
	}

	/**
	 * Obtiene los paths a las las librer�as que abren el dispositivo
	 */
	public Map<String,String> getPkcs11LibPaths() {
		if (pkcs11LibPaths == null) {
			pkcs11LibPaths = new HashMap<String, String>();
		}
		return pkcs11LibPaths;
	}

	/**
	 * Obtiene el path a la librer�a que abre el dispositivo
	 */
	public String getPkcs11LibPath() {
		return pkcs11LibPath;
	}

	/**
	 * Obtiene el nombre del fabricante
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}

	//-- M�todos protegidos

	/*
	 * Inicializa un gestor de PKCS#11 usando la implementaci�n del PKCS#11 
 	 * indicado. En caso de que hayan varios dispositivos conectados se 
 	 * elegir� el primero de ellos. Este m�todo se puede usar para el caso 
 	 * m�s habitual: que s�lo exista un dispositivo PKCS#11 conectado.
	 * 
	 * @throws ModuleNotFoundException No se ha encontrado ning�n m�dulo PKCS#11
	 * 	de la lista.
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la liber�a PKCS#11, 
	 * 	no existe un dispositivo para el valor de 'deviceID' o no es posible obtener
	 * 	informaci�n del dispositivo
	 * @throws ModuleNotFoundException El m�dulo no puede ser cargado
	 * @throws OpeningDeviceException 
	 * @throws LockedPINException 
	 * @throws IncorrectPUKException 
	 * @throws IncorrectPINException 
	 */
	protected Pkcs11Device getInstance (boolean openDevice, long deviceId, String pin, boolean isPUK) throws DeviceNotFoundException, ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException {

		logger.debug("[Pkcs11Manufacturer.getInstance]::Inicializando librer�a " + pkcs11Lib);
		
		//-- Obtenemos el m�dulo
		Module module;
		try {
			module = Module.getInstance(pkcs11Lib, this.iaikDLLFile.getAbsolutePath());
		} catch (Throwable e) {
			logger.info ("[Pkcs11Manufacturer.getInstance]:: El m�dulo '" + pkcs11Lib + "' no puede ser cargado.", e);
			throw new ModuleNotFoundException ("El m�dulo '" + pkcs11Lib + "' no puede ser cargado.", e);
		}
		
		//-- Obtenemos el token
		Token token;
		Token[] tokens = getTokens(module, getTreatableManufacturerIds());
		if (deviceId != -1) {
			token = getSelectedToken (tokens, deviceId);
			if (token == null) {
				//-- El tokenID no est� entre los dispositivos conectados
				logger.info ("[Pkcs11Manufacturer.getInstance]:: El dispositivo con ID igual a " + deviceId + " no existe para la librer�a " + pkcs11Lib);
				throw new DeviceNotFoundException ("El dispositivo con ID igual a " + deviceId + " no existe para la librer�a " + pkcs11Lib);
			}
		} else {
			token = tokens[0]; // Si no hubiesen elementos ya habr�a saltado la excepci�n
		}
		
		//-- Obtener informaci�n del token
		TokenInfo tokenInfo;
		try {
			tokenInfo = token.getTokenInfo();
		} catch (Exception e) {
			logger.info ("[Pkcs11Manufacturer.getInstance]:: No es posible obtener informaci�n del dispositivo.", e);
			throw new DeviceNotFoundException ("No es posible obtener informaci�n del dispositivo.", e);
		}
		
		logger.debug("Cargado m�dulo PKCS#11 ``" + pkcs11Lib + "��... OK");
		
		//-- Abrir el dispositivo (con PIN, con PUK o sin nada) y devolver el dispositivo
		Session session;
		if (openDevice) {
			session = open(token, pin, isPUK);
		} else {
			session = getSession (token);
		}

		logger.debug("Abierto dispositivo con m�dulo PKCS#11 ``" + pkcs11Lib + "��... OK");
		
		//-- Obtener el valor del path donde est� el PKCS11
		this.pkcs11LibPath = getPkcs11LibPaths().get(pkcs11Lib);
		
		//-- Devolver el dispositivo
		return new Pkcs11Device(openDevice, this, this.pkcs11Lib, module, token, tokenInfo, session);

	}
	
	
	/**
	 * Devuelve una lista de tokens conectados mediante el m�dulo que se pasa como par�metro
	 * 
	 * @param module M�dulo con el que obtener los tokens
	 * @param treatableManufacturerIds Colecci�n que contiene los nombres de los fabricantes que
	 * 	se tratar�n. Se hace porque algunos m�dulos leen varios tipos de tarjeta, y en algunos
	 * 	casos no interesa que un cierto m�dulo trate con m�s tipos de tarjeta que aquellos para
	 * 	los que se va a utilizar. 
	 * 
	 */
	protected static Token[] getTokens (Module module, Set treatableManufacturerIds) throws DeviceNotFoundException {
		logger.debug("[Pkcs11Manufacturer.getTokens]::Entrada::" + module);
		try {
			//-- Inicializar el m�dulo
			try {
				logger.debug("Inicializando m�dulo...");
				module.initialize(null);
				logger.debug("Inicializando m�dulo...OK");
			} catch (PKCS11Exception e) {
				logger.info("Inicializando m�dulo...ERROR");
				logger.info("Continuamos sin haberlo inicializado (puede que algo/alguien lo est� usando...)");
			}

			//-- Algunos dispositivos como el DNIe no encuentran tokens la primera vez, pero
			//-- si las siguientes
			Slot[] slotsWithToken = module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
				
			//-- Si no hay slots se lanza excepci�n
			if (slotsWithToken == null || slotsWithToken.length == 0) {
				throw new TokenException("No se ha encontrado ninguna tarjeta soportada por el m�dulo criptogr�fico.");
			}
			
			//-- Obtener la lista de tokens
			List lTokens = new ArrayList ();
			for (int i = 0; i < slotsWithToken.length; i++) {
				int retries = 0;
				while (retries < NUM_RETRIES) {
					
					retries++;
					try {
					Token token = slotsWithToken[i].getToken();
					try  {
						token.getTokenInfo();
					} catch (Exception e) {
						//-- El token no puede ser le�do por el m�dulo. Ocurre muchas
						//-- veces con el DNIe, por eso se hace otro reintento
						continue;
					}
					
					//-- Si no se puede leer el n�mero de serie es que algo raro pasa
					if (token.getTokenInfo() == null || token.getTokenInfo().getSerialNumber() == null ||
							token.getTokenInfo().getSerialNumber().trim().equals("")) {
						continue;
					}
					
					//-- Hay drivers que leen demasiadas tarjetas (bit4id). Hay que restringir
					//-- que solo lean los tokens que le pertenecen
					if (treatableManufacturerIds != null && (token.getTokenInfo().getManufacturerID() == null ||
							!treatableManufacturerIds.contains(token.getTokenInfo().getManufacturerID().trim()))) {
						continue;
					}
					
					//-- parece que todo va bien, a�adimos el token
					lTokens.add (token);
					break;
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
			
			//-- Si no hay tokens v�lidos se lanza excepci�n
			if (lTokens.isEmpty()) {
				throw new TokenException("No se ha encontrado ninguna tarjeta soportada por el m�dulo criptogr�fico.");
			}
			
			logger.debug("[Pkcs11Manufacturer.getTokens]::Se han obtenido " + lTokens.size() + " tokens para el m�dulo '" + module + "'" );
			return (Token[]) lTokens.toArray(new Token[0]);
			
		} catch (TokenException e) {
			throw new DeviceNotFoundException("Excepci�n obteniendo la lista de tokens para el m�dulo '" + module + "'", e);
		}
	}
	
	/**
	 * Selecciona el token cuyo id de dispositivo coincide con el pasado como par�metro
	 * 
	 * @param tokens Lista de tokens
	 * @param deviceId ID de dispositivo
	 * @return Token cuyo id de dispositivo coincide con el pasado como par�metro
	 */
	protected Token getSelectedToken(Token[] tokens, long deviceId) {
		
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].getTokenID() == deviceId) {
				return tokens[i];
			}
		}
		
		return null;
	}

	/**
	 * Crea una sesi�n en el dispositivo
	 * 
	 * @return Sesi�n creada
	 * @throws OpeningDeviceException No se puede crear una sesi�n en el dispositivo
	 */
	protected Session getSession(Token token) throws OpeningDeviceException {
		
		//-- Si la sesi�n est� abierta la cerramos
		if (token != null) {
			try {
				logger.debug("Cierre de sesiones previas...");
				token.closeAllSessions();
				logger.debug("Cierre de sesiones previas...OK");
			} catch (TokenException e) {
				logger.info("WARN: No se ha podido cerrar las sesiones previas");
				//-- Glurp!
			}
		}

		//-- Inicializamos la sesion del usuario
		try {
			return token.openSession(Token.SessionType.SERIAL_SESSION, true, null, null);
		} catch (TokenException e) {
			throw new OpeningDeviceException("No se puede crear una sesi�n en el dispositivo", e);
		}
	}
	
	/**
	 * Abre el dispositivo con el PIN o el PUK
	 * 
	 * @param pin PIN o PUK
	 * @param isPUK Flag que indica si es el par�metro anterior es
	 * 	el PIN o el PUK
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 */
	protected Session open(Token token, String pin, boolean isPUK) throws IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException {
		if (isPUK){
			logger.debug("Abriendo sesion (PUK)...");
		} else {
			logger.debug("Abriendo sesion (PIN)...");
		}
		logger.debug("isPUK: " + isPUK);

		//-- Crear la sesi�n
		Session session = getSession (token);
		
		try {
			
			//-- El modo de apertura difiere seg�n usemos PIN o PUK  
			if (isPUK) {
				//-- Login de SO (con PUK)
				session.login(Session.UserType.SO, pin.toCharArray());
				logger.debug("Sesi�n abierta (PUK)");
			} else {
				//-- Login de usuario normal (con PIN)
				session.login(Session.UserType.USER, pin.toCharArray());
				logger.debug("Sesi�n abierta (PIN)");
			}
			
		} catch (PKCS11Exception e) {
			long errorCode = e.getErrorCode();
			if (errorCode == PKCS11Constants.CKR_PIN_INCORRECT){
				if (isPUK) {
					throw new IncorrectPUKException("El PUK pasado como par�metro no es capaz de abrir el dispositivo", e);					
				} else {
					throw new IncorrectPINException("El PIN pasado como par�metro no es capaz de abrir el dispositivo", e);
				}
			} else if (errorCode == PKCS11Constants.CKR_PIN_LOCKED){
				throw new LockedPINException("El PIN de este dispositivo est� bloqueado.", e);				
			} else {
				throw new OpeningDeviceException("Error desconocido durante la apertura del dispositivo", e);			
			}
		} catch (TokenException e) {
			if (isPUK) {
				throw new IncorrectPUKException("El PUK pasado como par�metro no es capaz de abrir el dispositivo", e);					
			} else {
				throw new IncorrectPINException("El PIN pasado como par�metro no es capaz de abrir el dispositivo", e);
			}
		} 

		if (isPUK){
			logger.debug("Abriendo sesion (PUK)...OK");
		} else {
			logger.debug("Abriendo sesion (PIN)...OK");
		}
		
		return session;
	}

	/**
	 * Guarda (si todav�a no lo est�n) la lista de librer�as para trabajar con este 
	 * fabricante en la carpeta de Arang�. Una vez en disco, se cargan en el System
	 * para luego poder trabajar con ellas.
	 */
	protected void saveAndLoadLibraries() {
		logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Entrada");
		
		//-- Obtener la lista de librer�as y recursos
		String[] libraries;
		String[] resources;
		if (System.getProperty("os.arch") != null && System.getProperty("os.arch").indexOf("64") > -1) {
			libraries = getX64LibrariesNames ();
			resources = getX64ResourcesNames ();
		} else {
			libraries = getX86LibrariesNames ();
			resources = getX86ResourcesNames ();
		}
		
		//-- Guardar y cargar las librer�as
		Map<String,String> libreriasCargadas = saveAndLoadLibrariesAndResources(libraries, resources);
		
		//-- Si la librer�a es el PKCS#11 nos guardamos el path (se necesitar� para abrir el
		//-- proveedor de SUN)
		List<String> pkcs11Libraries = getPkcs11Libraries();
		for (Iterator iterator = pkcs11Libraries.iterator(); iterator.hasNext();) {
			String pkcs11Library = (String) iterator.next();
			if (libreriasCargadas.containsKey(pkcs11Library)) {
				getPkcs11LibPaths().put(pkcs11Library, libreriasCargadas.get(pkcs11Library));
			}
			
		}
		
		//-- Si no se ha cargado la librer�a "al vuelo" suponemos que est� en system32
		for (Iterator iterator = getPkcs11Libraries().iterator(); iterator.hasNext();) {
			String pkcs11Library = (String) iterator.next();
			if (getPkcs11LibPaths().get(pkcs11Library) == null) {
				String system32FolderPath = "c:/windows/system32";
				if (System.getenv("windir") != null) {
					system32FolderPath = new File (new File (System.getenv("windir")), "system32").getAbsolutePath();
				}
				
				getPkcs11LibPaths().put(pkcs11Library, new File (new File (system32FolderPath), pkcs11Library).getAbsolutePath());
			}
			
		}
		
	}

	/**
	 * Lista que contiene las librer�as PKCS#11 (normalmente s�lo ser� una
	 * que coincide con la que se pasa en el constructor)
	 */
	protected List<String> getPkcs11Libraries() {
		List<String> lPkcs11Libraries = new ArrayList<String>();
		lPkcs11Libraries.add(this.pkcs11Lib);
		
		return lPkcs11Libraries;
	}


}
