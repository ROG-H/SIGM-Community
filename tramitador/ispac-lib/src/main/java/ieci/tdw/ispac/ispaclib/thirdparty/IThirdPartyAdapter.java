package ieci.tdw.ispac.ispaclib.thirdparty;

import java.io.Serializable;

/**
 * Interfaz del adaptador para contener la informaci�n de un tercero.
 *
 */
public interface IThirdPartyAdapter extends Serializable {
	
	public static final String ADDRESS_TYPE_POSTAL 		= "P";
	public static final String ADDRESS_TYPE_TELEMATIC	= "T";
	
	public static final String TIPO_PERSONA_FISICA 		= "F";
	public static final String TIPO_PERSONA_JURIDICA 	= "J";

	/**
	 * Obtiene el identificador del tercero en el sistema externo.
	 * @return Identificador del tercero.
	 */
    public String getIdExt();

	/**
	 * Obtiene la identificaci�n del tercero.
	 * @return Identificaci�n del tercero.
	 */
    public String getIdentificacion();

    /**
	 * Obtiene el nombre del tercero.
	 * @return Nombre del tercero.
	 */
	public String getNombre();

    /**
	 * Obtiene el primer apellido del tercero.
	 * @return Primer apellido del tercero.
	 */
    public String getPrimerApellido();
    
    /**
	 * Obtiene el segundo apellido del tercero.
	 * @return Primer apellido del tercero.
	 */
    public String getSegundoApellido();

	/**
	 * Obtiene los apellidos del tercero.
	 * @return Apellidos del tercero.
	 */
    public String getApellidos();
    
	/**
	 * Obtiene el nombre completo del tercero.
	 * @return Nombre completo del tercero.
	 */
    public String getNombreCompleto();
    
	/**
	 * Indica si el tipo de direcci�n de notificaci�n es telem�tica.
	 * @return true si la notificaci�n es telem�tica.
	 */
    public boolean isNotificacionTelematica();

    /**
     * Obtiene el tipo de persona.
     * @return Tipo de persona: (F)�sica o (J)ur�dica.
     */
    public String getTipoPersona();

	/**
	 * Obtiene las direcciones electr�nicas.
	 * @return direcciones electr�nicas asociadas al tercero
	 */
	public IElectronicAddressAdapter[] getDireccionesElectronicas();

    /**
     * Obtiene la direcci�n electr�nica por defecto.
     * @return direcci�n electr�nica por defecto para el tercero
     */
    public IElectronicAddressAdapter getDefaultDireccionElectronica();

    /**
     * Establece la direcci�n electr�nica por defecto.
     * @param direccion direcci�n electr�nica por defecto para el tercero
     */
    public void setDefaultDireccionElectronica(IElectronicAddressAdapter direccion);
    
	/**
	 * Obtiene las direcciones postales.
	 * @return direcciones postales aosicadas al tercero
	 */
	public IPostalAddressAdapter[] getDireccionesPostales();

	/**
	 * Obtiene la direcci�n postal por defecto.
	 * @return direcci�n postal por defecto para el tercero
	 */
	public IPostalAddressAdapter getDefaultDireccionPostal();

    /**
     * Establece la direcci�n postal por defecto.
     * @param direccion direcci�n postal por defecto para el tercero
     */
    public void setDefaultDireccionPostal(IPostalAddressAdapter direccion);
    
    public String getDir3();
	public void setDir3(String dir3);

}