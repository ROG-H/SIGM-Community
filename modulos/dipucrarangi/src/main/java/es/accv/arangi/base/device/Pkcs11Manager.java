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
package es.accv.arangi.base.device;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.device.model.Pkcs11Device;
import es.accv.arangi.base.device.model.Pkcs11Manufacturer;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.IAIKDLLNotFoundException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.IncorrectPUKException;
import es.accv.arangi.base.exception.device.InitializeProviderException;
import es.accv.arangi.base.exception.device.LockedPINException;
import es.accv.arangi.base.exception.device.ModuleNotFoundException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.SavingObjectException;
import es.accv.arangi.base.exception.device.UnlockPINException;

/**
 * Clase para tratar almacenes de claves PKCS#11 (en tarjeta inteligente).<br><br>
 * 
 * Ejemplo de uso:<br><br>
 * 
 * <code>
 *  IDocument document = new FileDocument(new File ("/documento.txt"));<br>
 * 	Pkcs11Manager manager = new Pkcs11Manager ("12345678", new String { "siecap11.dll" });<br>
 * 	String aliases = manager.getAliasNamesList();<br>
 * 	for (int i=0;i<aliases.length;i++) {<br>
 *  &nbsp;&nbsp;System.out.println ("Certificate: " + manager.getCertificate(aliases[i]));<br>
 * 	&nbsp;&nbsp;System.out.println ("Firma: " + manager.signDocument(document, aliases[i])); // Firma con la clave privada del alias<br>
 * 	}<br>
 * </code><br>
 * 
 * El acceso a los dispositivos PKCS#11 se realiza mediante m�dulos (DLL de Windows).
 * La inicializaci�n de objetos de esta clase requiere que se indique los paths
 * de los ficheros que contienen los m�dulos que han de ser cargados para trabajar 
 * con las tarjetas. Normalmente estas DLLs se encuentran dentro del path del sistema
 * operativo, por lo que basta con proporcionar los nombres de los ficheros. Si se
 * desea conocer qu� m�dulos de una lista est�n disponibles en un equipo se puede usar
 * el m�todo est�tico {@link #getLoadableManufacturers(Pkcs11Manufacturer[]) getLoadableManufacturers}.<br><br>
 * 
 * Si se dispone de varios lectores y en cada uno de ellos hay una tarjeta del mismo
 * fabricante se estar� en el caso de que un m�dulo controla dos dispositivos PKCS#11.
 * Este caso no ser� lo normal, aunque por si alg�n desarrollador desea contemplarlo, 
 * se han realizado m�todos a tal efecto. En especial, el m�todo est�tico
 * {@link #getConnectedDevices(Pkcs11Manufacturer[]) getConnectedDevices} proporciona una lista de
 * todos los dispositivos conectados por m�dulo.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">Jos� M Guti�rrez</a>
 */
public class Pkcs11Manager extends AbstractPkcs11Manager { 
	
	/** Logger de clase */
	private static Logger logger = Logger.getLogger(Pkcs11Manager.class);
	
	public static final int MAX_NUMBER_CERTIFICATES_ALLOWED = 5;
	
	protected Pkcs11Manager() {
		
	}

