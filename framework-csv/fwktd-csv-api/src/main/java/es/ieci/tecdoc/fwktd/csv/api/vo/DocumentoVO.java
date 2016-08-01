package es.ieci.tecdoc.fwktd.csv.api.vo;

import java.util.Date;

import es.ieci.tecdoc.fwktd.core.model.Entity;

/**
 * Informaci�n de un documento.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class DocumentoVO extends Entity {

	private static final long serialVersionUID = 3673503978664698730L;

	/**
	 * Nombre del documento.
	 */
	private String nombre = null;

	/**
	 * Descripci�n del documento.
	 */
	private String descripcion = null;

	/**
	 * Tipo MIME del documento.
	 */
	private String tipoMIME = null;

	/**
	 * Fecha de creaci�n del documento.
	 */
	private Date fechaCreacion = null;

	/**
	 * Fecha de caducidad del documento.
	 */
	private Date fechaCaducidad = null;

	/**
	 * C�digo Seguro de Verificaci�n.
	 */
	private String csv = null;

	/**
	 * Fecha de generaci�n del CSV.
	 */
	private Date fechaCSV = null;

	/**
	 * Indica la disponibilidad del documento en la aplicaci�n externa.
	 */
	private Boolean disponible = Boolean.TRUE;

	/**
	 * Aplicaci�n externa.
	 */
	private AplicacionVO aplicacion = null;
	
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
	public DocumentoVO() {
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoMIME() {
		return tipoMIME;
	}

	public void setTipoMIME(String tipoMIME) {
		this.tipoMIME = tipoMIME;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public Date getFechaCSV() {
		return fechaCSV;
	}

	public void setFechaCSV(Date fechaCSV) {
		this.fechaCSV = fechaCSV;
	}

	public Boolean isDisponible() {
		return disponible;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	public AplicacionVO getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
}
