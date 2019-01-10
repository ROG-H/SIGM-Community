package ieci.tdw.ispac.ispaclib.sicres.vo;

import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;

import java.util.Calendar;

/**
 * Informaci�n sobre un organismo.
 */
public class Organization {

	/* Identificador del organismo.
     */
	private String id;
	
	/* C�digo del organismo.
     */
	private String code;
	
	/* Nombre del organismo.
     */
    private String name;
    
	/* Acr�nimo del organismo.
     */    
    private String acronym;
    
    /* Fecha de alta.
     */
    private Calendar creationDate;
    
    /* Fecha de baja.
     */
    private Calendar disabledDate;
    
    private String cif;
    
    private IPostalAddressAdapter direccionPostal;
    
    private IElectronicAddressAdapter direccionElectronica;

    public Organization() {
    }

    public Organization(String id,
    					String code,
    				    String name,
    				    String acronym,
    				    Calendar creationDate,
    				    Calendar disabledDate) {
    	
    	this.id = id;
        this.code = code;
        this.name = name;
        this.acronym = acronym;
        this.creationDate = creationDate;
        this.disabledDate = disabledDate;
    }

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the acronym.
	 */
	public String getAcronym() {
		return acronym;
	}
	/**
	 * @param acronym The acronym to set.
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the creationDate.
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return Returns the disabledDate.
	 */
	public Calendar getDisabledDate() {
		return disabledDate;
	}
	/**
	 * @param disabledDate The disabledDate to set.
	 */
	public void setDisabledDate(Calendar disabledDate) {
		this.disabledDate = disabledDate;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public IPostalAddressAdapter getDireccionPostal() {
		return direccionPostal;
	}

	public void setDireccionPostal(IPostalAddressAdapter direccionPostal) {
		this.direccionPostal = direccionPostal;
	}

	public IElectronicAddressAdapter getDireccionElectronica() {
		return direccionElectronica;
	}

	public void setDireccionElectronica( IElectronicAddressAdapter direccionElectronica) {
		this.direccionElectronica = direccionElectronica;
	}
}