	/**
	 * Inicializa un gestor de PKCS#11 realizando previamente un proceso de 
	 * autodetecci�n de la tarjeta insertada el lector. En caso de que
	 * hayan varios dispositivos conectados se elegir� el primero de ellos.
	 * Este m�todo se puede usar para el caso m�s habitual: que s�lo exista
	 * un dispositivo PKCS#11 conectado.
	 * 
	 * @param pin PIN para abrir el dispositivo
	 * @param manufacturers Lista de fabricantes a tratar por Arangi.
	 * @throws ModuleNotFoundException No se ha encontrado ning�n m�dulo PKCS#11
	 * 	de la lista.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(String pin, Pkcs11Manufacturer[] manufacturers) throws ModuleNotFoundException, IncorrectPINException, LockedPINException, OpeningDeviceException, IAIKDLLNotFoundException, InitializeProviderException {
		
		//-- Inicializar
		try {
			initialize(pin, false, manufacturers);
		} catch (IncorrectPUKException e) {
			// No se va a dar porque no se inicializa con PUK
			logger.info("[Pkcs11Manager(pin,manufacturers]::Error por PUK incorrecto pero no se accede por puk", e);
		}
		
	}
	
	/**
	 * Inicializa un gestor de PKCS#11 realizando previamente un proceso de 
	 * autodetecci�n de la tarjeta insertada el lector. En caso de que
	 * hayan varios dispositivos conectados se elegir� el primero de ellos.
	 * Este m�todo se puede usar para el caso m�s habitual: que s�lo exista
	 * un dispositivo PKCS#11 conectado.
	 * 
	 * @param password PIN o PUK para abrir el dispositivo
	 * @param isPUK Determina si el primer par�metro es el PIN o el PUK
	 * @param manufacturers Lista de fabricantes a tratar por Arangi.
	 * @throws ModuleNotFoundException No se ha encontrado ning�n m�dulo PKCS#11
	 * 	de la lista.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(String password, boolean isPUK, Pkcs11Manufacturer[] manufacturers) throws ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, IAIKDLLNotFoundException, InitializeProviderException {

		//-- Inicializar
		initialize(password, isPUK, manufacturers);

	}
	
	/**
	 * Inicializa un gestor de PKCS#11 realizando previamente un proceso de 
	 * autodetecci�n de la tarjeta insertada el lector. En caso de que
	 * hayan varios dispositivos conectados se elegir� el primero de ellos.
	 * Este m�todo se puede usar para el caso m�s habitual: que s�lo exista
	 * un dispositivo PKCS#11 conectado.
	 * 
	 * @param password PIN o PUK para abrir el dispositivo
	 * @param isPUK Determina si el primer par�metro es el PIN o el PUK
	 * @param manufacturers Lista de fabricantes a tratar por Arangi.
	 * @param withKeystore Indica si se quiere cargar el keystore interno de firma.
	 * 	Si se va a utilizar el manager para actualizar el contenido o modificar el
	 *  PIN o el PUK es m�s optimo marcar este par�metro a falso.
	 * @throws ModuleNotFoundException No se ha encontrado ning�n m�dulo PKCS#11
	 * 	de la lista.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(String password, boolean isPUK, Pkcs11Manufacturer[] manufacturers, boolean withKeystore) throws ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, IAIKDLLNotFoundException, InitializeProviderException {

		//-- Inicializar
		initialize(password, isPUK, manufacturers, withKeystore);

	}
	
	/**
	 * Inicializa un gestor de PKCS#11 para el dispositivo pasado como par�metro.
	 * Dicho dispositivo puede ser obtenido tras el proceso de autodetecci�n de
	 * todos los dispositivos PKCS#11 realizado mediante el m�todo est�tico 
	 * {@link #getConnectedDevices(Pkcs11Manufacturer[]) getConnectedDevices}. 
	 * 
	 * @param device Dispositivo elegido para este manager
	 * @param pin PIN para abrir el dispositivo
	 * 
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	adecuado instalado en el equipo.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws DeviceNotFoundException El dispositivo no existe
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(Pkcs11Device device, String pin) throws ModuleNotFoundException, IncorrectPINException, 
		LockedPINException, OpeningDeviceException, DeviceNotFoundException, IAIKDLLNotFoundException, InitializeProviderException {
		
		//-- Abrir el dispositivo
		try {
			initialize(device, pin, false);
		} catch (IncorrectPUKException e) {
			// No se va a dar porque no se abre con PUK
			logger.info("[Pkcs11Manager]::Error extra�o porque no se abre con puk", e);
		}
	}
	
	/**
	 * Inicializa un gestor de PKCS#11 para el dispositivo pasado como par�metro.
	 * Dicho dispositivo puede ser obtenido tras el proceso de autodetecci�n de
	 * todos los dispositivos PKCS#11 realizado mediante el m�todo est�tico 
	 * {@link #getConnectedDevices(Pkcs11Manufacturer[]) getConnectedDevices}. <br><br>
	 * 
	 * Si el dispositivo se abre con el PUK, normalmente el siguiente paso ser�
	 * invocar al m�todo {@link #unlockPIN(String) unlockPIN}, ya que el PUK se
	 * suele usar para desbloquear el PIN de la tarjeta. Otra forma de hacer esto
	 * mismo en un s�lo paso es llamar al m�todo est�tico {@link #unlockPIN(Pkcs11Device,String,String) unlockPIN}.
	 * 
	 * @param device Dispositivo elegido para este manager
	 * @param password PIN o PUK para abrir el dispositivo
	 * @param isPUK Determina si el primer par�metro es el PIN o el PUK
	 * 
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	adecuado instalado en el equipo.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws DeviceNotFoundException El dispositivo no existe
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(Pkcs11Device device, String password, boolean isPUK) throws ModuleNotFoundException, 
		IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, 
		DeviceNotFoundException, IAIKDLLNotFoundException, InitializeProviderException {

		initialize(device, password, isPUK);
	}

	/**
	 * Inicializa un gestor de PKCS#11 para el dispositivo pasado como par�metro.
	 * Dicho dispositivo puede ser obtenido tras el proceso de autodetecci�n de
	 * todos los dispositivos PKCS#11 realizado mediante el m�todo est�tico 
	 * {@link #getConnectedDevices(Pkcs11Manufacturer[]) getConnectedDevices}. <br><br>
	 * 
	 * Si el dispositivo se abre con el PUK, normalmente el siguiente paso ser�
	 * invocar al m�todo {@link #unlockPIN(String) unlockPIN}, ya que el PUK se
	 * suele usar para desbloquear el PIN de la tarjeta. Otra forma de hacer esto
	 * mismo en un s�lo paso es llamar al m�todo est�tico {@link #unlockPIN(Pkcs11Device,String,String) unlockPIN}.
	 * 
	 * @param device Dispositivo elegido para este manager
	 * @param password PIN o PUK para abrir el dispositivo
	 * @param isPUK Determina si el primer par�metro es el PIN o el PUK
	 * @param withKeystore Indica si se quiere cargar el keystore interno de firma.
	 * 	Si se va a utilizar el manager para actualizar el contenido o modificar el
	 *  PIN o el PUK es m�s optimo marcar este par�metro a falso.
	 * 
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	adecuado instalado en el equipo.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws DeviceNotFoundException El dispositivo no existe
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(Pkcs11Device device, String password, boolean isPUK, boolean withKeystore) throws ModuleNotFoundException, 
		IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, 
		DeviceNotFoundException, IAIKDLLNotFoundException, InitializeProviderException {

		initialize(device, password, isPUK, withKeystore);
	}

	/**
	 * Inicializa un gestor de PKCS#11 usando el m�dulo cuyo nombre se 
 	 * indica en el primer par�metro. En caso de que hayan varios dispositivos 
 	 * conectados que trabajen con el mismo m�dulo se elegir� el primero de 
 	 * ellos. Este m�todo se puede usar para el caso m�s habitual: que s�lo 
 	 * exista un dispositivo PKCS#11 conectado.
	 * 
	 * @param manufacturer Fabricante que abrir� el dispositivo
	 * @param pin PIN para abrir el dispositivo
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	liber�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	adecuado instalado en el equipo.
	 * @throws IncorrectPINException El PIN no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(Pkcs11Manufacturer manufacturer, String pin) throws DeviceNotFoundException, ModuleNotFoundException, 
		IncorrectPINException, LockedPINException, OpeningDeviceException, IAIKDLLNotFoundException, InitializeProviderException {
		
		//-- Inicializar
		try {
			initialize(pin, false, manufacturer);
		} catch (IncorrectPUKException e) {
			// No se va a dar porque no se accede con PUK
		}
		
	}
	
	/**
	 * Inicializa un gestor de PKCS#11 usando la implementaci�n del PKCS#11 
 	 * indicado. En caso de que hayan varios dispositivos conectados se 
 	 * elegir� el primero de ellos. Este m�todo se puede usar para el caso 
 	 * m�s habitual: que s�lo exista un dispositivo PKCS#11 conectado.
	 * 
	 * @param manufacturer Fabricante que abrir� el dispositivo
	 * @param password PIN o PUK para abrir el dispositivo
	 * @param isPUK Determina si el primer par�metro es el PIN o el PUK
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	liber�a PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11
	 * 	adecuado instalado en el equipo.
	 * @throws IncorrectPINException El PIN (o el PUK) no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public Pkcs11Manager(Pkcs11Manufacturer manufacturer, String password, boolean isPUK) throws DeviceNotFoundException, 
		ModuleNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, 
		OpeningDeviceException, IAIKDLLNotFoundException, InitializeProviderException {

		//-- Inicializar
		initialize(pin, isPUK, manufacturer);
	}
	
	/**
	 * M�todo que obtiene informaci�n de los dispositivos conectados para todas
	 * las librer�as PKCS#11 definidas en el primer par�metro.
	 * 
	 * @param manufacturers Lista de fabricantes a detectar por Arangi.
	 * @return Lista de objetos {@link es.accv.arangi.base.device.model.Pkcs11Device Pkcs11Device} 
	 * 	con	informaci�n de cada uno de los dispositivos encontrados
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 */
	public static List getConnectedDevices (Pkcs11Manufacturer[] manufacturers) throws IAIKDLLNotFoundException {
		
		logger.debug("[Pkcs11Manager.getConnectedDevices]::Entrada::" + manufacturers);
		
		//-- Llamada al m�todo que obtiene la informaci�n
		return AbstractPkcs11Manager.getConnectedDevices(manufacturers);
		
	}
	
