/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versi�n 1.1 o �en cuanto sean aprobadas por laComisi�n Europea� versiones posteriores de la EUPL (la �Licencia�); 
* Solo podr� usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislaci�n aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye �TAL CUAL�, SIN GARANT�AS NI CONDICIONES DE NING�N TIPO, ni expresas ni impl�citas. 
* V�ase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;

/**
 * Bean que guarda los mails de los usuarios que van a recibir mails de distribucion.
 * 
 * @author cmorenog
 */
public class DistributionMail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer idDept = null;
	private String name = null;
	private String email = null;


	/**
	 * Constructor.
	 */
	public DistributionMail() {
	}
	

	/**
	 * Obtiene el valor del par�metro name.
	 * 
	 * @return name valor del campo a obtener.
	 */
	public String getName() {
	    return name;
	}

	/**
	 * Guarda el valor del par�metro name.
	 * 
	 * @param name
	 *            valor del campo a guardar.
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * Obtiene el valor del par�metro idDept.
	 * 
	 * @return idDept valor del campo a obtener.
	 */
	public Integer getIdDept() {
	    return idDept;
	}

	/**
	 * Guarda el valor del par�metro idDept.
	 * 
	 * @param idDept
	 *            valor del campo a guardar.
	 */
	public void setIdDept(Integer idDept) {
	    this.idDept = idDept;
	}


	/**
	 * Obtiene el valor del par�metro email.
	 * 
	 * @return email valor del campo a obtener.
	 */
	public String getEmail() {
	    return email;
	}
	
	/**
	 * Guarda el valor del par�metro email.
	 * 
	 * @param email
	 *            valor del campo a guardar.
	 */
	public void setEmail(String email) {
	    this.email = email;
	}

}