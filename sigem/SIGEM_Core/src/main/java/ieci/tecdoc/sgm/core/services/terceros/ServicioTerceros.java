package ieci.tecdoc.sgm.core.services.terceros;

import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionElectronica;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;

import java.util.List;


/**
 * Interfaz para el servicio de Terceros.  
 * 
 */
public interface ServicioTerceros {

	/**
	 * Obtiene una lista de terceros en funci�n del c�digo de identificaci�n.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param code C�digo de identificaci�n del tercero
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public List lookup(String entityId, String code) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en funci�n del c�digos de identificaci�n.
	 * @param entityId Identificador de la entidad administrativa. 
	 * @param code C�digo de identificaci�n del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Lista de terceros ({@link Tercero}. 
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public List lookup(String entityId, String code, boolean defaultValues) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en funci�n del nombre y dos apellidos.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param name Nombre del tercero
	 * @param surnam1 Primer apellido del tercero
	 * @param surname2 Segundo apellido del tercero
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public List lookup(String entityId, String name, String surname1, 
			String surname2) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en funci�n del nombre y dos apellidos.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param name Nombre del tercero
	 * @param surnam1 Primer apellido del tercero
	 * @param surname2 Segundo apellido del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public List lookup(String entityId, String name, String surname1, 
			String surname2, boolean onlyDefaultValues) throws TercerosException;

	/**
	 * Obtiene un tercero a partir de su identificador interno.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id Identificador interno del tercero.
	 * @return Informaci�n del tercero.
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public Tercero lookupById(String entityId, String id) throws TercerosException;
	
	/**
	 * Obtiene un tercero a partir de su identificador.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Informaci�n del tercero con sus direcciones por defecto si as� se indica 
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public Tercero lookupById(String entityId, String id, boolean onlyDefaultValues) 
			throws TercerosException;

	/**
	 * Obtiene un tercero a partir de su identificador interno.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param postalAddressId Identificador de la direcci�n postal.
	 * @param electronicAddressId Identificador de la direcci�n postal.
	 * @return Informaci�n del tercero.
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public Tercero lookupById(String entityId, String id, String postalAddressId, 
			String electronicAddressId) throws TercerosException;

	/**
     * Obtiene una colecci�n de todas las direcciones postales para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return lista de direcciones de postales relacionadas con un tercero
     * @exception TercerosException si ocurre alg�n error.
     */
	public DireccionPostal[] lookupPostalAddresses(String entityId, String id) throws TercerosException;
	
    /**
     * Obtiene la direcci�n postal por defecto para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return direcci�n postal por defecto
     * @exception TercerosException si ocurre alg�n error.
     */
	public DireccionPostal lookupDefaultPostalAddress(String entityId, String id) 
			throws TercerosException;

    /**
     * Obtiene una colecci�n de todas las direcciones electr�nicas para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return colecci�n de direcciones electr�nicas
     * @exception TercerosException si ocurre alg�n error.
     */
	public DireccionElectronica[] lookupElectronicAddresses(String entityId, String id) 
			throws TercerosException;

    /**
     * Obtiene la direcci�n electr�nica por defecto para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return direcci�n electr�nica
     * @exception TercerosException si ocurre alg�n error.
     */
	public DireccionElectronica lookupDefaultElectronicAddress(String entityId, String id) 
			throws TercerosException;

	/**
	 * Obtiene una direcci�n postal seg�n su identificador
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador de direcci�n postal
	 * @return direcci�n postal
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public DireccionPostal getPostalAddress(String entityId, String id) throws TercerosException;

	/**
	 * Obtiene una direcci�n electr�nica seg�n su identificador
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador de direcci�n electr�nica
	 * @return direcci�n electr�nica
	 * @exception TercerosException si ocurre alg�n error.
	 */
	public DireccionElectronica getElectronicAddress(String entityId, String id) throws TercerosException;

	/**
	 * [eCenpri-Felipe #477]
	 * Inserta un nuevo tercero en la BBDD
	 * @param entityId
	 * @param nif
	 * @param tipoDoc
	 * @param nombre
	 * @param ape1
	 * @param ape2
	 * @param tipo
	 * @param provincia
	 * @param municipio
	 * @param cpostal
	 * @param direccion
	 * @param tfnoFijo
	 * @param tfnoMovil
	 * @param email
	 * @return
	 * @throws TercerosException
	 */
    public boolean insertThirdParty(String entityId, String nif, int tipoDoc, String nombre, String ape1, String ape2,
    		String tipo, String provincia, String municipio, String cpostal, String direccion,
    		String tfnoFijo, String tfnoMovil, String email) throws TercerosException;
    
    /**
	 * [dipucr-Felipe #583]
	 * @param idPerson
	 * @param email
	 * @return
	 * @throws ISPACException 
	 */
	public boolean insertDefaultEmail(String entityId, int idPerson, String email) throws TercerosException;
    
	/**
	 * [eCenpri-Felipe #592] Actualiza los datos del terceros en la base de datos
	 * @param idPerson
	 * @param nombre
	 * @param ape1
	 * @param ape2
	 * @param provincia
	 * @param municipio
	 * @param cpostal
	 * @param direccion
	 * @return
	 * @throws ISPACException
	 */
	public boolean updateThirdParty(String entityId, int idPerson, String nombre, String ape1, String ape2, 
			String provincia, String municipio, String cpostal, String direccion) throws TercerosException;
    
	/**
	 * Establece el origen de datos.
	 * @param dsName Origen de datos.
	 */
	public void setDsName(String dsName);
}