	/**
	 * M�todo est�tico que desbloquea el PIN del dispositivo. Para ello abre el
	 * dispositivo con el PUK y desbloquea el PIN. El nuevo valor del PIN puede
	 * ser el valor anterior del PIN, no es necesario que tenga un valor diferente.
	 * 
	 * @param device Dispositivo
	 * @param puk PUK del dispositivo
	 * @param newPin Nuevo PIN
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la liber�a PKCS#11 
	 * 	o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11 adecuado 
	 * 	instalado en el equipo.
	 * @throws IncorrectPINException El PIN (o el PUK) no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws UnlockPINException No se puede desbloquear el PIN
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 */
	public static void unlockPIN (Pkcs11Device device, String puk, String newPin) throws DeviceNotFoundException, ModuleNotFoundException, 
		IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, UnlockPINException, IAIKDLLNotFoundException, InitializeProviderException {
		
		logger.debug("[Pkcs11Manager.unlockPIN]::Entrada");
		
		Pkcs11Manager manager = new Pkcs11Manager (device, puk, true);
		manager.unlockPIN(newPin);
		manager.close();
	}
	
	/**
	 * Cambia el PUK del dispositivo. Para ello abre el dispositivo con el PUK y 
	 * utiliza el m�todo changePin.
	 * 
	 * @param device Dispositivo
	 * @param puk PUK del dispositivo
	 * @param newPuk Nuevo PUK
	 * @throws ModuleNotFoundException No se ha encontrado el m�dulo PKCS#11 adecuado 
	 * 	instalado en el equipo.
	 * @throws IncorrectPINException El PIN (o el PUK) no es correcto
	 * @throws IncorrectPUKException El PUK no es correcto
	 * @throws LockedPINException El PIN est� bloqueado
	 * @throws OpeningDeviceException Error durante el proceso de apertura
	 * @throws DeviceNotFoundException No existen dispositivos para la liber�a PKCS#11 
	 * 	o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11 de Sun
	 * @throws SavingObjectException Error cambiando el PUK en el dispositivo
	 */
	public static void changePUK (Pkcs11Device device, String puk, String newPuk) throws ModuleNotFoundException, IncorrectPINException, 
		IncorrectPUKException, LockedPINException, OpeningDeviceException, DeviceNotFoundException, IAIKDLLNotFoundException, InitializeProviderException, 
		SavingObjectException  {
	
		logger.debug("[Pkcs11Manager.changePUK]::Entrada");
		
		Pkcs11Manager manager = new Pkcs11Manager (device, puk, true);
		manager.changePin(newPuk);
		manager.close();
	}
	
