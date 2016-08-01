package es.ieci.tecdoc.fwktd.csv.core.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.ieci.tecdoc.fwktd.core.model.BaseValueObject;

/**
 * Informaci�n del documento para generar el CSV.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class InfoDocumentoCSVForm extends BaseValueObject {

	private static final long serialVersionUID = 2625674065935223667L;

	/**
	 * Nombre del documento.
	 */
	private String nombre = null;

	/**
	 * Descripciones del documento en distintos idiomas.
	 */
	private Map<String, String> descripciones = new HashMap<String, String>();

	/**
	 * Tipo MIME del documento.
	 */
	private String tipoMime = null;

	/**
	 * Fecha de creaci�n del documento.
	 */
	private Date fechaCreacion = null;

	/**
	 * Fecha de caducidad del documento.
	 */
	private Date fechaCaducidad = null;

	/**
	 * C�digo de la aplicaci�n que almacena el documento.
	 */
	private String codigoAplicacion = null;

	/**
	 * Indica la disponibilidad del documento en la aplicaci�n externa.
	 */
	private boolean disponible = true;
	
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
	 */
	/**
	 * N�mero de Registro del documento 
	 */
	private String numeroRegistro = null;
	/**
	 * Fecha de Registro del documento
	 */
	private Date fechaRegistro = null;
	/**
	 * Origne del registro
	 */
	private String origenRegistro = null;
	/**
	 * Destino del registro
	 */
	private String destinoRegistro = null;
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
	 */

	/**
	 * Constructor.
	 */
	public InfoDocumentoCSVForm() {
		super();
	}
	
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getOrigenRegistro() {
		return origenRegistro;
	}

	public void setOrigenRegistro(String origenRegistro) {
		this.origenRegistro = origenRegistro;
	}

	public String getDestinoRegistro() {
		return destinoRegistro;
	}

	public void setDestinoRegistro(String destinoRegistro) {
		this.destinoRegistro = destinoRegistro;
	}
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - A�adir campos para registros de salida 
	 */

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoMime() {
		return tipoMime;
	}

	public void setTipoMime(String tipoMime) {
		this.tipoMime = tipoMime;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getCodigoAplicacion() {
		return codigoAplicacion;
	}

	public void setCodigoAplicacion(String codigoAplicacion) {
		this.codigoAplicacion = codigoAplicacion;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public Map<String, String> getDescripciones() {
		return descripciones;
	}

	public void addDescripciones(Map<String, String> descripciones) {
		if (descripciones != null) {
			this.descripciones.putAll(descripciones);
		}
	}

	/**
	 * A�ade la descripci�n por defecto del documento.
	 * @param descripcion Descripci�n del documento.
	 */
	public void addDescripcionPorDefecto(String descripcion) {
		getDescripciones().put("default", descripcion);
	}

	/**
	 * A�ade una descripci�n del documento.
	 * @param locale Locale.
	 * @param descripcion Descripci�n del documento.
	 */
	public void addDescripcion(Locale locale, String descripcion) {
		if (locale != null) {
			getDescripciones().put(locale.toString(), descripcion);
			if (getDescripciones().get("default") == null) {
				getDescripciones().put("default", descripcion);
			}
		} else {
			getDescripciones().put("default", descripcion);
		}
	}

	/**
	 * Obtiene la descripci�n del documento
	 * @param locale Locale.
	 * @return Descripci�n del documento.
	 */
	public String getDescripcion(Locale locale) {

		String descripcion = null;

		if (locale == null) {

			// Obtener la descripci�n marcada por defecto
			descripcion = getDescripciones().get("default");

		} else {

			// Obtener la descripci�n en el locale indicado: idioma_pais_variante
			descripcion = getDescripciones().get(locale.toString());
			if (descripcion == null) {

				// Obtener la descripci�n en el locale: idioma_pais
				descripcion = getDescripciones().get(locale.getLanguage() + "_" + locale.getCountry());
				if (descripcion == null) {

					// Obtener la descripci�n en el locale: idioma
					descripcion = getDescripciones().get(locale.getLanguage());
					if (descripcion == null) {

						// Obtener la descripci�n marcada por defecto
						descripcion = getDescripciones().get("default");
					}
				}
			}
		}

		return descripcion;
	}

}