	/**
	 * Este m�todo trata de cargar e inicializar la lista de m�dulos de los fabricantes de
	 * dispositivos que se pasan como par�metro. Devuelve una lista con los fabricantes 
	 * cuyos m�dulos se encuentran disponibles
	 * 
	 * @param manufacturers Fabricantes de dispositivos detectar por Arangi.
	 * @return Lista de m�dulos disponibles en el equipo
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 */
	public static List getLoadableManufacturers (Pkcs11Manufacturer[] manufacturers) throws IAIKDLLNotFoundException {
		
		logger.debug("[Pkcs11Manager.getLoadableManufacturers]::Entrada::" + manufacturers);
		
		//-- Obtener la lista de m�dulos
		List result = new ArrayList ();
		for (int i = 0; i < manufacturers.length; i++) {
			if (manufacturers[i].isModulePresent()) {
				logger.debug("[Pkcs11Manager.getLoadableManufacturers]::El fabricante " + manufacturers[i].getManufacturerName() + " tiene sus m�dulos instalados");
				result.add (manufacturers[i]);
			} else {
				logger.debug("[Pkcs11Manager.getLoadableManufacturers]::El fabricante " + manufacturers[i].getManufacturerName() + " no tiene sus m�dulos instalados");
			}
		}
		
		//-- Devolver lista de m�dulos que han podido ser cargados
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.AbstractPkcs11Manager#getMaxNumberCertificates()
	 */
	protected int getMaxNumberCertificates() {
		return MAX_NUMBER_CERTIFICATES_ALLOWED;
	}

}
